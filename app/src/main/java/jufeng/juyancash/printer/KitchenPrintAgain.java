package jufeng.juyancash.printer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.PrinterFailedHistoryEntity;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/12/3.
 */

public class KitchenPrintAgain implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private String[] mPrintDishBeen;
    private OrderEntity mOrderEntity;
    private String areaName = "";
    private String employeeName = "无";
    private String tableCode = "";
    private PrintKitchenEntity mPrintKitchenEntity;
    private boolean mIsAddDish;
    private String title;
    private PrinterFailedHistoryEntity mPrinterFailedHistoryEntity;
    private String mark;
    private int mStoreVersion;

    public KitchenPrintAgain(Context context, PrinterFailedHistoryEntity printerFailedHistoryEntity, String[] printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, boolean isAddDish){
        initData(context,printerFailedHistoryEntity,printDishBeenes,orderId,printKitchenEntity,isAddDish);
    }

    private void initData(Context context, PrinterFailedHistoryEntity printerFailedHistoryEntity, String[] printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, boolean isAddDish){
        this.mContext = context;
        mPrintDishBeen = printDishBeenes;
        this.mIsAddDish = isAddDish;
        this.mPrintKitchenEntity = printKitchenEntity;
        this.mPrinterFailedHistoryEntity = printerFailedHistoryEntity;
        mStoreVersion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
        try {
            mOrderEntity = DBHelper.getInstance(context).getOneOrderEntity(orderId);
            if(mStoreVersion == 0){
                if (mOrderEntity.getOrderType() == 1) {
                    title = "外卖-配菜单";
                    areaName = "外卖";
                    tableCode = "";
                    TakeOutOrderEntity takeOutOrderEntity = DBHelper.getInstance(mContext).getTakeOutOrderById(orderId);
                    if(takeOutOrderEntity != null && takeOutOrderEntity.getTakeoutMark()!=null){
                        mark = takeOutOrderEntity.getTakeoutMark();
                    }
                } else {
                    title = "配菜单";
                    areaName = DBHelper.getInstance(context).getAreaNameById(mOrderEntity.getAreaId());
                    employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = "-" + DBHelper.getInstance(context).getTableNameByTableId(mOrderEntity.getTableId());
                }
            }else if(mStoreVersion == 1){
                if (mOrderEntity.getOrderType() == 1) {
                    title = "外卖-配菜单";
                    areaName = "外卖";
                    tableCode = "";
                    TakeOutOrderEntity takeOutOrderEntity = DBHelper.getInstance(mContext).getTakeOutOrderById(orderId);
                    if(takeOutOrderEntity != null && takeOutOrderEntity.getTakeoutMark()!=null){
                        mark = takeOutOrderEntity.getTakeoutMark();
                    }
                } else {
                    title = "配菜单";
                    employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = mOrderEntity.getTableId() == null ? "":mOrderEntity.getTableId();
                }
            }else if(mStoreVersion == 2){
                if (mOrderEntity.getOrderType() == 1) {
                    title = "外卖-配菜单";
                    areaName = "外卖";
                    tableCode = "";
                    TakeOutOrderEntity takeOutOrderEntity = DBHelper.getInstance(mContext).getTakeOutOrderById(orderId);
                    if(takeOutOrderEntity != null && takeOutOrderEntity.getTakeoutMark()!=null){
                        mark = takeOutOrderEntity.getTakeoutMark();
                    }
                } else {
                    title = "配菜单";
                    employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
                    tableCode = mOrderEntity.getTableId() == null ? "":mOrderEntity.getTableId();
                }
            }
        }catch (NullPointerException e){

        }
    }

    @Override
    public String call() {
        if(printer != null && printer.getIfOpen()) {
            for (int i = 0; i < mPrintKitchenEntity.getPrintCount() + 1; i++) {
                if (mPrintKitchenEntity.getIsOneDishOneCut() == 1) {
                    printOneDao();
                    if (mPrintKitchenEntity.getIsPrintTotalOrder() == 1) {
                        printData();
                    }
                } else {
                    printData();
                }
            }
            printHistoty(1);
        }else{
            printHistoty(0);
        }
        return "success";
    }

    @Override
    public void setNetPrinter(NetPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void setUsbPrinter(USBPrinter usbPrinter) {

    }

    public void printHistoty(int status){
        if(mPrinterFailedHistoryEntity != null){
            mPrinterFailedHistoryEntity.setPrintStatus(status);
            DBHelper.getInstance(mContext).changePrintHistory(mPrinterFailedHistoryEntity);
        }
        Intent intent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        intent.putExtra("type", 4);
        mContext.sendBroadcast(intent);
    }

    private void printOneDao(){
        for (int i = 0; i < mPrintDishBeen.length; i++) {
            printer.Set();
            if(mIsAddDish){
                printer.PrintText(CustomMethod.getPrintStr(title+"(加菜)",2,48), 0, 3, 0);
            }else{
                printer.PrintText(CustomMethod.getPrintStr(title,2,48), 0, 3, 0);
            }
            printer.PrintEnter();
            if(mStoreVersion == 0){
                printer.PrintText(CustomMethod.getPrintStr(areaName + tableCode,"单号：" + mOrderEntity.getOrderNumber1(),2,48), 0, 3, 0);
                printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"),"服务员：" + employeeName,1,48), 0, 0, 0);
            }else if(mStoreVersion == 1){
                printer.PrintText(CustomMethod.getPrintStr("服务员：" + employeeName,"单号：" + mOrderEntity.getOrderNumber1(),2,48), 0, 3, 0);
                printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"),1,48), 0, 0, 0);
            }else if(mStoreVersion == 2){
                printer.PrintText(CustomMethod.getPrintStr("服务员：" + employeeName,"单号：" + mOrderEntity.getOrderNumber1(),2,48), 0, 3, 0);
                printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"),1,48), 0, 0, 0);
            }
            printer.PrintText(mPrintDishBeen[i],0,3,0);
            printer.PrintEnter();
            printer.CutPage(3);
        }
    }

    private void printData(){
        printer.Set();
        if(mIsAddDish) {
            printer.PrintText(CustomMethod.getPrintStr(title+"(加菜)",2,48), 0, 3, 0);
        }else{
            printer.PrintText(CustomMethod.getPrintStr(title,2,48), 0, 3, 0);
        }
        printer.PrintEnter();
        if(mStoreVersion == 0){
            printer.PrintText(CustomMethod.getPrintStr(areaName + tableCode,"单号：" + mOrderEntity.getOrderNumber1(),2,48), 0, 3, 0);
            printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"),"服务员：" + employeeName,1,48), 0, 0, 0);
        }else if(mStoreVersion == 1){
            printer.PrintText(CustomMethod.getPrintStr("服务员：" + employeeName,"单号：" + mOrderEntity.getOrderNumber1(),2,48), 0, 3, 0);
            printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"),1,48), 0, 0, 0);
        }else if(mStoreVersion == 2){
            printer.PrintText(CustomMethod.getPrintStr("服务员：" + employeeName,"单号：" + mOrderEntity.getOrderNumber1(),2,48), 0, 3, 0);
            printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"),1,48), 0, 0, 0);
        }
        if(mOrderEntity.getOrderType() == 1){
            //外卖单
            printer.PrintText("备注："+mark,0,0,0);
        }
        for (int i = 0; i < mPrintDishBeen.length; i++) {
            printer.PrintText(mPrintDishBeen[i],0,3,0);
            printer.PrintEnter();
        }
        printer.PrintText(CustomMethod.getPrintStr(areaName + tableCode,2,48), 0, 3, 0);
        printer.PrintEnter();
        printer.CutPage(3);
        printer.OpenBuzzing();
    }

    @Override
    public void printFailed() {
        Log.d("###", "厨打失败");
    }

    @Override
    public void printSuccess() {
        Log.d("###", "厨打成功");
    }

    private void sendMainBroad(){
        Intent intent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        intent.putExtra("type",20);
        mContext.sendBroadcast(intent);
    }
}
