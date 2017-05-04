/**
 * @(#)Jodo, 2013年10月5日
 */
package jufeng.juyancash.bean;

import java.io.Serializable;

/**
 * 公共报文头
 * @author wujf
 */
public  class SMZFVo implements Serializable {


	private static final long serialVersionUID = 1L;
	//报文相应头head
    private String version;//版本号
    private String msgType;//报文类型
    private String smzfMsgId;//平台流水号
    private String reqDate;//请求日期时间 ,格式为yyyyMMddHHmmss
    private String respDate;//请求日期时间 ,格式为yyyyMMddHHmmss
    private String respType;//应答类型,S：成功 ,E：失败, R：不确定（处理中）  
    private String respCode;//应答码 ,成功：000000,补单成功：000090 ,失败：返回具体的响应码
    private String respMsg;//应答描述
    
    private String reqMsgId;//请求流水号(合作方生成)
    private String oriReqMsgId;//原交易流水号(合作方生成的订单号)
    
    
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getSmzfMsgId() {
		return smzfMsgId;
	}
	public void setSmzfMsgId(String smzfMsgId) {
		this.smzfMsgId = smzfMsgId;
	}
	public String getReqDate() {
		return reqDate;
	}
	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}
	public String getRespDate() {
		return respDate;
	}
	public void setRespDate(String respDate) {
		this.respDate = respDate;
	}
	public String getRespType() {
		return respType;
	}
	public void setRespType(String respType) {
		this.respType = respType;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	public String getReqMsgId() {
		return reqMsgId;
	}
	public void setReqMsgId(String reqMsgId) {
		this.reqMsgId = reqMsgId;
	}
	public String getOriReqMsgId() {
		return oriReqMsgId;
	}
	public void setOriReqMsgId(String oriReqMsgId) {
		this.oriReqMsgId = oriReqMsgId;
	}
    
   

}
