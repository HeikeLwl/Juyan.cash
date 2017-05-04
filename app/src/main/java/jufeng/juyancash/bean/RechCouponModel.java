package jufeng.juyancash.bean;

import java.util.Date;
import java.util.Set;

/**
 * 会员充值券
 * @author wuzhisheng
 */
public class RechCouponModel{
	
	private String id;
	//主题
	private String theme;
	//商家编号
	private String partnerCode;
	//开始时间
	private Date startTime;
	//结束时间
	private Date endTime;
	//创建时间
	private Date createTime;
	//是否启用  0-否 1-是
	private int inUse;
	
	private Set<RechCouponItemModel> rechCouponItem;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getInUse() {
		return inUse;
	}

	public void setInUse(int inUse) {
		this.inUse = inUse;
	}

	public Set<RechCouponItemModel> getRechCouponItem() {
		return rechCouponItem;
	}

	public void setRechCouponItem(Set<RechCouponItemModel> rechCouponItem) {
		this.rechCouponItem = rechCouponItem;
	}
}
