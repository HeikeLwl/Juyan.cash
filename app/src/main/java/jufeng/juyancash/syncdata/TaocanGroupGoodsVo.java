package jufeng.juyancash.syncdata;

public class TaocanGroupGoodsVo {

	private String id;
	private int property;
	private String partnerCode;// 商家编号
	private String groupId;// 所属分组
	private String goodsId; // 商品ID
	private String goodsName; // 商品名称
	private String goodsGuigeId; // 商品规格ID
	private int addPrice; // 商品加价
	private int goodsNum; // 商品数量

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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

	public String getGoodsGuigeId() {
		return goodsGuigeId;
	}

	public void setGoodsGuigeId(String goodsGuigeId) {
		this.goodsGuigeId = goodsGuigeId;
	}

	public int getAddPrice() {
		return addPrice;
	}

	public void setAddPrice(int addPrice) {
		this.addPrice = addPrice;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

}
