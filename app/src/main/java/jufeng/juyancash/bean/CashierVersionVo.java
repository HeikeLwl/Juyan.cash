package jufeng.juyancash.bean;


/**
 * 
 * @author wujf
 *
 */
public class CashierVersionVo {

	private String partnerName;//商户名称
	private int cashierVersion; //收银端版本，0-非快餐版，1-快餐版
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public int getCashierVersion() {
		return cashierVersion;
	}
	public void setCashierVersion(int cashierVersion) {
		this.cashierVersion = cashierVersion;
	}
	
	
	
}
