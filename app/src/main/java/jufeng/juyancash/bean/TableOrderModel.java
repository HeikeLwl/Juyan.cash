package jufeng.juyancash.bean;

import java.util.Date;
import java.util.Set;

public class TableOrderModel{
	private String  id;

	// 支付类型:0-现金,1-银行卡,2-微信支付,3-支付宝消费,4-团购,5-会员充值(消费),6-挂账
	/** 现金消费 */
	public static final int PAY_MONEY = 0;
	/** 银行卡消费 */
	public static final int PAY_BANK = 1;
	/** 微信消费 */
	public static final int PAY_WXPAY = 2;
	/** 支付宝消费 */
	public static final int PAY_ALIPAY = 3;
	/** 团购消费 */
	public static final int PAY_GROUP = 4;
	/** 会员消费 */
	public static final int PAY_VIPPAY = 5;

	// 订单状态:
	public static final int ORDER_FINISH = 3;

	public static final int NOT_PAY_ORDER = -1;

	private String partnerCode;// 商家编号
	private String orderNo;// 订单号
	private String remark; // 备注
	private int totalPrice;// 总价格
	private Date createTime; // 创建时间
	private String vipNo;//会员卡卡号
	private int vipType;//会员卡类型
	private Set<OrderDetailModel> orderDetail;// 订单详情
	private Set<OrderTaocanModel> orderTaocan;//套餐

	private String tableNo;// 桌号

	public int getVipType() {
		return vipType;
	}

	public void setVipType(int vipType) {
		this.vipType = vipType;
	}

	public String getVipNo() {
		return vipNo;
	}

	public void setVipNo(String vipNo) {
		this.vipNo = vipNo;
	}

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<OrderDetailModel> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(Set<OrderDetailModel> orderDetail) {
		this.orderDetail = orderDetail;
	}

	public Set<OrderTaocanModel> getOrderTaocan() {
		return orderTaocan;
	}

	public void setOrderTaocan(Set<OrderTaocanModel> orderTaocan) {
		this.orderTaocan = orderTaocan;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}
}
