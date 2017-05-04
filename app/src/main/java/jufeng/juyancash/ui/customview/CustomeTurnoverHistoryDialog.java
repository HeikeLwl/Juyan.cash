package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.DialogTurnoverHistoryAdapter;
import jufeng.juyancash.dao.TurnoverHistoryEntity;
import jufeng.juyancash.eventbus.TurnoverHistoryCloseDialogEvent;
import jufeng.juyancash.eventbus.TurnoverHistoryPrintAgainEvent;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeTurnoverHistoryDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private DialogTurnoverHistoryAdapter adapter;
    private ImageButton btnClose;

    public CustomeTurnoverHistoryDialog(Context context) {
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_turnover_history);
        initView(window);
        setAdapter();
        setListener();
    }

    private void initView(Window window) {
        mRecyclerView = (RecyclerView) window.findViewById(R.id.recyclerview);
        btnClose = (ImageButton) window.findViewById(R.id.btn_close);
    }

    private void setAdapter() {
        adapter = new DialogTurnoverHistoryAdapter(mContext);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(adapter);
    }

    private void setListener() {
        adapter.setOnDialogScheduleHistoryClickListener(new DialogTurnoverHistoryAdapter.OnDialogTurnoverHistoryClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onDialogTurnoverHistoryClick(TurnoverHistoryEntity turnoverHistoryEntity) {
                EventBus.getDefault().post(new TurnoverHistoryPrintAgainEvent(turnoverHistoryEntity));
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new TurnoverHistoryCloseDialogEvent());
            }
        });
    }

    public void dismiss() {
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
    }
}
