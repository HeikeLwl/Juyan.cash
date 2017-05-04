package jufeng.juyancash.printer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.BillAccountHistoryEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

/**
 * 打印消费底联
 * Created by Administrator102 on 2016/9/8.
 */
public class PrintAccountRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private String mOrderId;
    private String mIP;
    private boolean isPrintPresentDish,isPrintRetreatDish;
    private BillAccountHistoryEntity mBillAccountHistoryEntity;
    private int mStoreVersion;

    public PrintAccountRunnable(Context context, String orderId, BillAccountHistoryEntity billAccountHistoryEntity,String ip, boolean isPrintPresentDish, boolean isPrintRetreatDish) {
        this.mContext = context;
        this.mOrderId = orderId;
        this.mIP = ip;
        this.mBillAccountHistoryEntity = billAccountHistoryEntity;
        this.isPrintPresentDish = isPrintPresentDish;
        this.isPrintRetreatDish = isPrintRetreatDish;
        this.mStoreVersion = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
    }

    @Override
    public void printSuccess() {
        Log.d("###", "挂账打印成功");
    }

    @Override
    public void printFailed() {
        Log.d("###", "挂账打印失败");
    }

    @Override
    public String call() {
        if(printer != null)
            printData();
        return "success";
    }

    @Override
    public void setUsbPrinter(USBPrinter usbPrinter) {

    }

    @Override
    public void setNetPrinter(NetPrinter printer) {
        this.printer = printer;
    }

    public void printData(){
        long currentTime = System.currentTimeMillis();
        OrderEntity orderEntity = DBHelper.getInstance(mContext).getOneOrderEntity(mOrderId);
        ArrayList<PrintDishBean> orderDishEntities = DBHelper.getInstance(mContext).queryPrintDish(mOrderId,isPrintPresentDish,isPrintRetreatDish);
        String areaName = DBHelper.getInstance(mContext).getAreaNameById(orderEntity.getAreaId());
        String tableCode = "";
        if(mStoreVersion == 0){
            tableCode = DBHelper.getInstance(mContext).getTableNameByTableId(orderEntity.getTableId());
        }else if(mStoreVersion == 1){
            tableCode = orderEntity.getTableId() == null ?"":orderEntity.getTableId();
        } else if(mStoreVersion == 2){
            tableCode = orderEntity.getTableId() == null ?"":orderEntity.getTableId();
        }
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getCashierId());
        String waiterName = DBHelper.getInstance(mContext).getEmployeeNameById(orderEntity.getWaiterId());
        double totalMoney = DBHelper.getInstance(mContext).getBillMoneyByOrderId(mOrderId,1);
        String incomeMoney = AmountUtils.changeF2Y(mBillAccountHistoryEntity.getBillAccountMoney());
        float yhMoney = ((float)DBHelper.getInstance(mContext).getYHMoney(mOrderId))/100;
        double zhMoney = DBHelper.getInstance(mContext).getReceivableMoney(mOrderId,1);
        String billAccountName = mBillAccountHistoryEntity.getBillAccountName();
        String billUnitName = mBillAccountHistoryEntity.getBillAccountUnitName() == null ? "无":mBillAccountHistoryEntity.getBillAccountUnitName();
        String billPersonName = mBillAccountHistoryEntity.getBillAccountPersonName() == null ? "无":mBillAccountHistoryEntity.getBillAccountPersonName();
        String signName = mBillAccountHistoryEntity.getBillAccountSignName() == null ? "无":mBillAccountHistoryEntity.getBillAccountSignName();
        String partnerName = mContext.getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerName","");
        printer.Set();
        printer.PrintText(CustomMethod.getPrintStr(partnerName,2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("挂账签字单",2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr(CustomMethod.parseTime(currentTime,"yyyy-MM-dd HH:mm"),1,48),0,0,0);
        printer.PrintEnter();
        if(mStoreVersion == 0){
            printer.PrintText(CustomMethod.getPrintStr("台号："+areaName+"-"+tableCode,"单号："+String.valueOf(orderEntity.getOrderNumber1()),2,48),0,3,0);
            printer.PrintText(CustomMethod.getPrintStr("帐单号："+orderEntity.getSerialNumber(),"人数："+orderEntity.getOrderGuests(),1,48),0,0,0);
            printer.PrintText(CustomMethod.getPrintStr("服务员："+waiterName,"单位：元",1,48),0,0,0);
        }else if(mStoreVersion == 1){
            printer.PrintText("流水号："+orderEntity.getSerialNumber(),0,0,0);
            printer.PrintEnter();
            printer.PrintText(CustomMethod.getPrintStr("单号："+orderEntity.getOrderNumber1(),"单位：元",1,48),0,0,0);
        }else if(mStoreVersion == 2){
            printer.PrintText("流水号："+orderEntity.getSerialNumber(),0,0,0);
            printer.PrintEnter();
            printer.PrintText(CustomMethod.getPrintStr("单号："+orderEntity.getOrderNumber1(),"单位：元",1,48),0,0,0);
        }

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
                printer.PrintText(CustomMethod.getPrintStr(4,13,printDishBean.getDishCount()+printDishBean.getUnitName()+""),0,0,0);
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
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("总金额",String.valueOf(totalMoney),2,48),0,3,0);
        printer.PrintText(CustomMethod.getPrintStr("优惠金额",String.valueOf(yhMoney),2,48),0,3,0);
        printer.PrintText(CustomMethod.getPrintStr("应付金额",String.valueOf(zhMoney),2,48),0,3,0);
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("挂账金额",String.valueOf(incomeMoney),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("挂账名称",billAccountName,1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("挂账单位",billUnitName,1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("挂账人",billPersonName,1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("本店签字人",signName,1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("挂账日期",CustomMethod.parseTime(currentTime,"yyyy-MM-dd HH:mm"),1,48),0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("顾客签字：","",1,48),0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText(("操作员："+employeeName),0,0,0);
        printer.PrintEnter();
        printer.PrintText(("打印时间："+CustomMethod.parseTime(currentTime,"yyyy-MM-dd HH:mm")),0,0,0);
        printer.PrintEnter();
        printer.CutPage(2);
        printer.OpenBuzzing();
    }
}
