package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/23.
 */

public class OnTableCodeItemClickEvent {
    private String tableCode;

    public OnTableCodeItemClickEvent(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }
}
