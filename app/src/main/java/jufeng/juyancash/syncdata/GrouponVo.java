package jufeng.juyancash.syncdata;

public class GrouponVo {

	private String id;
	private int property;
	private String partnerCode;
	private String name;
	private String code;
	private int settled;

	public int getSettled() {
		return settled;
	}

	public void setSettled(int settled) {
		this.settled = settled;
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

}
