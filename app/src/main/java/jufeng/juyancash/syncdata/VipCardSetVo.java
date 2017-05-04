package jufeng.juyancash.syncdata;

public class VipCardSetVo {

	
	private String id;
	private int property;
	private String partnerCode;// 商家编号
	private int cardType;// 会员卡类型 0-银卡,1-金卡,2-砖石卡
	private int discount;// 优惠类型 0-不打折,1-打折
	private int discountRate;// 折扣率
	private int giveSet;// 领取设置0-免费领取,1-通过升级获取
	private int upgradeAmount;// 升级消费金额
	private int oneUpgradeAmount;// 一次性充值升级金额
	private int upgradeScore;
	private String pic;
	private String vipName;

	
	
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

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(int discountRate) {
		this.discountRate = discountRate;
	}

	public int getGiveSet() {
		return giveSet;
	}

	public void setGiveSet(int giveSet) {
		this.giveSet = giveSet;
	}

	public int getUpgradeAmount() {
		return upgradeAmount;
	}

	public void setUpgradeAmount(int upgradeAmount) {
		this.upgradeAmount = upgradeAmount;
	}

	public int getOneUpgradeAmount() {
		return oneUpgradeAmount;
	}

	public void setOneUpgradeAmount(int oneUpgradeAmount) {
		this.oneUpgradeAmount = oneUpgradeAmount;
	}

	public int getUpgradeScore() {
		return upgradeScore;
	}

	public void setUpgradeScore(int upgradeScore) {
		this.upgradeScore = upgradeScore;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

}
