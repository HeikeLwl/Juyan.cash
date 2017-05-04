package jufeng.juyancash.syncdata;

import java.util.Date;

public class GoodsVo {

	private String id;
	private int property;
	private String partnerCode;// 所属商家编号
	private String typeId;// 商品分类
	private String name;// 商品名称
	private String code;// 商品编码
	private int price;// 单价
	private String unit;// 结账单位
	private int discount;// 允许打折 0-不允许,1-允许
	private int bill;// 是否打印厨房单据 0-不打印,1-打印
	private int orderCheckoutUnit;// 点菜单位是否与结账单位相同 0-不相同,1-相同
	private String orderUnit;// 点菜单位
	private int jiagongTime;// 加工耗时时间(分钟)
	private int salesTc;// 销售提成 0-不提成,按比例,按固定金额
	private int tcMoney;// 提成金额(元)
	private int fuwuCost;// 收取服务费 0-不收取,1-固定费用,2-商品价格百分比
	private int fuwuMoney;// 费用
	private int changePrice;// 允许收银员在收银时修改单价 0-不允许,1-允许
	private int tuicaiCheck;// 退菜时需要权限验证 0-不需要,1-需要
	private int give;// 可作为赠菜 0-不可以,1-可以
	private int online;// 商品是否上架 0-下架,1-上架
	private String codeOne;
	private String pic;// 菜品图片,多张用逗号隔开
	private int show;// 菜品详情介绍 0-文本模式,1-图文模式
	private String textIntroduce;// 文本模式菜品详情介绍
	private String picIntroduce;// 图文模式菜品详情介绍
	private int recommend;// 是否设置为特色菜 0-否,1-是
	private String recommendDesc;// 特色菜描述
	private int waimai;// 是否允许外卖 0-否,1-是

	private Date createTime;// 创建时间

	public String getCodeOne() {
		return codeOne;
	}

	public void setCodeOne(String codeOne) {
		this.codeOne = codeOne;
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

	public int getBill() {
		return bill;
	}

	public void setBill(int bill) {
		this.bill = bill;
	}

	public int getOrderCheckoutUnit() {
		return orderCheckoutUnit;
	}

	public void setOrderCheckoutUnit(int orderCheckoutUnit) {
		this.orderCheckoutUnit = orderCheckoutUnit;
	}

	public String getOrderUnit() {
		return orderUnit;
	}

	public void setOrderUnit(String orderUnit) {
		this.orderUnit = orderUnit;
	}

	public int getJiagongTime() {
		return jiagongTime;
	}

	public void setJiagongTime(int jiagongTime) {
		this.jiagongTime = jiagongTime;
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

	public int getFuwuCost() {
		return fuwuCost;
	}

	public void setFuwuCost(int fuwuCost) {
		this.fuwuCost = fuwuCost;
	}

	public int getFuwuMoney() {
		return fuwuMoney;
	}

	public void setFuwuMoney(int fuwuMoney) {
		this.fuwuMoney = fuwuMoney;
	}

	public int getChangePrice() {
		return changePrice;
	}

	public void setChangePrice(int changePrice) {
		this.changePrice = changePrice;
	}

	public int getTuicaiCheck() {
		return tuicaiCheck;
	}

	public void setTuicaiCheck(int tuicaiCheck) {
		this.tuicaiCheck = tuicaiCheck;
	}

	public int getGive() {
		return give;
	}

	public void setGive(int give) {
		this.give = give;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getShow() {
		return show;
	}

	public void setShow(int show) {
		this.show = show;
	}

	public String getTextIntroduce() {
		return textIntroduce;
	}

	public void setTextIntroduce(String textIntroduce) {
		this.textIntroduce = textIntroduce;
	}

	public String getPicIntroduce() {
		return picIntroduce;
	}

	public void setPicIntroduce(String picIntroduce) {
		this.picIntroduce = picIntroduce;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public String getRecommendDesc() {
		return recommendDesc;
	}

	public void setRecommendDesc(String recommendDesc) {
		this.recommendDesc = recommendDesc;
	}

	public int getWaimai() {
		return waimai;
	}

	public void setWaimai(int waimai) {
		this.waimai = waimai;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
