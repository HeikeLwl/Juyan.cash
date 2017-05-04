package jufeng.juyancash.eventbus;

import java.util.ArrayList;

import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.PrintKitchenEntity;

/**
 * Created by Administrator102 on 2017/3/29.
 */

public class SnackRetreatPrintEvent {
    private ArrayList<PrintDishBean> mPrintDishBeen;
    private String orderId;
    private PrintKitchenEntity printKitchenEntity;
    private String employeeName;
    private String reason;

    public SnackRetreatPrintEvent(ArrayList<PrintDishBean> printDishBeen, String orderId, PrintKitchenEntity printKitchenEntity, String employeeName, String reason) {
        mPrintDishBeen = printDishBeen;
        this.orderId = orderId;
        this.printKitchenEntity = printKitchenEntity;
        this.employeeName = employeeName;
        this.reason = reason;
    }

    public ArrayList<PrintDishBean> getPrintDishBeen() {
        return mPrintDishBeen;
    }

    public void setPrintDishBeen(ArrayList<PrintDishBean> printDishBeen) {
        mPrintDishBeen = printDishBeen;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public PrintKitchenEntity getPrintKitchenEntity() {
        return printKitchenEntity;
    }

    public void setPrintKitchenEntity(PrintKitchenEntity printKitchenEntity) {
        this.printKitchenEntity = printKitchenEntity;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
