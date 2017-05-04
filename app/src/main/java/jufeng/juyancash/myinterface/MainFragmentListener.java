package jufeng.juyancash.myinterface;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import jufeng.juyancash.bean.DishModel;
import jufeng.juyancash.bean.DishTypeCollectionItemBean;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.BillAccountHistoryEntity;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.GrouponEntity;
import jufeng.juyancash.dao.GrouponTaocanEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.ScheduleEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.dao.TurnoverHistoryEntity;
import jufeng.juyancash.dao.WXMessageEntity;

/**
 * Created by Administrator102 on 2017/1/6.
 */

public interface MainFragmentListener {
    void selectMenu(int checked, Bundle bundle);

    void onChangeTakeoutStatusCount();

    void onTakeOutOrderClick(TakeOutOrderEntity takeOutOrderEntity, int type);

    void onOrderRightClick(OrderEntity orderEntity, int employeeId, int shift, String paymodeId, int date, String type);

    void onOrderClick(TakeOutOrderEntity takeOutOrderEntity);

    void onCancleClick(TakeOutOrderEntity takeOutOrderEntity);

    void onTransferClick(TakeOutOrderEntity takeOutOrderEntity);

    void onOverClick(TakeOutOrderEntity takeOutOrderEntity);

    void onPassClick(TakeOutOrderEntity takeOutOrderEntity);

    void onPrintData(String orderId);

    void onPrintTakeOutOrder(String orderId);

    void onScheduleSuccess();

    void onSelectTime(Calendar dateNow);

    void orderCollection(int cashier, int shift, String payModeId, String payModeName, int date, String type);

    void orderCashier(String cashierId, int shift, String payModeId, String payModeName, int date, String type);

    void typeCollection(int cashier, int shift, String payModeId, String payModeName, int date, String type);

    void nothing();

    void onOrderRightStatisticClick(DishTypeModel dishTypeModel, int shift, int date);

    void selectWXMessage(WXMessageEntity wxMessageEntity);

    void openSchedule(TableEntity tableEntity);

    void openScheduleHistory(TableEntity tableEntity);

    void lockTable(TableEntity tableEntity);

    void unlockTable(TableEntity tableEntity);

    //    void syncData(String orderId);
    void syncDataLater(String orderId);

    void openTable(String tableId, String orderId);

    void joinTable(String tableId, String orderId);

    void changeTable(String tableId, String orderId);

    void presentDish(String orderId, String tableId, int type);

    void retreatDish(String orderId, String tableId, int type);

    void remindDish(String orderId, String tableId, int type);

    void cashier(String orderId, String tableId, int type);

    void onOrderSpinnerChange(int type, int employeeId, int shift, String payModeId, String payModeName, int date, String orderType);

    void onTurnoverRightClick(OrderEntity orderEntity, int employeeId, int shift, String paymodeId, int date, String type);

    void turnoverCollection(int cashier, int shift, String payModeId, int date, String type);

    void turnoverCashier(String cashierId, int shift, String payModeId, int date, String type);

    void turnoverTypeCollection(int cashier, int shift, String payModeId, int date, String type);

    void turnoverNothing();

    void onTurnoverSpinnerChange(int type, int employeeId, int shift, String payModeId, int date, String orderType);

    void onScheduleConfirm();

    void onScheduleCancle();

    void onReturnOrderClick();

    void onClose(int type, String orderDishId);

    void onCancle(String dishId);

    void onDelete(String orderDishId);

    void onConfirm(String dishId, String specifyId, String practiceId, String orderDishId, String note,double count);

    void onDishListItemClick(OrderDishEntity orderDishEntity);

    void configDish(String dishId);

    void selectTaocan(String taocanId, String orderId);

    void addDish(OrderDishEntity orderDishEntity);

    void reduceDish(OrderDishEntity orderDishEntity);

    void dishDetail(String orderDishId);

    void onOrderDishConfirm();

    void printOrder(String orderId, int type);

    void printCaiwulian(String orderId);

    void printWaimai(String orderId);

    void printKitchen(ArrayList<PrintDishBean> printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, boolean isAddDish);

    void printXFDL(ArrayList<PrintDishBean> printDishBeenes, String orderId, String ip);

    void printHCL(ArrayList<PrintDishBean> printDishBeenes, String orderId, String ip);

    void onOrderDishScroll(int dy);

    void onRemindBackClick(String orderId);

    void onRemindAllClick(String orderId,ArrayList<OrderDishEntity> orderDishEntities);

    void onRemindClick(String orderId,ArrayList<OrderDishEntity> orderDishEntities);

    void onRemindPrint(ArrayList<PrintDishBean> printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, String employeeName);

    void onSelectgrouponTaocanEntity(String orderId,GrouponTaocanEntity grouponTaocanEntity, PayModeEntity payModeEntity,GrouponEntity grouponEntity);

    void onSelectGroupon(GrouponEntity grouponEntity, GrouponTaocanEntity grouponTaocanEntity, PayModeEntity payModeEntity);

