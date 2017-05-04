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
 * 客单分类统计打印
 * Created by Administrator102 on 2016/9/8.
 */
public class QiantaiRemindPrintRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private ArrayList<PrintDishBean> mPrintDishBeen;
    private OrderEntity mOrderEntity;
    private String areaName = "";
    private String employeeName = "无";
    private String tableCode = "";
    private String mIp;
    private int mStoreVersion;

    public QiantaiRemindPrintRunnable(Context context, ArrayList<PrintDishBean> printDishBeenes, String orderId, String ip, String employeeName) {
        initData(context,printDishBeenes,orderId,ip,employeeName);
    }

    private void initData(Context context, ArrayList<PrintDishBean> printDishBeenes, String orderId,String ip, String employeeName){
        this.mContext = context;
        mPrintDishBeen = printDishBeenes;
        this.mIp = ip;
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
            tableCode = mOrderEntity.getTableId() == null ? "" : mOrderEntity.getTableId();
        }else if(mStoreVersion == 2){
            if(employeeName == null) {
                this.employeeName = DBHelper.getInstance(context).getEmployeeNameById(mOrderEntity.getCashierId());
            }else{
                this.employeeName = employeeName;
            }
            tableCode = mOrderEntity.getTableId() == null ? "" : mOrderEntity.getTableId();
        }

    }

    @Override
    public void printSuccess() {
        Log.d("###", "前台催菜打印成功");
    }

    @Override
    public void printFailed() {
        Log.d("###", "前台催菜打印失败");
    }

    @Override
    public String call() {
//        boolean ifOpen = false;
//        int status = 0;
//        printer = new NetPrinter();
//        ifOpen = printer.Open(mIp, 9100);
//        if (ifOpen) {
//            status = 1;
        if(printer != null)
            printData();
//            printer.Close();
//        }else{
//            return null;
//        }
        return "success";
    }

    @Override
    public void setUsbPrinter(USBPrinter usbPrinter) {

    }

    @Override
    public void setNetPrinter(NetPrinter printer) {
        this.printer = printer;
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
        for (int i = 0; i < mPrintDishBeen.size(); i++) {
            PrintDishBean prePrintDishBean = mPrintDishBeen.get(i-1 < 0 ? 0:i-1);
            PrintDishBean printDishBean = mPrintDishBeen.get(i);
            if(printDishBean.getType() == 1){
                //套餐
                if(prePrintDishBean.getType() == 1 && printDishBean.getParentBean().getOrderDishId().equals(prePrintDishBean.getParentBean().getOrderDishId())){
                    //当前套餐内商品和前面的套餐内商品属于同一个套餐
                    printer.PrintText("    (催)" + printDishBean.getDishCount() + printDishBean.getUnitName() + " X " + printDishBean.getDishName(), 0, 2, 0);
                    printer.PrintEnter();
                    if (printDishBean.getDishConfig().length() > 0) {
                        printer.PrintText("    " + printDishBean.getDishConfig(), 0, 2, 0);
                        printer.PrintEnter();
                    }
                }else {
                    printer.PrintText("(催)" + printDishBean.getParentBean().getDishName() + printDishBean.getParentBean().getDishCount() + printDishBean.getUnitName(), 0, 3, 0);
                    printer.PrintEnter();
                    printer.PrintText("    (催)" + printDishBean.getDishName() + printDishBean.getDishCount() + printDishBean.getUnitName(), 0, 2, 0);
                    printer.PrintEnter();
                    if (printDishBean.getDishConfig().length() > 0) {
                        printer.PrintText("    " + printDishBean.getDishConfig(), 0, 2, 0);
                        printer.PrintEnter();
                    }
                }
            }else{
                printer.PrintText("（催）"+printDishBean.getDishName(), 0, 3, 0);
                printer.PrintText(printDishBean.getDishCount()+printDishBean.getUnitName(),0,3,0);
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
