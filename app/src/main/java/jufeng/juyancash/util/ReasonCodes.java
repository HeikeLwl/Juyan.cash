package jufeng.juyancash.util;

import java.util.ArrayList;


/**
 * 订单取消枚举
 * 
 * @author 伍值胜
 *
 * @date 2017年3月30日
 */
public enum ReasonCodes {

	
	//美团取消
	MTOVERTIME(1001, "系统取消，超时未确认"),
	MT30MINOPAY(1002, "系统取消，在线支付订单30分钟未支付"), 
	MTCANCELPAY(1101, "用户取消，在线支付中取消"), 
	MTERPCONFIRM(1102, "用户取消，商家确认前取消"), 
	MTREFUND(1103, "用户取消，用户退款取消"), 
	MTKFERRORORDER(1201, "客服取消，用户下错单"), 
	MTKFTEST(1202, "客服取消，用户测试"),
	MTKFORDERRE(1203, "客服取消，重复订单"),
	MTKFOTHER(1204, "客服取消，其他原因"),
	MTOTHER(1301, "其他原因"),
	
	//商家取消
	ERPOVERTIME(2001, "商家超时接单【商家取消时填写】"),
	ERPUPNONCUSTOMER(2002, "非顾客原因修改订单"), 
	ERPCANCELNONCUSTOMER(2003, "非客户原因取消订单"), 
	ERPDELAYED(2004, "配送延迟"), 
	ERPCOMPLAINT(2005, "售后投诉"), 
	ERPCUSTOMERASK(2006, "用户要求取消"), 
	ERPOTHER(2007, "其他原因");

	private final int code;

	private final String value;
	
	public int getCode() {
		return code;
	}



	public String getValue() {
		return value;
	}

	private ReasonCodes(int code, String value) {
		this.code = code;
		this.value = value;
	}
	
	/** 
     * 根据code获取value 
     *  
     * @param code 
     *            
     * @return String 
     */  
    public static String getValueByKey(int code) {  
    	ReasonCodes[] enums = ReasonCodes.values();  
        for (int i = 0; i < enums.length; i++) {  
            if (enums[i].getCode() == code) {  
                return enums[i].getValue();  
            }  
        }  
        return null;  
    } 
	
    
    /** 
     * 转换为MAP集合 
     *  
     * @returnMap<String, String> 
     */  
    public static ArrayList<Integer> toArrayList() {
		ArrayList<Integer> results = new ArrayList<>();
        ReasonCodes[] enums = ReasonCodes.values();  
        for (int i = 0; i < enums.length; i++) {
			if(enums[i].getCode() > 2000) {
				results.add(enums[i].getCode());
			}
        }  
        return results;
    }  
    
    public static boolean verification(int code){
    	if(getValueByKey(code)==null){
    		return false;
    	}
    	return true;
    }
}