    void onGrouponCancle();

    void onGrouponConfirm(String orderId);

    void onSelectBillPerson(String orderId,BillAccountHistoryEntity billAccountHistoryEntity);

    void selectedScheme(DiscountHistoryEntity discountHistoryEntity);

    void onSelectSomeDiscountGoods(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities);

    void onSelectReason(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities);

    void onDiscountAllCancle();

    void onDiscountAllConfirm(String value, String orderId, boolean isOpenJoinOrder);

    void onSelectAllReason(DiscountHistoryEntity discountHistoryEntity);

    void onDiscountSomeCancle();

    void onDiscountSomeConfirm(String orderId, boolean isOpenJoinOrder);

    void onSelectSomeReason(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities);

    void onSelectSomeGoods(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities);

    void onDiscountSchemeCancle();

    void onDiscountSchemeConfirm(String orderId, boolean isOpenJoinOrder);

    void onSelectSchemeReason(DiscountHistoryEntity discountHistoryEntity);

    void onSelectScheme(DiscountHistoryEntity discountHistoryEntity);

    void onPresentBackClick(String orderId);

    void onPresentAllClick(ArrayList<OrderDishEntity> orderDishEntities, String orderId);

    void onPresentClick(ArrayList<OrderDishEntity> orderDishEntities, String orderId);

    void onPresentOrderCancle(String orderId);

    void onPresentOrderConfirm(String orderId);

    void onRetreatOrderCancle(String orderId);

    void onRetreatOrderConfirm(String orderId);

    void onRetreatPrint(ArrayList<PrintDishBean> printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, String employeeName, String reason);

    void onRetreatBackClick(String orderId);

    void onRetreatAllClick(ArrayList<OrderDishEntity> orderDishEntities, String orderId);

    void onRetreatClick(ArrayList<OrderDishEntity> orderDishEntities, String orderId);

    void onElectricCancle();

    void onElectricConfirm();

    void onBankCancle();

    void onBankConfirm();

    void onAccountUnitClick(BillAccountHistoryEntity billAccountHistoryEntity);

    void onAccountPeopleClick(BillAccountHistoryEntity billAccountHistoryEntity);

    void onSignPeopleClick(BillAccountHistoryEntity billAccountHistoryEntity);

    void onAccountCancle();

    void onAccountConfirm(String orderId, BillAccountHistoryEntity billAccountHistoryEntity);

    void onCashCancle();

    void onCashConfirm(String value);

    void onContinuCashier(String orderId, boolean isOpenJoinOrder);

    void onMLClick(String orderId, double money);

    void onKeyClick(int keyCode, String orderId, boolean isOpenJoinOrder, String tableId);

    void cashierDishDetail(String orderDishId,String orderId,String tableId);

    void retreatDish(String orderId);

    void presentDish(String orderId);

    void remindDish(String orderId);

    void changeChildOrder(String orderId);

    void changeJoinOrder();

    void printKHL(String orderId);

    void onCashierOrderScroll(int dy);

    void onLogOut();

    void onOver();

    void checkCancle();

    void onPrintAgain(String ip, PrintBean callable);

    void onClearClick(int type);

    void onStoreMessageChange();

    void refreshStock();

    void openCashBox();

    void selectTableCancel();

    void selectedTables(int operateType, int type, TableEntity tableEntity);

    void scheduleSelectTable();

    void scheduleConfirmTable(ScheduleEntity scheduleEntity);

    void changeOrderMessageCount();

    void refreshTable();

    void printOrderCollection(String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, Map<String, String> orderMoneyDetail, ArrayList<PayModeEntity> payModeEntities);

    void printTypeCollection(String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, ArrayList<DishTypeCollectionItemBean> dishTypeCollectionItemBeen);

    void printCashierCollection(String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, Map<String, String> orderMoneyDetail, ArrayList<PayModeEntity> payModeEntities);

    void printStatistic(int dateType, DishTypeModel dishTypeModel, ArrayList<DishModel> dishModels);

    void onTaocanDishDetail(OrderDishEntity orderDishEntity,String activityTag,String taocanId,String orderId,String tableId);

    void onTaocanCancle(String tableId,String orderId,String activityTag);
    void onTaocanDelete(String tableId,String orderId,OrderDishEntity orderDishEntity,String activityTag);
    void onTaocanChangeConfirm(String tableId,String orderId,OrderDishEntity orderDishEntity,String activityTag);
    void onTaocanAddConfirm(String tableId,String orderId,OrderDishEntity orderDishEntity,String activityTag);

    void printTurnOverOrder(String turnOverTime,String cashierName, int dateType, String areaTypeName,ArrayList<PayModeEntity> payModeEntities,ArrayList<DishTypeCollectionItemBean> dishTypeCollectionItemBeens,Map<String,String> orderMoneyDetail);
    void onArraySortCall(String str);
    void printTurnOverOrder(TurnoverHistoryEntity turnoverHistoryEntity);
}
