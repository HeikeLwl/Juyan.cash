package jufeng.juyancash.bean;

public class OrderPayModel{
	private String id;
	private String orderId;//订单号
	private String payId;//支付类型id
	private String payName;//支付类型名称
	private int payType;//支付类型,0-现金,1-银行卡,2-微信支付,3-支付宝支付,4-团购,5-会员卡,6-挂账
	private double payMoney;//支付金额
	private String payTime;//支付时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}
}
