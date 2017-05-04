package jufeng.juyancash.printer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.DishTypeCollectionItemBean;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.util.CustomMethod;

/**
 * 打印外卖单
 * Created by Administrator102 on 2016/9/8.
 */
public class PrintTypeCollectionRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private int mDateType;
    private String cashierName,isShift,payModeName,areaTypeName;
    private ArrayList<DishTypeCollectionItemBean>dishTypeCollectionItemBeens;

    public PrintTypeCollectionRunnable(Context context, String cashierName, String isShift, String payModeName, int dateType, String areaTypeName,ArrayList<DishTypeCollectionItemBean> dishTypeCollectionItemBeen) {
        this.mContext = context;
        this.mDateType = dateType;
        this.cashierName = cashierName;
        this.isShift = isShift;
        this.payModeName = payModeName;
        this.areaTypeName = areaTypeName;
        this.dishTypeCollectionItemBeens = dishTypeCollectionItemBeen;
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
        printer.PrintText(CustomMethod.getPrintStr("菜品分类汇总",2,48),0,3,0);
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
        printer.PrintText("菜品分类                数量                合计",0,0,0);
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        for (DishTypeCollectionItemBean dishTypeCollectionItemBean :
                dishTypeCollectionItemBeens) {
            printer.PrintText(dishTypeCollectionItemBean.getDishTypeName(),0,0,0);
            printer.PrintEnter();
            printer.PrintText(CustomMethod.getPrintStr("                        "+dishTypeCollectionItemBean.getDishTypeCount(), "" + dishTypeCollectionItemBean.getDishTypeMoney(), 1, 48) ,0,0,0);
            printer.PrintEnter();
        }
        printer.PrintText("------------------------------------------------" ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("操作员：" + employeeName  ,0,0,0);
        printer.PrintEnter();
        printer.PrintText("打印时间：" + CustomMethod.parseTime(currentTime, "yyyy-MM-dd HH:mm")  ,0,0,0);
        printer.PrintEnter();
        printer.CutPage(3);
    }
}
