package jufeng.juyancash.printer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * 打印划菜底联
 * Created by Administrator102 on 2016/9/8.
 */
public class PrintHuacaiLianRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private ArrayList<PrintDishBean> mPrintDishBeen;
    private OrderEntity mOrderEntity;
    private String areaName = "";
    private String employeeName = "无";
    private String tableCode = "";
    private String serailNumber = "";
    private double mTotalMoney = 0.0f;
    private String mTitle = "";
    private String mIP = "";
    private String parnterName = "";
    private int mStoreVersion;

    public PrintHuacaiLianRunnable(Context context, ArrayList<PrintDishBean> printDishBeenes, String orderId, String ip) {
        this.mContext = context;
        parnterName = context.getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerName","");
        mPrintDishBeen = printDishBeenes;
        this.mTitle = "点菜单(划菜联)";
        this.mIP = ip;
        mOrderEntity = DBHelper.getInstance(context).getOneOrderEntity(orderId);
        serailNumber = mOrderEntity.getSerialNumber();
        double mTotalMoney = 0.00;
        for (PrintDishBean printDishBean :
                printDishBeenes) {
            mTotalMoney = jufeng.juyancash.util.AmountUtils.add(printDishBean.getDishPrice(),mTotalMoney);
        }
        this.mStoreVersion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
        if(mStoreVersion == 0){
            if (mOrderEntity.getOrderType() == 1) {
                areaName = "外卖";
                tableCode = "";
            } else {
                areaName = DBHelper.getInstance(context).getAreaNameById(mOrderEntity.getAreaId());
                employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
                tableCode = "-" + DBHelper.getInstance(context).getTableNameByTableId(mOrderEntity.getTableId());
            }
        }else if(mStoreVersion == 1){
            if (mOrderEntity.getOrderType() == 1) {
                areaName = "外卖";
                tableCode = "";
            } else {
                employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
                tableCode = mOrderEntity.getTableId() == null ? "":mOrderEntity.getTableId();
            }
        }else if(mStoreVersion == 2){
            if (mOrderEntity.getOrderType() == 1) {
                areaName = "外卖";
                tableCode = "";
            } else {
                employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
                tableCode = mOrderEntity.getTableId() == null ? "":mOrderEntity.getTableId();
            }
        }

    }

    @Override
    public void printSuccess() {
        Log.d("###", "划菜联打印成功");
    }

    @Override
    public void printFailed() {
        Log.d("###", "划菜联打印失败");
    }

    @Override
    public String call() {
        if(printer != null)
            printData();
        return "success";
    }

    @Override
    public void setNetPrinter(NetPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void setUsbPrinter(USBPrinter usbPrinter) {

    }

    private void printData(){
        printer.Set();
        printer.PrintText(CustomMethod.getPrintStr(parnterName,2,48), 0, 3, 0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr(mTitle,2,48), 0, 3, 0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm"),1,48),0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        if(mStoreVersion == 0) {
            printer.PrintText("台号：",0,0,0);
            printer.PrintText(areaName+tableCode, 0, 3, 0);
            printer.PrintEnter();
            printer.PrintText("单号：",0,0,0);
            printer.PrintText(String.valueOf(mOrderEntity.getOrderNumber()), 0, 3, 0);
            printer.PrintEnter();
            printer.PrintText(CustomMethod.getPrintStr("帐单号：" + serailNumber, "人数：" + mOrderEntity.getOrderGuests(), 1, 48), 0, 0, 0);
            printer.PrintText(CustomMethod.getPrintStr("服务员：" + employeeName, "单位：元", 1, 48), 2, 0, 0);
        }else if(mStoreVersion == 1){
            printer.PrintText(tableCode, 0, 3, 0);
            printer.PrintEnter();
            printer.PrintText("流水号：" + serailNumber, 0, 0, 0);
            printer.PrintEnter();
            printer.PrintText(CustomMethod.getPrintStr("单号：" + String.valueOf(mOrderEntity.getOrderNumber1()), "单位：元", 1, 48), 2, 0, 0);
        }
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText("品名                    数量                金额",0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText("-----点菜明细-----",0,0,0);
        printer.PrintEnter();
        for (PrintDishBean printDishBean :
                mPrintDishBeen) {
            printer.PrintText(printDishBean.getDishName(), 0, 0, 0);
            if(printDishBean.getDishConfig().length() > 0){
                printer.PrintText(printDishBean.getDishConfig(),0,0,0);
            }
            printer.PrintEnter();
            printer.PrintText("    ",0,0,0);
            printer.PrintText(CustomMethod.getPrintStr(4,24,printDishBean.getDishCount()+printDishBean.getUnitName()+""),0,0,0);
            printer.PrintText(CustomMethod.getPrintStr(28,20,String.valueOf(printDishBean.getDiscountMoney())),0,0,0);
            printer.PrintEnter();
        }
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("合计",String.valueOf(mTotalMoney),2,48),0,3,0);
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText("打印时间："+CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm"),0,0,0);
        printer.PrintText("     ",0,0,0);
        printer.PrintEnter();
        printer.PrintText("     ",0,0,0);
        printer.PrintEnter();
        printer.PrintText("     ",0,0,0);
        printer.PrintEnter();
        printer.CutPage(2);
        printer.OpenBuzzing();
        Log.d("###", "打印划菜联结束");
    }

}
