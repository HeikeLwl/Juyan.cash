package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2016/9/10.
 */
public class TakeOutBean {
    private String order;
    private int type;
    private int status;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
