package jufeng.juyancash.bean;


import java.util.List;

public class OrderVo {
    private String id;
    private String partnerCode;//商家编号
    private String orderNo;//订单流水号
    private String deskCode;//桌位编号
    private String deskName;//桌位名称
    private String openTime;//开单时间
    private String checkoutTime;//结账时间
    private int personNum;//人数
    private double totalPrice;//总价格
    private int orderState;//订单状态
    private String employeeId;//收银员id
    private String employeeName;//收银员名称
    private List<OrderDetailVo> orderDetail;//订单详情
    private List<OrderPayVo> orderPay;//支付详情


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeskCode() {
        return deskCode;
    }

    public void setDeskCode(String deskCode) {
        this.deskCode = deskCode;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public List<OrderDetailVo> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetailVo> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public List<OrderPayVo> getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(List<OrderPayVo> orderPay) {
        this.orderPay = orderPay;
    }


}
