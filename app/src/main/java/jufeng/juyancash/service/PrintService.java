package jufeng.juyancash.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.DishModel;
import jufeng.juyancash.bean.DishTypeCollectionItemBean;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.BillAccountHistoryEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.dao.PrintCashierEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.TurnoverHistoryEntity;
import jufeng.juyancash.printer.NetPrinter;
import jufeng.juyancash.printer.OpenQianXiangRunnable;
import jufeng.juyancash.printer.PrintAccountRunnable;
import jufeng.juyancash.printer.PrintCaiwulianRunnable;
import jufeng.juyancash.printer.PrintCashierCollectionRunnable;
import jufeng.juyancash.printer.PrintOrderCollectionRunnable;
import jufeng.juyancash.printer.PrintQianTaiRunnable;
import jufeng.juyancash.printer.PrintStatisticRunnable;
import jufeng.juyancash.printer.PrintThread;
import jufeng.juyancash.printer.PrintTurnOverOrderRunnable;
import jufeng.juyancash.printer.PrintTurnOverOrderRunnable1;
import jufeng.juyancash.printer.PrintTypeCollectionRunnable;
import jufeng.juyancash.printer.PrintWaimaiOrderRunnable;
import jufeng.juyancash.printer.USBPrinter;
import jufeng.juyancash.printer.UsbPrintThread;

/**
 * Created by Administrator102 on 2016/9/9.
 */
