package jufeng.juyancash.syncdata;

public class TaocanGroupVo {
	private String id;
	private int property;
	private String partnerCode;// 商家编号
	private String taocanId;// 所属套餐
	private String name; // 分组名称
	private int mode; // 点菜方式 0-允许顾客自选,1-必修全部选择
	private int num; // 允许点的数量

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

	public String getTaocanId() {
		return taocanId;
	}

	public void setTaocanId(String taocanId) {
		this.taocanId = taocanId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
