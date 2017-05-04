package jufeng.juyancash.bean;

import java.util.List;


/**
 * 退菜VO
 * @author wujf
 *
 */
public class GoodsRetreatVo {
	private int giveAmount;
	private String partnerCode;//商家编号
	private String orderId;//订单id
	private List<GoodsRetreatDetailVo> goodsList;//商品
	private List<GoodsRetreatDetailVo> taocanList;//套餐
	private int totalAmount;//订单价格
	private int yhAmount;//优惠金额
	private int payAmount;//已支付金额
	private int couzheng;//代金券凑整金额

	public int getGiveAmount() {
		return giveAmount;
	}

	public void setGiveAmount(int giveAmount) {
		this.giveAmount = giveAmount;
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

	
	public List<GoodsRetreatDetailVo> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsRetreatDetailVo> goodsList) {
		this.goodsList = goodsList;
	}
	public List<GoodsRetreatDetailVo> getTaocanList() {
		return taocanList;
	}
	public void setTaocanList(List<GoodsRetreatDetailVo> taocanList) {
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

	public int getCouzheng() {
		return couzheng;
	}

	public void setCouzheng(int couzheng) {
		this.couzheng = couzheng;
	}
}
