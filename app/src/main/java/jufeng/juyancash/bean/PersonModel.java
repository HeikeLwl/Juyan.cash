package jufeng.juyancash.bean;

import java.util.Date;
public class PersonModel{
	private String id;
	private String partnerCode;//商家编号
	private String name;//姓名
	private String tel;//手机号
	private String headUrl;//头像url
	private int tcAmount;//每单提成金额(分)
	private int sendSms;//配单时短信通知配送员 0-不发送,1-发送
	private Date createTime;//创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public int getTcAmount() {
		return tcAmount;
	}

	public void setTcAmount(int tcAmount) {
		this.tcAmount = tcAmount;
	}

	public int getSendSms() {
		return sendSms;
	}

	public void setSendSms(int sendSms) {
		this.sendSms = sendSms;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
