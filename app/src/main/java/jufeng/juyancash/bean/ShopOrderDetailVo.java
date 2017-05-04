package jufeng.juyancash.bean;

/**
 * 
 * @author wujf
 *
 */
public class ShopOrderDetailVo {
	private String id;
    private String orderId;//订单id
    private String goodsId;//商品id
    private String goodsName;//商品名称
    private int goodsPrice;//商品价格
    private double num;//数量
    private int goodsRate;//商品折扣率
	private int tcAmount;//提成金额
	private String checkoutTime;//结账时间
	private String partnerCode;
	private int isTaocan;
	private int isGive;
	private int couponRate;
	private int realPrice;
	private String goodsMaterial;//商品加料

	public String getGoodsMaterial() {
		return goodsMaterial;
	}

	public void setGoodsMaterial(String goodsMaterial) {
		this.goodsMaterial = goodsMaterial;
	}

	public int getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(int realPrice) {
		this.realPrice = realPrice;
	}

	public void setCouponRate(int couponRate) {
		this.couponRate = couponRate;
	}

	public int getCouponRate() {
		return couponRate;
	}

	public void setIsGive(int isGive) {
		this.isGive = isGive;
	}

	public int getIsGive() {
		return isGive;
	}

	public void setIsTaocan(int isTaocan) {
		this.isTaocan = isTaocan;
	}

	public int getIsTaocan() {
		return isTaocan;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

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
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(int goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public double getNum() {
		return num;
	}
	public void setNum(double num) {
		this.num = num;
	}
	public int getGoodsRate() {
		return goodsRate;
	}
	public void setGoodsRate(int goodsRate) {
		this.goodsRate = goodsRate;
	}
	public int getTcAmount() {
		return tcAmount;
	}
	public void setTcAmount(int tcAmount) {
		this.tcAmount = tcAmount;
	}
	public String getCheckoutTime() {
		return checkoutTime;
	}
	public void setCheckoutTime(String checkoutTime) {
		this.checkoutTime = checkoutTime;
	}
  
	
}
