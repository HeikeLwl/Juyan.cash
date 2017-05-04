package jufeng.juyancash.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.gprinter.command.EscCommand;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.DishModel;
import jufeng.juyancash.bean.DishTypeCollectionItemBean;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.BillAccountHistoryEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.dao.PrintCashierEntity;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.dao.TurnoverHistoryEntity;
import jufeng.juyancash.printer.sdk.Command;
import jufeng.juyancash.printer.sdk.PrintPicture;
import jufeng.juyancash.printer.sdk.PrinterCommand;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;
import jufeng.juyancash.util.UsbController;
import zj.com.customize.sdk.Other;


/**
 * Created by Administrator102 on 2016/10/28.
 */

public class USBPrinter {
    private Context mContext;
    private static UsbController usbCtrl = null;
    private static UsbController usbCtrl1 = null;
    private static UsbDevice dev = null;
    private static UsbDevice dev1 = null;
    private String khlWZ = null;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    //连接上usb打印机
                    if (dev1 != null && !(usbCtrl.isHasPermission(dev1))) {
                        usbCtrl1.getPermission(dev1);
                    }
                    break;
                default:
                    if (dev1 != null && !(usbCtrl.isHasPermission(dev1))) {
                        usbCtrl1.getPermission(dev1);
                    }
                    break;
            }
        }
    };

    private final Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    //连接上usb打印机

                    break;
                default:

                    break;
            }
        }
    };

    public USBPrinter(Context context) {
        this.mContext = context;
        usbCtrl = new UsbController(context, mHandler);
        usbCtrl1 = new UsbController(context, mHandler1);
    }

    public void closePort() {
        if (usbCtrl != null) {
            usbCtrl.close();
            usbCtrl = null;
        }
        if (usbCtrl1 != null) {
            usbCtrl1.close();
            usbCtrl1 = null;
        }
        if(mContext != null){
            mContext = null;
        }
    }

    public void openPort() {
        dev = null;
        dev1 = null;
        usbCtrl.close();
        usbCtrl1.close();
        HashMap<String, UsbDevice> map = usbCtrl.getUsbList();
        for (Map.Entry<String, UsbDevice> entity :
                map.entrySet()) {
            try {
                int vid = entity.getValue().getVendorId();
                int pid = entity.getValue().getProductId();
                if ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85) || (vid == 6790 && pid == 30084)
                        || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512) || (vid == 26728 && pid == 256)
                        || (vid == 26728 && pid == 768) || (vid == 26728 && pid == 1024) || (vid == 26728 && pid == 1280)
                        || (vid == 26728 && pid == 1536) || (vid == 7358 && pid == 2)) {
                    if (dev1 == null) {
                        dev1 = entity.getValue();
                    }
                    continue;
                }
                int deviceClass = entity.getValue().getDeviceClass();
                if (deviceClass == 0) {
                    UsbInterface anInterface = entity.getValue().getInterface(0);
                    int interfaceClass = anInterface.getInterfaceClass();
                    if (interfaceClass == 7) {
                        if (dev == null) {
                            dev = entity.getValue();
                        }
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                dev = null;
                dev1 = null;
            }
        }

        if (dev != null && !(usbCtrl.isHasPermission(dev))) {
            usbCtrl.getPermission(dev);
        } else {
            if (dev1 != null && !(usbCtrl1.isHasPermission(dev1))) {
                usbCtrl1.getPermission(dev1);
            }
        }

        PrintCashierEntity printCashierEntity = DBHelper.getInstance(mContext).getPrintCashierEntity();
        if (printCashierEntity != null) {
            khlWZ = printCashierEntity.getPrintDesc();
        } else {
            khlWZ = null;
        }
    }

    public String testLabel(final ArrayList<PrintDishBean> printDishBeenes, final String orderId) {
        try {
            Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dms);
            OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
            // 绘制简体中文
            for (PrintDishBean printDishBean :
                    printDishBeenes) {
                for (int i = 0; i < printDishBean.getDishCount(); i++) {
                    printLabel(b, orderEntity, printDishBean);
                }
            }
            if (b != null && !b.isRecycled()) {
                b.recycle();
                b = null;
            }
            return "success";
        } catch (Exception e) {
            return "failed";
        }
    }

    private void printLabel(Bitmap b, OrderEntity orderEntity, PrintDishBean printDishBean) {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(58, 30); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(2); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        int positionH = 10;
        int positionW = 60;
        tsc.addText(60, positionH, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "NO." + orderEntity.getOrderNumber1());
        tsc.addText(160, positionH, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "￥" + printDishBean.getDiscountMoney());
        if (orderEntity.getOrderType() == 1) {
            //外卖单
            tsc.addText(260, positionH, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "外卖单");
        }
        positionH += 30;
        tsc.addText(60, positionH, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                printDishBean.getDishName() + "（打包）");
        positionH += 30;
        if (printDishBean.getDishConfig() != null) {
            tsc.addText(60, positionH, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "-- " + printDishBean.getDishConfig().substring(1, printDishBean.getDishConfig().length() - 1));
        }
        positionH += 30;
        if (!TextUtils.isEmpty(printDishBean.getDishSelectedMaterialEntities())) {
            tsc.addText(60, positionH, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "++ " + printDishBean.getDishSelectedMaterialEntities());
            positionH += 30;
        }
        if (!TextUtils.isEmpty(printDishBean.getMark())) {
            tsc.addText(60, positionH, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "** " + printDishBean.getMark().replace("`", "、"));
        }
        tsc.addText(60, 160, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "** 请在两小时内饮用完毕！**");
        tsc.addText(60, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"));
        // 绘制图片
        tsc.addBitmap(350, 0, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth() * 2 / 3, b);

//            tsc.addQRCode(250, 80, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, "www.baidu.com");
        // 绘制一维条码
//            tsc.add1DBarcode(20, 250, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        SendDataByte1(bytes);
    }

    private void SendDataByte1(byte[] data) {
        try {
            if (data.length > 0)
                usbCtrl1.sendByte(data, dev1);
            Log.d("USBDEVICE", "DEV1:" + dev1.toString());
        } catch (Exception e) {
            Log.d("USBDEVICE", "SendDataByte1: " + e.toString());
        }
    }

    private void SendDataString1(String data) throws Exception {
        if (data.length() > 0)
            usbCtrl.sendMsg(data, "GBK", dev1);
    }

    public String openQianxiangThread() {
        if (usbCtrl != null && dev != null) {
            usbCtrl.openCashBox(dev);
            return "success";
        } else {
            return null;
        }
    }

    //打印客户联
    public String printKehulian(String orderId, boolean isPrintPresentDish, boolean isPrintRetreatDish) {
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        ArrayList<PrintDishBean> orderDishEntities = DBHelper.getInstance(mContext).queryPrintDish(orderId, isPrintPresentDish, isPrintRetreatDish);
        String areaName = DBHelper.getInstance(mContext).getAreaNameById(orderEntity.getAreaId());
        int storeversion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
        String tableCode = "";
        if (storeversion == 0) {
            tableCode = DBHelper.getInstance(mContext).getTableNameByTableId(orderEntity.getTableId());
        } else if (storeversion == 1) {
            tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
        } else if (storeversion == 2) {
            tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
        }
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getCashierId());
        String waiterName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getWaiterId());
        double totalMoney = DBHelper.getInstance(mContext).getBillMoneyByOrderId(orderId, 1);
        float yhMoney = ((float) DBHelper.getInstance(mContext).getYHMoney(orderId)) / 100;
        double zhMoney = DBHelper.getInstance(mContext).getReceivableMoney(orderId, 1);
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        payModeEntities.addAll(DBHelper.getInstance(mContext).getPayModeByOrderId(orderId));
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32)).getBytes("GB2312"));
            SendDataByte((CustomMethod.getPrintStr("结账单(客户联)", 2, 32)).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"), 1, 32) + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            if (storeversion == 0) {
                //中餐版
                SendDataByte(CustomMethod.getPrintStr("台号：" + areaName + "-" + tableCode, "单号：" + orderEntity.getOrderNumber1(), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("帐单号：" + orderEntity.getSerialNumber(), "人数：" + orderEntity.getOrderGuests(), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("服务员：" + waiterName, "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 1) {
                //快餐版
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 2) {
                //奶茶版
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("品名         数量    单价   金额\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("-----点菜明细-----\n".getBytes("GB2312"));
            for (PrintDishBean orderDish :
                    orderDishEntities) {
                if (orderDish.getType() == 0) {
                    if (orderDish.getStatus() == -2) {
                        //赠菜
                        SendDataByte(("(赠)" + orderDish.getDishName() + orderDish.getDishConfig() + "\n").getBytes("GB2312"));
                    } else if (orderDish.getStatus() == -1) {
                        //退菜
                        SendDataByte(("(退)" + orderDish.getDishName() + orderDish.getDishConfig() + "\n").getBytes("GB2312"));
                    } else {
                        SendDataByte((orderDish.getDishName() + orderDish.getDishConfig() + "\n").getBytes("GB2312"));
                    }
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 13, orderDish.getDishCount() + orderDish.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(17, 8, String.valueOf(orderDish.getDishUnitPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(25, 7, String.valueOf(orderDish.getDishPrice()) + "\n").getBytes("GB2312"));
                } else {
                    SendDataByte(("    " + orderDish.getDishName() + orderDish.getDishConfig() + "\n").getBytes("GB2312"));
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 13, orderDish.getDishCount() + orderDish.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(17, 8, String.valueOf(orderDish.getDishUnitPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(25, 7, String.valueOf(orderDish.getDishPrice()) + "\n").getBytes("GB2312"));
                }
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("总金额", String.valueOf(totalMoney), 2, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("优惠金额", String.valueOf(yhMoney), 2, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("应付金额", String.valueOf(zhMoney), 2, 32).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            if (payModeEntities.size() > 0) {
                SendDataByte("--------------------------------\n".getBytes("GB2312"));
            }
            for (PayModeEntity payMode :
                    payModeEntities) {
                SendDataByte((payMode.getPaymentName() + "：" + payMode.getPayMoney() + "\n").getBytes("GB2312"));
                if (payMode.getPaymentType() == 0) {
                    try {
                        String[] realMoney = payMode.getElectricOrderSerial().split("`");
                        SendDataByte(("（实收：" + realMoney[0] + "，找零：" + realMoney[1] + "）\n").getBytes("GB2312"));
                    } catch (Exception e) {
                        Log.d("###", "解析失败");
                    }
                }
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            if (khlWZ != null) {
                SendDataByte(CustomMethod.getPrintStr("**" + khlWZ, 1, 32).getBytes("GB2312"));
            }
            SendDataByte(CustomMethod.getPrintStr("微信扫一扫立即支付", 1, 32).getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            Print_BMP();
            Log.d("USBDEVICE", "printKehulian---success");
            return "success";
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            Log.d("USBDEVICE", "printKehulian: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    //打印财务联
    public String printCaiwulian(String orderId, boolean isPrintPresentDish, boolean isPrintRetreatDish) {
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        ArrayList<PrintDishBean> orderDishEntities = new ArrayList<>();
        orderDishEntities.addAll(DBHelper.getInstance(mContext).queryPrintDish(orderId, isPrintPresentDish, isPrintRetreatDish));
        String areaName = DBHelper.getInstance(mContext).getAreaNameById(orderEntity.getAreaId());
        int storeversion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
        String tableCode = "";
        if (storeversion == 0) {
            tableCode = DBHelper.getInstance(mContext).getTableNameByTableId(orderEntity.getTableId());
        } else if (storeversion == 1) {
            tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
        } else if (storeversion == 2) {
            tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
        }
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getCashierId());
        String waiterName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getWaiterId());
        double totalMoney = DBHelper.getInstance(mContext).getBillMoneyByOrderId(orderId, 1);
        float yhMoney = ((float) DBHelper.getInstance(mContext).getYHMoney(orderId)) / 100;
        double zhMoney = DBHelper.getInstance(mContext).getReceivableMoney(orderId, 1);
        double incomeMoney = DBHelper.getInstance(mContext).getHadPayMoneyByOrderId(orderId);
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        payModeEntities.addAll(DBHelper.getInstance(mContext).getPayModeByOrderId(orderId));
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32)).getBytes("GB2312"));
            SendDataByte((CustomMethod.getPrintStr("结账单(财务联)", 2, 32)).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"), 1, 32) + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            if (storeversion == 0) {
                //中餐版
                SendDataByte(CustomMethod.getPrintStr("台号：" + areaName + "-" + tableCode, "单号：" + orderEntity.getOrderNumber1(), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("帐单号：" + orderEntity.getSerialNumber(), "人数：" + orderEntity.getOrderGuests(), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("服务员：" + waiterName, "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 1) {
                //快餐版
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 2) {
                //快餐版
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("品名   数量  单价  金额   折后额\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("-----点菜明细-----\n".getBytes("GB2312"));
            for (PrintDishBean printDishBean :
                    orderDishEntities) {
                if (printDishBean.getType() == 0) {
                    if (printDishBean.getStatus() == -2) {
                        //赠菜
                        SendDataByte(("(赠)" + printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    } else if (printDishBean.getStatus() == -1) {
                        //退菜
                        SendDataByte(("(退)" + printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    } else {
                        SendDataByte((printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    }
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 7, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(11, 6, String.valueOf(printDishBean.getDishUnitPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(17, 6, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(23, 9, String.valueOf(printDishBean.getDiscountMoney()) + "\n").getBytes("GB2312"));
                } else {
                    //套餐
                    SendDataByte(("    " + printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 7, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(11, 6, String.valueOf(printDishBean.getDishUnitPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(17, 6, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(23, 9, String.valueOf(printDishBean.getDiscountMoney()) + "\n").getBytes("GB2312"));
                }
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("总金额", String.valueOf(totalMoney), 2, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("优惠金额", String.valueOf(yhMoney), 2, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("应付金额", String.valueOf(zhMoney), 2, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("实收金额", String.valueOf(incomeMoney), 2, 32).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            if (payModeEntities.size() > 0) {
                SendDataByte("--------------------------------\n".getBytes("GB2312"));
            }
            for (PayModeEntity payMode :
                    payModeEntities) {
                SendDataByte((payMode.getPaymentName() + "：" + payMode.getPayMoney() + "\n").getBytes("GB2312"));
                if (payMode.getPaymentType() == 0) {
                    try {
                        String[] realMoney = payMode.getElectricOrderSerial().split("`");
                        SendDataByte(("（实收：" + realMoney[0] + "，找零：" + realMoney[1] + "）\n").getBytes("GB2312"));
                    } catch (Exception e) {

                    }
                }
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            return "success";
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    //打印消费底联
    public String Print_XFDL(ArrayList<PrintDishBean> printDishBeenes, String orderId) {
        String parnterName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        ArrayList<PrintDishBean> orderDishEntities = printDishBeenes;
        OrderEntity mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        String serailNumber = mOrderEntity.getSerialNumber();
        double mTotalMoney = 0.00;
        for (PrintDishBean printDishBean :
                orderDishEntities) {
            mTotalMoney = jufeng.juyancash.util.AmountUtils.add(printDishBean.getDishPrice(), mTotalMoney);
        }
        String waiterName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getWaiterId());
        String areaName = "";
        String tableCode = "";
        String employeeName = "无";
        int storeversion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
        if (storeversion == 0) {
            if (mOrderEntity.getOrderType() == 1) {
                areaName = "外卖";
                tableCode = "";
            } else {
                areaName = DBHelper.getInstance(mContext).getAreaNameById(mOrderEntity.getAreaId());
                employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                tableCode = "-" + DBHelper.getInstance(mContext).getTableNameByTableId(mOrderEntity.getTableId());
            }
        } else if (storeversion == 1) {
            if (mOrderEntity.getOrderType() == 1) {
                tableCode = "";
            } else {
                employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
            }
        } else if (storeversion == 2) {
            if (mOrderEntity.getOrderType() == 1) {
                tableCode = "";
            } else {
                employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
            }
        }
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr(parnterName, 2, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("点菜单(消费底联)", 2, 32).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr((CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm")), 1, 32) + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            if (storeversion == 0) {
                SendDataByte("台号：".getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x11;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte((areaName + "-" + tableCode + "\n").getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte("单号：".getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x11;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte((orderEntity.getOrderNumber1() + "\n").getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte(CustomMethod.getPrintStr("帐单号：" + serailNumber, "人数：" + orderEntity.getOrderGuests(), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("服务员：" + waiterName, "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 1) {
                //快餐版
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 2) {
                //快餐版
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            }
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("品名            数量        金额\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("-----点菜明细-----\n".getBytes("GB2312"));
            for (PrintDishBean printDishBean :
                    orderDishEntities) {
                if (printDishBean.getType() == 0) {
                    if (printDishBean.getDishConfig().length() > 0) {
                        SendDataByte((printDishBean.getDishName()).getBytes("GB2312"));
                        SendDataByte((printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    } else {
                        SendDataByte((printDishBean.getDishName() + "\n").getBytes("GB2312"));
                    }
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 16, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(20, 12, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                } else {
                    //套餐
                    if (printDishBean.getDishConfig().length() > 0) {
                        SendDataByte(("    " + printDishBean.getDishName()).getBytes("GB2312"));
                        SendDataByte((printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    } else {
                        SendDataByte(("    " + printDishBean.getDishName() + "\n").getBytes("GB2312"));
                    }
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 16, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(20, 12, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                }
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("合计", String.valueOf(mTotalMoney), 2, 32).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(("收银员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "\n\n").getBytes("GB2312"));
            if (khlWZ != null) {
                SendDataByte(CustomMethod.getPrintStr("**" + khlWZ, 1, 32).getBytes("GB2312"));
            }
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            return "success";
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    //打印划菜联
    public String Print_HCL(ArrayList<PrintDishBean> printDishBeenes, String orderId) {
        String parnterName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        ArrayList<PrintDishBean> orderDishEntities = printDishBeenes;
        OrderEntity mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        double mTotalMoney = 0.00;
        for (PrintDishBean printDishBean :
                orderDishEntities) {
            mTotalMoney = jufeng.juyancash.util.AmountUtils.add(printDishBean.getDishPrice(), mTotalMoney);
        }
        String waiterName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getWaiterId());
        String areaName = "";
        String tableCode = "";
        String employeeName = "无";
        int storeversion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
        if (storeversion == 0) {
            if (mOrderEntity.getOrderType() == 1) {
                areaName = "外卖";
                tableCode = "";
            } else {
                areaName = DBHelper.getInstance(mContext).getAreaNameById(mOrderEntity.getAreaId());
                employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                tableCode = "-" + DBHelper.getInstance(mContext).getTableNameByTableId(mOrderEntity.getTableId());
            }
        } else if (storeversion == 1) {
            if (mOrderEntity.getOrderType() == 1) {
                tableCode = "";
            } else {
                employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
            }
        } else if (storeversion == 2) {
            if (mOrderEntity.getOrderType() == 1) {
                tableCode = "";
            } else {
                employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
            }
        }
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr(parnterName, 2, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("点菜单(划菜联)", 2, 32).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"), 1, 32) + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            if (storeversion == 0) {
                SendDataByte("台号：".getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x11;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte((areaName + "-" + tableCode + "\n").getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte("单号：".getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x11;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte((orderEntity.getOrderNumber1() + "\n").getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte(CustomMethod.getPrintStr("帐单号：" + orderEntity.getSerialNumber(), "人数：" + orderEntity.getOrderGuests(), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("服务员：" + waiterName, "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 1) {
                //快餐版
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 2) {
                //快餐版
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            }
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("品名            数量        金额\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("-----点菜明细-----\n".getBytes("GB2312"));
            for (PrintDishBean printDishBean :
                    orderDishEntities) {
                if (printDishBean.getType() == 0) {
                    if (printDishBean.getDishConfig().length() > 0) {
                        SendDataByte((printDishBean.getDishName()).getBytes("GB2312"));
                        SendDataByte((printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    } else {
                        SendDataByte((printDishBean.getDishName() + "\n").getBytes("GB2312"));
                    }
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 16, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(20, 12, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                } else {
                    //套餐
                    if (printDishBean.getDishConfig().length() > 0) {
                        SendDataByte(("    " + printDishBean.getDishName()).getBytes("GB2312"));
                        SendDataByte((printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    } else {
                        SendDataByte(("    " + printDishBean.getDishName() + "\n").getBytes("GB2312"));
                    }
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 16, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(20, 12, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                }
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("合计", String.valueOf(mTotalMoney), 2, 32).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(("收银员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "\n\n").getBytes("GB2312"));
            if (khlWZ != null) {
                SendDataByte(CustomMethod.getPrintStr("**" + khlWZ, 1, 32).getBytes("GB2312"));
            }
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            return "success";
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    //前台打印的菜品
    public String printQiantaiDish(ArrayList<PrintDishBean> printDishBeenes, String orderId, boolean isAddDish) {
        try {
            String areaName = "";
            String tableCode = "";
            String employeeName = "无";
            OrderEntity mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
            int storeversion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
            if (storeversion == 0) {
                if (mOrderEntity.getOrderType() == 1) {
                    areaName = "外卖";
                    tableCode = "";
                } else {
                    areaName = DBHelper.getInstance(mContext).getAreaNameById(mOrderEntity.getAreaId());
                    employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = "-" + DBHelper.getInstance(mContext).getTableNameByTableId(mOrderEntity.getTableId());
                }
            } else if (storeversion == 1) {
                if (mOrderEntity.getOrderType() == 1) {
                    tableCode = "";
                } else {
                    employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = mOrderEntity.getTableId() == null ? "" : mOrderEntity.getTableId();
                }
            } else if (storeversion == 2) {
                if (mOrderEntity.getOrderType() == 1) {
                    tableCode = "";
                } else {
                    employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = mOrderEntity.getTableId() == null ? "" : mOrderEntity.getTableId();
                }
            }
            SendDataByte(Command.ESC_Init);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            if (isAddDish) {
                SendDataByte(CustomMethod.getPrintStr("配菜单(加菜)", 2, 32).getBytes("GB2312"));
            } else {
                SendDataByte(CustomMethod.getPrintStr("配菜单", 2, 32).getBytes("GB2312"));
            }
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            if (storeversion == 0) {
                SendDataByte(CustomMethod.getPrintStr(areaName + "-" + tableCode, "单号：" + mOrderEntity.getOrderNumber1(), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"), "服务员：" + employeeName, 1, 32).getBytes("GB2312"));
            } else if (storeversion == 1) {
                SendDataByte(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + mOrderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 2) {
                SendDataByte(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + mOrderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            }

            for (PrintDishBean printDishBean :
                    printDishBeenes) {
                if (printDishBean.getType() == 1) {
                    //套餐
                    SendDataByte((printDishBean.getParentBean().getDishCount() + printDishBean.getUnitName() + " X " + printDishBean.getParentBean().getDishName() + "\n").getBytes("GB2312"));
                    SendDataByte(("    " + printDishBean.getDishCount() + printDishBean.getUnitName() + " X " + printDishBean.getDishName() + "\n").getBytes("GB2312"));
                    if (printDishBean.getDishConfig().length() > 0) {
                        SendDataByte(("    " + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    }
                } else {
                    SendDataByte((printDishBean.getDishCount() + printDishBean.getUnitName() + " X " + printDishBean.getDishName() + "\n").getBytes("GB2312"));
                    if (printDishBean.getDishConfig().length() > 0) {
                        SendDataByte((printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    }
                }
            }
            SendDataByte((CustomMethod.getPrintStr(areaName + "-" + tableCode, 1, 32) + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            return "success";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //打印外卖单
    public String printWaimai(String orderId, boolean isPrintPresentDish, boolean isPrintRetreatDish) {
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        TakeOutOrderEntity takeOutOrderEntity = DBHelper.getInstance(mContext).getTakeOutOrderById(orderId);
        if (orderEntity != null && takeOutOrderEntity != null) {
            ArrayList<PrintDishBean> orderDishEntities = new ArrayList<>();
            String areaName = "外卖";
            orderDishEntities.addAll(DBHelper.getInstance(mContext).queryPrintDish(orderId, isPrintPresentDish, isPrintRetreatDish));
            String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getCashierId());
            String sendTime = CustomMethod.parseTime(takeOutOrderEntity.getTakeoutTime(), "yyyy-MM-dd HH:mm");
            String sendAddress = takeOutOrderEntity.getTakeoutAddress();
            String phone = takeOutOrderEntity.getGuestPhone();
            String name = takeOutOrderEntity.getGuestName();
            String[] marks = null;
            try {
                marks = takeOutOrderEntity.getTakeoutMark().split("`");
            } catch (Exception e) {

            }
            String totalMoney = AmountUtils.changeF2Y(orderEntity.getTotalMoney());
            String yhMoney = AmountUtils.changeF2Y(orderEntity.getDiscountTotalMoney());
            String zhMoney = AmountUtils.changeF2Y(orderEntity.getCloseMoney());
            String incomeMoney = AmountUtils.changeF2Y(orderEntity.getCloseMoney());
            ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
            payModeEntities.addAll(DBHelper.getInstance(mContext).getPayModeByOrderId(orderId));
            String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
            try {
                SendDataByte(Command.ESC_Init);
                Command.ESC_Align[2] = 0x00;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x11;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32) + "\n").getBytes("GB2312"));
                SendDataByte((CustomMethod.getPrintStr("结账单", 2, 32) + "\n").getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte((CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"), 1, 32) + "\n").getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x01;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte(CustomMethod.getPrintStr("台号：" + areaName, "单号：" + String.valueOf(orderEntity.getOrderNumber1()), 2, 32).getBytes("GB2312"));
                SendDataByte(("配送地址：" + sendAddress + "\n").getBytes("GB2312"));
                SendDataByte(("联系电话：" + phone + "\n").getBytes("GB2312"));
                SendDataByte(("联系人：" + name + "\n").getBytes("GB2312"));
                SendDataByte(("配送时间：" + sendTime + "\n").getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                if (marks != null && marks.length > 0) {
                    SendDataByte(("备注：" + marks[0] + "\n").getBytes("GB2312"));
                }
                if (marks != null && marks.length > 1) {
                    SendDataByte(("发票：" + marks[1] + "\n").getBytes("GB2312"));
                }
                SendDataByte(CustomMethod.getPrintStr("帐单号：" + orderEntity.getSerialNumber(), "单位：元", 1, 32).getBytes("GB2312"));
                SendDataByte("--------------------------------\n".getBytes("GB2312"));
                SendDataByte("品名   数量  单价  金额   折后额\n".getBytes("GB2312"));
                SendDataByte("--------------------------------\n".getBytes("GB2312"));
                SendDataByte("-----点菜明细-----\n".getBytes("GB2312"));
                for (PrintDishBean printDishBean :
                        orderDishEntities) {
                    if (printDishBean.getType() == 0) {
                        if (printDishBean.getStatus() == -2) {
                            //赠菜
                            SendDataByte(("(赠)" + printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                        } else if (printDishBean.getStatus() == -1) {
                            //退菜
                            SendDataByte(("(退)" + printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                        } else {
                            SendDataByte((printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                        }
                        SendDataByte("    ".getBytes("GB2312"));
                        SendDataByte(CustomMethod.getPrintStr(4, 7, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                        SendDataByte(CustomMethod.getPrintStr(11, 6, String.valueOf(printDishBean.getDishUnitPrice())).getBytes("GB2312"));
                        SendDataByte(CustomMethod.getPrintStr(17, 6, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                        SendDataByte(CustomMethod.getPrintStr(23, 9, String.valueOf(printDishBean.getDiscountMoney()) + "\n").getBytes("GB2312"));
                    } else {
                        //套餐
                        SendDataByte(("    " + printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                        SendDataByte("    ".getBytes("GB2312"));
                        SendDataByte(CustomMethod.getPrintStr(4, 7, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                        SendDataByte(CustomMethod.getPrintStr(11, 6, String.valueOf(printDishBean.getDishUnitPrice())).getBytes("GB2312"));
                        SendDataByte(CustomMethod.getPrintStr(17, 6, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                        SendDataByte(CustomMethod.getPrintStr(23, 9, String.valueOf(printDishBean.getDiscountMoney()) + "\n").getBytes("GB2312"));
                    }
                }

                SendDataByte("--------------------------------\n".getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x11;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte(CustomMethod.getPrintStr("总金额", String.valueOf(totalMoney), 2, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("优惠金额", String.valueOf(yhMoney), 2, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("应付金额", String.valueOf(zhMoney), 2, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("实收金额", String.valueOf(incomeMoney), 2, 32).getBytes("GB2312"));
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                if (payModeEntities.size() > 0) {
                    SendDataByte("--------------------------------\n".getBytes("GB2312"));
                }
                for (PayModeEntity payMode :
                        payModeEntities) {
                    SendDataByte((payMode.getPaymentName() + "：" + payMode.getPayMoney() + "\n").getBytes("GB2312"));
                    if (payMode.getPaymentType() == 0) {
                        try {
                            String[] realMoney = payMode.getElectricOrderSerial().split("`");
                            SendDataByte(("（实收：" + realMoney[0] + "，找零：" + realMoney[1] + "）\n").getBytes("GB2312"));
                        } catch (Exception e) {

                        }
                    }
                }
                SendDataByte("--------------------------------\n".getBytes("GB2312"));
                SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
                SendDataByte(("打印时间：" + CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
                if (khlWZ != null) {
                    SendDataByte(CustomMethod.getPrintStr("**" + khlWZ, 1, 32).getBytes("GB2312"));
                }
                SendDataByte("\n".getBytes("GB2312"));
                SendDataByte("\n".getBytes("GB2312"));
                SendDataByte("\n".getBytes("GB2312"));
                SendDataByte("\n".getBytes("GB2312"));
                SendDataByte("\n".getBytes("GB2312"));
                return "success";
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    //打印催菜
    public String printRemind(ArrayList<PrintDishBean> printDishBeenes, String orderId) {
        try {
            String areaName = "";
            String tableCode = "";
            String employeeName = "无";
            OrderEntity mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
            int storeversion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
            if (storeversion == 0) {
                if (mOrderEntity.getOrderType() == 1) {
                    areaName = "外卖";
                    tableCode = "";
                } else {
                    areaName = DBHelper.getInstance(mContext).getAreaNameById(mOrderEntity.getAreaId());
                    employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = "-" + DBHelper.getInstance(mContext).getTableNameByTableId(mOrderEntity.getTableId());
                }
            } else if (storeversion == 1) {
                if (mOrderEntity.getOrderType() == 1) {
                    tableCode = "";
                } else {
                    employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = mOrderEntity.getTableId() == null ? "" : mOrderEntity.getTableId();
                }
            } else if (storeversion == 2) {
                if (mOrderEntity.getOrderType() == 1) {
                    tableCode = "";
                } else {
                    employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = mOrderEntity.getTableId() == null ? "" : mOrderEntity.getTableId();
                }
            }
            SendDataByte(Command.ESC_Init);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("催菜单", 2, 32).getBytes("GB2312"));
            if (storeversion == 0) {
                SendDataByte((areaName + tableCode + "\n").getBytes("GB2312"));
                SendDataByte(("单号：" + mOrderEntity.getOrderNumber1() + "\n").getBytes("GB2312"));
            } else if (storeversion == 1) {
                SendDataByte(("单号：" + mOrderEntity.getOrderNumber1() + "\n").getBytes("GB2312"));
            } else if (storeversion == 2) {
                SendDataByte(("单号：" + mOrderEntity.getOrderNumber1() + "\n").getBytes("GB2312"));
            }

            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            for (int i = 0; i < printDishBeenes.size(); i++) {
                PrintDishBean prePrintDishBean = printDishBeenes.get(i - 1 < 0 ? 0 : i - 1);
                PrintDishBean printDishBean = printDishBeenes.get(i);
                if (printDishBean.getType() == 1) {
                    //套餐
                    if (prePrintDishBean.getType() == 1 && printDishBean.getParentBean().getOrderDishId().equals(prePrintDishBean.getParentBean().getOrderDishId())) {
                        //当前套餐内商品和前面的套餐内商品属于同一个套餐
                        SendDataByte(("    (催)" + printDishBean.getDishCount() + printDishBean.getUnitName() + " X " + printDishBean.getDishName() + "\n").getBytes("GB2312"));
                        if (printDishBean.getDishConfig().length() > 0) {
                            SendDataByte(("    " + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                        }
                    } else {
                        SendDataByte(("(催)" + printDishBean.getParentBean().getDishName() + printDishBean.getParentBean().getDishCount() + printDishBean.getUnitName() + "\n").getBytes("GB2312"));
                        SendDataByte(("    (催)" + printDishBean.getDishName() + printDishBean.getDishCount() + printDishBean.getUnitName() + "\n").getBytes("GB2312"));
                        if (printDishBean.getDishConfig().length() > 0) {
                            SendDataByte(("    " + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                        }
                    }
                } else {
                    SendDataByte(("（催）" + printDishBean.getDishName() + "\n").getBytes("GB2312"));
                    SendDataByte((printDishBean.getDishCount() + printDishBean.getUnitName() + "\n").getBytes("GB2312"));
                    if (printDishBean.getDishConfig().length() > 0) {
                        SendDataByte((printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    }
                }
            }
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("时间：" + CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            return "success";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String printRetreat(ArrayList<PrintDishBean> printDishBeenes, String orderId, String employeeName, String reason) {
        try {
            String areaName = "";
            String tableCode = "";
            OrderEntity mOrderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
            int storeversion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
            if (storeversion == 0) {
                if (mOrderEntity.getOrderType() == 1) {
                    areaName = "外卖";
                    tableCode = "";
                } else {
                    areaName = DBHelper.getInstance(mContext).getAreaNameById(mOrderEntity.getAreaId());
                    employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = "-" + DBHelper.getInstance(mContext).getTableNameByTableId(mOrderEntity.getTableId());
                }
            } else if (storeversion == 1) {
                if (mOrderEntity.getOrderType() == 1) {
                    tableCode = "";
                } else {
                    employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = mOrderEntity.getTableId() == null ? "" : mOrderEntity.getTableId();
                }
            } else if (storeversion == 2) {
                if (mOrderEntity.getOrderType() == 1) {
                    tableCode = "";
                } else {
                    employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = mOrderEntity.getTableId() == null ? "" : mOrderEntity.getTableId();
                }
            }
            SendDataByte(Command.ESC_Init);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("退菜单", 2, 32).getBytes("GB2312"));
            if (storeversion == 0) {
                SendDataByte((areaName + tableCode + "\n").getBytes("GB2312"));
                SendDataByte(("单号：" + mOrderEntity.getOrderNumber1() + "\n").getBytes("GB2312"));
            } else if (storeversion == 1) {
                SendDataByte(("单号：" + mOrderEntity.getOrderNumber1() + "\n").getBytes("GB2312"));
            } else if (storeversion == 2) {
                SendDataByte(("单号：" + mOrderEntity.getOrderNumber1() + "\n").getBytes("GB2312"));
            }

            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            for (int i = 0; i < printDishBeenes.size(); i++) {
                PrintDishBean prePrintDishBean = printDishBeenes.get(i - 1 < 0 ? 0 : i - 1);
                PrintDishBean printDishBean = printDishBeenes.get(i);
                if (printDishBean.getType() == 1) {
                    //套餐
                    if (prePrintDishBean.getType() == 1 && printDishBean.getParentBean().getOrderDishId().equals(prePrintDishBean.getParentBean().getOrderDishId())) {
                        //当前套餐内商品和前面的套餐内商品属于同一个套餐
                        SendDataByte(("    (退)" + printDishBean.getDishCount() + printDishBean.getUnitName() + " X " + printDishBean.getDishName() + "\n").getBytes("GB2312"));
                        if (printDishBean.getDishConfig().length() > 0) {
                            SendDataByte(("    " + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                        }
                    } else {
                        SendDataByte(("(退)" + printDishBean.getParentBean().getDishName() + printDishBean.getParentBean().getDishCount() + printDishBean.getUnitName() + "\n").getBytes("GB2312"));
                        SendDataByte(("    (退)" + printDishBean.getDishName() + printDishBean.getDishCount() + printDishBean.getUnitName() + "\n").getBytes("GB2312"));
                        if (printDishBean.getDishConfig().length() > 0) {
                            SendDataByte(("    " + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                        }
                    }
                } else {
                    SendDataByte(("（退）" + printDishBean.getDishName() + "\n").getBytes("GB2312"));
                    SendDataByte((printDishBean.getDishCount() + printDishBean.getUnitName() + "\n").getBytes("GB2312"));
                    if (printDishBean.getDishConfig().length() > 0) {
                        SendDataByte((printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    }
                }
            }
            SendDataByte(("--说明：" + reason + "\n").getBytes("GB2312"));
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("时间：" + CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            return "success";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //挂账单
    public String printAccountPay(String orderId, BillAccountHistoryEntity mBillAccountHistoryEntity, boolean isPrintPresentDish, boolean isPrintRetreatDish) {
        long currentTime = System.currentTimeMillis();
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(orderId);
        ArrayList<PrintDishBean> orderDishEntities = new ArrayList<>();
        orderDishEntities.addAll(DBHelper.getInstance(mContext).queryPrintDish(orderId, isPrintPresentDish, isPrintRetreatDish));
        String areaName = DBHelper.getInstance(mContext).getAreaNameById(orderEntity.getAreaId());
        String tableCode = "";
        int storeversion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
        if (storeversion == 0) {
            tableCode = DBHelper.getInstance(mContext).getTableNameByTableId(orderEntity.getTableId());
        } else if (storeversion == 1) {
            tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
        } else if (storeversion == 2) {
            tableCode = orderEntity.getTableId() == null ? "" : orderEntity.getTableId();
        }
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getCashierId());
        String waiterName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getWaiterId());
        double totalMoney = DBHelper.getInstance(mContext).getBillMoneyByOrderId(orderId, 1);
        float yhMoney = ((float) DBHelper.getInstance(mContext).getYHMoney(orderId)) / 100;
        double zhMoney = DBHelper.getInstance(mContext).getReceivableMoney(orderId, 1);
        double incomeMoney = DBHelper.getInstance(mContext).getHadPayMoneyByOrderId(orderId);
        String billAccountName = mBillAccountHistoryEntity.getBillAccountName();
        String billUnitName = mBillAccountHistoryEntity.getBillAccountUnitName() == null ? "无" : mBillAccountHistoryEntity.getBillAccountUnitName();
        String billPersonName = mBillAccountHistoryEntity.getBillAccountPersonName() == null ? "无" : mBillAccountHistoryEntity.getBillAccountPersonName();
        String signName = mBillAccountHistoryEntity.getBillAccountSignName() == null ? "无" : mBillAccountHistoryEntity.getBillAccountSignName();
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32)).getBytes("GB2312"));
            SendDataByte((CustomMethod.getPrintStr("挂账签字单", 2, 32)).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm"), 1, 32) + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            if (storeversion == 0) {
                SendDataByte(CustomMethod.getPrintStr("台号：" + areaName + "-" + tableCode, "单号：" + orderEntity.getOrderNumber1(), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("帐单号：" + orderEntity.getSerialNumber(), "人数：" + orderEntity.getOrderGuests(), 1, 32).getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("服务员：" + waiterName, "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 1) {
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            } else if (storeversion == 2) {
                SendDataByte(("流水号：" + orderEntity.getSerialNumber() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("单号：" + orderEntity.getOrderNumber1(), "单位：元", 1, 32).getBytes("GB2312"));
            }

            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("品名   数量  单价  金额   折后额\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("-----点菜明细-----\n".getBytes("GB2312"));
            for (PrintDishBean printDishBean :
                    orderDishEntities) {
                if (printDishBean.getType() == 0) {
                    if (printDishBean.getStatus() == -2) {
                        //赠菜
                        SendDataByte(("(赠)" + printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    } else if (printDishBean.getStatus() == -1) {
                        //退菜
                        SendDataByte(("(退)" + printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    } else {
                        SendDataByte((printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    }
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 7, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(11, 6, String.valueOf(printDishBean.getDishUnitPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(17, 6, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(23, 9, String.valueOf(printDishBean.getDiscountMoney()) + "\n").getBytes("GB2312"));
                } else {
                    //套餐
                    SendDataByte(("    " + printDishBean.getDishName() + printDishBean.getDishConfig() + "\n").getBytes("GB2312"));
                    SendDataByte("    ".getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(4, 7, printDishBean.getDishCount() + printDishBean.getUnitName()).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(11, 6, String.valueOf(printDishBean.getDishUnitPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(17, 6, String.valueOf(printDishBean.getDishPrice())).getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr(23, 9, String.valueOf(printDishBean.getDiscountMoney()) + "\n").getBytes("GB2312"));
                }
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("总金额：", String.valueOf(totalMoney), 2, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("优惠金额：", String.valueOf(yhMoney), 2, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("应付金额：", String.valueOf(zhMoney), 2, 32).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("挂账金额：", String.valueOf(incomeMoney), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("挂账名称：", String.valueOf(billAccountName), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("挂账单位：", String.valueOf(billUnitName), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("挂账人：", String.valueOf(billPersonName), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("本店签字人：", String.valueOf(signName), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("挂账日期：", CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm"), 1, 32).getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("顾客签字：", "", 1, 32).getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            if (khlWZ != null) {
                SendDataByte(CustomMethod.getPrintStr("**" + khlWZ, 1, 32).getBytes("GB2312"));
            }
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            return "success";
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public String printStatistic(int dateType, DishTypeModel dishTypeModel, ArrayList<DishModel> dishModels) {
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        String cashierId = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", "");
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(cashierId);
        String startTime = "", endTime = "";
        long currentTime = CustomMethod.parseTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        switch (dateType) {
            case -1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 0:
                startTime = CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
        }
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32)).getBytes("GB2312"));
            SendDataByte((CustomMethod.getPrintStr("菜类点菜统计", 2, 32)).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("菜类：".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((dishTypeModel.getDishTypeModelTypeName() + "\n").getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("统计时间段：".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x01;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((startTime + "\n").getBytes("GB2312"));
            SendDataByte(("            " + endTime + "\n").getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("     菜名                   数量\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            for (DishModel dishModel :
                    dishModels) {
                SendDataByte(CustomMethod.getPrintStr(dishModel.getDishModelName(), String.valueOf(dishModel.getCount()), 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("###", "打印菜类统计异常");
        }
        return "";
    }

    //打印账单统计单
    public String printOrderCollection(String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, Map<String, String> orderMoneyDetail, ArrayList<PayModeEntity> payModeEntities) {
        String startTime = "", endTime = "";
        long currentTime = CustomMethod.parseTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        switch (dateType) {
            case -1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 0:
                startTime = CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
        }
        if (areaTypeName.equals("-1")) {
            areaTypeName = "全部";
        } else if (areaTypeName.equals("0")) {
            areaTypeName = "外卖单";
        } else {
            areaTypeName = DBHelper.getInstance(mContext).getAreaNameById(areaTypeName);
        }
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        String cashierId = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", "");
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(cashierId);
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32)).getBytes("GB2312"));
            SendDataByte((CustomMethod.getPrintStr("账单汇总", 2, 32)).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("统计时间段：".getBytes("GB2312"));
            SendDataByte((startTime + "\n").getBytes("GB2312"));
            SendDataByte(("            " + endTime + "\n").getBytes("GB2312"));
            SendDataByte(("收 银 员：" + cashierName + "\n").getBytes("GB2312"));
            SendDataByte(("交接状态：" + isShift + "\n").getBytes("GB2312"));
            SendDataByte(("付款方式：" + payModeName + "\n").getBytes("GB2312"));
            SendDataByte(("区    域：" + areaTypeName + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("账单总数", String.valueOf(orderMoneyDetail.get("totalCount")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("已结账账单总数", String.valueOf(orderMoneyDetail.get("cashieredCount")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("已结账账单金额", String.valueOf(orderMoneyDetail.get("cashieredMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("未结账账单总数", String.valueOf(orderMoneyDetail.get("unCashieredCount")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("未结账账单金额", String.valueOf(orderMoneyDetail.get("unCashieredMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("----支付详情----\n".getBytes("GB2312"));
            for (PayModeEntity payMode :
                    payModeEntities) {
                SendDataByte(CustomMethod.getPrintStr(payMode.getPaymentName(), "" + payMode.getPayMoney(), 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("合计", String.valueOf(orderMoneyDetail.get("incomeMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x01;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("消费合计", String.valueOf(orderMoneyDetail.get("receivableMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("付款合计", String.valueOf(orderMoneyDetail.get("incomeMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("赠菜合计", String.valueOf(orderMoneyDetail.get("presentMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("抹零合计", String.valueOf(orderMoneyDetail.get("mlMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("折扣合计", String.valueOf(orderMoneyDetail.get("discountMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("不吉利尾数合计", String.valueOf(orderMoneyDetail.get("bjlMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("优惠券合计", String.valueOf(orderMoneyDetail.get("couponMoney")), 1, 32).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("###", "打印菜类统计异常");
        }
        return "";
    }

    //打印收银员账单统计
    public String printCashierCollection(String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, Map<String, String> orderMoneyDetail, ArrayList<PayModeEntity> payModeEntities) {
        String startTime = "", endTime = "";
        long currentTime = CustomMethod.parseTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        switch (dateType) {
            case -1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 0:
                startTime = CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
        }
        if (areaTypeName.equals("-1")) {
            areaTypeName = "全部";
        } else if (areaTypeName.equals("0")) {
            areaTypeName = "外卖单";
        } else {
            areaTypeName = DBHelper.getInstance(mContext).getAreaNameById(areaTypeName);
        }
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        String cashierId = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", "");
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(cashierId);
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32)).getBytes("GB2312"));
            SendDataByte((CustomMethod.getPrintStr("员工账单汇总", 2, 32)).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("统计时间段：".getBytes("GB2312"));
            SendDataByte((startTime + "\n").getBytes("GB2312"));
            SendDataByte(("            " + endTime + "\n").getBytes("GB2312"));
            SendDataByte(("收 银 员：" + cashierName + "\n").getBytes("GB2312"));
            SendDataByte(("交接状态：" + isShift + "\n").getBytes("GB2312"));
            SendDataByte(("付款方式：" + payModeName + "\n").getBytes("GB2312"));
            SendDataByte(("区    域：" + areaTypeName + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("----支付详情----\n".getBytes("GB2312"));
            for (PayModeEntity payMode :
                    payModeEntities) {
                SendDataByte(CustomMethod.getPrintStr(payMode.getPaymentName(), "" + payMode.getPayMoney(), 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("合计", String.valueOf(orderMoneyDetail.get("incomeMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x01;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("消费合计", String.valueOf(orderMoneyDetail.get("receivableMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("付款合计", String.valueOf(orderMoneyDetail.get("incomeMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("赠菜合计", String.valueOf(orderMoneyDetail.get("presentMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("抹零合计", String.valueOf(orderMoneyDetail.get("mlMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("折扣合计", String.valueOf(orderMoneyDetail.get("discountMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("不吉利尾数合计", String.valueOf(orderMoneyDetail.get("bjlMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("优惠券合计", String.valueOf(orderMoneyDetail.get("couponMoney")), 1, 32).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("###", "打印菜类统计异常");
        }
        return "";
    }

    //打印分类汇总账单
    public String printTypeCollection(String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, ArrayList<DishTypeCollectionItemBean> dishTypeCollectionItemBeens) {
        String startTime = "", endTime = "";
        long currentTime = CustomMethod.parseTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        switch (dateType) {
            case -1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 0:
                startTime = CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
        }
        if (areaTypeName.equals("-1")) {
            areaTypeName = "全部";
        } else if (areaTypeName.equals("0")) {
            areaTypeName = "外卖单";
        } else {
            areaTypeName = DBHelper.getInstance(mContext).getAreaNameById(areaTypeName);
        }
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        String cashierId = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", "");
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(cashierId);
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32)).getBytes("GB2312"));
            SendDataByte((CustomMethod.getPrintStr("菜品分类汇总", 2, 32)).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("统计时间段：".getBytes("GB2312"));
            SendDataByte((startTime + "\n").getBytes("GB2312"));
            SendDataByte(("            " + endTime + "\n").getBytes("GB2312"));
            SendDataByte(("收 银 员：" + cashierName + "\n").getBytes("GB2312"));
            SendDataByte(("交接状态：" + isShift + "\n").getBytes("GB2312"));
            SendDataByte(("付款方式：" + payModeName + "\n").getBytes("GB2312"));
            SendDataByte(("区    域：" + areaTypeName + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("菜品分类        数量        合计\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            for (DishTypeCollectionItemBean dishTypeCollectionItemBean :
                    dishTypeCollectionItemBeens) {
                SendDataByte((dishTypeCollectionItemBean.getDishTypeName() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("                " + dishTypeCollectionItemBean.getDishTypeCount(), "" + dishTypeCollectionItemBean.getDishTypeMoney(), 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("###", "打印菜类统计异常");
        }
        return "";
    }

    /**
     * 交接班打印
     *
     * @param turnOverTime                交接班时间
     * @param cashierName                 收银员名称
     * @param dateType                    时间段
     * @param areaTypeName                客单区域
     * @param payModeEntities             支付详情
     * @param dishTypeCollectionItemBeens 分类统计
     * @param orderMoneyDetail            金额明细
     * @return
     */
    public String printTurnOverOrder(String turnOverTime, String cashierName, int dateType, String areaTypeName, ArrayList<PayModeEntity> payModeEntities, ArrayList<DishTypeCollectionItemBean> dishTypeCollectionItemBeens, Map<String, String> orderMoneyDetail) {
        String startTime = "", endTime = "";
        long currentTime = CustomMethod.parseTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        switch (dateType) {
            case -1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 0:
                startTime = CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
        }
        if (areaTypeName.equals("-1")) {
            areaTypeName = "全部";
        } else if (areaTypeName.equals("0")) {
            areaTypeName = "外卖单";
        } else {
            areaTypeName = DBHelper.getInstance(mContext).getAreaNameById(areaTypeName);
        }
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        String cashierId = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", "");
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(cashierId);
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32)).getBytes("GB2312"));
            SendDataByte((CustomMethod.getPrintStr("交接班账单", 2, 32)).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(("交接班时间：" + turnOverTime + "\n").getBytes("GB2312"));
            SendDataByte("统计时间段：".getBytes("GB2312"));
            SendDataByte((startTime + "\n").getBytes("GB2312"));
            SendDataByte(("            " + endTime + "\n").getBytes("GB2312"));
            SendDataByte(("收 银 员：" + cashierName + "\n").getBytes("GB2312"));
            SendDataByte(("交接状态：未交接\n").getBytes("GB2312"));
            SendDataByte(("付款方式：所有\n").getBytes("GB2312"));
            SendDataByte(("区    域：" + areaTypeName + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("账单总数", String.valueOf(orderMoneyDetail.get("totalCount")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("已结账账单总数", String.valueOf(orderMoneyDetail.get("cashieredCount")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("已结账账单金额", String.valueOf(orderMoneyDetail.get("cashieredMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("未结账账单总数", String.valueOf(orderMoneyDetail.get("unCashieredCount")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("未结账账单金额", String.valueOf(orderMoneyDetail.get("unCashieredMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("----支付详情----\n".getBytes("GB2312"));
            for (PayModeEntity payMode :
                    payModeEntities) {
                SendDataByte(CustomMethod.getPrintStr(payMode.getPaymentName(), "" + payMode.getPayMoney(), 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("菜品分类        数量        合计\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            for (DishTypeCollectionItemBean dishTypeCollectionItemBean :
                    dishTypeCollectionItemBeens) {
                SendDataByte((dishTypeCollectionItemBean.getDishTypeName() + "\n").getBytes("GB2312"));
                SendDataByte(CustomMethod.getPrintStr("                " + dishTypeCollectionItemBean.getDishTypeCount(), "" + dishTypeCollectionItemBean.getDishTypeMoney(), 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x01;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("消费合计", String.valueOf(orderMoneyDetail.get("receivableMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("付款合计", String.valueOf(orderMoneyDetail.get("incomeMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("赠菜合计", String.valueOf(orderMoneyDetail.get("presentMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("抹零合计", String.valueOf(orderMoneyDetail.get("mlMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("折扣合计", String.valueOf(orderMoneyDetail.get("discountMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("不吉利尾数合计", String.valueOf(orderMoneyDetail.get("bjlMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("优惠券合计", String.valueOf(orderMoneyDetail.get("couponMoney")), 1, 32).getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(("操作员：" + employeeName + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("###", "打印菜类统计异常");
        }
        return "";
    }

    /**
     * 交接班打印
     *
     * @return
     */
    public String printTurnOverOrder(TurnoverHistoryEntity turnoverHistoryEntity) {
        String startTime = turnoverHistoryEntity.getTurnoverStartTime(), endTime = turnoverHistoryEntity.getTurnoverEndTime();
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        String turnOverTime = turnoverHistoryEntity.getStartTurnoverTime();
        String cashierName = turnoverHistoryEntity.getCashierName();
        String areaTypeName = turnoverHistoryEntity.getAreaType();
        String[] payModeEntities, dishTypeCollectionItemBeens;
        try {
            payModeEntities = turnoverHistoryEntity.getPayments().split("`");
            dishTypeCollectionItemBeens = turnoverHistoryEntity.getDishTypes().split("`");
        } catch (Exception e) {
            payModeEntities = new String[0];
            dishTypeCollectionItemBeens = new String[0];
        }
        try {
            SendDataByte(Command.ESC_Init);
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte((CustomMethod.getPrintStr(partnerName, 2, 32)).getBytes("GB2312"));
            SendDataByte((CustomMethod.getPrintStr("交接班账单", 2, 32)).getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(("交接班时间：" + turnOverTime + "\n").getBytes("GB2312"));
            SendDataByte("统计时间段：".getBytes("GB2312"));
            SendDataByte((startTime + "\n").getBytes("GB2312"));
            SendDataByte(("            " + endTime + "\n").getBytes("GB2312"));
            SendDataByte(("收 银 员：" + cashierName + "\n").getBytes("GB2312"));
            SendDataByte(("交接状态：" + turnoverHistoryEntity.getTurnoverState() + "\n").getBytes("GB2312"));
            SendDataByte(("付款方式：所有\n").getBytes("GB2312"));
            SendDataByte(("区    域：" + areaTypeName + "\n").getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("账单总数", turnoverHistoryEntity.getOrderTotalCount(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("已结账账单总数", turnoverHistoryEntity.getOrderedTotalCount(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("已结账账单金额", turnoverHistoryEntity.getOrderedTotalMoney(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("未结账账单总数", turnoverHistoryEntity.getUnOrderedTotalCount(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("未结账账单金额", turnoverHistoryEntity.getUnOrderedTotalMoney(), 1, 32).getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("----支付详情----\n".getBytes("GB2312"));
            if (payModeEntities.length > 1) {
                for (int i = 0; i < payModeEntities.length; ) {
                    SendDataByte(CustomMethod.getPrintStr(payModeEntities[i], payModeEntities[i + 1], 1, 32).getBytes("GB2312"));
                    i += 2;
                }
            } else {
                SendDataByte(CustomMethod.getPrintStr("暂无数据", 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            SendDataByte("菜品分类        数量        合计\n".getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            if (dishTypeCollectionItemBeens.length > 2) {
                for (int i = 0; i < dishTypeCollectionItemBeens.length; ) {
                    SendDataByte((dishTypeCollectionItemBeens[i] + "\n").getBytes("GB2312"));
                    SendDataByte(CustomMethod.getPrintStr("                " + dishTypeCollectionItemBeens[i + 1], "" + dishTypeCollectionItemBeens[i + 2], 1, 32).getBytes("GB2312"));
                    i += 3;
                }
            } else {
                SendDataByte(CustomMethod.getPrintStr("暂无数据", 1, 32).getBytes("GB2312"));
            }
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x01;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(CustomMethod.getPrintStr("消费合计", turnoverHistoryEntity.getMoney0(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("付款合计", turnoverHistoryEntity.getMoney1(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("赠菜合计", turnoverHistoryEntity.getMoney2(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("抹零合计", turnoverHistoryEntity.getMoney3(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("折扣合计", turnoverHistoryEntity.getMoney4(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("不吉利尾数合计", turnoverHistoryEntity.getMoney5(), 1, 32).getBytes("GB2312"));
            SendDataByte(CustomMethod.getPrintStr("优惠券合计", turnoverHistoryEntity.getMoney6(), 1, 32).getBytes("GB2312"));
            SendDataByte("--------------------------------\n".getBytes("GB2312"));
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(("操作员：" + turnoverHistoryEntity.getOperatorName() + "\n").getBytes("GB2312"));
            SendDataByte(("打印时间：" + CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "\n").getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
            SendDataByte("\n".getBytes("GB2312"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("###", "打印菜类统计异常");
        }
        return "";
    }

    /****************************************************************************************************/
    private void SendDataByte(byte[] data) {
        try {
            if (data.length > 0)
                usbCtrl.sendByte(data, dev);
            Log.d("USBDEVICE", "DEV:" + dev.toString());
        } catch (Exception e) {
            Log.d("USBDEVICE", "SendDataByte: " + e.toString());
        }
    }

    private void SendDataString(String data) {
        if (data.length() > 0)
            usbCtrl.sendMsg(data, "GBK", dev);
    }

    private void printBitmap(Bitmap bitmap) {
        try {
            String txt_msg = "";
            Bitmap bmp = Other.createAppIconText(bitmap, txt_msg, 24, true, 200);
            byte[] buffer = PrinterCommand.POS_Set_PrtInit();
            byte[] sp = PrinterCommand.POS_Set_LineSpace(0);
            int nMode = 0;
            int nPaperWidth = 384;
            if (bmp != null) {
                usbCtrl.sendByte(buffer, dev);
                usbCtrl.sendByte(sp, dev);
                byte[] data = PrintPicture.POS_PrintBMP(bmp, nPaperWidth, nMode);
                usbCtrl.sendByte(data, dev);
                usbCtrl.sendByte(new byte[]{0x1b, 0x4a, 0x30, 0x1d, 0x56, 0x42, 0x01}, dev);
            } else {
                Log.d("###", "图片转换失败");
            }

            if (bitmap != null && bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            if (bmp != null && bmp.isRecycled()) {
                bmp.recycle();
                bmp = null;
            }
        } catch (Exception e) {

        }
    }

    private void Print_BMP() {
        final String filePath = CustomMethod.getFileRoot(mContext) + File.separator
                + "qr_order_pay" + ".png";
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        byte[] buffer = PrinterCommand.POS_Set_PrtInit();
        int nMode = 0;
        int nPaperWidth = 320;
        if (bitmap != null) {
            byte[] data = PrintPicture.POS_PrintBMP(bitmap, nPaperWidth, nMode);
            usbCtrl.sendByte(buffer, dev);
            usbCtrl.sendByte(data, dev);
            usbCtrl.sendByte(new byte[]{0x1b, 0x4a, 0x30, 0x1d, 0x56, 0x42, 0x01, 0x0a, 0x1b, 0x40}, dev);
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

}
