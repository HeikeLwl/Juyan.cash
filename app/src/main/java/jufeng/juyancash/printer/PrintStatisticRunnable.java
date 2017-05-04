package jufeng.juyancash.printer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.DishModel;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.util.CustomMethod;

/**
 * 打印外卖单
 * Created by Administrator102 on 2016/9/8.
 */
public class PrintStatisticRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private int mDateType;
    private DishTypeModel mDishTypeModel;
    private ArrayList<DishModel> mDishModels;

    public PrintStatisticRunnable(Context context, int dateType, DishTypeModel dishTypeModel, ArrayList<DishModel> dishModels) {
        this.mContext = context;
        this.mDateType = dateType;
        this.mDishTypeModel = dishTypeModel;
        this.mDishModels = dishModels;
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
        String cashierId = mContext.getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("employeeId","");
        String employeeName = DBHelper.getInstance(mContext).getEmployeeNameById(cashierId);
        String startTime = "",endTime = "";
        long currentTime = CustomMethod.parseTime(CustomMethod.parseTime(System.currentTimeMillis(),"yyyy-MM-dd")+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        switch (mDateType){
            case -1:
                startTime = CustomMethod.parseTime(currentTime - 24*60*60*1000,"yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24*60*60*1000-1000,"yyyy-MM-dd HH:mm:ss");
                break;
            case 1:
                startTime = CustomMethod.parseTime(currentTime - 24*60*60*1000,"yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime-1000,"yyyy-MM-dd HH:mm:ss");
                break;
            case 0:
                startTime = CustomMethod.parseTime(currentTime,"yyyy-MM-dd HH:mm:ss");
                endTime = CustomMethod.parseTime(currentTime + 24*60*60*1000-1000,"yyyy-MM-dd HH:mm:ss");
                break;
        }
        String partnerName = mContext.getSharedPreferences("loginData",Context.MODE_PRIVATE).getString("partnerName","");
        printer.Set();
        printer.PrintText(CustomMethod.getPrintStr(partnerName,2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText(CustomMethod.getPrintStr("菜类点菜统计",2,48),0,3,0);
        printer.PrintEnter();
        printer.PrintText("菜类",0,0,0);
        printer.PrintText(mDishTypeModel.getDishTypeModelTypeName(),0,3,0);
        printer.PrintEnter();
        printer.PrintText("统计时间段：",0,0,0);
        printer.PrintText(startTime,0,2,0);
        printer.PrintText("            "+endTime,0,2,0);
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        printer.PrintText("             数量                           数量",0,0,0);
        printer.PrintEnter();
        printer.PrintText("------------------------------------------------",0,0,0);
        printer.PrintEnter();
        for (DishModel dishModel :
                mDishModels) {
            printer.PrintText(CustomMethod.getPrintStr(dishModel.getDishModelName(),String.valueOf(dishModel.getCount()),1,48),0,2,0);
            printer.PrintEnter();
        }
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
