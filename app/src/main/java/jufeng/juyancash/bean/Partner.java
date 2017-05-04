package jufeng.juyancash.bean;


import java.util.Date;

public class Partner {
	private String id;
	private String name; //店家名称
	private String code; //编号
	private String password;
	private String email;//邮箱
	private String contacts; //联系人
	private String phone;//电话
	private String jpushId;//jpushId
	
	private String logo;//店铺logo
	private String kefuTel;//客服电话
	private int timeType; //时间类型0-当日,1-次日
	private String startTime;//营业开始时间
	private String endTime;//营业结束时间
	private String address;//店铺地址
	private int smsNum; //短信数量
	private int wxRedBalance; //微信红包余额
	
	private Date createTime;
	public Partner(){
		this.name="";
		this.code="";
		this.email="";
		this.contacts="";
		this.phone="";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getJpushId() {
		return jpushId;
	}

	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getKefuTel() {
		return kefuTel;
	}

	public void setKefuTel(String kefuTel) {
		this.kefuTel = kefuTel;
	}

	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(int smsNum) {
		this.smsNum = smsNum;
	}

	public int getWxRedBalance() {
		return wxRedBalance;
	}

	public void setWxRedBalance(int wxRedBalance) {
		this.wxRedBalance = wxRedBalance;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
