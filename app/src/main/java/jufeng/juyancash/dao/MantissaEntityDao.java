package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO four table "MANTISSA_ENTITY".
*/
public class MantissaEntityDao extends AbstractDao<MantissaEntity, Long> {

    public static final String TABLENAME = "MANTISSA_ENTITY";

    /**
     * Properties of entity MantissaEntity.<br/>
     * Can be used four QueryBuilder and four referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MantissaId = new Property(1, String.class, "mantissaId", false, "MANTISSA_ID");
        public final static Property MantissaValue = new Property(2, Integer.class, "mantissaValue", false, "MANTISSA_VALUE");
        public final static Property ReduceValue = new Property(3, Integer.class, "reduceValue", false, "REDUCE_VALUE");
    }


    public MantissaEntityDao(DaoConfig config) {
        super(config);
    }
    
    public MantissaEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MANTISSA_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"MANTISSA_ID\" TEXT NOT NULL UNIQUE ," + // 1: mantissaId
                "\"MANTISSA_VALUE\" INTEGER," + // 2: mantissaValue
                "\"REDUCE_VALUE\" INTEGER);"); // 3: reduceValue
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MANTISSA_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MantissaEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getMantissaId());
 
        Integer mantissaValue = entity.getMantissaValue();
        if (mantissaValue != null) {
            stmt.bindLong(3, mantissaValue);
        }
 
        Integer reduceValue = entity.getReduceValue();
        if (reduceValue != null) {
            stmt.bindLong(4, reduceValue);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MantissaEntity readEntity(Cursor cursor, int offset) {
        MantissaEntity entity = new MantissaEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // mantissaId
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // mantissaValue
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // reduceValue
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MantissaEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMantissaId(cursor.getString(offset + 1));
        entity.setMantissaValue(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setReduceValue(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MantissaEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MantissaEntity entity) {
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
