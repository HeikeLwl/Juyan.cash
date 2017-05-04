package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity mapped to table "TAKE_OUT_ORDER_ENTITY".
 */
public class TakeOutOrderEntity implements Parcelable {

    private Long id;
    /** Not-null value. */
    private String takeoutId;
    private String orderId;
    private String guestName;
    private String guestPhone;
    private String takeoutAddress;
    private Long takeoutTime;
    private String takeoutMark;
    private Integer boxFee;
    private Integer dispatchFee;
    private String otherOrderId;
    private Integer takeoutFrom;
    private Integer takeoutStatus;

    public TakeOutOrderEntity() {
    }

    public TakeOutOrderEntity(Long id) {
        this.id = id;
    }

    public TakeOutOrderEntity(Long id, String takeoutId, String orderId, String guestName, String guestPhone, String takeoutAddress, Long takeoutTime, String takeoutMark, Integer boxFee, Integer dispatchFee, String otherOrderId, Integer takeoutFrom, Integer takeoutStatus) {
        this.id = id;
        this.takeoutId = takeoutId;
        this.orderId = orderId;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
        this.takeoutAddress = takeoutAddress;
        this.takeoutTime = takeoutTime;
        this.takeoutMark = takeoutMark;
        this.boxFee = boxFee;
        this.dispatchFee = dispatchFee;
        this.otherOrderId = otherOrderId;
        this.takeoutFrom = takeoutFrom;
        this.takeoutStatus = takeoutStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTakeoutId() {
        return takeoutId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTakeoutId(String takeoutId) {
        this.takeoutId = takeoutId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getTakeoutAddress() {
        return takeoutAddress;
    }

    public void setTakeoutAddress(String takeoutAddress) {
        this.takeoutAddress = takeoutAddress;
    }

    public Long getTakeoutTime() {
        return takeoutTime;
    }

    public void setTakeoutTime(Long takeoutTime) {
        this.takeoutTime = takeoutTime;
    }

    public String getTakeoutMark() {
        return takeoutMark;
    }

    public void setTakeoutMark(String takeoutMark) {
        this.takeoutMark = takeoutMark;
    }

    public Integer getBoxFee() {
        if(boxFee == null){
            return 0;
        }else {
            return boxFee;
        }
    }

    public void setBoxFee(Integer boxFee) {
        this.boxFee = boxFee;
    }

    public Integer getDispatchFee() {
        if(dispatchFee == null){
            return 0;
        }else {
            return dispatchFee;
        }
    }

    public void setDispatchFee(Integer dispatchFee) {
        this.dispatchFee = dispatchFee;
    }

    public String getOtherOrderId() {
        return otherOrderId;
    }

    public void setOtherOrderId(String otherOrderId) {
        this.otherOrderId = otherOrderId;
    }

    public Integer getTakeoutFrom() {
        return takeoutFrom;
    }

    public void setTakeoutFrom(Integer takeoutFrom) {
        this.takeoutFrom = takeoutFrom;
    }

    public Integer getTakeoutStatus() {
        return takeoutStatus;
    }

    public void setTakeoutStatus(Integer takeoutStatus) {
        this.takeoutStatus = takeoutStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.takeoutId);
        dest.writeString(this.orderId);
        dest.writeString(this.guestName);
        dest.writeString(this.guestPhone);
        dest.writeString(this.takeoutAddress);
        dest.writeValue(this.takeoutTime);
        dest.writeString(this.takeoutMark);
        dest.writeValue(this.boxFee);
        dest.writeValue(this.dispatchFee);
        dest.writeString(this.otherOrderId);
        dest.writeValue(this.takeoutFrom);
        dest.writeValue(this.takeoutStatus);
    }

    protected TakeOutOrderEntity(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.takeoutId = in.readString();
        this.orderId = in.readString();
        this.guestName = in.readString();
        this.guestPhone = in.readString();
        this.takeoutAddress = in.readString();
        this.takeoutTime = (Long) in.readValue(Long.class.getClassLoader());
        this.takeoutMark = in.readString();
        this.boxFee = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dispatchFee = (Integer) in.readValue(Integer.class.getClassLoader());
        this.otherOrderId = in.readString();
        this.takeoutFrom = (Integer) in.readValue(Integer.class.getClassLoader());
        this.takeoutStatus = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<TakeOutOrderEntity> CREATOR = new Parcelable.Creator<TakeOutOrderEntity>() {
        @Override
        public TakeOutOrderEntity createFromParcel(Parcel source) {
            return new TakeOutOrderEntity(source);
        }

        @Override
        public TakeOutOrderEntity[] newArray(int size) {
            return new TakeOutOrderEntity[size];
        }
    };
}