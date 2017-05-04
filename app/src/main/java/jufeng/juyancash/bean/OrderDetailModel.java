package jufeng.juyancash.bean;

public class OrderDetailModel {
	private String id;
	private OrderModel order;//订单
	private String goodsId;//商品id
	private String goodsName;//商品名称
	private String makeId;  //做法id
	private String guigeId; //规格id
	private int goodsPrice;//商品价格
	private double num;//数量
	/** 做法名称 */
	private String makeName;
	/** 规格名称 */
	private String guigeName;
	/** 分类ID */
	private String typeId;
	/** 是否允许打折 */
	private int discount;
	private String material;

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrderModel getOrder() {
		return order;
	}

	public void setOrder(OrderModel order) {
		this.order = order;
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

	public String getMakeId() {
		return makeId;
	}

	public void setMakeId(String makeId) {
		this.makeId = makeId;
	}

	public String getGuigeId() {
		return guigeId;
	}

	public void setGuigeId(String guigeId) {
		this.guigeId = guigeId;
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
}