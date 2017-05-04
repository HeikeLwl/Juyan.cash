package jufeng.juyancash.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.UUID;

import cn.jpush.android.api.JPushInterface;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.bean.JpushMessageExtras;
import jufeng.juyancash.bean.MeituanDispacherBean;
import jufeng.juyancash.bean.MeituanTakeoutBean;
import jufeng.juyancash.bean.PayQRBean;
import jufeng.juyancash.bean.TakeOutBean;
import jufeng.juyancash.bean.TurnoverBean;
import jufeng.juyancash.bean.WXPayBean;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.StoreMessageEntity;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.dao.WXMessageEntity;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.fragment.LFragmentAutorityQR;
import jufeng.juyancash.ui.fragment.LFragmentTurnOverQR;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/7/7 0007.
 */
public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的自定义消息");
            Bundle bundle1 = intent.getExtras();
            receivingCustomMessage(context, bundle1);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingCustomMessage(final Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
        String content = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
        Log.d(TAG, "content : " + content);
        try {
            JpushMessageExtras jpushMessageExtras = JSON.parseObject(extras, JpushMessageExtras.class);
            switch (jpushMessageExtras.getType()) {
                case 0://外卖单
                    DBHelper.getInstance(context).insertJpushMessage(message, 0);
                    break;
                case 1://预定
                    DBHelper.getInstance(context).insertJpushMessage(message, 1);
                    break;
                case 2://通知公告
                    StoreMessageEntity storeMessageEntity = new StoreMessageEntity();
                    storeMessageEntity.setIsRead(0);
                    storeMessageEntity.setStoreTitle(title);
                    storeMessageEntity.setStoreContent(message);
                    storeMessageEntity.setStoreMessageId(UUID.randomUUID().toString());
                    storeMessageEntity.setStoreTime(CustomMethod.parseTime(jpushMessageExtras.getTime(), "yyyy-MM-dd HH:mm"));
                    storeMessageEntity.setStoreImgUrl(jpushMessageExtras.getPicurl());
                    DBHelper.getInstance(context).insertStoreMessage(storeMessageEntity);
                    sendMainMessage(context, 2, storeMessageEntity);
                    break;
                case 3://外卖确认收货
                    TakeOutOrderEntity mTakeOutOrderEntity = DBHelper.getInstance(context).getTakeOutOrderById(message);
                    if (mTakeOutOrderEntity != null) {
                        DBHelper.getInstance(context).changeTakeOutOrderStatus(mTakeOutOrderEntity,3);
                        DBHelper.getInstance(context).insertUploadData(mTakeOutOrderEntity.getOrderId(), mTakeOutOrderEntity.getOrderId(), 7);
                        sendMainMessage(context, 3, mTakeOutOrderEntity);
                    }
                    break;
                case 4://修改订单状态
                    TakeOutBean takeOutBean = JSON.parseObject(extras, TakeOutBean.class);
                    TakeOutOrderEntity mTakeOutOrderEntity1 = DBHelper.getInstance(context).getTakeOutOrderById(message);
                    if (mTakeOutOrderEntity1 != null && mTakeOutOrderEntity1.getTakeoutStatus() != takeOutBean.getStatus()) {
                        DBHelper.getInstance(context).changeTakeOutOrderStatus(mTakeOutOrderEntity1,takeOutBean.getStatus());
                        sendMainMessage(context, 3, mTakeOutOrderEntity1);
                    }
                    break;
                case 5://到店点餐
                    DBHelper.getInstance(context).insertJpushMessage(message, 3);
                    break;
                case 6://排号
                    DBHelper.getInstance(context).insertJpushMessage(message, 2);
                    break;
                case 7://放弃排号
                    DBHelper.getInstance(context).deleteArrange(message);
                    sendMainMessage(context, 19);
                    break;
                case 8://扫码结果
                    TurnoverBean turnoverBean = new TurnoverBean(message);
                    if (turnoverBean != null) {
                        if (turnoverBean.getType().equals("0")) {//确认交接班
                            sendTurnOverMessage(context, turnoverBean);
                        } else {//权限验证
                            sendAuthorityMessage(context, turnoverBean);
                        }
                    }
                    break;
                case 9://微信点餐支付
                    try {
                        WXPayBean wxPayBean = JSON.parseObject(message, WXPayBean.class);
                        if (wxPayBean != null) {
                            DBHelper.getInstance(context).insertWxPay(wxPayBean);
                            sendMainMessage(context, wxPayBean.getOid(), 21);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 10://催菜服务
                    DBHelper.getInstance(context).insertJpushMessage(message, 4);
                    break;
                case 11://呼叫服务
                    String[] datas = message.split("`");
                    if (datas != null && datas.length > 1) {
                        OrderEntity orderEntity = DBHelper.getInstance(context).getOneOrderEntity(datas[0]);
                        if (orderEntity != null) {
                            String tableName = DBHelper.getInstance(context).getTableNameByTableId(orderEntity.getTableId());
                            String tableCode = DBHelper.getInstance(context).getTableCodeByTableId(orderEntity.getTableId());
                            WXMessageEntity wxMessageEntity = new WXMessageEntity();
                            wxMessageEntity.setWxMessageId(UUID.randomUUID().toString());
                            wxMessageEntity.setIsRead(0);
                            if(orderEntity.getStoreVersion() == 0){
                                wxMessageEntity.setWxContent("桌位：" + tableName + "(" + tableCode + ")" + " \n单号：NO." + orderEntity.getOrderNumber1() + " \n呼叫服务：" + datas[1]);
                            }else if(orderEntity.getStoreVersion() == 1){
                                wxMessageEntity.setWxContent("单号：NO." + orderEntity.getOrderNumber1() + " \n呼叫服务：" + datas[1]);
                            }
                            wxMessageEntity.setWxTitle("微信呼叫服务");
                            wxMessageEntity.setWxType(1);
                            wxMessageEntity.setWxTime(CustomMethod.parseTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm"));
                            DBHelper.getInstance(context).insertWXMessage(wxMessageEntity);
                            sendMainMessage(context, 16, wxMessageEntity);
                        }
                    }
                    break;
                case 12://微信扫描打印单二维码支付推送消息
                    try {
                        PayQRBean payQRBean = JSON.parseObject(message, PayQRBean.class);
                        DBHelper.getInstance(context.getApplicationContext()).insertQRPay(payQRBean);
                        sendMainMessage(context, payQRBean.getId(), 21);
                    } catch (Exception e) {

                    }
                    break;
                case 20://美团外卖订单推送消息
                    DBHelper.getInstance(context).insertJpushMessage(message, 5);
                    break;
                case 21://美团外卖取消订单
                    try{
                        MeituanTakeoutBean meituanTakeoutOver = JSON.parseObject(message,MeituanTakeoutBean.class);
                        if(meituanTakeoutOver.getOrderId() != null){
                            TakeOutOrderEntity mTakeOutOrderEntity3 = DBHelper.getInstance(context).getTakeoutOrderByOtherId(meituanTakeoutOver.getOrderId());
                            if (mTakeOutOrderEntity3 != null) {
                                DBHelper.getInstance(context).changeTakeOutOrderStatus(mTakeOutOrderEntity3, 5);
                                sendMainMessage(context,3,mTakeOutOrderEntity3);
                            }
                        }
                    }catch (Exception e){

                    }

                    break;
                case 22://美团顾客发起退款

                    break;
                case 23://美团顾客确认订单

                    break;
                case 24://美团外卖顾客端点击完成
                    try {
                        MeituanTakeoutBean meituanTakeoutOver = JSON.parseObject(message, MeituanTakeoutBean.class);
                        if(meituanTakeoutOver.getOrderId() != null){
                            TakeOutOrderEntity mTakeOutOrderEntity2 = DBHelper.getInstance(context).getTakeoutOrderByOtherId(meituanTakeoutOver.getOrderId());
                            if (mTakeOutOrderEntity2 != null) {
                                DBHelper.getInstance(context).changeTakeOutOrderStatus(mTakeOutOrderEntity2,3);
                                DBHelper.getInstance(context).insertUploadData(mTakeOutOrderEntity2.getOrderId(), mTakeOutOrderEntity2.getOrderId(), 7);
                                sendMainMessage(context, 3, mTakeOutOrderEntity2);
                            }
                        }
                    }catch (Exception e){

                    }
                    break;
                case 25://美团配送信息
                    MeituanDispacherBean meituanDispacherBean = JSON.parseObject(message, MeituanDispacherBean.class);
                    switch (meituanDispacherBean.getShippingStatus()){
                        case 0://待分配配送员

                            break;
                        case 10://已分配配送员
                            DBHelper.getInstance(context).addEmployeeForTakeout(meituanDispacherBean);
                            break;
                        case 20://配送员取餐

                            break;
                        case 40://配送员已送达

                            break;
                        case 100://配送单已取消

                            break;
                    }
                    break;
                case 501://快餐版-到店点餐
                    DBHelper.getInstance(context).insertJpushMessage(message, 6);
                    break;
            }
        } catch (Exception e) {

        }
    }

    private void sendAuthorityMessage(Context context, TurnoverBean turnoverBean) {
        Intent mIntent = new Intent(LFragmentAutorityQR.ACTION_INTENT_AUTHORITY);
        mIntent.putExtra("turnoverBean", turnoverBean);
        context.sendBroadcast(mIntent);
    }

    private void sendTurnOverMessage(Context context, TurnoverBean turnoverBean) {
        Intent mIntent = new Intent(LFragmentTurnOverQR.ACTION_INTENT_AUTHORITY);
        mIntent.putExtra("turnoverBean", turnoverBean);
        context.sendBroadcast(mIntent);
    }

    private void sendMainMessage(Context context, int type) {
        Intent mIntent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        mIntent.putExtra("type", type);
        context.sendBroadcast(mIntent);
    }

    private void sendMainMessage(Context context, String orderId, int type) {
        Intent mIntent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        mIntent.putExtra("type", type);
        mIntent.putExtra("orderId", orderId);
        context.sendBroadcast(mIntent);
    }

    private void sendMainMessage(Context context, int type, StoreMessageEntity storeMessageEntity) {
        Intent mIntent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        mIntent.putExtra("type", type);
        mIntent.putExtra("storeMessage", storeMessageEntity);
        context.sendBroadcast(mIntent);
    }

    private void sendMainMessage(Context context, int type, WXMessageEntity wxMessageEntity) {
        Intent mIntent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        mIntent.putExtra("type", type);
        mIntent.putExtra("wxMessage", wxMessageEntity);
        context.sendBroadcast(mIntent);
    }

    private void sendMainMessage(Context context, int type, TakeOutOrderEntity mTakeOutOrderEntity) {
        Intent mIntent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        mIntent.putExtra("type", type);
        mIntent.putExtra("takeOutOrderEntity", mTakeOutOrderEntity);
        context.sendBroadcast(mIntent);
    }

    private void sendMainMessage(Context context, int type, TurnoverBean turnoverBean) {
        Intent mIntent = new Intent(MainActivity.ACTION_INTENT_MAIN);
        mIntent.putExtra("type", type);
        mIntent.putExtra("turnoverBean", turnoverBean);
        context.sendBroadcast(mIntent);
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("myKey");
        } catch (Exception e) {
            return;
        }
        if ("".equals(myValue)) {
            Intent mIntent = new Intent(context, MainActivity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        } else if ("".equals(myValue)) {
            Intent mIntent = new Intent(context, MainActivity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }
    }
}
