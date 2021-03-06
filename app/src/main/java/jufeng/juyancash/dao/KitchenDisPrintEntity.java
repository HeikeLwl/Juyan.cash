package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "KITCHEN_DIS_PRINT_ENTITY".
 */
public class KitchenDisPrintEntity {

    private Long id;
    private Long printTime;
    private String orderId;
    private String dishId;
    private String kitchenId;

    public KitchenDisPrintEntity() {
    }

    public KitchenDisPrintEntity(Long id) {
        this.id = id;
    }

    public KitchenDisPrintEntity(Long id, Long printTime, String orderId, String dishId, String kitchenId) {
        this.id = id;
        this.printTime = printTime;
        this.orderId = orderId;
        this.dishId = dishId;
        this.kitchenId = kitchenId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Long printTime) {
        this.printTime = printTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(String kitchenId) {
        this.kitchenId = kitchenId;
    }

}
