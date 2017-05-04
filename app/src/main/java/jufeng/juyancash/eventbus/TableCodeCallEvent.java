package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/4/5.
 */

public class TableCodeCallEvent {
    private String tableCode;

    public TableCodeCallEvent(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }
}
