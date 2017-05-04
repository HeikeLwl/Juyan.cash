package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "PRINT_RESULT_ENTITY".
*/
public class PrintResultEntityDao extends AbstractDao<PrintResultEntity, Long> {

    public static final String TABLENAME = "PRINT_RESULT_ENTITY";

    /**
     * Properties of entity PrintResultEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PrintResultId = new Property(1, String.class, "printResultId", false, "PRINT_RESULT_ID");
        public final static Property OrderId = new Property(2, String.class, "orderId", false, "ORDER_ID");
        public final static Property PrintIp = new Property(3, String.class, "printIp", false, "PRINT_IP");
        public final static Property PrintResult = new Property(4, String.class, "printResult", false, "PRINT_RESULT");
        public final static Property PrintTime = new Property(5, String.class, "printTime", false, "PRINT_TIME");
    };


    public PrintResultEntityDao(DaoConfig config) {
        super(config);
    }
    
    public PrintResultEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PRINT_RESULT_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"PRINT_RESULT_ID\" TEXT UNIQUE ," + // 1: printResultId
                "\"ORDER_ID\" TEXT," + // 2: orderId
                "\"PRINT_IP\" TEXT," + // 3: printIp
                "\"PRINT_RESULT\" TEXT," + // 4: printResult
                "\"PRINT_TIME\" TEXT);"); // 5: printTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PRINT_RESULT_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PrintResultEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String printResultId = entity.getPrintResultId();
        if (printResultId != null) {
            stmt.bindString(2, printResultId);
        }
 
        String orderId = entity.getOrderId();
        if (orderId != null) {
            stmt.bindString(3, orderId);
        }
 
        String printIp = entity.getPrintIp();
        if (printIp != null) {
            stmt.bindString(4, printIp);
        }
 
        String printResult = entity.getPrintResult();
        if (printResult != null) {
            stmt.bindString(5, printResult);
        }
 
        String printTime = entity.getPrintTime();
        if (printTime != null) {
            stmt.bindString(6, printTime);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PrintResultEntity readEntity(Cursor cursor, int offset) {
        PrintResultEntity entity = new PrintResultEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // printResultId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // orderId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // printIp
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // printResult
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // printTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PrintResultEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPrintResultId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setOrderId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPrintIp(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPrintResult(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPrintTime(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PrintResultEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PrintResultEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
