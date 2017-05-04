package jufeng.juyancash.bean;

import jufeng.juyancash.printer.NetPrinter;
import jufeng.juyancash.printer.USBPrinter;

/**
 * Created by Administrator102 on 2016/12/26.
 */

public interface PrintBean {
    String call();
    void setNetPrinter(NetPrinter printer);
    void setUsbPrinter(USBPrinter usbPrinter);
    void printFailed();
    void printSuccess();
}
