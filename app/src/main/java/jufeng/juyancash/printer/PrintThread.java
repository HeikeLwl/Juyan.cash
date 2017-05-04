package jufeng.juyancash.printer;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.ui.activity.MainActivity;

/**
 * Created by Administrator102 on 2016/12/26.
 */

public class PrintThread implements Runnable {
    private Context mContext;
    private ConcurrentHashMap<String, NetPrinter> mPrinterMap;//打印机map
    private ConcurrentHashMap<String, ArrayList<PrintBean>> mPrintBeanMap;//任务map
    private ConcurrentHashMap<String, Boolean> mStatusMap;//打印机状态

    public PrintThread(Context context, ConcurrentHashMap<String, NetPrinter> printerMap) {
        this.mContext = context;
        mPrinterMap = new ConcurrentHashMap<>();
        mPrintBeanMap = new ConcurrentHashMap<>();
        mStatusMap = new ConcurrentHashMap<>();
        this.mPrinterMap.putAll(printerMap);
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
            for (final Map.Entry<String, NetPrinter> entity :
                    mPrinterMap.entrySet()) {
                try {
                    entity.getValue().getPosStatus(1);
                } catch (Exception e) {
                    if(entity.getValue().getIfOpen()) {
                        entity.getValue().Close();
                    }
                }
                if (!entity.getValue().getIfOpen()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                entity.getValue().Open(entity.getKey(), 9100);
                            } catch (Exception e) {
                            }
                        }
                    }).start();
                }
                mStatusMap.put(entity.getKey(), entity.getValue().getIfOpen());
            }
            boolean isChanged = DBHelper.getInstance(mContext).changePrinterStatus(mStatusMap);
            if (isChanged) {
                Intent intent = new Intent(MainActivity.ACTION_INTENT_MAIN);
                intent.putExtra("type", 5);
                mContext.sendBroadcast(intent);
            }

            if (mPrintBeanMap.size() > 0) {
                for (Map.Entry<String, ArrayList<PrintBean>> entity :
                        mPrintBeanMap.entrySet()) {
                final ArrayList<PrintBean> printBeanes = new ArrayList<>();
                printBeanes.addAll(entity.getValue());
                    final String ip = entity.getKey();
                    if (printBeanes != null && printBeanes.size() > 0) {
                        for (PrintBean printBean :
                                printBeanes) {
                            try {
                                printBean.setUsbPrinter(null);
                                printBean.setNetPrinter(mPrinterMap.get(ip));
                                printBean.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                                printBean.printFailed();
                            }
                        }
                    }
                    for (PrintBean printBean :
                            printBeanes) {
                        entity.getValue().remove(printBean);
                    }
                }
            }
        }catch (Exception e){
        }
    }
}