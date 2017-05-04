package jufeng.juyancash.syncdata;

public class PaymentVo {

	private String id;
	private int property;
	private String partnerCode;
	private int type;// 支付类型 0-现金，1-银行卡,2-电子支付,3-挂账
	private String name;// 付款方式名称
	private int sale;// 是否计入销售额 0-不计入 1-计入
	private int cashbox;// 是否打开钱箱 0-不开启 1-开启
	private int scanCode;// 是否打开扫码枪0-不开启 1-开启
	private int nece;// 是否可以修改0-否 1-是

	public PaymentVo() {
		this.name = "";
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSale() {
		return sale;
	}

	public void setSale(int sale) {
		this.sale = sale;
	}

	public int getCashbox() {
		return cashbox;
	}

	public void setCashbox(int cashbox) {
		this.cashbox = cashbox;
	}

	public int getScanCode() {
		return scanCode;
	}

	public void setScanCode(int scanCode) {
		this.scanCode = scanCode;
	}

	public int getNece() {
		return nece;
	}

	public void setNece(int nece) {
		this.nece = nece;
	}

}
