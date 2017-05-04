package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity mapped to table "ORDER_TAOCAN_GROUP_DISH_ENTITY".
 */
public class OrderTaocanGroupDishEntity implements Parcelable {

    private Long id;
    /**
     * Not-null value.
     */
    private String orderTaocanGroupDishId;
    private String orderId;
    private String taocanGroupDishId;
    private String taocanGroupId;
    private String orderDishId;
    private String orderDishName;
    private String dishId;
    private String dishName;
    private String practiceId;
    private String practiceName;
    private String specifyId;
    private String specifyName;
    private Integer taocanGroupDishCount;
    private Float extraPrice;
    private String remark;
    private Integer status;
    private Long createTime;
    private String taocanTypeId;
    private String dishTypeId;
    private String taocanTypeName;
    private String dishTypeName;
    private Integer isPrint;

    public boolean compareTo(OrderTaocanGroupDishEntity orderTaocanGroupDishEntity) {
        try {
            boolean result = orderTaocanGroupDishEntity.getOrderId().equals(orderId);
            result = result && orderTaocanGroupDishEntity.getOrderDishId().equals(orderDishId);
            result = result && orderTaocanGroupDishEntity.getDishName().equals(dishName);
            result = result && orderTaocanGroupDishEntity.getDishId().equals(dishId);
            result = result && ((orderTaocanGroupDishEntity.getRemark() == null && remark == null)||(orderTaocanGroupDishEntity.getRemark() != null && remark != null && orderTaocanGroupDishEntity.getRemark().equals(remark)));
            result = result && orderTaocanGroupDishEntity.getTaocanGroupId().equals(taocanGroupId);
            result = result && orderTaocanGroupDishEntity.getTaocanGroupDishId().equals(taocanGroupDishId);
            result = result && orderTaocanGroupDishEntity.getDishTypeId().equals(dishTypeId);
            result = result && orderTaocanGroupDishEntity.getDishTypeName().equals(dishTypeName);
            result = result && ((orderTaocanGroupDishEntity.getPracticeId() == null && practiceId == null) || (orderTaocanGroupDishEntity.getPracticeId() != null && practiceId != null && orderTaocanGroupDishEntity.getPracticeId().equals(practiceId)));
            result = result && ((orderTaocanGroupDishEntity.getSpecifyId() == null && specifyId == null) || (orderTaocanGroupDishEntity.getSpecifyId() != null && specifyId != null && orderTaocanGroupDishEntity.getSpecifyId().equals(specifyId)));
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    public OrderTaocanGroupDishEntity() {
    }

    public OrderTaocanGroupDishEntity(Long id) {
        this.id = id;
    }

    public OrderTaocanGroupDishEntity(Long id, String orderTaocanGroupDishId, String orderId, String taocanGroupDishId, String taocanGroupId, String orderDishId, String orderDishName, String dishId, String dishName, String practiceId, String practiceName, String specifyId, String specifyName, Integer taocanGroupDishCount, Float extraPrice, String remark, Integer status, Long createTime, String taocanTypeId, String dishTypeId, String taocanTypeName, String dishTypeName,Integer isPrint) {
        this.id = id;
        this.orderTaocanGroupDishId = orderTaocanGroupDishId;
        this.orderId = orderId;
        this.taocanGroupDishId = taocanGroupDishId;
        this.taocanGroupId = taocanGroupId;
        this.orderDishId = orderDishId;
        this.orderDishName = orderDishName;
        this.dishId = dishId;
        this.dishName = dishName;
        this.practiceId = practiceId;
        this.practiceName = practiceName;
        this.specifyId = specifyId;
        this.specifyName = specifyName;
        this.taocanGroupDishCount = taocanGroupDishCount;
        this.extraPrice = extraPrice;
        this.remark = remark;
        this.status = status;
        this.createTime = createTime;
        this.taocanTypeId = taocanTypeId;
        this.dishTypeId = dishTypeId;
        this.taocanTypeName = taocanTypeName;
        this.dishTypeName = dishTypeName;
        this.isPrint = isPrint;
    }

    @Override
    public String toString() {
        return "OrderTaocanGroupDishEntity{" +
                "id=" + id +
                ", orderTaocanGroupDishId='" + orderTaocanGroupDishId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", taocanGroupDishId='" + taocanGroupDishId + '\'' +
                ", taocanGroupId='" + taocanGroupId + '\'' +
                ", orderDishId='" + orderDishId + '\'' +
                ", orderDishName='" + orderDishName + '\'' +
                ", dishId='" + dishId + '\'' +
                ", dishName='" + dishName + '\'' +
                ", practiceId='" + practiceId + '\'' +
                ", practiceName='" + practiceName + '\'' +
                ", specifyId='" + specifyId + '\'' +
                ", specifyName='" + specifyName + '\'' +
                ", taocanGroupDishCount=" + taocanGroupDishCount +
                ", extraPrice=" + extraPrice +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", taocanTypeId='" + taocanTypeId + '\'' +
                ", dishTypeId='" + dishTypeId + '\'' +
                ", taocanTypeName='" + taocanTypeName + '\'' +
                ", dishTypeName='" + dishTypeName + '\'' +
                ", isPrint=" + isPrint +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Not-null value.
     */
    public String getOrderTaocanGroupDishId() {
        return orderTaocanGroupDishId;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setOrderTaocanGroupDishId(String orderTaocanGroupDishId) {
        this.orderTaocanGroupDishId = orderTaocanGroupDishId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTaocanGroupDishId() {
        return taocanGroupDishId;
    }

    public void setTaocanGroupDishId(String taocanGroupDishId) {
        this.taocanGroupDishId = taocanGroupDishId;
    }

    public String getTaocanGroupId() {
        return taocanGroupId;
    }

    public void setTaocanGroupId(String taocanGroupId) {
        this.taocanGroupId = taocanGroupId;
    }

    public String getOrderDishId() {
        return orderDishId;
    }

    public void setOrderDishId(String orderDishId) {
        this.orderDishId = orderDishId;
    }

    public String getOrderDishName() {
        return orderDishName;
    }

    public void setOrderDishName(String orderDishName) {
        this.orderDishName = orderDishName;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(String practiceId) {
        this.practiceId = practiceId;
    }

    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    public String getSpecifyId() {
        return specifyId;
    }

    public void setSpecifyId(String specifyId) {
        this.specifyId = specifyId;
    }

    public String getSpecifyName() {
        return specifyName;
    }

    public void setSpecifyName(String specifyName) {
        this.specifyName = specifyName;
    }

    public Integer getTaocanGroupDishCount() {
        return taocanGroupDishCount;
    }

    public void setTaocanGroupDishCount(Integer taocanGroupDishCount) {
        this.taocanGroupDishCount = taocanGroupDishCount;
    }

    public Float getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(Float extraPrice) {
        this.extraPrice = extraPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getTaocanTypeId() {
        return taocanTypeId;
    }

    public void setTaocanTypeId(String taocanTypeId) {
        this.taocanTypeId = taocanTypeId;
    }

    public String getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(String dishTypeId) {
        this.dishTypeId = dishTypeId;
    }

    public String getTaocanTypeName() {
        return taocanTypeName;
    }

    public void setTaocanTypeName(String taocanTypeName) {
        this.taocanTypeName = taocanTypeName;
    }

    public String getDishTypeName() {
        return dishTypeName;
    }

    public void setDishTypeName(String dishTypeName) {
        this.dishTypeName = dishTypeName;
    }

    public Integer getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Integer isPrint) {
        this.isPrint = isPrint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.orderTaocanGroupDishId);
        dest.writeString(this.orderId);
        dest.writeString(this.taocanGroupDishId);
        dest.writeString(this.taocanGroupId);
        dest.writeString(this.orderDishId);
        dest.writeString(this.orderDishName);
        dest.writeString(this.dishId);
        dest.writeString(this.dishName);
        dest.writeString(this.practiceId);
        dest.writeString(this.practiceName);
        dest.writeString(this.specifyId);
        dest.writeString(this.specifyName);
        dest.writeValue(this.taocanGroupDishCount);
        dest.writeValue(this.extraPrice);
        dest.writeString(this.remark);
        dest.writeValue(this.status);
        dest.writeValue(this.createTime);
        dest.writeString(this.taocanTypeId);
        dest.writeString(this.dishTypeId);
        dest.writeString(this.taocanTypeName);
        dest.writeString(this.dishTypeName);
        dest.writeValue(this.isPrint);
    }

    protected OrderTaocanGroupDishEntity(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.orderTaocanGroupDishId = in.readString();
        this.orderId = in.readString();
        this.taocanGroupDishId = in.readString();
        this.taocanGroupId = in.readString();
        this.orderDishId = in.readString();
        this.orderDishName = in.readString();
        this.dishId = in.readString();
        this.dishName = in.readString();
        this.practiceId = in.readString();
        this.practiceName = in.readString();
        this.specifyId = in.readString();
        this.specifyName = in.readString();
        this.taocanGroupDishCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.extraPrice = (Float) in.readValue(Float.class.getClassLoader());
        this.remark = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createTime = (Long) in.readValue(Long.class.getClassLoader());
        this.taocanTypeId = in.readString();
        this.dishTypeId = in.readString();
        this.taocanTypeName = in.readString();
        this.dishTypeName = in.readString();
        this.isPrint = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<OrderTaocanGroupDishEntity> CREATOR = new Parcelable.Creator<OrderTaocanGroupDishEntity>() {
        @Override
        public OrderTaocanGroupDishEntity createFromParcel(Parcel source) {
            return new OrderTaocanGroupDishEntity(source);
        }

        @Override
        public OrderTaocanGroupDishEntity[] newArray(int size) {
            return new OrderTaocanGroupDishEntity[size];
        }
    };
}