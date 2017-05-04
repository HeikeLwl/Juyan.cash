package jufeng.juyancash.syncdata;

public class PrinterVo {

	private String id;
	private int property;
	private String partnerCode;// 商家编号
	private String ip;// 原ip
	private String spareIp;// 备用ip

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSpareIp() {
		return spareIp;
	}

	public void setSpareIp(String spareIp) {
		this.spareIp = spareIp;
	}

}
