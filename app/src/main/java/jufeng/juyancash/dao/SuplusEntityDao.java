package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "SUPLUS_ENTITY".
*/
public class SuplusEntityDao extends AbstractDao<SuplusEntity, Long> {

    public static final String TABLENAME = "SUPLUS_ENTITY";

    /**
     * Properties of entity SuplusEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SurplusType = new Property(1, Integer.class, "surplusType", false, "SURPLUS_TYPE");
        public final static Property AccurateValue = new Property(2, Integer.class, "accurateValue", false, "ACCURATE_VALUE");
        public final static Property MinAmount = new Property(3, Integer.class, "minAmount", false, "MIN_AMOUNT");
    }


    public SuplusEntityDao(DaoConfig config) {
        super(config);
    }
    
    public SuplusEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SUPLUS_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"SURPLUS_TYPE\" INTEGER," + // 1: surplusType
                "\"ACCURATE_VALUE\" INTEGER," + // 2: accurateValue
                "\"MIN_AMOUNT\" INTEGER);"); // 3: minAmount
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SUPLUS_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SuplusEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer surplusType = entity.getSurplusType();
        if (surplusType != null) {
            stmt.bindLong(2, surplusType);
        }
 
        Integer accurateValue = entity.getAccurateValue();
        if (accurateValue != null) {
            stmt.bindLong(3, accurateValue);
        }
 
        Integer minAmount = entity.getMinAmount();
        if (minAmount != null) {
            stmt.bindLong(4, minAmount);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SuplusEntity readEntity(Cursor cursor, int offset) {
        SuplusEntity entity = new SuplusEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // surplusType
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // accurateValue
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // minAmount
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SuplusEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSurplusType(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setAccurateValue(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setMinAmount(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SuplusEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SuplusEntity entity) {
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