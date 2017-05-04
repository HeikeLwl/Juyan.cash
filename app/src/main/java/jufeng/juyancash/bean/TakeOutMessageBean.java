package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/4/5.
 */

public class TakeOutMessageBean {
    private String partnerName;
    private String orderNo;
    private String targetAdr;
    private String hopeTime;
    private String phone;

    public TakeOutMessageBean(String partnerName, String orderNo, String targetAdr, String hopeTime, String phone) {
        this.partnerName = partnerName;
        this.orderNo = orderNo;
        this.targetAdr = targetAdr;
        this.hopeTime = hopeTime;
        this.phone = phone;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTargetAdr() {
        return targetAdr;
    }

    public void setTargetAdr(String targetAdr) {
        this.targetAdr = targetAdr;
    }

    public String getHopeTime() {
        return hopeTime;
    }

    public void setHopeTime(String hopeTime) {
        this.hopeTime = hopeTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
