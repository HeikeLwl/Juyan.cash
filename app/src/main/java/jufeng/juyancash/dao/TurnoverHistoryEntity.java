package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "TURNOVER_HISTORY_ENTITY".
 */
public class TurnoverHistoryEntity {

    private Long id;
    private String turnoverHistoryId;
    private String startTurnoverTime;
    private String turnoverStartTime;
    private String turnoverEndTime;
    private String cashierName;
    private String turnoverState;
    private String payMentType;
    private String areaType;
    private String orderTotalCount;
    private String orderedTotalCount;
    private String unOrderedTotalCount;
    private String orderedTotalMoney;
    private String unOrderedTotalMoney;
    private String payments;
    private String dishTypes;
    private String money0;
    private String money1;
    private String money2;
    private String money3;
    private String money4;
    private String money5;
    private String money6;
    private String operatorName;
    private String printTime;
    private Long createTime;

    public TurnoverHistoryEntity() {
    }

    @Override
    public String toString() {
        return "TurnoverHistoryEntity{" +
                "id=" + id +
                ", turnoverHistoryId='" + turnoverHistoryId + '\'' +
                ", startTurnoverTime='" + startTurnoverTime + '\'' +
                ", turnoverStartTime='" + turnoverStartTime + '\'' +
                ", turnoverEndTime='" + turnoverEndTime + '\'' +
                ", cashierName='" + cashierName + '\'' +
                ", turnoverState='" + turnoverState + '\'' +
                ", payMentType='" + payMentType + '\'' +
                ", areaType='" + areaType + '\'' +
                ", orderTotalCount='" + orderTotalCount + '\'' +
                ", orderedTotalCount='" + orderedTotalCount + '\'' +
                ", unOrderedTotalCount='" + unOrderedTotalCount + '\'' +
                ", orderedTotalMoney='" + orderedTotalMoney + '\'' +
                ", unOrderedTotalMoney='" + unOrderedTotalMoney + '\'' +
                ", payments='" + payments + '\'' +
                ", dishTypes='" + dishTypes + '\'' +
                ", money0='" + money0 + '\'' +
                ", money1='" + money1 + '\'' +
                ", money2='" + money2 + '\'' +
                ", money3='" + money3 + '\'' +
                ", money4='" + money4 + '\'' +
                ", money5='" + money5 + '\'' +
                ", money6='" + money6 + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", printTime='" + printTime + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public TurnoverHistoryEntity(Long id) {
        this.id = id;
    }

    public TurnoverHistoryEntity(Long id, String turnoverHistoryId, String startTurnoverTime, String turnoverStartTime, String turnoverEndTime, String cashierName, String turnoverState, String payMentType, String areaType, String orderTotalCount, String orderedTotalCount, String unOrderedTotalCount, String orderedTotalMoney, String unOrderedTotalMoney, String payments, String dishTypes, String money0, String money1, String money2, String money3, String money4, String money5, String money6, String operatorName, String printTime, Long createTime) {
        this.id = id;
        this.turnoverHistoryId = turnoverHistoryId;
        this.startTurnoverTime = startTurnoverTime;
        this.turnoverStartTime = turnoverStartTime;
        this.turnoverEndTime = turnoverEndTime;
        this.cashierName = cashierName;
        this.turnoverState = turnoverState;
        this.payMentType = payMentType;
        this.areaType = areaType;
        this.orderTotalCount = orderTotalCount;
        this.orderedTotalCount = orderedTotalCount;
        this.unOrderedTotalCount = unOrderedTotalCount;
        this.orderedTotalMoney = orderedTotalMoney;
        this.unOrderedTotalMoney = unOrderedTotalMoney;
        this.payments = payments;
        this.dishTypes = dishTypes;
        this.money0 = money0;
        this.money1 = money1;
        this.money2 = money2;
        this.money3 = money3;
        this.money4 = money4;
        this.money5 = money5;
        this.money6 = money6;
        this.operatorName = operatorName;
        this.printTime = printTime;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTurnoverHistoryId() {
        return turnoverHistoryId;
    }

    public void setTurnoverHistoryId(String turnoverHistoryId) {
        this.turnoverHistoryId = turnoverHistoryId;
    }

    public String getStartTurnoverTime() {
        return startTurnoverTime;
    }

    public void setStartTurnoverTime(String startTurnoverTime) {
        this.startTurnoverTime = startTurnoverTime;
    }

    public String getTurnoverStartTime() {
        return turnoverStartTime;
    }

    public void setTurnoverStartTime(String turnoverStartTime) {
        this.turnoverStartTime = turnoverStartTime;
    }

    public String getTurnoverEndTime() {
        return turnoverEndTime;
    }

    public void setTurnoverEndTime(String turnoverEndTime) {
        this.turnoverEndTime = turnoverEndTime;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getTurnoverState() {
        return turnoverState;
    }

    public void setTurnoverState(String turnoverState) {
        this.turnoverState = turnoverState;
    }

    public String getPayMentType() {
        return payMentType;
    }

    public void setPayMentType(String payMentType) {
        this.payMentType = payMentType;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getOrderTotalCount() {
        return orderTotalCount;
    }

    public void setOrderTotalCount(String orderTotalCount) {
        this.orderTotalCount = orderTotalCount;
    }

    public String getOrderedTotalCount() {
        return orderedTotalCount;
    }

    public void setOrderedTotalCount(String orderedTotalCount) {
        this.orderedTotalCount = orderedTotalCount;
    }

    public String getUnOrderedTotalCount() {
        return unOrderedTotalCount;
    }

    public void setUnOrderedTotalCount(String unOrderedTotalCount) {
        this.unOrderedTotalCount = unOrderedTotalCount;
    }

    public String getOrderedTotalMoney() {
        return orderedTotalMoney;
    }

    public void setOrderedTotalMoney(String orderedTotalMoney) {
        this.orderedTotalMoney = orderedTotalMoney;
    }

    public String getUnOrderedTotalMoney() {
        return unOrderedTotalMoney;
    }

    public void setUnOrderedTotalMoney(String unOrderedTotalMoney) {
        this.unOrderedTotalMoney = unOrderedTotalMoney;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(String dishTypes) {
        this.dishTypes = dishTypes;
    }

    public String getMoney0() {
        return money0;
    }

    public void setMoney0(String money0) {
        this.money0 = money0;
    }

    public String getMoney1() {
        return money1;
    }

    public void setMoney1(String money1) {
        this.money1 = money1;
    }

    public String getMoney2() {
        return money2;
    }

    public void setMoney2(String money2) {
        this.money2 = money2;
    }

    public String getMoney3() {
        return money3;
    }

    public void setMoney3(String money3) {
        this.money3 = money3;
    }

    public String getMoney4() {
        return money4;
    }

    public void setMoney4(String money4) {
        this.money4 = money4;
    }

    public String getMoney5() {
        return money5;
    }

    public void setMoney5(String money5) {
        this.money5 = money5;
    }

    public String getMoney6() {
        return money6;
    }

    public void setMoney6(String money6) {
        this.money6 = money6;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

}
