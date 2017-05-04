package jufeng.juyancash.bean;

public class GoodsDetailVo {
	private String id;//主键
	private String orderDetailId;//商品详情id
	private String goodsId;//商品id
	private String goodsName;//商品名称
	private String makeId; //做法id,多个用逗号隔开
	private String guigeId; //规格id,多个用逗号隔开
	private  int goodsPrice;//商品价格
	private  double num;//数量
	private  int isGive;//是否赠菜0-否,1-是
	private String makeName;//做法名称
	private String guigeName;//规格名称
	private String typeId;//分类ID
	private int discount;//是否允许打折

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
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
	public int getIsGive() {
		return isGive;
	}
	public void setIsGive(int isGive) {
		this.isGive = isGive;
	}
	public String getMakeName() {
		return makeName;
	}
	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
	public String getGuigeName() {
		return guigeName;
	}
	public void setGuigeName(String guigeName) {
		this.guigeName = guigeName;
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
	

}
