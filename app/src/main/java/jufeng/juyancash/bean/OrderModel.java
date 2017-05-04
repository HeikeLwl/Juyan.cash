package jufeng.juyancash.bean;

import java.util.Date;
import java.util.Set;

public class OrderModel{
	private String id;
	private static final long serialVersionUID = 1L;

	//支付类型:0-现金,1-银行卡,2-微信支付,3-支付宝消费,4-团购,5-会员充值(消费),6-挂账
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

	//订单状态:
	public static final int ORDER_FINISH=3;

	public static final int NOT_PAY_ORDER=-1;

	private String partnerCode;//商家编号
	private String openId;//微信openid
	private String orderNo;//订单号
	private String remark; //备注
	private int totalPrice;//总价格
	private String sendTime; //送餐时间
	private  Integer orderState;//订单状态 -1,代付款,0,未接单,1-已接单,2-配送中,3.订单完成
	private String userName;//用户姓名
	private String address;//外卖地址
	private String tel;//用户电话
	private Date createTime; //创建时间
	private String dpIp;//发起支付的IP
	private int payFee; //支付金额
	private Date payTime; //付款时间
	private String payBank; //支付代码CFT表示微支付
	private String transactionId; //微信支付流水号
	private String payOpenId;//支付公众号平台下的用户openId
	private String payNo;//支付流水号
	private Integer orderType;//订单类型
	private String pic;//图片
	private Integer payStatus;//0,未付款,1-已付款
	private Integer payType;//支付类型
	private Date updateTime;//最后修改时间
	private int couponDisAmount;//优惠券优惠金额
	private int vipDisAmount;//会员卡优惠金额
	private int mantissa;//不吉利尾数
	private int ml;//抹零
	private String vipNo;//会员卡卡号
	private int vipType;//会员卡类型
	private int yhAmount;//优惠总金额
	private int vipDis;//是否有会员卡优惠
	private WxCouponModel wxCoupon;

	//------到店点餐---
	private int source;//订单来源，0，外卖，1-到店点餐
	private String tableNo;//桌号
	private Set<OrderDetailModel> orderDetail;//订单详情
	/** 是否用优惠券 */
	private Integer useCoupon;
	/** 优惠券ID */
	private String couponId;
	/**
	 * 支付类型ID
	 */
	private String payTypeId;
	/**
	 * 支付类型名称
	 */
	private String payTypeName;
	/** 当天序列号 */
	private Integer series;
	private OrderExtraModel extral;

	public OrderExtraModel getExtral() {
		return extral;
	}

	public void setExtral(OrderExtraModel extra) {
		this.extral = extra;
	}

	public WxCouponModel getWxCoupon() {
		return wxCoupon;
	}

	public void setWxCoupon(WxCouponModel wxCoupon) {
		this.wxCoupon = wxCoupon;
	}

	public int getVipDis() {
		return vipDis;
	}

	public void setVipDis(int vipDis) {
		this.vipDis = vipDis;
	}

	public int getYhAmount() {
		return yhAmount;
	}

	public void setYhAmount(int yhAmount) {
		this.yhAmount = yhAmount;
	}

	public int getCouponDisAmount() {
		return couponDisAmount;
	}

	public void setCouponDisAmount(int couponDisAmount) {
		this.couponDisAmount = couponDisAmount;
	}

	public int getVipDisAmount() {
		return vipDisAmount;
	}

	public void setVipDisAmount(int vipDisAmount) {
		this.vipDisAmount = vipDisAmount;
	}

	public int getMantissa() {
		return mantissa;
	}

	public void setMantissa(int mantissa) {
		this.mantissa = mantissa;
	}

	public int getMl() {
		return ml;
	}

	public void setMl(int ml) {
		this.ml = ml;
	}

	public String getVipNo() {
		return vipNo;
	}

	public void setVipNo(String vipNo) {
		this.vipNo = vipNo;
	}

	public int getVipType() {
		return vipType;
	}

	public void setVipType(int vipType) {
		this.vipType = vipType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static int getPayMoney() {
		return PAY_MONEY;
	}

	public static int getPayBank() {
		return PAY_BANK;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPayOpenId() {
		return payOpenId;
	}

	public void setPayOpenId(String payOpenId) {
		this.payOpenId = payOpenId;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public void setSeries(int series) {
		this.series = series;
	}

	public int getSeries() {
		return series;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Set<OrderDetailModel> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(Set<OrderDetailModel> orderDetail) {
		this.orderDetail = orderDetail;
	}

	public Integer getUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(Integer useCoupon) {
		this.useCoupon = useCoupon;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(String payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public static int getPayWxpay() {
		return PAY_WXPAY;
	}

	public static int getPayAlipay() {
		return PAY_ALIPAY;
	}

	public static int getPayGroup() {
		return PAY_GROUP;
	}

	public static int getPayVippay() {
		return PAY_VIPPAY;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDpIp() {
		return dpIp;
	}

	public void setDpIp(String dpIp) {
		this.dpIp = dpIp;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPayFee() {
		return payFee;
	}

	public void setPayFee(int payFee) {
		this.payFee = payFee;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public void setSeries(Integer series) {
		this.series = series;
	}
}
