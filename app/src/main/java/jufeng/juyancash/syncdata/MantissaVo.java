package jufeng.juyancash.syncdata;
public class MantissaVo {

	private String id;
	private int property;
	private String partnerCode;
	private int mantissa; //尾数
	private int reduce; //扣减额
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
	public int getMantissa() {
		return mantissa;
	}
	public void setMantissa(int mantissa) {
		this.mantissa = mantissa;
	}
	public int getReduce() {
		return reduce;
	}
	public void setReduce(int reduce) {
		this.reduce = reduce;
	}
	
}
