package jufeng.juyancash.printer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * 打印外卖单
 * Created by Administrator102 on 2016/9/8.
 */
public class PrintWaimaiOrderRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private String mOrderId;
    private String mIP;
    private boolean isPrintPresentDish,isPrintRetreatDish;
    private int mStoreVersion;

    public PrintWaimaiOrderRunnable(Context context, String orderId, String ip, boolean isPrintPresentDish, boolean isPrintRetreatDish) {
        this.mContext = context;
        this.mOrderId = orderId;
        this.mIP = ip;
        this.isPrintPresentDish = isPrintPresentDish;
        this.isPrintRetreatDish = isPrintRetreatDish;
        this.mStoreVersion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
    }
    @Override
    public void printSuccess() {
        Log.d("###", "外卖打印成功");
    }

    @Override
    public void printFailed() {
        Log.d("###", "外卖打印失败");
    }


    @Override
    public String call() {
        if(printer != null) {
            OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(mOrderId);
            TakeOutOrderEntity takeOutOrderEntity = DBHelper.getInstance(mContext).getTakeOutOrderById(mOrderId);
            if (orderEntity != null && takeOutOrderEntity != null) {
                printData(orderEntity, takeOutOrderEntity);
            }
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

    public void printData(OrderEntity orderEntity, TakeOutOrderEntity takeOutOrderEntity){
        ArrayList<PrintDishBean> orderDishEntities = DBHelper.getInstance(mContext).queryPrintDish(mOrderId,isPrintPresentDish,isPrintRetreatDish);
        String areaName = "外卖";
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getCashierId());
        String sendTime = CustomMethod.parseTime(takeOutOrderEntity.getTakeoutTime(),"yyyy-MM-dd HH:mm");
        String sendAddress = takeOutOrderEntity.getTakeoutAddress();
        String phone = takeOutOrderEntity.getGuestPhone();
        String name = takeOutOrderEntity.getGuestName();
        String[] marks = null;
        try{
            marks = takeOutOrderEntity.getTakeoutMark().split("`");
        }catch (Exception e){

        }
        String totalMoney = AmountUtils.changeF2Y(orderEntity.getTotalMoney());
        String yhMoney = AmountUtils.changeF2Y(orderEntity.getDiscountTotalMoney());
        String zhMoney = AmountUtils.changeF2Y(orderEntity.getCloseMoney());
        String incomeMoney = AmountUtils.changeF2Y(orderEntity.getCloseMoney());
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        payModeEntities.addAll(DBHelper.getInstance(mContext).getPayModeByOrderId(mOrderId));
        String partnerName = mContext.getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerName","来御来");
        printer.Set();
        printer.PrintText(CustomMethod.getPrintStr(partnerName,2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("结账单",2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm"),1,48),0,0,0);
        printer.PrintEnter();
        if(mStoreVersion == 0){
            printer.PrintText(CustomMethod.getPrintStr("台号："+areaName,"单号："+String.valueOf(orderEntity.getOrderNumber1()),2,48),0,3,0);
        }else if(mStoreVersion == 1){
            printer.PrintText(CustomMethod.getPrintStr("单号："+String.valueOf(orderEntity.getOrderNumber1()),2,48),0,3,0);
        }else if(mStoreVersion == 2){
            printer.PrintText(CustomMethod.getPrintStr("单号："+String.valueOf(orderEntity.getOrderNumber1()),2,48),0,3,0);
        }
        printer.PrintText("配送地址："+sendAddress,0,1,0);
        printer.PrintEnter();
        printer.PrintText("联系电话："+phone,0,1,0);
        printer.PrintEnter();
        printer.PrintText("联系人："+name,0,1,0);
        printer.PrintEnter();
        printer.PrintText("配送时间："+sendTime,0,1,0);
        printer.PrintEnter();
        if(marks != null && marks.length > 0){
            printer.PrintText("备注："+marks[0],0,1,0);
            printer.PrintEnter();
        }
        if(marks != null && marks.length > 1){
            printer.PrintText("发票："+marks[1],0,1,0);
            printer.PrintEnter();
        }
        printer.PrintText(CustomMethod.getPrintStr("帐单号："+orderEntity.getSerialNumber(),"单位：元",1,48),0,0,0);
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText("品名         数量     单价      金额      折后额",0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText("-----点菜明细-----",0,0,0);
        printer.PrintEnter();
        for (PrintDishBean printDishBean :
                orderDishEntities) {
            if(printDishBean.getType() == 0){
                if(printDishBean.getStatus() == -2){
                    //赠菜
                    printer.PrintText(("(赠)"+printDishBean.getDishName()+printDishBean.getDishConfig()),0,0,0);
                }else if(printDishBean.getStatus() == -1){
                    //退菜
                    printer.PrintText(("(退)"+printDishBean.getDishName()+printDishBean.getDishConfig()),0,0,0);
                }else{
                    printer.PrintText((printDishBean.getDishName()+printDishBean.getDishConfig()),0,0,0);
                }
                printer.PrintEnter();
                printer.PrintText("    ",0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(4,13,printDishBean.getDishCount()+printDishBean.getUnitName()),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(17,9,String.valueOf(printDishBean.getDishUnitPrice())),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(26,10,String.valueOf(printDishBean.getDishPrice())),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(36,12,String.valueOf(printDishBean.getDiscountMoney())),0,0,0);
                printer.PrintEnter();
            }else{
                //套餐
                printer.PrintText(("    "+printDishBean.getDishName()+printDishBean.getDishConfig()),0,0,0);
                printer.PrintEnter();
                printer.PrintText("    ",0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(4,13,printDishBean.getDishCount()+printDishBean.getUnitName()+""),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(17,9,String.valueOf(printDishBean.getDishUnitPrice())),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(26,10,String.valueOf(printDishBean.getDishPrice())),0,0,0);
                printer.PrintText(CustomMethod.getPrintStr(36,12,String.valueOf(printDishBean.getDiscountMoney())),0,0,0);
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
        printer.PrintText(CustomMethod.getPrintStr("实收金额",String.valueOf(incomeMoney),2,48),0,3,0);
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText(("操作员："+employeeName),0,0,0);
        printer.PrintEnter();
        printer.PrintText(("打印时间："+CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm")),0,0,0);
        printer.PrintEnter();
        printer.CutPage(2);
        printer.OpenBuzzing();
    }
}
