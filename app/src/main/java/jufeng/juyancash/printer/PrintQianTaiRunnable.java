package jufeng.juyancash.printer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * 打印消费底联
 * Created by Administrator102 on 2016/9/8.
 */
public class PrintQianTaiRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private String mOrderId;
    private String mIP;
    private boolean isPrintPresentDish,isPrintRetreatDish;
    private int mStoreVersion;

    public PrintQianTaiRunnable(Context context, String orderId, String ip,boolean isPrintPresentDish,boolean isPrintRetreatDish) {
        this.mContext = context;
        this.mOrderId = orderId;
        this.mIP = ip;
        this.isPrintPresentDish = isPrintPresentDish;
        this.isPrintRetreatDish = isPrintRetreatDish;
        this.mStoreVersion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
    }

    @Override
    public String call() {
        if(printer != null)
            printData();
        return "success";
    }

    @Override
    public void printSuccess() {
        Log.d("###", "前台打印成功");
    }

    @Override
    public void printFailed() {
        Log.d("###", "前台打印失败");
    }

    @Override
    public void setUsbPrinter(USBPrinter usbPrinter) {

    }

    @Override
    public void setNetPrinter(NetPrinter printer) {
        this.printer = printer;
    }

    public void printData(){
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(mOrderId);
        ArrayList<PrintDishBean> orderDishEntities = DBHelper.getInstance(mContext).queryPrintDish(mOrderId,isPrintPresentDish,isPrintRetreatDish);
        String areaName = DBHelper.getInstance(mContext).getAreaNameById(orderEntity.getAreaId());
        String tableCode = "";
        if(mStoreVersion == 0){
            tableCode = DBHelper.getInstance(mContext).getTableNameByTableId(orderEntity.getTableId());
        }else if(mStoreVersion == 1){
            tableCode = orderEntity.getTableId() == null ? "":orderEntity.getTableId();
        }else if(mStoreVersion == 2){
            tableCode = orderEntity.getTableId() == null ? "":orderEntity.getTableId();
        }
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getCashierId());
        double totalMoney = DBHelper.getInstance(mContext).getBillMoneyByOrderId(mOrderId,1);
        float yhMoney = ((float)DBHelper.getInstance(mContext).getYHMoney(mOrderId))/100;
        double zhMoney = DBHelper.getInstance(mContext).getReceivableMoney(mOrderId,1);
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        payModeEntities.addAll(DBHelper.getInstance(mContext).getPayModeByOrderId(mOrderId));
        String partnerName = mContext.getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerName","来御来");
        printer.Set();
        printer.PrintText(CustomMethod.getPrintStr(partnerName,2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("结账单（客户联）",2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm"),1,48),0,0,0);
        printer.PrintEnter();
        if(mStoreVersion == 0){
            printer.PrintText(CustomMethod.getPrintStr("台号："+areaName+"-"+tableCode,"单号："+String.valueOf(orderEntity.getOrderNumber1()),2,48),0,3,0);
            printer.PrintText(CustomMethod.getPrintStr("帐单号："+orderEntity.getSerialNumber(),"人数："+orderEntity.getOrderGuests(),1,48),0,0,0);
            printer.PrintText(CustomMethod.getPrintStr("服务员："+employeeName,"单位：元",1,48),0,0,0);
        }else if(mStoreVersion == 1){
            printer.PrintText("流水号："+orderEntity.getSerialNumber(),0,0,0);
            printer.PrintEnter();
            printer.PrintText(CustomMethod.getPrintStr("单号："+String.valueOf(orderEntity.getOrderNumber1()),"单位：元",1,48),0,0,0);
        }else if(mStoreVersion == 2){
            printer.PrintText("流水号："+orderEntity.getSerialNumber(),0,0,0);
            printer.PrintEnter();
            printer.PrintText(CustomMethod.getPrintStr("单号："+String.valueOf(orderEntity.getOrderNumber1()),"单位：元",1,48),0,0,0);
        }

        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText("品名                 数量        单价       金额",0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText("-----点菜明细-----",0,0,0);
        printer.PrintEnter();
        for (PrintDishBean orderDish :
                orderDishEntities) {
            if(orderDish.getType() == 0){
                if(orderDish.getStatus() == -2){
                    //赠菜
                    printer.PrintText(("(赠)"+orderDish.getDishName()+orderDish.getDishConfig()),0,0,0);
                    printer.PrintEnter();
                }else if(orderDish.getStatus() == -1){
                    //退菜
                    printer.PrintText(("(退)"+orderDish.getDishName()+orderDish.getDishConfig()),0,0,0);
                }else{
                    printer.PrintText((orderDish.getDishName()+orderDish.getDishConfig()),0,0,0);
                }
                printer.PrintEnter();
                printer.PrintText("    ",0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(4,21,orderDish.getDishCount()+orderDish.getUnitName()+""),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(25,12,String.valueOf(orderDish.getDishUnitPrice())),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(37,11,String.valueOf(orderDish.getDishPrice())),0,0,0);
                printer.PrintEnter();
            }else{
                printer.PrintText(("    "+orderDish.getDishName()+orderDish.getDishConfig()),0,0,0);
                printer.PrintEnter();
                printer.PrintText("    ",0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(4,21,orderDish.getDishCount()+orderDish.getUnitName()+""),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(25,12,String.valueOf(orderDish.getDishUnitPrice())),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(37,11,String.valueOf(orderDish.getDishPrice())),0,0,0);
                printer.PrintEnter();
            }
        }
        if(payModeEntities.size() > 0) {
            printer.PrintText("------------------------------------------------", 0, 0, 0);
        }
        for (PayModeEntity payMode:
                payModeEntities) {
            printer.PrintText(payMode.getPaymentName()+"："+payMode.getPayMoney(),0,0,0);
            printer.PrintEnter();
            if(payMode.getPaymentType() == 0){
                try {
                    String[] realMoney = payMode.getElectricOrderSerial().split("`");
                    printer.PrintText("（实收：" + realMoney[0]+"，找零："+realMoney[1] + "）", 0, 0, 0);
                    printer.PrintEnter();
                }catch (Exception e){

                }
            }
        }
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("总金额",String.valueOf(totalMoney),2,48),0,3,0);
        printer.PrintText(CustomMethod.getPrintStr("优惠金额",String.valueOf(yhMoney),2,48),0,3,0);
        printer.PrintText(CustomMethod.getPrintStr("应付金额",String.valueOf(zhMoney),2,48),0,3,0);
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText(("操作员："+employeeName),0,0,0);
        printer.PrintEnter();
        printer.PrintText(("打印时间："+CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm")),0,0,0);
        printer.PrintEnter();
        printer.CutPage(3);
        printer.OpenBuzzing();
    }
}
