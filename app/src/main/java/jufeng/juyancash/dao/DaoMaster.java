package jufeng.juyancash.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * Master of DAO (schema version 1): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 11;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        AreaEntityDao.createTable(db, ifNotExists);
        ArrangeEntityDao.createTable(db, ifNotExists);
        DishEntityDao.createTable(db, ifNotExists);
        DishTypeEntityDao.createTable(db, ifNotExists);
        EmployeeEntityDao.createTable(db, ifNotExists);
        KichenDishEntityDao.createTable(db, ifNotExists);
        KichenPrintEntityDao.createTable(db, ifNotExists);
        KitchenDishTypeEntityDao.createTable(db, ifNotExists);
        KitchenDisPrintEntityDao.createTable(db, ifNotExists);
        OrderDishEntityDao.createTable(db, ifNotExists);
        OrderEntityDao.createTable(db, ifNotExists);
        PayModeEntityDao.createTable(db, ifNotExists);
        PermissionEntityDao.createTable(db, ifNotExists);
        PracticeEntityDao.createTable(db, ifNotExists);
        DishPracticeEntityDao.createTable(db, ifNotExists);
        RankEntityDao.createTable(db, ifNotExists);
        RankPermissionEntityDao.createTable(db, ifNotExists);
        RoomEntityDao.createTable(db, ifNotExists);
        ScheduleEntityDao.createTable(db, ifNotExists);
        SellCheckEntityDao.createTable(db, ifNotExists);
        TableEntityDao.createTable(db, ifNotExists);
        TaocanEntityDao.createTable(db, ifNotExists);
        TaocanGroupDishEntityDao.createTable(db, ifNotExists);
        TaocanGroupEntityDao.createTable(db, ifNotExists);
        TaocanTypeEntityDao.createTable(db, ifNotExists);
        SpecifyEntityDao.createTable(db, ifNotExists);
        DishSpecifyEntityDao.createTable(db, ifNotExists);
        TakeOutOrderEntityDao.createTable(db, ifNotExists);
        CashierDisplayEntityDao.createTable(db, ifNotExists);
        SpecialEntityDao.createTable(db, ifNotExists);
        MantissaEntityDao.createTable(db, ifNotExists);
        SuplusEntityDao.createTable(db, ifNotExists);
        GrouponEntityDao.createTable(db, ifNotExists);
        GrouponTaocanEntityDao.createTable(db, ifNotExists);
        DiscountEntityDao.createTable(db, ifNotExists);
        DishTypeDiscountEntityDao.createTable(db, ifNotExists);
        PrintCashierEntityDao.createTable(db, ifNotExists);
        CashierClassifyEntityDao.createTable(db, ifNotExists);
        CashierDishEntityDao.createTable(db, ifNotExists);
        PrintKitchenEntityDao.createTable(db, ifNotExists);
        PrintKitchenClassifyEntityDao.createTable(db, ifNotExists);
        PrintKitchenDishEntityDao.createTable(db, ifNotExists);
        StandbyPrinterEntityDao.createTable(db, ifNotExists);
        PrintRemarkEntityDao.createTable(db, ifNotExists);
        BasicsPartnerEntityDao.createTable(db, ifNotExists);
        ShopMealsEntityDao.createTable(db, ifNotExists);
        ShopPaymentEntityDao.createTable(db, ifNotExists);
        ShopReceivableEntityDao.createTable(db, ifNotExists);
        ShopTimeEntityDao.createTable(db, ifNotExists);
        SystemMessageEntityDao.createTable(db, ifNotExists);
        WXMessageEntityDao.createTable(db, ifNotExists);
        StoreMessageEntityDao.createTable(db, ifNotExists);
        DiscountHistoryEntityDao.createTable(db, ifNotExists);
        SomeDiscountGoodsEntityDao.createTable(db, ifNotExists);
        OrderTaocanGroupDishEntityDao.createTable(db, ifNotExists);
        PrinterFailedHistoryEntityDao.createTable(db, ifNotExists);
        JpushMessageEntityDao.createTable(db, ifNotExists);
        BillAccountEntityDao.createTable(db, ifNotExists);
        BillAccountPersonEntityDao.createTable(db, ifNotExists);
        BillAccountSignEntityDao.createTable(db, ifNotExists);
        BillAccountHistoryEntityDao.createTable(db, ifNotExists);
        UploadDataEntityDao.createTable(db, ifNotExists);
        VipCardEntityDao.createTable(db, ifNotExists);
        WxOrderMessageEntityDao.createTable(db, ifNotExists);
        PrintResultEntityDao.createTable(db, ifNotExists);
        TurnoverHistoryEntityDao.createTable(db, ifNotExists);
        TableCodeEntityDao.createTable(db, ifNotExists);
        SendPersonEntityDao.createTable(db, ifNotExists);
        MaterialEntityDao.createTable(db, ifNotExists);
        DishTypeMaterialEntityDao.createTable(db, ifNotExists);
        DishSelectedMaterialEntityDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        AreaEntityDao.dropTable(db, ifExists);
        ArrangeEntityDao.dropTable(db, ifExists);
        DishEntityDao.dropTable(db, ifExists);
        DishTypeEntityDao.dropTable(db, ifExists);
        EmployeeEntityDao.dropTable(db, ifExists);
        KichenDishEntityDao.dropTable(db, ifExists);
        KichenPrintEntityDao.dropTable(db, ifExists);
        KitchenDishTypeEntityDao.dropTable(db, ifExists);
        KitchenDisPrintEntityDao.dropTable(db, ifExists);
        OrderDishEntityDao.dropTable(db, ifExists);
        OrderEntityDao.dropTable(db, ifExists);
        PayModeEntityDao.dropTable(db, ifExists);
        PermissionEntityDao.dropTable(db, ifExists);
        PracticeEntityDao.dropTable(db, ifExists);
        DishPracticeEntityDao.dropTable(db, ifExists);
        RankEntityDao.dropTable(db, ifExists);
        RankPermissionEntityDao.dropTable(db, ifExists);
        RoomEntityDao.dropTable(db, ifExists);
        ScheduleEntityDao.dropTable(db, ifExists);
        SellCheckEntityDao.dropTable(db, ifExists);
        TableEntityDao.dropTable(db, ifExists);
        TaocanEntityDao.dropTable(db, ifExists);
        TaocanGroupDishEntityDao.dropTable(db, ifExists);
        TaocanGroupEntityDao.dropTable(db, ifExists);
        TaocanTypeEntityDao.dropTable(db, ifExists);
        SpecifyEntityDao.dropTable(db, ifExists);
        DishSpecifyEntityDao.dropTable(db, ifExists);
        TakeOutOrderEntityDao.dropTable(db, ifExists);
        CashierDisplayEntityDao.dropTable(db, ifExists);
        SpecialEntityDao.dropTable(db, ifExists);
        MantissaEntityDao.dropTable(db, ifExists);
        SuplusEntityDao.dropTable(db, ifExists);
        GrouponEntityDao.dropTable(db, ifExists);
        GrouponTaocanEntityDao.dropTable(db, ifExists);
        DiscountEntityDao.dropTable(db, ifExists);
        DishTypeDiscountEntityDao.dropTable(db, ifExists);
        PrintCashierEntityDao.dropTable(db, ifExists);
        CashierClassifyEntityDao.dropTable(db, ifExists);
        CashierDishEntityDao.dropTable(db, ifExists);
        PrintKitchenEntityDao.dropTable(db, ifExists);
        PrintKitchenClassifyEntityDao.dropTable(db, ifExists);
        PrintKitchenDishEntityDao.dropTable(db, ifExists);
        StandbyPrinterEntityDao.dropTable(db, ifExists);
        PrintRemarkEntityDao.dropTable(db, ifExists);
        BasicsPartnerEntityDao.dropTable(db, ifExists);
        ShopMealsEntityDao.dropTable(db, ifExists);
        ShopPaymentEntityDao.dropTable(db, ifExists);
        ShopReceivableEntityDao.dropTable(db, ifExists);
        ShopTimeEntityDao.dropTable(db, ifExists);
        SystemMessageEntityDao.dropTable(db, ifExists);
        WXMessageEntityDao.dropTable(db, ifExists);
        StoreMessageEntityDao.dropTable(db, ifExists);
        DiscountHistoryEntityDao.dropTable(db, ifExists);
        SomeDiscountGoodsEntityDao.dropTable(db, ifExists);
        OrderTaocanGroupDishEntityDao.dropTable(db, ifExists);
        PrinterFailedHistoryEntityDao.dropTable(db, ifExists);
        JpushMessageEntityDao.dropTable(db, ifExists);
        BillAccountEntityDao.dropTable(db, ifExists);
        BillAccountPersonEntityDao.dropTable(db, ifExists);
        BillAccountSignEntityDao.dropTable(db, ifExists);
        BillAccountHistoryEntityDao.dropTable(db, ifExists);
        UploadDataEntityDao.dropTable(db, ifExists);
        VipCardEntityDao.dropTable(db, ifExists);
        WxOrderMessageEntityDao.dropTable(db, ifExists);
        PrintResultEntityDao.dropTable(db, ifExists);
        TurnoverHistoryEntityDao.dropTable(db, ifExists);
        TableCodeEntityDao.dropTable(db, ifExists);
        SendPersonEntityDao.dropTable(db, ifExists);
        MaterialEntityDao.dropTable(db, ifExists);
        DishTypeMaterialEntityDao.dropTable(db, ifExists);
        DishSelectedMaterialEntityDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                if (oldVersion <= 1) {
                    db.execSQL("CREATE TABLE " + " IF NOT EXISTS " + "\"TURNOVER_HISTORY_ENTITY\" (" + //
                            "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                            "\"TURNOVER_HISTORY_ID\" TEXT UNIQUE ," + // 1: turnoverHistoryId
                            "\"START_TURNOVER_TIME\" TEXT," + // 2: startTurnoverTime
                            "\"TURNOVER_START_TIME\" TEXT," + // 3: turnoverStartTime
                            "\"TURNOVER_END_TIME\" TEXT," + // 4: turnoverEndTime
                            "\"CASHIER_NAME\" TEXT," + // 5: cashierName
                            "\"TURNOVER_STATE\" TEXT," + // 6: turnoverState
                            "\"PAY_MENT_TYPE\" TEXT," + // 7: payMentType
                            "\"AREA_TYPE\" TEXT," + // 8: areaType
                            "\"ORDER_TOTAL_COUNT\" TEXT," + // 9: orderTotalCount
                            "\"ORDERED_TOTAL_COUNT\" TEXT," + // 10: orderedTotalCount
                            "\"UN_ORDERED_TOTAL_COUNT\" TEXT," + // 11: unOrderedTotalCount
                            "\"ORDERED_TOTAL_MONEY\" TEXT," + // 12: orderedTotalMoney
                            "\"UN_ORDERED_TOTAL_MONEY\" TEXT," + // 13: unOrderedTotalMoney
                            "\"PAYMENTS\" TEXT," + // 14: payments
                            "\"DISH_TYPES\" TEXT," + // 15: dishTypes
                            "\"MONEY0\" TEXT," + // 16: money0
                            "\"MONEY1\" TEXT," + // 17: money1
                            "\"MONEY2\" TEXT," + // 18: money2
                            "\"MONEY3\" TEXT," + // 19: money3
                            "\"MONEY4\" TEXT," + // 20: money4
                            "\"MONEY5\" TEXT," + // 21: money5
                            "\"MONEY6\" TEXT," + // 22: money6
                            "\"OPERATOR_NAME\" TEXT," + // 23: operatorName
                            "\"PRINT_TIME\" TEXT," +// 24: printTime
                            "\"CREATE_TIME\" INTEGER);"); //25:createTime
                    db.execSQL("ALTER TABLE \"ORDER_ENTITY\" ADD \"IS_SYNC\" INTEGER");
                }
                if (oldVersion <= 2) {
                    db.execSQL("ALTER TABLE \"DISH_ENTITY\" ADD \"DISH_CODE1\" TEXT");
                    db.execSQL("ALTER TABLE \"EMPLOYEE_ENTITY\" ADD \"IS_RETURN_SOME_ORDER\" INTEGER");
                    db.execSQL("ALTER TABLE \"EMPLOYEE_ENTITY\" ADD \"IS_RETRURN_ALL_ORDER\" INTEGER");
                    db.execSQL("ALTER TABLE \"EMPLOYEE_ENTITY\" ADD \"IS_BIND_VIP\" INTEGER");
                    db.execSQL("ALTER TABLE \"TAOCAN_ENTITY\" ADD \"TAOCAN_CODE1\" TEXT");
                }
                if (oldVersion <= 3) {
                    db.execSQL("CREATE TABLE " + " IF NOT EXISTS " + "\"TABLE_CODE_ENTITY\" (" + //
                            "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                            "\"TABLE_CODE\" TEXT UNIQUE );"); // 1: tableCode
                }
                if (oldVersion <= 4) {
                    db.execSQL("ALTER TABLE \"ORDER_ENTITY\" ADD \"STORE_VERSION\" INTEGER");
                }
                if (oldVersion <= 5) {
                    db.execSQL("ALTER TABLE \"TAKE_OUT_ORDER_ENTITY\" ADD \"BOX_FEE\" INTEGER");
                    db.execSQL("ALTER TABLE \"TAKE_OUT_ORDER_ENTITY\" ADD \"DISPATCH_FEE\" INTEGER");
                }
                if(oldVersion <= 6){
                    db.execSQL("ALTER TABLE \"TAKE_OUT_ORDER_ENTITY\" ADD \"OTHER_ORDER_ID\" TEXT");
                }
                if(oldVersion <= 7){
                    db.execSQL("CREATE TABLE " + " IF NOT EXISTS "  + "\"SEND_PERSON_ENTITY\" (" + //
                            "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                            "\"SEND_PERSON_ID\" TEXT," + // 1: sendPersonId
                            "\"SEND_PERSON_NAME\" TEXT," + // 2: sendPersonName
                            "\"SEND_PERSON_PHONE\" TEXT," + // 3: sendPersonPhone
                            "\"SEND_PERSON_TC\" INTEGER," + // 4: sendPersonTC
                            "\"IS_SEND_MESSAGE\" INTEGER);"); // 5: isSendMessage
                }
                if(oldVersion <= 8){
                    db.execSQL("ALTER TABLE \"ORDER_ENTITY\" ADD \"DISPACHER_TYPE\" INTEGER");
                    db.execSQL("ALTER TABLE \"ORDER_ENTITY\" ADD \"DISPACHER_NAME\" TEXT");
                    db.execSQL("ALTER TABLE \"ORDER_ENTITY\" ADD \"DISPACHER_TEL\" TEXT");
                    db.execSQL("ALTER TABLE \"ORDER_ENTITY\" ADD \"DISPACHER_ID\" TEXT");
                    db.execSQL("ALTER TABLE \"ORDER_ENTITY\" ADD \"DISPACHER_TC\" INTEGER");
                    db.execSQL("ALTER TABLE \"ORDER_ENTITY\" ADD \"DISPACHER_EXTRAL_MONEY\" INTEGER");
                }
                if(oldVersion <= 9){
                    db.execSQL("ALTER TABLE \"ORDER_DISH_ENTITY\" ADD \"IS_PRINT\" INTEGER");
                    db.execSQL("ALTER TABLE \"ORDER_TAOCAN_GROUP_DISH_ENTITY\" ADD \"IS_PRINT\" INTEGER");
                }
                if(oldVersion <= 10){
                    db.execSQL("CREATE TABLE " + " IF NOT EXISTS " + "\"MATERIAL_ENTITY\" (" + //
                            "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                            "\"MATERIAL_ID\" TEXT," + // 1: materialId
                            "\"MATERIAL_NAME\" TEXT," + // 2: materialName
                            "\"MATERIAL_PRICE\" INTEGER);"); // 3: materialPrice
                    db.execSQL("CREATE TABLE " + " IF NOT EXISTS " + "\"DISH_TYPE_MATERIAL_ENTITY\" (" + //
                            "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                            "\"DISH_TYPE_MATERIAL_ID\" TEXT," + // 1: dishTypeMaterialId
                            "\"DISH_TYPE_ID\" TEXT," + // 2: dishTypeId
                            "\"MATERIAL_ID\" TEXT);"); // 3: materialId
                    db.execSQL("CREATE TABLE " + " IF NOT EXISTS " + "\"DISH_SELECTED_MATERIAL_ENTITY\" (" + //
                            "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                            "\"DISH_SELECTED_MATERIAL_ID\" TEXT," + // 1: dishSelectedMaterialId
                            "\"ORDER_ID\" TEXT," + // 2: orderId
                            "\"ORDER_DISH_ID\" TEXT," + // 3: orderDishId
                            "\"DISH_TYPE_MATERIAL_ID\" TEXT," + // 4: dishTypeMaterialId
                            "\"MATERIAL_ID\" TEXT," + // 5: materialId
                            "\"MATERIAL_NAME\" TEXT," + // 6: materialName
                            "\"MATERIAL_PRICE\" INTEGER," + // 7: materialPrice
                            "\"SELECTED_COUNT\" INTEGER," + // 8: selectedCount
                            "\"TOTAL_PRICE\" INTEGER);"); // 9: totalPrice
                }
            }
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(AreaEntityDao.class);
        registerDaoClass(ArrangeEntityDao.class);
        registerDaoClass(DishEntityDao.class);
        registerDaoClass(DishTypeEntityDao.class);
        registerDaoClass(EmployeeEntityDao.class);
        registerDaoClass(KichenDishEntityDao.class);
        registerDaoClass(KichenPrintEntityDao.class);
        registerDaoClass(KitchenDishTypeEntityDao.class);
        registerDaoClass(KitchenDisPrintEntityDao.class);
        registerDaoClass(OrderDishEntityDao.class);
        registerDaoClass(OrderEntityDao.class);
        registerDaoClass(PayModeEntityDao.class);
        registerDaoClass(PermissionEntityDao.class);
        registerDaoClass(PracticeEntityDao.class);
        registerDaoClass(DishPracticeEntityDao.class);
        registerDaoClass(RankEntityDao.class);
        registerDaoClass(RankPermissionEntityDao.class);
        registerDaoClass(RoomEntityDao.class);
        registerDaoClass(ScheduleEntityDao.class);
        registerDaoClass(SellCheckEntityDao.class);
        registerDaoClass(TableEntityDao.class);
        registerDaoClass(TaocanEntityDao.class);
        registerDaoClass(TaocanGroupDishEntityDao.class);
        registerDaoClass(TaocanGroupEntityDao.class);
        registerDaoClass(TaocanTypeEntityDao.class);
        registerDaoClass(SpecifyEntityDao.class);
        registerDaoClass(DishSpecifyEntityDao.class);
        registerDaoClass(TakeOutOrderEntityDao.class);
        registerDaoClass(CashierDisplayEntityDao.class);
        registerDaoClass(SpecialEntityDao.class);
        registerDaoClass(MantissaEntityDao.class);
        registerDaoClass(SuplusEntityDao.class);
        registerDaoClass(GrouponEntityDao.class);
        registerDaoClass(GrouponTaocanEntityDao.class);
        registerDaoClass(DiscountEntityDao.class);
        registerDaoClass(DishTypeDiscountEntityDao.class);
        registerDaoClass(PrintCashierEntityDao.class);
        registerDaoClass(CashierClassifyEntityDao.class);
        registerDaoClass(CashierDishEntityDao.class);
        registerDaoClass(PrintKitchenEntityDao.class);
        registerDaoClass(PrintKitchenClassifyEntityDao.class);
        registerDaoClass(PrintKitchenDishEntityDao.class);
        registerDaoClass(StandbyPrinterEntityDao.class);
        registerDaoClass(PrintRemarkEntityDao.class);
        registerDaoClass(BasicsPartnerEntityDao.class);
        registerDaoClass(ShopMealsEntityDao.class);
        registerDaoClass(ShopPaymentEntityDao.class);
        registerDaoClass(ShopReceivableEntityDao.class);
        registerDaoClass(ShopTimeEntityDao.class);
        registerDaoClass(SystemMessageEntityDao.class);
        registerDaoClass(WXMessageEntityDao.class);
        registerDaoClass(StoreMessageEntityDao.class);
        registerDaoClass(DiscountHistoryEntityDao.class);
        registerDaoClass(SomeDiscountGoodsEntityDao.class);
        registerDaoClass(OrderTaocanGroupDishEntityDao.class);
        registerDaoClass(PrinterFailedHistoryEntityDao.class);
        registerDaoClass(JpushMessageEntityDao.class);
        registerDaoClass(BillAccountEntityDao.class);
        registerDaoClass(BillAccountPersonEntityDao.class);
        registerDaoClass(BillAccountSignEntityDao.class);
        registerDaoClass(BillAccountHistoryEntityDao.class);
        registerDaoClass(UploadDataEntityDao.class);
        registerDaoClass(VipCardEntityDao.class);
        registerDaoClass(WxOrderMessageEntityDao.class);
        registerDaoClass(PrintResultEntityDao.class);
        registerDaoClass(TurnoverHistoryEntityDao.class);
        registerDaoClass(TableCodeEntityDao.class);
        registerDaoClass(SendPersonEntityDao.class);
        registerDaoClass(MaterialEntityDao.class);
        registerDaoClass(DishTypeMaterialEntityDao.class);
        registerDaoClass(DishSelectedMaterialEntityDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}