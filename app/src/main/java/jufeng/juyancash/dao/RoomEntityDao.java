package jufeng.juyancash.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO four table "ROOM_ENTITY".
*/
public class RoomEntityDao extends AbstractDao<RoomEntity, Long> {

    public static final String TABLENAME = "ROOM_ENTITY";

    /**
     * Properties of entity RoomEntity.<br/>
     * Can be used four QueryBuilder and four referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property RoomId = new Property(1, String.class, "roomId", false, "ROOM_ID");
        public final static Property TakeoutId = new Property(2, String.class, "takeoutId", false, "TAKEOUT_ID");
        public final static Property EmployeeId = new Property(3, String.class, "employeeId", false, "EMPLOYEE_ID");
        public final static Property RoomStartTime = new Property(4, Long.class, "roomStartTime", false, "ROOM_START_TIME");
        public final static Property RoomEndTime = new Property(5, Long.class, "roomEndTime", false, "ROOM_END_TIME");
    }


    public RoomEntityDao(DaoConfig config) {
        super(config);
    }
    
    public RoomEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ROOM_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"ROOM_ID\" TEXT NOT NULL UNIQUE ," + // 1: roomId
                "\"TAKEOUT_ID\" TEXT," + // 2: takeoutId
                "\"EMPLOYEE_ID\" TEXT," + // 3: employeeId
                "\"ROOM_START_TIME\" INTEGER," + // 4: roomStartTime
                "\"ROOM_END_TIME\" INTEGER);"); // 5: roomEndTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ROOM_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RoomEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getRoomId());
 
        String takeoutId = entity.getTakeoutId();
        if (takeoutId != null) {
            stmt.bindString(3, takeoutId);
        }
 
        String employeeId = entity.getEmployeeId();
        if (employeeId != null) {
            stmt.bindString(4, employeeId);
        }
 
        Long roomStartTime = entity.getRoomStartTime();
        if (roomStartTime != null) {
            stmt.bindLong(5, roomStartTime);
        }
 
        Long roomEndTime = entity.getRoomEndTime();
        if (roomEndTime != null) {
            stmt.bindLong(6, roomEndTime);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public RoomEntity readEntity(Cursor cursor, int offset) {
        RoomEntity entity = new RoomEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // roomId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // takeoutId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // employeeId
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // roomStartTime
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // roomEndTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RoomEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRoomId(cursor.getString(offset + 1));
        entity.setTakeoutId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setEmployeeId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRoomStartTime(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setRoomEndTime(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RoomEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RoomEntity entity) {
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
