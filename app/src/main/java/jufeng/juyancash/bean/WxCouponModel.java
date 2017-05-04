package jufeng.juyancash.bean;

/**
 * 
 * @author wuzhisheng
 *
 */
public class WxCouponModel{
	private String id;
	/** 减免金额*/
	private int faceValue;
	/** 满减 */
	private int fullCut;
	/** 优惠券类型 0：满减；1：代金；2：折扣券*/
	private int type;
	/** 是否和VIP可以同时使用   0 - 否 1 - 是 **/
	private int disVip;
	/** 无论套餐和VIP都可以和优惠券同时打折  0 - 否 1 - 是 **/
	private int forceDis;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
