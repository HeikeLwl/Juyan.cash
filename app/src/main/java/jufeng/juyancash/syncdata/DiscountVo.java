package jufeng.juyancash.syncdata;

public class DiscountVo {

	private String id;
	private int property;
	private String partnerCode;
	private String name;// 方案名称
	private int state;// 优惠方式 0-打折, 1-使用会员
	private int percentage;// 折扣率(%)
	private int enforcement;// 设置对不允许打折商品也打折(0-N 1-Y)
	private int percentageSame;// 商品折扣相同 0-不相同 ,1-相同
	private int employeePercentage;// 全部员工可使用折扣 0指定员工使用 1全部使用
	private int dateAlidity;// 日期内有效 0-关闭 , 1-开启
	private String dateBegin;//
	private String dateEnd;//
	private int timeAlidity;// 时间内有效 0-关闭 , 1-开启
	private String timeBegin;//
	private String timeEnd;//
	private int weekDateAlidity;// 每周特定日期有效 0-关闭 , 1-开启
	private String date;//

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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public int getEnforcement() {
		return enforcement;
	}

	public void setEnforcement(int enforcement) {
		this.enforcement = enforcement;
	}

	public int getPercentageSame() {
		return percentageSame;
	}

	public void setPercentageSame(int percentageSame) {
		this.percentageSame = percentageSame;
	}

	public int getEmployeePercentage() {
		return employeePercentage;
	}

	public void setEmployeePercentage(int employeePercentage) {
		this.employeePercentage = employeePercentage;
	}

	public int getDateAlidity() {
		return dateAlidity;
	}

	public void setDateAlidity(int dateAlidity) {
		this.dateAlidity = dateAlidity;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public int getTimeAlidity() {
		return timeAlidity;
	}

	public void setTimeAlidity(int timeAlidity) {
		this.timeAlidity = timeAlidity;
	}

	public String getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public int getWeekDateAlidity() {
		return weekDateAlidity;
	}

	public void setWeekDateAlidity(int weekDateAlidity) {
		this.weekDateAlidity = weekDateAlidity;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
