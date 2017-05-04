package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2016/11/12.
 */

public class ClearModel {
    private String id;
    private String name;
    private int type;//0：付款；1：打折；2：挂账；3：手动抹零
    private int money;
    private int payType;
    private String paySerial;

    public ClearModel(String id,String name,int type,int money,int payType,String paySerial){
        this.id = id;
        this.name = name;
        this.type = type;
        this.money = money;
        this.payType = payType;
        this.paySerial = paySerial;
    }

    public String getPaySerial() {
        return paySerial;
    }

    public void setPaySerial(String paySerial) {
        this.paySerial = paySerial;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getPayType() {
        return payType;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
