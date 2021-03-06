package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PRINT_RESULT_ENTITY".
 */
public class PrintResultEntity {

    private Long id;
    private String printResultId;
    private String orderId;
    private String printIp;
    private String printResult;
    private String printTime;

    public PrintResultEntity() {
    }

    public PrintResultEntity(Long id) {
        this.id = id;
    }

    public PrintResultEntity(Long id, String printResultId, String orderId, String printIp, String printResult, String printTime) {
        this.id = id;
        this.printResultId = printResultId;
        this.orderId = orderId;
        this.printIp = printIp;
        this.printResult = printResult;
        this.printTime = printTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrintResultId() {
        return printResultId;
    }

    public void setPrintResultId(String printResultId) {
        this.printResultId = printResultId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrintIp() {
        return printIp;
    }

    public void setPrintIp(String printIp) {
        this.printIp = printIp;
    }

    public String getPrintResult() {
        return printResult;
    }

    public void setPrintResult(String printResult) {
        this.printResult = printResult;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    @Override
    public String toString() {
        return "PrintResultEntity{" +
                "id=" + id +
                ", printResultId='" + printResultId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", printIp='" + printIp + '\'' +
                ", printResult='" + printResult + '\'' +
                ", printTime='" + printTime + '\'' +
                '}';
    }
}
