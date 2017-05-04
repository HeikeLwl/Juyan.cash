package jufeng.juyancash.syncdata;

public class ReceivablesVo {
	private String id;
	private int property;
	private String partnerCode;// 商家编号
	private int type;// 账号类型 0个人账号 1公司账号
	private String bank;// 开户银行
	private String province;// 开户省份
	private String city;// 开户城市
	private String bankBranch;// 开户支行
	private String name;// 开户人姓名
	private String bankCode;// 银行账号
	private int idType;// 证件类型 0-身份证,1-护照
	private String idNo;// 证件号码
	private String tel;// 手机号

	public ReceivablesVo() {
		this.bank = "";
		this.province = "";
		this.city = "";
		this.bankBranch = "";
		this.name = "";
		this.bankCode = "";
		this.idNo = "";
		this.tel = "";
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public int getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
