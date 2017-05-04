package jufeng.juyancash.bean;

public class EstimateModel {
	private String id;
	private String partnerCode;//商家编号
	private String dishId;//菜品id
	private int isSellOut;//是否已售完
	private int type;//是否是套餐,0-非套餐,1-套餐
	private String unitName;//单位名称

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getDishId() {
		return dishId;
	}

	public void setDishId(String dishId) {
		this.dishId = dishId;
	}

	public int getIsSellOut() {
		return isSellOut;
	}

	public void setIsSellOut(int isSellOut) {
		this.isSellOut = isSellOut;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
