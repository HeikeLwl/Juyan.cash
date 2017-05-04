package jufeng.juyancash.syncdata;

import java.util.Date;

public class TaocanVo {
	private String id;
	private int property;
	private String partnerCode;
	private String typeId;
	private String name;
	private String code;
	private int price;
	private String unit;
	private int discount;
	private int salesTc;// 销售提成 0-不提成,1-按比例,2-按固定金额
	private int tcMoney;// 提成金额(元)
	private int tuicaiCheck;
	private int online;
	private String codeOne;

	public String getCodeOne() {
		return codeOne;
	}

	public void setCodeOne(String codeOne) {
		this.codeOne = codeOne;
	}

	private String introduce;// 套餐介绍
	private String pic;// 套餐图片
	private Date createTime;// 创建时间

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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getSalesTc() {
		return salesTc;
	}

	public void setSalesTc(int salesTc) {
		this.salesTc = salesTc;
	}

	public int getTcMoney() {
		return tcMoney;
	}

	public void setTcMoney(int tcMoney) {
		this.tcMoney = tcMoney;
	}

	public int getTuicaiCheck() {
		return tuicaiCheck;
	}

	public void setTuicaiCheck(int tuicaiCheck) {
		this.tuicaiCheck = tuicaiCheck;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
