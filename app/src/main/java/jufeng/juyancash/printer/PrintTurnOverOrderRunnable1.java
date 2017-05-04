package jufeng.juyancash.printer;

import android.content.Context;
import android.util.Log;

import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.dao.TurnoverHistoryEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * 打印外卖单
 * Created by Administrator102 on 2016/9/8.
 */
public class PrintTurnOverOrderRunnable1 implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private TurnoverHistoryEntity mTurnoverHistoryEntity;

    public PrintTurnOverOrderRunnable1(Context context, TurnoverHistoryEntity turnoverHistoryEntity) {
        this.mContext = context;
        this.mTurnoverHistoryEntity = turnoverHistoryEntity;
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
            printData();
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

    public void printData(){
        String startTime = mTurnoverHistoryEntity.getTurnoverStartTime(), endTime = mTurnoverHistoryEntity.getTurnoverEndTime();
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        String turnOverTime = mTurnoverHistoryEntity.getStartTurnoverTime();
        String cashierName = mTurnoverHistoryEntity.getCashierName();
        String areaTypeName = mTurnoverHistoryEntity.getAreaType();
        String[] payModeEntities,dishTypeCollectionItemBeens;
        try {
            payModeEntities = mTurnoverHistoryEntity.getPayments().split("`");
            dishTypeCollectionItemBeens = mTurnoverHistoryEntity.getDishTypes().split("`");
        }catch (Exception e){
            payModeEntities = new String[0];
            dishTypeCollectionItemBeens = new String[0];
        }
        printer.Set();
        printer.PrintText(CustomMethod.getPrintStr(partnerName,2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("菜品分类汇总",2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText("交接班时间："+turnOverTime,0,0,0);
        printer.PrintEnter();
        printer.PrintText("统计时间段：",0,0,0);
        printer.PrintText(startTime,0,2,0);
        printer.PrintEnter();
        printer.PrintText("            "+endTime,0,2,0);
        printer.PrintEnter();
        printer.PrintText("收 银 员：" + cashierName ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("交接状态：未交接",0,0,0);
        printer.PrintEnter();
        printer.PrintText("付款方式：全部",0,0,0);
        printer.PrintEnter();
        printer.PrintText("区    域：" + areaTypeName ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------" ,0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("账单总数",mTurnoverHistoryEntity.getOrderTotalCount(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("已结账账单总数",mTurnoverHistoryEntity.getOrderedTotalCount(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("已结账账单金额",mTurnoverHistoryEntity.getOrderedTotalMoney(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("未结账账单总数",mTurnoverHistoryEntity.getUnOrderedTotalCount(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("未结账账单金额",mTurnoverHistoryEntity.getUnOrderedTotalMoney(),1,48),0,0,0);
        printer.PrintText("------------------------------------------------" ,0,0,0);
        printer.PrintText("------支付详情------",0,0,0);
        if(payModeEntities.length > 0){
            for(int i = 0 ; i < payModeEntities.length; ){
                printer.PrintText(CustomMethod.getPrintStr(payModeEntities[i],payModeEntities[i+1],1,48),0,0,0);
                i += 2;
            }
        }else{
            printer.PrintText(CustomMethod.getPrintStr("暂无数据", 1, 48),0,0,0);
        }
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintText("菜品分类                数量                合计",0,0,0);
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        if(dishTypeCollectionItemBeens.length > 0){
            for (int i = 0; i < dishTypeCollectionItemBeens.length;){
                printer.PrintText(dishTypeCollectionItemBeens[i],0,0,0);
                printer.PrintEnter();
                printer.PrintText(CustomMethod.getPrintStr("                        "+dishTypeCollectionItemBeens[i+1], "" + dishTypeCollectionItemBeens[i+2], 1, 48) ,0,0,0);
                printer.PrintEnter();
                i += 3;
            }
        }else{
            printer.PrintText(CustomMethod.getPrintStr("暂无数据", 1, 48),0,0,0);
        }
        printer.PrintText("------------------------------------------------" ,0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("消费合计",mTurnoverHistoryEntity.getMoney0(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("付款合计",mTurnoverHistoryEntity.getMoney1(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("赠菜合计",mTurnoverHistoryEntity.getMoney2(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("抹零合计",mTurnoverHistoryEntity.getMoney3(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("折扣合计",mTurnoverHistoryEntity.getMoney4(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("不吉利尾数合计",mTurnoverHistoryEntity.getMoney5(),1,48),0,0,0);
        printer.PrintText(CustomMethod.getPrintStr("优惠券合计",mTurnoverHistoryEntity.getMoney6(),1,48),0,0,0);
        printer.PrintText("------------------------------------------------" ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("操作员：" + mTurnoverHistoryEntity.getOperatorName()  ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("打印时间：" + CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm")  ,0,0,0);
        printer.PrintEnter();
        printer.CutPage(3);
    }
}
