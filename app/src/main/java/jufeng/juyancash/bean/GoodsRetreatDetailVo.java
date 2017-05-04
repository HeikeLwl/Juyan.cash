package jufeng.juyancash.bean;



/**
 * 退菜VO
 * @author wujf
 *
 */
public class GoodsRetreatDetailVo {
	private String goodsName;//商品名称
	private String goodsId;//商品id
	private double num;//数量
	private int type;//退菜类型 0-菜品,1-套餐
	private int amount;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public double getNum() {
		return num;
	}
	public void setNum(double num) {
		this.num = num;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsName() {
		return goodsName;
	}
}
