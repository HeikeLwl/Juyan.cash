package jufeng.juyancash.bean;

public class AppVersionModel{
	private int version;//版本号
	private int type;//app类型 1-Android，2-ios
	private String time;//创建时间
	private String link;//APP链接
	private String content;//更新说明

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
