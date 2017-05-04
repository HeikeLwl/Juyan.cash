package jufeng.juyancash.printer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * 打印外卖单
 * Created by Administrator102 on 2016/9/8.
 */
public class PrintOrderCollectionRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private int mDateType;
    private String cashierName,isShift,payModeName,areaTypeName;
    private Map<String, String> orderMoneyDetail;
    private ArrayList<PayModeEntity> payModeEntities;

    public PrintOrderCollectionRunnable(Context context, String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, Map<String, String> orderMoneyDetail, ArrayList<PayModeEntity> payModeEntities) {
        this.mContext = context;
        this.mDateType = dateType;
        this.cashierName = cashierName;
        this.isShift = isShift;
        this.payModeName = payModeName;
        this.areaTypeName = areaTypeName;
        this.orderMoneyDetail = orderMoneyDetail;
        this.payModeEntities = payModeEntities;
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
        if (printer != null) {
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

    public void printData() {
        String startTime = "", endTime = "";
        long currentTime = CustomMethod.parseTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        switch (mDateType) {
            case -1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 1:
                startTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
            case 0:
                startTime = CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24 * 60 * 60 * 1000 - 1000, "yyyy-MM-dd HH:mm:ss");
                break;
        }
        if (areaTypeName.equals("-1")) {
            areaTypeName = "全部";
        } else if (areaTypeName.equals("0")) {
            areaTypeName = "外卖单";
        } else {
            areaTypeName = DBHelper.getInstance(mContext).getAreaNameById(areaTypeName);
        }
        String partnerName = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerName", "");
        String cashierId = mContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", "");
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(cashierId);
        printer.Set();
        printer.PrintText(CustomMethod.getPrintStr(partnerName,2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("账单汇总",2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText("统计时间段：",0,0,0);
        printer.PrintText(startTime,0,2,0);
        printer.PrintEnter();
        printer.PrintText("            "+endTime,0,2,0);
        printer.PrintEnter();
        printer.PrintText("收 银 员：" + cashierName ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("交接状态：" + isShift  ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("付款方式：" + payModeName ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("区    域：" + areaTypeName ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("账单总数", String.valueOf(orderMoneyDetail.get("totalCount")), 1, 48) ,0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("已结账账单总数", String.valueOf(orderMoneyDetail.get("cashieredCount")), 1, 48) ,0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("已结账账单金额", String.valueOf(orderMoneyDetail.get("cashieredMoney")), 1, 48) ,0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("未结账账单总数", String.valueOf(orderMoneyDetail.get("unCashieredCount")), 1, 48) ,0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("未结账账单金额", String.valueOf(orderMoneyDetail.get("unCashieredMoney")), 1, 48) ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------\n" ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("----支付详情----\n" ,0,0,0);
        printer.PrintEnter();
        for (PayModeEntity payMode :
                payModeEntities) {
            printer.PrintText(CustomMethod.getPrintStr(payMode.getPaymentName(), "" + payMode.getPayMoney(), 1, 48) ,0,0,0);
            printer.PrintEnter();
        }
        printer.PrintText("------------------------------------------------" ,0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("合计", String.valueOf(orderMoneyDetail.get("incomeMoney")), 1, 48) ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------" ,0,0,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("消费合计", String.valueOf(orderMoneyDetail.get("receivableMoney")), 2, 48) ,0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("付款合计", String.valueOf(orderMoneyDetail.get("incomeMoney")), 2, 48) ,0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("赠菜合计", String.valueOf(orderMoneyDetail.get("presentMoney")), 2, 48) ,0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("抹零合计", String.valueOf(orderMoneyDetail.get("mlMoney")), 2, 48) ,0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("折扣合计", String.valueOf(orderMoneyDetail.get("discountMoney")), 2, 48) ,0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("不吉利尾数合计", String.valueOf(orderMoneyDetail.get("bjlMoney")), 2, 48) ,0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("优惠券合计", String.valueOf(orderMoneyDetail.get("couponMoney")), 2, 48) ,0,3,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------" ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("操作员：" + employeeName  ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("打印时间：" + CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm")  ,0,0,0);
        printer.PrintEnter();
        printer.CutPage(3);
    }
}
