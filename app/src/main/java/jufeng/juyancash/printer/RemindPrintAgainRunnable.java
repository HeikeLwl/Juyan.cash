package jufeng.juyancash.printer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.PrinterFailedHistoryEntity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.util.CustomMethod;

/**
 * 客单分类统计打印
 * Created by Administrator102 on 2016/9/8.
 */
public class RemindPrintAgainRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private String[] mPrintDishBeen;
    private OrderEntity mOrderEntity;
    private String areaName = "";
    private String employeeName = "无";
    private String tableCode = "";
    private PrintKitchenEntity mPrintKitchenEntity;
    private PrinterFailedHistoryEntity mPrinterFailedHistoryEntity;
    private int mStoreVersion;

    public RemindPrintAgainRunnable(Context context, PrinterFailedHistoryEntity printerFailedHistoryEntity, String[] printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, String employeeName) {
        initData(context,printerFailedHistoryEntity,printDishBeenes,orderId,printKitchenEntity,employeeName);
    }

    private void initData(Context context, PrinterFailedHistoryEntity printerFailedHistoryEntity, String[] printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, String employeeName){
        this.mContext = context;
        mPrintDishBeen = printDishBeenes;
        this.mPrintKitchenEntity = printKitchenEntity;
        this.mPrinterFailedHistoryEntity = printerFailedHistoryEntity;
        mOrderEntity = DBHelper.getInstance(context).getOneOrderEntity(orderId);
        this.mStoreVersion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
        if(mStoreVersion == 0){
            areaName = DBHelper.getInstance(context).getAreaNameById(mOrderEntity.getAreaId());
            if(employeeName == null) {
                this.employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
            }else{
                this.employeeName = employeeName;
            }
            tableCode = "-" + DBHelper.getInstance(context).getTableNameByTableId(mOrderEntity.getTableId());
        }else if(mStoreVersion == 1){
            if(employeeName == null) {
                this.employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
            }else{
                this.employeeName = employeeName;
            }
            tableCode = mOrderEntity.getTableId() == null ? "":mOrderEntity.getTableId();
        }else if(mStoreVersion == 2){
            if(employeeName == null) {
                this.employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
            }else{
                this.employeeName = employeeName;
            }
            tableCode = mOrderEntity.getTableId() == null ? "":mOrderEntity.getTableId();
        }
    }

    @Override
    public void printSuccess() {
        Log.d("###", "催菜打印成功");
    }

    @Override
    public void printFailed() {
        Log.d("###", "催菜打印失败");
    }

    @Override
    public String call() {
        if(printer != null && printer.getIfOpen()) {
            printData();
            printHistory(1);
        }else{
            printHistory(0);
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

    public void printHistory(int status){
        if(mPrinterFailedHistoryEntity != null){
            mPrinterFailedHistoryEntity.setPrintStatus(status);
            DBHelper.getInstance(mContext).changePrintHistory(mPrinterFailedHistoryEntity);
        }
        Intent intent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        intent.putExtra("type", 4);
        mContext.sendBroadcast(intent);
    }

    private void printData(){
        printer.Set();
        printer.PrintText(CustomMethod.getPrintStr("催菜单",2,48), 0, 3, 0);
        printer.PrintEnter();
        if(mStoreVersion == 0){
            printer.PrintText(CustomMethod.getPrintStr(areaName+tableCode,"单号："+mOrderEntity.getOrderNumber1(),2,48),0,3,0);
        }else if(mStoreVersion == 1){
            printer.PrintText(CustomMethod.getPrintStr("单号："+mOrderEntity.getOrderNumber1(),2,48),0,3,0);
        }else if(mStoreVersion == 2){
            printer.PrintText(CustomMethod.getPrintStr("单号："+mOrderEntity.getOrderNumber1(),2,48),0,3,0);
        }
        printer.PrintEnter();
        for (int i = 0; i < mPrintDishBeen.length; i++) {
            printer.PrintText(mPrintDishBeen[i],0,3,0);
            printer.PrintEnter();
        }
        printer.PrintText("操作员："+employeeName, 0, 0, 0);
        printer.PrintEnter();
        printer.PrintText("时间："+CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm"), 0, 0, 0);
        printer.PrintEnter();
        printer.CutPage(3);
        printer.OpenBuzzing();
    }

    private void sendMainBroad(){
        Intent intent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        intent.putExtra("type",20);
        mContext.sendBroadcast(intent);
    }
}
