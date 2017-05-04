package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO four table "PRINT_REMARK_ENTITY".
*/
public class PrintRemarkEntityDao extends AbstractDao<PrintRemarkEntity, Long> {

    public static final String TABLENAME = "PRINT_REMARK_ENTITY";

    /**
     * Properties of entity PrintRemarkEntity.<br/>
     * Can be used four QueryBuilder and four referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PrintRemarkId = new Property(1, String.class, "printRemarkId", false, "PRINT_REMARK_ID");
        public final static Property PrintRemarkName = new Property(2, String.class, "printRemarkName", false, "PRINT_REMARK_NAME");
    }


    public PrintRemarkEntityDao(DaoConfig config) {
        super(config);
    }
    
    public PrintRemarkEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PRINT_REMARK_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"PRINT_REMARK_ID\" TEXT NOT NULL UNIQUE ," + // 1: printRemarkId
                "\"PRINT_REMARK_NAME\" TEXT);"); // 2: printRemarkName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PRINT_REMARK_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PrintRemarkEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getPrintRemarkId());
 
        String printRemarkName = entity.getPrintRemarkName();
        if (printRemarkName != null) {
            stmt.bindString(3, printRemarkName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PrintRemarkEntity readEntity(Cursor cursor, int offset) {
        PrintRemarkEntity entity = new PrintRemarkEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // printRemarkId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // printRemarkName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PrintRemarkEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPrintRemarkId(cursor.getString(offset + 1));
        entity.setPrintRemarkName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PrintRemarkEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PrintRemarkEntity entity) {
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
