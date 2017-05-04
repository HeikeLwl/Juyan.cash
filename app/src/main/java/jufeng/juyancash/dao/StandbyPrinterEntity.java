package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.sql.ResultSet;
import java.sql.SQLException;

import jufeng.juyancash.jdbc.ResultObjectBuilder;
import jufeng.juyancash.syncdata.PrinterVo;

/**
 * Entity mapped to table "STANDBY_PRINTER_ENTITY".
 */
public class StandbyPrinterEntity {

    private Long id;
    /** Not-null value. */
    private String standbyPrinterId;
    private String standbyPrinterIp;
    private String oldPrinterIp;

    public StandbyPrinterEntity() {
    }

    public StandbyPrinterEntity(Long id) {
        this.id = id;
    }

    public StandbyPrinterEntity(Long id, String standbyPrinterId, String standbyPrinterIp, String oldPrinterIp) {
        this.id = id;
        this.standbyPrinterId = standbyPrinterId;
        this.standbyPrinterIp = standbyPrinterIp;
        this.oldPrinterIp = oldPrinterIp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getStandbyPrinterId() {
        return standbyPrinterId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStandbyPrinterId(String standbyPrinterId) {
        this.standbyPrinterId = standbyPrinterId;
    }

    public String getStandbyPrinterIp() {
        return standbyPrinterIp;
    }

    public void setStandbyPrinterIp(String standbyPrinterIp) {
        this.standbyPrinterIp = standbyPrinterIp;
    }

    public String getOldPrinterIp() {
        return oldPrinterIp;
    }

    public void setOldPrinterIp(String oldPrinterIp) {
        this.oldPrinterIp = oldPrinterIp;
    }

    public StandbyPrinterEntity(PrinterVo printerModel){
        this.standbyPrinterId = printerModel.getId();
        this.standbyPrinterIp = printerModel.getSpareIp();
        this.oldPrinterIp = printerModel.getIp();
    }

    public static ResultObjectBuilder<StandbyPrinterEntity> builder = new ResultObjectBuilder<StandbyPrinterEntity>() {
        @Override
        public StandbyPrinterEntity build(ResultSet rs) throws SQLException {
            StandbyPrinterEntity standbyPrinterEntity = new StandbyPrinterEntity();
            standbyPrinterEntity.setStandbyPrinterId(rs.getString("c_id"));
            standbyPrinterEntity.setStandbyPrinterIp(rs.getString("c_spare_ip"));
            standbyPrinterEntity.setOldPrinterIp(rs.getString("c_ip"));
            return standbyPrinterEntity;
        }
    };

}