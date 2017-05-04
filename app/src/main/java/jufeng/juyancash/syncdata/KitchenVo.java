package jufeng.juyancash.syncdata;

public class KitchenVo {

	private String id;
	private int property;
	private String partnerCode;
	private int type;// 打印机类型
	private String name;// 设备名称
	private String ip;// 打印机IP
	private int paperWidth;// 打印纸宽度 0-58mm,1-76mm,2-80mm
	private int rowNum;// 每行打印字符数
	private int num;// 打印份数
	private int dish;// 一菜一切 0-N,1-Y
	private int zongdan;// 同时打印一份总单0-N,1-Y

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPaperWidth() {
		return paperWidth;
	}

	public void setPaperWidth(int paperWidth) {
		this.paperWidth = paperWidth;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getDish() {
		return dish;
	}

	public void setDish(int dish) {
		this.dish = dish;
	}

	public int getZongdan() {
		return zongdan;
	}

	public void setZongdan(int zongdan) {
		this.zongdan = zongdan;
	}

}
