package jufeng.juyancash.bean;

public class CashierPayLogModel{
	private String id;
	private String orderId;//订单ID
	private String vipNo;//会员卡号
	private int payAmount;//实付款
	private int totleAmount;//总价格
	private int yhAmount;//优惠金额

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

	public String getVipNo() {
		return vipNo;
	}

	public void setVipNo(String vipNo) {
		this.vipNo = vipNo;
	}

	public int getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}

	public int getTotleAmount() {
		return totleAmount;
	}

	public void setTotleAmount(int totleAmount) {
		this.totleAmount = totleAmount;
	}

	public int getYhAmount() {
		return yhAmount;
	}

	public void setYhAmount(int yhAmount) {
		this.yhAmount = yhAmount;
	}
}
