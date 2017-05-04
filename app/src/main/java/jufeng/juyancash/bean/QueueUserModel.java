package jufeng.juyancash.bean;

import java.util.Date;

public class QueueUserModel {
	private String id;
	/**varchar(36) NULL排队openid */
	private String openid;
	/**int(11) NULL排队号码 */
	private int number;
	/**int(11) NULL取号时间 */
	private Date createTime;
	/**int(11) NULL人数 */
	private int people;
	/** 商家编号 */
	private String partnerCode;
	/** 显示排队数字号码*/
	private int viewNumber;//
	/**显示排队字母序号 */
	private int letter;//
	/**用户看到的取号号码 */
	private String viewNo;//
	/**手机号*/
	private String mobile;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public int getViewNumber() {
		return viewNumber;
	}

	public void setViewNumber(int viewNumber) {
		this.viewNumber = viewNumber;
	}

	public String getViewNo() {
		return viewNo;
	}

	public void setViewNo(String viewNo) {
		this.viewNo = viewNo;
	}

	public int getLetter() {
		return letter;
	}

	public void setLetter(int letter) {
		this.letter = letter;
	}
}
