package jufeng.juyancash.bean;

import java.util.List;


public class GiveGoodsVo {

	private String partnerCode;//所属商家编号
	private String orderId;//订单id
	private List<OrderDetailModel> goodsList;
	private List<OrderTaocanModel> taocanList;
	private int totalAmount;//订单价格
	private int yhAmount;//优惠金额
	private int payAmount;//已支付金额

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
	public List<OrderDetailModel> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<OrderDetailModel> goodsList) {
		this.goodsList = goodsList;
	}
	
	public List<OrderTaocanModel> getTaocanList() {
		return taocanList;
	}
	public void setTaocanList(List<OrderTaocanModel> taocanList) {
		this.taocanList = taocanList;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getYhAmount() {
		return yhAmount;
	}
	public void setYhAmount(int yhAmount) {
		this.yhAmount = yhAmount;
	}
	public int getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}
	
	
	
}
