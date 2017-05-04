package jufeng.juyancash.syncdata;

public class ShopMealsVo {
	private String id;
	private int property;
	private String partnerCode;
	private int state;// 是否开启限时用餐 0-关闭,1-开启
	private String mealsTime;// 限制用餐时间
	private String remindTime;// 限制用餐时间提醒

	public ShopMealsVo() {
		this.mealsTime = "";
		this.remindTime = "";
	}

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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMealsTime() {
		return mealsTime;
	}

	public void setMealsTime(String mealsTime) {
		this.mealsTime = mealsTime;
	}

	public String getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(String remindTime) {
		this.remindTime = remindTime;
	}

}
