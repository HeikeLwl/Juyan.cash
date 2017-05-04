package jufeng.juyancash.syncdata;

public class GoodsHasMakeVo {
	
	
	private String id;
	private int property;
	private String partnerCode;// 商家编号
	private String goodsId;// 商品
	private String makeId;// 做法
	private int addPriceType;// 加价模式 0-不加价,1-一次性加价,2-每购买单位加价,3-每结账单位加价
	private int makeAddPrice;// 做法加价

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
		this.property = property;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getMakeId() {
		return makeId;
	}

	public void setMakeId(String makeId) {
		this.makeId = makeId;
	}

	public int getAddPriceType() {
		return addPriceType;
	}

	public void setAddPriceType(int addPriceType) {
		this.addPriceType = addPriceType;
	}

	public int getMakeAddPrice() {
		return makeAddPrice;
	}

	public void setMakeAddPrice(int makeAddPrice) {
		this.makeAddPrice = makeAddPrice;
	}

}
