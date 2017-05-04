package jufeng.juyancash.syncdata;

public class CashierEstimateVo {
	private String id;// char(36) NOT NULL主键
	private String partnerCode;// char(36) NOT NULL商家编号
	private String dishId;// char(36) NOT NULL菜品id
	private int isSellOut;// int(11) NULL是否已售完
	private String unitName;// varchar(50) NULL单位
	private int type;// int(11) NULL是否是套餐,0-非套餐,1-套餐
	private int property;// int(11) NULL属性

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
		this.property = property;
	}

}
