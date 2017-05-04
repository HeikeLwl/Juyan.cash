package jufeng.juyancash.printer;

import android.content.Context;
import android.util.Log;

import jufeng.juyancash.bean.PrintBean;

/**
 * Created by Administrator102 on 2016/9/8.
 */
public class OpenQianXiangRunnable implements PrintBean {
    private NetPrinter printer;
    private Context mContext;
    private String mIP;

    public OpenQianXiangRunnable(Context context, String ip) {
        this.mContext = context;
        this.mIP = ip;
    }

    @Override
    public void printSuccess() {
        Log.d("###", "打开钱箱打印成功");
    }

    @Override
    public void printFailed() {
        Log.d("###", "打开钱箱打印失败");
    }

    @Override
    public String call() {
        if(printer != null)
            printData();
        return "success";
    }

    @Override
    public void setNetPrinter(NetPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void setUsbPrinter(USBPrinter usbPrinter) {

    }

    private void printData(){
        printer.Set();
        printer.OpenQianXiang();
    }
}
