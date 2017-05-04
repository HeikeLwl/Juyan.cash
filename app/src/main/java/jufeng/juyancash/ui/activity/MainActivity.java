package jufeng.juyancash.ui.activity;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.bean.DishModel;
import jufeng.juyancash.bean.DishTypeCollectionItemBean;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.bean.PrintBean;
import jufeng.juyancash.bean.PrintDishBean;
import jufeng.juyancash.dao.BillAccountEntity;
import jufeng.juyancash.dao.BillAccountHistoryEntity;
import jufeng.juyancash.dao.DiscountHistoryEntity;
import jufeng.juyancash.dao.DishEntity;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.GrouponEntity;
import jufeng.juyancash.dao.GrouponTaocanEntity;
import jufeng.juyancash.dao.OrderDishEntity;
import jufeng.juyancash.dao.OrderEntity;
import jufeng.juyancash.dao.OrderTaocanGroupDishEntity;
import jufeng.juyancash.dao.PayModeEntity;
import jufeng.juyancash.dao.Payment;
import jufeng.juyancash.dao.PrintCashierEntity;
import jufeng.juyancash.dao.PrintKitchenEntity;
import jufeng.juyancash.dao.ScheduleEntity;
import jufeng.juyancash.dao.ShopPaymentEntity;
import jufeng.juyancash.dao.SomeDiscountGoodsEntity;
import jufeng.juyancash.dao.StoreMessageEntity;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.dao.TakeOutOrderEntity;
import jufeng.juyancash.dao.TurnoverHistoryEntity;
import jufeng.juyancash.dao.WXMessageEntity;
import jufeng.juyancash.eventbus.OnTaocanDetailClickEvent;
import jufeng.juyancash.eventbus.PrintCaiwulianEvent;
import jufeng.juyancash.eventbus.PrintHCLEvent;
import jufeng.juyancash.eventbus.PrintKitchenEvent;
import jufeng.juyancash.eventbus.PrintLabelEvent;
import jufeng.juyancash.eventbus.PrintXFDLEvent;
import jufeng.juyancash.eventbus.SnackCashierTopLeftRefreshEvent;
import jufeng.juyancash.eventbus.SnackCashierTopRightRefreshEvent;
import jufeng.juyancash.eventbus.SnackOpenCashBoxEvent;
import jufeng.juyancash.eventbus.SnackOrderDetailChangeEvent;
import jufeng.juyancash.eventbus.SnackOrderListRefreshEvent;
import jufeng.juyancash.eventbus.SnackPrintKHLEvent;
import jufeng.juyancash.eventbus.SnackRemindPrintEvent;
import jufeng.juyancash.eventbus.SnackRetreatPrintEvent;
import jufeng.juyancash.eventbus.SnackReturnOrderEvent;
import jufeng.juyancash.eventbus.SnackUnreadMessageRefreshEvent;
import jufeng.juyancash.eventbus.TableCodeCallEvent;
import jufeng.juyancash.eventbus.TurnoverHistoryPrintAgainEvent;
import jufeng.juyancash.materialdaterangepicker.date.DatePickerDialog;
import jufeng.juyancash.materialdaterangepicker.time.RadialPickerLayout;
import jufeng.juyancash.materialdaterangepicker.time.TimePickerDialog;
import jufeng.juyancash.myinterface.CashierTopRightChangeListener;
import jufeng.juyancash.myinterface.InitKeyboardInterface;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.printer.KitchenPrint;
import jufeng.juyancash.printer.PrintHuacaiLianRunnable;
import jufeng.juyancash.printer.PrintXiaofeiDilianRunnable;
import jufeng.juyancash.printer.QiantaiPrintDishRunnable;
import jufeng.juyancash.printer.QiantaiRemindPrintRunnable;
import jufeng.juyancash.printer.QiantaiRetreatPrintRunnable;
import jufeng.juyancash.printer.RemindPrintRunnable;
import jufeng.juyancash.printer.RetreatPrintRunnable;
import jufeng.juyancash.service.CoreService;
import jufeng.juyancash.service.HeartBeatService;
import jufeng.juyancash.service.ServerStatusReceiver;
import jufeng.juyancash.ui.customview.CashierKeyboardUtil;
import jufeng.juyancash.ui.customview.CustomeAuthorityDialog;
import jufeng.juyancash.ui.customview.CustomeClearAllDialog;
import jufeng.juyancash.ui.customview.CustomeRemindDialog;
import jufeng.juyancash.ui.customview.CustomeScheduleHistoryDialog;
import jufeng.juyancash.ui.customview.CustomeSelectPaymentDialog;
import jufeng.juyancash.ui.fragment.LFragmentCashier;
import jufeng.juyancash.ui.fragment.LFragmentMain;
import jufeng.juyancash.ui.fragment.LFragmentOrderDish;
import jufeng.juyancash.ui.fragment.LFragmentSelectTables;
import jufeng.juyancash.ui.fragment.LFragmentSelectTaocan;
import jufeng.juyancash.util.ActivityIntentUtil;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/14 0014.
 */
