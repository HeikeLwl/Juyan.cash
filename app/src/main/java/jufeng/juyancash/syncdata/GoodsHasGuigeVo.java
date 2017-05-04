package jufeng.juyancash.syncdata;

public class GoodsHasGuigeVo {

	private String id;
	private int property;
	private String partnerCode;// 商家编号
	private String goodsId;// 商品
	private String guigeId;// 规格
	private int defaultPrice;// 默认价格
	private int customPrice;// 自定义价格

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

	public String getGuigeId() {
		return guigeId;
	}

	public void setGuigeId(String guigeId) {
		this.guigeId = guigeId;
	}

	public int getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(int defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	public int getCustomPrice() {
		return customPrice;
	}

	public void setCustomPrice(int customPrice) {
		this.customPrice = customPrice;
	}
}
