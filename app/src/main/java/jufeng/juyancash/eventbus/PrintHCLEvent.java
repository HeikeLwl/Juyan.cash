package jufeng.juyancash.eventbus;

import java.util.ArrayList;

import jufeng.juyancash.bean.PrintDishBean;

/**
 * Created by Administrator102 on 2017/3/24.
 */

public class PrintHCLEvent {
    private ArrayList<PrintDishBean> printDishBeenes;
    private String orderId;
    private String ip;

    public PrintHCLEvent(ArrayList<PrintDishBean> printDishBeenes, String orderId, String ip) {
        this.printDishBeenes = printDishBeenes;
        this.orderId = orderId;
        this.ip = ip;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
