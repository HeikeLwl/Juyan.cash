package jufeng.juyancash.syncdata;

import java.util.Date;

public class EmployeeVo {
	private String id;
	private int property;
	private String partnerCode; // 商家编号
	private String loginName;// 用户登录名
	private String password;// 员工密码
	private String roleId;// 角色
	private String name;// 姓名
	private int sex;// 性别
	private String tel;// 手机号码
	private String headUrl;// 头像
	private String identity;// 身份证号
	private String picUrl;// 身份证正面图
	private String picUrl1;// 身份证反面图
	private int saleCard;// 售卡人 0-否,1-是
	private int saleGoods;// 允许对可打折商品打折 0-不允许,1-允许
	private int minDiscount;// 最低打折额度(%)
	private int noSaleGoods;// 允许对不打折商品打折 0-不允许,1-允许
	private int minNoDiscount;// 最低打折额度(%)
	private int printLimitMun;// 财务联打印次数限制 0-限制,1-不限制
	private int limitMun;// 最多打印次数
	private int limitQuota;// 限制去零额度 0-限制,1-不限制
	private int maxQuota; // 最大可去零额度(分)
	private int idCardLogin;// 使用ID卡登陆 0-否,1-是
	private String idCard; // ID卡号
	private Date createTime;

	private String wxOpenid; // 微信opneid
	private int work;// 是否工作中0-N,1-Y
	private int binding;// 是否绑定0-N,1-Y

	private String jpushId;// 极光推送id
	private String account;// 登录账号

	private int authCashier;// 是否有收银权限0-否,1-是
	private int authTc;// 退菜权限0-否,1-是
	private int authZc;// 赠菜权限0-否,1-是
	private int authFjz;// 反结账权限0-否,1-是
	private int authQkcd;// 清空厨打权限0-否,1-是
	private int authBill;// 挂账权限0-否,1-是
	private String emall;// 个人电子邮箱
	private int authFjzOne;
	private int authFjzTwo;
	private int authVipBind;

	public int getAuthFjzOne() {
		return authFjzOne;
	}

	public void setAuthFjzOne(int authFjzOne) {
		this.authFjzOne = authFjzOne;
	}

	public int getAuthFjzTwo() {
		return authFjzTwo;
	}

	public void setAuthFjzTwo(int authFjzTwo) {
		this.authFjzTwo = authFjzTwo;
	}

	public int getAuthVipBind() {
		return authVipBind;
	}

	public void setAuthVipBind(int authVipBind) {
		this.authVipBind = authVipBind;
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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
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

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPicUrl1() {
		return picUrl1;
	}

	public void setPicUrl1(String picUrl1) {
		this.picUrl1 = picUrl1;
	}

	public int getSaleCard() {
		return saleCard;
	}

	public void setSaleCard(int saleCard) {
		this.saleCard = saleCard;
	}

	public int getSaleGoods() {
		return saleGoods;
	}

	public void setSaleGoods(int saleGoods) {
		this.saleGoods = saleGoods;
	}

	public int getMinDiscount() {
		return minDiscount;
	}

	public void setMinDiscount(int minDiscount) {
		this.minDiscount = minDiscount;
	}

	public int getNoSaleGoods() {
		return noSaleGoods;
	}

	public void setNoSaleGoods(int noSaleGoods) {
		this.noSaleGoods = noSaleGoods;
	}

	public int getMinNoDiscount() {
		return minNoDiscount;
	}

	public void setMinNoDiscount(int minNoDiscount) {
		this.minNoDiscount = minNoDiscount;
	}

	public int getPrintLimitMun() {
		return printLimitMun;
	}

	public void setPrintLimitMun(int printLimitMun) {
		this.printLimitMun = printLimitMun;
	}

	public int getLimitMun() {
		return limitMun;
	}

	public void setLimitMun(int limitMun) {
		this.limitMun = limitMun;
	}

	public int getLimitQuota() {
		return limitQuota;
	}

	public void setLimitQuota(int limitQuota) {
		this.limitQuota = limitQuota;
	}

	public int getMaxQuota() {
		return maxQuota;
	}

	public void setMaxQuota(int maxQuota) {
		this.maxQuota = maxQuota;
	}

	public int getIdCardLogin() {
		return idCardLogin;
	}

	public void setIdCardLogin(int idCardLogin) {
		this.idCardLogin = idCardLogin;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getWxOpenid() {
		return wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}

	public int getWork() {
		return work;
	}

	public void setWork(int work) {
		this.work = work;
	}

	public int getBinding() {
		return binding;
	}

	public void setBinding(int binding) {
		this.binding = binding;
	}

	public String getJpushId() {
		return jpushId;
	}

	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getAuthCashier() {
		return authCashier;
	}

	public void setAuthCashier(int authCashier) {
		this.authCashier = authCashier;
	}

	public int getAuthTc() {
		return authTc;
	}

	public void setAuthTc(int authTc) {
		this.authTc = authTc;
	}

	public int getAuthZc() {
		return authZc;
	}

	public void setAuthZc(int authZc) {
		this.authZc = authZc;
	}

	public int getAuthFjz() {
		return authFjz;
	}

	public void setAuthFjz(int authFjz) {
		this.authFjz = authFjz;
	}

	public int getAuthQkcd() {
		return authQkcd;
	}

	public void setAuthQkcd(int authQkcd) {
		this.authQkcd = authQkcd;
	}

	public int getAuthBill() {
		return authBill;
	}

	public void setAuthBill(int authBill) {
		this.authBill = authBill;
	}

	public String getEmall() {
		return emall;
	}

	public void setEmall(String emall) {
		this.emall = emall;
	}

	@Override
	public String toString() {
		return "EmployeeVo{" +
				"id='" + id + '\'' +
				", property=" + property +
				", partnerCode='" + partnerCode + '\'' +
				", loginName='" + loginName + '\'' +
				", password='" + password + '\'' +
				", roleId='" + roleId + '\'' +
				", name='" + name + '\'' +
				", sex=" + sex +
				", tel='" + tel + '\'' +
				", headUrl='" + headUrl + '\'' +
				", identity='" + identity + '\'' +
				", picUrl='" + picUrl + '\'' +
				", picUrl1='" + picUrl1 + '\'' +
				", saleCard=" + saleCard +
				", saleGoods=" + saleGoods +
				", minDiscount=" + minDiscount +
				", noSaleGoods=" + noSaleGoods +
				", minNoDiscount=" + minNoDiscount +
				", printLimitMun=" + printLimitMun +
				", limitMun=" + limitMun +
				", limitQuota=" + limitQuota +
				", maxQuota=" + maxQuota +
				", idCardLogin=" + idCardLogin +
				", idCard='" + idCard + '\'' +
				", createTime=" + createTime +
				", wxOpenid='" + wxOpenid + '\'' +
				", work=" + work +
				", binding=" + binding +
				", jpushId='" + jpushId + '\'' +
				", account='" + account + '\'' +
				", authCashier=" + authCashier +
				", authTc=" + authTc +
				", authZc=" + authZc +
				", authFjz=" + authFjz +
				", authQkcd=" + authQkcd +
				", authBill=" + authBill +
				", emall='" + emall + '\'' +
				'}';
	}
}
