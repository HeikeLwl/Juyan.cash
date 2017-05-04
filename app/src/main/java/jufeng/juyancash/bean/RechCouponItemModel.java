package jufeng.juyancash.bean;

public class RechCouponItemModel{

	private String id;
	
	private String partnerCode;
	
	private RechCouponModel rechCoupon;
	
	private Double mianzhi;
	
	private Double zengsong;
	
	private Integer times;

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

	public RechCouponModel getRechCoupon() {
		return rechCoupon;
	}

	public void setRechCoupon(RechCouponModel rechCoupon) {
		this.rechCoupon = rechCoupon;
	}

	public Double getMianzhi() {
		return mianzhi;
	}

	public void setMianzhi(Double mianzhi) {
		this.mianzhi = mianzhi;
	}

	public Double getZengsong() {
		return zengsong;
	}

	public void setZengsong(Double zengsong) {
		this.zengsong = zengsong;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}
}
