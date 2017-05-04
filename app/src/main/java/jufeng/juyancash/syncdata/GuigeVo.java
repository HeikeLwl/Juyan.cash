package jufeng.juyancash.syncdata;

public class GuigeVo {

	private String id;
	private int property;
	private String partnerCode;// 所属商家编号
	private String name;// 规格名称
	private double materiaMultiple;// 规格用料是基准商品的倍数
	private double priceMultiple;// 规格价格是基准商品的倍数
	

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

	public double getMateriaMultiple() {
		return materiaMultiple;
	}

	public void setMateriaMultiple(double materiaMultiple) {
		this.materiaMultiple = materiaMultiple;
	}

	public double getPriceMultiple() {
		return priceMultiple;
	}

	public void setPriceMultiple(double priceMultiple) {
		this.priceMultiple = priceMultiple;
	}

}
