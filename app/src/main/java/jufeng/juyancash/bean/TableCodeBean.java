package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2017/3/22.
 */

public class TableCodeBean {
    private String areaId;
    private String areaName;
    private int type;
    private String tableCode;

    public TableCodeBean(String areaId, String areaName, int type, String tableCode) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.type = type;
        this.tableCode = tableCode;
    }

    public TableCodeBean(){

    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }
}
