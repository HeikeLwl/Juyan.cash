package jufeng.juyancash.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import jufeng.juyancash.ui.activity.MainActivity;

/**
 * Created by Administrator102 on 2017/3/30.
 */

public class ServerStatusReceiver extends BroadcastReceiver {

    private static final String ACTION = "com.yanzhenjie.andserver.receiver";

    private static final String CMD_KEY = "CMD_KEY";

    private static final int CMD_VALUE_START = 1;
    private static final int CMD_VALUE_STARTED = 2;
    private static final int CMD_VALUE_STOP = 3;

    /**
     * Notify serverStart.
     *
     * @param context context.
     */
    public static void serverStart(Context context) {
        sendBroadcast(context, CMD_VALUE_START);
    }

    /**
     * Notify serverHasStarted.
     *
     * @param context context.
     */
    public static void serverHasStarted(Context context) {
        sendBroadcast(context, CMD_VALUE_STARTED);
    }

    /**
     * Notify serverStop.
     *
     * @param context context.
     */
    public static void serverStop(Context context) {
        sendBroadcast(context, CMD_VALUE_STOP);
    }

    private static void sendBroadcast(Context context, int cmd) {
        Intent broadcast = new Intent(ACTION);
        broadcast.putExtra(CMD_KEY, cmd);
        context.sendBroadcast(broadcast);
    }

    private MainActivity mActivity;

    public ServerStatusReceiver(MainActivity mMainActivity) {
        this.mActivity = mMainActivity;
    }

    /**
     * Register broadcast.
     */
    public void register() {
        IntentFilter filter = new IntentFilter(ACTION);
        mActivity.registerReceiver(this, filter);
    }

    /**
     * UnRegister broadcast.
     */
    public void unRegister() {
        mActivity.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION.equals(action)) {
            int cmd = intent.getIntExtra(CMD_KEY, 0);
            switch (cmd) {
                case CMD_VALUE_START: {
                    Toast.makeText(mActivity, "服务器开启成功", Toast.LENGTH_SHORT).show();
                    break;
                }
                case CMD_VALUE_STARTED: {
                    Toast.makeText(mActivity, "服务器已成功开启", Toast.LENGTH_SHORT).show();
                    break;
                }
                case CMD_VALUE_STOP: {
                    Toast.makeText(mActivity, "服务器关闭", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

}