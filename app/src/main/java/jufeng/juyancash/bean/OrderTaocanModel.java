package jufeng.juyancash.bean;

import java.util.Set;

public class OrderTaocanModel{
	private String id;
	private OrderModel order;// 订单
	private String taocanId;// 商品id
	private String taocanName;// 商品名称
	private int taocanPrice;// 商品价格
	private double num;// 数量
	// 套餐分类名称:
	private String tcTypeName;
	// 套餐分类id:
	private String tcTypeId;
	private Set<OrderTaocanDetailModel> goods;

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

	public String getTaocanId() {
		return taocanId;
	}

	public void setTaocanId(String taocanId) {
		this.taocanId = taocanId;
	}

	public String getTaocanName() {
		return taocanName;
	}

	public void setTaocanName(String taocanName) {
		this.taocanName = taocanName;
	}

	public int getTaocanPrice() {
		return taocanPrice;
	}

	public void setTaocanPrice(int taocanPrice) {
		this.taocanPrice = taocanPrice;
	}

	public double getNum() {
		return num;
	}

	public void setNum(double num) {
		this.num = num;
	}

	public String getTcTypeName() {
		return tcTypeName;
	}

	public void setTcTypeName(String tcTypeName) {
		this.tcTypeName = tcTypeName;
	}

	public String getTcTypeId() {
		return tcTypeId;
	}

	public void setTcTypeId(String tcTypeId) {
		this.tcTypeId = tcTypeId;
	}

	public Set<OrderTaocanDetailModel> getGoods() {
		return goods;
	}

	public void setGoods(Set<OrderTaocanDetailModel> goods) {
		this.goods = goods;
	}
}
