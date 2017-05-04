package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO four table "SPECIAL_ENTITY".
*/
public class SpecialEntityDao extends AbstractDao<SpecialEntity, Long> {

    public static final String TABLENAME = "SPECIAL_ENTITY";

    /**
     * Properties of entity SpecialEntity.<br/>
     * Can be used four QueryBuilder and four referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SpecialId = new Property(1, String.class, "specialId", false, "SPECIAL_ID");
        public final static Property SpecialType = new Property(2, Integer.class, "specialType", false, "SPECIAL_TYPE");
        public final static Property SpecialName = new Property(3, String.class, "specialName", false, "SPECIAL_NAME");
    }


    public SpecialEntityDao(DaoConfig config) {
        super(config);
    }
    
    public SpecialEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SPECIAL_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"SPECIAL_ID\" TEXT NOT NULL UNIQUE ," + // 1: specialId
                "\"SPECIAL_TYPE\" INTEGER," + // 2: specialType
                "\"SPECIAL_NAME\" TEXT);"); // 3: specialName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SPECIAL_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SpecialEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getSpecialId());
 
        Integer specialType = entity.getSpecialType();
        if (specialType != null) {
            stmt.bindLong(3, specialType);
        }
 
        String specialName = entity.getSpecialName();
        if (specialName != null) {
            stmt.bindString(4, specialName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SpecialEntity readEntity(Cursor cursor, int offset) {
        SpecialEntity entity = new SpecialEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // specialId
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // specialType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // specialName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SpecialEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSpecialId(cursor.getString(offset + 1));
        entity.setSpecialType(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setSpecialName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SpecialEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SpecialEntity entity) {
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