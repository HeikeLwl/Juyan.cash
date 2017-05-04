package jufeng.juyancash;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import jufeng.juyancash.bean.ClearModel;
import jufeng.juyancash.bean.DishBean;
import jufeng.juyancash.bean.DishModel;
import jufeng.juyancash.bean.DishTypeCollectionItemBean;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.bean.MeituanDispacherBean;
import jufeng.juyancash.bean.OffLineWxCouponModel;
import jufeng.juyancash.bean.OrderDetailModel;
import jufeng.juyancash.bean.OrderModel;
import jufeng.juyancash.bean.OrderTaocanDetailModel;
import jufeng.juyancash.bean.OrderTaocanModel;
import jufeng.juyancash.bean.PayQRBean;
import jufeng.juyancash.bean.PreOrderModel;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.bean.QueueUserModel;
import jufeng.juyancash.bean.RemindBean;
import jufeng.juyancash.bean.SnackOrderedDishBean;
import jufeng.juyancash.bean.StockBean;
import jufeng.juyancash.bean.TableCodeBean;
import jufeng.juyancash.bean.TableModel;
import jufeng.juyancash.bean.TableOrderModel;
import jufeng.juyancash.bean.TaocanBean;
import jufeng.juyancash.bean.WXPayBean;
import jufeng.juyancash.bean.WxDishMaterialBean;
import jufeng.juyancash.dao.*;
import jufeng.juyancash.syncdata.GoodsInventoryVo;
import jufeng.juyancash.util.AmountUtils;
import jufeng.juyancash.util.CustomMethod;

import static jufeng.juyancash.util.CustomMethod.parseTime;

/**
 * Created by 15157_000 on 2016/7/5 0005.
 */
public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    private static DBHelper instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private AreaEntityDao mAreaEntityDao;
    private ArrangeEntityDao mArrangeEntityDao;
    private BasicsPartnerEntityDao mBasicsPartnerEntityDao;
    private CashierDisplayEntityDao mCashierDisplayEntityDao;
    private DiscountEntityDao mDiscountEntityDao;
    private DishEntityDao mDishEntityDao;
    private DishPracticeEntityDao mDishPracticeEntityDao;
    private DishSpecifyEntityDao mDishSpecifyEntityDao;
    private DishTypeDiscountEntityDao mDishTypeDiscountEntityDao;
    private DishTypeEntityDao mDishTypeEntityDao;
    private EmployeeEntityDao mEmployeeEntityDao;
    private GrouponEntityDao mGrouponEntityDao;
    private GrouponTaocanEntityDao mGrouponTaocanEntityDao;
    private KitchenDishTypeEntityDao mKitchenDishTypeEntityDao;
    private KitchenDisPrintEntityDao mKitchenDisPrintEntityDao;
    private KichenDishEntityDao mKichenDishEntityDao;
    private KichenPrintEntityDao mKichenPrintEntityDao;
    private MantissaEntityDao mMantissaEntityDao;
    private OrderDishEntityDao mOrderDishEntityDao;
    private OrderEntityDao mOrderEntityDao;
    private PermissionEntityDao mPermissionEntityDao;
    private PracticeEntityDao mPracticeEntityDao;
    private PrintCashierEntityDao mPrintCashierEntityDao;
    private PrintKitchenClassifyEntityDao mPrintKitchenClassifyEntityDao;
    private PrintKitchenDishEntityDao mPrintKitchenDishEntityDao;
    private PrintKitchenEntityDao mPrintKitchenEntityDao;
    private PrintRemarkEntityDao mPrintRemarkEntityDao;
    private RankEntityDao mRankEntityDao;
    private RankPermissionEntityDao mRankPermissionEntityDao;
    private RoomEntityDao mRoomEntityDao;
    private ScheduleEntityDao mScheduleEntityDao;
    private SellCheckEntityDao mSellCheckEntityDao;
    private ShopMealsEntityDao mShopMealsEntityDao;
    private ShopPaymentEntityDao mShopPaymentEntityDao;
    private PayModeEntityDao mPayModeEntityDao;
    private ShopReceivableEntityDao mShopReceivableEntityDao;
    private ShopTimeEntityDao mShopTimeEntityDao;
    private SpecialEntityDao mSpecialEntityDao;
    private SpecifyEntityDao mSpecifyEntityDao;
    private StandbyPrinterEntityDao mStandbyPrinterEntityDao;
    private SuplusEntityDao mSuplusEntityDao;
    private TableEntityDao mTableEntityDao;
    private TakeOutOrderEntityDao mTakeOutOrderEntityDao;
    private TaocanEntityDao mTaocanEntityDao;
    private TaocanGroupDishEntityDao mTaocanGroupDishEntityDao;
    private TaocanGroupEntityDao mTaocanGroupEntityDao;
    private TaocanTypeEntityDao mTaocanTypeEntityDao;
    private SystemMessageEntityDao mSystemMessageEntityDao;
    private StoreMessageEntityDao mStoreMessageEntityDao;
    private WXMessageEntityDao mWXMessageEntityDao;
    private DiscountHistoryEntityDao mDiscountHistoryEntityDao;
    private SomeDiscountGoodsEntityDao mSomeDiscountGoodsEntityDao;
    private OrderTaocanGroupDishEntityDao mOrderTaocanGroupDishEntityDao;
    private PrinterFailedHistoryEntityDao mPrinterFailedHistoryEntityDao;
    private JpushMessageEntityDao mJpushMessageEntityDao;
    private BillAccountEntityDao mBillAccountEntityDao;
    private BillAccountPersonEntityDao mBillAccountPersonEntityDao;
    private BillAccountSignEntityDao mBillAccountSignEntityDao;
    private BillAccountHistoryEntityDao mBillAccountHistoryEntityDao;
    private UploadDataEntityDao mUploadDataEntityDao;
    private CashierClassifyEntityDao mCashierClassifyEntityDao;
    private CashierDishEntityDao mCashierDishEntityDao;
    private VipCardEntityDao mVipCardEntityDao;
    private WxOrderMessageEntityDao mWxOrderMessageEntityDao;
    private PrintResultEntityDao mPrintResultEntityDao;
    private TurnoverHistoryEntityDao mTurnoverHistoryEntityDao;
    private TableCodeEntityDao mTableCodeEntityDao;
    private SendPersonEntityDao mSendPersonEntityDao;
    private MaterialEntityDao mMaterialEntityDao;
    private DishTypeMaterialEntityDao mDishTypeMaterialEntityDao;
    private DishSelectedMaterialEntityDao mDishSelectedMaterialEntityDao;

    private DBHelper() {
    }

    //单例模式，DBHelper只初始化一次
    public static DBHelper getInstance(Context context) {

        if (instance == null) {
            instance = new DBHelper();
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = CashApplication.getDaoSession(appContext);
            instance.mAreaEntityDao = instance.mDaoSession.getAreaEntityDao();
            instance.mArrangeEntityDao = instance.mDaoSession.getArrangeEntityDao();
            instance.mBasicsPartnerEntityDao = instance.mDaoSession.getBasicsPartnerEntityDao();
            instance.mCashierDisplayEntityDao = instance.mDaoSession.getCashierDisplayEntityDao();
            instance.mDiscountEntityDao = instance.mDaoSession.getDiscountEntityDao();
            instance.mDishEntityDao = instance.mDaoSession.getDishEntityDao();
            instance.mDishPracticeEntityDao = instance.mDaoSession.getDishPracticeEntityDao();
            instance.mDishSpecifyEntityDao = instance.mDaoSession.getDishSpecifyEntityDao();
            instance.mDishTypeDiscountEntityDao = instance.mDaoSession.getDishTypeDiscountEntityDao();
            instance.mDishTypeEntityDao = instance.mDaoSession.getDishTypeEntityDao();
            instance.mEmployeeEntityDao = instance.mDaoSession.getEmployeeEntityDao();
            instance.mGrouponEntityDao = instance.mDaoSession.getGrouponEntityDao();
            instance.mGrouponTaocanEntityDao = instance.mDaoSession.getGrouponTaocanEntityDao();
            instance.mKitchenDishTypeEntityDao = instance.mDaoSession.getKitchenDishTypeEntityDao();
            instance.mKitchenDisPrintEntityDao = instance.mDaoSession.getKitchenDisPrintEntityDao();
            instance.mKichenDishEntityDao = instance.mDaoSession.getKichenDishEntityDao();
            instance.mKichenPrintEntityDao = instance.mDaoSession.getKichenPrintEntityDao();
            instance.mMantissaEntityDao = instance.mDaoSession.getMantissaEntityDao();
            instance.mOrderDishEntityDao = instance.mDaoSession.getOrderDishEntityDao();
            instance.mOrderEntityDao = instance.mDaoSession.getOrderEntityDao();
            instance.mPermissionEntityDao = instance.mDaoSession.getPermissionEntityDao();
            instance.mPracticeEntityDao = instance.mDaoSession.getPracticeEntityDao();
            instance.mPrintCashierEntityDao = instance.mDaoSession.getPrintCashierEntityDao();
            instance.mPrintKitchenClassifyEntityDao = instance.mDaoSession.getPrintKitchenClassifyEntityDao();
            instance.mPrintKitchenDishEntityDao = instance.mDaoSession.getPrintKitchenDishEntityDao();
            instance.mPrintKitchenEntityDao = instance.mDaoSession.getPrintKitchenEntityDao();
            instance.mPrintRemarkEntityDao = instance.mDaoSession.getPrintRemarkEntityDao();
            instance.mRankEntityDao = instance.mDaoSession.getRankEntityDao();
            instance.mRankPermissionEntityDao = instance.mDaoSession.getRankPermissionEntityDao();
            instance.mRoomEntityDao = instance.mDaoSession.getRoomEntityDao();
            instance.mScheduleEntityDao = instance.mDaoSession.getScheduleEntityDao();
            instance.mSellCheckEntityDao = instance.mDaoSession.getSellCheckEntityDao();
            instance.mShopMealsEntityDao = instance.mDaoSession.getShopMealsEntityDao();
            instance.mShopPaymentEntityDao = instance.mDaoSession.getShopPaymentEntityDao();
            instance.mPayModeEntityDao = instance.mDaoSession.getPayModeEntityDao();
            instance.mShopReceivableEntityDao = instance.mDaoSession.getShopReceivableEntityDao();
            instance.mShopTimeEntityDao = instance.mDaoSession.getShopTimeEntityDao();
            instance.mSpecialEntityDao = instance.mDaoSession.getSpecialEntityDao();
            instance.mSpecifyEntityDao = instance.mDaoSession.getSpecifyEntityDao();
            instance.mStandbyPrinterEntityDao = instance.mDaoSession.getStandbyPrinterEntityDao();
            instance.mSuplusEntityDao = instance.mDaoSession.getSuplusEntityDao();
            instance.mTableEntityDao = instance.mDaoSession.getTableEntityDao();
            instance.mTakeOutOrderEntityDao = instance.mDaoSession.getTakeOutOrderEntityDao();
            instance.mTaocanEntityDao = instance.mDaoSession.getTaocanEntityDao();
            instance.mTaocanGroupDishEntityDao = instance.mDaoSession.getTaocanGroupDishEntityDao();
            instance.mTaocanGroupEntityDao = instance.mDaoSession.getTaocanGroupEntityDao();
            instance.mTaocanTypeEntityDao = instance.mDaoSession.getTaocanTypeEntityDao();
            instance.mSystemMessageEntityDao = instance.mDaoSession.getSystemMessageEntityDao();
            instance.mStoreMessageEntityDao = instance.mDaoSession.getStoreMessageEntityDao();
            instance.mWXMessageEntityDao = instance.mDaoSession.getWXMessageEntityDao();
            instance.mDiscountHistoryEntityDao = instance.mDaoSession.getDiscountHistoryEntityDao();
            instance.mSomeDiscountGoodsEntityDao = instance.mDaoSession.getSomeDiscountGoodsEntityDao();
            instance.mOrderTaocanGroupDishEntityDao = instance.mDaoSession.getOrderTaocanGroupDishEntityDao();
            instance.mPrinterFailedHistoryEntityDao = instance.mDaoSession.getPrinterFailedHistoryEntityDao();
            instance.mJpushMessageEntityDao = instance.mDaoSession.getJpushMessageEntityDao();
            instance.mBillAccountEntityDao = instance.mDaoSession.getBillAccountEntityDao();
            instance.mBillAccountPersonEntityDao = instance.mDaoSession.getBillAccountPersonEntityDao();
            instance.mBillAccountSignEntityDao = instance.mDaoSession.getBillAccountSignEntityDao();
            instance.mBillAccountHistoryEntityDao = instance.mDaoSession.getBillAccountHistoryEntityDao();
            instance.mUploadDataEntityDao = instance.mDaoSession.getUploadDataEntityDao();
            instance.mCashierClassifyEntityDao = instance.mDaoSession.getCashierClassifyEntityDao();
            instance.mCashierDishEntityDao = instance.mDaoSession.getCashierDishEntityDao();
            instance.mVipCardEntityDao = instance.mDaoSession.getVipCardEntityDao();
            instance.mWxOrderMessageEntityDao = instance.mDaoSession.getWxOrderMessageEntityDao();
            instance.mPrintResultEntityDao = instance.mDaoSession.getPrintResultEntityDao();
            instance.mTurnoverHistoryEntityDao = instance.mDaoSession.getTurnoverHistoryEntityDao();
            instance.mTableCodeEntityDao = instance.mDaoSession.getTableCodeEntityDao();
            instance.mSendPersonEntityDao = instance.mDaoSession.getSendPersonEntityDao();
            instance.mMaterialEntityDao = instance.mDaoSession.getMaterialEntityDao();
            instance.mDishTypeMaterialEntityDao = instance.mDaoSession.getDishTypeMaterialEntityDao();
            instance.mDishSelectedMaterialEntityDao = instance.mDaoSession.getDishSelectedMaterialEntityDao();
        }
        return instance;
    }

    //创建所有表
    public void createAllTable() {
        clearBaseDataTable();
        AreaEntityDao.createTable(mDaoSession.getDatabase(), true);
        ArrangeEntityDao.createTable(mDaoSession.getDatabase(), true);
        BasicsPartnerEntityDao.createTable(mDaoSession.getDatabase(), true);
        CashierDisplayEntityDao.createTable(mDaoSession.getDatabase(), true);
        DiscountEntityDao.createTable(mDaoSession.getDatabase(), true);
        DishEntityDao.createTable(mDaoSession.getDatabase(), true);
        DishPracticeEntityDao.createTable(mDaoSession.getDatabase(), true);
        DishSpecifyEntityDao.createTable(mDaoSession.getDatabase(), true);
        DishTypeDiscountEntityDao.createTable(mDaoSession.getDatabase(), true);
        DishTypeEntityDao.createTable(mDaoSession.getDatabase(), true);
        EmployeeEntityDao.createTable(mDaoSession.getDatabase(), true);
        GrouponEntityDao.createTable(mDaoSession.getDatabase(), true);
        GrouponTaocanEntityDao.createTable(mDaoSession.getDatabase(), true);
        KichenDishEntityDao.createTable(mDaoSession.getDatabase(), true);
        KichenPrintEntityDao.createTable(mDaoSession.getDatabase(), true);
        KitchenDishTypeEntityDao.createTable(mDaoSession.getDatabase(), true);
        KitchenDisPrintEntityDao.createTable(mDaoSession.getDatabase(), true);
        MantissaEntityDao.createTable(mDaoSession.getDatabase(), true);
        OrderDishEntityDao.createTable(mDaoSession.getDatabase(), true);
        OrderEntityDao.createTable(mDaoSession.getDatabase(), true);
        PermissionEntityDao.createTable(mDaoSession.getDatabase(), true);
        PracticeEntityDao.createTable(mDaoSession.getDatabase(), true);
        PrintCashierEntityDao.createTable(mDaoSession.getDatabase(), true);
        PrintKitchenClassifyEntityDao.createTable(mDaoSession.getDatabase(), true);
        PrintKitchenDishEntityDao.createTable(mDaoSession.getDatabase(), true);
        PrintKitchenEntityDao.createTable(mDaoSession.getDatabase(), true);
        PrintRemarkEntityDao.createTable(mDaoSession.getDatabase(), true);
        RankEntityDao.createTable(mDaoSession.getDatabase(), true);
        RankPermissionEntityDao.createTable(mDaoSession.getDatabase(), true);
        RoomEntityDao.createTable(mDaoSession.getDatabase(), true);
        ScheduleEntityDao.createTable(mDaoSession.getDatabase(), true);
        SellCheckEntityDao.createTable(mDaoSession.getDatabase(), true);
        ShopMealsEntityDao.createTable(mDaoSession.getDatabase(), true);
        ShopPaymentEntityDao.createTable(mDaoSession.getDatabase(), true);
        ShopReceivableEntityDao.createTable(mDaoSession.getDatabase(), true);
        ShopTimeEntityDao.createTable(mDaoSession.getDatabase(), true);
        SpecialEntityDao.createTable(mDaoSession.getDatabase(), true);
        SpecifyEntityDao.createTable(mDaoSession.getDatabase(), true);
        StandbyPrinterEntityDao.createTable(mDaoSession.getDatabase(), true);
        SuplusEntityDao.createTable(mDaoSession.getDatabase(), true);
        TableEntityDao.createTable(mDaoSession.getDatabase(), true);
        TakeOutOrderEntityDao.createTable(mDaoSession.getDatabase(), true);
        TaocanEntityDao.createTable(mDaoSession.getDatabase(), true);
        TaocanGroupDishEntityDao.createTable(mDaoSession.getDatabase(), true);
        TaocanGroupEntityDao.createTable(mDaoSession.getDatabase(), true);
        TaocanTypeEntityDao.createTable(mDaoSession.getDatabase(), true);
        PayModeEntityDao.createTable(mDaoSession.getDatabase(), true);
        SystemMessageEntityDao.createTable(mDaoSession.getDatabase(), true);
        StoreMessageEntityDao.createTable(mDaoSession.getDatabase(), true);
        WXMessageEntityDao.createTable(mDaoSession.getDatabase(), true);
        DiscountHistoryEntityDao.createTable(mDaoSession.getDatabase(), true);
        SomeDiscountGoodsEntityDao.createTable(mDaoSession.getDatabase(), true);
        OrderTaocanGroupDishEntityDao.createTable(mDaoSession.getDatabase(), true);
        PrinterFailedHistoryEntityDao.createTable(mDaoSession.getDatabase(), true);
        JpushMessageEntityDao.createTable(mDaoSession.getDatabase(), true);
        BillAccountEntityDao.createTable(mDaoSession.getDatabase(), true);
        BillAccountPersonEntityDao.createTable(mDaoSession.getDatabase(), true);
        BillAccountSignEntityDao.createTable(mDaoSession.getDatabase(), true);
        BillAccountHistoryEntityDao.createTable(mDaoSession.getDatabase(), true);
        UploadDataEntityDao.createTable(mDaoSession.getDatabase(), true);
        CashierClassifyEntityDao.createTable(mDaoSession.getDatabase(), true);
        CashierDishEntityDao.createTable(mDaoSession.getDatabase(), true);
        VipCardEntityDao.createTable(mDaoSession.getDatabase(), true);
        WxOrderMessageEntityDao.createTable(mDaoSession.getDatabase(), true);
        PrintResultEntityDao.createTable(mDaoSession.getDatabase(), true);
        TurnoverHistoryEntityDao.createTable(mDaoSession.getDatabase(), true);
        TableCodeEntityDao.createTable(mDaoSession.getDatabase(), true);
        SendPersonEntityDao.createTable(mDaoSession.getDatabase(), true);
        MaterialEntityDao.createTable(mDaoSession.getDatabase(), true);
        DishTypeMaterialEntityDao.createTable(mDaoSession.getDatabase(), true);
        DishSelectedMaterialEntityDao.createTable(mDaoSession.getDatabase(), true);
    }

    //清空数据库
    public void dropAllTable() {
        AreaEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ArrangeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        BasicsPartnerEntityDao.dropTable(mDaoSession.getDatabase(), true);
        CashierDisplayEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DiscountEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishPracticeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishSpecifyEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishTypeDiscountEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishTypeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        EmployeeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        GrouponEntityDao.dropTable(mDaoSession.getDatabase(), true);
        GrouponTaocanEntityDao.dropTable(mDaoSession.getDatabase(), true);
        KichenDishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        KichenPrintEntityDao.dropTable(mDaoSession.getDatabase(), true);
        KitchenDishTypeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        KitchenDisPrintEntityDao.dropTable(mDaoSession.getDatabase(), true);
        MantissaEntityDao.dropTable(mDaoSession.getDatabase(), true);
        OrderDishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        OrderEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PermissionEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PracticeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintCashierEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintKitchenClassifyEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintKitchenDishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintKitchenEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintRemarkEntityDao.dropTable(mDaoSession.getDatabase(), true);
        RankEntityDao.dropTable(mDaoSession.getDatabase(), true);
        RankPermissionEntityDao.dropTable(mDaoSession.getDatabase(), true);
        RoomEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ScheduleEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SellCheckEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ShopMealsEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ShopPaymentEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ShopReceivableEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ShopTimeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SpecialEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SpecifyEntityDao.dropTable(mDaoSession.getDatabase(), true);
        StandbyPrinterEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SuplusEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TableEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TakeOutOrderEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TaocanEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TaocanGroupDishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TaocanGroupEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TaocanTypeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PayModeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SystemMessageEntityDao.dropTable(mDaoSession.getDatabase(), true);
        StoreMessageEntityDao.dropTable(mDaoSession.getDatabase(), true);
        WXMessageEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DiscountHistoryEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SomeDiscountGoodsEntityDao.dropTable(mDaoSession.getDatabase(), true);
        OrderTaocanGroupDishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrinterFailedHistoryEntityDao.dropTable(mDaoSession.getDatabase(), true);
        BillAccountEntityDao.dropTable(mDaoSession.getDatabase(), true);
        BillAccountPersonEntityDao.dropTable(mDaoSession.getDatabase(), true);
        BillAccountSignEntityDao.dropTable(mDaoSession.getDatabase(), true);
        BillAccountHistoryEntityDao.dropTable(mDaoSession.getDatabase(), true);
        JpushMessageEntityDao.dropTable(mDaoSession.getDatabase(), true);
        UploadDataEntityDao.dropTable(mDaoSession.getDatabase(), true);
        CashierClassifyEntityDao.dropTable(mDaoSession.getDatabase(), true);
        CashierDishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        VipCardEntityDao.dropTable(mDaoSession.getDatabase(), true);
        WxOrderMessageEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintResultEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TurnoverHistoryEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TableCodeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SendPersonEntityDao.dropTable(mDaoSession.getDatabase(), true);
        MaterialEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishTypeMaterialEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishSelectedMaterialEntityDao.dropTable(mDaoSession.getDatabase(), true);
    }

    public void clearBaseDataTable() {
        AreaEntityDao.dropTable(mDaoSession.getDatabase(), true);
        CashierDisplayEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DiscountEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishPracticeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishSpecifyEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishTypeDiscountEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishTypeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        EmployeeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        GrouponEntityDao.dropTable(mDaoSession.getDatabase(), true);
        GrouponTaocanEntityDao.dropTable(mDaoSession.getDatabase(), true);
        MantissaEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PermissionEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PracticeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintCashierEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintKitchenClassifyEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintKitchenDishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintKitchenEntityDao.dropTable(mDaoSession.getDatabase(), true);
        PrintRemarkEntityDao.dropTable(mDaoSession.getDatabase(), true);
        RankEntityDao.dropTable(mDaoSession.getDatabase(), true);
        RankPermissionEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ShopMealsEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ShopPaymentEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ShopReceivableEntityDao.dropTable(mDaoSession.getDatabase(), true);
        ShopTimeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SpecialEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SpecifyEntityDao.dropTable(mDaoSession.getDatabase(), true);
        StandbyPrinterEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SuplusEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TableEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TaocanEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TaocanGroupDishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TaocanGroupEntityDao.dropTable(mDaoSession.getDatabase(), true);
        TaocanTypeEntityDao.dropTable(mDaoSession.getDatabase(), true);
        BillAccountEntityDao.dropTable(mDaoSession.getDatabase(), true);
        BillAccountPersonEntityDao.dropTable(mDaoSession.getDatabase(), true);
        BillAccountSignEntityDao.dropTable(mDaoSession.getDatabase(), true);
        CashierClassifyEntityDao.dropTable(mDaoSession.getDatabase(), true);
        CashierDishEntityDao.dropTable(mDaoSession.getDatabase(), true);
        VipCardEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SellCheckEntityDao.dropTable(mDaoSession.getDatabase(), true);
        SendPersonEntityDao.dropTable(mDaoSession.getDatabase(), true);
        MaterialEntityDao.dropTable(mDaoSession.getDatabase(), true);
        DishTypeMaterialEntityDao.dropTable(mDaoSession.getDatabase(), true);
    }

    /**
     * 删除部分数据
     */
    public void deleteSomeData() {
        long zeroTime = CustomMethod.parseTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        long yestodayZeroTime = zeroTime - 1000 * 60 * 60 * 24;
        //删除昨天的已完成的排号数据
        QueryBuilder queryBuilder = mArrangeEntityDao.queryBuilder();
        queryBuilder.where(queryBuilder.and(ArrangeEntityDao.Properties.ArrangeStatus.eq(1), ArrangeEntityDao.Properties.SignTime.lt(zeroTime))).buildDelete().executeDeleteWithoutDetachingEntities();

        //删除昨天的已完成或者已取消的预定数据
        queryBuilder = mScheduleEntityDao.queryBuilder();
        queryBuilder.where(queryBuilder.and(queryBuilder.or(ScheduleEntityDao.Properties.ScheduleStatus.eq(2), ScheduleEntityDao.Properties.ScheduleStatus.eq(3)), ScheduleEntityDao.Properties.MealTime.lt(zeroTime))).buildDelete().executeDeleteWithoutDetachingEntities();

        //删除两天前的外卖数据
        queryBuilder = mTakeOutOrderEntityDao.queryBuilder();
        ArrayList<TakeOutOrderEntity> takeOutOrderEntities = new ArrayList<>();
        takeOutOrderEntities.addAll(queryBuilder.where(queryBuilder.and(TakeOutOrderEntityDao.Properties.TakeoutTime.lt(yestodayZeroTime), queryBuilder.or(TakeOutOrderEntityDao.Properties.TakeoutStatus.eq(4), TakeOutOrderEntityDao.Properties.TakeoutStatus.eq(6)))).list());
        for (TakeOutOrderEntity takeOutOrder :
                takeOutOrderEntities) {
            if (takeOutOrder != null && takeOutOrder.getOrderId() != null) {
                deleteAllOrderDishByOrderId(takeOutOrder.getOrderId());
            }
        }
        takeOutOrderEntities.clear();

        //删除两天前的所有订单数据
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        queryBuilder = mOrderEntityDao.queryBuilder();
        orderEntities.addAll(queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderStatus.eq(1), OrderEntityDao.Properties.CloseTime.lt(yestodayZeroTime))).list());
        for (OrderEntity orderEntity :
                orderEntities) {
            deleteAllOrderDishByOrderId(orderEntity.getOrderId());
        }

        //删除微信消息的两天前的数据
        mWXMessageEntityDao.queryBuilder().where(WXMessageEntityDao.Properties.WxTime.lt(yestodayZeroTime)).buildDelete().executeDeleteWithoutDetachingEntities();

        //删除交接班记录
        mTurnoverHistoryEntityDao.queryBuilder().where(TurnoverHistoryEntityDao.Properties.CreateTime.lt(yestodayZeroTime)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 清除订单相关数据
     *
     * @param orderId
     */
    public void deleteAllOrderDishByOrderId(String orderId) {
        mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mOrderTaocanGroupDishEntityDao.queryBuilder().where(OrderTaocanGroupDishEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mPrinterFailedHistoryEntityDao.queryBuilder().where(PrinterFailedHistoryEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mPayModeEntityDao.queryBuilder().where(PayModeEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mDiscountHistoryEntityDao.queryBuilder().where(DiscountHistoryEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mBillAccountHistoryEntityDao.queryBuilder().where(BillAccountHistoryEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mScheduleEntityDao.queryBuilder().where(ScheduleEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mTakeOutOrderEntityDao.queryBuilder().where(TakeOutOrderEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mUploadDataEntityDao.queryBuilder().where(UploadDataEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mJpushMessageEntityDao.queryBuilder().where(JpushMessageEntityDao.Properties.Message.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mWxOrderMessageEntityDao.queryBuilder().where(WxOrderMessageEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        mDishSelectedMaterialEntityDao.queryBuilder().where(DishSelectedMaterialEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    //插入或者更新记录
    public long saveObject(Object object) {
        if (object instanceof AreaEntity) {
            return mAreaEntityDao.insertOrReplace((AreaEntity) object);
        } else if (object instanceof ArrangeEntity) {
            return mArrangeEntityDao.insertOrReplace((ArrangeEntity) object);
        } else if (object instanceof BasicsPartnerEntity) {
            return mBasicsPartnerEntityDao.insertOrReplace((BasicsPartnerEntity) object);
        } else if (object instanceof CashierDisplayEntity) {
            return mCashierDisplayEntityDao.insertOrReplace((CashierDisplayEntity) object);
        } else if (object instanceof DiscountEntity) {
            return mDiscountEntityDao.insertOrReplace((DiscountEntity) object);
        } else if (object instanceof DishEntity) {
            return mDishEntityDao.insertOrReplace((DishEntity) object);
        } else if (object instanceof DishPracticeEntity) {
            return mDishPracticeEntityDao.insertOrReplace((DishPracticeEntity) object);
        } else if (object instanceof DishSpecifyEntity) {
            return mDishSpecifyEntityDao.insertOrReplace((DishSpecifyEntity) object);
        } else if (object instanceof DishTypeDiscountEntity) {
            return mDishTypeDiscountEntityDao.insertOrReplace((DishTypeDiscountEntity) object);
        } else if (object instanceof DishTypeEntity) {
            return mDishTypeEntityDao.insertOrReplace((DishTypeEntity) object);
        } else if (object instanceof EmployeeEntity) {
            return mEmployeeEntityDao.insertOrReplace((EmployeeEntity) object);
        } else if (object instanceof GrouponEntity) {
            return mGrouponEntityDao.insertOrReplace((GrouponEntity) object);
        } else if (object instanceof GrouponTaocanEntity) {
            return mGrouponTaocanEntityDao.insertOrReplace((GrouponTaocanEntity) object);
        } else if (object instanceof KichenDishEntity) {
            return mKichenDishEntityDao.insertOrReplace((KichenDishEntity) object);
        } else if (object instanceof KichenPrintEntity) {
            return mKichenPrintEntityDao.insertOrReplace((KichenPrintEntity) object);
        } else if (object instanceof KitchenDishTypeEntity) {
            return mKitchenDishTypeEntityDao.insertOrReplace((KitchenDishTypeEntity) object);
        } else if (object instanceof KitchenDisPrintEntity) {
            return mKitchenDisPrintEntityDao.insertOrReplace((KitchenDisPrintEntity) object);
        } else if (object instanceof MantissaEntity) {
            return mMantissaEntityDao.insertOrReplace((MantissaEntity) object);
        } else if (object instanceof OrderDishEntity) {
            return mOrderDishEntityDao.insertOrReplace((OrderDishEntity) object);
        } else if (object instanceof OrderEntity) {
            return mOrderEntityDao.insertOrReplace((OrderEntity) object);
        } else if (object instanceof PermissionEntity) {
            return mPermissionEntityDao.insertOrReplace((PermissionEntity) object);
        } else if (object instanceof PracticeEntity) {
            return mPracticeEntityDao.insertOrReplace((PracticeEntity) object);
        } else if (object instanceof PrintCashierEntity) {
            return mPrintCashierEntityDao.insertOrReplace((PrintCashierEntity) object);
        } else if (object instanceof PrintKitchenClassifyEntity) {
            return mPrintKitchenClassifyEntityDao.insertOrReplace((PrintKitchenClassifyEntity) object);
        } else if (object instanceof PrintKitchenDishEntity) {
            return mPrintKitchenDishEntityDao.insertOrReplace((PrintKitchenDishEntity) object);
        } else if (object instanceof PrintKitchenEntity) {
            return mPrintKitchenEntityDao.insertOrReplace((PrintKitchenEntity) object);
        } else if (object instanceof PrintRemarkEntity) {
            return mPrintRemarkEntityDao.insertOrReplace((PrintRemarkEntity) object);
        } else if (object instanceof RankEntity) {
            return mRankEntityDao.insertOrReplace((RankEntity) object);
        } else if (object instanceof RankPermissionEntity) {
            return mRankPermissionEntityDao.insertOrReplace((RankPermissionEntity) object);
        } else if (object instanceof RoomEntity) {
            return mRoomEntityDao.insertOrReplace((RoomEntity) object);
        } else if (object instanceof ScheduleEntity) {
            return mScheduleEntityDao.insertOrReplace((ScheduleEntity) object);
        } else if (object instanceof SellCheckEntity) {
            return mSellCheckEntityDao.insertOrReplace((SellCheckEntity) object);
        } else if (object instanceof ShopMealsEntity) {
            return mShopMealsEntityDao.insertOrReplace((ShopMealsEntity) object);
        } else if (object instanceof ShopPaymentEntity) {
            return mShopPaymentEntityDao.insertOrReplace((ShopPaymentEntity) object);
        } else if (object instanceof PayModeEntity) {
            return mPayModeEntityDao.insertOrReplace((PayModeEntity) object);
        } else if (object instanceof ShopReceivableEntity) {
            return mShopReceivableEntityDao.insertOrReplace((ShopReceivableEntity) object);
        } else if (object instanceof ShopTimeEntity) {
            return mShopTimeEntityDao.insertOrReplace((ShopTimeEntity) object);
        } else if (object instanceof SpecialEntity) {
            return mSpecialEntityDao.insertOrReplace((SpecialEntity) object);
        } else if (object instanceof SpecifyEntity) {
            return mSpecifyEntityDao.insertOrReplace((SpecifyEntity) object);
        } else if (object instanceof StandbyPrinterEntity) {
            return mStandbyPrinterEntityDao.insertOrReplace((StandbyPrinterEntity) object);
        } else if (object instanceof SuplusEntity) {
            return mSuplusEntityDao.insertOrReplace((SuplusEntity) object);
        } else if (object instanceof TableEntity) {
            return mTableEntityDao.insertOrReplace((TableEntity) object);
        } else if (object instanceof TakeOutOrderEntity) {
            return mTakeOutOrderEntityDao.insertOrReplace((TakeOutOrderEntity) object);
        } else if (object instanceof TaocanEntity) {
            return mTaocanEntityDao.insertOrReplace((TaocanEntity) object);
        } else if (object instanceof TaocanGroupDishEntity) {
            return mTaocanGroupDishEntityDao.insertOrReplace((TaocanGroupDishEntity) object);
        } else if (object instanceof TaocanGroupEntity) {
            return mTaocanGroupEntityDao.insertOrReplace((TaocanGroupEntity) object);
        } else if (object instanceof TaocanTypeEntity) {
            return mTaocanTypeEntityDao.insertOrReplace((TaocanTypeEntity) object);
        } else if (object instanceof BillAccountEntity) {
            return mBillAccountEntityDao.insertOrReplace((BillAccountEntity) object);
        } else if (object instanceof BillAccountPersonEntity) {
            return mBillAccountPersonEntityDao.insertOrReplace((BillAccountPersonEntity) object);
        } else if (object instanceof BillAccountSignEntity) {
            return mBillAccountSignEntityDao.insertOrReplace((BillAccountSignEntity) object);
        } else if (object instanceof CashierClassifyEntity) {
            return mCashierClassifyEntityDao.insertOrReplace((CashierClassifyEntity) object);
        } else if (object instanceof CashierDishEntity) {
            return mCashierDishEntityDao.insertOrReplace((CashierDishEntity) object);
        } else if (object instanceof VipCardEntity) {
            return mVipCardEntityDao.insertOrReplace((VipCardEntity) object);
        } else if (object instanceof SendPersonEntity) {
            return mSendPersonEntityDao.insertOrReplace((SendPersonEntity) object);
        } else if (object instanceof MaterialEntity) {
            return mMaterialEntityDao.insertOrReplace((MaterialEntity) object);
        } else if (object instanceof DishTypeMaterialEntity) {
            return mDishTypeMaterialEntityDao.insertOrReplace((DishTypeMaterialEntity) object);
        } else {
            return -1;
        }
    }

    /**
     * 查询所有员工数据
     *
     * @return
     */
    public ArrayList<EmployeeEntity> queryEmployeeData() {
        ArrayList<EmployeeEntity> employeeEntities = new ArrayList<>();
        try {
            Query query = mDaoSession.queryBuilder(EmployeeEntity.class).build();
            employeeEntities.addAll(query.list());
        } catch (Exception e) {

        }
        return employeeEntities;
    }

    /**
     * 查询所有员工数据，根据职级排序
     */
    public ArrayList<EmployeeEntity> queryEmployeeData1() {
        ArrayList<EmployeeEntity> employeeEntities = new ArrayList<>();
        try {
            Query query = mDaoSession.queryBuilder(EmployeeEntity.class).orderAsc(EmployeeEntityDao.Properties.RankId).build();
            employeeEntities.addAll(query.list());
        } catch (Exception e) {

        }
        return employeeEntities;
    }

    /**
     * 查询区域数据
     *
     * @return
     */
    public ArrayList<AreaEntity> queryAreaData() {
        ArrayList<AreaEntity> areaEntities = new ArrayList<>();
        try {
            Query query = mDaoSession.queryBuilder(AreaEntity.class).build();
            areaEntities.addAll(query.list());
        } catch (Exception e) {

        }
        return areaEntities;
    }

    /**
     * 根据区域ID查询区域名称
     *
     * @param areaId
     * @return
     */
    public String getAreaNameById(String areaId) {
        try {
            AreaEntity areaEntity = mAreaEntityDao.queryBuilder().where(AreaEntityDao.Properties.AreaId.eq(areaId)).build().unique();
            return areaEntity.getAreaName();
        } catch (Exception e) {
            return null;
        }
    }

    //查询桌位数据
    public ArrayList<TableEntity> queryAllTableData(String areaId) {
        ArrayList<TableEntity> tableEntities = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
            tableEntities.addAll(queryBuilder.where(TableEntityDao.Properties.AreaId.eq(areaId)).orderAsc(TableEntityDao.Properties.TableCode).list());
        } catch (Exception e) {

        }
        return tableEntities;
    }

    //查询其他桌位数据
    public ArrayList<TableEntity> queryTableData(String areaId, String oldTableId) {
        ArrayList<TableEntity> tableEntities = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
            if (oldTableId != null) {
                tableEntities.addAll(queryBuilder.where(queryBuilder.and(TableEntityDao.Properties.AreaId.eq(areaId), TableEntityDao.Properties.TableId.notEq(oldTableId))).orderAsc(TableEntityDao.Properties.TableCode).list());
            } else {
                tableEntities.addAll(queryBuilder.where(TableEntityDao.Properties.AreaId.eq(areaId)).orderAsc(TableEntityDao.Properties.TableCode).list());
            }
        } catch (Exception e) {

        }
        return tableEntities;
    }

    //查询桌位数据
    public ArrayList<TableEntity> queryTableData(String areaId) {
        ArrayList<TableEntity> tableEntities = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
            tableEntities.addAll(queryBuilder.where(TableEntityDao.Properties.AreaId.eq(areaId)).orderAsc(TableEntityDao.Properties.TableCode).list());
        } catch (Exception e) {

        }
        return tableEntities;
    }

    //查询桌位数据
    public ArrayList<TableEntity> queryTableData(String areaId, int status) {
        ArrayList<TableEntity> tableEntities = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
            if (status == -1) {
                tableEntities.addAll(queryBuilder.where(TableEntityDao.Properties.AreaId.eq(areaId)).orderAsc(TableEntityDao.Properties.TableCode).list());
            } else if (status == 2) {
                tableEntities.addAll(queryBuilder.where(queryBuilder.and(TableEntityDao.Properties.AreaId.eq(areaId), TableEntityDao.Properties.IsLock.gt(0))).orderAsc(TableEntityDao.Properties.TableCode).list());
            } else {
                tableEntities.addAll(queryBuilder.where(queryBuilder.and(TableEntityDao.Properties.AreaId.eq(areaId), TableEntityDao.Properties.TableStatus.eq(status))).orderAsc(TableEntityDao.Properties.TableCode).list());
            }
        } catch (Exception e) {

        }
        return tableEntities;
    }

    //查询所有桌位数据
    public ArrayList<TableEntity> queryAllTableData() {
        QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
        ArrayList<TableEntity> tableEntities = (ArrayList<TableEntity>) queryBuilder.orderAsc(TableEntityDao.Properties.TableCode).list();
        return tableEntities;
    }

    //根据桌位id查询桌位详情
    public TableEntity queryOneTableData(String tableId) {
        if (tableId == null) {
            return null;
        } else {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
            TableEntity tableEntity = (TableEntity) queryBuilder.where(TableEntityDao.Properties.TableId.eq(tableId)).build().unique();
            return tableEntity;
        }
    }

    //查询菜品分类数据
    public ArrayList<DishTypeEntity> queryDishTypeData() {
        QueryBuilder queryBuilder = mDishTypeEntityDao.queryBuilder().where(DishTypeEntityDao.Properties.IsHasParent.eq(0)).orderDesc(DishTypeEntityDao.Properties.DishTypeCode);
        ArrayList<DishTypeEntity> dishTypeEntities = (ArrayList<DishTypeEntity>) queryBuilder.list();
        return dishTypeEntities;
    }

    //查询菜品子分类数据
    public ArrayList<DishTypeEntity> queryChildDishTypeData(String parentId) {
        ArrayList<DishTypeEntity> dishTypeEntities = new ArrayList<>();
        if (parentId != null) {
            QueryBuilder queryBuilder = mDishTypeEntityDao.queryBuilder();
            queryBuilder.where(queryBuilder.and(DishTypeEntityDao.Properties.IsHasParent.eq(1), DishTypeEntityDao.Properties.ParentId.eq(parentId))).orderDesc(DishTypeEntityDao.Properties.DishTypeCode);
            dishTypeEntities.addAll(queryBuilder.list());
        }
        return dishTypeEntities;
    }

    //获取菜品分类和套餐分类
    public ArrayList<DishTypeModel> queryDishTypeModelData() {
        ArrayList<DishTypeModel> dishTypeModels = new ArrayList<>();
        dishTypeModels.addAll(mDishTypeEntityDao.queryBuilder().list());
        dishTypeModels.addAll(mTaocanTypeEntityDao.queryBuilder().list());
        return dishTypeModels;
    }

    /**
     * 获取套餐分类
     *
     * @return
     */
    public ArrayList<TaocanTypeEntity> queryTaocanTypeData() {
        return (ArrayList<TaocanTypeEntity>) mTaocanTypeEntityDao.queryBuilder().where(TaocanTypeEntityDao.Properties.IsHasParent.eq(0)).orderDesc(TaocanTypeEntityDao.Properties.TaocanTypeCode).list();
    }

    /**
     * 获取套餐子分类
     *
     * @return
     */
    public ArrayList<TaocanTypeEntity> queryChildTaocanTypeData(String parentId) {
        ArrayList<TaocanTypeEntity> results = new ArrayList<>();
        if (parentId != null) {
            QueryBuilder queryBuilder = mTaocanTypeEntityDao.queryBuilder();
            results.addAll(queryBuilder.where(queryBuilder.and(TaocanTypeEntityDao.Properties.IsHasParent.eq(1), TaocanTypeEntityDao.Properties.ParentId.eq(parentId))).orderDesc(TaocanTypeEntityDao.Properties.TaocanTypeCode).list());
        }
        return results;
    }

    /**
     * 根据菜品分类id查询菜品数据
     */
    public DishBean queryOneDishData(String orderId, DishEntity dishEntity) {
        DishBean dishBean = new DishBean();
        dishBean.setDishEntity(dishEntity);
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        if (orderId != null) {
            dishBean.setDishCount(0);
            ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
            orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.DishId.eq(dishEntity.getDishId()), OrderDishEntityDao.Properties.OrderId.eq(orderId))).list());
            for (OrderDishEntity orderDishEntity :
                    orderDishEntities) {
                dishBean.setDishCount(dishBean.getDishCount() + orderDishEntity.getDishCount());
            }
        }
        dishBean.setHasConfig(isDishHasGuige(dishEntity.getDishId()) || isDishHasZuofa(dishEntity.getDishId()));
        dishBean.setChing(isDishChing(dishEntity.getDishId()));
        return dishBean;
    }

    /**
     * 根据菜品分类id查询菜品数据
     */
    public TaocanBean queryOneTaocanData(String orderId, TaocanEntity taocanEntity) {
        TaocanBean taocanBean = new TaocanBean();
        taocanBean.setTaocanEntity(taocanEntity);
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        if (orderId != null) {
            taocanBean.setTaocanCount(0);
            ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
            orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.DishId.eq(taocanEntity.getTaocanId()), OrderDishEntityDao.Properties.OrderId.eq(orderId))).list());
            for (OrderDishEntity orderDishEntity :
                    orderDishEntities) {
                taocanBean.setTaocanCount(taocanBean.getTaocanCount() + orderDishEntity.getDishCount());
            }
        }
        taocanBean.setChing(false);
        return taocanBean;
    }

    /**
     * 根据菜品分类id查询菜品数据
     */
    public ArrayList<DishBean> queryDishDataByType(String typeId, String orderId) {
        ArrayList<DishBean> dishBeanes = new ArrayList<>();
        ArrayList<DishEntity> dishEntities = new ArrayList<>();
        Map<String, Double> counts = new HashMap<>();
        if (typeId != null) {
            dishEntities.addAll(mDishEntityDao.queryBuilder().where(DishEntityDao.Properties.DishTypeId.eq(typeId)).orderDesc(DishEntityDao.Properties.DishCode1).list());
            counts.putAll(getDishCountGroup(orderId, typeId));
        }
        for (DishEntity dishEntity :
                dishEntities) {
            DishBean dishBean = new DishBean();
            dishBean.setDishEntity(dishEntity);
            if (counts.containsKey(dishEntity.getDishId())) {
                dishBean.setDishCount(counts.get(dishEntity.getDishId()));
            } else {
                dishBean.setDishCount(0);
            }
            dishBean.setHasConfig(isDishHasGuige(dishEntity.getDishId()) || isDishHasZuofa(dishEntity.getDishId()));
            dishBean.setChing(isDishChing(dishEntity.getDishId()));
            dishBeanes.add(dishBean);
        }
        return dishBeanes;
    }

    /**
     * 根据套餐分类id获取套餐数据
     *
     * @param typeId
     * @return
     */
    public ArrayList<TaocanBean> queryTaocanDataByTypeId(String typeId, String orderId) {
        ArrayList<TaocanBean> taocanBeanes = new ArrayList<>();
        ArrayList<TaocanEntity> taocanEntities = new ArrayList<>();
        Map<String, Double> counts = new HashMap<>();
        if (typeId != null) {
            taocanEntities.addAll(mTaocanEntityDao.queryBuilder().where(TaocanEntityDao.Properties.TaocanTypeId.eq(typeId)).orderDesc(TaocanEntityDao.Properties.TaocanCode1).list());
            counts.putAll(getDishCountGroup(orderId, typeId));
        }
        for (TaocanEntity taocanEntity :
                taocanEntities) {
            TaocanBean taocanBean = new TaocanBean();
            taocanBean.setTaocanEntity(taocanEntity);
            if (counts.containsKey(taocanEntity.getTaocanId())) {
                taocanBean.setTaocanCount(counts.get(taocanEntity.getTaocanId()));
            } else {
                taocanBean.setTaocanCount(0);
            }
            taocanBean.setChing(isDishChing(taocanEntity.getTaocanId()));
            taocanBeanes.add(taocanBean);
        }
        return taocanBeanes;
    }

    /**
     * 根据套餐id获取套餐所有分组
     *
     * @return
     */
    public ArrayList<TaocanGroupEntity> queryTaocanGroup(String taocanId) {
        return (ArrayList<TaocanGroupEntity>) mTaocanGroupEntityDao.queryBuilder().where(TaocanGroupEntityDao.Properties.TaocanId.eq(taocanId)).list();
    }

    /**
     * 根据套餐id获取套餐名称
     *
     * @param taocanId
     * @return
     */
    public String getTaocanNameById(String taocanId) {
        if(taocanId == null){
            return "";
        }
        TaocanEntity taocanEntity = mTaocanEntityDao.queryBuilder().where(TaocanEntityDao.Properties.TaocanId.eq(taocanId)).build().unique();
        if (taocanEntity == null) {
            return "";
        } else {
            return taocanEntity.getTaocanName();
        }
    }

    /**
     * 根据套餐分组id获取套餐分组内商品
     *
     * @param taocanGroupId
     * @return
     */
    public ArrayList<TaocanGroupDishEntity> queryTaocanGroupDish(String taocanGroupId) {
        return (ArrayList<TaocanGroupDishEntity>) mTaocanGroupDishEntityDao.queryBuilder().where(TaocanGroupDishEntityDao.Properties.TaocanGroupId.eq(taocanGroupId)).list();
    }

    /**
     * 根据套餐id获取套餐
     *
     * @param taocanId
     * @return
     */
    public TaocanEntity queryTaocanById(String taocanId) {
        TaocanEntity taocanEntity = mTaocanEntityDao.queryBuilder().where(TaocanEntityDao.Properties.TaocanId.eq(taocanId)).build().unique();
        return taocanEntity;
    }

    //根据桌位id和账单状态查看账单数据，可能有多个账单
    public ArrayList<OrderEntity> queryOrderDataByTableId(String tableId, int orderStatus) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
        ArrayList<OrderEntity> orderEntities = (ArrayList<OrderEntity>) queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.TableId.eq(tableId), OrderEntityDao.Properties.OrderStatus.eq(orderStatus))).list();
        return orderEntities;
    }

    //根据桌位id和账单状态查看账单数据，可能有多个账单
    public ArrayList<OrderEntity> queryOrderDataByTableId(String tableId, int orderStatus, int orderType) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderType.eq(orderType));
        ArrayList<OrderEntity> orderEntities = (ArrayList<OrderEntity>) queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.TableId.eq(tableId), OrderEntityDao.Properties.OrderStatus.eq(orderStatus))).list();
        return orderEntities;
    }

    //根据桌位id和预订单状态查看预定数据，可能有多个预定单
    public ArrayList<ScheduleEntity> queryScheduleData(String tableId, int scheduleStatus) {
        QueryBuilder queryBuilder = mScheduleEntityDao.queryBuilder();
        ArrayList<ScheduleEntity> scheduleEntities = (ArrayList<ScheduleEntity>) queryBuilder.where(queryBuilder.and(ScheduleEntityDao.Properties.TableId.eq(tableId), ScheduleEntityDao.Properties.ScheduleStatus.eq(scheduleStatus))).orderAsc(ScheduleEntityDao.Properties.MealTime).list();
        return scheduleEntities;
    }

    //插入账单数据
    public void insertTableOrder(Context context, TableEntity tableEntity, OrderEntity orderEntity, int guestsCount, String remark) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        orderEntity.setCashierId(sharedPreferences.getString("employeeId", null));
        orderEntity.setWaiterId(sharedPreferences.getString("employeeId", null));
        orderEntity.setCloseTime(null);
        orderEntity.setIsLimited(isLimitedMeals());
        orderEntity.setIsShift(0);
        orderEntity.setLimitedTime(getLimitedTime());
        orderEntity.setRemindTime(getLimitedRemindTime());
        orderEntity.setOpenTime(System.currentTimeMillis());
        orderEntity.setOrderGuests(guestsCount);
        orderEntity.setOrderNumber(getOrderCount() + 1);
        orderEntity.setOrderStatus(0);
        orderEntity.setTableId(tableEntity.getTableId());
        orderEntity.setAreaId(tableEntity.getAreaId());
        orderEntity.setOrderType(0);
        orderEntity.setIsUpload(1);
        orderEntity.setIsJoinedOrder(0);
        orderEntity.setIsJoinedTable(0);
        orderEntity.setJoinedOrderId(null);
        orderEntity.setJoinedTableId(null);
        orderEntity.setSelfTreatMoney(0);
        orderEntity.setVipDiscountMoney(0);
        orderEntity.setCouponDiscountMoney(0);
        orderEntity.setMantissaMoney(0);
        orderEntity.setTreatmentMoney(0);
        orderEntity.setPresentMoney(0);
        orderEntity.setDiscountMoney(0);
        orderEntity.setDiscountTotalMoney(0);
        orderEntity.setInvoiceMoney(0);
        orderEntity.setStoreVersion(0);
        mOrderEntityDao.insertOrReplace(orderEntity);
    }

    //插入账单数据
    public String insertTableOrder(Context context, TableEntity tableEntity, int guestsCount, String remark) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(UUID.randomUUID().toString());
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        orderEntity.setCashierId(sharedPreferences.getString("employeeId", null));
        orderEntity.setWaiterId(sharedPreferences.getString("employeeId", null));
        orderEntity.setCloseTime(null);
        orderEntity.setIsLimited(isLimitedMeals());
        orderEntity.setIsShift(0);
        orderEntity.setLimitedTime(getLimitedTime());
        orderEntity.setRemindTime(getLimitedRemindTime());
        orderEntity.setOpenTime(System.currentTimeMillis());
        orderEntity.setOrderGuests(guestsCount);
        orderEntity.setOrderNumber(getOrderCount() + 1);
        orderEntity.setOrderStatus(0);
        orderEntity.setTableId(tableEntity.getTableId());
        orderEntity.setAreaId(tableEntity.getAreaId());
        orderEntity.setOrderType(0);
        orderEntity.setIsUpload(0);
        orderEntity.setIsJoinedTable(0);
        orderEntity.setIsJoinedOrder(0);
        orderEntity.setTotalMoney(0);
        orderEntity.setCloseMoney(0);
        orderEntity.setSelfTreatMoney(0);
        orderEntity.setVipDiscountMoney(0);
        orderEntity.setCouponDiscountMoney(0);
        orderEntity.setMantissaMoney(0);
        orderEntity.setTreatmentMoney(0);
        orderEntity.setPresentMoney(0);
        orderEntity.setDiscountMoney(0);
        orderEntity.setDiscountTotalMoney(0);
        orderEntity.setInvoiceMoney(0);
        orderEntity.setStoreVersion(0);
        mOrderEntityDao.insertOrReplace(orderEntity);
        replaceTableStatus(tableEntity.getTableId(), 1);
        return orderEntity.getOrderId();
    }

    //预定插入账单数据
    public String insertTableOrder(Context context, TableEntity tableEntity, long time, int guestsCount, String remark, int orderType) {
        Date date = new Date();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(UUID.randomUUID().toString());
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        orderEntity.setCashierId(sharedPreferences.getString("employeeId", null));
        orderEntity.setWaiterId(sharedPreferences.getString("employeeId", null));
        orderEntity.setCloseTime(null);
        orderEntity.setIsLimited(isLimitedMeals());
        orderEntity.setIsShift(0);
        orderEntity.setLimitedTime(getLimitedTime());
        orderEntity.setRemindTime(getLimitedRemindTime());
        orderEntity.setOpenTime(time);
        orderEntity.setOrderGuests(guestsCount);
        orderEntity.setOrderNumber(getOrderCount() + 1);
        orderEntity.setOrderStatus(0);
        orderEntity.setTableId(tableEntity.getTableId());
        orderEntity.setAreaId(tableEntity.getAreaId());
        orderEntity.setOrderType(orderType);
        orderEntity.setIsUpload(0);
        orderEntity.setIsJoinedTable(0);
        orderEntity.setIsJoinedOrder(0);
        orderEntity.setTotalMoney(0);
        orderEntity.setCloseMoney(0);
        orderEntity.setSelfTreatMoney(0);
        orderEntity.setVipDiscountMoney(0);
        orderEntity.setCouponDiscountMoney(0);
        orderEntity.setMantissaMoney(0);
        orderEntity.setTreatmentMoney(0);
        orderEntity.setPresentMoney(0);
        orderEntity.setDiscountMoney(0);
        orderEntity.setDiscountTotalMoney(0);
        orderEntity.setInvoiceMoney(0);
        orderEntity.setStoreVersion(0);
        mOrderEntityDao.insertOrReplace(orderEntity);
        return orderEntity.getOrderId();
    }

    //插入外卖
    public void insertTakeOut(Context context, OrderModel orderModel) {
        try {
            int count = (int) mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderId.eq(orderModel.getId())).buildCount().count();
            if (count > 0) {
                //已存在
            } else {
                //先生成一个外卖账单
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderId(orderModel.getId());
                SharedPreferences sharedPreferences = appContext.getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                orderEntity.setCashierId(sharedPreferences.getString("employeeId", null));
                orderEntity.setWaiterId(null);
                orderEntity.setOrderType(1);
                orderEntity.setOrderStatus(0);
                orderEntity.setOrderNumber(getOrderCount() + 1);
                orderEntity.setOpenTime(System.currentTimeMillis());
                orderEntity.setIsShift(0);
                orderEntity.setIsUpload(0);
                orderEntity.setSerialNumber(orderModel.getOrderNo());
                orderEntity.setCloseTime(orderModel.getPayTime() == null ? System.currentTimeMillis() : orderModel.getPayTime().getTime());
                orderEntity.setVipNo(orderModel.getVipNo());
                orderEntity.setVipType(orderModel.getVipType());
                orderEntity.setIsVip(orderModel.getVipDis());
                orderEntity.setUserCouponId(null);
                if (orderModel.getWxCoupon() != null) {
                    orderEntity.setCouponId(orderModel.getWxCoupon().getId());
                    orderEntity.setCouponCondition(orderModel.getWxCoupon().getFullCut());
                    orderEntity.setCouponFaceValue(orderModel.getWxCoupon().getFaceValue());
                    orderEntity.setCouponType(orderModel.getWxCoupon().getType());
                    orderEntity.setIsCouponWithVip(orderModel.getWxCoupon().getDisVip());
                    orderEntity.setIsCouponDiscountAll(orderModel.getWxCoupon().getForceDis());
                } else {
                    orderEntity.setCouponId(null);
                    orderEntity.setCouponCondition(null);
                    orderEntity.setCouponFaceValue(null);
                    orderEntity.setCouponType(null);
                    orderEntity.setIsCouponWithVip(null);
                    orderEntity.setIsCouponDiscountAll(null);
                }
                orderEntity.setCouponVipno(orderModel.getVipNo());
                orderEntity.setIsCoupon(orderModel.getUseCoupon());
                orderEntity.setCloseMoney(orderModel.getPayFee());
                orderEntity.setTotalMoney(orderModel.getTotalPrice());
                orderEntity.setSelfTreatMoney(0);
                orderEntity.setVipDiscountMoney(orderModel.getVipDisAmount());
                orderEntity.setCouponDiscountMoney(orderModel.getCouponDisAmount());
                orderEntity.setMantissaMoney(orderModel.getMantissa());
                orderEntity.setTreatmentMoney(orderModel.getMl());
                orderEntity.setPresentMoney(0);
                orderEntity.setDiscountMoney(0);
                orderEntity.setDiscountTotalMoney(orderModel.getYhAmount());
                orderEntity.setInvoiceMoney(0);
                mOrderEntityDao.insertOrReplace(orderEntity);

                TakeOutOrderEntity takeOutOrderEntity = new TakeOutOrderEntity();
                takeOutOrderEntity.setTakeoutId(UUID.randomUUID().toString());
                takeOutOrderEntity.setGuestName(orderModel.getUserName());
                takeOutOrderEntity.setGuestPhone(orderModel.getTel());
                takeOutOrderEntity.setTakeoutAddress(orderModel.getAddress());
                takeOutOrderEntity.setTakeoutMark(orderModel.getRemark());
                takeOutOrderEntity.setOrderId(orderEntity.getOrderId());
                takeOutOrderEntity.setTakeoutFrom(0);
                takeOutOrderEntity.setTakeoutStatus(0);
                if (orderModel.getExtral() == null) {
                    takeOutOrderEntity.setBoxFee(0);
                    takeOutOrderEntity.setDispatchFee(0);
                } else {
                    takeOutOrderEntity.setBoxFee(orderModel.getExtral().getBoxFee());
                    takeOutOrderEntity.setDispatchFee(orderModel.getExtral().getDispatchFee());
                }
                takeOutOrderEntity.setTakeoutMark(orderModel.getRemark());
                takeOutOrderEntity.setTakeoutTime(parseTime(orderModel.getSendTime(), "yyyy-MM-dd HH:mm"));
                mTakeOutOrderEntityDao.insertOrReplace(takeOutOrderEntity);
                ArrayList<OrderDetailModel> orderDetailModels = new ArrayList<>(orderModel.getOrderDetail());
                DishTypeEntity dishType = null;
                for (int i = 0; i < orderDetailModels.size(); i++) {
                    OrderDishEntity orderDishEntity = new OrderDishEntity();
                    orderDishEntity.setDishTypeId(orderDetailModels.get(i).getTypeId());
                    orderDishEntity.setDishId(orderDetailModels.get(i).getGoodsId());
                    orderDishEntity.setOrderDishId(orderDetailModels.get(i).getId());
                    orderDishEntity.setDishCount(orderDetailModels.get(i).getNum());
                    orderDishEntity.setIsOrdered(1);
                    orderDishEntity.setDishName(orderDetailModels.get(i).getGoodsName());
                    orderDishEntity.setPracticeId(orderDetailModels.get(i).getMakeId());
                    orderDishEntity.setDishPractice(orderDetailModels.get(i).getMakeName());
                    orderDishEntity.setDishPrice(Float.parseFloat(AmountUtils.multiply(orderDetailModels.get(i).getGoodsPrice() + "", "0.01")));
                    orderDishEntity.setSpecifyId(orderDetailModels.get(i).getGuigeId());
                    orderDishEntity.setDishSpecify(orderDetailModels.get(i).getGuigeName());
                    orderDishEntity.setOrderId(orderModel.getId());
                    orderDishEntity.setIsAbleDiscount(orderDetailModels.get(i).getDiscount());
                    orderDishEntity.setType(0);
                    DishEntity dishEntity = queryOneDishEntity(orderDetailModels.get(i).getId());
                    if (dishEntity != null) {
                        dishType = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
                        orderDishEntity.setDishTypeId(dishType.getDishTypeId());
                        orderDishEntity.setDishTypeName(dishType.getDishTypeName());
                        orderDishEntity.setIsPresent(dishEntity.getIsAblePresent());
                        orderDishEntity.setIsRetreat(dishEntity.getIsAbleRetreat());
                    } else {
                        orderDishEntity.setIsPresent(0);
                        orderDishEntity.setIsRetreat(0);
                    }
                    ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities = new ArrayList<>();
                    try {
                        ArrayList<WxDishMaterialBean> wxDishMaterialBeens = new ArrayList<>();
                        wxDishMaterialBeens.addAll(JSON.parseArray(orderDetailModels.get(i).getMaterial(), WxDishMaterialBean.class));
                        if (wxDishMaterialBeens.size() > 0) {
                            for (WxDishMaterialBean bean :
                                    wxDishMaterialBeens) {
                                DishSelectedMaterialEntity dishSelectedMaterialEntity = new DishSelectedMaterialEntity();
                                dishSelectedMaterialEntity.setDishSelectedMaterialId(UUID.randomUUID().toString());
                                dishSelectedMaterialEntity.setDishTypeMaterialId(bean.getId());
                                dishSelectedMaterialEntity.setMaterialId(bean.getMaterial().getId());
                                dishSelectedMaterialEntity.setOrderDishId(orderDetailModels.get(i).getId());
                                dishSelectedMaterialEntity.setOrderId(orderModel.getId());
                                dishSelectedMaterialEntity.setTotalPrice(bean.getMaterial().getTotalPrice());
                                dishSelectedMaterialEntity.setMaterialPrice(bean.getMaterial().getPrice());
                                dishSelectedMaterialEntity.setSelectedCount(bean.getMaterial().getSelectedCount());
                                dishSelectedMaterialEntity.setMaterialName(bean.getMaterial().getName());
                                dishSelectedMaterialEntities.add(dishSelectedMaterialEntity);
                            }
                        }
                    } catch (Exception e) {

                    }
                    insertDishSelectMaterial(orderDetailModels.get(i).getId(), dishSelectedMaterialEntities);
                    mOrderDishEntityDao.insertOrReplace(orderDishEntity);
                }
                String payName = "";
                if (orderModel.getPayType() == 2) {
                    payName = "微信";
                    orderModel.setPayTypeId("2");
                } else if (orderModel.getPayType() == 5) {
                    payName = "会员卡";
                    orderModel.setPayTypeId("5");
                } else {
                    payName = "其他";
                }
                insertPayMode(orderEntity.getOrderId(), orderModel.getPayTypeId(), payName, orderModel.getPayType(), Float.valueOf(String.valueOf(((double) orderModel.getPayFee()) / 100)), 0);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    //插入美团外卖
    public void insertMeituanTakeOut(Context context, OrderModel orderModel, String meituanOrderId) {
        try {
            int count = (int) mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderId.eq(orderModel.getId())).buildCount().count();
            if (count > 0) {
                //已存在
            } else {
                //先生成一个外卖账单
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderId(orderModel.getId());
                SharedPreferences sharedPreferences = appContext.getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                orderEntity.setCashierId(sharedPreferences.getString("employeeId", null));
                orderEntity.setWaiterId(null);
                orderEntity.setOrderType(1);
                orderEntity.setOrderStatus(0);
                orderEntity.setOrderNumber(getOrderCount() + 1);
                orderEntity.setOpenTime(System.currentTimeMillis());
                orderEntity.setIsShift(0);
                orderEntity.setIsUpload(0);
                orderEntity.setSerialNumber(orderModel.getOrderNo());
                orderEntity.setCloseTime(System.currentTimeMillis());
                orderEntity.setVipNo(orderModel.getVipNo());
                orderEntity.setVipType(orderModel.getVipType());
                orderEntity.setIsVip(orderModel.getVipDis());
                orderEntity.setUserCouponId(null);
                if (orderModel.getWxCoupon() != null) {
                    orderEntity.setCouponId(orderModel.getWxCoupon().getId());
                    orderEntity.setCouponCondition(orderModel.getWxCoupon().getFullCut());
                    orderEntity.setCouponFaceValue(orderModel.getWxCoupon().getFaceValue());
                    orderEntity.setCouponType(orderModel.getWxCoupon().getType());
                    orderEntity.setIsCouponWithVip(orderModel.getWxCoupon().getDisVip());
                    orderEntity.setIsCouponDiscountAll(orderModel.getWxCoupon().getForceDis());
                } else {
                    orderEntity.setCouponId(null);
                    orderEntity.setCouponCondition(null);
                    orderEntity.setCouponFaceValue(null);
                    orderEntity.setCouponType(null);
                    orderEntity.setIsCouponWithVip(null);
                    orderEntity.setIsCouponDiscountAll(null);
                }
                orderEntity.setCouponVipno(orderModel.getVipNo());
                orderEntity.setIsCoupon(orderModel.getUseCoupon());
                orderEntity.setCloseMoney(orderModel.getPayFee());
                orderEntity.setTotalMoney(orderModel.getTotalPrice());
                orderEntity.setSelfTreatMoney(0);
                orderEntity.setVipDiscountMoney(orderModel.getVipDisAmount());
                orderEntity.setCouponDiscountMoney(orderModel.getCouponDisAmount());
                orderEntity.setMantissaMoney(orderModel.getMantissa());
                orderEntity.setTreatmentMoney(orderModel.getMl());
                orderEntity.setPresentMoney(0);
                orderEntity.setDiscountMoney(0);
                orderEntity.setDiscountTotalMoney(orderModel.getYhAmount());
                orderEntity.setInvoiceMoney(0);
                mOrderEntityDao.insertOrReplace(orderEntity);

                TakeOutOrderEntity takeOutOrderEntity = new TakeOutOrderEntity();
                takeOutOrderEntity.setTakeoutId(UUID.randomUUID().toString());
                takeOutOrderEntity.setGuestName(orderModel.getUserName());
                takeOutOrderEntity.setGuestPhone(orderModel.getTel());
                takeOutOrderEntity.setTakeoutAddress(orderModel.getAddress());
                takeOutOrderEntity.setTakeoutMark(orderModel.getRemark());
                takeOutOrderEntity.setOrderId(orderEntity.getOrderId());
                takeOutOrderEntity.setTakeoutFrom(1);
                takeOutOrderEntity.setTakeoutStatus(0);
                if (orderModel.getExtral() == null) {
                    takeOutOrderEntity.setBoxFee(0);
                    takeOutOrderEntity.setDispatchFee(0);
                } else {
                    takeOutOrderEntity.setBoxFee(orderModel.getExtral().getBoxFee());
                    takeOutOrderEntity.setDispatchFee(orderModel.getExtral().getDispatchFee());
                }
                takeOutOrderEntity.setTakeoutMark(orderModel.getRemark());
                takeOutOrderEntity.setOtherOrderId(meituanOrderId);
                takeOutOrderEntity.setTakeoutTime(CustomMethod.parseTime(orderModel.getSendTime(), "yyyy-MM-dd HH:mm"));
                mTakeOutOrderEntityDao.insertOrReplace(takeOutOrderEntity);
                ArrayList<OrderDetailModel> orderDetailModels = new ArrayList<>(orderModel.getOrderDetail());
                DishTypeEntity dishType = null;
                for (int i = 0; i < orderDetailModels.size(); i++) {
                    OrderDishEntity orderDishEntity = new OrderDishEntity();
                    orderDishEntity.setDishTypeId(orderDetailModels.get(i).getTypeId());
                    orderDishEntity.setDishId(orderDetailModels.get(i).getGoodsId());
                    orderDishEntity.setOrderDishId(UUID.randomUUID().toString());
                    orderDishEntity.setDishCount(orderDetailModels.get(i).getNum());
                    orderDishEntity.setIsOrdered(1);
                    orderDishEntity.setDishName(orderDetailModels.get(i).getGoodsName());
                    orderDishEntity.setPracticeId(orderDetailModels.get(i).getMakeId());
                    orderDishEntity.setDishPractice(orderDetailModels.get(i).getMakeName());
                    orderDishEntity.setDishPrice(Float.parseFloat(AmountUtils.multiply("" + AmountUtils.multiply(orderDetailModels.get(i).getGoodsPrice(), orderDishEntity.getDishCount()), "0.01")));
                    orderDishEntity.setSpecifyId(orderDetailModels.get(i).getGuigeId());
                    orderDishEntity.setDishSpecify(orderDetailModels.get(i).getGuigeName());
                    orderDishEntity.setOrderId(orderModel.getId());
                    orderDishEntity.setIsAbleDiscount(orderDetailModels.get(i).getDiscount());
                    orderDishEntity.setType(0);
                    DishEntity dishEntity = queryOneDishEntity(orderDetailModels.get(i).getId());
                    if (dishEntity != null) {
                        dishType = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
                        orderDishEntity.setDishTypeId(dishType.getDishTypeId());
                        orderDishEntity.setDishTypeName(dishType.getDishTypeName());
                        orderDishEntity.setIsPresent(dishEntity.getIsAblePresent());
                        orderDishEntity.setIsRetreat(dishEntity.getIsAbleRetreat());
                    } else {
                        orderDishEntity.setDishTypeId("123456789");
                        orderDishEntity.setDishTypeName("其他");
                        orderDishEntity.setIsPresent(0);
                        orderDishEntity.setIsRetreat(0);
                    }
                    mOrderDishEntityDao.insertOrReplace(orderDishEntity);
                }
                String payName = "";
                if (orderModel.getPayType() == 1) {
                    payName = "美团货到付款";
                    orderModel.setPayTypeId("6");
                } else if (orderModel.getPayType() == 2) {
                    payName = "美团线上支付";
                    orderModel.setPayTypeId("7");
                } else {
                    payName = "其他";
                }
                insertPayMode(orderEntity.getOrderId(), orderModel.getPayTypeId(), payName, orderModel.getPayType(), Float.valueOf(String.valueOf(((double) orderModel.getPayFee()) / 100)), 0);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.d("###", "插入外卖单异常");
        }
    }

    //修改外卖单状态
    public void changeTakeOutOrderStatus(TakeOutOrderEntity takeOutOrderEntity, int status) {
        takeOutOrderEntity.setTakeoutStatus(status);
        mTakeOutOrderEntityDao.insertOrReplace(takeOutOrderEntity);
        OrderEntity orderEntity = getOneOrderEntity(takeOutOrderEntity.getOrderId());
        if (orderEntity != null) {
            orderEntity.setCloseTime(System.currentTimeMillis());
            if (status == 3) {
                orderEntity.setOrderStatus(1);
            }
            if (status == 5) {
                orderEntity.setOrderStatus(-1);
            }
            mOrderEntityDao.insertOrReplace(orderEntity);
        }
        if (status == 1) {
            //下单
            ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
            orderDishEntities.addAll(queryOrderedDish(orderEntity.getOrderId(), 1));
            for (OrderDishEntity orderDishEntity :
                    orderDishEntities) {
                changeStock(orderDishEntity.getDishId(), orderDishEntity.getDishCount());
            }
            insertUploadData(orderEntity.getOrderId(), orderEntity.getOrderId(), 13);
        }
        mOrderEntityDao.insertOrReplace(orderEntity);
    }

    //修改外卖单状态
    public void changeTakeOutOrderStatus(TakeOutOrderEntity takeOutOrderEntity) {
        mTakeOutOrderEntityDao.insertOrReplace(takeOutOrderEntity);
    }

    //获取待审核的外卖单数量
    public int getOnCheckTakeOutOrderCount() {
        QueryBuilder queryBuilder = mTakeOutOrderEntityDao.queryBuilder();
        return (int) queryBuilder.where(TakeOutOrderEntityDao.Properties.TakeoutStatus.eq(0)).buildCount().count();
    }

    //获取流水号
    public String getSerialNumber(String tableCode, String orderNumber) {
        String result;
        if (tableCode != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            result = Long.valueOf(sdf.format(new Date())) + tableCode + orderNumber;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            result = Long.valueOf(sdf.format(new Date())) + orderNumber;
        }
        return result;
    }

    //获取当日账单总数
    public int getOrderCount() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        //凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
        ArrayList<OrderEntity> orderEntities = (ArrayList<OrderEntity>) queryBuilder.where(OrderEntityDao.Properties.OpenTime.gt(cal.getTimeInMillis())).orderDesc(OrderEntityDao.Properties.OrderNumber).list();
        if (orderEntities.size() > 0) {
            return orderEntities.get(0).getOrderNumber();
        } else {
            return 0;
        }
    }

    //是否限时用餐
    public int isLimitedMeals() {
        QueryBuilder queryBuilder = mShopMealsEntityDao.queryBuilder();
        ArrayList<ShopMealsEntity> shopMealsEntities = new ArrayList<>();
        shopMealsEntities.addAll(queryBuilder.list());
        if (shopMealsEntities.size() > 0) {
            return shopMealsEntities.get(0).getMealsState();
        } else {
            return 0;
        }
    }

    //获取限时用餐时长
    public int getLimitedTime() {
        QueryBuilder queryBuilder = mShopMealsEntityDao.queryBuilder();
        ArrayList<ShopMealsEntity> shopMealsEntities = new ArrayList<>();
        shopMealsEntities.addAll(queryBuilder.list());
        if (shopMealsEntities.size() > 0) {
            return Integer.valueOf(shopMealsEntities.get(0).getMealsStartTime());
        } else {
            return 0;
        }
    }

    //获取提醒时间
    public int getLimitedRemindTime() {
        QueryBuilder queryBuilder = mShopMealsEntityDao.queryBuilder();
        ArrayList<ShopMealsEntity> shopMealsEntities = new ArrayList<>();
        shopMealsEntities.addAll(queryBuilder.list());
        if (shopMealsEntities.size() > 0) {
            return Integer.valueOf(shopMealsEntities.get(0).getMealsRemindTime());
        } else {
            return 0;
        }
    }

    //根据账单id获取对应的商品
    public ArrayList<SnackOrderedDishBean> queryAllOrderdDishEntity(String orderId) {
        ArrayList<SnackOrderedDishBean> results = new ArrayList<>();
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.DishId.notEq("voucherdish"))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        for (OrderDishEntity orderDish :
                orderDishEntities) {
            if (orderDish.getType() == 0) {
                SnackOrderedDishBean bean = new SnackOrderedDishBean(0, orderDish, null);
                results.add(bean);
            } else if (orderDish.getType() == 1) {
                SnackOrderedDishBean bean = new SnackOrderedDishBean(1, orderDish, null);
                results.add(bean);
                ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishes = new ArrayList<>();
                orderTaocanGroupDishes.addAll(getOrderedTaocanDish(orderDish));
                for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                        orderTaocanGroupDishes) {
                    SnackOrderedDishBean bean1 = new SnackOrderedDishBean(2, orderDish, orderTaocanGroupDish);
                    results.add(bean1);
                }
            }
        }
        return results;
    }

    //根据账单id获取对应的商品
    public ArrayList<OrderDishEntity> queryOrderDishEntity(String orderId) {
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.DishId.notEq("voucherdish"))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list());
        }catch (Exception e){

        }
        return orderDishEntities;
    }

    //根据账单id获取未下单商品
    public ArrayList<OrderDishEntity> queryPrintOrderDishEntity(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsOrdered.eq(0))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    /**
     * 获取并单的所有菜品
     *
     * @param joinOrderId
     * @param status
     * @return
     */
    public ArrayList<OrderDishEntity> queryJoinOrderDish(String joinOrderId, int status) {
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        for (OrderEntity orderEntity :
                orderEntities) {
            OrderDishEntity orderDishEntity = new OrderDishEntity();
            orderDishEntity.setOrderId(orderEntity.getOrderId());
            orderDishEntities.add(orderDishEntity);
            orderDishEntities.addAll(queryOrderDishEntity(orderEntity.getOrderId(), 1));
        }
        return orderDishEntities;
    }

    /**
     * 获取并单的所有菜品，除去赠菜，退菜
     *
     * @param joinOrderId
     * @return
     */
    public ArrayList<OrderDishEntity> queryJoinDiscountNormalDish(String joinOrderId) {
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        for (OrderEntity orderEntity :
                orderEntities) {
            orderDishEntities.addAll(queryNormalOrderDish(orderEntity.getOrderId()));
        }
        return orderDishEntities;
    }


    /**
     * 获取并单的所有菜品，除去赠菜，退菜
     *
     * @param joinOrderId
     * @return
     */
    public ArrayList<OrderDishEntity> queryJoinOrderNormalDish(String joinOrderId) {
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        for (OrderEntity orderEntity :
                orderEntities) {
            OrderDishEntity orderDishEntity = new OrderDishEntity();
            orderDishEntity.setOrderId(orderEntity.getOrderId());
            orderDishEntities.add(orderDishEntity);
            orderDishEntities.addAll(queryNormalOrderDish(orderEntity.getOrderId()));
        }
        return orderDishEntities;
    }

    /**
     * 获取并单中可赠送的菜品
     *
     * @param joinOrderId
     * @return
     */
    public ArrayList<OrderDishEntity> queryJoinPresentableDish(String joinOrderId) {
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        for (OrderEntity orderEntity :
                orderEntities) {
            OrderDishEntity orderDishEntity = new OrderDishEntity();
            orderDishEntity.setOrderId(orderEntity.getOrderId());
            orderDishEntities.add(orderDishEntity);
            orderDishEntities.addAll(queryPresentableOrderDish(orderEntity.getOrderId()));
        }
        return orderDishEntities;
    }

    /**
     * 获取并单中可退的菜品
     *
     * @param joinOrderId
     * @return
     */
    public ArrayList<OrderDishEntity> queryJoinRetreatableDish(String joinOrderId) {
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        for (OrderEntity orderEntity :
                orderEntities) {
            OrderDishEntity orderDishEntity = new OrderDishEntity();
            orderDishEntity.setOrderId(orderEntity.getOrderId());
            orderDishEntities.add(orderDishEntity);
            orderDishEntities.addAll(queryRetreatableOrderDish(orderEntity.getOrderId()));
        }
        return orderDishEntities;
    }

    /**
     * 获取已下单的菜品，除去赠菜，退菜
     *
     * @param orderId
     * @return
     */
    public ArrayList<OrderDishEntity> queryNormalOrderDish(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsOrdered.eq(1))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    /**
     * 获取已下单的菜品，除去赠菜，退菜
     *
     * @param orderId
     * @return
     */
    public ArrayList<OrderDishEntity> queryPresentableOrderDish(String orderId) {
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsPresent.eq(1), OrderDishEntityDao.Properties.IsOrdered.eq(1))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list());
        } catch (Exception e) {

        }
        return orderDishEntities;
    }

    /**
     * 获取已下单的菜品，除去赠菜，退菜
     *
     * @param orderId
     * @return
     */
    public ArrayList<OrderDishEntity> queryRetreatableOrderDish(String orderId) {
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        if(orderId == null){
            return orderDishEntities;
        }
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(1), OrderDishEntityDao.Properties.IsOrdered.eq(-2)), OrderDishEntityDao.Properties.DishId.notEq("voucherdish"))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list());
        return orderDishEntities;
    }

    //根据账单id获取已下单的商品
    public ArrayList<OrderDishEntity> queryUploadOrderDish(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(1), OrderDishEntityDao.Properties.IsOrdered.eq(-2)))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    //根据账单id获取已下单的商品
    public ArrayList<OrderDishEntity> queryOrderDishEntity(String orderId, int status) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(status), OrderDishEntityDao.Properties.IsOrdered.eq(-1), OrderDishEntityDao.Properties.IsOrdered.eq(-2)))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    //根据账单id获取除去赠菜和退菜后的商品
    public ArrayList<OrderDishEntity> queryOrderDish(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(0), OrderDishEntityDao.Properties.IsOrdered.eq(1)), OrderDishEntityDao.Properties.DishId.notEq("voucherdish"))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    //根据账单id获取除去赠菜和退菜后的已下单商品
    public ArrayList<OrderDishEntity> queryOrderedDish2(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.DishId.notEq("voucherdish"), OrderDishEntityDao.Properties.IsOrdered.eq(1), OrderDishEntityDao.Properties.IsAbleDiscount.eq(1))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    //根据账单id获取除去赠菜和退菜后的已下单商品
    public ArrayList<OrderDishEntity> queryOrderedDish3(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.DishId.notEq("voucherdish"), OrderDishEntityDao.Properties.IsOrdered.eq(1))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    //根据账单id获取除去赠菜和退菜后的已下单商品
    public ArrayList<OrderDishEntity> queryOrderedDish1(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.DishId.notEq("voucherdish"), OrderDishEntityDao.Properties.IsOrdered.eq(1))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    //根据账单id获取除去赠菜和退菜后的已下单商品
    public ArrayList<OrderDishEntity> queryOrderedDish(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsOrdered.eq(1))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    /**
     * 获取下单打印商品
     *
     * @param orderId
     * @param isPresent
     * @param isRetreat
     * @return
     */
    public ArrayList<PrintDishBean> queryPrintDish(String orderId, boolean isPresent, boolean isRetreat) {
        ArrayList<PrintDishBean> printDishBeenes = new ArrayList<>();
        DiscountHistoryEntity discountHistoryEntity = getDiscount(orderId);
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            if (isPresent && isRetreat) {//赠菜和退菜都打印
                orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(1), OrderDishEntityDao.Properties.IsOrdered.eq(-1), OrderDishEntityDao.Properties.IsOrdered.eq(-2)))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list());
            } else if (isPresent) {//打印赠菜，不打印退菜
                orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(1), OrderDishEntityDao.Properties.IsOrdered.eq(-2)))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list());
            } else if (isRetreat) {//打印退菜，不打印赠菜
                orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(1), OrderDishEntityDao.Properties.IsOrdered.eq(-1)))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list());
            }
            double unitPrice = -1;
            int discountRate[] = {100, 100};
            String dishPriceStr = "";
            double dishPrice = 0;
            double rate0 = 100;
            double rate1 = 100;
            double discountMoney = 0;
            String dishPriceStr1 = "";
            String unitName = "";
            DishEntity dishEntity = null;
            for (OrderDishEntity orderDish :
                    orderDishEntities) {
                unitPrice = -1;
                discountRate = getDishDiscountRate(orderEntity, orderDish, discountHistoryEntity);
                rate0 = AmountUtils.multiply(discountRate[0], 0.01);
                rate1 = AmountUtils.multiply(discountRate[1], 0.01);
                if (orderDish.getType() == 1) {
                    //套餐
                    dishPrice = AmountUtils.multiply1(getOrderedTaocanPrice(orderDish) + "", "" + orderDish.getDishCount());
                    discountMoney = AmountUtils.multiply(rate0, dishPrice, rate1);
                    TaocanEntity taocanEntity = queryTaocanById(orderDish.getDishId());
                    if (taocanEntity != null) {
                        unitPrice = AmountUtils.multiply1(taocanEntity.getTaocanPrice() + "", "1.0");
                    } else {
                        unitPrice = -1;
                    }
                    PrintDishBean printDishBean = new PrintDishBean(orderDish, "份", unitPrice, discountMoney, discountRate);
                    printDishBeenes.add(printDishBean);
                    ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                    orderTaocanGroupDishEntities.addAll(getOrderedTaocanDish(orderDish));
                    for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                            orderTaocanGroupDishEntities) {
                        dishEntity = queryOneDishEntity(orderTaocanGroupDish.getDishId());
                        if (dishEntity != null) {
                            unitName = dishEntity.getCheckOutUnit();
                        } else {
                            unitName = "";
                        }
                        PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish, printDishBean, unitName);
                        printDishBeenes.add(printDishBean1);
                    }
                } else {
                    //非套餐
                    dishPrice = AmountUtils.multiply1(orderDish.getDishPrice() + "", "1.0");
                    discountMoney = AmountUtils.multiply(rate0, dishPrice, rate1);
                    dishEntity = queryOneDishEntity(orderDish.getDishId());
                    if (dishEntity != null) {
                        unitPrice = AmountUtils.multiply1(dishEntity.getDishPrice() + "", "1.0");
                        unitName = dishEntity.getCheckOutUnit();
                    } else {
                        unitPrice = -1;
                        unitName = "份";
                    }
                    PrintDishBean printDishBean = new PrintDishBean(orderDish, unitName, unitPrice, discountMoney, discountRate);
                    printDishBeenes.add(printDishBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return printDishBeenes;
    }

    //根据账单id获取已下单的商品，去除退菜
    public ArrayList<OrderDishEntity> queryOrderedDish(String orderId, int status) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(status), OrderDishEntityDao.Properties.IsOrdered.eq(-2)))).orderDesc(OrderDishEntityDao.Properties.OrderedTime).list();
        return orderDishEntities;
    }

    //根据账单id获取账单详情
    public OrderEntity getOneOrderEntity(String orderId) {
        if (orderId == null) {
            return null;
        } else {
            try {
                QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
                return (OrderEntity) queryBuilder.where(OrderEntityDao.Properties.OrderId.eq(orderId)).build().unique();
            } catch (Exception e) {
                return null;
            }
        }
    }

    //根据orderDishId获取OrderDishEntity
    public OrderDishEntity queryOneOrderDishEntity(String orderDishId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        return (OrderDishEntity) queryBuilder.where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishId)).build().unique();
    }

    //根据商品id获取商品详情
    public DishEntity queryOneDishEntity(String dishId) {
        if (dishId == null) {
            return null;
        } else {
            QueryBuilder queryBuilder = mDishEntityDao.queryBuilder();
            DishEntity dishEntity = (DishEntity) queryBuilder.where(DishEntityDao.Properties.DishId.eq(dishId)).build().unique();
            return dishEntity;
        }
    }


    //判断商品是否有规格
    public boolean isDishHasGuige(String dishId) {
        boolean result = false;
        try {
            QueryBuilder queryBuilder = mDishSpecifyEntityDao.queryBuilder();
            ArrayList<DishSpecifyEntity> dishSpecifyEntity = (ArrayList<DishSpecifyEntity>) queryBuilder.where(DishSpecifyEntityDao.Properties.DishId.eq(dishId)).list();
            result = dishSpecifyEntity.size() > 0;
        } catch (Exception e) {

        }
        return result;
    }

    //判断商品是否有做法
    public boolean isDishHasZuofa(String dishId) {
        boolean result = false;
        try {
            QueryBuilder queryBuilder = mDishPracticeEntityDao.queryBuilder();
            ArrayList<DishPracticeEntity> dishPracticeEntity = (ArrayList<DishPracticeEntity>) queryBuilder.where(DishPracticeEntityDao.Properties.DishId.eq(dishId)).list();
            result = dishPracticeEntity.size() > 0;
        } catch (Exception e) {

        }
        return result;
    }

    //根据做法id获取做法名称
    public String getPracticeName(String practiceId) {
        String result = "";
        try {
            QueryBuilder queryBuilder = mPracticeEntityDao.queryBuilder();
            ArrayList<PracticeEntity> practiceEntity = (ArrayList<PracticeEntity>) queryBuilder.where(PracticeEntityDao.Properties.PracticeId.eq(practiceId)).list();
            result = practiceEntity.get(0).getPracticeName();
        } catch (Exception e) {

        }
        return result;
    }

    //根据规格id获取规格名称
    public String getSpecifyName(String specifyId) {
        if (specifyId != null) {
            QueryBuilder queryBuilder = mSpecifyEntityDao.queryBuilder();
            SpecifyEntity specifyEntity = (SpecifyEntity) queryBuilder.where(SpecifyEntityDao.Properties.SpecifyId.eq(specifyId)).build().unique();
            if (specifyEntity != null)
                return specifyEntity.getSpecifyName();
            else
                return "";
        } else {
            return "";
        }
    }

    //修改桌位状态
    public void replaceTableStatus(String tableId, int status) {
        try {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
            TableEntity tableEntity = (TableEntity) queryBuilder.where(TableEntityDao.Properties.TableId.eq(tableId)).build().unique();
            tableEntity.setTableStatus(status);
            mTableEntityDao.insertOrReplace(tableEntity);
        } catch (Exception e) {

        }
    }

    /**
     * 更新所有桌位状态
     */
    public void updateTableStatus() {
        try {
            ArrayList<TableEntity> tableEntities = (ArrayList<TableEntity>) mTableEntityDao.queryBuilder().list();
            for (TableEntity tableEntity :
                    tableEntities) {
                updateTableStatus(tableEntity.getTableId());
            }
        } catch (Exception e) {

        }
    }

    //更新桌位状态为清台
    public long changeToChingTable(String tableId) {
        try {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
            TableEntity tableEntity = (TableEntity) queryBuilder.where(TableEntityDao.Properties.TableId.eq(tableId)).build().unique();
            if (queryOrderData(tableId, 0, 0).size() > 0) {
                //有开台的账单
                if (tableEntity.getTableStatus() != 1)
                    tableEntity.setTableStatus(1);
            } else {
                //空桌
                tableEntity.setTableStatus(3);
            }
            return mTableEntityDao.insertOrReplace(tableEntity);
        } catch (Exception e) {
            return 0;
        }
    }

    //更新桌位状态
    public long updateTableStatus(String tableId) {
        try {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
            TableEntity tableEntity = (TableEntity) queryBuilder.where(TableEntityDao.Properties.TableId.eq(tableId)).build().unique();
            if (queryOrderData(tableId, 0, 0).size() > 0) {
                //有开台的账单
                if (tableEntity != null && tableEntity.getTableStatus() != 1)
                    tableEntity.setTableStatus(1);
            } else {
                //空桌
                if (tableEntity != null && tableEntity.getTableStatus() != 0)
                    tableEntity.setTableStatus(0);
            }
            return mTableEntityDao.insertOrReplace(tableEntity);
        } catch (Exception e) {
            return 0;
        }
    }

    //根据账单id删除账单
    public void dropOneOrder(String orderId) {
        try {
            QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
            queryBuilder.where(OrderEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        } catch (Exception e) {

        }
    }

    //查看所有账单，将账单对应的桌位状态修改为使用中
    public void queryAllOrderData() {
        ArrayList<TableEntity> tableEntities = new ArrayList<>();
        tableEntities.addAll(mTableEntityDao.queryBuilder().list());
        for (TableEntity tableEntity :
                tableEntities) {
            updateTableStatus(tableEntity.getTableId());
        }
    }

    //更新桌位
    public long updateTableStatus(String tableId, int status) {
        QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
        ArrayList<TableEntity> tableEntities = new ArrayList<TableEntity>();
        tableEntities.addAll(queryBuilder.where(TableEntityDao.Properties.TableId.eq(tableId)).list());
        if (tableEntities.size() > 0) {
            TableEntity tableEntity = tableEntities.get(0);
            tableEntity.setTableStatus(status);
            return mTableEntityDao.insertOrReplace(tableEntity);
        } else {
            return -1;
        }
    }

    //获取各种状态下桌位的数量
    public int[] getTableStatusCount(String areaId) {
        int[] statusCounts = {0, 0, 0, 0, 0};
        try {
            QueryBuilder queryBuilder1 = mTableEntityDao.queryBuilder().where(TableEntityDao.Properties.AreaId.eq(areaId));
            //获取areaId区域桌位总数
            statusCounts[0] = (int) queryBuilder1.buildCount().count();
            //获取空闲桌位总数
            QueryBuilder queryBuilder2 = mTableEntityDao.queryBuilder().where(TableEntityDao.Properties.AreaId.eq(areaId));
            statusCounts[1] = (int) queryBuilder2.where(TableEntityDao.Properties.TableStatus.eq(0)).buildCount().count();
            //获取使用中的桌位总数
            QueryBuilder queryBuilder3 = mTableEntityDao.queryBuilder().where(TableEntityDao.Properties.AreaId.eq(areaId));
            statusCounts[2] = (int) queryBuilder3.where(TableEntityDao.Properties.TableStatus.eq(1)).buildCount().count();
            //获取预定桌位总数
            statusCounts[3] = 0;
            QueryBuilder queryBuilder4 = mTableEntityDao.queryBuilder().where(TableEntityDao.Properties.AreaId.eq(areaId));
            ArrayList<TableEntity> tableEntities = new ArrayList<>();
            tableEntities.addAll(queryBuilder4.list());
            for (TableEntity tableEntity :
                    tableEntities) {
                if (queryOrderData(tableEntity.getTableId(), 0, 2).size() > 0) {
                    statusCounts[3] += 1;
                }
            }
            //获取已结账桌位总数
            QueryBuilder queryBuilder5 = mTableEntityDao.queryBuilder().where(TableEntityDao.Properties.AreaId.eq(areaId));
            statusCounts[4] = (int) queryBuilder5.where(TableEntityDao.Properties.TableStatus.eq(3)).buildCount().count();
        } catch (Exception e) {

        }
        return statusCounts;
    }

    /**
     * 获取代金券凑整金额
     *
     * @param orderId
     * @return
     */
    public double getVoucherDishPrice(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        OrderDishEntity voucherDish = (OrderDishEntity) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.DishId.eq("voucherdish"))).build().unique();
        if (voucherDish == null) {
            return 0;
        } else {
            return AmountUtils.multiply1(voucherDish.getDishPrice() + "", "1.0");
        }
    }

    /**
     * 删除代金券凑整商品
     *
     * @param orderId
     */
    public void deleteVoucherDish(String orderId) {
        try {
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.DishId.eq("voucherdish"))).buildDelete().executeDeleteWithoutDetachingEntities();
        } catch (Exception e) {

        }
    }

    /**
     * 添加代金券凑整商品或修改单价
     *
     * @param orderId
     * @param dishPrice
     * @return
     */
    public OrderDishEntity insertVoucherDish(String orderId, double dishPrice) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        OrderDishEntity orderDishEntity = (OrderDishEntity) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.DishId.eq("voucherdish"))).build().unique();
        if (orderDishEntity == null) {
            orderDishEntity = new OrderDishEntity();
            orderDishEntity.setOrderDishId(UUID.randomUUID().toString());
            orderDishEntity.setDishId("voucherdish");
            orderDishEntity.setDishName("代金券凑整");
            orderDishEntity.setOrderId(orderId);
            orderDishEntity.setDishCount(1.0);
            orderDishEntity.setDishPrice(Float.parseFloat(dishPrice + ""));
            orderDishEntity.setIsOrdered(1);
            orderDishEntity.setOrderedTime(System.currentTimeMillis());
            orderDishEntity.setIsAbleDiscount(0);
            orderDishEntity.setType(0);
            orderDishEntity.setIsFromWX(0);
            orderDishEntity.setDishTypeId("voucherdishparent");
            orderDishEntity.setDishTypeName("代金券凑整");
            orderDishEntity.setIsPresent(0);
            orderDishEntity.setIsRetreat(0);
        } else {
            orderDishEntity.setDishPrice(Float.parseFloat(dishPrice + ""));
        }
        mOrderDishEntityDao.insertOrReplace(orderDishEntity);
        return orderDishEntity;
    }

    //添加菜品到OrderDishEntity，没有做法和规格
    public OrderDishEntity insertSnackNewDish(String orderId, String dishId) {
        QueryBuilder queryBuilder = mDishEntityDao.queryBuilder().where(DishEntityDao.Properties.DishId.eq(dishId));
        DishEntity dishEntity = ((ArrayList<DishEntity>) queryBuilder.list()).get(0);
        OrderDishEntity orderDishEntity = new OrderDishEntity();
        orderDishEntity.setOrderDishId(UUID.randomUUID().toString());
        orderDishEntity.setDishId(dishEntity.getDishId());
        orderDishEntity.setDishName(dishEntity.getDishName());
        orderDishEntity.setOrderId(orderId);
        orderDishEntity.setDishCount(1.0);
        orderDishEntity.setDishPrice(dishEntity.getDishPrice());
        orderDishEntity.setIsOrdered(1);
        orderDishEntity.setIsPrint(0);
        orderDishEntity.setOrderedTime(System.currentTimeMillis());
        orderDishEntity.setIsAbleDiscount(dishEntity.getIsAbleDiscount());
        orderDishEntity.setType(0);
        orderDishEntity.setIsFromWX(0);
        DishTypeEntity dishType = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
        orderDishEntity.setDishTypeId(dishType.getDishTypeId());
        orderDishEntity.setDishTypeName(dishType.getDishTypeName());
        orderDishEntity.setIsPresent(dishEntity.getIsAblePresent());
        orderDishEntity.setIsRetreat(dishEntity.getIsAbleRetreat());
        mOrderDishEntityDao.insertOrReplace(orderDishEntity);
        return orderDishEntity;
    }

    //添加菜品到OrderDishEntity，没有做法和规格
    public OrderDishEntity insertNewDish(String orderId, String dishId) {
        QueryBuilder queryBuilder = mDishEntityDao.queryBuilder().where(DishEntityDao.Properties.DishId.eq(dishId));
        DishEntity dishEntity = ((ArrayList<DishEntity>) queryBuilder.list()).get(0);
        OrderDishEntity orderDishEntity = new OrderDishEntity();
        orderDishEntity.setOrderDishId(UUID.randomUUID().toString());
        orderDishEntity.setDishId(dishEntity.getDishId());
        orderDishEntity.setDishName(dishEntity.getDishName());
        orderDishEntity.setOrderId(orderId);
        orderDishEntity.setDishCount(1.0);
        orderDishEntity.setDishPrice(dishEntity.getDishPrice());
        orderDishEntity.setIsOrdered(0);
        orderDishEntity.setOrderedTime(System.currentTimeMillis());
        orderDishEntity.setIsAbleDiscount(dishEntity.getIsAbleDiscount());
        orderDishEntity.setType(0);
        orderDishEntity.setIsFromWX(0);
        DishTypeEntity dishType = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
        orderDishEntity.setDishTypeId(dishType.getDishTypeId());
        orderDishEntity.setDishTypeName(dishType.getDishTypeName());
        orderDishEntity.setIsPresent(dishEntity.getIsAblePresent());
        orderDishEntity.setIsRetreat(dishEntity.getIsAbleRetreat());
        mOrderDishEntityDao.insertOrReplace(orderDishEntity);
        return orderDishEntity;
    }

    /**
     * 获取所有外卖菜品
     *
     * @param orderId
     * @return
     */
    public ArrayList<PrintDishBean> getAllPrintDish(String orderId) {
        ArrayList<PrintDishBean> printDishes = new ArrayList<>();
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        orderDishEntities.addAll(queryOrderDishEntity(orderId, 1));
        String unitName;
        DishEntity dishEntity;
        for (int i = 0; i < orderDishEntities.size(); i++) {
            OrderDishEntity orderDishEntity = orderDishEntities.get(i);
            if (orderDishEntity.getType() == 0) {
                dishEntity = queryOneDishEntity(orderDishEntity.getDishId());
                if (dishEntity != null) {
                    unitName = dishEntity.getCheckOutUnit();
                } else {
                    unitName = "份";
                }
                PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, unitName, 0, orderDishEntity.getDishPrice(), new int[]{100, 100},getSelectedDishMaterial(orderDishEntity.getOrderDishId()));
                printDishes.add(printDishBean);
            }
        }
        return printDishes;
    }

    //下单
    public ArrayList<PrintDishBean> orderDish(String orderId) {
        ArrayList<PrintDishBean> printDishes = new ArrayList<>();
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        //合并相同的未下单的菜品，套餐除外
        ArrayList<OrderDishEntity> orderDishEntities1 = new ArrayList<>();
        orderDishEntities1.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.Type.eq(0), OrderDishEntityDao.Properties.IsOrdered.eq(0))).list());
        ArrayList<OrderDishEntity> joinResults = new ArrayList<>();
        boolean b;
        for (OrderDishEntity orderDishEntity :
                orderDishEntities1) {
            if (orderDishEntity.getType() == 0) {
                b = false;
                for (OrderDishEntity orderDish :
                        joinResults) {
                    //如果菜品做法规格备注菜品id均相同则认为是同一个菜，需要进行合并数量
                    if (orderDishEntity.getDishId().equals(orderDish.getDishId()) &&
                            ((orderDishEntity.getDishNote() == null && orderDish.getDishNote() == null) || (orderDishEntity.getDishNote() != null && orderDish.getDishNote() != null && orderDishEntity.getDishNote().equals(orderDish.getDishNote()))) &&
                            ((orderDishEntity.getSpecifyId() == null && orderDish.getSpecifyId() == null) || (orderDishEntity.getSpecifyId() != null && orderDish.getSpecifyId() != null && orderDishEntity.getSpecifyId().equals(orderDish.getSpecifyId()))) &&
                            ((orderDishEntity.getPracticeId() == null && orderDish.getPracticeId() == null) || (orderDishEntity.getPracticeId() != null && orderDish.getPracticeId() != null && orderDishEntity.getPracticeId().equals(orderDish.getPracticeId())))) {
                        b = true;
                        orderDish.setDishCount(orderDish.getDishCount() + orderDishEntity.getDishCount());
                        orderDish.setDishPrice(getOrderDishPrice(orderDish.getDishId(), orderDish.getPracticeId(), orderDish.getSpecifyId(), orderDish.getDishCount(), orderDish.getOrderDishId()));
                    }
                }
                if (!b) {
                    joinResults.add(orderDishEntity);
                }
            }
        }

        //合并完成后删除所有原有菜品
        for (OrderDishEntity orderDishEntity :
                orderDishEntities1) {
            mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
        }

        //将合并后的菜品添加到数据库
        for (OrderDishEntity orderDish :
                joinResults) {
            mOrderDishEntityDao.insertOrReplace(orderDish);
        }

        QueryBuilder queryBuilder1 = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder1.where(queryBuilder1.or(queryBuilder1.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.Type.eq(0), OrderDishEntityDao.Properties.IsOrdered.eq(0)), queryBuilder1.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.Type.eq(1)))).list();
        OrderDishEntity orderDishEntity;
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        String unitName = "";
        DishEntity dishEntity;
        double unitPrice = 0;
        for (int i = 0; i < orderDishEntities.size(); i++) {
            orderDishEntity = orderDishEntities.get(i);
            if (orderEntity != null && orderEntity.getIsUpload() == 1 && orderDishEntity.getIsFromWX() != null && orderDishEntity.getIsFromWX() == 0 && orderDishEntity.getIsOrdered() == 0) {
                insertUploadData(orderId, orderDishEntity.getOrderDishId(), 0);
            }
            if (orderDishEntity.getType() == 1) {
                if (orderDishEntity.getIsOrdered() != -1 && orderDishEntity.getIsOrdered() != -2) {
                    //套餐
                    PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, "份", getOrderedTaocanPrice(orderDishEntity), AmountUtils.multiply1(getOrderedTaocanPrice(orderDishEntity) + "", orderDishEntity.getDishCount() + ""), new int[]{100, 100});
                    orderTaocanGroupDishEntities.clear();
                    orderTaocanGroupDishEntities.addAll(getUnOrderedTaocanDish(orderDishEntity));
                    if (orderTaocanGroupDishEntities.size() > 0) {
                        printDishes.add(printDishBean);
                        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                orderTaocanGroupDishEntities) {
                            if (orderTaocanGroupDish.getStatus() != 1) {
                                unitName = "";
                                dishEntity = queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                if (dishEntity != null) {
                                    unitName = dishEntity.getCheckOutUnit();
                                }
                                PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish, printDishBean, unitName);
                                printDishes.add(printDishBean1);
                                orderTaocanGroupDish.setStatus(1);
                                mOrderTaocanGroupDishEntityDao.insertOrReplace(orderTaocanGroupDish);
                                //落单时修改库存
                                changeStock(orderTaocanGroupDish.getDishId(), orderTaocanGroupDish.getTaocanGroupDishCount());
                            }
                        }
                    }
                    orderDishEntity.setDishPrice(getOrderedTaocanPrice(orderDishEntity));
                    orderDishEntity.setIsOrdered(1);
                    mOrderDishEntityDao.insertOrReplace(orderDishEntity);
                }
            } else {
                if (orderDishEntity.getIsOrdered() == 0) {
                    orderDishEntity.setIsOrdered(1);
                    unitName = "份";
                    dishEntity = queryOneDishEntity(orderDishEntity.getDishId());
                    if (dishEntity != null) {
                        unitPrice = AmountUtils.multiply1("" + dishEntity.getDishPrice(), "1");
                        unitName = dishEntity.getCheckOutUnit();
                    }
                    PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, unitName, unitPrice, orderDishEntity.getDishPrice(), new int[]{100, 100},getSelectedDishMaterial(orderDishEntity.getOrderDishId()));
                    printDishes.add(printDishBean);
                    mOrderDishEntityDao.insertOrReplace(orderDishEntity);
                    changeStock(orderDishEntity.getDishId(), orderDishEntity.getDishCount());
                }
            }
        }
        if (orderEntity.getSerialNumber() == null) {
            orderEntity.setSerialNumber(getSerialNumber(getTableCodeByTableId(orderEntity.getTableId()), orderEntity.getOrderNumber1()));
            if (orderEntity.getIsUpload() == 1) {
                insertUploadData(orderId, orderId, 3);
                deleteWxOrderMessage(orderId, 0);
            }
        }
        mOrderEntityDao.insertOrReplace(orderEntity);
        insertUploadData(orderId, orderId, 13);
        return printDishes;
    }

    //下单
    public ArrayList<PrintDishBean> SnackOrderDish(String orderId) {
        ArrayList<PrintDishBean> printDishes = new ArrayList<>();
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        //合并相同的未下单的菜品，套餐除外
        ArrayList<OrderDishEntity> orderDishEntities1 = new ArrayList<>();
        orderDishEntities1.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.Type.eq(0), OrderDishEntityDao.Properties.IsPrint.eq(0))).list());
        ArrayList<OrderDishEntity> joinResults = new ArrayList<>();
        boolean b;
        for (OrderDishEntity orderDishEntity :
                orderDishEntities1) {
            if (orderDishEntity.getType() == 0) {
                b = false;
                for (OrderDishEntity orderDish :
                        joinResults) {
                    //如果菜品做法规格备注菜品id均相同则认为是同一个菜，需要进行合并数量
                    if (orderDishEntity.getDishId().equals(orderDish.getDishId()) &&
                            ((orderDishEntity.getDishNote() == null && orderDish.getDishNote() == null) || (orderDishEntity.getDishNote() != null && orderDish.getDishNote() != null && orderDishEntity.getDishNote().equals(orderDish.getDishNote()))) &&
                            ((orderDishEntity.getSpecifyId() == null && orderDish.getSpecifyId() == null) || (orderDishEntity.getSpecifyId() != null && orderDish.getSpecifyId() != null && orderDishEntity.getSpecifyId().equals(orderDish.getSpecifyId()))) &&
                            ((orderDishEntity.getPracticeId() == null && orderDish.getPracticeId() == null) || (orderDishEntity.getPracticeId() != null && orderDish.getPracticeId() != null && orderDishEntity.getPracticeId().equals(orderDish.getPracticeId())))) {
                        b = true;
                        orderDish.setDishCount(orderDish.getDishCount() + orderDishEntity.getDishCount());
                        orderDish.setDishPrice(getOrderDishPrice(orderDish.getDishId(), orderDish.getPracticeId(), orderDish.getSpecifyId(), orderDish.getDishCount(), orderDish.getOrderDishId()));
                    }
                }
                if (!b) {
                    joinResults.add(orderDishEntity);
                }
            }
        }

        //合并完成后删除所有原有菜品
        for (OrderDishEntity orderDishEntity :
                orderDishEntities1) {
            mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
        }

        //将合并后的菜品添加到数据库
        for (OrderDishEntity orderDish :
                joinResults) {
            mOrderDishEntityDao.insertOrReplace(orderDish);
        }

        QueryBuilder queryBuilder1 = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder1.where(queryBuilder1.or(queryBuilder1.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.Type.eq(0), OrderDishEntityDao.Properties.IsPrint.eq(0)), queryBuilder1.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.Type.eq(1)))).list();
        OrderDishEntity orderDishEntity;
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        String unitName = "";
        DishEntity dishEntity;
        double unitPrice = 0;
        for (int i = 0; i < orderDishEntities.size(); i++) {
            orderDishEntity = orderDishEntities.get(i);
            if (orderEntity != null && orderEntity.getIsUpload() == 1 && orderDishEntity.getIsFromWX() != null && orderDishEntity.getIsFromWX() == 0 && orderDishEntity.getIsPrint() != null && orderDishEntity.getIsPrint() == 0) {
                insertUploadData(orderId, orderDishEntity.getOrderDishId(), 0);
            }
            if (orderDishEntity.getType() == 1) {
                if (orderDishEntity.getIsPrint() != null && orderDishEntity.getIsPrint() == 0) {
                    orderDishEntity.setIsPrint(1);
                    //套餐
                    PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, "份", getOrderedTaocanPrice(orderDishEntity), AmountUtils.multiply1(getOrderedTaocanPrice(orderDishEntity) + "", orderDishEntity.getDishCount() + ""), new int[]{100, 100});
                    orderTaocanGroupDishEntities.clear();
                    orderTaocanGroupDishEntities.addAll(getUnOrderedTaocanDish(orderDishEntity));
                    if (orderTaocanGroupDishEntities.size() > 0) {
                        printDishes.add(printDishBean);
                        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                orderTaocanGroupDishEntities) {
                            if (orderTaocanGroupDish.getStatus() != 1) {
                                orderTaocanGroupDish.setIsPrint(1);
                                unitName = "";
                                dishEntity = queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                if (dishEntity != null) {
                                    unitName = dishEntity.getCheckOutUnit();
                                }
                                PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish, printDishBean, unitName);
                                printDishes.add(printDishBean1);
                                orderTaocanGroupDish.setStatus(1);
                                mOrderTaocanGroupDishEntityDao.insertOrReplace(orderTaocanGroupDish);
                                //落单时修改库存
                                changeStock(orderTaocanGroupDish.getDishId(), orderTaocanGroupDish.getTaocanGroupDishCount());
                            }
                        }
                    }
                    orderDishEntity.setDishPrice(getOrderedTaocanPrice(orderDishEntity));
                    orderDishEntity.setIsOrdered(1);
                    mOrderDishEntityDao.insertOrReplace(orderDishEntity);
                }
            } else {
                if (orderDishEntity.getIsPrint() != null && orderDishEntity.getIsPrint() == 0) {
                    orderDishEntity.setIsPrint(1);
                    unitName = "份";
                    dishEntity = queryOneDishEntity(orderDishEntity.getDishId());
                    if (dishEntity != null) {
                        unitPrice = AmountUtils.multiply1("" + dishEntity.getDishPrice(), "1");
                        unitName = dishEntity.getCheckOutUnit();
                    }
                    PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, unitName, unitPrice, orderDishEntity.getDishPrice(), new int[]{100, 100}, getSelectedDishMaterial(orderDishEntity.getOrderDishId()));
                    printDishes.add(printDishBean);
                    mOrderDishEntityDao.insertOrReplace(orderDishEntity);
                    changeStock(orderDishEntity.getDishId(), orderDishEntity.getDishCount());
                }
            }
        }
//        if (orderEntity.getSerialNumber() == null) {
//            orderEntity.setSerialNumber(getSerialNumber(null, orderEntity.getOrderNumber1()));
        if (orderEntity.getIsUpload() == 1) {
            Log.d("###", "微信点餐加菜");
            insertUploadData(orderId, orderId, 3);
            deleteWxOrderMessage(orderId, 0);
        }
//        }
        mOrderEntityDao.insertOrReplace(orderEntity);
        insertUploadData(orderId, orderId, 13);
        return printDishes;
    }

    public void changeStock(String dishId, double count) {
        QueryBuilder queryBuilder = mSellCheckEntityDao.queryBuilder();
        SellCheckEntity sellCheckEntity = (SellCheckEntity) queryBuilder.where(queryBuilder.and(SellCheckEntityDao.Properties.DishId.eq(dishId), SellCheckEntityDao.Properties.IsSellOut.eq(0))).build().unique();
        if (sellCheckEntity != null) {
            sellCheckEntity.setStock(sellCheckEntity.getStock() - count);
            mSellCheckEntityDao.insertOrReplace(sellCheckEntity);
        }
    }

    /**
     * 判断是否在该厨打设备打印
     *
     * @param printKitchenEntity
     * @param printDishBean
     * @return
     */
    public boolean isPrint(PrintKitchenEntity printKitchenEntity, PrintDishBean printDishBean) {
        QueryBuilder queryBuilder = mPrintKitchenClassifyEntityDao.queryBuilder();
        int count = 0;
        if (printDishBean.getDishTypeId() != null) {
            count = (int) queryBuilder.where(queryBuilder.and(PrintKitchenClassifyEntityDao.Properties.DishTypeId.eq(printDishBean.getDishTypeId()), PrintKitchenClassifyEntityDao.Properties.PrintKitchenId.eq(printKitchenEntity.getPrintKitchenId()))).buildCount().count();
        }
        if (count > 0) {
            return true;
        } else {
            QueryBuilder queryBuilder1 = mPrintKitchenDishEntityDao.queryBuilder();
            return queryBuilder1.where(queryBuilder1.and(PrintKitchenDishEntityDao.Properties.DishId.eq(printDishBean.getDishId()), PrintKitchenDishEntityDao.Properties.PrintKitchenId.eq(printKitchenEntity.getPrintKitchenId()))).buildCount().count() > 0;
        }
    }

    /**
     * 判断前台是否打印该商品
     *
     * @param printDishBean
     * @return
     */
    public boolean isPrint(PrintDishBean printDishBean) {
        QueryBuilder queryBuilder = mCashierClassifyEntityDao.queryBuilder();
        int count = 0;
        if (printDishBean.getDishTypeId() != null) {
            count = (int) queryBuilder.where(CashierClassifyEntityDao.Properties.DishTypeId.eq(printDishBean.getDishTypeId())).buildCount().count();
        }
        if (count > 0) {
            return true;
        } else {
            QueryBuilder queryBuilder1 = mCashierDishEntityDao.queryBuilder();
            return printDishBean.getDishId() != null && queryBuilder1.where(CashierDishEntityDao.Properties.DishId.eq(printDishBean.getDishId())).buildCount().count() > 0;
        }
    }

    //获取桌位编码
    public String getTableCodeByTableId(String tableId) {
        if (tableId == null) {
            return null;
        } else {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder();
            TableEntity tableEntity = (TableEntity) queryBuilder.where(TableEntityDao.Properties.TableId.eq(tableId)).build().unique();
            if (tableEntity == null) {
                return null;
            } else {
                return tableEntity.getTableCode();
            }
        }
    }

    //添加有规格和做法的菜品到OrderDishEntity
    public OrderDishEntity insertNewDish(String orderId, String dishId, String practiceId, String specifyId, String note, double count, ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities) {
        QueryBuilder queryBuilder = mDishEntityDao.queryBuilder().where(DishEntityDao.Properties.DishId.eq(dishId));
        DishEntity dishEntity = ((ArrayList<DishEntity>) queryBuilder.list()).get(0);
        OrderDishEntity orderDishEntity = new OrderDishEntity();
        orderDishEntity.setOrderDishId(UUID.randomUUID().toString());
        orderDishEntity.setDishId(dishEntity.getDishId());
        orderDishEntity.setOrderId(orderId);
        orderDishEntity.setDishName(dishEntity.getDishName());
        orderDishEntity.setDishCount(count);
        orderDishEntity.setDishNote(note);
        //有做法或规格的商品价格需要重新计算，商品价格=商品做法加价+商品规格倍数*商品价格
        DishSpecifyEntity dishSpecify = getDishSpecifyById(specifyId);
        if (dishSpecify != null) {
            orderDishEntity.setDishSpecify(getSpecifyName(dishSpecify.getSpecifyId()));
        }
        try {
            String practiceName = "";
            DishPracticeEntity dishPractice = null;
            dishPractice = getDishPracticeById(practiceId);
            practiceName = dishPractice != null ? getPracticeName(dishPractice.getPracticeId()) : null;
            orderDishEntity.setDishPractice(TextUtils.isEmpty(practiceName) ? null : practiceName);
        } catch (Exception e) {
            orderDishEntity.setDishPractice(null);
        }
        insertDishSelectMaterial(orderDishEntity.getOrderDishId(), dishSelectedMaterialEntities);
        float dishPrice = getOrderDishPrice(dishId, practiceId, specifyId, count, orderDishEntity.getOrderDishId());
        orderDishEntity.setDishPrice(dishPrice);
        orderDishEntity.setIsOrdered(0);
        orderDishEntity.setOrderedTime(System.currentTimeMillis());
        orderDishEntity.setPracticeId(practiceId);
        orderDishEntity.setSpecifyId(specifyId);
        orderDishEntity.setIsAbleDiscount(dishEntity.getIsAbleDiscount());
        orderDishEntity.setType(0);
        orderDishEntity.setIsFromWX(0);
        DishTypeEntity dishType = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
        orderDishEntity.setDishTypeId(dishType.getDishTypeId());
        orderDishEntity.setDishTypeName(dishType.getDishTypeName());
        orderDishEntity.setIsPresent(dishEntity.getIsAblePresent());
        orderDishEntity.setIsRetreat(dishEntity.getIsAbleRetreat());
        mOrderDishEntityDao.insertOrReplace(orderDishEntity);
        return orderDishEntity;
    }

    //添加有规格和做法的菜品到OrderDishEntity
    public OrderDishEntity insertSnackNewDish(String orderId, String dishId, String practiceId, String specifyId, String note, double count, ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities) {
        QueryBuilder queryBuilder = mDishEntityDao.queryBuilder().where(DishEntityDao.Properties.DishId.eq(dishId));
        DishEntity dishEntity = ((ArrayList<DishEntity>) queryBuilder.list()).get(0);
        OrderDishEntity orderDishEntity = new OrderDishEntity();
        orderDishEntity.setOrderDishId(UUID.randomUUID().toString());
        orderDishEntity.setDishId(dishEntity.getDishId());
        orderDishEntity.setOrderId(orderId);
        orderDishEntity.setDishName(dishEntity.getDishName());
        orderDishEntity.setDishCount(count);
        Log.d("###", "备注2：" + note);
        orderDishEntity.setDishNote(note);
        //有做法或规格的商品价格需要重新计算，商品价格=商品做法加价+商品规格倍数*商品价格
        DishSpecifyEntity dishSpecify = getDishSpecifyById(specifyId);
        if (dishSpecify != null) {
            orderDishEntity.setDishSpecify(getSpecifyName(dishSpecify.getSpecifyId()));
        }
        try {
            String practiceName = "";
            DishPracticeEntity dishPractice = null;
            dishPractice = getDishPracticeById(practiceId);
            practiceName = dishPractice != null ? getPracticeName(dishPractice.getPracticeId()) : null;
            orderDishEntity.setDishPractice(TextUtils.isEmpty(practiceName) ? null : practiceName);
        } catch (Exception e) {
            orderDishEntity.setDishPractice(null);
        }
        insertDishSelectMaterial(orderDishEntity.getOrderDishId(), dishSelectedMaterialEntities);
        float dishPrice = getOrderDishPrice(dishId, practiceId, specifyId, count, orderDishEntity.getOrderDishId());
        orderDishEntity.setDishPrice(dishPrice);
        orderDishEntity.setIsOrdered(1);
        orderDishEntity.setIsPrint(0);
        orderDishEntity.setOrderedTime(System.currentTimeMillis());
        orderDishEntity.setPracticeId(practiceId);
        orderDishEntity.setSpecifyId(specifyId);
        orderDishEntity.setIsAbleDiscount(dishEntity.getIsAbleDiscount());
        orderDishEntity.setType(0);
        orderDishEntity.setIsFromWX(0);
        DishTypeEntity dishType = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
        orderDishEntity.setDishTypeId(dishType.getDishTypeId());
        orderDishEntity.setDishTypeName(dishType.getDishTypeName());
        orderDishEntity.setIsPresent(dishEntity.getIsAblePresent());
        orderDishEntity.setIsRetreat(dishEntity.getIsAbleRetreat());
        mOrderDishEntityDao.insertOrReplace(orderDishEntity);
        return orderDishEntity;
    }

    //添加有规格和做法的菜品到OrderDishEntity
    public long insertSnackShopOrderDish(OrderDetailModel orderDetailModel, String orderId) {
        OrderDishEntity orderDishEntity = new OrderDishEntity();
        orderDishEntity.setOrderDishId(orderDetailModel.getId());
        orderDishEntity.setDishId(orderDetailModel.getGoodsId());
        orderDishEntity.setOrderId(orderId);
        orderDishEntity.setDishName(orderDetailModel.getGoodsName());
        orderDishEntity.setDishCount(orderDetailModel.getNum());
        //有做法或规格的商品价格需要重新计算，商品价格=商品做法加价+商品规格倍数*商品价格
        orderDishEntity.setDishPractice(orderDetailModel.getMakeName());
        orderDishEntity.setDishSpecify(orderDetailModel.getGuigeName());
        ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities = new ArrayList<>();
        try {
            ArrayList<WxDishMaterialBean> wxDishMaterialBeens = new ArrayList<>();
            wxDishMaterialBeens.addAll(JSON.parseArray(orderDetailModel.getMaterial(), WxDishMaterialBean.class));
            if (wxDishMaterialBeens.size() > 0) {
                for (WxDishMaterialBean bean :
                        wxDishMaterialBeens) {
                    DishSelectedMaterialEntity dishSelectedMaterialEntity = new DishSelectedMaterialEntity();
                    dishSelectedMaterialEntity.setDishSelectedMaterialId(UUID.randomUUID().toString());
                    dishSelectedMaterialEntity.setDishTypeMaterialId(bean.getId());
                    dishSelectedMaterialEntity.setMaterialId(bean.getMaterial().getId());
                    dishSelectedMaterialEntity.setOrderDishId(orderDetailModel.getId());
                    dishSelectedMaterialEntity.setOrderId(orderId);
                    dishSelectedMaterialEntity.setTotalPrice(bean.getMaterial().getTotalPrice());
                    dishSelectedMaterialEntity.setMaterialPrice(bean.getMaterial().getPrice());
                    dishSelectedMaterialEntity.setSelectedCount(bean.getMaterial().getSelectedCount());
                    dishSelectedMaterialEntity.setMaterialName(bean.getMaterial().getName());
                    dishSelectedMaterialEntities.add(dishSelectedMaterialEntity);
                }
            }
        } catch (Exception e) {

        }
        insertDishSelectMaterial(orderDetailModel.getId(), dishSelectedMaterialEntities);
        orderDishEntity.setDishPrice(Float.parseFloat(((double) orderDetailModel.getGoodsPrice()) / 100 + ""));
        orderDishEntity.setIsOrdered(1);
        orderDishEntity.setIsPrint(0);
        orderDishEntity.setOrderedTime(System.currentTimeMillis());
        orderDishEntity.setPracticeId(orderDetailModel.getMakeId());
        orderDishEntity.setSpecifyId(orderDetailModel.getGuigeId());
        orderDishEntity.setIsAbleDiscount(orderDetailModel.getDiscount());
        orderDishEntity.setType(0);
        if (orderDetailModel.getTypeId() != null) {
            DishTypeEntity dishType = getDishTypeByDishTypeId(orderDetailModel.getTypeId());
            orderDishEntity.setDishTypeId(dishType.getDishTypeId());
            orderDishEntity.setDishTypeName(dishType.getDishTypeName());
        }
        DishEntity dishEntity = null;
        if (orderDetailModel.getGoodsId() != null && (dishEntity = queryOneDishEntity(orderDetailModel.getGoodsId())) != null) {
            orderDishEntity.setIsPresent(dishEntity.getIsAblePresent());
            orderDishEntity.setIsRetreat(dishEntity.getIsAbleRetreat());
        } else {
            orderDishEntity.setIsPresent(0);
            orderDishEntity.setIsRetreat(0);
        }
        orderDishEntity.setIsFromWX(1);
        return mOrderDishEntityDao.insertOrReplace(orderDishEntity);
    }

    //添加有规格和做法的菜品到OrderDishEntity
    public long insertShopOrderDish(OrderDetailModel orderDetailModel, String orderId) {
        OrderDishEntity orderDishEntity = new OrderDishEntity();
        orderDishEntity.setOrderDishId(orderDetailModel.getId());
        orderDishEntity.setDishId(orderDetailModel.getGoodsId());
        orderDishEntity.setOrderId(orderId);
        orderDishEntity.setDishName(orderDetailModel.getGoodsName());
        orderDishEntity.setDishCount(orderDetailModel.getNum());
        //有做法或规格的商品价格需要重新计算，商品价格=商品做法加价+商品规格倍数*商品价格
        orderDishEntity.setDishPractice(orderDetailModel.getMakeName());
        orderDishEntity.setDishSpecify(orderDetailModel.getGuigeName());
        orderDishEntity.setDishPrice(Float.parseFloat(((double) orderDetailModel.getGoodsPrice()) / 100 + ""));
        orderDishEntity.setIsOrdered(0);
        orderDishEntity.setOrderedTime(System.currentTimeMillis());
        orderDishEntity.setPracticeId(orderDetailModel.getMakeId());
        orderDishEntity.setSpecifyId(orderDetailModel.getGuigeId());
        orderDishEntity.setIsAbleDiscount(orderDetailModel.getDiscount());
        orderDishEntity.setType(0);
        if (orderDetailModel.getTypeId() != null) {
            DishTypeEntity dishType = getDishTypeByDishTypeId(orderDetailModel.getTypeId());
            orderDishEntity.setDishTypeId(dishType.getDishTypeId());
            orderDishEntity.setDishTypeName(dishType.getDishTypeName());
        }
        DishEntity dishEntity = null;
        if (orderDetailModel.getGoodsId() != null && (dishEntity = queryOneDishEntity(orderDetailModel.getGoodsId())) != null) {
            orderDishEntity.setIsPresent(dishEntity.getIsAblePresent());
            orderDishEntity.setIsRetreat(dishEntity.getIsAbleRetreat());
        } else {
            orderDishEntity.setIsPresent(0);
            orderDishEntity.setIsRetreat(0);
        }
        orderDishEntity.setIsFromWX(1);
        return mOrderDishEntityDao.insertOrReplace(orderDishEntity);
    }

//    /**
//     * 添加有规格和做法的菜品到OrderDishEntity
//     *
//     * @param orderId
//     * @param dishId
//     * @param practiceId
//     * @param specifyId
//     * @param orderDishId
//     * @return
//     */
//    public OrderDishEntity insertNewDish(String orderId, String dishId, String practiceId, String specifyId, String orderDishId, String note, double count) {
//        if (orderDishId != null) {
//            OrderDishEntity orderDishEntity = queryOneOrderDishEntity(orderDishId);
//            orderDishEntity.setPracticeId(practiceId);
//            orderDishEntity.setSpecifyId(specifyId);
//            orderDishEntity.setDishCount(count);
//            DishEntity dishEntity = queryOneDishEntity(dishId);
//            DishSpecifyEntity dishSpecify = getDishSpecifyById(specifyId);
//            if (dishSpecify != null) {
//                orderDishEntity.setDishSpecify(getSpecifyName(dishSpecify.getSpecifyId()));
//            }
//            try {
//                String practiceName = "";
//                DishPracticeEntity dishPractice = null;
//                dishPractice = getDishPracticeById(practiceId);
//                practiceName += dishPractice != null ? getPracticeName(dishPractice.getPracticeId()) : null;
//                orderDishEntity.setDishPractice(TextUtils.isEmpty(practiceName) ? null : practiceName);
//            } catch (Exception e) {
//                orderDishEntity.setDishPractice(null);
//            }
//            insertDishSelectMaterial(orderDishId,dishSelectedMaterialEntities);
//            float dishPrice = getOrderDishPrice(dishId, practiceId, specifyId, orderDishEntity.getDishCount(),orderDishId);
//            orderDishEntity.setDishPrice(dishPrice);
//            orderDishEntity.setType(0);
//            DishTypeEntity dishType = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
//            orderDishEntity.setDishTypeId(dishType.getDishTypeId());
//            orderDishEntity.setDishTypeName(dishType.getDishTypeName());
//            orderDishEntity.setDishNote(note);
//            mOrderDishEntityDao.insertOrReplace(orderDishEntity);
//            return orderDishEntity;
//        } else {
//            return insertNewDish(orderId, dishId, practiceId, specifyId, note, count,dishSelectedMaterialEntities);
//        }
//    }

    /**
     * 添加有规格和做法的菜品到OrderDishEntity
     *
     * @param orderId
     * @param dishId
     * @param practiceId
     * @param specifyId
     * @param orderDishId
     * @return
     */
    public OrderDishEntity insertNewDish(String orderId, String dishId, String practiceId, String specifyId, String orderDishId, String note, double count, ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities) {
        if (orderDishId != null) {
            OrderDishEntity orderDishEntity = queryOneOrderDishEntity(orderDishId);
            orderDishEntity.setPracticeId(practiceId);
            orderDishEntity.setSpecifyId(specifyId);
            orderDishEntity.setDishCount(count);
            DishEntity dishEntity = queryOneDishEntity(dishId);
            DishSpecifyEntity dishSpecify = getDishSpecifyById(specifyId);
            if (dishSpecify != null) {
                orderDishEntity.setDishSpecify(getSpecifyName(dishSpecify.getSpecifyId()));
            }
            try {
                String practiceName = "";
                DishPracticeEntity dishPractice = null;
                dishPractice = getDishPracticeById(practiceId);
                practiceName = dishPractice != null ? getPracticeName(dishPractice.getPracticeId()) : null;
                orderDishEntity.setDishPractice(TextUtils.isEmpty(practiceName) ? null : practiceName);
            } catch (Exception e) {
                orderDishEntity.setDishPractice(null);
            }
            insertDishSelectMaterial(orderDishId, dishSelectedMaterialEntities);
            float dishPrice = getOrderDishPrice(dishId, practiceId, specifyId, orderDishEntity.getDishCount(), orderDishId);
            orderDishEntity.setDishPrice(dishPrice);
            orderDishEntity.setType(0);
            DishTypeEntity dishType = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
            orderDishEntity.setDishTypeId(dishType.getDishTypeId());
            orderDishEntity.setDishTypeName(dishType.getDishTypeName());
            orderDishEntity.setDishNote(note);
            mOrderDishEntityDao.insertOrReplace(orderDishEntity);
            return orderDishEntity;
        } else {
            return insertNewDish(orderId, dishId, practiceId, specifyId, note, count, dishSelectedMaterialEntities);
        }
    }

    /**
     * 插入加料
     *
     * @param orderDishId
     * @param dishSelectedMaterialEntities
     */
    public void insertDishSelectMaterial(String orderDishId, ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities) {
        try {
            if (dishSelectedMaterialEntities == null) {
                throw new NullPointerException();
            }
            QueryBuilder queryBuilder = mDishSelectedMaterialEntityDao.queryBuilder();
            DishSelectedMaterialEntity dishSelectedMaterialEntity;
            for (DishSelectedMaterialEntity dishSelectMaterial :
                    dishSelectedMaterialEntities) {
                dishSelectedMaterialEntity = (DishSelectedMaterialEntity) queryBuilder.where(DishSelectedMaterialEntityDao.Properties.DishSelectedMaterialId.eq(dishSelectMaterial.getDishSelectedMaterialId())).build().unique();
                if (dishSelectedMaterialEntity != null) {//该加料已有记录
                    if (dishSelectMaterial.getSelectedCount() > 0) {//修改后数量大于0，进行更新
                        if (dishSelectMaterial.getOrderDishId() == null) {
                            dishSelectMaterial.setOrderDishId(orderDishId);
                        }
                        mDishSelectedMaterialEntityDao.insertOrReplace(dishSelectMaterial);
                    } else {//更新后数量小于等于0，删除该记录
                        queryBuilder.where(DishSelectedMaterialEntityDao.Properties.DishSelectedMaterialId.eq(dishSelectMaterial.getDishSelectedMaterialId())).buildDelete().executeDeleteWithoutDetachingEntities();
                    }
                } else {//该加料没有记录
                    if (dishSelectMaterial.getSelectedCount() > 0) {
                        if (dishSelectMaterial.getOrderDishId() == null) {
                            dishSelectMaterial.setOrderDishId(orderDishId);
                        }
                        mDishSelectedMaterialEntityDao.insertOrReplace(dishSelectMaterial);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 添加有规格和做法的菜品到OrderDishEntity
     *
     * @param orderId
     * @param dishId
     * @param practiceId
     * @param specifyId
     * @param orderDishId
     * @return
     */
    public OrderDishEntity insertSnackNewDish(String orderId, String dishId, String practiceId, String specifyId, String orderDishId, String note, double count, ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities) {
        if (orderDishId != null) {
            OrderDishEntity orderDishEntity = queryOneOrderDishEntity(orderDishId);
            orderDishEntity.setPracticeId(practiceId);
            orderDishEntity.setSpecifyId(specifyId);
            orderDishEntity.setDishCount(count);
            DishEntity dishEntity = queryOneDishEntity(dishId);
            DishSpecifyEntity dishSpecify = getDishSpecifyById(specifyId);
            if (dishSpecify != null) {
                orderDishEntity.setDishSpecify(getSpecifyName(dishSpecify.getSpecifyId()));
            }
            try {
                String practiceName = "";
                DishPracticeEntity dishPractice = null;
                dishPractice = getDishPracticeById(practiceId);
                practiceName = dishPractice != null ? getPracticeName(dishPractice.getPracticeId()) : null;
                orderDishEntity.setDishPractice(TextUtils.isEmpty(practiceName) ? null : practiceName);
            } catch (Exception e) {
                orderDishEntity.setDishPractice(null);
            }
            insertDishSelectMaterial(orderDishId, dishSelectedMaterialEntities);
            float dishPrice = getOrderDishPrice(dishId, practiceId, specifyId, orderDishEntity.getDishCount(), orderDishId);
            orderDishEntity.setDishPrice(dishPrice);
            orderDishEntity.setType(0);
            DishTypeEntity dishType = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
            orderDishEntity.setDishTypeId(dishType.getDishTypeId());
            orderDishEntity.setDishTypeName(dishType.getDishTypeName());
            orderDishEntity.setDishNote(note);
            mOrderDishEntityDao.insertOrReplace(orderDishEntity);
            return orderDishEntity;
        } else {
            return insertSnackNewDish(orderId, dishId, practiceId, specifyId, note, count, dishSelectedMaterialEntities);
        }
    }

    /**
     * 计算菜品金额
     *
     * @param dishId
     * @param practiceId
     * @param specifyId
     * @param count
     * @return
     */
    public float getOrderDishPrice(String dishId, String practiceId, String specifyId, double count, String orderDishId) {
        if (count > 0) {
            DishEntity dishEntity = queryOneDishEntity(dishId);
            if (dishEntity == null) {
                return 0;
            }
            double dishPrice = AmountUtils.multiply1("" + dishEntity.getDishPrice(), "1");
            if (specifyId != null) {
                DishSpecifyEntity dishSpecify = getDishSpecifyById(specifyId);
                if (dishSpecify != null) {
                    if (dishSpecify.getCustomPrice() == 0 && dishSpecify.getDefaultPrice() == 0) {
                        dishPrice = AmountUtils.multiply(dishPrice, getIncreaseSpecifyPrice(dishSpecify.getSpecifyId()));
                    } else {
                        dishPrice = dishSpecify.getCustomPrice();
                    }
                }
            }
            dishPrice = AmountUtils.multiply(dishPrice, count);
            if (practiceId != null) {
                try {
                    DishPracticeEntity dishPractice = null;
                    dishPractice = getDishPracticeById(practiceId);
                    switch (dishPractice.getIncreaseMode()) {
                        case 0://不加价

                            break;
                        case 1://一次性加价
                            dishPrice = AmountUtils.add(dishPrice, dishPractice.getIncreaseValue());
                            break;
                        case 2://每购买单位加价
                            dishPrice = AmountUtils.add(dishPrice, dishPractice.getIncreaseValue() * count);
                            break;
                        case 3://每结账单位加价
                            dishPrice = AmountUtils.add(dishPrice, dishPractice.getIncreaseValue() * count);
                            break;
                    }
                } catch (Exception e) {

                }
            }
            if (orderDishId != null) {
                ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities = new ArrayList<>();
                dishSelectedMaterialEntities.addAll(getSelectedDishMaterial(orderDishId));
                for (DishSelectedMaterialEntity dishSelectedMaterial :
                        dishSelectedMaterialEntities) {
                    dishPrice = AmountUtils.add(dishPrice, AmountUtils.multiply(count, AmountUtils.multiply(0.01, dishSelectedMaterial.getTotalPrice())));
                }
            }
            return Float.parseFloat("" + dishPrice);
        } else {
            return 0;
        }
    }

    //根据orderDishId删除菜品
    public void deleteOrderDishById(String orderDishId) {
        if (orderDishId != null) {
            try {
                mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishId)).buildDelete().executeDeleteWithoutDetachingEntities();
            } catch (Exception e) {

            }
        }
    }

    //获取商品做法的增加价格
    public DishPracticeEntity getDishPractice(String dishId, String practiceId) {
        QueryBuilder queryBuilder = mDishPracticeEntityDao.queryBuilder();
        DishPracticeEntity dishPracticeEntity = (DishPracticeEntity) queryBuilder.where(queryBuilder.and(DishPracticeEntityDao.Properties.DishId.eq(dishId), DishPracticeEntityDao.Properties.PracticeId.eq(practiceId))).build().unique();
        return dishPracticeEntity;
    }

    //获取商品规格的价格倍数
    public Float getIncreaseSpecifyPrice(String specifyId) {
        QueryBuilder queryBuilder = mSpecifyEntityDao.queryBuilder();
        SpecifyEntity specifyEntity = (SpecifyEntity) queryBuilder.where(SpecifyEntityDao.Properties.SpecifyId.eq(specifyId)).build().unique();
        return specifyEntity.getPriceMultiple();
    }

    //获取商品对应的所有做法
    public ArrayList<DishPracticeEntity> queryAllPractice(String dishId) {
        QueryBuilder queryBuilder = mDishPracticeEntityDao.queryBuilder().where(DishPracticeEntityDao.Properties.DishId.eq(dishId));
        return (ArrayList<DishPracticeEntity>) queryBuilder.list();
    }

    //获取商品对应的所有做法
    public ArrayList<DishSpecifyEntity> queryAllSpecify(String dishId) {
        ArrayList<DishSpecifyEntity> results = new ArrayList<>();
        QueryBuilder queryBuilder = mDishSpecifyEntityDao.queryBuilder().where(DishSpecifyEntityDao.Properties.DishId.eq(dishId));
        results.addAll(queryBuilder.list());
        return results;
    }

    //通过做法id获取做法详情
    public ArrayList<PracticeEntity> getPracticeById(String practiceId) {
        ArrayList<PracticeEntity> practiceEntities = new ArrayList<>();
        QueryBuilder queryBuilder = mPracticeEntityDao.queryBuilder().where(PracticeEntityDao.Properties.PracticeId.eq(practiceId));
        practiceEntities.addAll(queryBuilder.list());
        return practiceEntities;
    }

    //通过规格id获取做法详情
    public SpecifyEntity getSpecifyeById(String specifyId) {
        return mSpecifyEntityDao.queryBuilder().where(SpecifyEntityDao.Properties.SpecifyId.eq(specifyId)).build().unique();
    }

    //通过账单id获取菜品数量
    public double getDishCountByOrderId(String orderId) {
        ArrayList<OrderDishEntity> orderDishEntities = queryOrderDishEntity(orderId);
        double count = 0;
        for (OrderDishEntity orderDishEntity :
                orderDishEntities) {
            if (orderDishEntity.getIsOrdered() != -1) {
                count = AmountUtils.add(count, orderDishEntity.getDishCount());
            }
        }
        return count;
    }

    /**
     * 获取并单菜品总数
     *
     * @param joinOrderId
     * @return
     */
    public int getJoinOrderDishCount(String joinOrderId) {
        int count = 0;
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        for (OrderEntity orderEntity :
                orderEntities) {
            count += getDishCountByOrderId(orderEntity.getOrderId());
        }
        return count;
    }

    //通过账单id获取账单总金额
    public double getBillMoneyByOrderId(String orderId) {
        double billMoney = 0.0;
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        orderDishEntities.addAll(queryOrderDishEntity(orderId));
        OrderDishEntity orderDishEntity;
        for (int i = 0; i < orderDishEntities.size(); i++) {
            if (orderDishEntities.get(i).getIsOrdered() != -1) {
                orderDishEntity = orderDishEntities.get(i);
                if (orderDishEntity.getType() == 0) {
                    billMoney = AmountUtils.add1(AmountUtils.multiply("" + orderDishEntity.getDishPrice(), "1.0"), billMoney + "");
                } else {
                    billMoney = AmountUtils.add1(AmountUtils.multiply("" + getTaocanPrice(orderDishEntity), orderDishEntity.getDishCount() + ""), billMoney + "");
                }
            }
        }
        return billMoney;
    }

    /**
     * 获取并单总金额
     *
     * @param joinOrderId
     * @return
     */
    public float getJoinOrderBillMoney(String joinOrderId) {
        float billMoney = 0;
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        for (OrderEntity orderEntity :
                orderEntities) {
            billMoney += getBillMoneyByOrderId(orderEntity.getOrderId(), 1);
        }
        return billMoney;
    }

    //通过下单账单id获取账单总金额
    public double getBillMoneyByOrderId(String orderId, int status) {
        double billMoney = 0.0;
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        ArrayList<OrderDishEntity> orderDishEntities = (ArrayList<OrderDishEntity>) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(status), OrderDishEntityDao.Properties.IsOrdered.eq(-2)))).list();
        OrderDishEntity orderDishEntity;
        for (int i = 0; i < orderDishEntities.size(); i++) {
            orderDishEntity = orderDishEntities.get(i);
            if (orderDishEntity.getType() == 1 && status == 1) {
                billMoney = AmountUtils.add1(billMoney + "", AmountUtils.multiply(getOrderedTaocanPrice(orderDishEntity) + "", orderDishEntity.getDishCount() + ""));
            } else {
                billMoney = AmountUtils.add1(billMoney + "", orderDishEntity.getDishPrice() + "");
            }
        }

        return billMoney;
    }

    /**
     * 设置开发票金额
     *
     * @param orderEntity
     * @param money
     */
    public void openInvoiceOfOrder(OrderEntity orderEntity, float money) {
        orderEntity.setInvoiceMoney(AmountUtils.changeY2F (AmountUtils.multiply(money,1)));
        mOrderEntityDao.insertOrReplace(orderEntity);
    }

    /**
     * 获取零头处理的值
     *
     * @param billMoney1
     * @return
     */
    public int getTreatMentMoneyByOrderId(int billMoney1) {
        int billMoney = billMoney1;
        SuplusEntity suplusEntity = mSuplusEntityDao.queryBuilder().build().unique();
        if (suplusEntity.getMinAmount() == null || billMoney >= suplusEntity.getMinAmount()) {
            switch (suplusEntity.getSurplusType()) {
                case 0://不处理

                    break;
                case 1://抹零
                    switch (suplusEntity.getAccurateValue()) {
                        case 0://角
                            billMoney = (billMoney / 10) * 10;
                            break;
                        case 1://元
                            billMoney = (billMoney / 100) * 100;
                            break;
                        case 2://十元
                            billMoney = (billMoney / 1000) * 1000;
                            break;
                    }
                    break;
            }
        }
        if (billMoney % 100 == 0) {
            ArrayList<MantissaEntity> mantissaEntities = (ArrayList<MantissaEntity>) mMantissaEntityDao.queryBuilder().list();
            int length;
            for (MantissaEntity mantissaEntity :
                    mantissaEntities) {
                length = CustomMethod.sizeOfInt(mantissaEntity.getMantissaValue());
                if ((billMoney / 100) % AmountUtils.changeY2F(Math.pow(10, length)) == mantissaEntity.getMantissaValue()) {
                    billMoney = billMoney - mantissaEntity.getReduceValue() * 100;
                    break;
                }
            }
        }
        return billMoney1 - billMoney;
    }

    /**
     * 获取零头处理和不吉利尾数的值
     *
     * @param orderId
     * @param status
     * @return
     */
    public int[] getMLMoneyByOrderId(String orderId, int status) {
        int hadPayMoney = AmountUtils.changeY2F(getHadPayMoneyByOrderId(orderId));
        int[] result = new int[2];
        result[0] = 0;
        result[1] = 0;
        int billMoney1 = getDishPriceSum1(orderId, status);
        if (hadPayMoney == billMoney1) {
            //支付金额等于应收金额，不需要抹零和不吉利位数处理
        } else {
            int billMoney = billMoney1;
            SuplusEntity suplusEntity = mSuplusEntityDao.queryBuilder().build().unique();
            if (suplusEntity != null && (suplusEntity.getMinAmount() <= billMoney || suplusEntity.getMinAmount() == null)) {
                switch (suplusEntity.getSurplusType()) {
                    case 0://不处理

                        break;
                    case 1://抹零
                        switch (suplusEntity.getAccurateValue()) {
                            case 0://角
                                billMoney = (billMoney / 10) * 10;
                                break;
                            case 1://元
                                billMoney = (billMoney / 100) * 100;
                                break;
                            case 2://十元
                                billMoney = (billMoney / 1000) * 1000;
                                break;
                        }
                        break;
                }
            }
            result[0] = billMoney1 - billMoney;
            int billMoney2 = billMoney;
            if (billMoney % 100 == 0) {
                ArrayList<MantissaEntity> mantissaEntities = (ArrayList<MantissaEntity>) mMantissaEntityDao.queryBuilder().list();
                int length;
                for (MantissaEntity mantissaEntity :
                        mantissaEntities) {
                    length = CustomMethod.sizeOfInt(mantissaEntity.getMantissaValue());
                    if ((billMoney / 100) % AmountUtils.changeY2F(Math.pow(10, length)) == mantissaEntity.getMantissaValue()) {
                        billMoney = billMoney - mantissaEntity.getReduceValue() * 100;
                        break;
                    }
                }
            }
            result[1] = billMoney2 - billMoney;
        }
        return result;
    }

    public int getDishPriceSum1(String orderId, int status) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
            DiscountHistoryEntity mDiscountHistoryEntity = getDiscount(orderId);
            if (status == 0) {
                orderDishEntities.addAll(queryOrderDish(orderId));
            } else {
                orderDishEntities.addAll(queryOrderedDish1(orderId));
            }
            double totalPrice = 0;
            double dishPrice = 0.0;//商品原价
            int rate[];
            double rate0, rate1;
            for (OrderDishEntity orderDishEntity :
                    orderDishEntities) {
                if (orderDishEntity.getType() == 0) {
                    //非套餐
                    dishPrice = AmountUtils.multiply1(orderDishEntity.getDishPrice() + "", "1.0");
                } else {
                    //套餐
                    if (status == 1) {
                        dishPrice = AmountUtils.multiply1(getOrderedTaocanPrice(orderDishEntity) * orderDishEntity.getDishCount() + "", "1.0");
                    } else {
                        dishPrice = AmountUtils.multiply1(getTaocanPrice(orderDishEntity) * orderDishEntity.getDishCount() + "", "1.0");
                    }
                }
                if (orderDishEntity.getIsOrdered() == 1) {
                    //已下单
                    if (orderDishEntity.getType() == 0) {
                        //非套餐
                        rate = getDishDiscountRate(orderEntity, orderDishEntity, mDiscountHistoryEntity);
                        rate0 = (double) (rate[0]) / 100;
                        rate1 = (double) (rate[1]) / 100;
                        dishPrice = AmountUtils.multiply(rate0, dishPrice, rate1);
                    } else {
                        //套餐
                        rate = getTaocanDishDiscountRate(orderEntity, orderDishEntity, mDiscountHistoryEntity);
                        rate0 = (double) (rate[0]) / 100;
                        rate1 = (double) (rate[1]) / 100;
                        dishPrice = AmountUtils.multiply(rate0, dishPrice, rate1);
                    }
                }
                totalPrice = AmountUtils.add(totalPrice, dishPrice);
            }
            return AmountUtils.changeY2F(totalPrice);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getDishPriceSum(String orderId, int status) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
            DiscountHistoryEntity mDiscountHistoryEntity = getDiscount(orderId);
            if (status == 0) {
                orderDishEntities.addAll(queryOrderDish(orderId));
            } else {
                orderDishEntities.addAll(queryOrderedDish(orderId));
            }
            double totalPrice = 0;
            double dishPrice = 0.0f;//商品原价
            int rate[];
            String dishPriceStr;
            double rate0, rate1;
            for (OrderDishEntity orderDishEntity :
                    orderDishEntities) {
                if (orderDishEntity.getType() == 0) {
                    //非套餐
                    dishPrice = AmountUtils.multiply1(orderDishEntity.getDishPrice() + "", "1.0");
                } else {
                    //套餐
                    if (status == 1) {
                        dishPrice = getOrderedTaocanPrice(orderDishEntity) * orderDishEntity.getDishCount();
                    } else {
                        dishPrice = getTaocanPrice(orderDishEntity) * orderDishEntity.getDishCount();
                    }
                }
                if (orderDishEntity.getIsOrdered() == 1) {
                    //已下单
                    if (orderDishEntity.getType() == 0) {
                        //非套餐
                        rate = getDishDiscountRate(orderEntity, orderDishEntity, mDiscountHistoryEntity);
                        rate0 = AmountUtils.multiply(rate[0], 0.01);
                        rate1 = AmountUtils.multiply(rate[1], 0.01);
                        dishPrice = AmountUtils.multiply(rate0, dishPrice, rate1);
                    } else {
                        //套餐
                        rate = getTaocanDishDiscountRate(orderEntity, orderDishEntity, mDiscountHistoryEntity);
                        rate0 = AmountUtils.multiply(rate[0], 0.01);
                        rate1 = AmountUtils.multiply(rate[0], 0.01);
                        dishPrice = AmountUtils.multiply(rate0, dishPrice, rate1);
                    }
                }
                totalPrice = AmountUtils.add(dishPrice, totalPrice);
            }
            return AmountUtils.changeY2F(totalPrice);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 获取零头处理的值
     *
     * @param orderId
     * @param status
     * @return
     */
    public int getTreatMentMoneyByOrderId(String orderId, int status) {
        int hadPayMoney = AmountUtils.changeY2F (getHadPayMoneyByOrderId(orderId));
        int billMoney1 = getDishPriceSum1(orderId, status);
        if (hadPayMoney == billMoney1) {
            //如果已支付金额等于应收金额，此时不需要抹零和不吉利位数处理，并将应收款作为抹零金额
            return 0;
        } else {
            int billMoney = billMoney1;
            SuplusEntity suplusEntity = mSuplusEntityDao.queryBuilder().build().unique();
            if (suplusEntity != null && (suplusEntity.getMinAmount() == null || suplusEntity.getMinAmount() <= billMoney)) {
                switch (suplusEntity.getSurplusType()) {
                    case 0://不处理

                        break;
                    case 1://抹零
                        switch (suplusEntity.getAccurateValue()) {
                            case 0://角
                                billMoney = (billMoney / 10) * 10;
                                break;
                            case 1://元
                                billMoney = (billMoney / 100) * 100;
                                break;
                            case 2://十元
                                billMoney = (billMoney / 1000) * 1000;
                                break;
                        }
                        break;
                }
            }
            if (billMoney % 100 == 0) {
                ArrayList<MantissaEntity> mantissaEntities = (ArrayList<MantissaEntity>) mMantissaEntityDao.queryBuilder().list();
                int length;
                for (MantissaEntity mantissaEntity :
                        mantissaEntities) {
                    length = CustomMethod.sizeOfInt(mantissaEntity.getMantissaValue());
                    if ((billMoney / 100) % AmountUtils.changeY2F(Math.pow(10, length)) == mantissaEntity.getMantissaValue()) {
                        billMoney = billMoney - mantissaEntity.getReduceValue() * 100;
                        break;
                    }
                }
            }

            return billMoney1 - billMoney;
        }
    }

    /**
     * 处理代金券
     *
     * @param orderEntity
     * @param status
     * @return
     */
    public void dealWithVoucher(OrderEntity orderEntity, int status) {
        if (orderEntity != null) {
            int selftTreatMoney = orderEntity.getSelfTreatMoney() == null ? 0 : orderEntity.getSelfTreatMoney();//手动抹零
            int couponFaceValue = 0;
            couponFaceValue += getGrouponMoneySum(orderEntity.getOrderId());//团购金额
            int needPayMoney = getDishPriceSum1(orderEntity.getOrderId(), status) - getTreatMentMoneyByOrderId(orderEntity.getOrderId(), status) - selftTreatMoney;
            if (orderEntity != null && orderEntity.getIsCoupon() != null && orderEntity.getIsCoupon() == 1 && orderEntity.getCouponType() != null && orderEntity.getCouponType() == 1) {
                couponFaceValue += orderEntity.getCouponFaceValue() == null ? 0 : orderEntity.getCouponFaceValue();
            }
            if (couponFaceValue > 0 && couponFaceValue > needPayMoney) {
                int dishPrice = couponFaceValue - needPayMoney;
                try {
                    double dishPrice1 = AmountUtils.multiply(dishPrice, 0.01);
                    insertVoucherDish(orderEntity.getOrderId(), dishPrice1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                deleteVoucherDish(orderEntity.getOrderId());
            }
        }
    }

    /**
     * 获取团购总金额
     *
     * @param orderId
     * @return
     */
    private int getGrouponMoneySum(String orderId) {
        int result = 0;
        QueryBuilder queryBuilder = mPayModeEntityDao.queryBuilder();
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        payModeEntities.addAll(queryBuilder.where(queryBuilder.and(PayModeEntityDao.Properties.OrderId.eq(orderId), PayModeEntityDao.Properties.PaymentType.eq(4))).list());
        for (PayModeEntity payModeEntity :
                payModeEntities) {
            result += AmountUtils.changeY2F(AmountUtils.multiply(payModeEntity.getPayMoney(),1));
        }
        return result;
    }

    /**
     * 根据账单id和状态返回应收金额 = 折后额-系统抹零金额-手动抹零金额-优惠券金额
     *
     * @param orderId
     * @param status
     * @return
     */
    public double getReceivableMoney(String orderId, int status) {
        double billMoney = getBillMoneyByOrderId(orderId, status);
        double yhMoney = AmountUtils.multiply(getYHMoney(orderId), 0.01);
        return AmountUtils.subtract(billMoney, yhMoney);
    }

    /**
     * 获取优惠券的面值
     *
     * @param orderEntity
     * @return
     */
    public int getCouponMoney(OrderEntity orderEntity, int status) {
        if (orderEntity.getIsCoupon() != null && orderEntity.getIsCoupon() == 1 && orderEntity.getCouponType() != null) {
            if (orderEntity.getCouponType() == 0) {
                //满减券
                return orderEntity.getCouponFaceValue();
            } else if (orderEntity.getCouponType() == 1) {
                //代金券
                return orderEntity.getCouponFaceValue();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取账单优惠金额
     *
     * @param orderId
     * @return
     */
    public int getYHMoney(String orderId) {
        int treatmentMoney = 0;
        DiscountHistoryEntity discountHistoryEntity = getDiscount(orderId);
        if (discountHistoryEntity != null) {
            try {
                treatmentMoney += getDiscountMoney(orderId, discountHistoryEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                treatmentMoney += getVipDiscountMoney(orderId);
                treatmentMoney += getCouponDiscountMoney(orderId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            treatmentMoney += getPresentMoney(orderId);
            treatmentMoney += getTreatMentMoneyByOrderId(orderId, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null) {
            try {
                treatmentMoney += orderEntity.getSelfTreatMoney();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return treatmentMoney;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ArrayMap<String, String> getYHMoneyMap(String orderId) {
        ArrayMap<String, String> resultMap = new ArrayMap<>();
        DiscountHistoryEntity discountHistoryEntity = getDiscount(orderId);
        if (discountHistoryEntity != null) {
            int treatmentMoney = getDiscountMoney(orderId, discountHistoryEntity);
            resultMap.put("discountMoney", String.valueOf(treatmentMoney));
            resultMap.put("vipDiscount", String.valueOf(0));
            resultMap.put("couponDiscount", String.valueOf(0));
        } else {
            resultMap.put("discountMoney", String.valueOf(0));
            resultMap.put("vipDiscount", String.valueOf(getVipDiscountMoney(orderId)));
            resultMap.put("couponDiscount", String.valueOf(getCouponDiscountMoney(orderId)));
        }
        resultMap.put("presentMoney", String.valueOf(getPresentMoney(orderId)));
        resultMap.put("treatMentMoney", String.valueOf(getTreatMentMoneyByOrderId(orderId, 1)));
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null) {
            resultMap.put("selfMoney", String.valueOf(orderEntity.getSelfTreatMoney()));
        } else {
            resultMap.put("selfMoney", String.valueOf(0));
        }
        return resultMap;
    }

    /**
     * 获取优惠详情
     *
     * @param orderId
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ArrayMap<String, Integer> getYHMoneyMapInt(String orderId) {
        ArrayMap<String, Integer> resultMap = new ArrayMap<>();
        DiscountHistoryEntity discountHistoryEntity = getDiscount(orderId);
        if (discountHistoryEntity != null) {
            int treatmentMoney = getDiscountMoney(orderId, discountHistoryEntity);
            resultMap.put("discountMoney", treatmentMoney);
            resultMap.put("vipDiscount", 0);
            resultMap.put("couponDiscount", 0);
        } else {
            resultMap.put("discountMoney", 0);
            resultMap.put("vipDiscount", getVipDiscountMoney(orderId));
            resultMap.put("couponDiscount", getCouponDiscountMoney(orderId));
        }
        resultMap.put("presentMoney", getPresentMoney(orderId));
        int[] treatMoney = getMLMoneyByOrderId(orderId, 1);
        resultMap.put("treatMentMoney", treatMoney[0]);
        resultMap.put("mantissaMoney", treatMoney[1]);
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null && orderEntity.getSelfTreatMoney() != null) {
            resultMap.put("selfMoney", orderEntity.getSelfTreatMoney());
        } else {
            resultMap.put("selfMoney", 0);
        }
        return resultMap;
    }

    /**
     * 获取总单已支付金额
     *
     * @param joinOrderId
     * @return
     */
    public float getJoinOrderHadPayMoney(String joinOrderId) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        float hadPayMoney = 0;
        for (OrderEntity orderEntity :
                orderEntities) {
            hadPayMoney += getHadPayMoneyByOrderId(orderEntity.getOrderId(), 0);
        }
        hadPayMoney += getHadPayMoneyByOrderId(joinOrderId, 1);
        return hadPayMoney;
    }

    //根据账单id获取还需收款数额
    public int getReceivableMoneyByOrderId(String orderId) {
        double receivableMoney = getReceivableMoney(orderId, 1);
        double hadPayMoney = getHadPayMoneyByOrderId(orderId);
        int receivableMoneyInt = AmountUtils.changeY2F(AmountUtils.subtract(receivableMoney, hadPayMoney));
        return receivableMoneyInt;
    }

    /**
     * 获取已支付金额
     *
     * @param orderId
     * @return
     */
    public double getHadPayMoneyByOrderId(String orderId, int isJoinOrderPay) {
        QueryBuilder queryBuilder = mPayModeEntityDao.queryBuilder();
        double result = 0;
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<PayModeEntity>();
        payModeEntities.addAll(queryBuilder.where(queryBuilder.and(PayModeEntityDao.Properties.OrderId.eq(orderId), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0), PayModeEntityDao.Properties.IsJoinOrderPay.eq(isJoinOrderPay))).list());
        for (int i = 0; i < payModeEntities.size(); i++) {
            result = AmountUtils.add(result, Double.parseDouble("" + payModeEntities.get(i).getPayMoney()));
        }
        return result;
    }

    /**
     * 获取已支付金额
     *
     * @param orderId
     * @return
     */
    public double getHadPayMoneyByOrderId(String orderId) {
        QueryBuilder queryBuilder = mPayModeEntityDao.queryBuilder();
        double result = 0;
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        payModeEntities.addAll(queryBuilder.where(queryBuilder.and(PayModeEntityDao.Properties.OrderId.eq(orderId), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0))).list());
        for (int i = 0; i < payModeEntities.size(); i++) {
            result = AmountUtils.add(result, Double.parseDouble(AmountUtils.multiply(payModeEntities.get(i).getPayMoney() + "", "1.0")));
        }
        return result;
    }

    /**
     * 判断订单是否有支付
     *
     * @param orderId
     * @return
     */
    public boolean isOrderHasPay(String orderId) {
        QueryBuilder queryBuilder = mPayModeEntityDao.queryBuilder();
        return queryBuilder.where(queryBuilder.and(PayModeEntityDao.Properties.OrderId.eq(orderId), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0))).buildCount().count() > 0;
    }

    /**
     * 判断订单是否已经支付完成
     *
     * @param orderId
     * @return
     */
    public boolean isOrderPayOver(String orderId) {
        try {
            return getReceivableMoneyByOrderId(orderId) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据并单id获取所有并单的子单
     *
     * @param joinOrderId
     * @return
     */
    public ArrayList<OrderEntity> queryJoinOrderData(String joinOrderId) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
        orderEntities.addAll(queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.IsJoinedOrder.eq(1), OrderEntityDao.Properties.JoinedOrderId.eq(joinOrderId))).list());
        return orderEntities;
    }

    /**
     * 根据桌位获取其他订单
     *
     * @param tableId
     * @return
     */
    public ArrayList<OrderEntity> queryOrderData(String tableId, String orderId) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
        queryBuilder = queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderStatus.eq(0), OrderEntityDao.Properties.IsJoinedOrder.eq(0), OrderEntityDao.Properties.OrderId.notEq(orderId), OrderEntityDao.Properties.OrderType.eq(0)));
        ArrayList<OrderEntity> orderEntities = (ArrayList<OrderEntity>) queryBuilder.where(queryBuilder.or(queryBuilder.and(OrderEntityDao.Properties.TableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(0))
                , queryBuilder.and(OrderEntityDao.Properties.JoinedTableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(1)))).list();
        return orderEntities;
    }

    /**
     * 根据桌位获取所有订单
     *
     * @param tableId
     * @return
     */
    public ArrayList<OrderEntity> queryOrderData(String tableId) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderStatus.eq(0));
        ArrayList<OrderEntity> orderEntities = (ArrayList<OrderEntity>) queryBuilder.where(queryBuilder.or(queryBuilder.and(OrderEntityDao.Properties.TableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(0))
                , queryBuilder.and(OrderEntityDao.Properties.JoinedTableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(1)))).list();
        return orderEntities;
    }

    //根据桌位查看账单数据
    public ArrayList<OrderEntity> queryOrderData(String tableId, int orderStatus) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
        ArrayList<OrderEntity> orderEntities = (ArrayList<OrderEntity>) queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.TableId.eq(tableId), OrderEntityDao.Properties.OrderStatus.eq(orderStatus))).list();
        return orderEntities;
    }

    //根据桌位查看账单数据
    public int queryScheduleOrderCount(String tableId) {
        int result = 0;
        try {
            QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
            queryBuilder = queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderType.eq(2), OrderEntityDao.Properties.OrderStatus.eq(0)));
            result = (int) queryBuilder.where(queryBuilder.or(queryBuilder.and(OrderEntityDao.Properties.TableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(0)),
                    queryBuilder.and(OrderEntityDao.Properties.JoinedTableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(1)))).orderAsc(OrderEntityDao.Properties.OpenTime).buildCount().count();
        } catch (Exception e) {

        }
        return result;
    }

    //根据桌位查看账单数据
    public ArrayList<OrderEntity> queryOrderData(String tableId, int orderStatus, int orderType) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
            queryBuilder = queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderType.eq(orderType), OrderEntityDao.Properties.OrderStatus.eq(orderStatus)));
            orderEntities.addAll(queryBuilder.where(queryBuilder.or(queryBuilder.and(OrderEntityDao.Properties.TableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(0)),
                    queryBuilder.and(OrderEntityDao.Properties.JoinedTableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(1)))).orderAsc(OrderEntityDao.Properties.OpenTime).list());
        } catch (Exception e) {

        }
        return orderEntities;
    }

    //根据桌位查看账单数据
    public OrderEntity queryFirstOrder(String tableId, int orderStatus, int orderType) {
        try {
            QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
            queryBuilder = queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderType.eq(orderType), OrderEntityDao.Properties.OrderStatus.eq(orderStatus)));
            OrderEntity orderEntity = (OrderEntity) queryBuilder.where(queryBuilder.or(queryBuilder.and(OrderEntityDao.Properties.TableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(0)),
                    queryBuilder.and(OrderEntityDao.Properties.JoinedTableId.eq(tableId), OrderEntityDao.Properties.IsJoinedTable.eq(1)))).orderAsc(OrderEntityDao.Properties.OpenTime).list().get(0);
            return orderEntity;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 插入预订单
     *
     * @param context
     * @param scheduleEntity
     * @param tableEntity
     */
    public void insertOneSchedule(Context context, ScheduleEntity scheduleEntity, TableEntity tableEntity) {
        //生成订单信息，并设置账单类型为预订单2
        String orderId = insertTableOrder(context, tableEntity, scheduleEntity.getMealTime(), scheduleEntity.getMealPeople(), scheduleEntity.getScheduleMark(), 2);
        //修改桌位状态
        scheduleTable(tableEntity.getTableId());
        scheduleEntity.setOrderId(orderId);
        scheduleEntity.setTableId(tableEntity.getTableId());
        scheduleEntity.setScheduleStatus(1);
        mScheduleEntityDao.insertOrReplace(scheduleEntity);
    }

    /**
     * 预定某张桌
     *
     * @param tableId
     */
    public void scheduleTable(String tableId) {
        try {
            TableEntity tableEntity = queryOneTableData(tableId);
            tableEntity.setIsLock(tableEntity.getIsLock() + 1);
            mTableEntityDao.insertOrReplace(tableEntity);
        } catch (Exception e) {

        }
    }

    /**
     * 取消预定或者预定开台
     *
     * @param tableId
     */
    public void cancelScheduleTable(String tableId) {
        try {
            TableEntity tableEntity = queryOneTableData(tableId);
            tableEntity.setIsLock(tableEntity.getIsLock() - 1);
            mTableEntityDao.insertOrReplace(tableEntity);
        } catch (Exception e) {

        }
    }

    //添加一个预订单
    public String insertOneSchedule(Context context, TableEntity mTableEntity, String name, String phoneNumber, long mealsTime, int seatCount, int isOrder, String mark) {
        //生成订单信息，并设置账单类型为预订单2
        String orderId = insertTableOrder(context, mTableEntity, mealsTime, seatCount, mark, 2);
        //修改桌位状态
        updateTableStatus(mTableEntity.getTableId());
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setScheduleId(UUID.randomUUID().toString());
        scheduleEntity.setOrderId(orderId);
        scheduleEntity.setGuestName(name);
        scheduleEntity.setGuestPhone(phoneNumber);
        scheduleEntity.setTableId(mTableEntity.getTableId());
        scheduleEntity.setMealTime(mealsTime);
        scheduleEntity.setMealPeople(seatCount);
        scheduleEntity.setIsOrdered(isOrder);
        scheduleEntity.setScheduleFrom(0);
        scheduleEntity.setScheduleMark(mark);
        scheduleEntity.setScheduleStatus(1);
        scheduleTable(mTableEntity.getTableId());
        mScheduleEntityDao.insertOrReplace(scheduleEntity);
        return orderId;
    }

    //根据状态获取预订单数据
    public ArrayList<ScheduleEntity> queryScheduleByStatus(int status) {
        QueryBuilder queryBuilder = mScheduleEntityDao.queryBuilder();
        return (ArrayList<ScheduleEntity>) queryBuilder.where(ScheduleEntityDao.Properties.ScheduleStatus.eq(status)).orderAsc(ScheduleEntityDao.Properties.MealTime).list();
    }

    //获取桌位上已生效的预订单
    public ArrayList<ScheduleEntity> queryScheduleByTable(String tableId) {
        QueryBuilder queryBuilder = mScheduleEntityDao.queryBuilder();
        return (ArrayList<ScheduleEntity>) queryBuilder.where(queryBuilder.and(ScheduleEntityDao.Properties.ScheduleStatus.eq(1), ScheduleEntityDao.Properties.TableId.eq(tableId))).orderAsc(ScheduleEntityDao.Properties.MealTime).list();
    }


    //根据桌位id获取桌位名称
    public String getTableNameByTableId(String tableId) {
        if (tableId == null) {
            return "";
        } else {
            QueryBuilder queryBuilder = mTableEntityDao.queryBuilder().where(TableEntityDao.Properties.TableId.eq(tableId));
            TableEntity tableEntity = (TableEntity) queryBuilder.build().unique();
            if (tableEntity == null) {
                return "";
            } else {
                return tableEntity.getTableName();
            }
        }
    }

    //根据预订单id修改状态
    public long changeScheduleStatusById(String scheduleId, int status) {
        QueryBuilder queryBuilder = mScheduleEntityDao.queryBuilder();
        ScheduleEntity scheduleEntity = (ScheduleEntity) queryBuilder.where(ScheduleEntityDao.Properties.ScheduleId.eq(scheduleId)).build().unique();
        scheduleEntity.setScheduleStatus(status);
        return mScheduleEntityDao.insertOrReplace(scheduleEntity);
    }

    //根据外卖单状态
    public ArrayList<TakeOutOrderEntity> queryTakeoutByStatus(int status) {
        QueryBuilder queryBuilder = mTakeOutOrderEntityDao.queryBuilder();
        ArrayList<TakeOutOrderEntity> takeOutOrderEntities = (ArrayList<TakeOutOrderEntity>) queryBuilder.where(TakeOutOrderEntityDao.Properties.TakeoutStatus.eq(status)).orderDesc(TakeOutOrderEntityDao.Properties.Id).list();
        return takeOutOrderEntities;
    }

    /**
     * 根据账单id获取外卖单
     *
     * @param orderId
     * @return
     */
    public TakeOutOrderEntity getTakeOutOrderById(String orderId) {
        return mTakeOutOrderEntityDao.queryBuilder().where(TakeOutOrderEntityDao.Properties.OrderId.eq(orderId)).build().unique();
    }

    public TakeOutOrderEntity getTakeoutOrderByOtherId(String otherOrderId) {
        return mTakeOutOrderEntityDao.queryBuilder().where(TakeOutOrderEntityDao.Properties.OtherOrderId.eq(otherOrderId)).build().unique();
    }

    //根据账单id获取已下单商品数量
    public double getOrderedDishCountByOrderId(String orderId) {
        double result = 0;
        if (orderId == null) {
        } else {
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
            orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsOrdered.eq(1))).list());
            for (OrderDishEntity orderDish :
                    orderDishEntities) {
                result = AmountUtils.add(result, orderDish.getDishCount());
            }
        }
        return result;
    }


    //更换桌位
    public void changeTable(String oldTableId, String newTableId, String orderId) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        orderEntity.setTableId(newTableId);
        orderEntity.setAreaId(queryOneTableData(newTableId).getAreaId());
        mOrderEntityDao.insertOrReplace(orderEntity);
        updateTableStatus(oldTableId);
        updateTableStatus(newTableId);
        TableEntity tableEntity = queryOneTableData(newTableId);
        if (tableEntity != null && orderEntity != null && orderEntity.getIsUpload() == 1) {
            insertUploadData(orderId, newTableId + "`" + tableEntity.getTableName(), 4);
        }
    }

    /**
     * 合台
     *
     * @param oldTableId
     * @param selectedTables
     */
    public void joinTable(String oldTableId, ArrayList<TableEntity> selectedTables) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        for (TableEntity tableEntity :
                selectedTables) {
            orderEntities.clear();
            orderEntities.addAll(queryOrderData(tableEntity.getTableId()));
            for (OrderEntity orderEntity :
                    orderEntities) {
                orderEntity.setIsJoinedTable(1);
                orderEntity.setJoinedTableId(oldTableId);
                mOrderEntityDao.insertOrReplace(orderEntity);
            }
            updateTableStatus(tableEntity.getTableId());
        }
        updateTableStatus(oldTableId);
    }

    /**
     * 取消合台
     *
     * @param tableId
     */
    public void cancleJoinTable(String tableId) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryOrderData(tableId));
        for (OrderEntity orderEntity :
                orderEntities) {
            if (orderEntity.getIsJoinedTable() == 1 && orderEntity.getJoinedTableId().equals(tableId)) {
                orderEntity.setIsJoinedTable(0);
                orderEntity.setJoinedTableId(null);
                mOrderEntityDao.insertOrReplace(orderEntity);
                updateTableStatus(orderEntity.getTableId());
            }
        }
        updateTableStatus(tableId);
    }

    /**
     * 并单
     *
     * @param orderId
     * @param selectOrderes
     */
    public void joinOrder(String tableId, String orderId, ArrayList<OrderEntity> selectOrderes) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null) {
            for (OrderEntity orderEntity1 :
                    selectOrderes) {
                orderEntity1.setIsJoinedOrder(1);
                orderEntity1.setJoinedOrderId(orderId);
                mOrderEntityDao.insertOrReplace(orderEntity1);
                if (orderEntity1.getIsJoinedTable() == 1 && orderEntity1.getJoinedTableId() != null) {
                    updateTableStatus(orderEntity1.getJoinedTableId());
                } else {
                    updateTableStatus(orderEntity1.getTableId());
                }
            }
            orderEntity.setIsJoinedOrder(1);
            orderEntity.setJoinedOrderId(orderId);
            mOrderEntityDao.insertOrReplace(orderEntity);
            if (orderEntity.getIsJoinedTable() == 1 && orderEntity.getJoinedTableId() != null) {
                updateTableStatus(orderEntity.getJoinedTableId());
            } else {
                updateTableStatus(orderEntity.getTableId());
            }
        }
    }

    /**
     * 取消并单
     *
     * @param joinOrderId
     */
    public void cancleJoinOrder(String joinOrderId) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderStatus.eq(0));
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.IsJoinedOrder.eq(1), OrderEntityDao.Properties.JoinedOrderId.eq(joinOrderId))).list());
        for (OrderEntity orderEntity :
                orderEntities) {
            orderEntity.setIsJoinedOrder(0);
            orderEntity.setJoinedOrderId(null);
            mOrderEntityDao.insertOrReplace(orderEntity);
            if (orderEntity.getIsJoinedTable() == 1 && orderEntity.getJoinedTableId() != null) {
                updateTableStatus(orderEntity.getJoinedTableId());
            } else {
                updateTableStatus(orderEntity.getTableId());
            }
        }
    }

    //再开一单
    public String insertOrderAgain(Context context, TableEntity tableEntity) {
        return insertTableOrder(context, tableEntity, tableEntity.getTableSeat(), null);
    }

    //取消开台
    public int cancleTable(TableEntity tableEntity, String orderId) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            boolean isHasPay = isOrderHasPay(orderId);
            if (getOrderedDishCountByOrderId(orderId) > 0) {
                //已有下单商品，不能取消开台
                return 0;
            } else {
                if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1 && isHasPay) {
                    //微信点餐已支付
                    return 3;
                } else {
                    if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                        //微信订单
                        return 4;
                    } else {
                        //店内订单直接取消
                        confirmCancleTable(tableEntity, orderId);
                        return 1;
                    }
                }
            }
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 店内订单确认取消开台
     *
     * @param tableEntity
     * @param orderId
     */
    public void confirmCancleTable(TableEntity tableEntity, String orderId) {
        try {
            dropOneOrder(orderId);
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            queryBuilder.where(OrderDishEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
            updateTableStatus(tableEntity.getTableId());
        } catch (Exception e) {

        }
    }

    //根据账单id获取已下单商品数量
    public int getPrintedCount(String orderId) {
        int result = 0;
        try {
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            result = (int) queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsOrdered.notEq(-1), OrderDishEntityDao.Properties.IsPrint.eq(1))).buildCount().count();
        } catch (Exception e) {

        }
        return result;
    }

    //取消开台
    public int cancleSnackOrder(String orderId) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            double payMoney = getHadPayMoneyByOrderId(orderId);
            int count = getPrintedCount(orderId);
            if (count > 0) {
                //该笔订单已经有打印
                return 0;
            }
            if (payMoney > 0) {
                //该笔订单已有支付
                return 3;
            }
            if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                //微信订单
                return 4;
            }
            dropOneOrder(orderId);
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            queryBuilder.where(OrderDishEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    //取消开台
    public int cancleTable(String orderId) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            boolean isHasPay = isOrderHasPay(orderId);
            if (getOrderedDishCountByOrderId(orderId) > 0) {
                //已有下单商品，不能取消开台
                return 0;
            } else {
                if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1 && isHasPay) {
                    //微信点餐已支付
                    return 3;
                } else {
                    if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                        //微信订单
                        return 4;
                    } else {
                        //店内订单直接取消
                        dropOneOrder(orderId);
                        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
                        queryBuilder.where(OrderDishEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
                        return 1;
                    }
                }
            }
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 微信订单确认取消开台
     *
     * @param tableEntity
     * @param orderId
     */
    public void confirmWXCancleTable(TableEntity tableEntity, String orderId) {
        try {
            dropOneOrder(orderId);
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            queryBuilder.where(OrderDishEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
            updateTableStatus(tableEntity.getTableId());
            deleteWxOrderMessage(orderId, 0);
        } catch (Exception e) {

        }
    }

    /**
     * 微信订单确认取消开台
     *
     * @param orderId
     */
    public void confirmWXCancleTable(String orderId) {
        try {
            dropOneOrder(orderId);
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            queryBuilder.where(OrderDishEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
            deleteWxOrderMessage(orderId, 0);
        } catch (Exception e) {

        }
    }

    //根据账单id修改账单类型
    public void changeOrderTypeById(String orderId, int orderType) {
        OrderEntity orderEntity = mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderId.eq(orderId)).build().unique();
        orderEntity.setOrderType(orderType);
        orderEntity.setOpenTime(System.currentTimeMillis());
        mOrderEntityDao.insertOrReplace(orderEntity);
    }

    //预定开台
    public void openScheduleTable(String tableId, String orderId) {
        ScheduleEntity scheduleEntity = mScheduleEntityDao.queryBuilder().where(ScheduleEntityDao.Properties.OrderId.eq(orderId)).build().unique();
        changeScheduleStatusById(scheduleEntity.getScheduleId(), 3);
        changeOrderTypeById(orderId, 0);
        updateTableStatus(tableId);
        cancelScheduleTable(tableId);
    }

    //取消预定
    public void cancleScheduleTable(String tableId, String orderId) {
        //账单中删除该预定单，预定记录中修改为取消状态，刷新桌位状态
        mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderId.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        ScheduleEntity scheduleEntity = mScheduleEntityDao.queryBuilder().where(ScheduleEntityDao.Properties.OrderId.eq(orderId)).build().unique();
        scheduleEntity.setScheduleStatus(2);
        mScheduleEntityDao.insertOrReplace(scheduleEntity);
        cancelScheduleTable(tableId);
    }

    /**
     * 获取总单所有的支付
     *
     * @param joinOrderId
     * @return
     */
    public ArrayList<PayModeEntity> getJoinOrderAllPayMode(String joinOrderId) {
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        for (OrderEntity orderEntity :
                orderEntities) {
            payModeEntities.addAll(getPayModeByOrderId(orderEntity.getOrderId()));
        }
        payModeEntities.addAll(getJoinOrderPayMode(joinOrderId));
        return payModeEntities;
    }

    /**
     * 获取总单支付记录
     *
     * @param joinOrderId
     * @return
     */
    public ArrayList<PayModeEntity> getJoinOrderPayMode(String joinOrderId) {
        QueryBuilder queryBuidler = mPayModeEntityDao.queryBuilder();
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        payModeEntities.addAll(queryBuidler.where(queryBuidler.and(PayModeEntityDao.Properties.OrderId.eq(joinOrderId), PayModeEntityDao.Properties.IsJoinOrderPay.eq(1))).list());
        return payModeEntities;
    }

    //根据账单id获取付款记录
    public ArrayList<PayModeEntity> getPayModeByOrderId(String orderId) {
        QueryBuilder queryBuidler = mPayModeEntityDao.queryBuilder();
        ArrayList<PayModeEntity> payModeEntities = (ArrayList<PayModeEntity>) queryBuidler.where(queryBuidler.and(PayModeEntityDao.Properties.OrderId.eq(orderId), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0))).list();
        return payModeEntities;
    }

    //根据账单id获取付款记录
    public ArrayList<PayModeEntity> getAllPayModeByOrderId(String orderId) {
        QueryBuilder queryBuidler = mPayModeEntityDao.queryBuilder();
        ArrayList<PayModeEntity> payModeEntities = (ArrayList<PayModeEntity>) queryBuidler.where(PayModeEntityDao.Properties.OrderId.eq(orderId)).list();
        return payModeEntities;
    }

    /**
     * 设置订单手动抹零金额
     *
     * @param orderId
     * @param money
     */
    public boolean setMLMoneyByOrderId(String orderId, double money, EmployeeEntity employeeEntity) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null) {
            if (AmountUtils.add(AmountUtils.multiply(orderEntity.getSelfTreatMoney(),0.01) , money) <= employeeEntity.getMaxResetAmount()) {
                orderEntity.setSelfTreatMoney(orderEntity.getSelfTreatMoney() + AmountUtils.changeY2F(money));
                mOrderEntityDao.insertOrReplace(orderEntity);
                return true;
            }
        }
        return false;
    }

    //插入付款记录
    public String insertPayModeCash(String orderId, String paymentId, String paymentName, int paymentType, float payMoney, int isJoinOrderPay, String realMoney) {
        PayModeEntity payModeEntity = new PayModeEntity();
        payModeEntity.setOrderId(orderId);
        payModeEntity.setPaymentId(paymentId);
        payModeEntity.setPayModeId(UUID.randomUUID().toString());
        payModeEntity.setPayMoney(payMoney);
        payModeEntity.setPaymentName(paymentName);
        payModeEntity.setPaymentType(paymentType);
        payModeEntity.setPayTime(System.currentTimeMillis());
        payModeEntity.setIsJoinOrderPay(0);
        payModeEntity.setElectricOrderSerial(realMoney);
        mPayModeEntityDao.insertOrReplace(payModeEntity);
        return payModeEntity.getPayModeId();
    }

    //插入付款记录
    public String insertPayMode(String orderId, String paymentId, String paymentName, int paymentType, float payMoney, int isJoinOrderPay) {
        PayModeEntity payModeEntity = new PayModeEntity();
        payModeEntity.setOrderId(orderId);
        payModeEntity.setPaymentId(paymentId);
        payModeEntity.setPayModeId(UUID.randomUUID().toString());
        payModeEntity.setPayMoney(payMoney);
        payModeEntity.setPaymentName(paymentName);
        payModeEntity.setPaymentType(paymentType);
        payModeEntity.setPayTime(System.currentTimeMillis());
        payModeEntity.setIsJoinOrderPay(0);
        mPayModeEntityDao.insertOrReplace(payModeEntity);
        return payModeEntity.getPayModeId();
    }

    //插入付款记录
    public void insertPayMode(String orderId, String paymentId, String paymentName, int paymentType, float payMoney, int isJoinOrderPay, String paySerial) {
        PayModeEntity payModeEntity = new PayModeEntity();
        payModeEntity.setOrderId(orderId);
        payModeEntity.setPaymentId(paymentId);
        payModeEntity.setPayModeId(UUID.randomUUID().toString());
        payModeEntity.setPayMoney(payMoney);
        payModeEntity.setPaymentName(paymentName);
        payModeEntity.setPaymentType(paymentType);
        payModeEntity.setPayTime(System.currentTimeMillis());
        payModeEntity.setIsJoinOrderPay(isJoinOrderPay);
        payModeEntity.setElectricOrderSerial(paySerial);
        mPayModeEntityDao.insertOrReplace(payModeEntity);
    }

    /**
     * 获取会员充值的金额
     *
     * @return
     */
    public double getVipRechargeMoney() {
        double result = 0;
        QueryBuilder queryBuilder = mPayModeEntityDao.queryBuilder();
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        payModeEntities.addAll(queryBuilder.where(PayModeEntityDao.Properties.OrderId.eq("100"), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0)).list());
        for (PayModeEntity payModeEntity :
                payModeEntities) {
            result = AmountUtils.add(result, Double.parseDouble("" + payModeEntity.getPayMoney()));
        }
        return result;
    }

    /**
     * 插入支付记录
     *
     * @param payModeEntity
     */
    public void insertPayMode(PayModeEntity payModeEntity) {
        payModeEntity.setIsJoinOrderPay(0);
        mPayModeEntityDao.insertOrReplace(payModeEntity);
    }

    //根据特殊处理的类型获取特殊处理
    public ArrayList<SpecialEntity> queryAllSpecialByType(int type) {
        ArrayList<SpecialEntity> result = new ArrayList<>();
        QueryBuilder queryBuilder = mSpecialEntityDao.queryBuilder();
        result.addAll(queryBuilder.where(SpecialEntityDao.Properties.SpecialType.eq(type)).list());
        return result;
    }

    //根据商品名称模糊查询
    public ArrayList<DishEntity> searchDishByName(String matchStr) {
        QueryBuilder queryBuilder = mDishEntityDao.queryBuilder();
        return (ArrayList<DishEntity>) queryBuilder.where(DishEntityDao.Properties.DishCode.like("%" + matchStr + "%")).list();
    }

    //根据商品名称模糊查询
    public ArrayList<DishBean> searchDishByName(String orderId, String matchStr) {
        ArrayList<DishBean> dishBeanes = new ArrayList<>();
        try {
            ArrayList<DishEntity> dishEntities = new ArrayList<>();
            if (matchStr.length() > 0) {
                QueryBuilder queryBuilder = mDishEntityDao.queryBuilder();
                dishEntities.addAll(queryBuilder.where(queryBuilder.or(DishEntityDao.Properties.DishCode.like("%" + matchStr + "%"), DishEntityDao.Properties.DishCode1.like("%" + matchStr + "%"))).list());
            }
            for (DishEntity dishEntity :
                    dishEntities) {
                DishBean dishBean = new DishBean();
                dishBean.setDishEntity(dishEntity);
                if (orderId != null) {
                    dishBean.setDishCount(getOneDishCountGroup(orderId, dishEntity.getDishId()));
                } else {
                    dishBean.setDishCount(0);
                }
                dishBean.setHasConfig(isDishHasGuige(dishEntity.getDishId()) || isDishHasZuofa(dishEntity.getDishId()));
                dishBean.setChing(isDishChing(dishEntity.getDishId()));
                dishBeanes.add(dishBean);
            }
        } catch (Exception e) {

        }
        return dishBeanes;
    }

    /**
     * 搜索套餐
     *
     * @param orderId
     * @param matchStr
     * @return
     */
    public ArrayList<TaocanBean> searchTaocanByName(String orderId, String matchStr) {
        ArrayList<TaocanBean> taocanBeanes = new ArrayList<>();
        try {
            ArrayList<TaocanEntity> taocanEntities = new ArrayList<>();
            if (matchStr.length() > 0) {
                QueryBuilder queryBuilder = mTaocanEntityDao.queryBuilder();
                taocanEntities.addAll(queryBuilder.where(queryBuilder.or(TaocanEntityDao.Properties.TaocanCode.like("%" + matchStr + "%"), TaocanEntityDao.Properties.TaocanCode1.like("%" + matchStr + "%"))).list());
            }
            for (TaocanEntity taocanEntity :
                    taocanEntities) {
                TaocanBean taocanBean = new TaocanBean();
                taocanBean.setTaocanEntity(taocanEntity);
                if (orderId != null) {
                    taocanBean.setTaocanCount(getOneDishCountGroup(orderId, taocanEntity.getTaocanId()));
                } else {
                    taocanBean.setTaocanCount(0);
                }
                taocanBean.setChing(isDishChing(taocanEntity.getTaocanId()));
                taocanBeanes.add(taocanBean);
            }
        } catch (Exception e) {

        }
        return taocanBeanes;
    }

    //获取所有估清的商品
    public ArrayList<SellCheckEntity> getAllSellOut() {
        QueryBuilder queryBuilder = mSellCheckEntityDao.queryBuilder().where(SellCheckEntityDao.Properties.IsSellOut.eq(1));
        return (ArrayList<SellCheckEntity>) queryBuilder.build().list();
    }

    //获取库存数据
    public ArrayList<SellCheckEntity> getStockData() {
        QueryBuilder queryBuilder = mSellCheckEntityDao.queryBuilder().where(SellCheckEntityDao.Properties.IsSellOut.eq(0));
        return (ArrayList<SellCheckEntity>) queryBuilder.build().list();
    }

    //添加一条估清记录
    public void insertChing(String dishId) {
        SellCheckEntity sellCheckEntity = new SellCheckEntity();
        sellCheckEntity.setDishId(dishId);
        sellCheckEntity.setSellCheckId(UUID.randomUUID().toString());
        sellCheckEntity.setIsSellOut(1);
        mSellCheckEntityDao.insertOrReplace(sellCheckEntity);
    }

    //添加一条估清记录
    public void insertChing(SellCheckEntity sellCheckEntity) {
        mSellCheckEntityDao.insertOrReplace(sellCheckEntity);
    }

    //删除估清记录
    public void dropChing(SellCheckEntity sellCheckEntity) {
        mSellCheckEntityDao.delete(sellCheckEntity);
    }

    /**
     * 刷新库存
     *
     * @param vos
     */
    public void refreshStock(ArrayList<GoodsInventoryVo> vos) {
        mSellCheckEntityDao.queryBuilder().where(SellCheckEntityDao.Properties.IsSellOut.eq(0)).buildDelete().executeDeleteWithoutDetachingEntities();
        for (GoodsInventoryVo vo :
                vos) {
            SellCheckEntity sellCheckEntity = new SellCheckEntity(vo);
            mSellCheckEntityDao.insertOrReplace(sellCheckEntity);
        }
    }

    /**
     * 判断商品是否已售完
     *
     * @param dishId
     * @return
     */
    public boolean isDishChing(String dishId) {
        QueryBuilder queryBuilder = mSellCheckEntityDao.queryBuilder();
        return queryBuilder.where(queryBuilder.and(SellCheckEntityDao.Properties.DishId.eq(dishId), SellCheckEntityDao.Properties.IsSellOut.eq(1))).buildCount().count() > 0;
    }

    /**
     * 判断商品分类是否有上级分类
     *
     * @param dishTypeId
     * @param type
     * @return
     */
    public boolean isHasChild(String dishTypeId, int type) {
        if (dishTypeId == null) {
            return false;
        }
        QueryBuilder queryBuilder;
        switch (type) {
            case 0:
                queryBuilder = mDishTypeEntityDao.queryBuilder();
                return queryBuilder.where(queryBuilder.and(DishTypeEntityDao.Properties.IsHasParent.eq(1), DishTypeEntityDao.Properties.ParentId.eq(dishTypeId))).buildCount().count() > 0;
            case 1:
                queryBuilder = mTaocanTypeEntityDao.queryBuilder();
                return queryBuilder.where(queryBuilder.and(TaocanTypeEntityDao.Properties.IsHasParent.eq(1), TaocanTypeEntityDao.Properties.ParentId.eq(dishTypeId))).buildCount().count() > 0;
            default:
                return false;
        }
    }

    //清空估清表
    public void clearChing() {
        mSellCheckEntityDao.deleteAll();
    }

    //根据商品id获取商品名称
    public String getDishNameByDishId(String dishId) {
        QueryBuilder queryBuilder = mDishEntityDao.queryBuilder();
        DishEntity dishEntity = (DishEntity) queryBuilder.where(DishEntityDao.Properties.DishId.eq(dishId)).build().unique();
        if (dishEntity != null) {
            return dishEntity.getDishName();
        } else {
            return "";
        }
    }

    //根据员工id获取员工名称
    public String getEmployeeNameById(String employeeId) {
        if (employeeId == null) {
            return null;
        } else {
            QueryBuilder queryBuilder = mEmployeeEntityDao.queryBuilder();
            EmployeeEntity employeeEntity = (EmployeeEntity) queryBuilder.where(EmployeeEntityDao.Properties.EmployeeId.eq(employeeId)).build().unique();
            if (employeeEntity == null) {
                return null;
            } else {
                return employeeEntity.getEmployeeName();
            }
        }
    }

    /**
     * 获取所有订单
     *
     * @return
     */
    public ArrayList<OrderEntity> getAllUnSyncOrders() {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
        try {
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.OrderStatus.eq(1));
            orderEntities.addAll(queryBuilder.list());
        } catch (Exception e) {

        }
        return orderEntities;
    }

    //所有账单明细筛选
    public ArrayList<OrderEntity> getAllOrderEntity(String employeeId, int shift, String payModeId, int startDate, String type) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderType.notEq(2));
        queryBuilder = queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderType.notEq(2), OrderEntityDao.Properties.OrderStatus.eq(1)));
        if (employeeId != null) {
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CashierId.eq(employeeId));
        }
        if (shift != -1) {
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.IsShift.eq(shift));
        }
        if (startDate == -1) {
            //两日内
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CloseTime.gt(getYestodayStartMillis()));
        } else if (startDate == 0) {
            //今日
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CloseTime.gt(getTodayStartMillis()));
        } else if (startDate == 1) {
            //昨日
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CloseTime.between(getYestodayStartMillis(), getTodayStartMillis()));
        }
        //客单分类筛选
        if (type.equals("-1")) {
            //全部

        } else if (type.equals("0")) {
            //外卖单
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.OrderType.eq(1));
        } else {
            //正常单
            queryBuilder = queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderType.eq(0), OrderEntityDao.Properties.AreaId.eq(type)));
        }
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryBuilder.list());
        ArrayList<OrderEntity> results = new ArrayList<>();
        if (payModeId != null) {
            //筛选付款方式
            QueryBuilder queryBuilder1;
            for (OrderEntity orderEntity :
                    orderEntities) {
                queryBuilder1 = mPayModeEntityDao.queryBuilder();
                int count = (int) queryBuilder1.where(queryBuilder1.and(PayModeEntityDao.Properties.OrderId.eq(orderEntity.getOrderId()), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0), PayModeEntityDao.Properties.PaymentId.eq(payModeId))).buildCount().count();
                if (count > 0) {
                    results.add(orderEntity);
                }
            }
        } else {
            results.addAll(orderEntities);
        }
        return results;
    }

    //未结账单明细筛选
    public ArrayList<OrderEntity> getSomeUnOrderEntity(String employeeId, int shift, String payModeId, int startDate, String type) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderStatus.eq(0), OrderEntityDao.Properties.OrderType.notEq(2));
        if (employeeId != null) {
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CashierId.eq(employeeId));
        }
        if (shift != -1) {
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.IsShift.eq(shift));
        }
        if (startDate == -1) {
            //两日内

        } else if (startDate == 0) {
            //今日
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CloseTime.gt(getTodayStartMillis()));
        } else if (startDate == 1) {
            //昨日
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CloseTime.between(getYestodayStartMillis(), getTodayStartMillis()));
        }
        //客单分类筛选
        if (type.equals("-1")) {
            //全部

        } else if (type.equals("0")) {
            //外卖单
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.OrderType.eq(1));
        } else {
            //正常单
            queryBuilder = queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderType.eq(0), OrderEntityDao.Properties.AreaId.eq(type)));
        }
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryBuilder.list());
        ArrayList<OrderEntity> results = new ArrayList<>();
        if (payModeId != null) {
            //筛选付款方式
            QueryBuilder queryBuilder1;
            for (OrderEntity orderEntity :
                    orderEntities) {
                queryBuilder1 = mPayModeEntityDao.queryBuilder();
                int count = (int) queryBuilder1.where(queryBuilder1.and(PayModeEntityDao.Properties.OrderId.eq(orderEntity.getOrderId()), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0), PayModeEntityDao.Properties.PaymentId.eq(payModeId))).buildCount().count();
                if (count > 0) {
                    results.add(orderEntity);
                }
            }
        } else {
            results.addAll(orderEntities);
        }
        return results;
    }

    public StringBuilder getQueryStr(String employeeId, int shift, String payModeId, int startDate, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ORDER_ENTITY inner join PAY_MODE_ENTITY on ORDER_ENTITY.ORDER_ID=PAY_MODE_ENTITY.ORDER_ID");
        sb.append(" where ORDER_ENTITY.ORDER_STATUS=1");
        if (employeeId != null) {
            sb.append(" and ORDER_ENTITY.CASHIER_ID='" + employeeId + "'");
        }
        if (shift != -1) {
            sb.append(" and ORDER_ENTITY.IS_SHIFT=" + shift);
        }
        if (startDate == -1) {
            //两日内
            sb.append(" and ORDER_ENTITY.CLOSE_TIME>=" + getYestodayStartMillis());
        } else if (startDate == 0) {
            //今日
            sb.append(" and ORDER_ENTITY.CLOSE_TIME>=" + getTodayStartMillis());
        } else if (startDate == 1) {
            //昨日
            sb.append(" and ORDER_ENTITY.CLOSE_TIME>=" + getYestodayStartMillis());
            sb.append(" and ORDER_ENTITY.CLOSE_TIME<=" + getTodayStartMillis());
        }
        //客单分类筛选
        if (type.equals("-1")) {
        } else if (type.equals("0")) {
            //外卖单
            sb.append(" and ORDER_ENTITY.ORDER_TYPE=1");
        } else {
            //正常单
            sb.append(" and ORDER_ENTITY.ORDER_TYPE=0");
            sb.append(" and ORDER_ENTITY.AREA_ID='" + type + "'");
        }
        if (payModeId != null) {
            sb.append(" and PAY_MODE_ENTITY.PAYMENT_ID='" + payModeId + "'");
        }
        return sb;
    }

    public ArrayList<OrderEntity> getOrderByPage(String employeeId, int shift, String payModeId, int startDate, String type, int pageNumber) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (payModeId != null) {
            sb.append("select * from ORDER_ENTITY inner join PAY_MODE_ENTITY on ORDER_ENTITY.ORDER_ID=PAY_MODE_ENTITY.ORDER_ID");
            sb.append(" where ORDER_ENTITY.ORDER_STATUS=1");
            sb.append(" and PAY_MODE_ENTITY.PAYMENT_ID='" + payModeId + "'");
        } else {
            sb.append("select * from ORDER_ENTITY");
            sb.append(" where ORDER_ENTITY.ORDER_STATUS=1");
        }
        if (employeeId != null) {
            sb.append(" and ORDER_ENTITY.CASHIER_ID='" + employeeId + "'");
        }
        if (shift != -1) {
            sb.append(" and ORDER_ENTITY.IS_SHIFT=" + shift);
        }
        if (startDate == -1) {
            //两日内
            sb.append(" and ORDER_ENTITY.CLOSE_TIME>=" + getYestodayStartMillis());
        } else if (startDate == 0) {
            //今日
            sb.append(" and ORDER_ENTITY.CLOSE_TIME>=" + getTodayStartMillis());
        } else if (startDate == 1) {
            //昨日
            sb.append(" and ORDER_ENTITY.CLOSE_TIME>=" + getYestodayStartMillis());
            sb.append(" and ORDER_ENTITY.CLOSE_TIME<=" + getTodayStartMillis());
        }
        //客单分类筛选
        if (type.equals("-1")) {
        } else if (type.equals("0")) {
            //外卖单
            sb.append(" and ORDER_ENTITY.ORDER_TYPE=1");
        } else {
            //正常单
            sb.append(" and ORDER_ENTITY.ORDER_TYPE=0");
            sb.append(" and ORDER_ENTITY.AREA_ID='" + type + "'");
        }
        sb.append(" order by ORDER_ENTITY.CLOSE_TIME DESC");
        if (pageNumber > 0) {
            sb.append(" limit " + (pageNumber - 1) * 24 + "," + pageNumber * 24);
        }
        ArrayList<String> orderDishIds = new ArrayList<>();
        Cursor c = mDaoSession.getDatabase().rawQuery(sb.toString(), null);
        while (c.moveToNext()) {
            if (!orderDishIds.contains(c.getString(c.getColumnIndex("ORDER_ID")))) {
                OrderEntity orderEntity = getOneOrderEntity(c.getString(c.getColumnIndex("ORDER_ID")));
                orderEntities.add(orderEntity);
                orderDishIds.add(c.getString(c.getColumnIndex("ORDER_ID")));
            }
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        return orderEntities;
    }

    /**
     * 获取分类下商品数量统计结果
     *
     * @param orderId
     * @param dishId
     * @return
     */
    public double getOneDishCountGroup(String orderId, String dishId) {
        double result = 0;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select SUM(DISH_COUNT) from ORDER_DISH_ENTITY WHERE ORDER_ID = '");
            sb.append(orderId);
            sb.append("' AND DISH_ID = '");
            sb.append(dishId);
            sb.append("'");
            Cursor c = mDaoSession.getDatabase().rawQuery(sb.toString(), null);
            while (c.moveToNext()) {
                result = c.getDouble(0);
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * 获取分类下商品数量统计结果
     *
     * @param orderId
     * @param typeId
     * @return
     */
    public Map<String, Double> getDishCountGroup(String orderId, String typeId) {
        Map<String, Double> results = new HashMap<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select DISH_ID,SUM(DISH_COUNT) from ORDER_DISH_ENTITY WHERE ORDER_ID = '");
            sb.append(orderId);
            sb.append("' AND DISH_TYPE_ID = '");
            sb.append(typeId);
            sb.append("' GROUP BY DISH_ID");
            Cursor c = mDaoSession.getDatabase().rawQuery(sb.toString(), null);
            while (c.moveToNext()) {
                if (!results.containsKey(c.getString(0))) {
                    results.put(c.getString(0), c.getDouble(1));
                }
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {

        }
        return results;
    }

    //获取所有
    public ArrayList<String> getAllOrderIds() {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ORDER_ENTITY inner join PAY_MODE_ENTITY on ORDER_ENTITY.ORDER_ID=PAY_MODE_ENTITY.ORDER_ID");
        sb.append(" where ORDER_ENTITY.ORDER_STATUS=1");
        ArrayList<String> orderDishIds = new ArrayList<>();
        Cursor c = mDaoSession.getDatabase().rawQuery(sb.toString(), null);
        while (c.moveToNext()) {
            if (!orderDishIds.contains(c.getString(c.getColumnIndex("ORDER_ID")))) {
                orderDishIds.add(c.getString(c.getColumnIndex("ORDER_ID")));
            }
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        return orderDishIds;
    }

    //已结账单明细筛选
    public ArrayList<OrderEntity> getSomeOrderEntity(String employeeId, int shift, String payModeId, int startDate, String type) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderStatus.eq(1)).orderDesc(OrderEntityDao.Properties.CloseTime);
        if (employeeId != null) {
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CashierId.eq(employeeId));
        }
        if (shift != -1) {
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.IsShift.eq(shift));
        }
        if (startDate == -1) {
            //两日内
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CloseTime.gt(getYestodayStartMillis()));
        } else if (startDate == 0) {
            //今日
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CloseTime.gt(getTodayStartMillis()));
        } else if (startDate == 1) {
            //昨日
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.CloseTime.between(getYestodayStartMillis(), getTodayStartMillis()));
        }
        //客单分类筛选
        if (type.equals("-1")) {
            //全部
        } else if (type.equals("0")) {
            //外卖单
            queryBuilder = queryBuilder.where(OrderEntityDao.Properties.OrderType.eq(1));
        } else {
            //正常单
            queryBuilder = queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderType.eq(0), OrderEntityDao.Properties.AreaId.eq(type)));
        }
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryBuilder.list());
        ArrayList<OrderEntity> results = new ArrayList<>();
        if (payModeId != null) {
            //筛选付款方式
            QueryBuilder queryBuilder1;
            int count;
            for (OrderEntity orderEntity :
                    orderEntities) {
                queryBuilder1 = mPayModeEntityDao.queryBuilder();
                count = (int) queryBuilder1.where(queryBuilder1.and(PayModeEntityDao.Properties.OrderId.eq(orderEntity.getOrderId()), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0), PayModeEntityDao.Properties.PaymentId.eq(payModeId))).buildCount().count();
                if (count > 0) {
                    results.add(orderEntity);
                }
            }
        } else {
            results.addAll(orderEntities);
        }
        return results;
    }

    //筛选账单后按收银员分类
    public ArrayList<String> getCashierIds(String employeeId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = getSomeOrderEntity(employeeId, shift, payModeId, startDate, type);
        ArrayList<String> cashierIds = new ArrayList<>();
        String cashierId;
        for (int i = 0; i < orderEntities.size(); i++) {
            cashierId = orderEntities.get(i).getCashierId();
            if (cashierId != null && !cashierIds.contains(cashierId)) {
                cashierIds.add(cashierId);
            }
        }
        return cashierIds;
    }

    //获取已结账单数
    public int getCashieredCount(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeOrderEntity(cashierId, shift, payModeId, startDate, type));
        return orderEntities.size();
    }

    //获取已结账金额
    public String getCashieredMoney(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeOrderEntity(cashierId, shift, payModeId, startDate, type));
        int result = 0;
        for (OrderEntity orderEntity :
                orderEntities) {
            result += orderEntity.getTotalMoney();
        }
        return AmountUtils.changeF2Y(result);
    }

    //获取未结账单数
    public int getUnCashierdOrderCount(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeUnOrderEntity(cashierId, shift, payModeId, startDate, type));
        return orderEntities.size();
    }

    //获取未结账金额
    public String getUnCashieredMoney(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeUnOrderEntity(cashierId, shift, payModeId, startDate, type));
        String result = "0";
        for (int i = 0; i < orderEntities.size(); i++) {
            result = AmountUtils.add(result, getBillMoneyByOrderId(orderEntities.get(i).getOrderId()) + "");
        }
        return result;
    }

    //获取账单总数
    public int getAllOrderCount(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        int unOrderedCount = getUnCashierdOrderCount(context, cashierId, shift, payModeId, startDate, type);
        int orderedCount = getCashieredCount(context, cashierId, shift, payModeId, startDate, type);
        return unOrderedCount + orderedCount;
    }

    //获取账单总金额
    public String getAllOrderMoney(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeUnOrderEntity(cashierId, shift, payModeId, startDate, type));
        String result = "0";
        for (int i = 0; i < orderEntities.size(); i++) {
            result = AmountUtils.add(result, getBillMoneyByOrderId(orderEntities.get(i).getOrderId()) + "");
        }
        orderEntities.clear();
        orderEntities = getSomeOrderEntity(cashierId, shift, payModeId, startDate, type);
        for (OrderEntity orderEntity :
                orderEntities) {
            result = AmountUtils.add(result, AmountUtils.multiply(orderEntity.getTotalMoney() + "", "0.01"));
        }
        return result;
    }

    /**
     * 获取应收总金额
     *
     * @param context
     * @param cashierId
     * @param shift
     * @param payModeId
     * @param startDate
     * @return
     */
    public String getAllOrderReceivableMoney(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeOrderEntity(cashierId, shift, payModeId, startDate, type));
        int result = 0;
        for (OrderEntity orderEntity :
                orderEntities) {
            try {
                result += orderEntity.getTotalMoney();
            } catch (Exception e) {
                e.printStackTrace();
                result += orderEntity.getTotalMoney();
            }
        }
        return AmountUtils.changeF2Y(result);
    }

    /**
     * 获取实收金额
     *
     * @param context
     * @param cashierId
     * @param shift
     * @param payModeId
     * @param startDate
     * @return
     */
    public String getAllOrderReceivedMoney(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        payModeEntities.addAll(getAllPayMode(context, cashierId, shift, payModeId, startDate, type));
        String result = "0";
        for (PayModeEntity payModeEntity :
                payModeEntities) {
            result = AmountUtils.add(result, payModeEntity.getPayMoney() + "");
        }
        return result;
    }

    /**
     * 获取抹零总金额
     *
     * @param context
     * @param cashierId
     * @param shift
     * @param payModeId
     * @param startDate
     * @return
     */
    public String getAllOrderTreatmentMoney(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeOrderEntity(cashierId, shift, payModeId, startDate, type));
        int result = 0;
        for (OrderEntity orderEntity :
                orderEntities) {
            result += orderEntity.getSelfTreatMoney() + orderEntity.getTreatmentMoney();
        }
        return AmountUtils.changeF2Y(result);
    }

    /**
     * 获取抹零总金额
     *
     * @param context
     * @param cashierId
     * @param shift
     * @param payModeId
     * @param startDate
     * @return
     */
    public String getAllOrderPresentMoney(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeOrderEntity(cashierId, shift, payModeId, startDate, type));
        int result = 0;
        for (OrderEntity orderEntity :
                orderEntities) {
            result += orderEntity.getPresentMoney();
        }
        return AmountUtils.changeF2Y(result);
    }

    /**
     * 获取不吉利尾数的总金额
     *
     * @param context
     * @param cashierId
     * @param shift
     * @param payModeId
     * @param startDate
     * @return
     */
    public String getAllOrderMantissaMoney(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeOrderEntity(cashierId, shift, payModeId, startDate, type));
        int result = 0;
        for (OrderEntity orderEntity :
                orderEntities) {
            result += orderEntity.getMantissaMoney();
        }
        return AmountUtils.changeF2Y(result);
    }

    /**
     * 获取折扣总金额
     *
     * @param context
     * @param cashierId
     * @param shift
     * @param payModeId
     * @param startDate
     * @return
     */
    public String[] getAllOrderDiscountMoney(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        int[] result = {0, 0};
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getSomeOrderEntity(cashierId, shift, payModeId, startDate, type));
        try {
            int discountMoney, vipDiscountMoney, couponDiscountMoney;
            for (OrderEntity orderEntity :
                    orderEntities) {
                discountMoney = orderEntity.getDiscountMoney();
                vipDiscountMoney = orderEntity.getVipDiscountMoney();
                couponDiscountMoney = orderEntity.getCouponDiscountMoney();
                result[0] += discountMoney + vipDiscountMoney;
                result[1] += couponDiscountMoney;
            }
        } catch (Exception e) {

        }
        String[] resultStr = {"", ""};
        resultStr[0] = AmountUtils.changeF2Y(result[0]);
        resultStr[1] = AmountUtils.changeF2Y(result[1]);
        return resultStr;
    }

    //获取所有的支付方式
    public ArrayList<PayModeEntity> getAllPayMode(Context context, String cashierId, int shift, String payModeId, int startDate, String type) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(getAllOrderEntity(cashierId, shift, payModeId, startDate, type));
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        for (int i = 0; i < orderEntities.size(); i++) {
            payModeEntities.addAll(getPayModeByOrderId(orderEntities.get(i).getOrderId()));
        }
        //合并相同的支付方式
        ArrayList<PayModeEntity> results = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        int position;
        for (PayModeEntity payMode :
                payModeEntities) {
            if (ids.contains(payMode.getPaymentId())) {
                position = ids.indexOf(payMode.getPaymentId());
                PayModeEntity payModeEntity = results.get(position);
                payModeEntity.setPayMoney(Float.parseFloat(AmountUtils.add(payModeEntity.getPayMoney() + "", payMode.getPayMoney() + "")));
            } else {
                PayModeEntity payModeEntity = new PayModeEntity(payMode);
                ids.add(payModeEntity.getPaymentId());
                results.add(payModeEntity);
            }
        }
        return results;
    }

    //获取当日凌晨的时间戳
    public long getTodayStartMillis() {
        Calendar cal = Calendar.getInstance();
        long currentMillis = cal.getTimeInMillis();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        //凌晨00:00:00
        cal.setTimeInMillis(currentMillis - millisecond);
        return cal.getTimeInMillis();
    }

    //获取昨日凌晨的时间戳
    public long getYestodayStartMillis() {
        long millisecond = 24 * 60 * 60 * 1000;
        return getTodayStartMillis() - millisecond;
    }

    //结账完毕
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean cashierOver(String orderId) {
        try {
            //修改订单状态为已结账
            QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
            OrderEntity orderEntity = (OrderEntity) queryBuilder.where(OrderEntityDao.Properties.OrderId.eq(orderId)).build().unique();
            orderEntity.setOrderStatus(1);//已结账
            if (orderEntity.getIsReturnOrder() != null && orderEntity.getIsReturnOrder() == 1) {
                orderEntity.setCloseTime(orderEntity.getCloseTime());//设置结账时间
            } else {
                orderEntity.setCloseTime(System.currentTimeMillis());//设置结账时间
            }
            ArrayMap<String, Integer> map = getYHMoneyMapInt(orderId);
            int discountTotalMoney = getYHMoney(orderId);
            double billMoney = getBillMoneyByOrderId(orderId, 1);//合计
            double receivableMoney = getReceivableMoney(orderId, 1);//应收
            String billMoneyStr = AmountUtils.changeY2F(billMoney + "");
            String receivableMoneyStr = AmountUtils.changeY2F(receivableMoney + "");
            int billMoney2 = 0;
            int receivableMoney2 = 0;
            int couponMoney = map.get("couponDiscount");
            int treatMoney = map.get("treatMentMoney");
            int mantissaMoney = map.get("mantissaMoney");
            int selfMoney = map.get("selfMoney");
            int presentMoney = map.get("presentMoney");
            int discountMoney = map.get("discountMoney");
            int vipDiscountMoney = map.get("vipDiscount");
            try {
                double billMoney1 = AmountUtils.changeF2Y(billMoneyStr).doubleValue();
                billMoney2 = AmountUtils.changeY2F(billMoney1);
                double receivableMoney1 = AmountUtils.changeF2Y(receivableMoneyStr).doubleValue();
                receivableMoney2 = AmountUtils.changeY2F(receivableMoney1);
            } catch (Exception e) {
                e.printStackTrace();
                billMoney2 = Integer.parseInt(billMoneyStr);
                receivableMoney2 = Integer.parseInt(receivableMoneyStr);
            }
            orderEntity.setCloseMoney(receivableMoney2);
            orderEntity.setTotalMoney(billMoney2);
            orderEntity.setSelfTreatMoney(selfMoney);
            orderEntity.setVipDiscountMoney(vipDiscountMoney);
            orderEntity.setCouponDiscountMoney(couponMoney);
            orderEntity.setMantissaMoney(mantissaMoney);
            orderEntity.setTreatmentMoney(treatMoney);
            orderEntity.setPresentMoney(presentMoney);
            orderEntity.setDiscountMoney(discountMoney);
            orderEntity.setDiscountTotalMoney(discountTotalMoney);
            orderEntity.setInvoiceMoney(0);
            mOrderEntityDao.insertOrReplace(orderEntity);
            if (orderEntity.getIsUpload() != null && orderEntity.getIsUpload() == 1) {
                insertUploadData(orderId, orderId, 14);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 反结账
     *
     * @param orderEntity
     * @param returnOrderReason
     */
    public void returnOrder(OrderEntity orderEntity, String returnOrderReason) {
        orderEntity.setOrderStatus(0);
        orderEntity.setIsReturnOrder(1);
        orderEntity.setReturnOrderReason(returnOrderReason);
        mOrderEntityDao.insertOrReplace(orderEntity);
        updateTableStatus(orderEntity.getTableId());
    }

    //根据账单id获取付款统计
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPayModeStrByOrderId(String orderId) {
        ArrayList<PayModeEntity> payModeEntities = getPayModeByOrderId(orderId);
        ArrayMap<String, Float> paymentIdMap = new ArrayMap<>();
        ArrayMap<String, String> paymentNameMap = new ArrayMap<>();
        String paymentId;
        float payMoney;
        String paymentName;
        float curMoney;
        for (int i = 0; i < payModeEntities.size(); i++) {
            paymentId = payModeEntities.get(i).getPaymentId();
            payMoney = payModeEntities.get(i).getPayMoney();
            paymentName = payModeEntities.get(i).getPaymentName();
            if (paymentIdMap.containsKey(paymentId)) {
                curMoney = paymentIdMap.get(paymentId);
                paymentIdMap.put(paymentId, Float.parseFloat(AmountUtils.add(curMoney + "", payMoney + "")));
            } else {
                paymentIdMap.put(paymentId, payMoney);
                paymentNameMap.put(paymentId, paymentName);
            }
        }
        String result = "";
        Iterator iter = paymentIdMap.entrySet().iterator();
        Iterator iter1 = paymentNameMap.entrySet().iterator();
        while (iter.hasNext() && iter1.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Map.Entry entry1 = (Map.Entry) iter1.next();
            Float price = (Float) entry.getValue();
            String name = (String) entry1.getValue();
            result += name + price + " ";
        }
        return result;
    }

    //获取账单汇总总金额
    public float getOrderCollectionSpend(String employeeId, int shift, String payModeId, int startDate, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("select sum(ORDER_ENTITY.TOTAL_MONEY) from ORDER_ENTITY inner join PAY_MODE_ENTITY on ORDER_ENTITY.ORDER_ID=PAY_MODE_ENTITY.ORDER_ID");
        sb.append(" where ORDER_ENTITY.ORDER_STATUS=1");
        if (employeeId != null) {
            sb.append(" and ORDER_ENTITY.CASHIER_ID='" + employeeId + "'");
        }
        if (shift != -1) {
            sb.append(" and ORDER_ENTITY.IS_SHIFT=" + shift);
        }
        if (startDate == -1) {
            //两日内
            sb.append(" and ORDER_ENTITY.CLOSE_TIME>=" + getYestodayStartMillis());
        } else if (startDate == 0) {
            //今日
            sb.append(" and ORDER_ENTITY.CLOSE_TIME>=" + getTodayStartMillis());
        } else if (startDate == 1) {
            //昨日
            sb.append(" and ORDER_ENTITY.CLOSE_TIME>=" + getYestodayStartMillis());
            sb.append(" and ORDER_ENTITY.CLOSE_TIME<=" + getTodayStartMillis());
        }
        //客单分类筛选
        if (type.equals("-1")) {
        } else if (type.equals("0")) {
            //外卖单
            sb.append(" and ORDER_ENTITY.ORDER_TYPE=1");
        } else {
            //正常单
            sb.append(" and ORDER_ENTITY.ORDER_TYPE=0");
            sb.append(" and ORDER_ENTITY.AREA_ID='" + type + "'");
        }
        if (payModeId != null) {
            sb.append(" and PAY_MODE_ENTITY.PAYMENT_ID='" + payModeId + "'");
        }
        ArrayList<OrderEntity> orderEntities = getSomeOrderEntity(employeeId, shift, payModeId, startDate, type);
        float totalSpend = 0;
        for (int i = 0; i < orderEntities.size(); i++) {
            totalSpend += getBillMoneyByOrderId(orderEntities.get(i).getOrderId(), 1);
        }
        return CustomMethod.decimalFloat(totalSpend);
    }

    //获取分类汇总的商品分类数据
    public ArrayList<DishTypeCollectionItemBean> getTypeCollectionDishType(String employeeId, int shift, String payModeId, int startDate, String type) {
        ArrayList<DishTypeCollectionItemBean> results = new ArrayList<>();
        ArrayList<OrderEntity> orderEntities = getSomeOrderEntity(employeeId, shift, payModeId, startDate, type);
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        for (int i = 0; i < orderEntities.size(); i++) {
            orderDishEntities.addAll(queryOrderedDish(orderEntities.get(i).getOrderId(), 1));
        }
        for (OrderDishEntity orderDishEntity :
                orderDishEntities) {
            boolean b = true;
            for (DishTypeCollectionItemBean itemBean :
                    results) {
                if (itemBean.getDishTypeId().equals(orderDishEntity.getDishTypeId())) {
                    b = false;
                    itemBean.setDishTypeCount(AmountUtils.add(itemBean.getDishTypeCount(), orderDishEntity.getDishCount()));
                    itemBean.setDishTypeMoney(itemBean.getDishTypeMoney() + orderDishEntity.getDishPrice());
                }
            }
            if (b) {
                DishTypeCollectionItemBean itemBean = new DishTypeCollectionItemBean();
                itemBean.setDishTypeId(orderDishEntity.getDishTypeId());
                itemBean.setDishTypeName(orderDishEntity.getDishTypeName());
                itemBean.setDishTypeCount(orderDishEntity.getDishCount());
                itemBean.setDishTypeMoney(CustomMethod.decimalFloat(orderDishEntity.getDishPrice()));
                results.add(itemBean);
            }
        }
        return results;
    }

    //获取分类数量
    public int getTypeCollectionDishTypeCount(String employeeId, int shift, String payModeId, int startDate, String dishTypeId, String type) {
        int results = 0;
        ArrayList<OrderEntity> orderEntities = getSomeOrderEntity(employeeId, shift, payModeId, startDate, type);
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        for (int i = 0; i < orderEntities.size(); i++) {
            orderDishEntities.addAll(queryOrderDishEntity(orderEntities.get(i).getOrderId(), 1));
        }
        for (int i = 0; i < orderDishEntities.size(); i++) {
            if (dishTypeId.equals(orderDishEntities.get(i).getDishTypeId())) {
                results += orderDishEntities.get(i).getDishCount();
            }
        }
        return results;
    }

    //获取分类小计
    public float getTypeCollectionDishTypeMoney(String employeeId, int shift, String payModeId, int startDate, String dishTypeId, String type) {
        float results = 0;
        ArrayList<OrderEntity> orderEntities = getSomeOrderEntity(employeeId, shift, payModeId, startDate, type);
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        for (int i = 0; i < orderEntities.size(); i++) {
            orderDishEntities.addAll(queryOrderDishEntity(orderEntities.get(i).getOrderId(), 1));
        }
        for (int i = 0; i < orderDishEntities.size(); i++) {
            if (dishTypeId.equals(orderDishEntities.get(i).getDishTypeId())) {
                results += orderDishEntities.get(i).getDishCount() * orderDishEntities.get(i).getDishPrice();
            }
        }
        return CustomMethod.decimalFloat(results);
    }

    //获取分类小计
    public int getTypeCollectionDishTypeTotal(String employeeId, int shift, String payModeId, int startDate, String type) {
        int results = 0;
        ArrayList<OrderEntity> orderEntities = getSomeOrderEntity(employeeId, shift, payModeId, startDate, type);
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        for (int i = 0; i < orderEntities.size(); i++) {
            orderDishEntities.addAll(queryOrderedDish(orderEntities.get(i).getOrderId(), 1));
        }
        for (int i = 0; i < orderDishEntities.size(); i++) {
            results += orderDishEntities.get(i).getDishCount();
        }
        return results;
    }

    //获取分类小计
    public float getTypeCollectionDishTypeTotalMoney(String employeeId, int shift, String payModeId, int startDate, String type) {
        float results = 0;
        ArrayList<OrderEntity> orderEntities = getSomeOrderEntity(employeeId, shift, payModeId, startDate, type);
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        for (int i = 0; i < orderEntities.size(); i++) {
            orderDishEntities.addAll(queryOrderedDish(orderEntities.get(i).getOrderId(), 1));
        }
        for (int i = 0; i < orderDishEntities.size(); i++) {
            results += orderDishEntities.get(i).getDishPrice();
        }
        return CustomMethod.decimalFloat(results);
    }

    //根据商品分类id获取商品分类详细信息
    public DishTypeEntity getDishTypeByDishTypeId(String dishTypeId) {
        if (dishTypeId == null) {
            return null;
        } else {
            QueryBuilder queryBuilder = mDishTypeEntityDao.queryBuilder().where(DishTypeEntityDao.Properties.DishTypeId.eq(dishTypeId));
            DishTypeEntity dishTypeEntity = (DishTypeEntity) queryBuilder.build().unique();
            return dishTypeEntity;
        }
    }

    private boolean checkParent(DishTypeModel dishTypeModel, String dishTypeId, int type) {
        if (dishTypeModel.getDishTypeModelId().equals(dishTypeId)) {
            return true;
        } else {
            switch (type) {
                case 0://非套餐
                    DishTypeEntity dishTypeEntity = getDishTypeByDishTypeId(dishTypeId);
                    if (dishTypeEntity != null && dishTypeEntity.getIsHasParent() == 1) {
                        return checkParent(dishTypeModel, dishTypeEntity.getParentId(), type);
                    } else {
                        return false;
                    }
                case 1://套餐
                    TaocanTypeEntity taocanTypeEntity = getTaocanTypeById(dishTypeId);
                    if (taocanTypeEntity != null && taocanTypeEntity.getIsHasParent() == 1) {
                        return checkParent(dishTypeModel, taocanTypeEntity.getParentId(), type);
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        }
    }

    //根据商品分类获取销售统计
    public ArrayList<DishModel> getStatisticDishByDishTypeId(Context context, DishTypeModel dishTypeModel, int shift, int date, String type) {
        ArrayList<DishModel> dishModels = new ArrayList<>();
        ArrayList<OrderEntity> orderEntities = getSomeOrderEntity(null, shift, null, date, type);//获取符合条件的所有账单
        OrderEntity orderEntity;
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        OrderDishEntity orderDishEntity;
        for (int i = 0; i < orderEntities.size(); i++) {
            orderEntity = orderEntities.get(i);
            orderDishEntities.clear();
            orderDishEntities.addAll(queryOrderedDish(orderEntity.getOrderId(), 1));//获取账单中点的菜品
            for (int j = 0; j < orderDishEntities.size(); j++) {
                orderDishEntity = orderDishEntities.get(j);
                try {
                    DishModel dishModel = new DishModel();
                    dishModel.setCount(orderDishEntity.getDishCount());
                    dishModel.setDishModelId(orderDishEntity.getDishId());
                    dishModel.setDishModelName(orderDishEntity.getDishName());
                    dishModel.setDishModelTypeId(orderDishEntity.getDishTypeId());
                    dishModel.setDishModelType(orderDishEntity.getType());
                    if (dishTypeModel.getDishTypeModelType() == dishModel.getDishModelType() && checkParent(dishTypeModel, dishModel.getDishModelTypeId(), dishModel.getDishModelType())) {
                        dishModels.add(dishModel);
                    }
                } catch (Exception e) {

                }
            }
        }
        return dishModels;
    }

    /**
     * 获取某个分类下已点商品
     *
     * @param context
     * @param dishTypeModel
     * @param shift
     * @param date
     * @return
     */
    public ArrayList<DishModel> getSingleStatisticDish(Context context, DishTypeModel dishTypeModel, int shift, int date) {
        ArrayList<DishModel> results = new ArrayList<>();
        ArrayList<DishModel> dishModels = getStatisticDishByDishTypeId(context, dishTypeModel, shift, date, "-1");
        boolean b = true;
        for (int i = 0; i < dishModels.size(); i++) {
            b = true;
            for (int j = 0; j < results.size(); j++) {
                if (results.get(j).getDishModelId().equals(dishModels.get(i).getDishModelId())) {
                    b = false;
                    results.get(j).setCount(AmountUtils.add(results.get(j).getCount(), dishModels.get(i).getCount()));
                }
            }
            if (b) {
                DishModel dishModel = new DishModel();
                dishModel.setDishModelId(dishModels.get(i).getDishModelId());
                dishModel.setDishModelName(dishModels.get(i).getDishModelName());
                dishModel.setCount(dishModels.get(i).getCount());
                dishModel.setDishModelType(dishModels.get(i).getDishModelType());
                dishModel.setDishModelTypeId(dishModels.get(i).getDishModelTypeId());
                results.add(dishModel);
            }
        }
        return results;
    }

    //获取排号记录
    public ArrayList<ArrangeEntity> getSomeArrange(int status) {
        if (status == 1) {
            QueryBuilder queryBuilder = mArrangeEntityDao.queryBuilder();
            return (ArrayList<ArrangeEntity>) queryBuilder.where(ArrangeEntityDao.Properties.ArrangeStatus.eq(status)).orderDesc(ArrangeEntityDao.Properties.SignTime).list();
        } else if (status == 0) {
            QueryBuilder queryBuilder = mArrangeEntityDao.queryBuilder();
            return (ArrayList<ArrangeEntity>) queryBuilder.where(ArrangeEntityDao.Properties.ArrangeStatus.eq(status)).orderAsc(ArrangeEntityDao.Properties.ArrangeNumber).list();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 放弃排号
     *
     * @param arrangeId
     */
    public void deleteArrange(String arrangeId) {
        mArrangeEntityDao.queryBuilder().where(ArrangeEntityDao.Properties.ArrangeId.eq(arrangeId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    //插入排号记录
    public void insertArrange(QueueUserModel queueUserModel) {
        try {
            int count = (int) mArrangeEntityDao.queryBuilder().where(ArrangeEntityDao.Properties.ArrangeId.eq(queueUserModel.getId())).buildCount().count();
            if (count > 0) {

            } else {
                ArrangeEntity arrangeEntity = new ArrangeEntity();
                arrangeEntity.setArrangeId(queueUserModel.getId());
                arrangeEntity.setArrangeStatus(0);
                arrangeEntity.setRemainCount(0);
                arrangeEntity.setTel(queueUserModel.getMobile());
                arrangeEntity.setSignTime(queueUserModel.getCreateTime().getTime());
                arrangeEntity.setArrangeNumber(queueUserModel.getViewNo());
                arrangeEntity.setMealPeople(queueUserModel.getPeople());
                mArrangeEntityDao.insertOrReplace(arrangeEntity);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    //获取排号记录的总数
    public String getArrangeCount() {
        char c = 'A';
        int count = getSomeArrange(0).size() + 1;
        c = (char) (c + count / 99);
        if (count % 99 < 10) {
            return c + "0" + count % 99;
        } else {
            return c + String.valueOf(count % 99);
        }
    }

    //叫号
    public void callArrange(ArrangeEntity arrangeEntity) {
        arrangeEntity.setRemainCount(arrangeEntity.getRemainCount() + 1);
        mArrangeEntityDao.insertOrReplace(arrangeEntity);
    }

    //完成叫号
    public void confirmArrange(ArrangeEntity arrangeEntity) {
        arrangeEntity.setArrangeStatus(1);
        mArrangeEntityDao.insertOrReplace(arrangeEntity);
    }

    //根据员工账号和密码获取员工
    public EmployeeEntity employeeLogin(String loginName, String loginPsd) {
        QueryBuilder queryBuilder = mEmployeeEntityDao.queryBuilder();
        return (EmployeeEntity) queryBuilder.where(queryBuilder.and(EmployeeEntityDao.Properties.LoginName.eq(loginName), EmployeeEntityDao.Properties.LoginPsd.eq(loginPsd))).build().unique();
    }

    //获取系统通知
    public ArrayList<SystemMessageEntity> getSystemMessages() {
        return (ArrayList<SystemMessageEntity>) mSystemMessageEntityDao.queryBuilder().orderDesc(SystemMessageEntityDao.Properties.SystemTime).list();
    }

    //获取店家通知
    public ArrayList<StoreMessageEntity> getStoreMessages() {
        return (ArrayList<StoreMessageEntity>) mStoreMessageEntityDao.queryBuilder().orderDesc(StoreMessageEntityDao.Properties.StoreTime).list();
    }

    //获取微信通知
    public ArrayList<WXMessageEntity> getWXMessages() {
        ArrayList<WXMessageEntity> results = new ArrayList<>();
        ArrayList<WXMessageEntity> wxMessageEntities = new ArrayList<>();
        wxMessageEntities.addAll(mWXMessageEntityDao.queryBuilder().orderDesc(WXMessageEntityDao.Properties.WxTime).list());
        OrderEntity orderEntity;
        for (WXMessageEntity wxMessage :
                wxMessageEntities) {
            try {
                if (wxMessage.getWxType() == 0) {
                    RemindBean remindBean = JSON.parseObject(wxMessage.getWxContent(), RemindBean.class);
                    orderEntity = getOneOrderEntity(remindBean.getOrderId());
                    if (orderEntity != null) {
                        results.add(wxMessage);
                    } else {
                        mWXMessageEntityDao.queryBuilder().where(WXMessageEntityDao.Properties.WxMessageId.eq(wxMessage.getWxMessageId())).buildDelete().executeDeleteWithoutDetachingEntities();
                    }
                } else {
                    results.add(wxMessage);
                }
            } catch (Exception e) {

            }
        }
        return results;
    }

    /**
     * 清空所有已读微信消息
     */
    public void clearWXMessage() {
        mWXMessageEntityDao.queryBuilder().where(WXMessageEntityDao.Properties.IsRead.eq(1)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    //插入系统通知
    public void insertSystemMessage(SystemMessageEntity systemMessageEntity) {
        mSystemMessageEntityDao.insertOrReplace(systemMessageEntity);
    }

    /**
     * 清空所有已读系统消息
     */
    public void clearSystemMessage() {
        mSystemMessageEntityDao.queryBuilder().where(SystemMessageEntityDao.Properties.IsRead.eq(1)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    //插入商家通知
    public void insertStoreMessage(StoreMessageEntity storeMessageEntity) {
        mStoreMessageEntityDao.insertOrReplace(storeMessageEntity);
    }

    /**
     * 清空所有已读商家消息
     */
    public void clearStoreMessage() {
        mStoreMessageEntityDao.queryBuilder().where(StoreMessageEntityDao.Properties.IsRead.eq(1)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    //插入微信通知
    public void insertWXMessage(WXMessageEntity wxMessageEntity) {
        try {
            mWXMessageEntityDao.insertOrReplace(wxMessageEntity);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    //修改系统通知为已读
    public void changeSystemReaded(SystemMessageEntity systemMessageEntity) {
        systemMessageEntity.setIsRead(1);
        mSystemMessageEntityDao.insertOrReplace(systemMessageEntity);
    }

    //修改商家通知为已读
    public void changeStoreReaded(StoreMessageEntity storeMessageEntity) {
        storeMessageEntity.setIsRead(1);
        mStoreMessageEntityDao.insertOrReplace(storeMessageEntity);
    }

    //修改微信通知为已读
    public void changeWXReaded(WXMessageEntity wxMessageEntity) {
        wxMessageEntity.setIsRead(1);
        mWXMessageEntityDao.insertOrReplace(wxMessageEntity);
    }

    //获取未读消息数
    public int getUnreadMessageCount() {
        int systemUnreadCount = (int) mSystemMessageEntityDao.queryBuilder().where(SystemMessageEntityDao.Properties.IsRead.eq(0)).buildCount().count();
        int storeUnreadCount = (int) mStoreMessageEntityDao.queryBuilder().where(StoreMessageEntityDao.Properties.IsRead.eq(0)).buildCount().count();
        int wxUnreadCount = (int) mWXMessageEntityDao.queryBuilder().where(WXMessageEntityDao.Properties.IsRead.eq(0)).buildCount().count();
        return systemUnreadCount + storeUnreadCount + wxUnreadCount;
    }

    //根据orderdishid修改数量
    public OrderDishEntity changeCountByOrderDishId(String orderDishId, int changeCount) {
        OrderDishEntity orderDishEntity = mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishId)).build().unique();
        try {
            double curCount = orderDishEntity.getDishCount();
            if (curCount + changeCount >= 1) {
                orderDishEntity.setDishCount(curCount + changeCount);
                if (orderDishEntity.getType() == 0) {
                    //非套餐
                    orderDishEntity.setDishPrice(getOrderDishPrice(orderDishEntity.getDishId(), orderDishEntity.getPracticeId(), orderDishEntity.getSpecifyId(), curCount + changeCount, orderDishId));
                } else {
                    //套餐
                    orderDishEntity.setDishPrice(Float.parseFloat(AmountUtils.multiply(getTaocanPrice(orderDishEntity), orderDishEntity.getDishCount()) + ""));
                }
                mOrderDishEntityDao.insertOrReplace(orderDishEntity);
            }
        } catch (Exception e) {

        }
        return orderDishEntity;
    }

    /**
     * 根据打折记录id删除对应的部分打折商品记录
     *
     * @param discountHistoryId
     */
    public void clearSomeDiscountGoods(String discountHistoryId) {
        mSomeDiscountGoodsEntityDao.queryBuilder().where(SomeDiscountGoodsEntityDao.Properties.DiscountHistoryId.eq(discountHistoryId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 根据打折记录id删除对应的打折记录
     *
     * @param discountHistoryId
     */
    public void deleteDiscountHistory(String discountHistoryId) {
        mDiscountHistoryEntityDao.queryBuilder().where(DiscountHistoryEntityDao.Properties.DiscountHistoryId.eq(discountHistoryId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 根据账单id获取部分打折或打折方案
     *
     * @param orderId
     * @param discountType
     * @return
     */
    public DiscountHistoryEntity getDiscount(String orderId, int discountType) {
        try {
            QueryBuilder queryBuilder = mDiscountHistoryEntityDao.queryBuilder();
            DiscountHistoryEntity discountHistoryEntity = (DiscountHistoryEntity) queryBuilder.where(queryBuilder.and(DiscountHistoryEntityDao.Properties.OrderId.eq(orderId), DiscountHistoryEntityDao.Properties.DiscountType.eq(discountType))).build().unique();
            return discountHistoryEntity;
        } catch (Exception e) {
            return null;
        }
    }

    public DiscountHistoryEntity getDiscount(String orderId) {
        try {
            QueryBuilder queryBuilder = mDiscountHistoryEntityDao.queryBuilder();
            DiscountHistoryEntity discountHistoryEntity = (DiscountHistoryEntity) queryBuilder.where(DiscountHistoryEntityDao.Properties.OrderId.eq(orderId)).build().unique();
            return discountHistoryEntity;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据orderId清空打折
     *
     * @param orderId
     */
    public void clearDiscount(String orderId) {
        //判断该账单id是否有对应的部分打折和方案打折，如果有则删除
        DiscountHistoryEntity discountHistoryEntity = null;
        if ((discountHistoryEntity = getDiscount(orderId, 1)) != null) {
            String discountHistoryId = discountHistoryEntity.getDiscountHistoryId();
            clearSomeDiscountGoods(discountHistoryId);
            deleteDiscountHistory(discountHistoryId);
        }
        if ((discountHistoryEntity = getDiscount(orderId, 2)) != null) {
            String discountHistoryId = discountHistoryEntity.getDiscountHistoryId();
            deleteDiscountHistory(discountHistoryId);
        }
        if ((discountHistoryEntity = getDiscount(orderId, 0)) != null) {
            String discountHistoryId = discountHistoryEntity.getDiscountHistoryId();
            deleteDiscountHistory(discountHistoryId);
        }
    }

    /**
     * 总单中进行整单打折
     *
     * @param discountVal
     * @param discountReason
     * @param isAbleDiscount
     * @param joinOrderId
     */
    public void insertJoinOrderAllDiscount(int discountVal, String discountReason, int isAbleDiscount, String joinOrderId) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(joinOrderId));
        for (OrderEntity orderEntity :
                orderEntities) {
            insertAllDiscount(discountVal, discountReason, isAbleDiscount, orderEntity.getOrderId());
        }
    }

    /**
     * 插入整单打折
     *
     * @param discountVal    折扣率
     * @param discountReason 原因名称
     * @param isAbleDiscount 是否对设置了不允许打折的商品打折
     * @param orderId        账单id
     */
    public void insertAllDiscount(int discountVal, String discountReason, int isAbleDiscount, String orderId) {
        clearDiscount(orderId);
        DiscountHistoryEntity discountHistoryEntity = new DiscountHistoryEntity();
        discountHistoryEntity.setDiscountHistoryId(UUID.randomUUID().toString());
        discountHistoryEntity.setDiscountType(0);
        discountHistoryEntity.setOrderId(orderId);
        discountHistoryEntity.setCreateTime(System.currentTimeMillis());
        discountHistoryEntity.setDiscountRate(discountVal);
        discountHistoryEntity.setDiscountReason(discountReason);
        discountHistoryEntity.setIsAbleDiscount(isAbleDiscount);
        mDiscountHistoryEntityDao.insertOrReplace(discountHistoryEntity);
//        OrderEntity orderEntity = getOneOrderEntity(orderId);
//        if (orderEntity != null && orderEntity.getSerialNumber() != null && orderEntity.getIsUpload() == 1) {
//            insertUploadData(orderId, orderId, 3);
//        }
    }

    /**
     * 插入总单的部分打折
     *
     * @param discountHistoryEntity
     * @param someDiscountGoodsEntities
     */
    public void insertJoinOrderSomeDiscount(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities) {
        ArrayList<String> orderIds = new ArrayList<>();
        for (SomeDiscountGoodsEntity someDiscountGoodsEntity :
                someDiscountGoodsEntities) {
            if (!orderIds.contains(someDiscountGoodsEntity.getOrderDishId())) {
                orderIds.add(someDiscountGoodsEntity.getOrderDishId());
            }
        }
        for (String orderId :
                orderIds) {
            clearDiscount(orderId);
            DiscountHistoryEntity discountHistoryEntity1 = new DiscountHistoryEntity();
            discountHistoryEntity1.setOrderId(orderId);
            discountHistoryEntity1.setCreateTime(discountHistoryEntity.getCreateTime());
            discountHistoryEntity1.setDiscountHistoryId(UUID.randomUUID().toString());
            discountHistoryEntity1.setDiscountId(discountHistoryEntity.getDiscountId());
            discountHistoryEntity1.setDiscountRate(discountHistoryEntity.getDiscountRate());
            discountHistoryEntity1.setDiscountReason(discountHistoryEntity.getDiscountReason());
            discountHistoryEntity1.setDiscountType(discountHistoryEntity.getDiscountType());
            discountHistoryEntity1.setIsAbleDiscount(discountHistoryEntity.getIsAbleDiscount());
            mDiscountHistoryEntityDao.insertOrReplace(discountHistoryEntity1);
            for (SomeDiscountGoodsEntity someGoods :
                    someDiscountGoodsEntities) {
                if (someGoods.getOrderDishId().equals(orderId)) {
                    someGoods.setDiscountHistoryId(discountHistoryEntity1.getDiscountHistoryId());
                    mSomeDiscountGoodsEntityDao.insertOrReplace(someGoods);
                }
            }
        }
    }

    /**
     * 插入部分打折
     *
     * @param discountHistoryEntity
     * @param someDiscountGoodsEntities
     */
    public void insertSomeDiscount(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities) {
        clearDiscount(discountHistoryEntity.getOrderId());
        discountHistoryEntity.setCreateTime(System.currentTimeMillis());
        mDiscountHistoryEntityDao.insertOrReplace(discountHistoryEntity);
        for (SomeDiscountGoodsEntity someDiscountGoodsEntity :
                someDiscountGoodsEntities) {
            mSomeDiscountGoodsEntityDao.insertOrReplace(someDiscountGoodsEntity);
        }
    }

    /**
     * 插入总的方案打折
     *
     * @param discountHistoryEntity
     */
    public void insertJoinOrderDiscountScheme(DiscountHistoryEntity discountHistoryEntity) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.addAll(queryJoinOrderData(discountHistoryEntity.getOrderId()));
        for (OrderEntity orderEntity :
                orderEntities) {
            clearDiscount(orderEntity.getOrderId());
            DiscountHistoryEntity discountHistory = new DiscountHistoryEntity();
            discountHistory.setDiscountReason(discountHistoryEntity.getDiscountReason());
            discountHistory.setDiscountHistoryId(UUID.randomUUID().toString());
            discountHistory.setDiscountType(2);
            discountHistory.setDiscountId(discountHistoryEntity.getDiscountId());
            discountHistory.setCreateTime(System.currentTimeMillis());
            discountHistory.setOrderId(orderEntity.getOrderId());
            mDiscountHistoryEntityDao.insertOrReplace(discountHistory);
        }
    }

    /**
     * 插入方案打折
     *
     * @param discountHistoryEntity
     */
    public void insertDiscountScheme(DiscountHistoryEntity discountHistoryEntity) {
        clearDiscount(discountHistoryEntity.getOrderId());
        discountHistoryEntity.setDiscountHistoryId(UUID.randomUUID().toString());
        discountHistoryEntity.setDiscountType(2);
        discountHistoryEntity.setDiscountId(discountHistoryEntity.getDiscountId());
        discountHistoryEntity.setDiscountReason(discountHistoryEntity.getDiscountReason());
        discountHistoryEntity.setCreateTime(System.currentTimeMillis());
        mDiscountHistoryEntityDao.insertOrReplace(discountHistoryEntity);
    }

    /**
     * 根据打折记录id获取打折商品
     *
     * @param discountHistoryId
     * @return
     */
    public ArrayList<SomeDiscountGoodsEntity> getSomeGoods(String discountHistoryId) {
        return (ArrayList<SomeDiscountGoodsEntity>) mSomeDiscountGoodsEntityDao.queryBuilder().where(SomeDiscountGoodsEntityDao.Properties.DiscountHistoryId.eq(discountHistoryId)).list();
    }

    /**
     * 获取所有支付打折挂账抹零记录
     *
     * @param orderId
     * @return
     */
    public ArrayList<ClearModel> getAllClearModel(String orderId) {
        ArrayList<ClearModel> clearModels = new ArrayList<>();
        ArrayList<PayModeEntity> payModeEntities = new ArrayList<>();
        QueryBuilder queryBuilder = mPayModeEntityDao.queryBuilder();
        payModeEntities.addAll(queryBuilder.where(queryBuilder.and(PayModeEntityDao.Properties.OrderId.eq(orderId), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0), PayModeEntityDao.Properties.IsJoinOrderPay.eq(0))).list());
        for (PayModeEntity payModeEntity :
                payModeEntities) {
            ClearModel clearModel = new ClearModel(payModeEntity.getPayModeId(), payModeEntity.getPaymentName(), 0, AmountUtils.changeY2F(AmountUtils.multiply(payModeEntity.getPayMoney(),1)), payModeEntity.getPaymentType(), payModeEntity.getElectricOrderSerial());
            clearModels.add(clearModel);
        }

        DiscountHistoryEntity discountHistory = mDiscountHistoryEntityDao.queryBuilder().where(DiscountHistoryEntityDao.Properties.OrderId.eq(orderId)).build().unique();
        if (discountHistory != null) {
            String name = "";
            int rate = 100;
            switch (discountHistory.getDiscountType()) {
                case 0:
                    //整单打折
                    name = "整单打折";
                    rate = discountHistory.getDiscountRate();
                    break;
                case 1:
                    name = "部分打折";
                    rate = discountHistory.getDiscountRate();
                    break;
                case 2:
                    if (discountHistory.getDiscountId() != null) {
                        name = getDiscountName(discountHistory.getDiscountId());
                        rate = getDiscountSchemeRate(discountHistory.getDiscountId());
                    }
                    break;
            }
            ClearModel clearModel = new ClearModel(discountHistory.getDiscountHistoryId(), name, 1, rate, -1, null);
            clearModels.add(clearModel);
        }

        ArrayList<BillAccountHistoryEntity> billAccountHistories = new ArrayList<>();
        QueryBuilder queryBuilder1 = mBillAccountHistoryEntityDao.queryBuilder();
        billAccountHistories.addAll(queryBuilder1.where(queryBuilder1.and(BillAccountHistoryEntityDao.Properties.OrderId.eq(orderId), BillAccountHistoryEntityDao.Properties.IsJoinOrder.eq(0))).list());
        for (BillAccountHistoryEntity billAccountHistory :
                billAccountHistories) {
            ClearModel clearModel = new ClearModel(billAccountHistory.getBillAccountHistoryId(), billAccountHistory.getBillAccountName(), 2, billAccountHistory.getBillAccountMoney(), -1, null);
            clearModels.add(clearModel);
        }

        OrderEntity orderEntity = getOneOrderEntity(orderId);
        int selfTreatMoney = orderEntity.getSelfTreatMoney() != null ? orderEntity.getSelfTreatMoney() : 0;
        if (selfTreatMoney > 0) {
            ClearModel clearModel = new ClearModel(orderId, "抹零", 3, selfTreatMoney, -1, null);
            clearModels.add(clearModel);
        }
        return clearModels;
    }

    /**
     * 清空某个支付
     *
     * @param payModeId
     */
    public void clearOnePay(String payModeId) {
        mPayModeEntityDao.queryBuilder().where(PayModeEntityDao.Properties.PayModeId.eq(payModeId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 清空某个支付
     *
     * @param clearModel
     */
    public void clearOnePay(ClearModel clearModel) {
        if (clearModel.getId() != null) {
            switch (clearModel.getType()) {
                case 0:
                    mPayModeEntityDao.queryBuilder().where(PayModeEntityDao.Properties.PayModeId.eq(clearModel.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
                    break;
                case 1:
                    mDiscountHistoryEntityDao.queryBuilder().where(DiscountHistoryEntityDao.Properties.DiscountHistoryId.eq(clearModel.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
                    break;
                case 2:
                    mBillAccountHistoryEntityDao.queryBuilder().where(BillAccountHistoryEntityDao.Properties.BillAccountHistoryId.eq(clearModel.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
                    break;
                case 3:
                    OrderEntity orderEntity = getOneOrderEntity(clearModel.getId());
                    if (orderEntity != null) {
                        orderEntity.setSelfTreatMoney(0);
                        mOrderEntityDao.insertOrReplace(orderEntity);
                    }
                    break;
            }
        }
    }

    /**
     * 获取所有的打折方案
     *
     * @return
     */
    public ArrayList<DiscountEntity> getAllDiscountEntities() {
        return (ArrayList<DiscountEntity>) mDiscountEntityDao.queryBuilder().list();
    }

    /**
     * 获取所有的打折方案
     *
     * @return
     */
    public ArrayList<DiscountEntity> getAllDiscountEntities(int type) {
        return (ArrayList<DiscountEntity>) mDiscountEntityDao.queryBuilder().where(DiscountEntityDao.Properties.DiscountType.eq(type)).list();
    }

    /**
     * 根据打折方案id获取打折方案名称
     *
     * @param discountId
     * @return
     */
    public String getDiscountName(String discountId) {
        if (discountId == null) {
            return "";
        } else {
            DiscountEntity discountEntity = mDiscountEntityDao.queryBuilder().where(DiscountEntityDao.Properties.DiscountId.eq(discountId)).build().unique();
            if (discountEntity != null) {
                return discountEntity.getDiscountName();
            } else {
                return "";
            }
        }
    }

    /**
     * 根据打折方案id获取打折方案折扣率
     *
     * @param discountId
     * @return
     */
    public int getDiscountSchemeRate(String discountId) {
        if (discountId == null) {
            return 100;
        } else {
            DiscountEntity discountEntity = mDiscountEntityDao.queryBuilder().where(DiscountEntityDao.Properties.DiscountId.eq(discountId)).build().unique();
            if (discountEntity != null) {
                return discountEntity.getDiscountPercentage();
            } else {
                return 100;
            }
        }
    }

    /**
     * 根据打折方案id获取打折方案
     *
     * @param discountId
     * @return
     */
    public DiscountEntity getDiscountEntity(String discountId) {
        return mDiscountEntityDao.queryBuilder().where(DiscountEntityDao.Properties.DiscountId.eq(discountId)).build().unique();
    }

    /**
     * 根据打折方案id获取不同折扣的的商品分类
     *
     * @param discountId
     * @return
     */
    public ArrayList<DishTypeDiscountEntity> getDishTypeDiscount(String discountId) {
        return (ArrayList<DishTypeDiscountEntity>) mDishTypeDiscountEntityDao.queryBuilder().where(DishTypeDiscountEntityDao.Properties.DiscountId.eq(discountId)).list();
    }

    /**
     * 获取会员卡打折金额
     *
     * @param orderId
     * @return
     */
    public int getVipDiscountMoney(String orderId) {
        double result = 0;
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity.getIsVip() != null && orderEntity.getIsVip() == 1) {
                //使用会员卡打折
                ArrayList<OrderDishEntity> datas = queryOrderDishEntity(orderId, 1);
                OrderDishEntity orderDishEntity;
                double dishPrice, discountPrice, rate0;
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getIsOrdered() == 0 || datas.get(i).getIsOrdered() == 1) {
                        dishPrice = AmountUtils.multiply1(datas.get(i).getDishPrice() + "", "1.0");
                        orderDishEntity = datas.get(i);
                        rate0 = AmountUtils.multiply(getVipDiscountRate(100, orderEntity, orderDishEntity), 0.01);
                        discountPrice = AmountUtils.multiply(dishPrice, rate0);
                        result = AmountUtils.add(result, AmountUtils.subtract(dishPrice, discountPrice));
                    }
                }
            } else {
                result = 0;
            }
        } catch (Exception e) {
            result = 0;
        }
        return AmountUtils.changeY2F(result);
    }

    /**
     * 获取优惠券优惠金额
     *
     * @param orderId
     * @return
     */
    public int getCouponDiscountMoney(String orderId) {
        int result = 0;
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity.getIsCoupon() != null && orderEntity.getIsCoupon() == 1) {
                //使用优惠券
                switch (orderEntity.getCouponType()) {
                    case 0:
                        //满减券
                        result += orderEntity.getCouponFaceValue();
                        break;
                    case 1:
                        //代金券
                        result = orderEntity.getCouponFaceValue();
                        break;
                    case 2://折扣券
                        ArrayList<OrderDishEntity> datas = queryOrderDishEntity(orderId, 1);
                        OrderDishEntity orderDishEntity;
                        double dishPrice, discountPrice, rate0, rate1, vipDiscountPrice, couponDiscountPrice;
                        int rate, couponRate;
                        double totalMoney = 0;
                        for (int i = 0; i < datas.size(); i++) {
                            if (datas.get(i).getIsOrdered() == 0 || datas.get(i).getIsOrdered() == 1) {
                                dishPrice = AmountUtils.multiply1(datas.get(i).getDishPrice() + "", "1.0");
                                orderDishEntity = datas.get(i);
                                rate = getVipDiscountRate(100, orderEntity, orderDishEntity);
                                couponRate = getCouponDiscountRate(100, orderEntity, orderDishEntity);
                                rate0 = AmountUtils.multiply(rate, 0.01);
                                rate1 = AmountUtils.multiply(couponRate, 0.01);
                                vipDiscountPrice = AmountUtils.multiply(rate0, dishPrice);
                                couponDiscountPrice = AmountUtils.multiply(rate0, dishPrice, rate1);
                                discountPrice = AmountUtils.subtract(vipDiscountPrice, couponDiscountPrice);
                                totalMoney = AmountUtils.add(totalMoney, discountPrice);
                            }
                        }
                        result = AmountUtils.changeY2F(totalMoney);
                        break;
                }
            } else {
                result = 0;
            }
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    /**
     * 根据账单id和打折记录计算折扣金额
     *
     * @param orderId
     * @param discountHistoryEntity
     * @return
     */
    public int getDiscountMoney(String orderId, DiscountHistoryEntity discountHistoryEntity) {
        double result = 0;
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            ArrayList<OrderDishEntity> datas = queryOrderDishEntity(orderId, 1);
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).getIsOrdered() == 0 || datas.get(i).getIsOrdered() == 1) {
                    double dishPrice = AmountUtils.multiply1(datas.get(i).getDishPrice() + "", "1.0");//商品原价
                    double discountPrice = 0;
                    OrderDishEntity orderDishEntity = datas.get(i);
                    if (datas.get(i).getType() == 0) {
                        //非套餐
                        int[] rate = getDishDiscountRate(orderEntity, orderDishEntity, discountHistoryEntity);
                        double rate0 = AmountUtils.multiply(rate[0], 0.01);
                        double rate1 = AmountUtils.multiply(rate[1], 0.01);
                        discountPrice = AmountUtils.multiply(rate0, dishPrice, rate1);
                    } else {
                        //套餐
                        int[] rate = getTaocanDishDiscountRate(orderEntity, orderDishEntity, discountHistoryEntity);
                        double rate0 = AmountUtils.multiply(rate[0], 0.01);
                        double rate1 = AmountUtils.multiply(rate[1], 0.01);
                        discountPrice = AmountUtils.multiply(rate0, dishPrice, rate1);
                    }
                    result = AmountUtils.add(result, AmountUtils.subtract(dishPrice, discountPrice));
                }
            }
        } catch (Exception e) {

        }
        return AmountUtils.changeY2F(result);
    }

    /**
     * 根据菜品id和打着记录获取菜品折扣率
     *
     * @param orderDishEntity
     * @param discountHistoryEntity
     * @return
     */
    public int[] getDishDiscountRate(OrderEntity orderEntity, OrderDishEntity orderDishEntity, DiscountHistoryEntity discountHistoryEntity) {
        int[] rate = {100, 100};
        if (discountHistoryEntity != null) {
            switch (discountHistoryEntity.getDiscountType()) {
                case 0://整单打折
                    if (discountHistoryEntity.getIsAbleDiscount() == 0 && orderDishEntity != null && orderDishEntity.getIsAbleDiscount() == 0) {//该商品不打折
                        rate[0] = 100;
                    } else {
                        rate[0] = discountHistoryEntity.getDiscountRate();
                    }
                    break;
                case 1://部分打折
                    ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities = getSomeGoods(discountHistoryEntity.getDiscountHistoryId());
                    boolean b = false;
                    for (SomeDiscountGoodsEntity someDiscountGoodsEntity :
                            someDiscountGoodsEntities) {
                        if (orderDishEntity.getDishId().equals(someDiscountGoodsEntity.getDishId())) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        if (discountHistoryEntity.getIsAbleDiscount() == 0 && orderDishEntity != null && orderDishEntity.getIsAbleDiscount() == 0) {
                            rate[0] = 100;
                        } else {
                            rate[0] = discountHistoryEntity.getDiscountRate();
                        }
                    } else {
                        rate[0] = 100;
                    }
                    break;
                case 2://方案打折
                    String discountId = discountHistoryEntity.getDiscountId();
                    DiscountEntity discountEntity = getDiscountEntity(discountId);
                    if (discountEntity != null) {
                        if (discountEntity.getIsEnforcement() == 0 && orderDishEntity != null && orderDishEntity.getIsAbleDiscount() == 0) {
                            rate[0] = 100;
                        } else {
                            rate[0] = discountEntity.getDiscountPercentage();
                        }
                        if (discountEntity.getIsDishSameDiscount() == 0) {//如果有不同分类的折扣率
                            ArrayList<DishTypeDiscountEntity> dishTypeDiscountEntities = getDishTypeDiscount(discountId);
                            for (DishTypeDiscountEntity dishTypeDiscountEntity :
                                    dishTypeDiscountEntities) {
                                if (orderDishEntity != null && dishTypeDiscountEntity.getDishTypeId().equals(orderDishEntity.getDishTypeId())) {
                                    if (discountEntity.getIsEnforcement() == 0 && orderDishEntity.getIsAbleDiscount() == 0) {
                                        rate[0] = 100;
                                    } else {
                                        rate[0] = dishTypeDiscountEntity.getDishTypePercentage();
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        rate[0] = 100;
                    }
                    break;
            }
        } else {
            rate[0] = getVipDiscountRate(100, orderEntity, orderDishEntity);//先用会员卡打折
            rate[1] = getCouponDiscountRate(100, orderEntity, orderDishEntity);//再用折扣券打折
        }
        return rate;
    }

    /**
     * 某个菜品通过会员卡打折的折扣率
     *
     * @param orderEntity
     * @param orderDishEntity
     * @return
     */
    private int getVipDiscountRate(int oldRate, OrderEntity orderEntity, OrderDishEntity orderDishEntity) {
        int rate = oldRate;
        try {
            if (orderEntity.getIsVip() != null && orderEntity.getIsVip() == 1) {
                //会员卡打折
                if (orderDishEntity.getIsAbleDiscount() == 1) {
                    //该商品允许打折
                    if (orderEntity.getVipType() != null) {
                        VipCardEntity vipCardEntity = getVipCardDetail(orderEntity.getVipType());
                        if (vipCardEntity != null && vipCardEntity.getVipCardDiscountRate() != null)
                            rate = rate * vipCardEntity.getVipCardDiscountRate() / 100;
                    }
                }
            }
        } catch (Exception e) {
            rate = oldRate;
        }
        return rate;
    }

    /**
     * 折扣券打折后的折扣率
     *
     * @param oldRate
     * @param orderEntity
     * @param orderDishEntity
     * @return
     */
    private int getCouponDiscountRate(int oldRate, OrderEntity orderEntity, OrderDishEntity orderDishEntity) {
        int rate = oldRate;
        try {
            if (orderEntity.getIsCoupon() != null && orderEntity.getIsCoupon() == 1 && orderEntity.getCouponType() == 2) {
                //折扣券
                if (orderEntity.getIsCouponDiscountAll() == 1 || orderDishEntity.getIsAbleDiscount() == 1) {
                    //该折扣券对任何商品打折
                    if (orderEntity.getIsCouponWithVip() == 1) {
                        //折扣券与会员卡同时使用
                        rate = rate * orderEntity.getCouponFaceValue() / 100;
                    } else {
                        rate = orderEntity.getCouponFaceValue();
                    }
                }
            }
        } catch (Exception e) {
            rate = oldRate;
        }
        return rate;
    }

    /**
     * 根据套餐id和打折记录获取菜品折扣率
     *
     * @param orderDishEntity
     * @param discountHistoryEntity
     * @return
     */
    public int[] getTaocanDishDiscountRate(OrderEntity orderEntity, OrderDishEntity orderDishEntity, DiscountHistoryEntity discountHistoryEntity) {
        int[] rate = {100, 100};
        if (discountHistoryEntity != null) {
            switch (discountHistoryEntity.getDiscountType()) {
                case 0://整单打折
                    if (discountHistoryEntity.getIsAbleDiscount() == 0 && orderDishEntity.getIsAbleDiscount() == 0) {//该商品不打折
                        rate[0] = 100;
                    } else {
                        rate[0] = discountHistoryEntity.getDiscountRate();
                    }
                    break;
                case 1://部分打折
                    ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities = getSomeGoods(discountHistoryEntity.getDiscountHistoryId());
                    boolean b = false;
                    for (SomeDiscountGoodsEntity someDiscountGoodsEntity :
                            someDiscountGoodsEntities) {
                        if (orderDishEntity.getDishId().equals(someDiscountGoodsEntity.getDishId())) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        if (discountHistoryEntity.getIsAbleDiscount() == 0 && orderDishEntity.getIsAbleDiscount() == 0) {
                            rate[0] = 100;
                        } else {
                            rate[0] = discountHistoryEntity.getDiscountRate();
                        }
                    } else {
                        rate[0] = 100;
                    }
                    break;
                case 2://方案打折
                    String discountId = discountHistoryEntity.getDiscountId();
                    DiscountEntity discountEntity = getDiscountEntity(discountId);
                    if (discountEntity.getIsEnforcement() == 0 && orderDishEntity.getIsAbleDiscount() == 0) {
                        rate[0] = 100;
                    } else {
                        rate[0] = discountEntity.getDiscountPercentage();
                    }
                    if (discountEntity.getIsDishSameDiscount() == 0) {//如果有不同分类的折扣率
                        ArrayList<DishTypeDiscountEntity> dishTypeDiscountEntities = getDishTypeDiscount(discountId);
                        for (DishTypeDiscountEntity dishTypeDiscountEntity :
                                dishTypeDiscountEntities) {
                            if (dishTypeDiscountEntity.getDishTypeId().equals(orderDishEntity.getDishTypeId())) {
                                if (discountEntity.getIsEnforcement() == 0 && orderDishEntity.getIsAbleDiscount() == 0) {
                                    rate[0] = 100;
                                } else {
                                    rate[0] = dishTypeDiscountEntity.getDishTypePercentage();
                                }
                                break;
                            }
                        }
                    }
                    break;
            }
        } else {
            rate[0] = getVipDiscountRate(100, orderEntity, orderDishEntity);//先用会员卡打折
            rate[1] = getCouponDiscountRate(100, orderEntity, orderDishEntity);//再用折扣券打折
        }
        return rate;
    }

    /**
     * 插入套餐默认必须商品，对于有做法的商品设置默认做法
     *
     * @param orderDishId
     * @param taocanId
     */
    public void insertSnackDefaultTaocanDish(String orderId, String orderDishId, String taocanId, int status) {
        TaocanEntity taocanEntity = queryTaocanById(taocanId);
        ArrayList<TaocanGroupEntity> taocanGroupEntities = new ArrayList<>();
        taocanGroupEntities.addAll(queryTaocanGroup(taocanEntity.getTaocanId()));
        for (TaocanGroupEntity taocanGroupEntity :
                taocanGroupEntities) {
            if (taocanGroupEntity.getSelectMode() == 1) {//必须全部选定
                ArrayList<TaocanGroupDishEntity> taocanGroupDishEntities = new ArrayList<>();
                taocanGroupDishEntities.addAll(getMustTaocanGroupDish(taocanGroupEntity.getTaocanGroupId()));
                for (TaocanGroupDishEntity taocanGroupDishEntity :
                        taocanGroupDishEntities) {
                    insertSnackTaocanDish(orderId, orderDishId, taocanEntity.getTaocanId(), taocanGroupDishEntity, null, taocanGroupDishEntity.getSelectDishCount(), status);
                }
            }
        }
    }

    /**
     * 插入套餐默认必须商品，对于有做法的商品设置默认做法
     *
     * @param orderDishId
     * @param taocanId
     */
    public void insertDefaultTaocanDish(String orderId, String orderDishId, String taocanId, int status) {
        TaocanEntity taocanEntity = queryTaocanById(taocanId);
        ArrayList<TaocanGroupEntity> taocanGroupEntities = new ArrayList<>();
        taocanGroupEntities.addAll(queryTaocanGroup(taocanEntity.getTaocanId()));
        for (TaocanGroupEntity taocanGroupEntity :
                taocanGroupEntities) {
            if (taocanGroupEntity.getSelectMode() == 1) {//必须全部选定
                ArrayList<TaocanGroupDishEntity> taocanGroupDishEntities = new ArrayList<>();
                taocanGroupDishEntities.addAll(getMustTaocanGroupDish(taocanGroupEntity.getTaocanGroupId()));
                for (TaocanGroupDishEntity taocanGroupDishEntity :
                        taocanGroupDishEntities) {
                    insertTaocanDish(orderId, orderDishId, taocanEntity.getTaocanId(), taocanGroupDishEntity, null, taocanGroupDishEntity.getSelectDishCount(), status);
                }
            }
        }
    }

    /**
     * 根据商品id获取对应的做法
     *
     * @param dishId
     * @return
     */
    public ArrayList<DishPracticeEntity> getAllPracticeByDishId(String dishId) {
        ArrayList<DishPracticeEntity> dishPracticeEntities = new ArrayList<>();
        QueryBuilder queryBuilder = mDishPracticeEntityDao.queryBuilder();
        dishPracticeEntities.addAll(queryBuilder.where(DishPracticeEntityDao.Properties.DishId.eq(dishId)).list());
        return dishPracticeEntities;
    }

    /**
     * 根据商品做法的id获取商品做法
     *
     * @param dishPracticeId
     * @return
     */
    public DishPracticeEntity getDishPracticeById(String dishPracticeId) {
        if (dishPracticeId == null) {
            return null;
        } else {
            return mDishPracticeEntityDao.queryBuilder().where(DishPracticeEntityDao.Properties.DishPracticeId.eq(dishPracticeId)).build().unique();
        }
    }

    /**
     * 根据商品规格id获取商品规格
     *
     * @param dishSpecifyId
     * @return
     */
    public DishSpecifyEntity getDishSpecifyById(String dishSpecifyId) {
        if (dishSpecifyId == null) {
            return null;
        } else {
            return mDishSpecifyEntityDao.queryBuilder().where(DishSpecifyEntityDao.Properties.DishSpecifyId.eq(dishSpecifyId)).build().unique();
        }
    }

    /**
     * 根据套餐分类的id获取套餐分类
     *
     * @param taocanTypeId
     * @return
     */
    public TaocanTypeEntity getTaocanTypeById(String taocanTypeId) {
        return mTaocanTypeEntityDao.queryBuilder().where(TaocanTypeEntityDao.Properties.TaocanTypeId.eq(taocanTypeId)).build().unique();
    }

    /**
     * 插入套餐内商品
     *
     * @param orderDishId
     * @param taocanId
     * @param taocanGroupDishEntity
     * @param practiceId
     * @param count
     */
    public OrderTaocanGroupDishEntity insertTaocanDish(String orderId, String orderDishId, String taocanId, TaocanGroupDishEntity taocanGroupDishEntity, String practiceId, int count, int status) {
        OrderTaocanGroupDishEntity orderTaocanGroupDishEntity = new OrderTaocanGroupDishEntity();
        orderTaocanGroupDishEntity.setOrderId(orderId);
        orderTaocanGroupDishEntity.setOrderDishId(orderDishId);
        orderTaocanGroupDishEntity.setOrderDishName(getTaocanNameById(taocanId));
        orderTaocanGroupDishEntity.setDishId(taocanGroupDishEntity.getDishId());
        DishEntity dishEntity = queryOneDishEntity(taocanGroupDishEntity.getDishId());
        DishTypeEntity dishTypeEntity = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
        orderTaocanGroupDishEntity.setDishTypeId(dishTypeEntity.getDishTypeId());
        orderTaocanGroupDishEntity.setDishTypeName(dishTypeEntity.getDishTypeName());
        TaocanGroupEntity taocanGroup = getTaocanGroupById(taocanGroupDishEntity.getTaocanGroupId());
        TaocanEntity taocanEntity = queryTaocanById(taocanGroup.getTaocanId());
        TaocanTypeEntity taocanTypeEntity = getTaocanTypeById(taocanEntity.getTaocanTypeId());
        orderTaocanGroupDishEntity.setTaocanTypeId(taocanTypeEntity.getTaocanTypeId());
        orderTaocanGroupDishEntity.setTaocanTypeName(taocanTypeEntity.getTaocanTypeName());
        orderTaocanGroupDishEntity.setDishName(getDishNameByDishId(taocanGroupDishEntity.getDishId()));
        orderTaocanGroupDishEntity.setOrderTaocanGroupDishId(UUID.randomUUID().toString());
        orderTaocanGroupDishEntity.setSpecifyId(taocanGroupDishEntity.getSpecifyId());//商品规格id
        DishSpecifyEntity dishSpecify = getDishSpecifyById(taocanGroupDishEntity.getSpecifyId());
        if (dishSpecify != null) {
            orderTaocanGroupDishEntity.setSpecifyName(getSpecifyName(dishSpecify.getSpecifyId()));
        }
        float extrasPrice = taocanGroupDishEntity.getIncreasePrice();
        //添加做法
        if (practiceId == null) {
            ArrayList<DishPracticeEntity> dishPracticeEntities = getAllPracticeByDishId(taocanGroupDishEntity.getDishId());
            if (dishPracticeEntities.size() > 0) {
                //设置第一个做法为默认做法
                DishPracticeEntity dishPracticeEntity = dishPracticeEntities.get(0);
                orderTaocanGroupDishEntity.setPracticeId(dishPracticeEntity.getDishPracticeId());
                orderTaocanGroupDishEntity.setPracticeName(getPracticeName(dishPracticeEntity.getPracticeId()));
            } else {
                orderTaocanGroupDishEntity.setPracticeId(null);
                orderTaocanGroupDishEntity.setPracticeName(null);
            }
        } else {
            DishPracticeEntity dishPractice = getDishPracticeById(practiceId);
            orderTaocanGroupDishEntity.setPracticeId(practiceId);
            orderTaocanGroupDishEntity.setPracticeName(getPracticeName(dishPractice.getPracticeId()));
        }
        if (orderTaocanGroupDishEntity.getPracticeId() != null) {
            DishPracticeEntity dishPracticeEntity = getDishPracticeById(orderTaocanGroupDishEntity.getPracticeId());
            switch (dishPracticeEntity.getIncreaseMode()) {
                case 0://不加价

                    break;
                case 1://一次性加价
                    extrasPrice += dishPracticeEntity.getIncreaseValue();
                    break;
                case 2://每购买单位加价
                    extrasPrice += dishPracticeEntity.getIncreaseValue() * taocanGroupDishEntity.getSelectDishCount();
                    break;
                case 3://每结账单位加价
                    extrasPrice += dishPracticeEntity.getIncreaseValue() * taocanGroupDishEntity.getSelectDishCount();
                    break;
            }
        }
        orderTaocanGroupDishEntity.setExtraPrice(extrasPrice);
        orderTaocanGroupDishEntity.setStatus(status);
        orderTaocanGroupDishEntity.setTaocanGroupDishId(taocanGroupDishEntity.getTaocanGroupDishId());
        orderTaocanGroupDishEntity.setTaocanGroupId(taocanGroupDishEntity.getTaocanGroupId());
        orderTaocanGroupDishEntity.setCreateTime(System.currentTimeMillis());
        orderTaocanGroupDishEntity.setTaocanGroupDishCount(count);
        orderTaocanGroupDishEntity.setExtraPrice(extrasPrice);
        mOrderTaocanGroupDishEntityDao.insertOrReplace(orderTaocanGroupDishEntity);
        return orderTaocanGroupDishEntity;
    }

    /**
     * 插入套餐内商品
     *
     * @param orderDishId
     * @param taocanId
     * @param taocanGroupDishEntity
     * @param practiceId
     * @param count
     */
    public OrderTaocanGroupDishEntity insertSnackTaocanDish(String orderId, String orderDishId, String taocanId, TaocanGroupDishEntity taocanGroupDishEntity, String practiceId, int count, int status) {
        OrderTaocanGroupDishEntity orderTaocanGroupDishEntity = new OrderTaocanGroupDishEntity();
        orderTaocanGroupDishEntity.setOrderId(orderId);
        orderTaocanGroupDishEntity.setIsPrint(0);
        orderTaocanGroupDishEntity.setOrderDishId(orderDishId);
        orderTaocanGroupDishEntity.setOrderDishName(getTaocanNameById(taocanId));
        orderTaocanGroupDishEntity.setDishId(taocanGroupDishEntity.getDishId());
        DishEntity dishEntity = queryOneDishEntity(taocanGroupDishEntity.getDishId());
        DishTypeEntity dishTypeEntity = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
        orderTaocanGroupDishEntity.setDishTypeId(dishTypeEntity.getDishTypeId());
        orderTaocanGroupDishEntity.setDishTypeName(dishTypeEntity.getDishTypeName());
        TaocanGroupEntity taocanGroup = getTaocanGroupById(taocanGroupDishEntity.getTaocanGroupId());
        TaocanEntity taocanEntity = queryTaocanById(taocanGroup.getTaocanId());
        TaocanTypeEntity taocanTypeEntity = getTaocanTypeById(taocanEntity.getTaocanTypeId());
        orderTaocanGroupDishEntity.setTaocanTypeId(taocanTypeEntity.getTaocanTypeId());
        orderTaocanGroupDishEntity.setTaocanTypeName(taocanTypeEntity.getTaocanTypeName());
        orderTaocanGroupDishEntity.setDishName(getDishNameByDishId(taocanGroupDishEntity.getDishId()));
        orderTaocanGroupDishEntity.setOrderTaocanGroupDishId(UUID.randomUUID().toString());
        orderTaocanGroupDishEntity.setSpecifyId(taocanGroupDishEntity.getSpecifyId());//商品规格id
        DishSpecifyEntity dishSpecify = getDishSpecifyById(taocanGroupDishEntity.getSpecifyId());
        if (dishSpecify != null) {
            orderTaocanGroupDishEntity.setSpecifyName(getSpecifyName(dishSpecify.getSpecifyId()));
        }
        float extrasPrice = taocanGroupDishEntity.getIncreasePrice();
        //添加做法
        if (practiceId == null) {
            ArrayList<DishPracticeEntity> dishPracticeEntities = getAllPracticeByDishId(taocanGroupDishEntity.getDishId());
            if (dishPracticeEntities.size() > 0) {
                //设置第一个做法为默认做法
                DishPracticeEntity dishPracticeEntity = dishPracticeEntities.get(0);
                orderTaocanGroupDishEntity.setPracticeId(dishPracticeEntity.getDishPracticeId());
                orderTaocanGroupDishEntity.setPracticeName(getPracticeName(dishPracticeEntity.getPracticeId()));
            } else {
                orderTaocanGroupDishEntity.setPracticeId(null);
                orderTaocanGroupDishEntity.setPracticeName(null);
            }
        } else {
            DishPracticeEntity dishPractice = getDishPracticeById(practiceId);
            orderTaocanGroupDishEntity.setPracticeId(practiceId);
            orderTaocanGroupDishEntity.setPracticeName(getPracticeName(dishPractice.getPracticeId()));
        }
        if (orderTaocanGroupDishEntity.getPracticeId() != null) {
            DishPracticeEntity dishPracticeEntity = getDishPracticeById(orderTaocanGroupDishEntity.getPracticeId());
            switch (dishPracticeEntity.getIncreaseMode()) {
                case 0://不加价

                    break;
                case 1://一次性加价
                    extrasPrice += dishPracticeEntity.getIncreaseValue();
                    break;
                case 2://每购买单位加价
                    extrasPrice += dishPracticeEntity.getIncreaseValue() * taocanGroupDishEntity.getSelectDishCount();
                    break;
                case 3://每结账单位加价
                    extrasPrice += dishPracticeEntity.getIncreaseValue() * taocanGroupDishEntity.getSelectDishCount();
                    break;
            }
        }
        orderTaocanGroupDishEntity.setExtraPrice(extrasPrice);
        orderTaocanGroupDishEntity.setStatus(status);
        orderTaocanGroupDishEntity.setTaocanGroupDishId(taocanGroupDishEntity.getTaocanGroupDishId());
        orderTaocanGroupDishEntity.setTaocanGroupId(taocanGroupDishEntity.getTaocanGroupId());
        orderTaocanGroupDishEntity.setCreateTime(System.currentTimeMillis());
        orderTaocanGroupDishEntity.setTaocanGroupDishCount(count);
        orderTaocanGroupDishEntity.setExtraPrice(extrasPrice);
        mOrderTaocanGroupDishEntityDao.insertOrReplace(orderTaocanGroupDishEntity);
        return orderTaocanGroupDishEntity;
    }

    /**
     * 获取选中的套餐分组内商品
     *
     * @param orderDishId
     * @param taocanGroupId
     * @return
     */
    public ArrayList<OrderTaocanGroupDishEntity> getSelectedTaocanDish(String orderDishId, String taocanGroupId) {
        QueryBuilder queryBuilder = mOrderTaocanGroupDishEntityDao.queryBuilder();
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        orderTaocanGroupDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishId), OrderTaocanGroupDishEntityDao.Properties.TaocanGroupId.eq(taocanGroupId))).list());
        return orderTaocanGroupDishEntities;
    }

    /**
     * 获取选中的套餐分组内下单商品
     *
     * @param orderDishId
     * @param taocanGroupId
     * @return
     */
    public ArrayList<OrderTaocanGroupDishEntity> getSelectedOrderedTaocanDish(String orderDishId, String taocanGroupId) {
        QueryBuilder queryBuilder = mOrderTaocanGroupDishEntityDao.queryBuilder().where(OrderTaocanGroupDishEntityDao.Properties.Status.eq(1));
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        orderTaocanGroupDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishId), OrderTaocanGroupDishEntityDao.Properties.TaocanGroupId.eq(taocanGroupId))).list());
        return orderTaocanGroupDishEntities;
    }

    /**
     * 获取已选套餐分组内商品总数
     *
     * @param orderDishId
     * @param taocanGroupId
     * @return
     */
    public int getSelectedTaocanGroupDishCount(String orderDishId, String taocanGroupId) {
        int selectedCount = 0;
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishes = getSelectedTaocanDish(orderDishId, taocanGroupId);
        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                orderTaocanGroupDishes) {
            selectedCount += orderTaocanGroupDish.getTaocanGroupDishCount();
        }
        return selectedCount;
    }

    /**
     * 获取已选套餐分组内下单商品总数
     *
     * @param orderDishId
     * @param taocanGroupId
     * @return
     */
    public int getSelectedOrderedTaocanGroupDishCount(String orderDishId, String taocanGroupId) {
        int selectedCount = 0;
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishes = getSelectedOrderedTaocanDish(orderDishId, taocanGroupId);
        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                orderTaocanGroupDishes) {
            selectedCount += orderTaocanGroupDish.getTaocanGroupDishCount();
        }
        return selectedCount;
    }

    /**
     * 获取已点套餐内某个商品的数量
     *
     * @param orderDishId
     * @param taocanGroupDishId
     * @return
     */
    public int getSelectedTaocanDishCount(String orderDishId, String taocanGroupDishId) {
        QueryBuilder queryBuilder = mOrderTaocanGroupDishEntityDao.queryBuilder();
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        orderTaocanGroupDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishId), OrderTaocanGroupDishEntityDao.Properties.TaocanGroupDishId.eq(taocanGroupDishId))).list());
        int count = 0;
        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                orderTaocanGroupDishEntities) {
            count += orderTaocanGroupDish.getTaocanGroupDishCount();
        }
        return count;
    }

    /**
     * 获取已点套餐内某个商品的下单数量
     *
     * @param orderDishId
     * @param taocanGroupDishId
     * @return
     */
    public int getSelectedOrderedTaocanDishCount(String orderDishId, String taocanGroupDishId) {
        QueryBuilder queryBuilder = mOrderTaocanGroupDishEntityDao.queryBuilder().where(OrderTaocanGroupDishEntityDao.Properties.Status.eq(1));
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        orderTaocanGroupDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishId), OrderTaocanGroupDishEntityDao.Properties.TaocanGroupDishId.eq(taocanGroupDishId))).list());
        int count = 0;
        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                orderTaocanGroupDishEntities) {
            count += orderTaocanGroupDish.getTaocanGroupDishCount();
        }
        return count;
    }

    /**
     * 根据套餐分组id获取套餐分组内商品
     *
     * @param taocanGroupId
     * @return
     */
    public ArrayList<TaocanGroupDishEntity> getMustTaocanGroupDish(String taocanGroupId) {
        ArrayList<TaocanGroupDishEntity> taocanGroupDishEntities = new ArrayList<>();
        QueryBuilder queryBuilder = mTaocanGroupDishEntityDao.queryBuilder();
        taocanGroupDishEntities.addAll(queryBuilder.where(TaocanGroupDishEntityDao.Properties.TaocanGroupId.eq(taocanGroupId)).list());
        return taocanGroupDishEntities;
    }

    /**
     * 获取套餐分组内的必须全部选定的数量
     *
     * @param taocanGroupId
     * @return
     */
    public int getMustTaocanGroupDishCount(String taocanGroupId) {
        int mustCount = 0;
        ArrayList<TaocanGroupDishEntity> taocanGroupDishEntities = new ArrayList<>();
        taocanGroupDishEntities.addAll(getMustTaocanGroupDish(taocanGroupId));
        for (TaocanGroupDishEntity taocanGroupDishEntity :
                taocanGroupDishEntities) {
            mustCount += taocanGroupDishEntity.getSelectDishCount();
        }
        return mustCount;
    }

    /**
     * 根据套餐分组id获取套餐分组
     *
     * @param taocanGroupId
     * @return
     */
    public TaocanGroupEntity getTaocanGroupById(String taocanGroupId) {
        QueryBuilder queryBuilder = mTaocanGroupEntityDao.queryBuilder();
        return (TaocanGroupEntity) queryBuilder.where(TaocanGroupEntityDao.Properties.TaocanGroupId.eq(taocanGroupId)).build().unique();
    }

    /**
     * 获取已点套餐商品的数量
     *
     * @param orderDishId
     * @param taocanGroupDishId
     * @return
     */
    public int getSelectedTaocanCount(String orderDishId, String taocanGroupDishId) {
        QueryBuilder queryBuilder = mOrderTaocanGroupDishEntityDao.queryBuilder();
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        orderTaocanGroupDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishId), OrderTaocanGroupDishEntityDao.Properties.TaocanGroupDishId.eq(taocanGroupDishId))).list());
        int count = 0;
        for (OrderTaocanGroupDishEntity orderTaocanGroupDishEntity :
                orderTaocanGroupDishEntities) {
            count += orderTaocanGroupDishEntity.getTaocanGroupDishCount();
        }
        return count;
    }

    /**
     * 根据下单套餐获取套餐内已点商品
     *
     * @param orderDishEntity
     * @return
     */
    public ArrayList<OrderTaocanGroupDishEntity> getOrderedTaocanDish(OrderDishEntity orderDishEntity) {
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        if(orderDishEntity == null){
            return orderTaocanGroupDishEntities;
        }
        String orderDishId = orderDishEntity.getOrderDishId();
        QueryBuilder queryBuilder = mOrderTaocanGroupDishEntityDao.queryBuilder();
        orderTaocanGroupDishEntities.addAll(queryBuilder.where(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishId)).list());
        return orderTaocanGroupDishEntities;
    }

    /**
     * 根据下单套餐获取套餐内已点商品
     *
     * @param orderDishEntity
     * @return
     */
    public ArrayList<OrderTaocanGroupDishEntity> getUnOrderedTaocanDish(OrderDishEntity orderDishEntity) {
        String orderDishId = orderDishEntity.getOrderDishId();
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        QueryBuilder queryBuilder = mOrderTaocanGroupDishEntityDao.queryBuilder();
        orderTaocanGroupDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishId), OrderTaocanGroupDishEntityDao.Properties.Status.notEq(1))).list());
        return orderTaocanGroupDishEntities;
    }

    /**
     * 根据下单套餐获取套餐内已下单商品
     *
     * @param orderDishEntity
     * @return
     */
    public ArrayList<OrderTaocanGroupDishEntity> getHadOrderedTaocanDish(OrderDishEntity orderDishEntity) {
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        if(orderDishEntity == null){
            return orderTaocanGroupDishEntities;
        }
        String orderDishId = orderDishEntity.getOrderDishId();
        QueryBuilder queryBuilder = mOrderTaocanGroupDishEntityDao.queryBuilder();
        orderTaocanGroupDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishId), OrderTaocanGroupDishEntityDao.Properties.Status.eq(1))).list());
        return orderTaocanGroupDishEntities;
    }

    /**
     * 获取已点套餐总价
     *
     * @param orderDishEntity
     * @return
     */
    public float getTaocanPrice(OrderDishEntity orderDishEntity) {
        float price = 0.0f;
        if(orderDishEntity == null){
            return price;
        }
        String taocanId = orderDishEntity.getDishId();
        TaocanEntity taocanEntity = queryTaocanById(taocanId);
        if (taocanEntity != null)
            price += taocanEntity.getTaocanPrice();
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = getOrderedTaocanDish(orderDishEntity);
        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                orderTaocanGroupDishEntities) {
            price += orderTaocanGroupDish.getExtraPrice() * orderTaocanGroupDish.getTaocanGroupDishCount();
        }
        return price;
    }

    /**
     * 获取已下单套餐总价
     *
     * @param orderDishEntity
     * @return
     */
    public float getOrderedTaocanPrice(OrderDishEntity orderDishEntity) {
        float price = 0.0f;
        String taocanId = orderDishEntity.getDishId();
        TaocanEntity taocanEntity = queryTaocanById(taocanId);
        if (taocanEntity != null) {
            price += taocanEntity.getTaocanPrice();
        }
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = getHadOrderedTaocanDish(orderDishEntity);
        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                orderTaocanGroupDishEntities) {
            price += orderTaocanGroupDish.getExtraPrice() * orderTaocanGroupDish.getTaocanGroupDishCount();
        }
        return price;
    }

    /**
     * 根据套餐内商品id获取商品
     *
     * @param taocanGroupDishId
     * @return
     */
    public TaocanGroupDishEntity queryTaocanGroupDishById(String taocanGroupDishId) {
        return mTaocanGroupDishEntityDao.queryBuilder().where(TaocanGroupDishEntityDao.Properties.TaocanGroupDishId.eq(taocanGroupDishId)).build().unique();
    }

    /**
     * 根据套餐分组id获取分组
     *
     * @param taocanGroupId
     * @return
     */
    public TaocanGroupEntity queryTaocanGroupById(String taocanGroupId) {
        return mTaocanGroupEntityDao.queryBuilder().where(TaocanGroupEntityDao.Properties.TaocanGroupId.eq(taocanGroupId)).build().unique();
    }

    /**
     * 修改商品数量
     *
     * @param orderTaocanGroupDish
     */
    public void changeTaocanDishCount(OrderTaocanGroupDishEntity orderTaocanGroupDish) {
        mOrderTaocanGroupDishEntityDao.insertOrReplace(orderTaocanGroupDish);
    }

    /**
     * 删除已点套餐内商品
     *
     * @param orderTaocanGroupDish
     */
    public void deleteTaocanGroupDish(OrderTaocanGroupDishEntity orderTaocanGroupDish) {
        mOrderTaocanGroupDishEntityDao.queryBuilder().where(OrderTaocanGroupDishEntityDao.Properties.OrderTaocanGroupDishId.eq(orderTaocanGroupDish.getOrderTaocanGroupDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 修改已点套餐商品
     *
     * @param orderTaocanGroupDish
     */
    public void changeTaocanGroupDish(OrderTaocanGroupDishEntity orderTaocanGroupDish) {
        mOrderTaocanGroupDishEntityDao.insertOrReplace(orderTaocanGroupDish);
    }

    /**
     * 取消编辑套餐
     *
     * @param orderDishEntity
     * @param orderTaocanGroupDishes
     */
    public void cancleTaocanEdit(OrderDishEntity orderDishEntity, ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishes) {
        mOrderTaocanGroupDishEntityDao.queryBuilder().where(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                orderTaocanGroupDishes) {
            mOrderTaocanGroupDishEntityDao.insertOrReplace(orderTaocanGroupDish);
        }
    }

    /**
     * 完成套餐编辑
     *
     * @param orderDishEntity
     */
    public OrderDishEntity confirmTaocanEdit(OrderDishEntity orderDishEntity) {
        TaocanEntity taocanEntity = queryTaocanById(orderDishEntity.getDishId());
        TaocanTypeEntity taocanType = getTaocanTypeById(taocanEntity.getTaocanTypeId());
        orderDishEntity.setDishTypeId(taocanType.getTaocanTypeId());
        orderDishEntity.setDishTypeName(taocanType.getTaocanTypeName());
        orderDishEntity.setDishPrice(getTaocanPrice(orderDishEntity));
        orderDishEntity.setIsRetreat(taocanEntity.getIsAbleRetreat());
        orderDishEntity.setIsPresent(0);
        mOrderDishEntityDao.insertOrReplace(orderDishEntity);
        ArrayList<OrderTaocanGroupDishEntity> results = new ArrayList<>();
        ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
        orderTaocanGroupDishEntities.addAll(getUnOrderedTaocanDish(orderDishEntity));
        boolean isExist = false;
        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                orderTaocanGroupDishEntities) {
            isExist = false;
            for (OrderTaocanGroupDishEntity result :
                    results) {
                if (orderTaocanGroupDish.compareTo(result)) {
                    //两个菜品一样需要合并
                    isExist = true;
                    result.setTaocanGroupDishCount(result.getTaocanGroupDishCount() + orderTaocanGroupDish.getTaocanGroupDishCount());
                    TaocanGroupDishEntity taocanGroupDishEntity = queryTaocanGroupDishById(orderTaocanGroupDish.getTaocanGroupDishId());
                    float extrasPrice = taocanGroupDishEntity.getIncreasePrice();
                    //添加做法
                    if (result.getPracticeId() != null) {
                        DishPracticeEntity dishPracticeEntity = getDishPracticeById(result.getPracticeId());
                        switch (dishPracticeEntity.getIncreaseMode()) {
                            case 0://不加价

                                break;
                            case 1://一次性加价
                                extrasPrice += dishPracticeEntity.getIncreaseValue();
                                break;
                            case 2://每购买单位加价
                                extrasPrice += dishPracticeEntity.getIncreaseValue() * taocanGroupDishEntity.getSelectDishCount();
                                break;
                            case 3://每结账单位加价
                                extrasPrice += dishPracticeEntity.getIncreaseValue() * taocanGroupDishEntity.getSelectDishCount();
                                break;
                        }
                    }
                    result.setExtraPrice(extrasPrice);
                    mOrderTaocanGroupDishEntityDao.queryBuilder().where(OrderTaocanGroupDishEntityDao.Properties.OrderTaocanGroupDishId.eq(orderTaocanGroupDish.getOrderTaocanGroupDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
                    result.setStatus(0);
                    break;
                }
            }
            if (!isExist) {
                orderTaocanGroupDish.setStatus(0);
                results.add(orderTaocanGroupDish);
            }
        }
        for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                results) {
            mOrderTaocanGroupDishEntityDao.insertOrReplace(orderTaocanGroupDish);
        }
        return orderDishEntity;
    }

    /**
     * 删除套餐
     *
     * @param orderDishEntity
     */
    public void deleteTaocan(OrderDishEntity orderDishEntity) {
        mOrderTaocanGroupDishEntityDao.queryBuilder().where(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
        mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 获取收银打印
     *
     * @return
     */
    public PrintCashierEntity getPrintCashierEntity() {
        return mPrintCashierEntityDao.queryBuilder().build().unique();
    }

    /**
     * 判断是否有未下单商品
     *
     * @param orderId
     * @return
     */
    public boolean isHasUnOrderedDish(String orderId) {
        try {
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            return queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsOrdered.eq(0))).buildCount().count() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取是否有未打印的商品
     *
     * @param orderId
     * @return
     */
    public boolean isHasUnPrintedDish(String orderId) {
        try {
            QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
            return queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsPrint.eq(0))).buildCount().count() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取是否有已下单商品
     *
     * @param orderId
     * @return
     */
    public boolean isHasOrderedDish(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        return queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsOrdered.notEq(0))).buildCount().count() > 0;
    }

    /**
     * 是否有赠送的商品或者已下单商品
     *
     * @param orderId
     * @return
     */
    public boolean isHasSnackOrderedDish(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        return queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), queryBuilder.or(OrderDishEntityDao.Properties.IsOrdered.eq(1), OrderDishEntityDao.Properties.IsOrdered.eq(-2)))).buildCount().count() > 0;
    }

    /**
     * 是否有已打印的菜品
     *
     * @param orderId
     * @return
     */
    public boolean isHasPrintedDish(String orderId) {
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        return queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsPrint.notEq(0))).buildCount().count() > 0;
    }

    /**
     * 获取所有厨打方案
     *
     * @return
     */
    public ArrayList<PrintKitchenEntity> getAllKichenPrinter() {
        ArrayList<PrintKitchenEntity> printKitchenEntities = new ArrayList<>();
        try {
            printKitchenEntities.addAll(mPrintKitchenEntityDao.queryBuilder().list());
        } catch (Exception e) {

        }
        return printKitchenEntities;
    }

    /**
     * 获取打印记录
     *
     * @return
     */
    public ArrayList<PrinterFailedHistoryEntity> getAllFailedPrinterHistory(String printKitchenId, int status) {
        ArrayList<PrinterFailedHistoryEntity> result = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mPrinterFailedHistoryEntityDao.queryBuilder();
            if (printKitchenId == null) {
                result.addAll(queryBuilder.where(PrinterFailedHistoryEntityDao.Properties.PrintStatus.eq(status)).orderDesc(PrinterFailedHistoryEntityDao.Properties.PrintTime).list());
            } else {
                result.addAll(queryBuilder.where(queryBuilder.and(PrinterFailedHistoryEntityDao.Properties.PrintStatus.eq(status), PrinterFailedHistoryEntityDao.Properties.PrintKitchenId.eq(printKitchenId))).orderDesc(PrinterFailedHistoryEntityDao.Properties.PrintTime).list());
            }
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * 更改打印机状态
     *
     * @param mStatusMap
     */
    public boolean changePrinterStatus(ConcurrentHashMap<String, Boolean> mStatusMap) {
        int changedCount = 0;
        try {
            ArrayList<PrintKitchenEntity> printKitchenEntities = new ArrayList<>();
            printKitchenEntities.addAll(getAllKichenPrinter());
            for (PrintKitchenEntity printKitchen :
                    printKitchenEntities) {
                if (mStatusMap.containsKey(printKitchen.getPrintKitchenIp())) {
                    int currentStatus = printKitchen.getConnectStatus();
                    int nowStatus = mStatusMap.get(printKitchen.getPrintKitchenIp()) ? 1 : 0;
                    if (currentStatus != nowStatus) {
                        printKitchen.setConnectStatus(mStatusMap.get(printKitchen.getPrintKitchenIp()) ? 1 : 0);
                        changedCount++;
                    }
                } else {
                    printKitchen.setConnectStatus(0);
                }
                mPrintKitchenEntityDao.insertOrReplace(printKitchen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return changedCount > 0;
    }

    /**
     * 获取未连接的打印机总数
     *
     * @return
     */
    public int getUnConnectPrinterCount() {
        int count = 0;
        ArrayList<PrintKitchenEntity> printKitchenEntities = getAllKichenPrinter();
        for (PrintKitchenEntity printKitchenEntity :
                printKitchenEntities) {
            if (printKitchenEntity.getConnectStatus() == 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * 插入打印记录
     *
     * @param orderEntity
     * @param printKitchenEntity
     * @param status
     */
    public void insertPrintHistory(OrderEntity orderEntity, PrintKitchenEntity printKitchenEntity, ArrayList<PrintDishBean> printDishBeans, int printType, int status) {
        PrinterFailedHistoryEntity printerFailedHistoryEntity = new PrinterFailedHistoryEntity();
        printerFailedHistoryEntity.setPrinterFailedHistoryId(UUID.randomUUID().toString());
        printerFailedHistoryEntity.setOrderId(orderEntity.getOrderId());
        printerFailedHistoryEntity.setPrintKitchenId(printKitchenEntity.getPrintKitchenId());
        printerFailedHistoryEntity.setPrintStatus(status);
        printerFailedHistoryEntity.setPrintTime(System.currentTimeMillis());
        printerFailedHistoryEntity.setPrintType(printType);
        String printDish = "";
        PrintDishBean prePrintDishBean = null;
        for (PrintDishBean orderDishEntity :
                printDishBeans) {
            if (orderDishEntity.getType() == 1) {
                //套餐
                if (prePrintDishBean != null && prePrintDishBean.getType() == 1 && orderDishEntity.getParentBean().getOrderDishId().equals(prePrintDishBean.getParentBean().getOrderDishId())) {
                    //当前套餐内商品和前面的套餐内商品属于同一个套餐
                    printDish += "    " + orderDishEntity.getDishCount() + orderDishEntity.getUnitName() + " X " + orderDishEntity.getDishName();
                    if (orderDishEntity.getDishConfig().length() > 0) {
                        printDish += "\n    " + orderDishEntity.getDishConfig();
                    }
                    printDish += "、";
                } else {
                    printDish += orderDishEntity.getParentBean().getDishCount() + orderDishEntity.getUnitName() + " X " + orderDishEntity.getParentBean().getDishName() + "、";
                    printDish += "    " + orderDishEntity.getDishCount() + orderDishEntity.getUnitName() + " X " + orderDishEntity.getDishName();
                    if (orderDishEntity.getDishConfig().length() > 0) {
                        printDish += "\n    " + orderDishEntity.getDishConfig();
                    }
                    printDish += "、";
                }
            } else {
                printDish += orderDishEntity.getDishCount() + orderDishEntity.getUnitName() + " X " + orderDishEntity.getDishName();
                if (orderDishEntity.getDishConfig().length() > 0) {
                    printDish += "\n" + orderDishEntity.getDishConfig();
                }
                if (orderDishEntity.getMark() != null && orderDishEntity.getMark().length() > 0) {
                    printDish += "\n备注：" + orderDishEntity.getMark();
                }
                if (orderDishEntity.getDishSelectedMaterialEntities() != null && orderDishEntity.getDishSelectedMaterialEntities().length() > 0) {
                    printDish += "\n加料：" + orderDishEntity.getDishSelectedMaterialEntities();
                }
                printDish += "、";
            }
            prePrintDishBean = orderDishEntity;
        }
        if (printDish.length() > 0) {
            printDish = printDish.substring(0, printDish.length() - 1);
        }
        printerFailedHistoryEntity.setPrintDishType("");
        printerFailedHistoryEntity.setPrintDish(printDish);
        mPrinterFailedHistoryEntityDao.insertOrReplace(printerFailedHistoryEntity);
    }

    /**
     * 更新打印记录
     *
     * @param printerFailedHistoryEntity
     */
    public void changePrintHistory(PrinterFailedHistoryEntity printerFailedHistoryEntity) {
        mPrinterFailedHistoryEntityDao.insertOrReplace(printerFailedHistoryEntity);
    }

    /**
     * 根据打印方案id获取打印方案
     *
     * @param printKitchenId
     * @return
     */
    public PrintKitchenEntity getPrintKitchen(String printKitchenId) {
        return mPrintKitchenEntityDao.queryBuilder().where(PrintKitchenEntityDao.Properties.PrintKitchenId.eq(printKitchenId)).build().unique();
    }

    /**
     * 根据打折方案和打印状态清空打印记录
     *
     * @param printKitchenId
     * @param status
     */
    public void clearPrintHistory(String printKitchenId, int status) {
        QueryBuilder queryBuilder = mPrinterFailedHistoryEntityDao.queryBuilder();
        if (printKitchenId == null) {
            queryBuilder.where(PrinterFailedHistoryEntityDao.Properties.PrintStatus.eq(status)).buildDelete().executeDeleteWithoutDetachingEntities();
        } else {
            queryBuilder.where(queryBuilder.and(PrinterFailedHistoryEntityDao.Properties.PrintKitchenId.eq(printKitchenId), PrinterFailedHistoryEntityDao.Properties.PrintStatus.eq(status))).buildDelete().executeDeleteWithoutDetachingEntities();
        }
    }

    /**
     * 判断菜品是否存在
     *
     * @param dishId
     * @return
     */
    public boolean isDishExist(String dishId) {
        DishEntity dishEntity = mDishEntityDao.queryBuilder().where(DishEntityDao.Properties.DishId.eq(dishId)).build().unique();
        return dishEntity != null;
    }

    /**
     * 根据打印记录id获取打印记录
     *
     * @param printHistoryId
     * @return
     */
    public PrinterFailedHistoryEntity getPrintHistory(String printHistoryId) {
        return mPrinterFailedHistoryEntityDao.queryBuilder().where(PrinterFailedHistoryEntityDao.Properties.PrinterFailedHistoryId.eq(printHistoryId)).build().unique();
    }

    /**
     * 插入推送消息，0：外卖；1：预定；2：排号；3：到店点餐；4：催菜；5：美团外卖；6：快餐版到店点餐
     *
     * @param orderId
     */
    public void insertJpushMessage(String orderId, int type) {
        JpushMessageEntity jpushMessageEntity = new JpushMessageEntity();
        jpushMessageEntity.setJpushMessageId(UUID.randomUUID().toString());
        jpushMessageEntity.setMessage(orderId);
        jpushMessageEntity.setType(type);
        jpushMessageEntity.setCount(0);
        jpushMessageEntity.setLastTime(System.currentTimeMillis());
        mJpushMessageEntityDao.insertOrReplace(jpushMessageEntity);
    }

    /**
     * 修改推送消息调用接口的次数
     *
     * @param jpushMessageEntity
     * @param time
     */
    public void changeJpushMessageCount(JpushMessageEntity jpushMessageEntity, long time) {
        jpushMessageEntity.setLastTime(time);
        jpushMessageEntity.setCount(jpushMessageEntity.getCount() + 1);
        mJpushMessageEntityDao.insertOrReplace(jpushMessageEntity);
    }

    /**
     * 删除推送消息
     *
     * @param jpushMessageId
     */
    public void clearJpushMessage(String jpushMessageId) {
        mJpushMessageEntityDao.queryBuilder().where(JpushMessageEntityDao.Properties.JpushMessageId.eq(jpushMessageId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 获取推送消息
     *
     * @return
     */
    public ArrayList<JpushMessageEntity> getAllJpushMessages() {
        ArrayList<JpushMessageEntity> jpushMessageEntities = new ArrayList<>();
        jpushMessageEntities.addAll(mJpushMessageEntityDao.queryBuilder().list());
        return jpushMessageEntities;
    }

    /**
     * 删除获取到店点餐消息
     *
     * @param orderId
     */
    public void clearTableOrder(String orderId) {
        try {
            mJpushMessageEntityDao.queryBuilder().where(JpushMessageEntityDao.Properties.Message.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除已下单的外卖消息
     *
     * @param orderId
     */
    public void deleteUnLoadTakeOutMessage(String orderId) {
        mJpushMessageEntityDao.queryBuilder().where(JpushMessageEntityDao.Properties.Message.eq(orderId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 获取对应的付款方式
     * 0：现金；1：银行卡；2：微信；3：支付宝；4：团购；5：会员卡；6：挂账
     *
     * @param type
     * @return
     */
    public ArrayList<Payment> queryPaymentByType(int type) {
        ArrayList<Payment> payments = new ArrayList<>();
        switch (type) {
            case 0://现金
                payments.addAll(mShopPaymentEntityDao.queryBuilder().where(ShopPaymentEntityDao.Properties.PaymentType.eq(0)).list());
                break;
            case 1://银行卡
                payments.addAll(mShopPaymentEntityDao.queryBuilder().where(ShopPaymentEntityDao.Properties.PaymentType.eq(1)).list());
                break;
            case 2://团购
                payments.addAll(mGrouponEntityDao.queryBuilder().list());
                break;
            case 3://挂账
                payments.addAll(mBillAccountEntityDao.queryBuilder().list());
                break;
        }
        return payments;
    }

    /**
     * 获取所有付款方式
     *
     * @return
     */
    public ArrayList<Payment> getAllPayment() {
        ArrayList<Payment> payments = new ArrayList<>();
        payments.addAll(mShopPaymentEntityDao.queryBuilder().list());
        payments.addAll(mGrouponEntityDao.queryBuilder().list());
        payments.addAll(mBillAccountEntityDao.queryBuilder().list());
        return payments;
    }

    /**
     * 获取挂账单位或者挂账人，签字人
     *
     * @param type
     * @param billAccountId
     * @return
     */
    public ArrayList<BillPerson> queryBillPersonByType(int type, String billAccountId) {
        ArrayList<BillPerson> billPersons = new ArrayList<>();
        switch (type) {
            case 0://选择挂账单位
                QueryBuilder queryBuilder = mBillAccountPersonEntityDao.queryBuilder();
                billPersons.addAll(queryBuilder.where(queryBuilder.and(BillAccountPersonEntityDao.Properties.BillAccountId.eq(billAccountId), BillAccountPersonEntityDao.Properties.BillAccountPersonType.eq(0))).list());
                break;
            case 1://选择挂账人
                QueryBuilder queryBuilder1 = mBillAccountPersonEntityDao.queryBuilder();
                billPersons.addAll(queryBuilder1.where(queryBuilder1.and(BillAccountPersonEntityDao.Properties.BillAccountId.eq(billAccountId), BillAccountPersonEntityDao.Properties.BillAccountPersonType.eq(1))).list());
                break;
            case 2://选择签字人
                QueryBuilder queryBuilder2 = mBillAccountSignEntityDao.queryBuilder();
                billPersons.addAll(queryBuilder2.where(BillAccountSignEntityDao.Properties.BillAccountId.eq(billAccountId)).list());
                break;
        }
        return billPersons;
    }

    /**
     * 插入挂账记录
     *
     * @param billAccountHistoryEntity
     */
    public void insertBillAccountHistory(BillAccountHistoryEntity billAccountHistoryEntity, int isJoinOrder) {
        mBillAccountHistoryEntityDao.insertOrReplace(billAccountHistoryEntity);
    }

    /**
     * 根据账单id获取所有挂账
     *
     * @param orderId
     * @return
     */
    public ArrayList<BillAccountHistoryEntity> queryAllBillAccountHistoryByOrderId(String orderId) {
        ArrayList<BillAccountHistoryEntity> billAccountHistoryEntities = new ArrayList<>();
        billAccountHistoryEntities.addAll(mBillAccountHistoryEntityDao.queryBuilder().where(BillAccountHistoryEntityDao.Properties.OrderId.eq(orderId)).list());
        return billAccountHistoryEntities;
    }

    /**
     * 根据团购网站获取团购套餐
     *
     * @param grouponId
     * @return
     */
    public ArrayList<GrouponTaocanEntity> queryGrouponTaocanById(String grouponId) {
        ArrayList<GrouponTaocanEntity> grouponTaocanEntities = new ArrayList<>();
        grouponTaocanEntities.addAll(mGrouponTaocanEntityDao.queryBuilder().where(GrouponTaocanEntityDao.Properties.GrouponId.eq(grouponId)).list());
        return grouponTaocanEntities;
    }

    /**
     * 获取客显设置
     *
     * @return
     */
    public CashierDisplayEntity getCashierDisplay() {
        try {
            CashierDisplayEntity cashierDisplayEntity = mCashierDisplayEntityDao.queryBuilder().build().unique();
            return cashierDisplayEntity;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 退菜
     *
     * @param retreatDishes
     */
    public void retreatOrderDish(String orderId, ArrayList<OrderDishEntity> retreatDishes) {
        for (OrderDishEntity orderDishEntity :
                retreatDishes) {
            if (orderDishEntity.getType() == 0) {//非套餐
                changeStock(orderDishEntity.getDishId(), -1 * orderDishEntity.getDishCount());
            } else {//套餐
                ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = getHadOrderedTaocanDish(orderDishEntity);
                for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                        orderTaocanGroupDishEntities) {
                    changeStock(orderTaocanGroupDish.getDishId(), AmountUtils.multiply(-1, AmountUtils.multiply(orderTaocanGroupDish.getTaocanGroupDishCount(), orderDishEntity.getDishCount())));
                }
            }
            OrderDishEntity retreatDish = orderDishEntity;
            OrderDishEntity orderDishEntity1 = mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).build().unique();
            if (AmountUtils.subtract(orderDishEntity1.getDishCount(), orderDishEntity.getDishCount()) > 0) {
                //该菜品未全部退掉
                orderDishEntity1.setDishCount(AmountUtils.subtract(orderDishEntity1.getDishCount(), orderDishEntity.getDishCount()));
                if (orderDishEntity1.getType() == 0) {
                    //非套餐
                    orderDishEntity1.setDishPrice(getOrderDishPrice(orderDishEntity1.getDishId(), orderDishEntity1.getPracticeId(), orderDishEntity1.getSpecifyId(), orderDishEntity1.getDishCount(), orderDishEntity.getOrderDishId()));
                } else {
                    //套餐
                    orderDishEntity1.setDishPrice(getTaocanPrice(orderDishEntity1));
                }
                mOrderDishEntityDao.insertOrReplace(orderDishEntity1);
            } else {
                mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
            }
            retreatDish.setId(null);
            retreatDish.setOrderDishId(UUID.randomUUID().toString());
            retreatDish.setIsOrdered(-1);
            if (retreatDish.getType() == 0) {
                //非套餐
                retreatDish.setDishPrice(getOrderDishPrice(retreatDish.getDishId(), retreatDish.getPracticeId(), retreatDish.getSpecifyId(), retreatDish.getDishCount(), retreatDish.getOrderDishId()));
            } else {
                //套餐
                retreatDish.setDishPrice(getTaocanPrice(retreatDish));
            }
            mOrderDishEntityDao.insertOrReplace(retreatDish);
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity != null && orderEntity.getIsUpload() == 1) {
                Log.d("###", "退菜ID："+orderDishEntity1.getOrderDishId());
                insertUploadData(orderId, orderDishEntity1.getOrderDishId() + "`" + retreatDish.getDishCount()+"`"+retreatDish.getDishPrice()+"`"+retreatDish.getType(), 1);
            }
        }
    }

    /**
     * 获取赠菜总金额
     *
     * @param orderId
     * @return
     */
    public int getPresentMoney(String orderId) {
        int presentMoney = 0;
        ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
        QueryBuilder queryBuilder = mOrderDishEntityDao.queryBuilder();
        orderDishEntities.addAll(queryBuilder.where(queryBuilder.and(OrderDishEntityDao.Properties.OrderId.eq(orderId), OrderDishEntityDao.Properties.IsOrdered.eq(-2))).list());
        for (OrderDishEntity orderDishEntity :
                orderDishEntities) {
            presentMoney += AmountUtils.changeY2F(AmountUtils.multiply(orderDishEntity.getDishPrice(),1));
        }
        return presentMoney;
    }

    /**
     * 赠菜
     *
     * @param presentDishes
     */
    public void presentOrderDish(String orderId, ArrayList<OrderDishEntity> presentDishes) {
        for (OrderDishEntity orderDishEntity :
                presentDishes) {
            OrderDishEntity orderDishEntity1 = mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).build().unique();
            if (AmountUtils.subtract(orderDishEntity1.getDishCount(), orderDishEntity.getDishCount()) > 0) {
                //该菜品未全部退掉
                orderDishEntity1.setDishCount(AmountUtils.subtract(orderDishEntity1.getDishCount(), orderDishEntity.getDishCount()));
                if (orderDishEntity1.getType() == 0) {
                    //非套餐
                    orderDishEntity1.setDishPrice(getOrderDishPrice(orderDishEntity1.getDishId(), orderDishEntity1.getPracticeId(), orderDishEntity1.getSpecifyId(), orderDishEntity1.getDishCount(), orderDishEntity1.getOrderDishId()));
                } else {
                    //套餐
                    orderDishEntity1.setDishPrice(getTaocanPrice(orderDishEntity1));
                }
                mOrderDishEntityDao.insertOrReplace(orderDishEntity1);
            } else {
                mOrderDishEntityDao.queryBuilder().where(OrderDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
                if (orderDishEntity.getType() == 1) {
                    //套餐
                    mOrderTaocanGroupDishEntityDao.queryBuilder().where(OrderTaocanGroupDishEntityDao.Properties.OrderDishId.eq(orderDishEntity.getOrderDishId())).buildDelete().executeDeleteWithoutDetachingEntities();
                }
            }
            orderDishEntity.setId(null);
            orderDishEntity.setOrderDishId(UUID.randomUUID().toString());
            orderDishEntity.setIsOrdered(-2);
            if (orderDishEntity.getType() == 0) {
                //非套餐
                orderDishEntity.setDishPrice(getOrderDishPrice(orderDishEntity.getDishId(), orderDishEntity.getPracticeId(), orderDishEntity.getSpecifyId(), orderDishEntity.getDishCount(), orderDishEntity.getOrderDishId()));
            } else {
                //套餐
                orderDishEntity.setDishPrice(getTaocanPrice(orderDishEntity));
            }
            mOrderDishEntityDao.insertOrReplace(orderDishEntity);
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity != null && orderEntity.getIsUpload() == 1) {
                insertUploadData(orderId, orderDishEntity1.getOrderDishId() + "`" + orderDishEntity.getOrderDishId(), 2);
            }
        }
    }

    /**
     * 获取不同座位类型的统计数据
     *
     * @return
     */
    public ArrayList<TableModel> getTableStatusData() {
        ArrayList<TableModel> tableModels = new ArrayList<>();
        ArrayList<TableEntity> tableEntities = new ArrayList<>();
        tableEntities.addAll(queryAllTableData());
        for (TableEntity tableEntity :
                tableEntities) {
            int seatCount = tableEntity.getTableSeat();
            boolean b = true;
            for (TableModel tableModel :
                    tableModels) {
                if (tableModel.getSeatCount() == seatCount) {
                    //该座位类型已存在
                    b = false;
                    if (tableEntity.getTableStatus() == 0) {
                        tableModel.setTableCount(tableModel.getTableCount() + 1);
                    }
                    break;
                }
            }
            if (b) {
                //该座位类型不存在
                TableModel tableModel = new TableModel();
                if (tableEntity.getTableStatus() == 0) {
                    tableModel.setTableCount(1);
                } else {
                    tableModel.setTableCount(0);
                }
                tableModel.setSeatCount(seatCount);
                tableModels.add(tableModel);
            }
        }
        Comparator comparator = new SortComparator();
        Collections.sort(tableModels, comparator);
        return tableModels;
    }

    public class SortComparator implements Comparator<TableModel> {
        @Override
        public int compare(TableModel lhs, TableModel rhs) {
            return lhs.getSeatCount() - rhs.getSeatCount();
        }
    }

    /**
     * 获取待审核的排号数
     *
     * @return
     */
    public int getOnCheckArrangeCount() {
        return (int) mArrangeEntityDao.queryBuilder().where(ArrangeEntityDao.Properties.ArrangeStatus.eq(0)).buildCount().count();
    }

    /**
     * 获取当前员工
     *
     * @param context
     * @return
     */
    public EmployeeEntity getCurrentEmployee(Context context) {
        EmployeeEntity employeeEntity = null;
        String employeeId = appContext.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", null);
        if (employeeId != null) {
            employeeEntity = getEmployeeById(employeeId);
        }
        return employeeEntity;
    }

    /**
     * 根据员工id获取员工
     *
     * @param employeeId
     * @return
     */
    public EmployeeEntity getEmployeeById(String employeeId) {
        return mEmployeeEntityDao.queryBuilder().where(EmployeeEntityDao.Properties.EmployeeId.eq(employeeId)).build().unique();
    }

    /**
     * 根据用户名和密码获取员工
     *
     * @param employeeAccount
     * @param employeePwd
     * @return
     */
    public EmployeeEntity getEmployeeByAccount(String employeeAccount, String employeePwd) {
        QueryBuilder queryBuilder = mEmployeeEntityDao.queryBuilder();
        return (EmployeeEntity) queryBuilder.where(queryBuilder.and(EmployeeEntityDao.Properties.LoginName.eq(employeeAccount), EmployeeEntityDao.Properties.LoginPsd.eq(employeePwd))).build().unique();
    }

    /**
     * 插入微信预订单
     *
     * @param preOrderModel
     */
    public void insertSchedule(PreOrderModel preOrderModel) {
        try {
            int count = (int) mScheduleEntityDao.queryBuilder().where(ScheduleEntityDao.Properties.ScheduleId.eq(preOrderModel.getId())).buildCount().count();
            if (count > 0) {

            } else {
                ScheduleEntity scheduleEntity = new ScheduleEntity();
                scheduleEntity.setScheduleId(preOrderModel.getId());
                scheduleEntity.setGuestName(preOrderModel.getName());
                scheduleEntity.setGuestPhone(preOrderModel.getMobile());
                scheduleEntity.setMealPeople(preOrderModel.getEatNum());
                scheduleEntity.setMealTime(preOrderModel.getEatTime().getTime());
                scheduleEntity.setScheduleFrom(1);
                scheduleEntity.setScheduleMark(preOrderModel.getOtherAsk());
                scheduleEntity.setScheduleStatus(0);
                mScheduleEntityDao.insertOrReplace(scheduleEntity);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取待审核的预订单数
     *
     * @return
     */
    public int getOnCheckScheduleCount() {
        return (int) mScheduleEntityDao.queryBuilder().where(ScheduleEntityDao.Properties.ScheduleStatus.eq(0)).buildCount().count();
    }

    /**
     * 插入到点点餐中的套餐
     *
     * @param orderTaocanModel
     */
    public void insertTaocanDish(String orderId, OrderTaocanModel orderTaocanModel) {
        OrderDishEntity orderDishEntity = new OrderDishEntity();
        orderDishEntity.setOrderDishId(orderTaocanModel.getId());
        orderDishEntity.setOrderId(orderId);
        orderDishEntity.setDishId(orderTaocanModel.getTaocanId());
        orderDishEntity.setIsOrdered(0);
        orderDishEntity.setDishCount(orderTaocanModel.getNum());
        orderDishEntity.setDishPrice(Float.parseFloat(AmountUtils.changeF2Y(orderTaocanModel.getTaocanPrice())));
        orderDishEntity.setDishName(orderTaocanModel.getTaocanName());
        orderDishEntity.setOrderedTime(System.currentTimeMillis());
        orderDishEntity.setType(1);//设置类型为套餐
        orderDishEntity.setDishTypeId(orderTaocanModel.getTcTypeId());
        orderDishEntity.setDishTypeName(orderTaocanModel.getTcTypeName());
        orderDishEntity.setIsFromWX(1);
        TaocanEntity taocanEntity = queryTaocanById(orderTaocanModel.getTaocanId());
        if (taocanEntity != null) {
            orderDishEntity.setIsAbleDiscount(taocanEntity.getIsAbleDiscount());
            orderDishEntity.setIsRetreat(taocanEntity.getIsAbleRetreat());
            orderDishEntity.setIsPresent(0);
        } else {
            orderDishEntity.setIsAbleDiscount(0);
            orderDishEntity.setIsRetreat(1);
            orderDishEntity.setIsPresent(0);
        }
        mOrderDishEntityDao.insertOrReplace(orderDishEntity);

        ArrayList<OrderTaocanDetailModel> orderTaocanDetailModels = new ArrayList<>();
        orderTaocanDetailModels.addAll(orderTaocanModel.getGoods());
        for (OrderTaocanDetailModel orderTaocanDetailModel :
                orderTaocanDetailModels) {
            OrderTaocanGroupDishEntity orderTaocanGroupDishEntity = new OrderTaocanGroupDishEntity();
            orderTaocanGroupDishEntity.setOrderId(orderId);
            orderTaocanGroupDishEntity.setOrderDishId(orderTaocanModel.getId());
            orderTaocanGroupDishEntity.setOrderDishName(orderTaocanModel.getTaocanName());
            orderTaocanGroupDishEntity.setDishId(orderTaocanDetailModel.getGoodsId());
            DishEntity dishEntity = queryOneDishEntity(orderTaocanDetailModel.getGoodsId());
            if (dishEntity != null) {
                orderTaocanGroupDishEntity.setDishTypeId(dishEntity.getDishTypeId());
                DishTypeEntity dishTypeEntity = getDishTypeByDishTypeId(dishEntity.getDishTypeId());
                if (dishTypeEntity != null) {
                    orderTaocanGroupDishEntity.setDishTypeName(dishTypeEntity.getDishTypeName());
                }
            }
            orderTaocanGroupDishEntity.setTaocanTypeId(orderTaocanModel.getTcTypeId());
            orderTaocanGroupDishEntity.setTaocanTypeName(orderTaocanModel.getTcTypeName());
            orderTaocanGroupDishEntity.setDishName(orderTaocanDetailModel.getGoodsName());
            orderTaocanGroupDishEntity.setOrderTaocanGroupDishId(UUID.randomUUID().toString());
            orderTaocanGroupDishEntity.setSpecifyId(orderTaocanDetailModel.getGuigeId());//商品规格id
            orderTaocanGroupDishEntity.setSpecifyName(orderTaocanDetailModel.getGuigeName());
            float extrasPrice = Float.parseFloat(AmountUtils.changeF2Y(orderTaocanDetailModel.getAddPrice()));
            //添加做法
            String practiceId = orderTaocanDetailModel.getMakeId();
            orderTaocanGroupDishEntity.setPracticeId(practiceId);
            orderTaocanGroupDishEntity.setPracticeName(orderTaocanDetailModel.getMakeName());
            orderTaocanGroupDishEntity.setExtraPrice(extrasPrice);
            orderTaocanGroupDishEntity.setStatus(0);
            orderTaocanGroupDishEntity.setOrderTaocanGroupDishId(orderTaocanDetailModel.getId());
            orderTaocanGroupDishEntity.setTaocanGroupDishId(orderTaocanDetailModel.getTcGoodsId());
            orderTaocanGroupDishEntity.setTaocanGroupId(orderTaocanDetailModel.getTcGroupId());
            orderTaocanGroupDishEntity.setCreateTime(System.currentTimeMillis());
            orderTaocanGroupDishEntity.setTaocanGroupDishCount(orderTaocanDetailModel.getNum());
            orderTaocanGroupDishEntity.setExtraPrice(extrasPrice);
            mOrderTaocanGroupDishEntityDao.insertOrReplace(orderTaocanGroupDishEntity);
        }
    }

    /**
     * 到店点餐插入订单
     *
     * @param context
     * @param tableOrderModel
     */
    public void insertShopOrder(Context context, TableOrderModel tableOrderModel) {
        try {
            int count = (int) mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderId.eq(tableOrderModel.getId())).buildCount().count();
            if (count > 0) {

            } else {
                TableEntity tableEntity = queryOneTableData(tableOrderModel.getTableNo());
                if (tableEntity != null) {
                    OrderEntity orderEntity = new OrderEntity();
                    orderEntity.setOrderId(tableOrderModel.getId());
                    orderEntity.setTotalMoney(tableOrderModel.getTotalPrice());
                    orderEntity.setCloseMoney(0);
                    if (tableOrderModel.getVipNo() != null && tableOrderModel.getVipNo().length() > 0) {
                        orderEntity.setIsVip(1);
                        orderEntity.setVipNo(tableOrderModel.getVipNo());
                        orderEntity.setVipType(tableOrderModel.getVipType());
                    } else {
                        orderEntity.setIsVip(0);
                    }
                    orderEntity.setIsCoupon(0);
                    insertTableOrder(context, tableEntity, orderEntity, tableEntity.getTableSeat(), tableOrderModel.getRemark());
                    updateTableStatus(tableEntity.getTableId());
                    ArrayList<OrderDetailModel> orderDetailes = new ArrayList<>(tableOrderModel.getOrderDetail());
                    for (OrderDetailModel orderDetail :
                            orderDetailes) {
                        //插入商品
                        insertShopOrderDish(orderDetail, tableOrderModel.getId());
                    }
                    ArrayList<OrderTaocanModel> orderTaocanModeles = new ArrayList<>(tableOrderModel.getOrderTaocan());
                    for (OrderTaocanModel orderTaocanModel :
                            orderTaocanModeles) {
                        insertTaocanDish(tableOrderModel.getId(), orderTaocanModel);
                    }
                    addWxOrderMessage(0, getAreaNameById(tableEntity.getAreaId()), tableEntity.getTableCode(), tableEntity.getTableName(), orderEntity.getOrderNumber(), orderEntity.getOrderId());
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 到店点餐插入订单
     *
     * @param context
     * @param tableOrderModel
     */
    public void insertSnackShopOrder(Context context, TableOrderModel tableOrderModel) {
        try {
            int count = (int) mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderId.eq(tableOrderModel.getId())).buildCount().count();
            if (count > 0) {

            } else {
                OrderEntity orderEntity = new OrderEntity();
                SharedPreferences sharedPreferences = appContext.getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                orderEntity.setCashierId(sharedPreferences.getString("employeeId", null));
                orderEntity.setWaiterId(sharedPreferences.getString("employeeId", null));
                orderEntity.setCloseTime(null);
                orderEntity.setIsLimited(isLimitedMeals());
                orderEntity.setIsShift(0);
                orderEntity.setLimitedTime(getLimitedTime());
                orderEntity.setRemindTime(getLimitedRemindTime());
                orderEntity.setOpenTime(System.currentTimeMillis());
                orderEntity.setOrderGuests(1);
                orderEntity.setOrderNumber(getOrderCount() + 1);
                orderEntity.setSerialNumber(getSerialNumber(null,orderEntity.getOrderNumber1()));
                orderEntity.setOrderStatus(0);
                orderEntity.setTableId("");
                orderEntity.setAreaId("");
                orderEntity.setOrderType(0);
                orderEntity.setIsUpload(1);
                orderEntity.setIsJoinedTable(0);
                orderEntity.setIsJoinedOrder(0);
                orderEntity.setCloseMoney(0);
                orderEntity.setSelfTreatMoney(0);
                orderEntity.setVipDiscountMoney(0);
                orderEntity.setCouponDiscountMoney(0);
                orderEntity.setMantissaMoney(0);
                orderEntity.setTreatmentMoney(0);
                orderEntity.setPresentMoney(0);
                orderEntity.setDiscountMoney(0);
                orderEntity.setDiscountTotalMoney(0);
                orderEntity.setInvoiceMoney(0);
                orderEntity.setStoreVersion(1);
                orderEntity.setOrderId(tableOrderModel.getId());
                orderEntity.setTotalMoney(tableOrderModel.getTotalPrice());
                if (tableOrderModel.getVipNo() != null && tableOrderModel.getVipNo().length() > 0) {
                    orderEntity.setIsVip(1);
                    orderEntity.setVipNo(tableOrderModel.getVipNo());
                    orderEntity.setVipType(tableOrderModel.getVipType());
                } else {
                    orderEntity.setIsVip(0);
                }
                orderEntity.setIsCoupon(0);
                mOrderEntityDao.insertOrReplace(orderEntity);
                ArrayList<OrderDetailModel> orderDetailes = new ArrayList<>(tableOrderModel.getOrderDetail());
                for (OrderDetailModel orderDetail :
                        orderDetailes) {
                    //插入商品
                    insertSnackShopOrderDish(orderDetail, tableOrderModel.getId());
                }
                ArrayList<OrderTaocanModel> orderTaocanModeles = new ArrayList<>(tableOrderModel.getOrderTaocan());
                for (OrderTaocanModel orderTaocanModel :
                        orderTaocanModeles) {
                    insertTaocanDish(tableOrderModel.getId(), orderTaocanModel);
                }
                addWxOrderMessage(0, "", "", "", orderEntity.getOrderNumber(), orderEntity.getOrderId());
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入需要同步的数据
     *
     * @param orderId
     * @param dataId
     * @param type
     */
    public void insertUploadData(String orderId, String dataId, int type) {
        UploadDataEntity uploadDataEntity = new UploadDataEntity();
        uploadDataEntity.setUploadDataId(UUID.randomUUID().toString());
        uploadDataEntity.setOrderId(orderId);
        uploadDataEntity.setDataId(dataId);
        uploadDataEntity.setDataType(type);
        uploadDataEntity.setIsHand(0);
        uploadDataEntity.setCount(0);
        uploadDataEntity.setLastTime(System.currentTimeMillis());
        mUploadDataEntityDao.insertOrReplace(uploadDataEntity);
    }

    /**
     * 修改上传数据的调用时间和次数
     */
    public void changeUploadTime(long time, UploadDataEntity uploadDataEntity) {
        uploadDataEntity.setLastTime(time);
        uploadDataEntity.setCount(uploadDataEntity.getCount() + 1);
        mUploadDataEntityDao.insertOrReplace(uploadDataEntity);
    }

    /**
     * 获取同步数据
     *
     * @return
     */
    public ArrayList<UploadDataEntity> getAllUploadData() {
        ArrayList<UploadDataEntity> uploadDataEntities = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mUploadDataEntityDao.queryBuilder();
            uploadDataEntities.addAll(queryBuilder.where(UploadDataEntityDao.Properties.IsHand.eq(0)).list());
            return uploadDataEntities;
        } catch (Exception e) {
            return uploadDataEntities;
        }
    }

    /**
     * 获取所有库存数据
     *
     * @return
     */
    public ArrayList<StockBean> getAllStock() {
        ArrayList<StockBean> stockBeenes = new ArrayList<>();
        ArrayList<SellCheckEntity> sellCheckEntities = new ArrayList<>();
        sellCheckEntities.addAll(mSellCheckEntityDao.queryBuilder().where(SellCheckEntityDao.Properties.IsSellOut.eq(0)).list());
        for (SellCheckEntity sellCheckEntity :
                sellCheckEntities) {
            StockBean stockBean = new StockBean(sellCheckEntity);
            stockBeenes.add(stockBean);
        }
        return stockBeenes;
    }

    /**
     * 获取同步所有营业数据
     *
     * @return
     */
    public ArrayList<UploadDataEntity> getAllBusinessOrder() {
        ArrayList<UploadDataEntity> uploadDataEntities = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mUploadDataEntityDao.queryBuilder();
            uploadDataEntities.addAll(queryBuilder.where(UploadDataEntityDao.Properties.DataType.eq(7)).list());
            return uploadDataEntities;
        } catch (Exception e) {
            return uploadDataEntities;
        }
    }

    /**
     * 修改上传营业数据为手动上传
     *
     * @param orderId
     */
    public void changeToHand(String orderId, int type) {
        QueryBuilder queryBuilder = mUploadDataEntityDao.queryBuilder();
        ArrayList<UploadDataEntity> uploadDataEntities = new ArrayList<>();
        uploadDataEntities.addAll(queryBuilder.where(queryBuilder.and(UploadDataEntityDao.Properties.OrderId.eq(orderId), UploadDataEntityDao.Properties.DataType.eq(type))).list());
        for (UploadDataEntity uploadDataEntity :
                uploadDataEntities) {
            uploadDataEntity.setIsHand(1);
            mUploadDataEntityDao.insertOrReplace(uploadDataEntity);
        }
    }

    /**
     * 交接班
     *
     * @param context
     * @param cashier
     * @param areaId
     */
    public void turnoverConfirm(Context context, int cashier, String areaId) {
        ArrayList<OrderEntity> orderEntities = new ArrayList<>();
        if (cashier == 0) {//全部收银员
            orderEntities.addAll(getSomeOrderEntity(null, 0, null, -1, areaId));
        } else if (cashier == 1) {//当前收银员
            SharedPreferences spf = appContext.getSharedPreferences("loginData", Context.MODE_PRIVATE);
            String cashierId = spf.getString("employeeId", null);
            orderEntities.addAll(getSomeOrderEntity(cashierId, 0, null, -1, areaId));
        }
        for (OrderEntity orderEntity :
                orderEntities) {
            orderEntity.setIsShift(1);
            mOrderEntityDao.insertOrReplace(orderEntity);
        }
    }

    /**
     * 清除已经同步服务器的数据
     */
    public void clearUploadData(String uploadDataId) {
        QueryBuilder queryBuilder = mUploadDataEntityDao.queryBuilder();
        queryBuilder.where(UploadDataEntityDao.Properties.UploadDataId.eq(uploadDataId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 判断桌位是否有合台的账单
     *
     * @param tableId
     * @return
     */
    public boolean isJoinTable(String tableId) {
        QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder().where(OrderEntityDao.Properties.OrderStatus.eq(0));
        return queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.IsJoinedTable.eq(1), OrderEntityDao.Properties.JoinedTableId.eq(tableId))).buildCount().count() > 0;
    }

    /**
     * 获取并单的名称
     *
     * @param orderEntity
     * @return
     */
    public String getJoinName(OrderEntity orderEntity) {
        if (orderEntity != null && orderEntity.getIsJoinedOrder() == 1 && orderEntity.getJoinedOrderId() != null) {
            OrderEntity orderEntity1 = getOneOrderEntity(orderEntity.getJoinedOrderId());
            if (orderEntity1 != null) {
                return "并" + orderEntity1.getOrderNumber1();
            }
        }
        return null;
    }

    /**
     * 根据并单的id获取并单的名称
     *
     * @param joinOrderId
     * @return
     */
    public String getJoinName(String joinOrderId) {
        OrderEntity orderEntity = getOneOrderEntity(joinOrderId);
        if (orderEntity != null && orderEntity.getIsJoinedOrder() == 1 && orderEntity.getJoinedOrderId().equals(joinOrderId)) {
            return "并" + orderEntity.getOrderNumber1();
        } else {
            return "";
        }
    }

    /**
     * 取消某个订单的并单状态
     *
     * @param orderId
     */
    public void cancleOneJoinOrder(String orderId) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null) {
            orderEntity.setIsJoinedOrder(0);
            orderEntity.setJoinedOrderId(null);
            mOrderEntityDao.insertOrReplace(orderEntity);
        }
    }

    /**
     * 插入微信支付
     *
     * @param wxPayBean
     */
    public void insertWxPay(WXPayBean wxPayBean) {
        String orderId = wxPayBean.getOid();
        if (orderId != null) {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity != null) {
                try {
                    switch (wxPayBean.getPt()) {
                        case 2://会员卡支付
                            insertPayMode(orderId, "5", "会员卡", 5, Float.parseFloat(AmountUtils.changeF2Y(wxPayBean.getPay())), 0, wxPayBean.getVno());
                            break;
                        case 1://微信支付
                            String payMoney = AmountUtils.multiply("" + wxPayBean.getPay(), "0.01");
                            insertPayMode(orderId, "2", "微信", 2, Float.valueOf(payMoney), 0, wxPayBean.getPno());
                            break;
                    }
                    if (wxPayBean.getVno() != null && wxPayBean.getVno().length() > 0) {
                        orderEntity.setIsVip(1);
                    } else {
                        orderEntity.setIsVip(0);
                    }
                    orderEntity.setVipNo(wxPayBean.getVno());
                    orderEntity.setVipType(wxPayBean.getVt());
                    orderEntity.setVipBalance(wxPayBean.getVb());
                    if (wxPayBean.getCuid() != null && wxPayBean.getCuid().length() > 0) {
                        orderEntity.setIsCoupon(1);
                        if (wxPayBean.getVp() == 0) {
                            orderEntity.setIsVip(0);
                        }
                    } else {
                        orderEntity.setIsCoupon(0);
                    }
                    orderEntity.setUserCouponId(wxPayBean.getCuid());
                    orderEntity.setCouponId(wxPayBean.getCid());
                    orderEntity.setCouponCondition(wxPayBean.getFc());
                    orderEntity.setCouponFaceValue(wxPayBean.getFv());
                    orderEntity.setCouponType(wxPayBean.getCpt());
                    orderEntity.setIsCouponWithVip(wxPayBean.getVp());
                    orderEntity.setIsCouponDiscountAll(wxPayBean.getVf());
                    orderEntity.setCouponVipno(wxPayBean.getVno());
                    mOrderEntityDao.insertOrReplace(orderEntity);
                    addWxOrderMessage(1, getAreaNameById(orderEntity.getAreaId()), getTableCodeByTableId(orderEntity.getTableId()), getTableNameByTableId(orderEntity.getTableId()), orderEntity.getOrderNumber(), orderId);
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 插入优惠券
     *
     * @param orderId
     * @param offLineWxCouponModel
     */
    public void insertCoupon(String orderId, OffLineWxCouponModel offLineWxCouponModel) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        orderEntity.setIsCoupon(1);
        orderEntity.setUserCouponId(offLineWxCouponModel.getBatch());
        orderEntity.setCouponId(offLineWxCouponModel.getCouponId());
        orderEntity.setCouponCondition(offLineWxCouponModel.getFullCut());
        orderEntity.setCouponFaceValue(offLineWxCouponModel.getFaceValue());
        orderEntity.setCouponType(offLineWxCouponModel.getType());
        orderEntity.setIsCouponWithVip(offLineWxCouponModel.getDisVip());
        orderEntity.setIsCouponDiscountAll(offLineWxCouponModel.getForceDis());
        orderEntity.setCouponVipno(null);
        mOrderEntityDao.insertOrReplace(orderEntity);
    }

    /**
     * 插入微信扫码支付
     *
     * @param payQRBean
     */
    public void insertQRPay(PayQRBean payQRBean) {
        try {
            if (payQRBean.getId() != null) {
                OrderEntity orderEntity = getOneOrderEntity(payQRBean.getId());
                if (orderEntity != null) {
                    String payMoney = AmountUtils.multiply("" + payQRBean.getPayFee(), "0.01");
                    insertPayMode(payQRBean.getId(), "2", "微信", 2, Float.valueOf(payMoney), 0, payQRBean.getPayNo());
                    addWxOrderMessage(2, getAreaNameById(orderEntity.getAreaId()), getTableCodeByTableId(orderEntity.getTableId()), getTableNameByTableId(orderEntity.getTableId()), orderEntity.getOrderNumber(), payQRBean.getId());
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 查询会员卡详情
     */
    public VipCardEntity getVipCardDetail(int type) {
        return mVipCardEntityDao.queryBuilder().where(VipCardEntityDao.Properties.VipCardType.eq(type)).build().unique();
    }

    /**
     * 插入会员卡信息
     */
    public void insertVipDetail(String orderId, String vipNo, int vipType, int vipBalance) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null) {
            orderEntity.setIsVip(1);
            orderEntity.setVipNo(vipNo);
            orderEntity.setVipType(vipType);
            orderEntity.setVipBalance(vipBalance);
            mOrderEntityDao.insertOrReplace(orderEntity);
        }
    }

    /**
     * 插入会员卡信息
     */
    public void insertVipDetailUnuse(String orderId, String vipNo, int vipType, int vipBalance) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null) {
            orderEntity.setIsVip(0);
            orderEntity.setVipNo(vipNo);
            orderEntity.setVipType(vipType);
            orderEntity.setVipBalance(vipBalance);
            mOrderEntityDao.insertOrReplace(orderEntity);
        }
    }

    /**
     * 修改会员卡使用状态
     *
     * @param orderId
     * @param status
     */
    public void changeVipStatus(String orderId, int status) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null) {
            orderEntity.setIsVip(status);
        }
    }

    /**
     * 修改优惠券使用状态
     *
     * @param orderId
     * @param status
     */
    public void changeCouponStatus(String orderId, int status) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null) {
            orderEntity.setIsCoupon(status);
        }
        mOrderEntityDao.insertOrReplace(orderEntity);
        dealWithVoucher(orderEntity, 1);
    }

    /**
     * 判断当前订单的vip卡号是否与vipNo相同
     *
     * @param vipNo
     * @return
     */
    public int isSameVip(String orderId, String vipNo) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity.getVipNo() != null) {
                if (orderEntity.getVipNo().equals(vipNo)) {//同一个vip
                    return 1;
                } else {
                    return 0;//不是同一个vip
                }
            } else {
                return 2;//未使用vip
            }
        } catch (Exception e) {
            return 2;//未使用vip
        }
    }

    /**
     * 判断当前订单是否已有会员卡支付
     *
     * @param orderId
     * @return
     */
    public boolean isHasVipPay(String orderId) {
        QueryBuilder queryBuilder = mPayModeEntityDao.queryBuilder();
        return queryBuilder.where(queryBuilder.and(PayModeEntityDao.Properties.OrderId.eq(orderId), PayModeEntityDao.Properties.PaymentType.eq(5))).buildCount().count() > 0;
    }

    /**
     * 判断当前订单是否有使用会员卡或优惠券
     *
     * @param orderId
     * @return
     */
    public boolean isHasVipOrCouponDiscount(String orderId) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            return (orderEntity.getIsCoupon() != null && orderEntity.getIsCoupon() == 1) || (orderEntity.getIsVip() != null && orderEntity.getIsVip() == 1);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 取消会员卡优惠
     *
     * @param orderId
     */
    public void cancleVipDiscount(String orderId) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            orderEntity.setIsVip(0);
            mOrderEntityDao.insertOrReplace(orderEntity);
        } catch (Exception e) {

        }
    }


    /**
     * 判断是否需要清除优惠券优惠
     *
     * @param orderId
     * @return
     */
    public boolean isCouponWithVip(String orderId) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity.getIsCoupon() != null && orderEntity.getIsCoupon() == 1 && orderEntity.getIsCouponWithVip() != null) {
                return orderEntity.getIsCouponWithVip() == 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否需要清除会员卡打折
     *
     * @param orderId
     * @return
     */
    public boolean isVipWithCoupon(String orderId) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity.getIsVip() != null && orderEntity.getIsVip() == 1 && orderEntity.getIsCoupon() != null && orderEntity.getIsCouponWithVip() != null) {
                return orderEntity.getIsCouponWithVip() == 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取会员卡卡号
     *
     * @param orderId
     * @return
     */
    public String getVipNoByOrderId(String orderId) {
        OrderEntity orderEntity = getOneOrderEntity(orderId);
        if (orderEntity != null && orderEntity.getVipNo() != null) {
            return orderEntity.getVipNo();
        } else {
            return null;
        }
    }

    //更换会员时判断优惠券使用条件
    public boolean canUseCoupon(String orderId, String vipNo) {
        boolean canUse = false;

        return canUse;
    }

    //赠菜时判断优惠券使用条件
    public boolean canUseCouponPresent(String orderId, ArrayList<OrderDishEntity> presentDishes) {
        boolean canUse = true;
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity.getUserCouponId() != null && orderEntity.getIsCoupon() != null && orderEntity.getIsCoupon() == 1 && orderEntity.getCouponCondition() != null) {
                //该订单有使用优惠券
                int dishPriceSum = 0;
                if (orderEntity.getIsCouponDiscountAll() == 1) {
                    //适用于所有菜品
                    dishPriceSum = getAllDishPriceSum(orderId, presentDishes);
                } else {
                    //只适用于打折商品
                    dishPriceSum = getDiscountDishPriceSum(orderId, presentDishes);
                }
                canUse = dishPriceSum >= orderEntity.getCouponCondition();
            }
        } catch (Exception e) {
            canUse = true;
        }
        return canUse;
    }

    //退菜时判断优惠券使用条件
    public boolean canUseCouponRetreat(String orderId, ArrayList<OrderDishEntity> retreatDishes) {
        boolean canUse = true;
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity.getUserCouponId() != null && orderEntity.getIsCoupon() != null && orderEntity.getIsCoupon() == 1 && orderEntity.getCouponCondition() != null) {
                //该订单有优惠券
                int dishPriceSum = 0;
                if (orderEntity.getIsCouponDiscountAll() == 1) {
                    //适用于所有菜品
                    dishPriceSum = getAllDishPriceSum(orderId, retreatDishes);
                } else {
                    //只适用于打折商品
                    dishPriceSum = getDiscountDishPriceSum(orderId, retreatDishes);
                }
                canUse = dishPriceSum >= orderEntity.getCouponCondition();
            }
        } catch (Exception e) {
            canUse = true;
        }
        return canUse;
    }

    //退菜时判断优惠券使用条件
    public boolean canUseCoupon(String orderId, OffLineWxCouponModel offLineWxCouponModel) {
        boolean canUse = true;
        try {
            if (offLineWxCouponModel != null) {
                //该订单有优惠券
                int dishPriceSum = 0;
                if (offLineWxCouponModel.getForceDis() == 1) {
                    //适用于所有菜品
                    dishPriceSum = getAllDishPriceSum(orderId, null);
                } else {
                    //只适用于打折商品
                    dishPriceSum = getDiscountDishPriceSum(orderId, null);
                }
                canUse = dishPriceSum >= offLineWxCouponModel.getFullCut();
            }
        } catch (Exception e) {
            canUse = true;
        }
        return canUse;
    }

    //重新启用优惠券时判断使用条件
    public boolean canUseCoupon(String orderId) {
        boolean canUse = false;
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            if (orderEntity.getUserCouponId() != null && orderEntity.getIsCoupon() != null && orderEntity.getIsCoupon() == 0 && orderEntity.getCouponCondition() != null) {
                //该订单有优惠券
                int dishPriceSum = 0;
                if (orderEntity.getIsCouponDiscountAll() == 1) {
                    //适用于所有菜品
                    dishPriceSum = getAllDishPriceSum(orderId, null);
                } else {
                    //只适用于打折商品
                    dishPriceSum = getDiscountDishPriceSum(orderId, null);
                }
                canUse = dishPriceSum >= orderEntity.getCouponCondition();
            }
        } catch (Exception e) {
            canUse = false;
        }
        return canUse;
    }

    /**
     * 获取优惠券使用前的总金额
     *
     * @param orderId
     * @param reduceDishes
     * @return
     */
    public int getAllDishPriceSum(String orderId, ArrayList<OrderDishEntity> reduceDishes) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
            DiscountHistoryEntity mDiscountHistoryEntity = getDiscount(orderId);
            orderDishEntities.addAll(queryOrderedDish1(orderId));
            double totalPrice = 0;
            double dishPrice;//商品原价
            String dishPriceStr;
            int rate[];
            double rate0;
            for (OrderDishEntity orderDishEntity :
                    orderDishEntities) {
                dishPriceStr = null;
                if (reduceDishes != null) {
                    for (OrderDishEntity reduceDish :
                            reduceDishes) {
                        if (orderDishEntity.getOrderDishId().equals(reduceDish.getOrderDishId())) {
                            //当前菜品需要进行减数量
                            if (orderDishEntity.getType() == 0) {
                                dishPriceStr = AmountUtils.changeY2F(getOrderDishPrice(orderDishEntity.getDishId(), orderDishEntity.getPracticeId(), orderDishEntity.getSpecifyId(), orderDishEntity.getDishCount() - reduceDish.getDishCount(), orderDishEntity.getOrderDishId()) + "");
                            } else if (orderDishEntity.getType() == 1) {
                                dishPriceStr = AmountUtils.changeY2F(AmountUtils.multiply(getOrderedTaocanPrice(orderDishEntity) + "", "" + (orderDishEntity.getDishCount() - reduceDish.getDishCount())));
                            }
                        }
                    }
                }
                if (orderDishEntity.getType() == 0) {
                    //非套餐
                    dishPriceStr = dishPriceStr == null ? AmountUtils.changeY2F(orderDishEntity.getDishPrice() + "") : dishPriceStr;
                    dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                } else {
                    //套餐
                    dishPriceStr = dishPriceStr == null ? AmountUtils.changeY2F(AmountUtils.multiply(getOrderedTaocanPrice(orderDishEntity) + "", "" + orderDishEntity.getDishCount())) : dishPriceStr;
                    dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                }
                if (orderDishEntity.getIsOrdered() == 1) {
                    //已下单
                    if (orderDishEntity.getType() == 0) {
                        //非套餐
                        rate = getDishDiscountRate(orderEntity, orderDishEntity, mDiscountHistoryEntity);
                        rate0 = AmountUtils.multiply(rate[0], 0.01);
                        dishPrice = AmountUtils.multiply(rate0, dishPrice, 1);
                    } else {
                        //套餐
                        rate = getTaocanDishDiscountRate(orderEntity, orderDishEntity, mDiscountHistoryEntity);
                        rate0 = AmountUtils.multiply(rate[0], 0.01);
                        dishPrice = AmountUtils.multiply(rate0, dishPrice, 1);
                    }
                }
                totalPrice = AmountUtils.add(totalPrice, dishPrice);
            }
            return AmountUtils.changeY2F(totalPrice);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getDiscountDishPriceSum(String orderId, ArrayList<OrderDishEntity> reduceDishes) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            ArrayList<OrderDishEntity> orderDishEntities = new ArrayList<>();
            DiscountHistoryEntity mDiscountHistoryEntity = getDiscount(orderId);
            orderDishEntities.addAll(queryOrderedDish2(orderId));
            double totalPrice = 0;
            double dishPrice;//商品原价
            String dishPriceStr;
            int rate[];
            double rate0;
            for (OrderDishEntity orderDishEntity :
                    orderDishEntities) {
                dishPriceStr = null;
                if (reduceDishes != null) {
                    for (OrderDishEntity reduceDish :
                            reduceDishes) {
                        if (orderDishEntity.getOrderDishId().equals(reduceDish.getOrderDishId())) {
                            //当前菜品需要进行减数量
                            if (orderDishEntity.getType() == 0) {
                                dishPriceStr = AmountUtils.changeY2F(getOrderDishPrice(orderDishEntity.getDishId(), orderDishEntity.getPracticeId(), orderDishEntity.getSpecifyId(), orderDishEntity.getDishCount() - reduceDish.getDishCount(), orderDishEntity.getOrderDishId()) + "");
                            } else if (orderDishEntity.getType() == 1) {
                                dishPriceStr = AmountUtils.changeY2F(AmountUtils.multiply(getOrderedTaocanPrice(orderDishEntity) + "", "" + (orderDishEntity.getDishCount() - reduceDish.getDishCount())));
                            }
                        }
                    }
                }
                if (orderDishEntity.getType() == 0) {
                    //非套餐
                    dishPriceStr = dishPriceStr == null ? AmountUtils.changeY2F(orderDishEntity.getDishPrice() + "") : dishPriceStr;
                    dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                } else {
                    //套餐
                    dishPriceStr = dishPriceStr == null ? AmountUtils.changeY2F(AmountUtils.multiply(getOrderedTaocanPrice(orderDishEntity) + "", "" + orderDishEntity.getDishCount())) : dishPriceStr;
                    dishPrice = AmountUtils.changeF2Y(dishPriceStr).doubleValue();
                }
                if (orderDishEntity.getIsOrdered() == 1) {
                    //已下单
                    if (orderDishEntity.getType() == 0) {
                        //非套餐
                        rate = getDishDiscountRate(orderEntity, orderDishEntity, mDiscountHistoryEntity);
                        rate0 = (double) (rate[0]) / 100;
                        dishPrice = AmountUtils.multiply(rate0, dishPrice, 1);
                    } else {
                        //套餐
                        rate = getTaocanDishDiscountRate(orderEntity, orderDishEntity, mDiscountHistoryEntity);
                        rate0 = (double) (rate[0]) / 100;
                        dishPrice = AmountUtils.multiply(rate0, dishPrice, 1);
                    }
                }
                totalPrice = AmountUtils.add(totalPrice, dishPrice);
            }
            return AmountUtils.changeY2F(totalPrice);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取外卖各个状态下订单数
     *
     * @return
     */
    public int[] getTakeoutCounts() {
        int[] results = {0, 0, 0, 0};
        results[0] = (int) mTakeOutOrderEntityDao.queryBuilder().where(TakeOutOrderEntityDao.Properties.TakeoutStatus.eq(0)).buildCount().count();
        results[2] = (int) mTakeOutOrderEntityDao.queryBuilder().where(TakeOutOrderEntityDao.Properties.TakeoutStatus.eq(1)).buildCount().count();
        results[3] = (int) mTakeOutOrderEntityDao.queryBuilder().where(TakeOutOrderEntityDao.Properties.TakeoutStatus.eq(2)).buildCount().count();
        results[1] = (int) mTakeOutOrderEntityDao.queryBuilder().where(TakeOutOrderEntityDao.Properties.TakeoutStatus.eq(4)).buildCount().count();
        return results;
    }

    /**
     * 获取各种消息未读数量
     *
     * @return
     */
    public int[] getUnreadMessageCounts() {
        int[] results = {0, 0, 0};
        results[0] = (int) mWXMessageEntityDao.queryBuilder().where(WXMessageEntityDao.Properties.IsRead.eq(0)).buildCount().count();
        results[1] = (int) mStoreMessageEntityDao.queryBuilder().where(StoreMessageEntityDao.Properties.IsRead.eq(0)).buildCount().count();
        results[2] = (int) mSystemMessageEntityDao.queryBuilder().where(SystemMessageEntityDao.Properties.IsRead.eq(0)).buildCount().count();
        return results;
    }

    /**
     * 判断当前是否有商品库存小于等于预警值，并且未估清
     *
     * @return
     */
    public boolean isStockWaring() {
        ArrayList<SellCheckEntity> sellCheckEntities = new ArrayList<>();
        sellCheckEntities.addAll(getStockData());
        for (SellCheckEntity sellCheck :
                sellCheckEntities) {
            if (sellCheck.getEarlyWarning() != null && sellCheck.getStock() <= sellCheck.getEarlyWarning()) {
                if (!isDishChing(sellCheck.getDishId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取团购结账后是否打开钱箱
     *
     * @return
     */
    public boolean isGrouponOpenCashBox() {
        try {
            ShopPaymentEntity shopPaymentEntity = mShopPaymentEntityDao.queryBuilder().where(ShopPaymentEntityDao.Properties.PaymentType.eq(4)).build().unique();
            if (shopPaymentEntity != null) {
                return shopPaymentEntity.getIsOpenCashBox() == 1 ? true : false;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取会员卡结账后是否打开钱箱
     *
     * @return
     */
    public boolean isVipOpenCashBox() {
        try {
            ShopPaymentEntity shopPaymentEntity = mShopPaymentEntityDao.queryBuilder().where(ShopPaymentEntityDao.Properties.PaymentType.eq(5)).build().unique();
            if (shopPaymentEntity != null) {
                return shopPaymentEntity.getIsOpenCashBox() == 1 ? true : false;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 修改外卖单配送员
     *
     * @param takeOutOrderEntity
     * @param sendPersonEntity
     */
    public void addEmployeeForTakeout(TakeOutOrderEntity takeOutOrderEntity, SendPersonEntity sendPersonEntity) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(takeOutOrderEntity.getOrderId());
            orderEntity.setDispacherId(sendPersonEntity.getSendPersonId());
            orderEntity.setDispacherName(sendPersonEntity.getSendPersonName());
            orderEntity.setDispacherTel(sendPersonEntity.getSendPersonPhone());
            orderEntity.setDispacherTc(sendPersonEntity.getSendPersonTC());
            orderEntity.setDispacherType(0);
            orderEntity.setDispacherExtralMoney(0);
            mOrderEntityDao.insertOrReplace(orderEntity);
        } catch (Exception e) {

        }
    }

    /**
     * 修改外卖单配送员
     *
     * @param meituanDispacherBean
     */
    public void addEmployeeForTakeout(MeituanDispacherBean meituanDispacherBean) {
        try {
            String otherOrderId = meituanDispacherBean.getOrderId();
            TakeOutOrderEntity takeOutOrderEntity = mTakeOutOrderEntityDao.queryBuilder().where(TakeOutOrderEntityDao.Properties.OtherOrderId.eq(otherOrderId)).build().unique();
            OrderEntity orderEntity = getOneOrderEntity(takeOutOrderEntity.getOrderId());
            orderEntity.setDispacherId(null);
            orderEntity.setDispacherName(meituanDispacherBean.getDispatcherName());
            orderEntity.setDispacherTel(meituanDispacherBean.getDispatcherMobile());
            orderEntity.setDispacherTc(0);
            orderEntity.setDispacherType(1);
            orderEntity.setDispacherExtralMoney(0);
            mOrderEntityDao.insertOrReplace(orderEntity);
        } catch (Exception e) {

        }
    }

    /**
     * 插入堂食消息
     *
     * @param messageType
     * @param areaName
     * @param tableCode
     * @param tableName
     * @param orderNum
     */
    public void addWxOrderMessage(int messageType, String areaName, String tableCode, String tableName, int orderNum, String orderId) {
        WxOrderMessageEntity wxOrderMessageEntity = new WxOrderMessageEntity();
        wxOrderMessageEntity.setWxOrderMessageId(UUID.randomUUID().toString());
        wxOrderMessageEntity.setWxOrderMessageName(messageType);//0:扫码点餐；1:微信支付
        wxOrderMessageEntity.setAreaName(areaName);
        wxOrderMessageEntity.setTableName(tableName);
        wxOrderMessageEntity.setTableCode(tableCode);
        wxOrderMessageEntity.setOrderNumber(orderNum);
        wxOrderMessageEntity.setOrderId(orderId);
        mWxOrderMessageEntityDao.insertOrReplace(wxOrderMessageEntity);
    }

    /**
     * 获取所有订单消息
     *
     * @return
     */
    public ArrayList<WxOrderMessageEntity> getAllWxOrderMessages() {
        ArrayList<WxOrderMessageEntity> results = new ArrayList<>();
        results.addAll(mWxOrderMessageEntityDao.queryBuilder().list());
        return results;
    }

    /**
     * 获取订单消息总数
     *
     * @return
     */
    public int getAllWxOrderMessageCount() {
        int result = 0;
        try {
            result = (int) mWxOrderMessageEntityDao.queryBuilder().buildCount().count();
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * 删除订单消息
     *
     * @param orderId
     * @param type
     */
    public void deleteWxOrderMessage(String orderId, int type) {
        QueryBuilder queryBuilder = mWxOrderMessageEntityDao.queryBuilder();
        queryBuilder.where(queryBuilder.and(WxOrderMessageEntityDao.Properties.WxOrderMessageName.eq(type), WxOrderMessageEntityDao.Properties.OrderId.eq(orderId))).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 清空所有订单消息
     */
    public void deleteAllWxOrderMessage() {
        mWxOrderMessageEntityDao.queryBuilder().buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void insertPrintResult(String orderId, String printIp, String result) {
        PrintResultEntity printResultEntity = new PrintResultEntity();
        printResultEntity.setPrintResultId(UUID.randomUUID().toString());
        printResultEntity.setOrderId(orderId);
        printResultEntity.setPrintIp(printIp);
        printResultEntity.setPrintResult(result);
        printResultEntity.setPrintTime(parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        Log.d("###", "打印机状态： " + printResultEntity.toString());
    }

    /**
     * 插入交接班记录
     *
     * @param turnoverHistoryEntity
     */
    public void insertTurnoverHistory(TurnoverHistoryEntity turnoverHistoryEntity) {
        mTurnoverHistoryEntityDao.insertOrReplace(turnoverHistoryEntity);
    }

    /**
     * 获取交接班记录
     *
     * @return
     */
    public ArrayList<TurnoverHistoryEntity> getTurnoverHistory() {
        ArrayList<TurnoverHistoryEntity> turnoverHistoryEntities = new ArrayList<>();
        try {
            turnoverHistoryEntities.addAll(mTurnoverHistoryEntityDao.queryBuilder().list());
        } catch (Exception e) {

        }
        return turnoverHistoryEntities;
    }

    /**
     * 获取最近一次交接班记录
     *
     * @return
     */
    public TurnoverHistoryEntity getLastTurnoverHistory() {
        try {
            TurnoverHistoryEntity turnoverHistoryEntity = mTurnoverHistoryEntityDao.queryBuilder().orderDesc(TurnoverHistoryEntityDao.Properties.CreateTime).build().list().get(0);
            return turnoverHistoryEntity;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取营业结束时间
     *
     * @return
     */
    public long getOverTime() {
        long currentTime = System.currentTimeMillis();
        String zeroTime = CustomMethod.parseTime(currentTime, "yyyy-MM-dd");
        String yestodayZeroTime = CustomMethod.parseTime(currentTime - 24 * 60 * 60 * 1000, "yyyy-MM-dd");
        long overTime = CustomMethod.parseTime(CustomMethod.parseTime(currentTime, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        try {
            ShopTimeEntity shopTimeEntity = mShopTimeEntityDao.queryBuilder().list().get(0);
            if (shopTimeEntity.getShopDayType() == 0) {
                //当日
                overTime = CustomMethod.parseTime(yestodayZeroTime + " " + shopTimeEntity.getShopEndTime(), "yyyy-MM-dd HH:mm");
            } else {
                //次日
                overTime = CustomMethod.parseTime(zeroTime + " " + shopTimeEntity.getShopEndTime(), "yyyy-MM-dd HH:mm");
            }
        } catch (Exception e) {
        }
        return overTime;
    }

    /**
     * 插入自定义牌号
     */
    public boolean addCustomTableCode(String tableCode) {
        int count = (int) mTableEntityDao.queryBuilder().where(TableEntityDao.Properties.TableCode.eq(tableCode)).buildCount().count();
        if (count > 0) {
            return false;
        }
        int count1 = (int) mTableCodeEntityDao.queryBuilder().where(TableCodeEntityDao.Properties.TableCode.eq(tableCode)).buildCount().count();
        if (count1 > 0) {
            return false;
        }
        TableCodeEntity tableCodeEntity = new TableCodeEntity();
        tableCodeEntity.setTableCode(tableCode);
        mTableCodeEntityDao.insertOrReplace(tableCodeEntity);
        return true;
    }

    public ArrayList<TableCodeBean> getAllTableCodeBean() {
        ArrayList<TableCodeBean> tableCodeBeans = new ArrayList<>();
        try {
            ArrayList<AreaEntity> areaEntities = queryAreaData();
            ArrayList<TableEntity> tableEntities = new ArrayList<>();
            for (AreaEntity area :
                    areaEntities) {
                TableCodeBean bean0 = new TableCodeBean(area.getAreaId(), area.getAreaName(), 0, area.getAreaName());
                tableCodeBeans.add(bean0);
                tableEntities.clear();
                tableEntities.addAll(queryTableData(area.getAreaId()));
                for (TableEntity table :
                        tableEntities) {
                    TableCodeBean bean = new TableCodeBean(area.getAreaId(), area.getAreaName(), 1, table.getTableCode());
                    tableCodeBeans.add(bean);
                }
            }
            tableEntities.clear();
            ArrayList<TableCodeEntity> tableCodeEntities = new ArrayList<>();
            tableCodeEntities.addAll(mTableCodeEntityDao.queryBuilder().orderAsc(TableCodeEntityDao.Properties.TableCode).list());
            TableCodeBean bean0 = new TableCodeBean("自定义", "自定义", 0, "");
            tableCodeBeans.add(bean0);
            for (TableCodeEntity tableCode :
                    tableCodeEntities) {
                TableCodeBean bean = new TableCodeBean("自定义", "自定义", 1, tableCode.getTableCode());
                tableCodeBeans.add(bean);
            }
        } catch (Exception e) {

        }
        return tableCodeBeans;
    }

    /**
     * 删除自定义牌号
     *
     * @param customTableCode
     */
    public void deleteCustomTableCode(String customTableCode) {
        mTableCodeEntityDao.queryBuilder().where(TableCodeEntityDao.Properties.TableCode.eq(customTableCode)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 新建快餐版订单
     */
    public String createSnackOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(UUID.randomUUID().toString());
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        orderEntity.setCashierId(sharedPreferences.getString("employeeId", null));
        orderEntity.setWaiterId(sharedPreferences.getString("employeeId", null));
        orderEntity.setCloseTime(null);
        orderEntity.setIsLimited(isLimitedMeals());
        orderEntity.setIsShift(0);
        orderEntity.setLimitedTime(getLimitedTime());
        orderEntity.setRemindTime(getLimitedRemindTime());
        orderEntity.setOpenTime(System.currentTimeMillis());
        orderEntity.setOrderGuests(1);
        orderEntity.setOrderNumber(getOrderCount() + 1);
        orderEntity.setSerialNumber(getSerialNumber(null, orderEntity.getOrderNumber1()));
        orderEntity.setOrderStatus(0);
        orderEntity.setTableId("");
        orderEntity.setAreaId("");
        orderEntity.setOrderType(0);
        orderEntity.setIsUpload(0);
        orderEntity.setIsJoinedTable(0);
        orderEntity.setIsJoinedOrder(0);
        orderEntity.setTotalMoney(0);
        orderEntity.setCloseMoney(0);
        orderEntity.setSelfTreatMoney(0);
        orderEntity.setVipDiscountMoney(0);
        orderEntity.setCouponDiscountMoney(0);
        orderEntity.setMantissaMoney(0);
        orderEntity.setTreatmentMoney(0);
        orderEntity.setPresentMoney(0);
        orderEntity.setDiscountMoney(0);
        orderEntity.setDiscountTotalMoney(0);
        orderEntity.setInvoiceMoney(0);
        orderEntity.setStoreVersion(1);
        mOrderEntityDao.insertOrReplace(orderEntity);
        return orderEntity.getOrderId();
    }

    /**
     * 获取所有快餐版订单
     *
     * @return
     */
    public ArrayList<OrderEntity> getAllSnackOrders() {
        ArrayList<OrderEntity> results = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
            results.addAll(queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.OrderStatus.eq(0), OrderEntityDao.Properties.StoreVersion.eq(1))).list());
        } catch (Exception e) {

        }
        return results;
    }

    /**
     * 更换牌号
     *
     * @param orderId
     * @param tableCode
     */
    public void changeTableCode(String orderId, String tableCode) {
        try {
            OrderEntity orderEntity = getOneOrderEntity(orderId);
            orderEntity.setTableId(tableCode);
            mOrderEntityDao.insertOrReplace(orderEntity);
        } catch (Exception e) {

        }
    }

    /**
     * 判断牌号是否在使用中
     *
     * @param tableCode
     * @return
     */
    public boolean isTableCodeUsed(String tableCode) {
        try {
            QueryBuilder queryBuilder = mOrderEntityDao.queryBuilder();
            int count = (int) queryBuilder.where(queryBuilder.and(OrderEntityDao.Properties.StoreVersion.eq(1), OrderEntityDao.Properties.OrderStatus.eq(0), OrderEntityDao.Properties.TableId.eq(tableCode))).buildCount().count();
            return count > 0;
        } catch (Exception e) {
            return true;
        }
    }

    public ArrayList<SendPersonEntity> getSendPerson() {
        ArrayList<SendPersonEntity> results = new ArrayList<>();
        try {
            results.addAll(mSendPersonEntityDao.queryBuilder().list());
        } catch (Exception e) {

        }
        return results;
    }

    public SendPersonEntity getSendPersonById(String sendPersonId) {
        try {
            return mSendPersonEntityDao.queryBuilder().where(SendPersonEntityDao.Properties.SendPersonId.eq(sendPersonId)).unique();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取所有客单备注
     *
     * @return
     */
    public ArrayList<String> getAllMarks() {
        ArrayList<PrintRemarkEntity> printRemarkEntities = new ArrayList<>();
        ArrayList<String> results = new ArrayList<>();
        printRemarkEntities.addAll(mPrintRemarkEntityDao.queryBuilder().list());
        for (PrintRemarkEntity printRemark :
                printRemarkEntities) {
            results.add(printRemark.getPrintRemarkName());
        }
        return results;
    }

    /**
     * 获取原料
     *
     * @param materialId
     * @return
     */
    public MaterialEntity getMaterialById(String materialId) {
        MaterialEntity materialEntity = null;
        try {
            QueryBuilder queryBuilder = mMaterialEntityDao.queryBuilder();
            materialEntity = (MaterialEntity) queryBuilder.where(MaterialEntityDao.Properties.MaterialId.eq(materialId)).build().unique();
        } catch (Exception e) {

        }
        return materialEntity;
    }

    public ArrayList<DishTypeMaterialEntity> getDishTypeMaterial(String dishId) {
        ArrayList<DishTypeMaterialEntity> results = new ArrayList<>();
        try {
            DishEntity dishEntity = queryOneDishEntity(dishId);
            QueryBuilder queryBuilder = mDishTypeMaterialEntityDao.queryBuilder();
            results.addAll(queryBuilder.where(DishTypeMaterialEntityDao.Properties.DishTypeId.eq(dishEntity.getDishTypeId())).build().list());
        } catch (Exception e) {

        }
        return results;
    }

    public DishSelectedMaterialEntity getOneSelectedMaterialEntity(String orderDishId, String dishTypeMaterialId) {
        try {
            QueryBuilder queryBuilder = mDishSelectedMaterialEntityDao.queryBuilder();
            DishSelectedMaterialEntity dishSelectedMaterialEntity = (DishSelectedMaterialEntity) queryBuilder.where(queryBuilder.and(DishSelectedMaterialEntityDao.Properties.OrderDishId.eq(orderDishId), DishSelectedMaterialEntityDao.Properties.DishTypeMaterialId.eq(dishTypeMaterialId))).build().unique();
            return dishSelectedMaterialEntity;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<DishSelectedMaterialEntity> getSelectedDishMaterial(String orderDishId) {
        ArrayList<DishSelectedMaterialEntity> results = new ArrayList<>();
        try {
            QueryBuilder queryBuilder = mDishSelectedMaterialEntityDao.queryBuilder();
            results.addAll(queryBuilder.where(DishSelectedMaterialEntityDao.Properties.OrderDishId.eq(orderDishId)).build().list());
        } catch (Exception e) {

        }
        return results;
    }


    /**
     * @param orderId
     * @param dishId
     * @param orderDishId
     * @return
     */
    public ArrayList<DishSelectedMaterialEntity> getSelectedMaterial(String orderId, String dishId, String orderDishId) {
        ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities = new ArrayList<>();
        ArrayList<DishTypeMaterialEntity> dishTypeMaterialEntities = new ArrayList<>();
        try {
            dishTypeMaterialEntities.addAll(getDishTypeMaterial(dishId));
            for (DishTypeMaterialEntity dishTypeMaretial :
                    dishTypeMaterialEntities) {
                DishSelectedMaterialEntity dishSelectedMaterialEntity = getOneSelectedMaterialEntity(orderDishId, dishTypeMaretial.getDishTypeMaterialId());
                if (dishSelectedMaterialEntity == null) {
                    MaterialEntity materialEntity = mMaterialEntityDao.queryBuilder().where(MaterialEntityDao.Properties.MaterialId.eq(dishTypeMaretial.getMaterialId())).build().unique();
                    if (materialEntity != null) {
                        dishSelectedMaterialEntity = new DishSelectedMaterialEntity();
                        dishSelectedMaterialEntity.setDishSelectedMaterialId(UUID.randomUUID().toString());
                        dishSelectedMaterialEntity.setDishTypeMaterialId(dishTypeMaretial.getDishTypeMaterialId());
                        dishSelectedMaterialEntity.setMaterialId(dishTypeMaretial.getMaterialId());
                        dishSelectedMaterialEntity.setMaterialName(materialEntity.getMaterialName());
                        dishSelectedMaterialEntity.setMaterialPrice(materialEntity.getMaterialPrice());
                        dishSelectedMaterialEntity.setSelectedCount(0);
                        dishSelectedMaterialEntity.setOrderId(orderId);
                        dishSelectedMaterialEntity.setOrderDishId(orderDishId);
                        dishSelectedMaterialEntity.setTotalPrice(0);
                        dishSelectedMaterialEntities.add(dishSelectedMaterialEntity);
                    }
                } else {
                    dishSelectedMaterialEntities.add(dishSelectedMaterialEntity);
                }
            }
        } catch (Exception e) {

        }
        return dishSelectedMaterialEntities;
    }

    /**
     * 获取菜品物料
     *
     * @param orderDishId
     * @return
     */
    public String getDishMaterialJson(String orderDishId) {
        ArrayList<DishSelectedMaterialEntity> dishSelectedMaterialEntities = new ArrayList<>();
        dishSelectedMaterialEntities.addAll(getSelectedDishMaterial(orderDishId));
        if (dishSelectedMaterialEntities.size() > 0) {
            return JSON.toJSONString(dishSelectedMaterialEntities);
        } else {
            return null;
        }
    }
}