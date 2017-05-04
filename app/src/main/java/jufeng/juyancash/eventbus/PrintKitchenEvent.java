package jufeng.juyancash.eventbus;

import java.util.ArrayList;

import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.PrintKitchenEntity;

/**
 * Created by Administrator102 on 2017/3/24.
 */

public class PrintKitchenEvent {
    private ArrayList<PrintDishBean> printDishBeenes;
    private String orderId;
    private PrintKitchenEntity printKitchenEntity;
    private boolean isAddDish;

    public PrintKitchenEvent(ArrayList<PrintDishBean> printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, boolean isAddDish) {
        this.printDishBeenes = printDishBeenes;
        this.orderId = orderId;
        this.printKitchenEntity = printKitchenEntity;
        this.isAddDish = isAddDish;
    }

    public ArrayList<PrintDishBean> getPrintDishBeenes() {
        return printDishBeenes;
    }

    public void setPrintDishBeenes(ArrayList<PrintDishBean> printDishBeenes) {
        this.printDishBeenes = printDishBeenes;
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

    public boolean isAddDish() {
        return isAddDish;
    }

    public void setAddDish(boolean addDish) {
        isAddDish = addDish;
    }
}
