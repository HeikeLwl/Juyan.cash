package jufeng.juyancash.eventbus;

/**
 * Created by Administrator102 on 2017/3/16.
 */

public class SearchDishUpdateEvent {
    private String matchStr;
    private int type;
    private String orderId;

    public SearchDishUpdateEvent(String matchStr, int type,String orderId) {
        this.matchStr = matchStr;
        this.type = type;
        this.orderId = orderId;
    }

    public String getOrderId() {
        if(orderId == null){
            return "";
        }
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMatchStr() {
        if(matchStr == null){
            return "";
        }
        return matchStr;
    }

    public void setMatchStr(String matchStr) {
        this.matchStr = matchStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
