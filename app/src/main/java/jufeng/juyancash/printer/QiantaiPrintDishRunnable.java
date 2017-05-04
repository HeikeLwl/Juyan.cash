package jufeng.juyancash.printer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2016/12/3.
 */

public class QiantaiPrintDishRunnable implements PrintBean{
    private NetPrinter printer;
    private Context mContext;
    private ArrayList<PrintDishBean> mPrintDishBeen;
    private OrderEntity mOrderEntity;
    private String areaName = "";
    private String employeeName = "无";
    private String tableCode = "";
    private boolean mIsAddDish;
    private String mIp;
    private String title;
    private int mStoreVersion;

    public QiantaiPrintDishRunnable(Context context, ArrayList<PrintDishBean> printDishBeenes, String orderId,String ip, boolean isAddDish){
        this.mContext = context;
        mPrintDishBeen = new ArrayList<>();
        mPrintDishBeen.addAll(printDishBeenes);
        this.mIp = ip;
        this.mIsAddDish = isAddDish;
        mStoreVersion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
        try {
            mOrderEntity = DBHelper.getInstance(context).getOneOrderEntity(orderId);
            if(mStoreVersion == 0){
                if (mOrderEntity.getOrderType() == 1) {
                    title = "外卖-配菜单";
                    areaName = "外卖";
                    tableCode = "";
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
    public void printSuccess() {
        Log.d("###", "前台商品打印成功");
    }

    @Override
    public void printFailed() {
        Log.d("###", "前台商品打印失败");
    }

    @Override
    public String call() {
        if(printer != null)
            printData();

//        DBHelper.getInstance(mContext).insertPrintHistory(mOrderEntity, null, status);
//        Intent intent = new Intent(MainActivity.ACTION_INTENT_MAIN);
//        intent.putExtra("type", 4);
//        mContext.sendBroadcast(intent);
//        sendMainBroad();
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
        if(mIsAddDish) {
            printer.PrintText(CustomMethod.getPrintStr(title + "(加菜)",2,48), 0, 3, 0);
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
        for (PrintDishBean printDishBean :
                mPrintDishBeen) {
            if(printDishBean.getType() == 1){
                //套餐
                printer.PrintText(printDishBean.getParentBean().getDishCount()+printDishBean.getUnitName()+" X "+printDishBean.getParentBean().getDishName(),0,3,0);
                printer.PrintEnter();
                printer.PrintText("    "+printDishBean.getDishCount()+printDishBean.getUnitName()+" X "+printDishBean.getDishName(), 0, 2, 0);
                printer.PrintEnter();
                if(printDishBean.getDishConfig().length() > 0){
                    printer.PrintText("    "+printDishBean.getDishConfig(),0,2,0);
                    printer.PrintEnter();
                }
            }else{
                printer.PrintText(printDishBean.getDishCount()+printDishBean.getUnitName()+" X "+printDishBean.getDishName(), 0, 3, 0);
                printer.PrintEnter();
                if(printDishBean.getDishConfig().length() > 0){
                    printer.PrintText(printDishBean.getDishConfig(),0,3,0);
                    printer.PrintEnter();
                }
                if(printDishBean.getMark() != null && printDishBean.getMark().length() > 0){
                    printer.PrintText("备注："+printDishBean.getMark(),0,1,0);
                    printer.PrintEnter();
                }
                if(printDishBean.getDishSelectedMaterialEntities() != null && printDishBean.getDishSelectedMaterialEntities().length() > 0){
                    printer.PrintText("加料："+printDishBean.getDishSelectedMaterialEntities(),0,2,0);
                    printer.PrintEnter();
                }
            }
        }
        printer.PrintText(CustomMethod.getPrintStr(areaName + tableCode,2,48), 0, 3, 0);
        printer.PrintEnter();
        printer.CutPage(2);
        printer.OpenBuzzing();
    }

    private void sendMainBroad(){
        Intent intent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        intent.putExtra("type",20);
        mContext.sendBroadcast(intent);
    }
}
