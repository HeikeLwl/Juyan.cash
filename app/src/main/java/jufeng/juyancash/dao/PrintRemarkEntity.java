package jufeng.juyancash.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.sql.ResultSet;
import java.sql.SQLException;

import jufeng.juyancash.jdbc.ResultObjectBuilder;
import jufeng.juyancash.syncdata.RemarkVo;

/**
 * Entity mapped to table "PRINT_REMARK_ENTITY".
 */
public class PrintRemarkEntity {

    private Long id;
    /** Not-null value. */
    private String printRemarkId;
    private String printRemarkName;

    public PrintRemarkEntity() {
    }

    public PrintRemarkEntity(Long id) {
        this.id = id;
    }

    public PrintRemarkEntity(Long id, String printRemarkId, String printRemarkName) {
        this.id = id;
        this.printRemarkId = printRemarkId;
        this.printRemarkName = printRemarkName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getPrintRemarkId() {
        return printRemarkId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPrintRemarkId(String printRemarkId) {
        this.printRemarkId = printRemarkId;
    }

    public String getPrintRemarkName() {
        return printRemarkName;
    }

    public void setPrintRemarkName(String printRemarkName) {
        this.printRemarkName = printRemarkName;
    }

    public PrintRemarkEntity(RemarkVo remarkModel){
        this.printRemarkId = remarkModel.getId();
        this.printRemarkName = remarkModel.getName();
    }

    public static ResultObjectBuilder<PrintRemarkEntity> builder = new ResultObjectBuilder<PrintRemarkEntity>() {
        @Override
        public PrintRemarkEntity build(ResultSet rs) throws SQLException {
            PrintRemarkEntity printRemarkEntity = new PrintRemarkEntity();
            printRemarkEntity.setPrintRemarkId(rs.getString("c_id"));
            printRemarkEntity.setPrintRemarkName(rs.getString("c_name"));
            return printRemarkEntity;
        }
    };

}
