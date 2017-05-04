package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2016/8/8.
 */
public class JpushMessageExtras {
    String orderId;
    int type;
    String picurl;
    long time;


    public JpushMessageExtras() {

    }

    public JpushMessageExtras(String param) {
        String[] params = param.split("`");
        this.orderId = params[0];
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