public class PrintService extends Service {
    private ConcurrentHashMap<String, NetPrinter> mPrinterMap;//打印机map
    private IBinder mIBinder = new PrintBinder();
    private ScheduledExecutorService service;
    private PrintThread mPrintThread;
    private UsbPrintThread mUsbPrintThread;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    class GetDataAsyncTask extends AsyncTask<String,Integer,Object>{
        private Context mContext;

        public GetDataAsyncTask(Context context){
            this.mContext = context;
        }

        @Override
        protected Object doInBackground(String... params) {
            mPrinterMap = new ConcurrentHashMap<>();
            ArrayList<PrintKitchenEntity> printKitchenEntities = DBHelper.getInstance(getApplicationContext()).getAllKichenPrinter();
            for (PrintKitchenEntity printKitchen :
                    printKitchenEntities) {
                if (!mPrinterMap.containsKey(printKitchen.getPrintKitchenIp())) {
                    NetPrinter printer = new NetPrinter();
                    mPrinterMap.put(printKitchen.getPrintKitchenIp(), printer);
                }
            }
            PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
            if (printCashierEntity != null) {
                if (printCashierEntity.getPrintType() == 1) {
                    //前台打印机
                    if (!mPrinterMap.containsKey(printCashierEntity.getPrintIp())) {
                        NetPrinter printer = new NetPrinter();
                        mPrinterMap.put(printCashierEntity.getPrintIp(), printer);
                    }
                }
                if (printCashierEntity.getVoucherType() == 1) {
                    //消费底联打印机
                    if (!mPrinterMap.containsKey(printCashierEntity.getVoucherIp())) {
                        NetPrinter printer = new NetPrinter();
                        mPrinterMap.put(printCashierEntity.getVoucherIp(), printer);
                    }
                }
                if (printCashierEntity.getHuacaiType() == 1) {
                    //消费底联打印机
                    if (!mPrinterMap.containsKey(printCashierEntity.getHuacaiIp())) {
                        NetPrinter printer = new NetPrinter();
                        mPrinterMap.put(printCashierEntity.getHuacaiIp(), printer);
                    }
                }
                if (printCashierEntity.getPrintDishType() == 1) {
                    //消费底联打印机
                    if (!mPrinterMap.containsKey(printCashierEntity.getPrintDishIp())) {
                        NetPrinter printer = new NetPrinter();
                        mPrinterMap.put(printCashierEntity.getPrintDishIp(), printer);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            service = Executors.newScheduledThreadPool(2);
//            mPrintThread = new PrintThread(mContext, mPrinterMap);
//            mUsbPrintThread = new UsbPrintThread(mContext);
//            service.scheduleWithFixedDelay(mPrintThread, 0, 2, TimeUnit.SECONDS);
//            service.scheduleWithFixedDelay(mUsbPrintThread, 0, 2, TimeUnit.SECONDS);
        }
    }

    public void startPrintThread(Context context) {
        new GetDataAsyncTask(context).execute();
    }

    public void addRunnable(String ip, PrintBean printBean) {
        if(ip.equals("usb")){
            if(mUsbPrintThread != null){
                mUsbPrintThread.addPrintBean(ip,printBean);
            }
        }else{
            if (mPrintThread != null) {
                mPrintThread.addPrintBean(ip, printBean);
            }
        }
    }

    public void qiantaiPrintDish(final ArrayList<PrintDishBean> printDishBeenes, final String orderId, final boolean isAddDish) {
        addRunnable("usb", new PrintBean() {
            private USBPrinter mUSBPrinter;

            @Override
            public String call() {
                return mUSBPrinter.printQiantaiDish(printDishBeenes, orderId, isAddDish);
            }

            @Override
            public void setNetPrinter(NetPrinter printer) {

            }

            @Override
            public void printSuccess() {
                Log.d("###", "USB打印成功");
            }

            @Override
            public void printFailed() {
                Log.d("###", "USB打印失败");
            }

            public void setUsbPrinter(USBPrinter USBPrinter) {
                mUSBPrinter = USBPrinter;
            }
        });
    }

    //usb打印消费底联
    public void usbPrintXFDL(final ArrayList<PrintDishBean> printDishBeenes, final String orderId) {
        addRunnable("usb", new PrintBean() {
            private USBPrinter mUSBPrinter;

            @Override
            public String call() {
                return mUSBPrinter.Print_XFDL(printDishBeenes, orderId);
            }

            @Override
            public void setNetPrinter(NetPrinter printer) {

            }

            @Override
            public void printSuccess() {
                Log.d("###", "前台打印成功");
            }

            @Override
            public void printFailed() {
                Log.d("###", "前台打印失败");
            }

            public void setUsbPrinter(USBPrinter USBPrinter) {
                mUSBPrinter = USBPrinter;
            }
        });
    }

    //usb打印划菜联
    public void usbPrintHCL(final ArrayList<PrintDishBean> printDishBeenes, final String orderId) {
        addRunnable("usb", new PrintBean() {
            private USBPrinter mUSBPrinter;

            @Override
            public String call() {
                return mUSBPrinter.Print_HCL(printDishBeenes, orderId);
            }

            @Override
            public void setNetPrinter(NetPrinter printer) {

            }

            @Override
            public void printSuccess() {
                Log.d("###", "USB打印成功");
            }

            @Override
            public void printFailed() {
                Log.d("###", "USB打印失败");
            }

            public void setUsbPrinter(USBPrinter USBPrinter) {
                mUSBPrinter = USBPrinter;
            }
        });
    }

    //客单打印即客户联
    public void printKehuLian(final String orderId) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null && printCashierEntity.getPrintType() == 0) {//前台打印机类型
            //usb打印
            addRunnable("usb", new PrintBean() {
                private USBPrinter mUSBPrinter;

                @Override
                public String call() {
                    return mUSBPrinter.printKehulian(orderId, printCashierEntity.getIsPrintPresenter() == 1, printCashierEntity.getIsPrintTuicai() == 1);
                }

                @Override
                public void setNetPrinter(NetPrinter printer) {

                }

                @Override
                public void printSuccess() {
                    Log.d("###", "USB打印成功");
                }

                @Override
                public void printFailed() {
                    Log.d("###", "USB打印失败");
                }

                public void setUsbPrinter(USBPrinter USBPrinter) {
                    mUSBPrinter = USBPrinter;
                }
            });
            if (printCashierEntity.getIsAutoOpenCustomerBox() == 1) {//打印客户联时自动打开钱箱
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.openQianxiangThread();
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            }
        } else {
            //网口打印
            if (printCashierEntity != null && printCashierEntity.getIsPrintVoucher() == 1 && printCashierEntity.getVoucherIp() != null && printCashierEntity.getVoucherIp().length() > 0) {//打印消费联
                addRunnable(printCashierEntity.getPrintIp(), new PrintQianTaiRunnable(getApplicationContext(), orderId, printCashierEntity.getPrintIp(), printCashierEntity.getIsPrintPresenter() == 1, printCashierEntity.getIsPrintTuicai() == 1));
                if (printCashierEntity.getIsAutoOpenCustomerBox() == 1) {//打印客户联时自动打开钱箱
                    addRunnable(printCashierEntity.getPrintIp(), new OpenQianXiangRunnable(this, printCashierEntity.getPrintIp()));
                }
            }
        }
    }

    public void openQianXiang() {
        PrintCashierEntity printCashierEntity = DBHelper.getInstance(this).getPrintCashierEntity();
        if (printCashierEntity != null) {
            if (printCashierEntity.getPrintType() == 0) {
                //前台打印机为usb
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.openQianxiangThread();
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            } else {
                //前台打印机为网口
                if (printCashierEntity.getPrintIp() != null && printCashierEntity.getPrintIp().length() > 0) {
                    addRunnable(printCashierEntity.getPrintIp(), new OpenQianXiangRunnable(this, printCashierEntity.getPrintIp()));
                } else {
                    Toast.makeText(this, "打印机ip地址有误，请检查ip设置", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //打印财务联
    public void printCaiwulian(final String orderId) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null && printCashierEntity.getIsAutoPrintFinance() == 1) {
            if (printCashierEntity.getPrintType() == 0) {//前台打印机类型
                //usb打印
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.printCaiwulian(orderId, printCashierEntity.getIsPrintPresenter() == 1, printCashierEntity.getIsPrintTuicai() == 1);
                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
                if (printCashierEntity.getIsAutoOpenFinanceBox() == 1) {//打印财务联时自动打开钱箱
                    addRunnable("usb", new PrintBean() {
                        private USBPrinter mUSBPrinter;

                        @Override
                        public String call() {
                            return mUSBPrinter.openQianxiangThread();
                        }

                        @Override
                        public void setNetPrinter(NetPrinter printer) {

                        }

                        @Override
                        public void printSuccess() {
                            Log.d("###", "USB打印成功");
                        }

                        @Override
                        public void printFailed() {
                            Log.d("###", "USB打印失败");
                        }

                        public void setUsbPrinter(USBPrinter USBPrinter) {
                            mUSBPrinter = USBPrinter;
                        }
                    });
                }
            } else {
                //网口打印
                if (printCashierEntity != null && printCashierEntity.getIsPrintVoucher() == 1 && printCashierEntity.getVoucherIp() != null && printCashierEntity.getVoucherIp().length() > 0) {//打印消费联
                    addRunnable(printCashierEntity.getPrintIp(), new PrintCaiwulianRunnable(getApplicationContext(), orderId, printCashierEntity.getPrintIp(), printCashierEntity.getIsPrintPresenter() == 1, printCashierEntity.getIsPrintTuicai() == 1));
                    if (printCashierEntity.getIsAutoOpenFinanceBox() == 1) {//打印客户联时自动打开钱箱
                        addRunnable(printCashierEntity.getPrintIp(), new OpenQianXiangRunnable(this, printCashierEntity.getPrintIp()));
                    }
                }
            }
        }
    }

    //打印外卖结账单
    public void printWaimai(final String orderId) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null) {
            if (printCashierEntity.getPrintType() == 0) {//前台打印机类型
                //usb打印
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.printWaimai(orderId, printCashierEntity.getIsPrintPresenter() == 1, printCashierEntity.getIsPrintTuicai() == 1);
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            } else {
                //网口打印
                if (printCashierEntity.getPrintIp() != null && printCashierEntity.getPrintIp().length() > 0) {//打印消费联
                    addRunnable(printCashierEntity.getPrintIp(), new PrintWaimaiOrderRunnable(getApplicationContext(), orderId, printCashierEntity.getPrintIp(), printCashierEntity.getIsPrintPresenter() == 1, printCashierEntity.getIsPrintTuicai() == 1));
                }
            }
        }
    }

    //usb打印催菜单
    public void usbPrintRemind(final ArrayList<PrintDishBean> printDishBeenes, final String orderId) {
        addRunnable("usb", new PrintBean() {
            private USBPrinter mUSBPrinter;

            @Override
            public String call() {
                return mUSBPrinter.printRemind(printDishBeenes, orderId);
            }

            @Override
            public void setNetPrinter(NetPrinter printer) {

            }

            @Override
            public void printSuccess() {
                Log.d("###", "USB打印成功");
            }

            @Override
            public void printFailed() {
                Log.d("###", "USB打印失败");
            }

            public void setUsbPrinter(USBPrinter USBPrinter) {
                mUSBPrinter = USBPrinter;
            }
        });
    }

    //usb打印退菜单
    public void usbPrintRetreat(final ArrayList<PrintDishBean> printDishBeenes, final String orderId, final String employeeName, final String reason) {
        addRunnable("usb", new PrintBean() {
            private USBPrinter mUSBPrinter;

            @Override
            public String call() {
                return mUSBPrinter.printRetreat(printDishBeenes, orderId, employeeName, reason);
            }

            @Override
            public void setNetPrinter(NetPrinter printer) {

            }

            @Override
            public void printSuccess() {
                Log.d("###", "USB打印成功");
            }

            @Override
            public void printFailed() {
                Log.d("###", "USB打印失败");
            }

            public void setUsbPrinter(USBPrinter USBPrinter) {
                mUSBPrinter = USBPrinter;
            }
        });
    }

    /**
     * 打印挂账单
     *
     * @param orderId
     */
    public void printAccountPay(final String orderId, final BillAccountHistoryEntity billAccountHistoryEntity) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null) {
            if (printCashierEntity.getPrintType() == 0) {//前台打印机类型
                //usb打印
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.printAccountPay(orderId, billAccountHistoryEntity, printCashierEntity.getIsPrintPresenter() == 1, printCashierEntity.getIsPrintTuicai() == 1);
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            } else {
                //网口打印
                if (printCashierEntity != null && printCashierEntity.getIsPrintVoucher() == 1 && printCashierEntity.getVoucherIp() != null && printCashierEntity.getVoucherIp().length() > 0) {//打印消费联
                    addRunnable(printCashierEntity.getPrintIp(), new PrintAccountRunnable(getApplicationContext(), orderId, billAccountHistoryEntity, printCashierEntity.getPrintIp(), printCashierEntity.getIsPrintPresenter() == 1, printCashierEntity.getIsPrintTuicai() == 1));
                }
            }
        }
    }