public class MainActivity extends BaseActivity implements TextToSpeech.OnInitListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, InitKeyboardInterface, CashierTopRightChangeListener, MainFragmentListener {
    public static String ACTION_INTENT_MAIN = "jufeng.juyancash.intent.main";
    private MainBroadCast mainBroadCast;
    private Handler mainMenuHandler, scheduleHandler, takeoutOrderHandler,
            takeoutHistoryHandler, orderLeftDetailHandler, orderLeftCollectionHandler,
            orderLeftStatisticHandler, systemMsgHandler, storeMsgHandler,
            wxMsgHandler, kitchenPrintHandler, orderHandler, turnoverHandler, turnoverLeftDetailHandler,
            turnoverLeftCollectionHandler, arrangeSortHandler, scheduleCheckHandler,
            forHereHandler, schedulePhoneTimeHandler, mOrderDishHandler, mainHandler,
            schedulePerfectHandler, scheduleCancleHandler, scheduleEffectHandler, arrangeHistoryHandler, allMessageHandler;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    JPushInterface.setAlias(getApplicationContext(),
                            (String) msg.obj,
                            mAliasCallback);
                    break;
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    SharedPreferences spf = getSharedPreferences("loginData", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putInt("isSetAlias", 1);
                    editor.putString("alias", alias);
                    editor.commit();
                    break;
                case 6002:
                    // 延迟 60 秒来调用 Handler 设置别名
                    handler.sendMessageDelayed(handler.obtainMessage(5, alias), 1000 * 60);
                    break;
                default:
            }
        }
    };
    private HeartBeatService mPrintService;
    private LFragmentMain mLFragmentMain;
    private Intent mService;
    /**
     * Accept and server status.
     */
    private ServerStatusReceiver mReceiver;
    private final static String ACTION = "android.hardware.usb.action.USB_STATE";

    private BroadcastReceiver usBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(ACTION)) {
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                if (mPrintService != null) {
                    mPrintService.openUsbPrinter();
                }
                Toast.makeText(MainActivity.this, "USB_DEVICE_ATTACHED", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainFragment(false);
        registerMainReceiver();
        registerUsbReceiver();
        startPrintService();
        sendDifferBroadcast(3, null, false);

        mService = new Intent(this, CoreService.class);
        mReceiver = new ServerStatusReceiver(this);

        mReceiver.register();
        startService(mService);



        //设置别名
        SharedPreferences spf = getSharedPreferences("loginData", Activity.MODE_PRIVATE);
        String partnerCode = spf.getString("partnerCode", null);
        Message msg = new Message();
        msg.obj = partnerCode + partnerCode + partnerCode;
        msg.what = 5;
        handler.sendMessage(msg);

        tts = new TextToSpeech(MainActivity.this, MainActivity.this);
    }

    private void registerUsbReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(usBroadcastReceiver, filter);
    }

    private void setMainFragment(boolean changed) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans, changed);
        if (mLFragmentMain == null) {
            mLFragmentMain = new LFragmentMain();
            trans.add(R.id.container, mLFragmentMain);
        } else {
            trans.show(mLFragmentMain);
        }
        trans.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction ft, boolean changed) {
        if (mLFragmentMain != null) {
            ft.hide(mLFragmentMain);
            if (changed) {
                mLFragmentMain = null;
            }
        }
        if (mLFragmentOrderDish != null) {
            ft.hide(mLFragmentOrderDish);
            if (changed) {
                mLFragmentOrderDish = null;
            }
        }
        if (mLFragmentCashier != null) {
            ft.hide(mLFragmentCashier);
            if (changed) {
                mLFragmentCashier = null;
            }
        }
        if (mLFragmentSelectTables != null) {
            ft.hide(mLFragmentSelectTables);
        }
        if (mLFragmentSelectTaocan != null) {
            ft.hide(mLFragmentSelectTaocan);
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPrintService = ((HeartBeatService.HearBeatBinder) service).getService();
            mPrintService.startPrintThread(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

//    private ServiceConnection conn1 = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

    private void startPrintService() {
//        Intent PrintServiceIntent = new Intent(getApplicationContext(), PrintService.class);
//        bindService(PrintServiceIntent, conn, Service.BIND_AUTO_CREATE);
        Intent HeartServiceIntent = new Intent(getApplicationContext(), HeartBeatService.class);
        bindService(HeartServiceIntent, conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onRestart() {
//        setMainFragment(true);
//        if (mainMenuHandler != null) {
//            mainMenuHandler.sendEmptyMessage(1);
//            mainMenuHandler.sendEmptyMessage(2);
//            mainMenuHandler.sendEmptyMessage(4);
//            mainMenuHandler.sendEmptyMessage(5);
//            mainMenuHandler.sendEmptyMessage(6);
//        }
//        sendDifferBroadcast(0, null, false);
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
        System.gc();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.shutdown();
        }
        unregisterReceiver(mainBroadCast);
        unregisterReceiver(usBroadcastReceiver);
        if (conn != null) {
            unbindService(conn);
        }
        mReceiver.unRegister();
        stopService(mService);
        setContentView(R.layout.view_null);
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // 设置朗读的语言
            int result = tts.setLanguage(Locale.US);
            if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                    && result != TextToSpeech.LANG_AVAILABLE) {
                CustomMethod.showMessage(this, "TTS暂时不支持这种语言的朗读"); // 调用自定义toast提示
            }
        } else {
            CustomMethod.showMessage(this, "装载tts引擎不成功！"); // 调用自定义toast提示
        }
    }

    public void setMainHandler(Handler handler) {
        this.mainHandler = handler;
    }

    //设置mainmenu的handler
    public void setMainMenuHandler(Handler handler) {
        this.mainMenuHandler = handler;
    }

    //设置schedule的handler
    public void setScheduleHandler(Handler handler) {
        this.scheduleHandler = handler;
    }

    //设置外卖的handler
    public void setTakeoutOrderHandler(Handler handler) {
        this.takeoutOrderHandler = handler;
    }

    //设置外卖记录的handler
    public void setTakeOutHistoryHandler(Handler handler) {
        this.takeoutHistoryHandler = handler;
    }

    //账单明细左侧详情的handler
    public void setOrderLeftDetailHandler(Handler handler) {
        this.orderLeftDetailHandler = handler;
    }

    //账单汇总左侧详情的handler
    public void setOrderLeftCollectionHandler(Handler handler) {
        this.orderLeftCollectionHandler = handler;
    }

    //销售统计左侧详情的handler
    public void setOrderLeftStatisticHandler(Handler handler) {
        this.orderLeftStatisticHandler = handler;
    }

    //设置消息中心的handler
    public void setAllMessageHandler(Handler handler) {
        this.allMessageHandler = handler;
    }

    //设置系统消息的handler
    public void setSystemMsgHandler(Handler handler) {
        this.systemMsgHandler = handler;
    }

    //设置商家消息的handler
    public void setStoreMsgHandler(Handler handler) {
        this.storeMsgHandler = handler;
    }

    //设置微信消息的handler
    public void setWxMsgHandler(Handler handler) {
        this.wxMsgHandler = handler;
    }

    //设置厨打的handler
    public void setKitchenPrintHandler(Handler handler) {
        this.kitchenPrintHandler = handler;
    }

    public void setFragmentOrderHandler(Handler handler) {
        this.orderHandler = handler;
    }

    public void setTurnoverLeftCollectionHandler(Handler turnoverLeftCollectionHandler) {
        this.turnoverLeftCollectionHandler = turnoverLeftCollectionHandler;
    }

    public void setTurnoverLeftDetailHandler(Handler turnoverLeftDetailHandler) {
        this.turnoverLeftDetailHandler = turnoverLeftDetailHandler;
    }

    public void setTurnoverHandler(Handler handler) {
        this.turnoverHandler = handler;
    }

    public void setArrangeSortHandler(Handler handler) {
        this.arrangeSortHandler = handler;
    }

    public void setScheduleCheckHandler(Handler handler) {
        this.scheduleCheckHandler = handler;
    }

    public void setForHereHandler(Handler handler) {
        this.forHereHandler = handler;
    }

    public void setSchedulePhoneTimeHandler(Handler handler) {
        this.schedulePhoneTimeHandler = handler;
    }

    public void setSchedulePerfectHandler(Handler handler) {
        this.schedulePerfectHandler = handler;
    }

    public void setScheduleCancleHandler(Handler handler) {
        this.scheduleCancleHandler = handler;
    }

    public void setScheduleEffectHandler(Handler handler) {
        this.scheduleEffectHandler = handler;
    }

    public void setArrangeHistoryHandler(Handler handler) {
        this.arrangeHistoryHandler = handler;
    }

    @Override
    public void selectMenu(int checked, Bundle bundle) {
        if (mainHandler != null) {
            Message msg = new Message();
            msg.obj = checked;
            msg.what = 0;
            msg.setData(bundle);
            mainHandler.sendMessage(msg);
        }
    }

    //*************************************FragmentForHere的监听事件
    @Override
    public void openSchedule(TableEntity tableEntity) {//预定
        if (mainMenuHandler != null) {
            Message msg = new Message();
            msg.what = 0;
            Bundle b = new Bundle();
            b.putInt("checkId", 1);
            b.putInt("type", 0);
            b.putParcelable("tableEntity", tableEntity);
            msg.setData(b);
            mainMenuHandler.sendMessage(msg);
        }
    }

    @Override
    public void openScheduleHistory(TableEntity tableEntity) {//查看预定记录
        final CustomeScheduleHistoryDialog dialog = new CustomeScheduleHistoryDialog(this, tableEntity.getTableId());
        dialog.setOnScheduleHistoryListener(new CustomeScheduleHistoryDialog.OnScheduleHistoryListener() {
            @Override
            public void onScheduleHistoryCancle() {
                dialog.dismiss();
            }

            @Override
            public void onScheduleCancle(ScheduleEntity scheduleEntity) {
                DBHelper.getInstance(getApplicationContext()).cancleScheduleTable(scheduleEntity.getTableId(), scheduleEntity.getOrderId());
                if (scheduleEntity.getScheduleFrom() == 1) {
                    DBHelper.getInstance(getApplicationContext()).insertUploadData(scheduleEntity.getScheduleId(), DBHelper.getInstance(getApplicationContext()).getTableNameByTableId(scheduleEntity.getTableId()), 12);
                } else {
                    String data = "{\"mobile\":\"" + scheduleEntity.getGuestPhone() + "\",\"eatTime\":\"" + CustomMethod.parseTime(scheduleEntity.getMealTime(), "yyyy年MM月dd日 HH:mm") + "\",\"op\":\"" + 1 + "\"}";
                    DBHelper.getInstance(getApplicationContext()).insertUploadData(scheduleEntity.getOrderId(), data, 18);
                }
                if (forHereHandler != null) {
                    forHereHandler.sendEmptyMessage(0);
                }
                if (scheduleEffectHandler != null) {
                    scheduleEffectHandler.sendEmptyMessage(0);
                }
                if (scheduleCancleHandler != null) {
                    scheduleCancleHandler.sendEmptyMessage(0);
                }
                dialog.dismiss();
            }

            @Override
            public void onScheduleHistoryOpen(ScheduleEntity scheduleEntity) {
                dialog.dismiss();
                if (scheduleEntity != null) {
                    DBHelper.getInstance(getApplicationContext()).openScheduleTable(scheduleEntity.getTableId(), scheduleEntity.getOrderId());
                    if (scheduleEntity.getScheduleFrom() == 1) {
                        DBHelper.getInstance(getApplicationContext()).insertUploadData(scheduleEntity.getScheduleId(), DBHelper.getInstance(getApplicationContext()).getTableNameByTableId(scheduleEntity.getTableId()), 11);
                    }
                    openTable(scheduleEntity.getTableId(), scheduleEntity.getOrderId());
                } else {
                    Toast.makeText(getApplicationContext(), "预订单为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onScheduleConfirm() {
        if (forHereHandler != null) {
            forHereHandler.sendEmptyMessage(0);
        }
        if (schedulePerfectHandler != null) {
            schedulePerfectHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onScheduleCancle() {
        if (forHereHandler != null) {
            forHereHandler.sendEmptyMessage(0);
        }
        if (scheduleCancleHandler != null) {
            scheduleCancleHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void checkCancle() {
        if (forHereHandler != null) {
            forHereHandler.sendEmptyMessage(0);
        }
        if (mainMenuHandler != null) {
            mainMenuHandler.sendEmptyMessage(5);
        }
        if (scheduleCancleHandler != null) {
            scheduleCancleHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void lockTable(TableEntity tableEntity) {
    }

    @Override
    public void unlockTable(TableEntity tableEntity) {
    }

    @Override
    public void syncDataLater(String orderId) {
    }

    @Override
    public void onScheduleSuccess() {
        if (scheduleHandler != null)
            scheduleHandler.sendEmptyMessage(0);
        if (forHereHandler != null) {
            forHereHandler.sendEmptyMessage(0);
        }
        if (scheduleEffectHandler != null) {
            scheduleEffectHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onSelectTime(Calendar calendar) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MainActivity.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(calendar);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        if (schedulePhoneTimeHandler != null) {
            Message msg = new Message();
            msg.what = 0;
            Bundle bundle = new Bundle();
            bundle.putString("date", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            msg.setData(bundle);
            schedulePhoneTimeHandler.sendMessage(msg);
        }
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        String time = "";
        if (minute < 10) {
            time = hourOfDay + ":0" + minute;
        } else {
            time = hourOfDay + ":" + minute;
        }
        if (schedulePhoneTimeHandler != null) {
            Message msg = new Message();
            msg.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("time", time);
            msg.setData(bundle);
            schedulePhoneTimeHandler.sendMessage(msg);
        }
    }

    //外卖记录的点击事件回调
    @Override
    public void onTakeOutOrderClick(TakeOutOrderEntity takeOutOrderEntity, int type) {
        if (takeoutOrderHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            bundle.putParcelable("takeOutOrderEntity", takeOutOrderEntity);
            msg.setData(bundle);
            msg.what = 0;
            takeoutOrderHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("###", "onActivityResult");
        if (requestCode == 1010 && resultCode == 1) {
            //预定点餐成功后
            if (scheduleHandler != null)
                scheduleHandler.sendEmptyMessage(0);
            if (scheduleCheckHandler != null)
                scheduleCheckHandler.sendEmptyMessage(0);
            if (mainMenuHandler != null)
                mainMenuHandler.sendEmptyMessage(5);
        } else if (requestCode == 1000 && resultCode == 1) {
            OrderDishEntity orderDishEntity = (OrderDishEntity) data.getParcelableExtra("orderDishEntity");
            if (mDishListHandler != null) {
                Message message = new Message();
                message.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("dishId", orderDishEntity.getDishId());
                bundle.putInt("type", 1);
                message.setData(bundle);
                mDishListHandler.sendMessage(message);
            }
            if (mOrderHandler != null) {
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putParcelable("orderDishEntity", orderDishEntity);
                message.setData(bundle);
                mOrderHandler.sendMessage(message);
            }
        } else if (requestCode == 10001 && resultCode == 1) {
            OrderDishEntity orderDishEntity = (OrderDishEntity) data.getParcelableExtra("orderDishEntity");
            if (mDishListHandler != null) {
                Message message = new Message();
                message.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("dishId", orderDishEntity.getDishId());
                bundle.putInt("type", 1);
                message.setData(bundle);
                mDishListHandler.sendMessage(message);
            }
            if (mOrderHandler != null) {
                Message message = new Message();
                message.what = 4;
                Bundle bundle = new Bundle();
                bundle.putParcelable("orderDishEntity", orderDishEntity);
                message.setData(bundle);
                mOrderHandler.sendMessage(message);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //外卖单按钮点击回调
    @Override
    public void onOrderClick(TakeOutOrderEntity takeOutOrderEntity) {
        if (takeoutHistoryHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("takeOutOrderEntity", takeOutOrderEntity);
            msg.what = 0;
            msg.setData(bundle);
            takeoutHistoryHandler.sendMessage(msg);
        }
        if (mainMenuHandler != null)
            mainMenuHandler.sendEmptyMessage(1);
    }

    @Override
    public void onCancleClick(TakeOutOrderEntity takeOutOrderEntity) {
        if (takeoutHistoryHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("takeOutOrderEntity", takeOutOrderEntity);
            msg.what = 0;
            msg.setData(bundle);
            takeoutHistoryHandler.sendMessage(msg);
        }
        if (mainMenuHandler != null)
            mainMenuHandler.sendEmptyMessage(1);
    }

    @Override
    public void onTransferClick(TakeOutOrderEntity takeOutOrderEntity) {
        if (takeoutHistoryHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("takeOutOrderEntity", takeOutOrderEntity);
            msg.what = 0;
            msg.setData(bundle);
            takeoutHistoryHandler.sendMessage(msg);
        }
        if (mainMenuHandler != null)
            mainMenuHandler.sendEmptyMessage(1);
    }

    @Override
    public void onOverClick(TakeOutOrderEntity takeOutOrderEntity) {
        if (takeoutHistoryHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("takeOutOrderEntity", takeOutOrderEntity);
            msg.what = 0;
            msg.setData(bundle);
            takeoutHistoryHandler.sendMessage(msg);
        }
        if (mainMenuHandler != null) {
            mainMenuHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onPassClick(TakeOutOrderEntity takeOutOrderEntity) {
        if (takeoutHistoryHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("takeOutOrderEntity", takeOutOrderEntity);
            msg.what = 0;
            msg.setData(bundle);
            takeoutHistoryHandler.sendMessage(msg);
        }
        if (mainMenuHandler != null)
            mainMenuHandler.sendEmptyMessage(1);
    }

    //账单回调接口
    @Override
    public void onOrderRightClick(OrderEntity orderEntity, int employeeId, int shift, String paymodeId, int date, String type) {
        if (orderLeftDetailHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderEntity", orderEntity);
            msg.what = 0;
            msg.setData(bundle);
            orderLeftDetailHandler.sendMessage(msg);
        }
    }

    //反结账
    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onReturnOrderClick() {
        if (forHereHandler != null)
            forHereHandler.sendEmptyMessage(0);
        if (orderHandler != null) {
            orderHandler.sendEmptyMessage(0);
        }
        EventBus.getDefault().post(new SnackReturnOrderEvent());
    }

    //账单汇总界面回调
    @Override
    public void orderCollection(int cashier, int shift, String payModeId, String payModeName, int date, String type) {
        if (orderLeftCollectionHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("employeeId", cashier);
            bundle.putInt("shift", shift);
            bundle.putString("payModeId", payModeId);
            bundle.putString("payModeName", payModeName);
            bundle.putInt("date", date);
            bundle.putString("type", type);
            msg.what = 0;
            msg.setData(bundle);
            orderLeftCollectionHandler.sendMessage(msg);
        }
    }

    @Override
    public void orderCashier(String cashierId, int shift, String payModeId, String payModeName, int date, String type) {
        if (orderLeftCollectionHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("employeeId", cashierId);
            bundle.putInt("shift", shift);
            bundle.putString("payModeId", payModeId);
            bundle.putString("payModeName", payModeName);
            bundle.putInt("date", date);
            bundle.putString("type", type);
            msg.what = 2;
            msg.setData(bundle);
            orderLeftCollectionHandler.sendMessage(msg);
        }
    }

    @Override
    public void typeCollection(int cashier, int shift, String payModeId, String payModeName, int date, String type) {
        if (orderLeftCollectionHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("employeeId", cashier);
            bundle.putInt("shift", shift);
            bundle.putString("payModeId", payModeId);
            bundle.putString("payModeName", payModeName);
            bundle.putInt("date", date);
            bundle.putString("type", type);
            msg.what = 1;
            msg.setData(bundle);
            orderLeftCollectionHandler.sendMessage(msg);
        }
    }

    @Override
    public void nothing() {
        if (orderLeftCollectionHandler != null)
            orderLeftCollectionHandler.sendEmptyMessage(-1);
    }

    //销售统计回调
    @Override
    public void onOrderRightStatisticClick(DishTypeModel dishTypeModel, int shift, int date) {
        if (orderLeftStatisticHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("dishTypeModel", dishTypeModel);
            bundle.putInt("employeeId", -1);
            bundle.putInt("shift", shift);
            bundle.putString("payModeId", null);
            bundle.putInt("date", date);
            bundle.putString("type", "-1");
            msg.what = 0;
            msg.setData(bundle);
            orderLeftStatisticHandler.sendMessage(msg);
        }
    }

    //账单统计,切换筛选条件
    @Override
    public void onOrderSpinnerChange(int type, int employeeId, int shift, String payModeId, String payModeName, int date, String orderType) {
        if (orderLeftDetailHandler != null) {
            Message msg0 = new Message();
            Bundle bundle0 = new Bundle();
            bundle0.putParcelable("orderEntity", null);
            msg0.what = 0;
            msg0.setData(bundle0);
            orderLeftDetailHandler.sendMessage(msg0);
        }

        if (orderLeftCollectionHandler != null)
            orderLeftCollectionHandler.sendEmptyMessage(-1);

        if (orderLeftStatisticHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("dishTypeModel", null);
            bundle.putInt("employeeId", employeeId);
            bundle.putInt("shift", shift);
            bundle.putString("payModeId", payModeId);
            bundle.putString("payModeName", payModeName);
            bundle.putInt("date", date);
            bundle.putString("type", orderType);
            msg.what = 0;
            msg.setData(bundle);
            orderLeftStatisticHandler.sendMessage(msg);
        }
    }

    //*****************************************************************交接班回调函数
    @Override
    public void onTurnoverRightClick(OrderEntity orderEntity, int employeeId, int shift, String paymodeId, int date, String type) {
        //账单明细
        if (turnoverLeftDetailHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderEntity", orderEntity);
            msg.what = 0;
            msg.setData(bundle);
            turnoverLeftDetailHandler.sendMessage(msg);
        }
    }

    @Override
    public void turnoverCollection(int cashier, int shift, String payModeId, int date, String type) {
        //账单汇总
        if (turnoverLeftCollectionHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("employeeId", cashier);
            bundle.putString("type", type);
            msg.what = 0;
            msg.setData(bundle);
            turnoverLeftCollectionHandler.sendMessage(msg);
        }
    }

    @Override
    public void turnoverCashier(String cashierId, int shift, String payModeId, int date, String type) {
        //收银员账单汇总
        if (turnoverLeftCollectionHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("employeeId", cashierId);
            bundle.putString("type", type);
            msg.what = 2;
            msg.setData(bundle);
            turnoverLeftCollectionHandler.sendMessage(msg);
        }
    }

    @Override
    public void turnoverTypeCollection(int cashier, int shift, String payModeId, int date, String type) {
        //分类汇总
        if (turnoverLeftCollectionHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("employeeId", cashier);
            bundle.putString("type", type);
            msg.what = 1;
            msg.setData(bundle);
            turnoverLeftCollectionHandler.sendMessage(msg);
        }
    }

    @Override
    public void turnoverNothing() {
        //什么都没有选择
        if (turnoverLeftCollectionHandler != null) {
            turnoverLeftCollectionHandler.sendEmptyMessage(-1);
        }
    }

    @Override
    public void onTurnoverSpinnerChange(int type, int employeeId, int shift, String payModeId, int date, String orderType) {
        if (turnoverLeftDetailHandler != null) {
            Message msg0 = new Message();
            Bundle bundle0 = new Bundle();
            bundle0.putParcelable("orderEntity", null);
            msg0.what = 0;
            msg0.setData(bundle0);
            turnoverLeftDetailHandler.sendMessage(msg0);
        }

        if (turnoverLeftCollectionHandler != null) {
            turnoverLeftCollectionHandler.sendEmptyMessage(-1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTableCodeCall(TableCodeCallEvent event) {
        if (event != null && event.getTableCode() != null) {
            CustomMethod.TtSpeech(tts, event.getTableCode() + "号，请您取餐！");
        }
    }

    //注册广播接收器
    private void registerMainReceiver() {
        mainBroadCast = new MainBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_MAIN);
        registerReceiver(mainBroadCast, filter);
    }

    //广播接收器与极光的接收器通信
    class MainBroadCast extends BroadcastReceiver {
        @Subscribe(threadMode = ThreadMode.MAIN)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_INTENT_MAIN)) {
                int type = intent.getIntExtra("type", 0);
                switch (type) {
                    case 0:
                        //外卖单
                        if (mainMenuHandler != null)
                            mainMenuHandler.sendEmptyMessage(1);
                        if (takeoutHistoryHandler != null) {
                            takeoutHistoryHandler.sendEmptyMessage(0);
                            takeoutHistoryHandler.sendEmptyMessage(1);
                        }
                        CustomMethod.TtSpeech(tts, "您有新的外卖单");
                        break;
                    case 1://预订单
                        if (mainMenuHandler != null)
                            mainMenuHandler.sendEmptyMessage(5);
                        if (scheduleCheckHandler != null)
                            scheduleCheckHandler.sendEmptyMessage(0);
                        CustomMethod.TtSpeech(tts, "您有新的预订单");
                        break;
                    case 2:
                        //店家消息
                        if (mainMenuHandler != null)
                            mainMenuHandler.sendEmptyMessage(2);
                        if (storeMsgHandler != null) {
                            StoreMessageEntity storeMessageEntity = intent.getParcelableExtra("storeMessage");
                            Message message = new Message();
                            message.what = 1;
                            message.obj = storeMessageEntity;
                            storeMsgHandler.sendMessage(message);
                        }
                        if (allMessageHandler != null) {
                            allMessageHandler.sendEmptyMessage(0);
                        }
                        break;
                    case 3://外卖单状态变化
                        TakeOutOrderEntity takeOutOrderEntity = intent.getParcelableExtra("takeOutOrderEntity");
                        if (takeoutHistoryHandler != null) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("takeOutOrderEntity", takeOutOrderEntity);
                            msg.setData(bundle);
                            msg.what = 0;
                            takeoutHistoryHandler.sendMessage(msg);
                            if (takeOutOrderEntity.getTakeoutStatus() == 1) {//由待审核变为已下单
                                onPrintData(takeOutOrderEntity.getOrderId());
                            }
                            if (takeOutOrderEntity.getTakeoutStatus() == 3) {
                                //确认收货
                                onOverClick(takeOutOrderEntity);
                                onChangeTakeoutStatusCount();
                                refreshStock();
                            }
                            if (takeOutOrderEntity.getTakeoutStatus() == 5) {
                                //取消订单
                                onCancleClick(takeOutOrderEntity);
                                onChangeTakeoutStatusCount();
                            }
                        }
                        if (mainMenuHandler != null) {
                            mainMenuHandler.sendEmptyMessage(1);
                        }
                        break;
                    case 4://更新打印机状态
                        if (mainMenuHandler != null) {
                            mainMenuHandler.sendEmptyMessage(3);
                        }
                        if (kitchenPrintHandler != null)
                            kitchenPrintHandler.sendEmptyMessage(0);
                        break;
                    case 5://更新打印机状态
                        if (mainMenuHandler != null) {
                            mainMenuHandler.sendEmptyMessage(3);
                        }
                        if (kitchenPrintHandler != null)
                            kitchenPrintHandler.sendEmptyMessage(1);
                        break;
                    case 6://有新的排号
                        if (mainMenuHandler != null)
                            mainMenuHandler.sendEmptyMessage(4);
                        if (arrangeSortHandler != null)
                            arrangeSortHandler.sendEmptyMessage(0);
                        if (arrangeHistoryHandler != null) {
                            arrangeHistoryHandler.sendEmptyMessage(0);
                        }
                        CustomMethod.TtSpeech(tts, "您有新的排号");
                        break;
                    case 8://确认交接班
                        if (turnoverHandler != null) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("turnoverBean", intent.getParcelableExtra("turnoverBean"));
                            msg.setData(bundle);
                            msg.what = 0;
                            turnoverHandler.sendMessage(msg);
                        }
                        break;
                    case 9://接收副收银信息
                        String ip = intent.getStringExtra("ip");
                        String msgStr = intent.getStringExtra("msg");
                        Message msg1 = new Message();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("msg", ip + ":" + msgStr);
                        msg1.setData(bundle1);
                        msg1.what = 2;
                        Toast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        break;
                    case 10:
                        String msgStr1 = intent.getStringExtra("msg");
                        Message msg2 = new Message();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("msg", msgStr1);
                        msg2.setData(bundle2);
                        msg2.what = 1;
                        break;
                    case 11:
                        String msgStr2 = intent.getStringExtra("msg");
                        Message msg3 = new Message();
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("msg", msgStr2);
                        msg3.setData(bundle3);
                        msg3.what = 0;
                        break;
                    case 12://桌位锁定
                        if (forHereHandler != null) {
                            forHereHandler.sendEmptyMessage(0);
                        }
                        break;
                    case 13://桌位解锁
                        if (forHereHandler != null) {
                            forHereHandler.sendEmptyMessage(0);
                        }
                        break;
                    case 14://刷新桌位
                        if (forHereHandler != null) {
                            DBHelper.getInstance(getApplicationContext()).updateTableStatus();
                            forHereHandler.sendEmptyMessage(0);
                        }
                        break;
                    case 15://收到到店点餐消息
                        if (forHereHandler != null) {
                            DBHelper.getInstance(getApplicationContext()).updateTableStatus();
                            forHereHandler.sendEmptyMessage(0);
                        }
                        EventBus.getDefault().post(new SnackUnreadMessageRefreshEvent());
                        EventBus.getDefault().post(new SnackOrderListRefreshEvent(null));
                        CustomMethod.TtSpeech(tts, "您有新的微信点餐订单");
                        break;
                    case 16:
                        WXMessageEntity wxMessageEntity = intent.getParcelableExtra("wxMessage");
                        if (mainMenuHandler != null)
                            mainMenuHandler.sendEmptyMessage(2);
                        if (wxMsgHandler != null) {
                            Message msg4 = new Message();
                            msg4.what = 1;
                            msg4.obj = wxMessageEntity;
                            wxMsgHandler.sendMessage(msg4);
                        }
                        if (allMessageHandler != null) {
                            allMessageHandler.sendEmptyMessage(0);
                        }
                        CustomMethod.TtSpeech(tts, "您有新的微信消息");
                        break;
                    case 17:
                        //暂停服务
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case 18:
                        //是否打开钱箱
                        CustomMethod.showMessage(MainActivity.this, "打印机连接失败");
                        break;
                    case 19:
                        //放弃排号
                        if (mainMenuHandler != null)
                            mainMenuHandler.sendEmptyMessage(4);
                        if (arrangeSortHandler != null)
                            arrangeSortHandler.sendEmptyMessage(0);
                        break;
                    case 21:
                        if (forHereHandler != null) {
                            DBHelper.getInstance(getApplicationContext()).updateTableStatus();
                            forHereHandler.sendEmptyMessage(0);
                        }
                        if (mCashierTopLeftHandler != null) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = intent.getStringExtra("orderId");
                            mCashierTopLeftHandler.sendMessage(msg);
                        }
                        if (mCashierHandler != null) {
                            Message msg = new Message();
                            msg.what = 16;
                            msg.obj = intent.getStringExtra("orderId");
                            mCashierHandler.sendMessage(msg);
                        }
                        EventBus.getDefault().post(new SnackUnreadMessageRefreshEvent());
                        EventBus.getDefault().post(new SnackCashierTopLeftRefreshEvent());
                        EventBus.getDefault().post(new SnackCashierTopRightRefreshEvent());
                        EventBus.getDefault().post(new SnackOrderListRefreshEvent(null));
                        break;
                    case 22:
                        if (mainMenuHandler != null)
                            mainMenuHandler.sendEmptyMessage(4);
                        if (arrangeSortHandler != null)
                            arrangeSortHandler.sendEmptyMessage(0);
                        if (arrangeHistoryHandler != null) {
                            arrangeHistoryHandler.sendEmptyMessage(0);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void selectWXMessage(final WXMessageEntity wxMessageEntity) {
        if (wxMessageEntity.getWxType() == 1) {
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
            dialog.setTitle("微信呼叫服务");
            dialog.setMessage(wxMessageEntity.getWxContent());
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DBHelper.getInstance(getApplicationContext()).changeWXReaded(wxMessageEntity);
                    if (mainMenuHandler != null)
                        mainMenuHandler.sendEmptyMessage(2);
                    if (wxMsgHandler != null) {
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = wxMessageEntity;
                        wxMsgHandler.sendMessage(msg);
                    }
                    if (allMessageHandler != null) {
                        allMessageHandler.sendEmptyMessage(0);
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (wxMessageEntity.getWxType() == 0) {
            final CustomeRemindDialog customeRemindDialog = new CustomeRemindDialog(this, wxMessageEntity);
            customeRemindDialog.setRemindDialogClickListener(new CustomeRemindDialog.RemindDialogClickListener() {
                @Override
                public void onRemindCancle() {
                    customeRemindDialog.dismiss();
                }

                @Override
                public void onRemindConfirm(String orderId, ArrayList<OrderDishEntity> orderDishEntities) {
                    remindPrint(orderId, orderDishEntities);
                    DBHelper.getInstance(getApplicationContext()).changeWXReaded(wxMessageEntity);
                    customeRemindDialog.dismiss();
                    if (mainMenuHandler != null)
                        mainMenuHandler.sendEmptyMessage(2);
                    if (wxMsgHandler != null) {
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = wxMessageEntity;
                        wxMsgHandler.sendMessage(msg);
                    }
                    if (allMessageHandler != null) {
                        allMessageHandler.sendEmptyMessage(0);
                    }
                }

                @Override
                public void onRemindedClick() {
                    DBHelper.getInstance(getApplicationContext()).changeWXReaded(wxMessageEntity);
                    customeRemindDialog.dismiss();
                    if (mainMenuHandler != null)
                        mainMenuHandler.sendEmptyMessage(2);
                    if (wxMsgHandler != null) {
                        wxMsgHandler.sendEmptyMessage(0);
                    }
                    if (allMessageHandler != null) {
                        allMessageHandler.sendEmptyMessage(0);
                    }
                }
            });
        }
    }

    //催菜打印
    private void remindPrint(String orderId, ArrayList<OrderDishEntity> retreatDishes) {
        if (orderId != null) {
            if (retreatDishes.size() > 0) {
                ArrayList<PrintKitchenEntity> printKitchenEntities = DBHelper.getInstance(getApplicationContext()).getAllKichenPrinter();
                for (PrintKitchenEntity printKitchen :
                        printKitchenEntities) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            retreatDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if (dishEntity != null) {
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, unitName, 0, 0, new int[]{100, 100});
                        if (DBHelper.getInstance(getApplicationContext()).isPrint(printKitchen, printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if (orderDishEntity.getType() == 1) {
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if (dishEntity1 != null) {
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish, printDishBean, unitName1);
                                    if (DBHelper.getInstance(getApplicationContext()).isPrint(printKitchen, printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        onRemindPrint(printDishBeenes1, orderId, printKitchen, null);
                    }
                }

                //前台商品打印
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
                if (printCashierEntity.getIsPrintDish() == 1) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (OrderDishEntity orderDishEntity :
                            retreatDishes) {
                        String unitName = "份";
                        DishEntity dishEntity = DBHelper.getInstance(getApplicationContext()).queryOneDishEntity(orderDishEntity.getDishId());
                        if (dishEntity != null) {
                            unitName = dishEntity.getCheckOutUnit();
                        }
                        PrintDishBean printDishBean = new PrintDishBean(orderDishEntity, unitName, 0, 0, new int[]{100, 100});
                        if (DBHelper.getInstance(getApplicationContext()).isPrint(printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                        if (orderDishEntity.getType() == 1) {
                            //套餐
                            ArrayList<OrderTaocanGroupDishEntity> orderTaocanGroupDishEntities = new ArrayList<>();
                            orderTaocanGroupDishEntities.addAll(DBHelper.getInstance(getApplicationContext()).getOrderedTaocanDish(orderDishEntity));
                            for (OrderTaocanGroupDishEntity orderTaocanGroupDish :
                                    orderTaocanGroupDishEntities) {
                                if (orderTaocanGroupDish.getStatus() == 1) {
                                    String unitName1 = "份";
                                    DishEntity dishEntity1 = DBHelper.getInstance(getApplicationContext()).queryOneDishEntity(orderTaocanGroupDish.getDishId());
                                    if (dishEntity1 != null) {
                                        unitName1 = dishEntity1.getCheckOutUnit();
                                    }
                                    PrintDishBean printDishBean1 = new PrintDishBean(orderTaocanGroupDish, printDishBean, unitName1);
                                    if (DBHelper.getInstance(getApplicationContext()).isPrint(printDishBean1)) {
                                        printDishBeenes1.add(printDishBean1);
                                    }
                                }
                            }
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        onRemindPrint(printDishBeenes1, orderId, null, null);
                    }
                }
            }
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    //打印
    @Override
    public void onPrintData(String orderId) {
        printKitchen(orderId);
    }

    @Override
    public void onPrintTakeOutOrder(String orderId) {
        if (mPrintService != null) {
            mPrintService.printWaimai(orderId);
        }
    }

    //外卖厨打
    @Subscribe(threadMode = ThreadMode.MAIN)
    private void printKitchen(String orderId) {
        if (orderId != null) {
            ArrayList<PrintDishBean> printDishBeenes = DBHelper.getInstance(getApplicationContext()).getAllPrintDish(orderId);
            if (printDishBeenes.size() > 0) {
                ArrayList<PrintKitchenEntity> printKitchenEntities = DBHelper.getInstance(getApplicationContext()).getAllKichenPrinter();
                for (PrintKitchenEntity printKitchen :
                        printKitchenEntities) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (PrintDishBean printDishBean :
                            printDishBeenes) {
                        if (DBHelper.getInstance(getApplicationContext()).isPrint(printKitchen, printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        if (mPrintService != null) {
                            mPrintService.addRunnable(printKitchen.getPrintKitchenIp(), new KitchenPrint(getApplicationContext(), printDishBeenes1, orderId, printKitchen, false));
                        }
                    }
                }

                //前台商品打印
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
                if (printCashierEntity.getIsPrintDish() == 1) {
                    ArrayList<PrintDishBean> printDishBeenes1 = new ArrayList<>();
                    for (PrintDishBean printDishBean :
                            printDishBeenes) {
                        if (DBHelper.getInstance(getApplicationContext()).isPrint(printDishBean)) {
                            printDishBeenes1.add(printDishBean);
                        }
                    }
                    if (printDishBeenes1.size() > 0) {
                        printKitchen(printDishBeenes1, orderId, null, false);
                    }
                }

                //标签打印
                int storeVersion = getSharedPreferences("loginData", Context.MODE_PRIVATE).getInt("storeversion", 0);
                if (storeVersion == 2) {
                    //奶茶版
                    EventBus.getDefault().post(new PrintLabelEvent(printDishBeenes, orderId, null));
                }
            }

            if (mPrintService != null) {
                mPrintService.printWaimai(orderId);
            }

        } else {
        }
    }

    private LFragmentOrderDish mLFragmentOrderDish;
    private Handler mOrderHandler;
    private Handler mDishListHandler;
    private TextToSpeech tts;

    @Override
    public void onDishListItemClick(OrderDishEntity orderDishEntity) {
        if (mOrderDishHandler != null) {
            mOrderDishHandler.sendEmptyMessage(0);
        }
        if (mOrderHandler != null) {
            Message msg = new Message();
            msg.what = 0;
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderDishEntity", orderDishEntity);
            msg.setData(bundle);
            mOrderHandler.sendMessage(msg);
        }
    }

    @Override
    public void configDish(String dishId) {
        if (mOrderDishHandler != null) {
            Message msg = new Message();
            msg.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("dishId", dishId);
            msg.setData(bundle);
            mOrderDishHandler.sendMessage(msg);
        }
    }

    @Override
    public void selectTaocan(String taocanId, String orderId) {
        if (mOrderDishHandler != null) {
            Message msg = new Message();
            msg.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("taocanId", taocanId);
            bundle.putString("orderId", orderId);
            msg.setData(bundle);
            mOrderDishHandler.sendMessage(msg);
        }
    }

    @Override
    public void onCancle(String dishId) {
        if (mOrderDishHandler != null) {
            mOrderDishHandler.sendEmptyMessage(3);
        }
    }

    @Override
    public void onDelete(String orderDishId) {
        if (mOrderDishHandler != null) {
            mOrderDishHandler.sendEmptyMessage(4);
        }

        if (mOrderHandler != null) {
            Message message = new Message();
            message.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("orderDishId", orderDishId);
            message.setData(bundle);
            mOrderHandler.sendMessage(message);
        }

        OrderDishEntity orderDishEntity = null;
        if ((orderDishEntity = DBHelper.getInstance(getApplicationContext()).queryOneOrderDishEntity(orderDishId)) != null && mDishListHandler != null) {
            if (orderDishEntity.getIsFromWX() == 1) {
            }
            Message message1 = new Message();
            message1.what = 3;
            Bundle bundle1 = new Bundle();
            bundle1.putString("dishId", orderDishEntity.getDishId());
            bundle1.putInt("type", orderDishEntity.getType());
            message1.setData(bundle1);
            mDishListHandler.sendMessage(message1);
        }
    }

    @Override
    public void onConfirm(String dishId, String specifyId, String practiceId, String orderDishId, String note, double count) {
        if (mOrderDishHandler != null) {
            mOrderDishHandler.sendEmptyMessage(5);
        }

        if (mOrderHandler != null) {
            Message message = new Message();
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("dishId", dishId);
            bundle.putString("specifyId", specifyId);
            bundle.putString("practiceId", practiceId);
            bundle.putString("orderDishId", orderDishId);
            bundle.putString("note", note);
            bundle.putDouble("count", count);
            message.setData(bundle);
            mOrderHandler.sendMessage(message);
        }

        if (mDishListHandler != null) {
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putString("dishId", dishId);
            bundle.putInt("type", 0);
            message.setData(bundle);
            mDishListHandler.sendMessage(message);
        }
    }

    @Override
    public void onClose(int type, String orderDishId) {
        if (type == 0 && mOrderDishHandler != null) {
            mOrderDishHandler.sendEmptyMessage(6);
        } else if (type == 1 && mCashierMainHandler != null) {
            mCashierMainHandler.sendEmptyMessage(6);
        }
    }

    @Override
    public void addDish(OrderDishEntity orderDishEntity) {
        if (mOrderDishHandler != null) {
            mOrderDishHandler.sendEmptyMessage(7);
        }

        if (mDishListHandler != null) {
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putString("dishId", orderDishEntity.getDishId());
            bundle.putInt("type", orderDishEntity.getType());
            message.setData(bundle);
            mDishListHandler.sendMessage(message);
        }
    }

    @Override
    public void reduceDish(OrderDishEntity orderDishEntity) {
        if (mOrderDishHandler != null) {
            mOrderDishHandler.sendEmptyMessage(8);
        }

        if (mDishListHandler != null) {
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putString("dishId", orderDishEntity.getDishId());
            bundle.putInt("type", orderDishEntity.getType());
            message.setData(bundle);
            mDishListHandler.sendMessage(message);
        }
        sendDifferBroadcast(1, orderDishEntity.getOrderId(), false);
    }

    @Override
    public void dishDetail(String orderDishId) {
        if (mOrderDishHandler != null) {
            Message msg = new Message();
            msg.what = 9;
            Bundle bundle = new Bundle();
            bundle.putString("orderDishId", orderDishId);
            msg.setData(bundle);
            mOrderDishHandler.sendMessage(msg);
        }
    }

    @Override
    public void onOrderDishConfirm() {
        setMainFragment(false);
        sendDifferBroadcast(0, null, false);
    }

    public void setOrderDishHandler(Handler handler) {
        this.mOrderDishHandler = handler;
    }

    public void setOrderHandler(Handler handler) {
        this.mOrderHandler = handler;
    }

    public void setDishListHandler(Handler handler) {
        this.mDishListHandler = handler;
    }

    @Override
    public void openTable(String tableId, String orderId) {
        setFragmentOrderDish(tableId, orderId);
        if (mOrderDishHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            bundle.putString("tableId", tableId);
            msg.setData(bundle);
            msg.what = 10;
            mOrderDishHandler.sendMessage(msg);
        }

        if (mDishListHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.setData(bundle);
            msg.what = 1;
            mDishListHandler.sendMessage(msg);
        }

        if (mOrderHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            bundle.putString("tableId", tableId);
            msg.setData(bundle);
            msg.what = 3;
            mOrderHandler.sendMessage(msg);
        }
    }

    private void setFragmentOrderDish(String tableId, String orderId) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans, false);
        if (mLFragmentOrderDish == null) {
            mLFragmentOrderDish = new LFragmentOrderDish();
            Bundle bundle = new Bundle();
            bundle.putString("tableId", tableId);
            bundle.putString("orderId", orderId);
            mLFragmentOrderDish.setArguments(bundle);
            trans.add(R.id.container, mLFragmentOrderDish);
        } else {
            trans.show(mLFragmentOrderDish);
        }
        trans.commitAllowingStateLoss();
    }

    private Handler mCashierOrderHandler, mCashierHandler, mCashierTopLeftHandler, mCashierTopRightHandler, mCashierMainHandler;
    private int currentPay;

    public void setCashierMainHandler(Handler handler) {
        this.mCashierMainHandler = handler;
    }

    public void setCashierOrderHandler(Handler orderHandler) {
        mCashierOrderHandler = orderHandler;
    }

    public void setCashierHandler(Handler cashierHandler) {
        this.mCashierHandler = cashierHandler;
    }

    public void setCashierTopLeftHandler(Handler cashierTopLeftHandler) {
        mCashierTopLeftHandler = cashierTopLeftHandler;
    }

    public void setCashierTopRightHandler(Handler cashierTopRightHandler) {
        mCashierTopRightHandler = cashierTopRightHandler;
    }


    //********************************************接口回调
    //账单界面
    @Override
    public void cashierDishDetail(String orderDishId, String orderId, String tableId) {
        try {
            OrderDishEntity orderDishEntity = DBHelper.getInstance(getApplicationContext()).queryOneOrderDishEntity(orderDishId);
            if (orderDishEntity.getType() == 0) {
                //非套餐
                if (mCashierMainHandler != null) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("orderDishId", orderDishId);
                    msg.what = 0;
                    msg.setData(bundle);
                    mCashierMainHandler.sendMessage(msg);
                }
            } else {
                //套餐
                setLFragmentSelectTaocan(orderDishEntity, ActivityIntentUtil.FRAGMENT_CASHIER, orderDishEntity.getDishId(), orderId, tableId);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onTaocanDishDetail(OrderDishEntity orderDishEntity, String activityTag, String taocanId, String orderId, String tableId) {
        setLFragmentSelectTaocan(orderDishEntity, activityTag, taocanId, orderId, tableId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTaocanDetailClick(OnTaocanDetailClickEvent event) {
        if (event != null) {
            onTaocanDishDetail(event.getOrderDishEntity(), event.getTag(), event.getTaocanId(), event.getOrderId(), event.getTableId());
        }
    }

    //退菜
    @Override
    public void retreatDish(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 1;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    //赠菜
    @Override
    public void presentDish(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 2;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    //催菜
    @Override
    public void remindDish(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 3;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void changeJoinOrder() {
        if (mCashierMainHandler != null) {
            mCashierMainHandler.sendEmptyMessage(4);
        }
    }

    @Override
    public void changeChildOrder(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 5;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void onMLClick(final String orderId, final double money) {
        if (money > 0) {
            boolean result = false;
            String employeeId = getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("employeeId", null);
            if (employeeId != null) {
                EmployeeEntity employeeEntity = DBHelper.getInstance(this).getEmployeeById(employeeId);
                if (employeeEntity != null) {
                    result = DBHelper.getInstance(this).setMLMoneyByOrderId(orderId, money, employeeEntity);
                }
            }
            if (result) {
                if (mCashierHandler != null)
                    mCashierHandler.sendEmptyMessage(0);
                if (mCashierTopLeftHandler != null)
                    mCashierTopLeftHandler.sendEmptyMessage(0);
                if (mCashierTopRightHandler != null)
                    mCashierTopRightHandler.sendEmptyMessage(0);
            } else {
                //权限验证对话框
                final CustomeAuthorityDialog dialog = new CustomeAuthorityDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "");
                dialog.setOnAuthorityListener(new CustomeAuthorityDialog.OnAuthorityListener() {
                    @Override
                    public void onAuthorityCancle() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onAuthoritySuccess(View view, EmployeeEntity employeeEntity) {
                        if (employeeEntity != null) {
                            boolean result = DBHelper.getInstance(getApplicationContext()).setMLMoneyByOrderId(orderId, money, employeeEntity);
                            if (result) {
                                if (mCashierHandler != null)
                                    mCashierHandler.sendEmptyMessage(0);
                                if (mCashierTopLeftHandler != null)
                                    mCashierTopLeftHandler.sendEmptyMessage(0);
                                if (mCashierTopRightHandler != null)
                                    mCashierTopRightHandler.sendEmptyMessage(0);
                                dialog.dismiss();
                            } else {
                                Snackbar.make(view, "该员工无权限", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        } else {
            CustomMethod.showMessage(this, "抹零金额不能小于0");
        }
    }

    //应收款界面
    @Override
    public void onContinuCashier(final String orderId, boolean isOpenJoinOrder) {
        final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(this, 0);
        dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
            @Override
            public void onPaymentSelected(Payment payment) {
                currentPay = 0;
                ShopPaymentEntity payModeEntity = (ShopPaymentEntity) payment;
                if (mCashierHandler != null) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId", orderId);
                    bundle.putParcelable("payment", payModeEntity);
                    msg.setData(bundle);
                    msg.what = 1;
                    mCashierHandler.sendMessage(msg);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onKeyClick(int keyCode, String orderId, boolean isOpenJoinOrder, String tableId) {
        switch (keyCode) {
            case CashierKeyboardUtil.KEYCODE_ZDDZ:
                onDiscountAllClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_BFDZ:
                onDiscountSomeClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_FADZ:
                onDiscountClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_QKSY:
                onClearAll(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_XJ:
                onCashClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_YHK:
                onBankClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_WXZF:
                onWXPayClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_ZFB:
                onAliPayClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_TG:
                onGroupClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_GZ:
                onAccountClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_HYZF:
                onOtherClick(orderId, isOpenJoinOrder);
                break;
            case CashierKeyboardUtil.KEYCODE_HYYH:
                if (orderId != null) {
                    OrderEntity orderEntity = DBHelper.getInstance(getApplicationContext()).getOneOrderEntity(orderId);
                    if (orderEntity != null) {
                        onVipDiscountClick(orderEntity);
                    }
                }
                break;
            case CashierKeyboardUtil.KEYCODE_FH:
                onCancleClick();
                break;
            case CashierKeyboardUtil.KEYCODE_JZWB:
                onCashierOverClick(orderId, isOpenJoinOrder, tableId);
                break;
        }
    }

    private void onVipDiscountClick(OrderEntity orderEntity) {
        if (orderEntity.getVipNo() != null && mCashierHandler != null) {
            //该订单已使用会员
            mCashierHandler.sendEmptyMessage(15);
        } else {
            mCashierHandler.sendEmptyMessage(14);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPrintCaiwulian(PrintCaiwulianEvent event) {
        if (event != null && event.getOrderId() != null) {
            if (mPrintService != null) {
                mPrintService.printCaiwulian(event.getOrderId());
            }
        }
    }

    public void onCashierOverClick(String orderId, boolean isOpenJoinOrder, final String tableId) {
        if (DBHelper.getInstance(getApplicationContext()).getReceivableMoneyByOrderId(orderId) == 0) {
            //点击结账完毕
            //子单
            DBHelper.getInstance(getApplicationContext()).cancleOneJoinOrder(orderId);
            boolean result = DBHelper.getInstance(getApplicationContext()).cashierOver(orderId);
            if (result) {
                DBHelper.getInstance(getApplicationContext()).insertUploadData(orderId, orderId, 7);
                if (mPrintService != null) {
                    mPrintService.printCaiwulian(orderId);
                }
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle("提示");
                dialog.setMessage("是否立即清台？");
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "立即清台", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper.getInstance(MainActivity.this).updateTableStatus(tableId);
                        dialog.dismiss();
                        setMainFragment(false);
                        if (forHereHandler != null) {
                            forHereHandler.sendEmptyMessage(0);
                        }
                        sendDifferBroadcast(0, null, false);
                    }
                });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "暂时不", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper.getInstance(MainActivity.this).changeToChingTable(tableId);
                        dialog.dismiss();
                        setMainFragment(false);
                        if (forHereHandler != null) {
                            forHereHandler.sendEmptyMessage(0);
                        }
                        sendDifferBroadcast(0, null, false);
                    }
                });
                dialog.show();
            } else {
                CustomMethod.showMessage(this, "结账失败，请重新尝试");
            }
        } else {
            CustomMethod.showMessage(this, "未结完账单，请继续结账");
        }
    }

    public void onCancleClick() {
        setMainFragment(false);
        if (forHereHandler != null) {
            forHereHandler.sendEmptyMessage(0);
        }
        sendDifferBroadcast(0, null, false);
    }

    //打折界面
    public void onDiscountAllClick(String orderId, boolean isOpenJoinOrder) {
        currentPay = 4;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(5);
    }

    public void onDiscountSomeClick(String orderId, boolean isOpenJoinOrder) {
        currentPay = 5;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(6);
    }

    public void onDiscountClick(String orderId, boolean isOpenJoinOrder) {
        currentPay = 6;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(7);
    }

    public void onClearAll(final String orderId, final boolean isOpenJoinOrder) {
        //清空所有支付和打折
        if (isOpenJoinOrder) {
        } else {
            final CustomeClearAllDialog dialog = new CustomeClearAllDialog();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            dialog.setArguments(bundle);
            dialog.setOnAuthorityListener(new CustomeClearAllDialog.OnClearAllListener() {
                @Override
                public void onClearSuccess() {
                    if (mCashierOrderHandler != null)
                        mCashierOrderHandler.sendEmptyMessage(0);
                    if (mCashierTopLeftHandler != null)
                        mCashierTopLeftHandler.sendEmptyMessage(0);
                    if (mCashierTopRightHandler != null)
                        mCashierTopRightHandler.sendEmptyMessage(0);
                    currentPay = -1;
                    if (mCashierHandler != null)
                        mCashierHandler.sendEmptyMessage(0);
                    sendDifferBroadcast(2, orderId, isOpenJoinOrder);
                    dialog.dismiss();
                }
            });
            dialog.show(getSupportFragmentManager(), "");
        }
    }

    //整单打折
    @Override
    public void onDiscountAllCancle() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
    }

    @Override
    public void onDiscountAllConfirm(String value, String orderId, boolean isOpenJoinOrder) {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierOrderHandler != null)
            mCashierOrderHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
        sendDifferBroadcast(2, orderId, isOpenJoinOrder);
    }

    @Override
    public void onSelectAllReason(DiscountHistoryEntity discountHistoryEntity) {
        if (mCashierHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("discountHistory", discountHistoryEntity);
            msg.setData(bundle);
            msg.what = 8;
            mCashierHandler.sendMessage(msg);
        }
    }

    //打折方案
    @Override
    public void onDiscountSchemeCancle() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
    }

    @Override
    public void onDiscountSchemeConfirm(String orderId, boolean isOpenJoinOrder) {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierOrderHandler != null)
            mCashierOrderHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
        sendDifferBroadcast(2, orderId, isOpenJoinOrder);
    }

    @Override
    public void onSelectScheme(DiscountHistoryEntity discountHistoryEntity) {
        if (mCashierHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("discountHistory", discountHistoryEntity);
            msg.setData(bundle);
            msg.what = 10;
            mCashierHandler.sendMessage(msg);
        }
    }

    @Override
    public void onSelectSchemeReason(DiscountHistoryEntity discountHistoryEntity) {
        if (mCashierHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("discountHistory", discountHistoryEntity);
            msg.setData(bundle);
            msg.what = 8;
            mCashierHandler.sendMessage(msg);
        }
    }

    //部分打折
    @Override
    public void onDiscountSomeCancle() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
    }

    @Override
    public void onDiscountSomeConfirm(String orderId, boolean isOpenJoinOrder) {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierOrderHandler != null)
            mCashierOrderHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
        sendDifferBroadcast(2, orderId, isOpenJoinOrder);
    }

    @Override
    public void onSelectSomeGoods(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities) {
        if (mCashierHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("discountHistory", discountHistoryEntity);
            bundle.putParcelableArrayList("someDiscountGoods", someDiscountGoodsEntities);
            msg.setData(bundle);
            msg.what = 9;
            mCashierHandler.sendMessage(msg);
        }
    }

    @Override
    public void onSelectSomeReason(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities) {
        if (mCashierHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("discountHistory", discountHistoryEntity);
            bundle.putParcelableArrayList("someDiscountGoods", someDiscountGoodsEntities);
            msg.setData(bundle);
            msg.what = 8;
            mCashierHandler.sendMessage(msg);
        }
    }

    //打折原因
    @Override
    public void onSelectReason(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        switch (currentPay) {
            case 4:
            case 5:
            case 6:
                bundle.putParcelableArrayList("someDiscountGoods", someDiscountGoodsEntities);
                bundle.putParcelable("discountHistory", discountHistoryEntity);
                msg.setData(bundle);
                msg.what = currentPay + 1;
                if (mCashierHandler != null)
                    mCashierHandler.sendMessage(msg);
                break;
            default:
                currentPay = -1;
                if (mCashierHandler != null)
                    mCashierHandler.sendEmptyMessage(0);
                break;
        }
    }

    //选择打折商品
    @Override
    public void onSelectSomeDiscountGoods(DiscountHistoryEntity discountHistoryEntity, ArrayList<SomeDiscountGoodsEntity> someDiscountGoodsEntities) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        switch (currentPay) {
            case 4:
            case 5:
            case 6:
                bundle.putParcelable("discountHistory", discountHistoryEntity);
                bundle.putParcelableArrayList("someDiscountGoods", someDiscountGoodsEntities);
                msg.setData(bundle);
                msg.what = currentPay + 1;
                if (mCashierHandler != null)
                    mCashierHandler.sendMessage(msg);
                break;
            default:
                currentPay = -1;
                if (mCashierHandler != null)
                    mCashierHandler.sendEmptyMessage(0);
                break;
        }
    }

    //选择打折方案
    @Override
    public void selectedScheme(DiscountHistoryEntity discountHistoryEntity) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        switch (currentPay) {
            case 4:
            case 5:
            case 6:
                bundle.putParcelable("discountHistory", discountHistoryEntity);
                msg.setData(bundle);
                msg.what = currentPay + 1;
                if (mCashierHandler != null)
                    mCashierHandler.sendMessage(msg);
                break;
            default:
                currentPay = -1;
                if (mCashierHandler != null)
                    mCashierHandler.sendEmptyMessage(0);
                break;
        }
    }

    //支付方式界面
    public void onCashClick(final String orderId, boolean isOpenJoinOrder) {
        final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(this, 0);
        dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
            @Override
            public void onPaymentSelected(Payment payment) {
                currentPay = 0;
                ShopPaymentEntity payModeEntity = (ShopPaymentEntity) payment;
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                bundle.putParcelable("payment", payModeEntity);
                msg.setData(bundle);
                msg.what = 1;
                if (mCashierHandler != null)
                    mCashierHandler.sendMessage(msg);
                dialog.dismiss();
            }
        });
    }

    public void onBankClick(final String orderId, boolean isOpenJoinOrder) {
        final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(this, 1);
        dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
            @Override
            public void onPaymentSelected(Payment payment) {
                currentPay = 1;
                ShopPaymentEntity payModeEntity = (ShopPaymentEntity) payment;
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                bundle.putParcelable("payment", payModeEntity);
                msg.setData(bundle);
                msg.what = 2;
                if (mCashierHandler != null)
                    mCashierHandler.sendMessage(msg);
                dialog.dismiss();
            }
        });
    }

    public void onWXPayClick(String orderId, boolean isOpenJoinOrder) {
        currentPay = 2;
        ShopPaymentEntity payModeEntity = new ShopPaymentEntity();
        payModeEntity.setPaymentName("微信");
        payModeEntity.setPaymentType(0);
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putParcelable("payment", payModeEntity);
        msg.setData(bundle);
        msg.what = 3;
        if (mCashierHandler != null)
            mCashierHandler.sendMessage(msg);
    }

    public void onAliPayClick(String orderId, boolean isOpenJoinOrder) {
        currentPay = 2;
        ShopPaymentEntity payModeEntity = new ShopPaymentEntity();
        payModeEntity.setPaymentName("支付宝");
        payModeEntity.setPaymentType(1);
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putParcelable("payment", payModeEntity);
        bundle.putInt("type", 1);
        msg.setData(bundle);
        msg.what = 3;
        if (mCashierHandler != null)
            mCashierHandler.sendMessage(msg);
    }

    public void onGroupClick(final String orderId, boolean isOpenJoinOrder) {
        final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(this, 2);
        dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
            @Override
            public void onPaymentSelected(Payment payment) {
                currentPay = 7;
                GrouponEntity grouponEntity = (GrouponEntity) payment;
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                bundle.putParcelable("groupon", grouponEntity);
                msg.setData(bundle);
                msg.what = 11;
                if (mCashierHandler != null)
                    mCashierHandler.sendMessage(msg);
                dialog.dismiss();
            }
        });
    }

    public void onAccountClick(final String orderId, boolean isOpenJoinOrder) {
        final CustomeSelectPaymentDialog dialog = new CustomeSelectPaymentDialog(this, 3);
        dialog.setOnPaymentSelectedListener(new CustomeSelectPaymentDialog.OnPaymentSelectedListener() {
            @Override
            public void onPaymentSelected(Payment payment) {
                currentPay = 3;
                BillAccountEntity billAccountEntity = (BillAccountEntity) payment;
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                bundle.putParcelable("payment", billAccountEntity);
                msg.setData(bundle);
                msg.what = 4;
                if (mCashierHandler != null)
                    mCashierHandler.sendMessage(msg);
                dialog.dismiss();
            }
        });
    }

    public void onOtherClick(String orderId, boolean isOpenJoinOrder) {
        currentPay = 2;
        ShopPaymentEntity payModeEntity = new ShopPaymentEntity();
        payModeEntity.setPaymentName("会员卡");
        payModeEntity.setPaymentType(2);
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putParcelable("payment", payModeEntity);
        bundle.putInt("type", 2);
        msg.setData(bundle);
        msg.what = 3;
        if (mCashierHandler != null)
            mCashierHandler.sendMessage(msg);
    }

    //现金支付界面
    @Override
    public void onCashCancle() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
    }

    @Override
    public void onCashConfirm(String value) {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
    }

    //挂账付款
    @Override
    public void onAccountUnitClick(BillAccountHistoryEntity billAccountHistoryEntity) {
        currentPay = 3;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        bundle.putParcelable("billHistory", billAccountHistoryEntity);
        msg.setData(bundle);
        msg.what = 12;
        if (mCashierHandler != null)
            mCashierHandler.sendMessage(msg);
    }

    @Override
    public void onAccountPeopleClick(BillAccountHistoryEntity billAccountHistoryEntity) {
        currentPay = 3;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putParcelable("billHistory", billAccountHistoryEntity);
        msg.setData(bundle);
        msg.what = 12;
        if (mCashierHandler != null)
            mCashierHandler.sendMessage(msg);
    }

    @Override
    public void onSignPeopleClick(BillAccountHistoryEntity billAccountHistoryEntity) {
        currentPay = 3;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putParcelable("billHistory", billAccountHistoryEntity);
        msg.setData(bundle);
        msg.what = 12;
        if (mCashierHandler != null)
            mCashierHandler.sendMessage(msg);
    }

    @Override
    public void onSelectBillPerson(String orderId, BillAccountHistoryEntity billAccountHistoryEntity) {
        currentPay = 3;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putParcelable("billHistory", billAccountHistoryEntity);
        msg.setData(bundle);
        msg.what = 4;
        if (mCashierHandler != null)
            mCashierHandler.sendMessage(msg);
    }

    @Override
    public void onAccountCancle() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
    }

    @Override
    public void onAccountConfirm(String orderId, BillAccountHistoryEntity billAccountHistoryEntity) {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
        if (mPrintService != null) {
            mPrintService.printAccountPay(orderId, billAccountHistoryEntity);
        }
    }

    //银行卡付款
    @Override
    public void onBankCancle() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
    }

    @Override
    public void onBankConfirm() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
    }

    //电子支付
    @Override
    public void onElectricCancle() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
    }

    @Override
    public void onElectricConfirm() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
    }

    //团购
    @Override
    public void onGrouponCancle() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
    }

    @Override
    public void onGrouponConfirm(String orderId) {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
        if (mCashierOrderHandler != null)
            mCashierOrderHandler.sendEmptyMessage(0);
        sendDifferBroadcast(2, orderId, false);
    }

    @Override
    public void onSelectGroupon(GrouponEntity grouponEntity, GrouponTaocanEntity grouponTaocanEntity, PayModeEntity payModeEntity) {
        currentPay = 7;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putParcelable("grouponEntity", grouponEntity);
        bundle.putParcelable("grouponTaocan", grouponTaocanEntity);
        bundle.putParcelable("payment", payModeEntity);
        msg.setData(bundle);
        msg.what = 13;
        if (mCashierHandler != null)
            mCashierHandler.sendMessage(msg);
    }

    //选择团购套餐
    @Override
    public void onSelectgrouponTaocanEntity(String orderId, GrouponTaocanEntity grouponTaocanEntity, PayModeEntity payModeEntity, GrouponEntity grouponEntity) {
        currentPay = 7;
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putParcelable("grouponTaocan", grouponTaocanEntity);
        bundle.putParcelable("payment", payModeEntity);
        bundle.putParcelable("groupon", grouponEntity);
        msg.setData(bundle);
        msg.what = 11;
        if (mCashierHandler != null)
            mCashierHandler.sendMessage(msg);
    }

    //退菜界面
    @Override
    public void onRetreatBackClick(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 7;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void onRetreatAllClick(ArrayList<OrderDishEntity> orderDishEntities, String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            bundle.putParcelableArrayList("orderDishEntities", orderDishEntities);
            msg.what = 8;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void onRetreatClick(ArrayList<OrderDishEntity> orderDishEntities, String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            bundle.putParcelableArrayList("orderDishEntities", orderDishEntities);
            msg.what = 9;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }

    }

    //退菜单界面
    @Override
    public void onRetreatOrderCancle(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 10;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }

    }

    @Override
    public void onRetreatOrderConfirm(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 11;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
    }

    //选择赠菜界面
    @Override
    public void onPresentBackClick(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 12;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void onPresentAllClick(ArrayList<OrderDishEntity> orderDishEntities, String orderId) {

    }

    @Override
    public void onPresentClick(ArrayList<OrderDishEntity> orderDishEntities, String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            bundle.putParcelableArrayList("orderDishEntities", orderDishEntities);
            msg.what = 13;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }

    }

    //赠菜单界面
    @Override
    public void onPresentOrderCancle(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 14;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }

    }

    @Override
    public void onPresentOrderConfirm(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 15;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
    }

    //催菜
    @Override
    public void onRemindClick(String orderId, ArrayList<OrderDishEntity> orderDishEntities) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 16;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void onRemindAllClick(String orderId, ArrayList<OrderDishEntity> orderDishEntities) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 16;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void onRemindBackClick(String orderId) {
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            msg.what = 16;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void presentDish(String orderId, String tableId, int type) {
        setCashierFragment(orderId, tableId, type);
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            bundle.putString("tableId", tableId);
            bundle.putInt("tag", type);
            msg.what = 17;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void retreatDish(String orderId, String tableId, int type) {
        setCashierFragment(orderId, tableId, type);
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            bundle.putString("tableId", tableId);
            bundle.putInt("tag", type);
            msg.what = 17;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void remindDish(String orderId, String tableId, int type) {
        setCashierFragment(orderId, tableId, type);
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            bundle.putString("tableId", tableId);
            bundle.putInt("tag", type);
            msg.what = 17;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    @Override
    public void cashier(String orderId, String tableId, int type) {
        try {
            OrderEntity orderEntity = DBHelper.getInstance(MainActivity.this).getOneOrderEntity(orderId);
            if (orderEntity.getIsUpload() == 1) {
                DBHelper.getInstance(MainActivity.this).deleteWxOrderMessage(orderId, 1);
                if (forHereHandler != null) {
                    forHereHandler.sendEmptyMessage(0);
                }
            }
        } catch (Exception e) {

        }
        setCashierFragment(orderId, tableId, type);
        if (mCashierMainHandler != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orderId);
            bundle.putString("tableId", tableId);
            bundle.putInt("tag", type);
            msg.what = 17;
            msg.setData(bundle);
            mCashierMainHandler.sendMessage(msg);
        }
    }

    private LFragmentCashier mLFragmentCashier;

    private void setCashierFragment(String orderId, String tableId, int type) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans, false);
        if (mLFragmentCashier == null) {
            mLFragmentCashier = new LFragmentCashier();
            Bundle bundle = new Bundle();
            bundle.putString("tableId", tableId);
            bundle.putString("orderId", orderId);
            bundle.putInt("tag", type);
            mLFragmentCashier.setArguments(bundle);
            trans.add(R.id.container, mLFragmentCashier);
        } else {
            trans.show(mLFragmentCashier);
        }
        trans.commitAllowingStateLoss();
    }

    @Override
    public void onLogOut() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("isUpdate", false);
        startActivity(intent);
        finish();
    }

    @Override
    public void onOver() {

    }

    @Override
    public void printOrder(String orderId, int type) {
        if (mPrintService != null) {
            printKehuLianQR(orderId);
        }
    }

    private void printKehuLianQR(final String orderId) {
        try {
            String payMoney = DBHelper.getInstance(getApplicationContext()).getReceivableMoneyByOrderId(orderId) + "";
            String orderNumber = "";
            OrderEntity orderEntity = DBHelper.getInstance(getApplicationContext()).getOneOrderEntity(orderId);
            if (orderEntity != null) {
                orderNumber += orderEntity.getOrderNumber1();
            }
            String partnerCode = getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("partnerCode", "");
            final String qrStr = getResources().getString(R.string.PAY_QR) + "?m=" + payMoney + "&p=" + partnerCode + "&id=" + orderId + "&no=" + orderNumber;
            final String filePath = CustomMethod.getFileRoot(this) + File.separator
                    + "qr_order_pay" + ".png";
            //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean success = CustomMethod.createQRImage(getApplicationContext(), qrStr, 320, 320, null, filePath);
                    if (success) {
                        mPrintService.printKehuLian(orderId);
                    }
                }
            }).start();
        } catch (Exception e) {

        }
    }

    @Override
    public void printCaiwulian(String orderId) {
        if (mPrintService != null) {
            mPrintService.printCaiwulian(orderId);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventPrintKitchen(PrintKitchenEvent event) {
        if (event != null) {
            if (event.getPrintKitchenEntity() == null) {//前台打印的商品
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
                if (printCashierEntity.getPrintDishType() == 0) {
                    //usb打印
                    if (mPrintService != null)
                        mPrintService.qiantaiPrintDish(event.getPrintDishBeenes(), event.getOrderId(), event.isAddDish());
                } else {
                    //网口打印
                    if (printCashierEntity.getPrintDishIp() != null && printCashierEntity.getPrintDishIp().length() > 0) {
                        if (mPrintService != null)
                            mPrintService.addRunnable(printCashierEntity.getPrintDishIp(), new QiantaiPrintDishRunnable(getApplicationContext(), event.getPrintDishBeenes(), event.getOrderId(), printCashierEntity.getPrintDishIp(), event.isAddDish()));
                    }
                }
            } else {
                if (mPrintService != null)
                    mPrintService.addRunnable(event.getPrintKitchenEntity().getPrintKitchenIp(), new KitchenPrint(getApplicationContext(), event.getPrintDishBeenes(), event.getOrderId(), event.getPrintKitchenEntity(), event.isAddDish()));
            }
        }
    }

    @Override
    public void printKitchen(ArrayList<PrintDishBean> printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, boolean isAddDish) {
        if (printKitchenEntity == null) {//前台打印的商品
            PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
            if (printCashierEntity.getPrintDishType() == 0) {
                //usb打印
                if (mPrintService != null)
                    mPrintService.qiantaiPrintDish(printDishBeenes, orderId, isAddDish);
            } else {
                //网口打印
                if (printCashierEntity.getPrintDishIp() != null && printCashierEntity.getPrintDishIp().length() > 0) {
                    if (mPrintService != null)
                        mPrintService.addRunnable(printCashierEntity.getPrintDishIp(), new QiantaiPrintDishRunnable(getApplicationContext(), printDishBeenes, orderId, printCashierEntity.getPrintDishIp(), isAddDish));
                }
            }
        } else {
            if (mPrintService != null)
                mPrintService.addRunnable(printKitchenEntity.getPrintKitchenIp(), new KitchenPrint(getApplicationContext(), printDishBeenes, orderId, printKitchenEntity, isAddDish));
        }
    }

    @Override
    public void printXFDL(ArrayList<PrintDishBean> printDishBeenes, String orderId, String ip) {
        if (ip == null) {
            //usb打印
            mPrintService.usbPrintXFDL(printDishBeenes, orderId);
        } else {
            //网口打印
            mPrintService.addRunnable(ip, new PrintXiaofeiDilianRunnable(getApplicationContext(), printDishBeenes, orderId, ip));
        }
    }

    @Override
    public void printHCL(ArrayList<PrintDishBean> printDishBeenes, String orderId, String ip) {
        if (ip == null) {
            //usb打印
            mPrintService.usbPrintHCL(printDishBeenes, orderId);
        } else {
            //网口打印
            mPrintService.addRunnable(ip, new PrintHuacaiLianRunnable(getApplicationContext(), printDishBeenes, orderId, ip));
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventPrintHCL(PrintHCLEvent event) {
        if (event != null) {
            if (event.getIp() == null) {
                //usb打印
                mPrintService.usbPrintHCL(event.getPrintDishBeenes(), event.getOrderId());
            } else {
                //网口打印
                mPrintService.addRunnable(event.getIp(), new PrintHuacaiLianRunnable(getApplicationContext(), event.getPrintDishBeenes(), event.getOrderId(), event.getIp()));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventPrintLabel(PrintLabelEvent event) {
        if (event != null) {
            if (event.getIp() == null) {
                //usb打印
                mPrintService.testLabel(event.getPrintDishBeenes(), event.getOrderId());
            } else {
                //网口打印
//                mPrintService.addRunnable(event.getIp(), new PrintXiaofeiDilianRunnable(getApplicationContext(), event.getPrintDishBeenes(), event.getOrderId(), event.getIp()));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventPrintXFDL(PrintXFDLEvent event) {
        if (event != null) {
            if (event.getIp() == null) {
                //usb打印
                mPrintService.usbPrintXFDL(event.getPrintDishBeenes(), event.getOrderId());
            } else {
                //网口打印
                mPrintService.addRunnable(event.getIp(), new PrintXiaofeiDilianRunnable(getApplicationContext(), event.getPrintDishBeenes(), event.getOrderId(), event.getIp()));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPrintKHL(SnackPrintKHLEvent event) {
        if (event != null && event.getOrderId() != null && mPrintService != null) {
            printKehuLianQR(event.getOrderId());
        }
    }

    @Override
    public void printKHL(String orderId) {
        if (mPrintService != null) {
            printKehuLianQR(orderId);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRemindPrint(SnackRemindPrintEvent event) {
        if (event != null) {
            onRemindPrint(event.getPrintDishBeen(), event.getOrderId(), event.getPrintKitchenEntity(), event.getEmployeeName());
        }
    }

    @Override
    public void onRemindPrint(ArrayList<PrintDishBean> printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, String employeeName) {
        if (printKitchenEntity == null) {
            PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
            if (printCashierEntity.getPrintDishType() == 0) {
                //usb打印
                if (mPrintService != null) {
                    mPrintService.usbPrintRemind(printDishBeenes, orderId);
                }
            } else {
                //网口打印
                if (printCashierEntity.getPrintDishIp() != null && printCashierEntity.getPrintDishIp().length() > 0) {
                    if (mPrintService != null)
                        mPrintService.addRunnable(printCashierEntity.getPrintDishIp(), new QiantaiRemindPrintRunnable(getApplicationContext(), printDishBeenes, orderId, printCashierEntity.getPrintDishIp(), employeeName));
                }
            }
        } else {
            if (mPrintService != null) {
                mPrintService.addRunnable(printKitchenEntity.getPrintKitchenIp(), new RemindPrintRunnable(getApplicationContext(), printDishBeenes, orderId, printKitchenEntity, employeeName));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRetreatPrint(SnackRetreatPrintEvent event) {
        if (event != null) {
            if (event.getPrintKitchenEntity() == null) {
                PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
                if (printCashierEntity.getPrintDishType() == 0) {
                    //usb打印
                    if (mPrintService != null) {
                        mPrintService.usbPrintRetreat(event.getPrintDishBeen(), event.getOrderId(), event.getEmployeeName(), event.getReason());
                    }
                } else {
                    //网口打印
                    if (printCashierEntity.getPrintDishIp() != null && printCashierEntity.getPrintDishIp().length() > 0) {
                        if (mPrintService != null)
                            mPrintService.addRunnable(printCashierEntity.getPrintDishIp(), new QiantaiRetreatPrintRunnable(getApplicationContext(), event.getPrintDishBeen(), event.getOrderId(), printCashierEntity.getPrintDishIp(), event.getEmployeeName(), event.getReason()));
                    }
                }
            } else {
                if (mPrintService != null) {
                    mPrintService.addRunnable(event.getPrintKitchenEntity().getPrintKitchenIp(), new RetreatPrintRunnable(getApplicationContext(), event.getPrintDishBeen(), event.getOrderId(), event.getPrintKitchenEntity(), event.getEmployeeName(), event.getReason()));
                }
            }
        }
    }

    @Override
    public void onRetreatPrint(ArrayList<PrintDishBean> printDishBeenes, String orderId, PrintKitchenEntity printKitchenEntity, String employeeName, String reason) {
        if (printKitchenEntity == null) {
            PrintCashierEntity printCashierEntity = DBHelper.getInstance(getApplicationContext()).getPrintCashierEntity();
            if (printCashierEntity.getPrintDishType() == 0) {
                //usb打印
                if (mPrintService != null) {
                    mPrintService.usbPrintRetreat(printDishBeenes, orderId, employeeName, reason);
                }
            } else {
                //网口打印
                if (printCashierEntity.getPrintDishIp() != null && printCashierEntity.getPrintDishIp().length() > 0) {
                    if (mPrintService != null)
                        mPrintService.addRunnable(printCashierEntity.getPrintDishIp(), new QiantaiRetreatPrintRunnable(getApplicationContext(), printDishBeenes, orderId, printCashierEntity.getPrintDishIp(), employeeName, reason));
                }
            }
        } else {
            if (mPrintService != null) {
                mPrintService.addRunnable(printKitchenEntity.getPrintKitchenIp(), new RetreatPrintRunnable(getApplicationContext(), printDishBeenes, orderId, printKitchenEntity, employeeName, reason));
            }
        }
    }

    @Override
    public void onPrintAgain(String ip, PrintBean callable) {
        if (mPrintService != null) {
            mPrintService.addRunnable(ip, callable);
        }
    }

    @Override
    public void printWaimai(String orderId) {
        if (mPrintService != null) {
            mPrintService.printWaimai(orderId);
        }
    }

    @Override
    public void printCashierCollection(String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, Map<String, String> orderMoneyDetail, ArrayList<PayModeEntity> payModeEntities) {
        if (mPrintService != null) {
            mPrintService.printCashierCollection(cashierName, isShift, payModeName, dateType, areaTypeName, orderMoneyDetail, payModeEntities);
        }
    }

    @Override
    public void printOrderCollection(String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, Map<String, String> orderMoneyDetail, ArrayList<PayModeEntity> payModeEntities) {
        if (mPrintService != null) {
            mPrintService.printOrderCollection(cashierName, isShift, payModeName, dateType, areaTypeName, orderMoneyDetail, payModeEntities);
        }
    }

    @Override
    public void printStatistic(int dateType, DishTypeModel dishTypeModel, ArrayList<DishModel> dishModels) {
        if (mPrintService != null) {
            mPrintService.printStatistic(dateType, dishTypeModel, dishModels);
        }
    }

    @Override
    public void printTypeCollection(String cashierName, String isShift, String payModeName, int dateType, String areaTypeName, ArrayList<DishTypeCollectionItemBean> dishTypeCollectionItemBeen) {
        if (mPrintService != null) {
            mPrintService.printTypeCollection(cashierName, isShift, payModeName, dateType, areaTypeName, dishTypeCollectionItemBeen);
        }
    }

    @Override
    public void printTurnOverOrder(String turnOverTime, String cashierName, int dateType, String areaTypeName, ArrayList<PayModeEntity> payModeEntities, ArrayList<DishTypeCollectionItemBean> dishTypeCollectionItemBeens, Map<String, String> orderMoneyDetail) {
        if (mPrintService != null) {
            mPrintService.printTurnOverOrder(turnOverTime, cashierName, dateType, areaTypeName, payModeEntities, dishTypeCollectionItemBeens, orderMoneyDetail);
        }
    }

    @Override
    public void printTurnOverOrder(TurnoverHistoryEntity turnoverHistoryEntity) {
        if (mPrintService != null) {
            mPrintService.printTurnOverOrder(turnoverHistoryEntity);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTurnoverHistoryPrintAgain(TurnoverHistoryPrintAgainEvent event) {
        if (event != null && event.getTurnoverHistoryEntity() != null && mPrintService != null) {
            mPrintService.printTurnOverOrder(event.getTurnoverHistoryEntity());
        }
    }

    @Override
    public void onClearClick(int type) {
        if (mainMenuHandler != null) {
            mainMenuHandler.sendEmptyMessage(2);
        }
        switch (type) {
            case 0:
                if (wxMsgHandler != null)
                    wxMsgHandler.sendEmptyMessage(0);
                break;
            case 1:
                if (storeMsgHandler != null) {
                    storeMsgHandler.sendEmptyMessage(0);
                }
                break;
            case 2:
                if (systemMsgHandler != null) {
                    systemMsgHandler.sendEmptyMessage(0);
                }
                break;
        }
    }

    @Override
    public void onStoreMessageChange() {
        if (mainMenuHandler != null) {
            mainMenuHandler.sendEmptyMessage(2);
        }
        if (allMessageHandler != null) {
            allMessageHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void setEdittext(EditText et) {
        if (mCashierHandler != null) {
            Message msg = new Message();
            msg.what = -1;
            msg.obj = et;
            mCashierHandler.sendMessage(msg);
        }
    }

    @Override
    public void onTopRightCancle() {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
    }

    @Override
    public void onTopRightConfirm(String orderId, boolean isOpenJoinOrder) {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
        if (mCashierOrderHandler != null)
            mCashierOrderHandler.sendEmptyMessage(0);
        sendDifferBroadcast(2, orderId, isOpenJoinOrder);
    }

    @Override
    public void onChangeVip() {
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(14);
    }

    @Override
    public void onChangeCouponStatus(String orderId, boolean isOpenJoinOrder) {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopLeftHandler != null)
            mCashierTopLeftHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
        if (mCashierOrderHandler != null)
            mCashierOrderHandler.sendEmptyMessage(0);
        sendDifferBroadcast(2, orderId, isOpenJoinOrder);
    }

    @Override
    public void onUseVoucher(String orderId, boolean isOpenJoinOrder) {
        currentPay = -1;
        if (mCashierHandler != null)
            mCashierHandler.sendEmptyMessage(0);
        if (mCashierTopRightHandler != null)
            mCashierTopRightHandler.sendEmptyMessage(0);
        if (mCashierOrderHandler != null)
            mCashierOrderHandler.sendEmptyMessage(0);
        sendDifferBroadcast(2, orderId, isOpenJoinOrder);
    }

    @Override
    public void onChangeTakeoutStatusCount() {
        if (takeoutHistoryHandler != null) {
            takeoutHistoryHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public void refreshStock() {
        if (mainMenuHandler != null) {
            mainMenuHandler.sendEmptyMessage(6);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOpenCashBox(SnackOpenCashBoxEvent event) {
        if (event != null) {
            if (mPrintService != null) {
                mPrintService.openQianXiang();
            }
        }
    }

    @Override
    public void openCashBox() {
        if (mPrintService != null) {
            mPrintService.openQianXiang();
        }
    }

    private LFragmentSelectTables mLFragmentSelectTables;

    private void setSelectTableFragment(String orderId, String tableId, int type, ScheduleEntity scheduleEntity) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans, false);
        if (mLFragmentSelectTables == null) {
            mLFragmentSelectTables = new LFragmentSelectTables();
            Bundle bundle = new Bundle();
            bundle.putString("tableId", tableId);
            bundle.putString("orderId", orderId);
            bundle.putInt("type", type);
            bundle.putParcelable("scheduleEntity", scheduleEntity);
            mLFragmentSelectTables.setArguments(bundle);
            trans.add(R.id.container, mLFragmentSelectTables);
        } else {
            mLFragmentSelectTables.setNewParam(type, scheduleEntity, tableId, orderId);
            trans.show(mLFragmentSelectTables);
        }
        trans.commitAllowingStateLoss();
    }

    @Override
    public void selectTableCancel() {
        setMainFragment(false);
    }

    @Override
    public void selectedTables(int operateType, int type, TableEntity tableEntity) {
        setMainFragment(false);
        switch (operateType) {
            case LFragmentSelectTables.OPERATION_TYPE_CHANGE_TABLE:

                break;
            case LFragmentSelectTables.OPERATION_TYPE_CONFIRM_TABLE:
                if (scheduleCheckHandler != null) {
                    scheduleCheckHandler.sendEmptyMessage(1);
                }
                if (scheduleEffectHandler != null) {
                    scheduleEffectHandler.sendEmptyMessage(0);
                }
                if (mainMenuHandler != null) {
                    mainMenuHandler.sendEmptyMessage(5);
                }
                break;
            case LFragmentSelectTables.OPERATION_TYPE_JOIN_TABLE:

                break;
            case LFragmentSelectTables.OPERATION_TYPE_SELECT_TABLE:
                if (schedulePhoneTimeHandler != null) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("tableEntity", tableEntity);
                    msg.what = 2;
                    msg.setData(bundle);
                    schedulePhoneTimeHandler.sendMessage(msg);
                }
                break;
        }
    }

    @Override
    public void joinTable(String tableId, String orderId) {
        setSelectTableFragment(orderId, tableId, LFragmentSelectTables.OPERATION_TYPE_JOIN_TABLE, null);
    }

    @Override
    public void changeTable(String tableId, String orderId) {
        setSelectTableFragment(orderId, tableId, LFragmentSelectTables.OPERATION_TYPE_CHANGE_TABLE, null);
    }

    @Override
    public void scheduleConfirmTable(ScheduleEntity scheduleEntity) {
        setSelectTableFragment(null, null, LFragmentSelectTables.OPERATION_TYPE_CONFIRM_TABLE, scheduleEntity);
    }

    @Override
    public void scheduleSelectTable() {
        setSelectTableFragment(null, null, LFragmentSelectTables.OPERATION_TYPE_SELECT_TABLE, null);
    }

    @Override
    public void changeOrderMessageCount() {
        if (forHereHandler != null) {
            forHereHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public void refreshTable() {
        if (forHereHandler != null) {
            forHereHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onOrderDishScroll(int dy) {
        sendDifferBroadcast1(4, dy);
    }

    @Override
    public void onCashierOrderScroll(int dy) {
        sendDifferBroadcast1(4, dy);
    }

    private LFragmentSelectTaocan mLFragmentSelectTaocan;

    private void setLFragmentSelectTaocan(OrderDishEntity orderDishEntity, String activityTag, String taocanId, String orderId, String tableId) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        hideFragment(trans, false);
        if (mLFragmentSelectTaocan == null) {
            mLFragmentSelectTaocan = new LFragmentSelectTaocan();
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderDishEntity", orderDishEntity);
            bundle.putString("activity", activityTag);
            bundle.putString("orderId", orderId);
            bundle.putString("taocanId", taocanId);
            bundle.putString("tableId", tableId);
            mLFragmentSelectTaocan.setArguments(bundle);
            trans.add(R.id.container, mLFragmentSelectTaocan);
        } else {
            mLFragmentSelectTaocan.setNewParam(orderDishEntity, activityTag, taocanId, orderId, tableId);
            trans.show(mLFragmentSelectTaocan);
        }
        trans.commitAllowingStateLoss();
    }

    private void hideSelectTaocanFragment(String tableId, String orderId, String activityTag) {
        if (activityTag.equals(ActivityIntentUtil.FRAGMENT_ORDERDISH)) {
            setFragmentOrderDish(tableId, orderId);
        } else if (activityTag.equals(ActivityIntentUtil.FRAGMENT_CASHIER)) {
            setCashierFragment(orderId, tableId, 0);
        }
    }

    @Override
    public void onTaocanCancle(String tableId, String orderId, String activityTag) {
        hideSelectTaocanFragment(tableId, orderId, activityTag);
    }

    @Override
    public void onTaocanDelete(String tableId, String orderId, OrderDishEntity orderDishEntity, String activityTag) {
        if (mDishListHandler != null) {
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putString("dishId", orderDishEntity.getDishId());
            bundle.putInt("type", 1);
            message.setData(bundle);
            mDishListHandler.sendMessage(message);
        }
        if (mOrderHandler != null) {
            Message message = new Message();
            message.what = 5;
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderDishEntity", orderDishEntity);
            message.setData(bundle);
            mOrderHandler.sendMessage(message);
        }
        hideSelectTaocanFragment(tableId, orderId, activityTag);
    }

    @Override
    public void onTaocanChangeConfirm(String tableId, String orderId, OrderDishEntity orderDishEntity, String activityTag) {
        if (mDishListHandler != null) {
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putString("dishId", orderDishEntity.getDishId());
            bundle.putInt("type", 1);
            message.setData(bundle);
            mDishListHandler.sendMessage(message);
        }
        if (mOrderHandler != null) {
            Message message = new Message();
            message.what = 4;
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderDishEntity", orderDishEntity);
            message.setData(bundle);
            mOrderHandler.sendMessage(message);
        }
        hideSelectTaocanFragment(tableId, orderId, activityTag);
    }

    @Override
    public void onTaocanAddConfirm(String tableId, String orderId, OrderDishEntity orderDishEntity, String activityTag) {
        if (mDishListHandler != null) {
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putString("dishId", orderDishEntity.getDishId());
            bundle.putInt("type", 1);
            message.setData(bundle);
            mDishListHandler.sendMessage(message);
        }
        if (mOrderHandler != null) {
            Message message = new Message();
            message.what = 0;
            Bundle bundle = new Bundle();
            bundle.putParcelable("orderDishEntity", orderDishEntity);
            message.setData(bundle);
            mOrderHandler.sendMessage(message);
        }
        hideSelectTaocanFragment(tableId, orderId, activityTag);
    }

    @Override
    public void onArraySortCall(String str) {
        CustomMethod.TtSpeech(tts, str);
    }

    //客显界面
    private void sendDifferBroadcast1(int type, int dy) {
        Intent intent = new Intent(DifferentDisplay.ACTION_INTENT_DIFF);
        intent.putExtra("type", type);
        intent.putExtra("dy", dy);
        sendBroadcast(intent);
    }

    //客显界面
    private void sendDifferBroadcast(int type, String orderId, boolean isOpenJoinOrder) {
        Intent intent = new Intent(DifferentDisplay.ACTION_INTENT_DIFF);
        intent.putExtra("type", type);
        intent.putExtra("orderId", orderId);
        intent.putExtra("isOpenJoinOrder", isOpenJoinOrder);
        sendBroadcast(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOrderDetailChange(SnackOrderDetailChangeEvent event) {
        if (event != null) {
            if (event.getOrderId() != null) {
                sendDifferBroadcast(event.getType(), event.getOrderId(), false);
            } else {
                sendDifferBroadcast(0, null, false);
            }
        }
    }
}
