package jufeng.juyancash.bean;


public class OrderTaocanDetailModel{
	private String id;
	private OrderTaocanModel taocan;
	private String goodsId;// 商品id
	private String goodsName;// 商品名称
	private String makeId;// 做法ID
	private String guigeId;// 规格ID
	private String makeName;// 做法名称
	private String guigeName;// 规格名称
	private String typeId;
	private String typeName;
	private int addPrice;// 商品总加价
	private int num;//数量
	private String tcGroupId;//套餐内分组ID
	private String tcGoodsId;//套餐分组内商品id

	public void setTcGoodsId(String tcGoodsId) {
		this.tcGoodsId = tcGoodsId;
	}

	public String getTcGoodsId() {
		return tcGoodsId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getTcGroupId() {
		return tcGroupId;
	}

	public void setTcGroupId(String tcGroupId) {
		this.tcGroupId = tcGroupId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGuigeName() {
		return guigeName;
	}

	public void setGuigeName(String guigeName) {
		this.guigeName = guigeName;
	}

	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}

	public String getGuigeId() {
		return guigeId;
	}

	public void setGuigeId(String guigeId) {
		this.guigeId = guigeId;
	}

	public String getMakeId() {
		return makeId;
	}

	public void setMakeId(String makeId) {
		this.makeId = makeId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public OrderTaocanModel getTaocan() {
		return taocan;
	}

	public void setTaocan(OrderTaocanModel taocan) {
		this.taocan = taocan;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getAddPrice() {
		return addPrice;
	}

	public void setAddPrice(int addPrice) {
		this.addPrice = addPrice;
	}
}
