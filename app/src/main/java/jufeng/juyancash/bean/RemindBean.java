package jufeng.juyancash.bean;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator102 on 2016/10/22.
 */

public class RemindBean {
    private List<JSONObject> goodIds;
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<JSONObject> getGoodIds() {
        return goodIds;
    }

    public void setGoodIds(List<JSONObject> goodIds) {
        this.goodIds = goodIds;
    }

    public ArrayList<HashMap<String,Object>> getRemindDish(){
        ArrayList<HashMap<String,Object>> results = new ArrayList<>();
        if(goodIds != null){
            for (JSONObject goodsId :
                    goodIds) {
                HashMap<String,Object> map = new HashMap<>();
                List<String> keys = new ArrayList<>(goodsId.keySet());
                for (String key :
                        keys) {
                    map.put("orderDishId",key);
                    map.put("remindCount",goodsId.getDoubleValue(key));
                    results.add(map);
                }
            }
        }
        return results;
    }
}
