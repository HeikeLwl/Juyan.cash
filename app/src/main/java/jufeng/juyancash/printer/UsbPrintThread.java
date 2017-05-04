package jufeng.juyancash.printer;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jufeng.juyancash.bean.PrintBean;

/**
 * Created by Administrator102 on 2016/12/26.
 */

public class UsbPrintThread implements Runnable {
    private Context mContext;
    private ConcurrentHashMap<String, ArrayList<PrintBean>> mPrintBeanMap;//任务map
    private USBPrinter mUSBPrinter;

    public UsbPrintThread(Context context, USBPrinter usbPrinter) {
        this.mContext = context;
        mPrintBeanMap = new ConcurrentHashMap<>();
        this.mUSBPrinter = usbPrinter;
    }

    public void closeUsbPrinter() {
        if (mUSBPrinter != null) {
            mUSBPrinter.closePort();
        }
    }

    public void addPrintBean(String ip, PrintBean printBean) {
        if (mPrintBeanMap.containsKey(ip)) {
            if (mPrintBeanMap.get(ip) != null) {
                mPrintBeanMap.get(ip).add(printBean);
            } else {
                ArrayList<PrintBean> printBeanes = new ArrayList<>();
                printBeanes.add(printBean);
                mPrintBeanMap.put(ip, printBeanes);
            }
        } else {
            ArrayList<PrintBean> printBeanes = new ArrayList<>();
            printBeanes.add(printBean);
            mPrintBeanMap.put(ip, printBeanes);
        }
    }

    @Override
    public void run() {
        //连接每一个打印机，并保存连接状态
        try {
            if (mPrintBeanMap.size() > 0) {
                for (final Map.Entry<String, ArrayList<PrintBean>> entity :
                        mPrintBeanMap.entrySet()) {
                    ArrayList<PrintBean> printBeans = new ArrayList<>();
                    printBeans.addAll(entity.getValue());
                    if (printBeans != null && printBeans.size() > 0) {
                        for (PrintBean printBean :
                                printBeans) {
                            try {
                                printBean.setUsbPrinter(mUSBPrinter);
                                printBean.setNetPrinter(null);
                                printBean.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                                printBean.printFailed();
                            }
                        }
                    }
                    for (PrintBean printBean :
                            printBeans) {
                        entity.getValue().remove(printBean);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}