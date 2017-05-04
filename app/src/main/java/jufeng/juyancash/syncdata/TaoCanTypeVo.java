package jufeng.juyancash.syncdata;

public class TaoCanTypeVo {

	private String id;
	private int property;
	private String partnerCode;// 商家编号
	private String name;// 名称
	private int addParent;
	private String parentId;
	private String code;
	private int salesOther;
	private String otherType;
	private int salesTc;
	private int tcMoney;// 提成金额(元)
	private String remark;
	private int isChild; // 是否有子类 0-没有，1-有
	private int level;// 等级0顶级

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

	public int getAddParent() {
		return addParent;
	}

	public void setAddParent(int addParent) {
		this.addParent = addParent;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getSalesOther() {
		return salesOther;
	}

	public void setSalesOther(int salesOther) {
		this.salesOther = salesOther;
	}

	public String getOtherType() {
		return otherType;
	}

	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}

	public int getSalesTc() {
		return salesTc;
	}

	public void setSalesTc(int salesTc) {
		this.salesTc = salesTc;
	}

	public int getTcMoney() {
		return tcMoney;
	}

	public void setTcMoney(int tcMoney) {
		this.tcMoney = tcMoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getIsChild() {
		return isChild;
	}

	public void setIsChild(int isChild) {
		this.isChild = isChild;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
