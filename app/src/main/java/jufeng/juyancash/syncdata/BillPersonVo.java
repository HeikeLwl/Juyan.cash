package jufeng.juyancash.syncdata;


public class BillPersonVo {
	
	private String id;
	private int property;
	private String partnerCode;//商家编号
	private String billId;//挂账
	private int type;//挂账人类型0-单位,1-个人
	private String name;//挂账人名称
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
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
