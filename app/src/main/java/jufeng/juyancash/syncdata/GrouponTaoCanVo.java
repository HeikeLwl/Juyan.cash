package jufeng.juyancash.syncdata;

public class GrouponTaoCanVo {
	private String id;
	private int property;
	private String partnerCode;
	private String grouponId;// 团购方案
	private String name;// 套餐名称
	private int price;// 套餐价格
	private int balance;// 套餐结算金额
	private int cashier;// 是否添加到前台收银 0-不添加, 1-添加
	private int online;// 是否上架 0-下架 1-上架

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

	public String getGrouponId() {
		return grouponId;
	}

	public void setGrouponId(String grouponId) {
		this.grouponId = grouponId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getCashier() {
		return cashier;
	}

	public void setCashier(int cashier) {
		this.cashier = cashier;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

}
