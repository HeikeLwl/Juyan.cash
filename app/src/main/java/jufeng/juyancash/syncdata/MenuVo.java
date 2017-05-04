package jufeng.juyancash.syncdata;

public class MenuVo {

	private String id;
	private int property;
	private String parentId;
	private int sort;
	private int type; // 1-后台菜单,2-收银菜单
	private String name;

	public MenuVo() {
		this.name = "";
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



	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
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

}
