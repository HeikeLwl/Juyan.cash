package jufeng.juyancash.bean;

/**
 * 
 * @author wujf
 *
 */
public class ShopOrderPayVo {
    private String id;
    private String partnerCode;//商家编号
    private String orderId;//订单号
    private String payId;//支付类型id
    private int payType;//支付类型
    private String payName;//支付类型名称
    private int payMoney;//支付金额
    private String payTime; //支付时间
    private String checkoutTime;//结账时间
    private int groupBalance ;//团购套餐结算金额
    
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	
	
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public int getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(int payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getCheckoutTime() {
		return checkoutTime;
	}
	public void setCheckoutTime(String checkoutTime) {
		this.checkoutTime = checkoutTime;
	}
	public int getGroupBalance() {
		return groupBalance;
	}
	public void setGroupBalance(int groupBalance) {
		this.groupBalance = groupBalance;
	}

    
   
}
