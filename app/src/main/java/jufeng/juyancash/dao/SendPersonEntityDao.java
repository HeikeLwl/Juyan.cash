package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "SEND_PERSON_ENTITY".
*/
public class SendPersonEntityDao extends AbstractDao<SendPersonEntity, Long> {

    public static final String TABLENAME = "SEND_PERSON_ENTITY";

    /**
     * Properties of entity SendPersonEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SendPersonId = new Property(1, String.class, "sendPersonId", false, "SEND_PERSON_ID");
        public final static Property SendPersonName = new Property(2, String.class, "sendPersonName", false, "SEND_PERSON_NAME");
        public final static Property SendPersonPhone = new Property(3, String.class, "sendPersonPhone", false, "SEND_PERSON_PHONE");
        public final static Property SendPersonTC = new Property(4, Integer.class, "sendPersonTC", false, "SEND_PERSON_TC");
        public final static Property IsSendMessage = new Property(5, Integer.class, "isSendMessage", false, "IS_SEND_MESSAGE");
    };


    public SendPersonEntityDao(DaoConfig config) {
        super(config);
    }
    
    public SendPersonEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SEND_PERSON_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"SEND_PERSON_ID\" TEXT," + // 1: sendPersonId
                "\"SEND_PERSON_NAME\" TEXT," + // 2: sendPersonName
                "\"SEND_PERSON_PHONE\" TEXT," + // 3: sendPersonPhone
                "\"SEND_PERSON_TC\" INTEGER," + // 4: sendPersonTC
                "\"IS_SEND_MESSAGE\" INTEGER);"); // 5: isSendMessage
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SEND_PERSON_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SendPersonEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String sendPersonId = entity.getSendPersonId();
        if (sendPersonId != null) {
            stmt.bindString(2, sendPersonId);
        }
 
        String sendPersonName = entity.getSendPersonName();
        if (sendPersonName != null) {
            stmt.bindString(3, sendPersonName);
        }
 
        String sendPersonPhone = entity.getSendPersonPhone();
        if (sendPersonPhone != null) {
            stmt.bindString(4, sendPersonPhone);
        }
 
        Integer sendPersonTC = entity.getSendPersonTC();
        if (sendPersonTC != null) {
            stmt.bindLong(5, sendPersonTC);
        }
 
        Integer isSendMessage = entity.getIsSendMessage();
        if (isSendMessage != null) {
            stmt.bindLong(6, isSendMessage);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SendPersonEntity readEntity(Cursor cursor, int offset) {
        SendPersonEntity entity = new SendPersonEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // sendPersonId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sendPersonName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // sendPersonPhone
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // sendPersonTC
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5) // isSendMessage
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SendPersonEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSendPersonId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSendPersonName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSendPersonPhone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSendPersonTC(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setIsSendMessage(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SendPersonEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SendPersonEntity entity) {
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