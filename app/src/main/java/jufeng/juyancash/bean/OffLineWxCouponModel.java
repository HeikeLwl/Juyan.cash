package jufeng.juyancash.bean;

public class OffLineWxCouponModel{
	private String couponId;
	/** 批次 */// (预留)
	private String batch;
	/** 代金券主题 **/
	private String theme;
	/** 减免金额 */
	private int faceValue;
	/** 满减 */
	private int fullCut;
	/** 优惠券类型 0-满减券,1-代金券 2-打折券 */
	private int type;
	/** 是否可以和会员卡打折同时用 */
	private int disVip;
	/** 无论套餐和VIP都可以和优惠券同时打折 0 - 否 1 - 是 **/
	private int forceDis;

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public int getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(int faceValue) {
		this.faceValue = faceValue;
	}

	public int getFullCut() {
		return fullCut;
	}

	public void setFullCut(int fullCut) {
		this.fullCut = fullCut;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDisVip() {
		return disVip;
	}

	public void setDisVip(int disVip) {
		this.disVip = disVip;
	}

	public int getForceDis() {
		return forceDis;
	}

	public void setForceDis(int forceDis) {
		this.forceDis = forceDis;
	}
}
