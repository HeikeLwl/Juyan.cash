package jufeng.juyancash.syncdata;

public class SetCashierVo {
	private String id;
	private int property;
	private String partnerCode;
	private int printType;// 选择前台打印机类型0-USB,1-网口
	private String qtIp;// 前台打印机ip
	private int sort;// 打印时单据上的商品排序方式 0-按菜类 1-按点菜时间
	private int sound;// 打单时发出声音 0-N 1-Y
	private String desc;// 客户联尾注
	private String logo;// 店家logo
	private int voucher;// 下单时打印消费底联 0-N 1-Y
	private String ip;// 打印机IP地址
	private int printTypeOne;// 消费底联打印机类型0-USB,1-网口
	private int rowNum;// 每行打印字数
	private int huacai;// 下单时打印划菜联 0-N 1-Y
	private int printTypeTwo;// 划菜联打印机类型0-USB,1-网口
	private String huacaiIp;// 划菜打印机IP
	private int rowNum1;//// 划菜每行打印字数
	private int autoPrintFinance;// 结账完毕后自动打印财务联 0-N 1-Y
	private int autoFinanceBox;// 打印财务联时自动打开钱箱 0-N 1-Y
	private int autoCustomerBox;// 打印客户联时自动打开钱箱 0-N 1-Y
	private int giveGoods;// 点菜单|客户联|财务联是否打印赠送商品 0-N 1-Y
	private int tuicai;// 客户联|财务联 上打印退菜明细 0-N 1-Y
	private int autoPrintOrder;// 下单后是否打印点菜单 0-N 1-Y
	private int qrCode;// 打印二维码0-N 1-Y
	private int printTypeThree;// 非厨打商品打印机类型0-USB,1-网口
	private int fcdGoods;// 非厨打商品打印机 0-N 1-Y
	private String fcdIp;// 非厨打商品打印机ip
	private int rowNum2;// 非厨打每行打印字数

	public SetCashierVo() {
		this.desc = "";
		this.logo = "";
		this.ip = "";
		this.huacaiIp = "";
	}

	public int getPrintType() {
		return printType;
	}

	public void setPrintType(int printType) {
		this.printType = printType;
	}

	public String getQtIp() {
		return qtIp;
	}

	public void setQtIp(String qtIp) {
		this.qtIp = qtIp;
	}

	public int getPrintTypeOne() {
		return printTypeOne;
	}

	public void setPrintTypeOne(int printTypeOne) {
		this.printTypeOne = printTypeOne;
	}

	public int getPrintTypeTwo() {
		return printTypeTwo;
	}

	public void setPrintTypeTwo(int printTypeTwo) {
		this.printTypeTwo = printTypeTwo;
	}

	public int getPrintTypeThree() {
		return printTypeThree;
	}

	public void setPrintTypeThree(int printTypeThree) {
		this.printTypeThree = printTypeThree;
	}

	public int getFcdGoods() {
		return fcdGoods;
	}

	public void setFcdGoods(int fcdGoods) {
		this.fcdGoods = fcdGoods;
	}

	public String getFcdIp() {
		return fcdIp;
	}

	public void setFcdIp(String fcdIp) {
		this.fcdIp = fcdIp;
	}

	public int getRowNum2() {
		return rowNum2;
	}

	public void setRowNum2(int rowNum2) {
		this.rowNum2 = rowNum2;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getSound() {
		return sound;
	}

	public void setSound(int sound) {
		this.sound = sound;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getVoucher() {
		return voucher;
	}

	public void setVoucher(int voucher) {
		this.voucher = voucher;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getHuacai() {
		return huacai;
	}

	public void setHuacai(int huacai) {
		this.huacai = huacai;
	}

	public String getHuacaiIp() {
		return huacaiIp;
	}

	public void setHuacaiIp(String huacaiIp) {
		this.huacaiIp = huacaiIp;
	}

	public int getRowNum1() {
		return rowNum1;
	}

	public void setRowNum1(int rowNum1) {
		this.rowNum1 = rowNum1;
	}

	public int getAutoPrintFinance() {
		return autoPrintFinance;
	}

	public void setAutoPrintFinance(int autoPrintFinance) {
		this.autoPrintFinance = autoPrintFinance;
	}

	public int getAutoFinanceBox() {
		return autoFinanceBox;
	}

	public void setAutoFinanceBox(int autoFinanceBox) {
		this.autoFinanceBox = autoFinanceBox;
	}

	public int getAutoCustomerBox() {
		return autoCustomerBox;
	}

	public void setAutoCustomerBox(int autoCustomerBox) {
		this.autoCustomerBox = autoCustomerBox;
	}

	public int getGiveGoods() {
		return giveGoods;
	}

	public void setGiveGoods(int giveGoods) {
		this.giveGoods = giveGoods;
	}

	public int getTuicai() {
		return tuicai;
	}

	public void setTuicai(int tuicai) {
		this.tuicai = tuicai;
	}

	public int getAutoPrintOrder() {
		return autoPrintOrder;
	}

	public void setAutoPrintOrder(int autoPrintOrder) {
		this.autoPrintOrder = autoPrintOrder;
	}

	public int getQrCode() {
		return qrCode;
	}

	public void setQrCode(int qrCode) {
		this.qrCode = qrCode;
	}

}