    //打印销售统计单
    public void printStatistic(final int dateType, final DishTypeModel dishTypeModel, final ArrayList<DishModel> dishModels) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null) {
            if (printCashierEntity.getPrintType() == 0) {//前台打印机类型
                //usb打印
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.printStatistic(dateType, dishTypeModel, dishModels);
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            } else {
                //网口打印
                if (printCashierEntity.getPrintIp() != null && printCashierEntity.getPrintIp().length() > 0) {//打印消费联
                    addRunnable(printCashierEntity.getPrintIp(), new PrintStatisticRunnable(getApplicationContext(), dateType, dishTypeModel, dishModels));
                }
            }
        }
    }

    //打印账单统计单
    public void printOrderCollection(final String cashierName, final String isShift, final String payModeName, final int dateType, final String areaTypeName, final Map<String, String> orderMoneyDetail, final ArrayList<PayModeEntity> payModeEntities) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null) {
            if (printCashierEntity.getPrintType() == 0) {//前台打印机类型
                //usb打印
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.printOrderCollection(cashierName, isShift, payModeName, dateType, areaTypeName, orderMoneyDetail, payModeEntities);
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            } else {
                //网口打印
                if (printCashierEntity.getPrintIp() != null && printCashierEntity.getPrintIp().length() > 0) {//打印消费联
                    addRunnable(printCashierEntity.getPrintIp(), new PrintOrderCollectionRunnable(getApplicationContext(), cashierName, isShift, payModeName, dateType, areaTypeName, orderMoneyDetail, payModeEntities));
                }
            }
        }
    }

    //打印收银员账单统计单
    public void printCashierCollection(final String cashierName, final String isShift, final String payModeName, final int dateType, final String areaTypeName, final Map<String, String> orderMoneyDetail, final ArrayList<PayModeEntity> payModeEntities) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null) {
            if (printCashierEntity.getPrintType() == 0) {//前台打印机类型
                //usb打印
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.printCashierCollection(cashierName, isShift, payModeName, dateType, areaTypeName, orderMoneyDetail, payModeEntities);
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            } else {
                //网口打印
                if (printCashierEntity.getPrintIp() != null && printCashierEntity.getPrintIp().length() > 0) {//打印消费联
                    addRunnable(printCashierEntity.getPrintIp(), new PrintCashierCollectionRunnable(getApplicationContext(), cashierName, isShift, payModeName, dateType, areaTypeName, orderMoneyDetail, payModeEntities));
                }
            }
        }
    }

    //打印分类汇总统计单统计单
    public void printTypeCollection(final String cashierName, final String isShift, final String payModeName, final int dateType, final String areaTypeName, final ArrayList<DishTypeCollectionItemBean> dishTypeCollectionItemBeen) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null) {
            if (printCashierEntity.getPrintType() == 0) {//前台打印机类型
                //usb打印
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.printTypeCollection(cashierName, isShift, payModeName, dateType, areaTypeName, dishTypeCollectionItemBeen);
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            } else {
                //网口打印
                if (printCashierEntity.getPrintIp() != null && printCashierEntity.getPrintIp().length() > 0) {//打印消费联
                    addRunnable(printCashierEntity.getPrintIp(), new PrintTypeCollectionRunnable(getApplicationContext(), cashierName, isShift, payModeName, dateType, areaTypeName, dishTypeCollectionItemBeen));
                }
            }
        }
    }

    //打印分类汇总统计单统计单
    public void printTurnOverOrder(final String turnOverTime, final String cashierName, final int dateType, final String areaTypeName, final ArrayList<PayModeEntity> payModeEntities, final ArrayList<DishTypeCollectionItemBean> dishTypeCollectionItemBeens, final Map<String, String> orderMoneyDetail) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null) {
            if (printCashierEntity.getPrintType() == 0) {//前台打印机类型
                //usb打印
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.printTurnOverOrder(turnOverTime, cashierName,dateType, areaTypeName, payModeEntities,dishTypeCollectionItemBeens, orderMoneyDetail);
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            } else {
                //网口打印
                if (printCashierEntity.getPrintIp() != null && printCashierEntity.getPrintIp().length() > 0) {//打印消费联
                    addRunnable(printCashierEntity.getPrintIp(), new PrintTurnOverOrderRunnable(getApplicationContext(), turnOverTime, cashierName,dateType, areaTypeName, payModeEntities,dishTypeCollectionItemBeens, orderMoneyDetail));
                }
            }
        }
    }

    //打印分类汇总统计单统计单
    public void printTurnOverOrder(final TurnoverHistoryEntity turnoverHistoryEntity) {
        final PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
        if (printCashierEntity != null) {
            if (printCashierEntity.getPrintType() == 0) {//前台打印机类型
                //usb打印
                addRunnable("usb", new PrintBean() {
                    private USBPrinter mUSBPrinter;

                    @Override
                    public String call() {
                        return mUSBPrinter.printTurnOverOrder(turnoverHistoryEntity);
                    }

                    @Override
                    public void setNetPrinter(NetPrinter printer) {

                    }

                    @Override
                    public void printSuccess() {
                        Log.d("###", "USB打印成功");
                    }

                    @Override
                    public void printFailed() {
                        Log.d("###", "USB打印失败");
                    }

                    public void setUsbPrinter(USBPrinter USBPrinter) {
                        mUSBPrinter = USBPrinter;
                    }
                });
            } else {
                //网口打印
                if (printCashierEntity.getPrintIp() != null && printCashierEntity.getPrintIp().length() > 0) {//打印消费联
                    addRunnable(printCashierEntity.getPrintIp(), new PrintTurnOverOrderRunnable1(getApplicationContext(), turnoverHistoryEntity));
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(mUsbPrintThread != null){
            mUsbPrintThread.closeUsbPrinter();
        }
        service.shutdown();
        for (Map.Entry<String, NetPrinter> entity :
                mPrinterMap.entrySet()) {
            if (entity.getValue() != null && entity.getValue().getIfOpen()) {
                entity.getValue().Close();
            }
        }
        if (mPrinterMap != null) {
            mPrinterMap.clear();
            mPrinterMap = null;
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class PrintBinder extends Binder {

        public PrintService getService() {
            return PrintService.this;
        }

    }
}
