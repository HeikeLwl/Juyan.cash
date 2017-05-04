package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.dao.TableEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/27 0027.
 */
public class LFragmentSchedulePhone extends BaseFragment {
    private TextView tvTableName;
    private TextView tvScheduleTime;
    private EditText etScheduleCount;
    private EditText etName;
    private EditText etPhone;
    private TextView tvMark;
    private Button btnCommit;
    private TableEntity mTableEntity;
    private MainFragmentListener mOnScheduleSuccess;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //设置日期
                    String date = msg.getData().getString("date");
                    if (date != null) {
                        tvScheduleTime.setText(date + " " + CustomMethod.parseTime(System.currentTimeMillis(), "HH:mm"));
                    }
                    break;
                case 1:
                    //设置时间
                    String time = msg.getData().getString("time");
                    if (!tvScheduleTime.getText().toString().isEmpty() && time != null) {
                        tvScheduleTime.setText(CustomMethod.parseTime(CustomMethod.parseTime(tvScheduleTime.getText().toString(), "yyyy-MM-dd HH:mm"), "yyyy-MM-dd") + " " + time);
                    }
                    break;
                case 2://选择桌位
                    mTableEntity = (TableEntity) msg.getData().getParcelable("tableEntity");
                    if (mTableEntity != null) {
                        tvTableName.setText(mTableEntity.getTableName());
                    }
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setSchedulePhoneTimeHandler(mHandler);
        try {
            mOnScheduleSuccess = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_schedule_phone, container, false);
        initView(mView);
        initData();
        setListener();
        return mView;
    }

    public void setNewParam(Bundle bundle) {
        if (bundle != null) {
            mTableEntity = bundle.getParcelable("tableEntity");
        }
        initData();
    }

    private void initView(View view) {
        tvTableName = (TextView) view.findViewById(R.id.tv_table_name);
        tvScheduleTime = (TextView) view.findViewById(R.id.tv_schedule_time);
        etScheduleCount = (EditText) view.findViewById(R.id.et_schedule_count);
        etName = (EditText) view.findViewById(R.id.et_name);
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        tvMark = (TextView) view.findViewById(R.id.tv_mark);
        btnCommit = (Button) view.findViewById(R.id.btn_commit);
        if (getArguments() != null) {
            mTableEntity = getArguments().getParcelable("tableEntity");
        }
    }

    private void initData() {
        resetView();
        if (mTableEntity != null) {
            tvTableName.setText(mTableEntity.getTableName());
            etScheduleCount.setText(String.valueOf(mTableEntity.getTableSeat()));
        } else {
            tvTableName.setText("");
            etScheduleCount.setText("");
        }
    }

    private void setListener() {
        tvScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvScheduleTime.getText().toString().length() > 0) {
                    Date date = new Date();
                    date.setTime(CustomMethod.parseTime(tvScheduleTime.getText().toString(), "yyyy-MM-dd HH:mm"));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    mOnScheduleSuccess.onSelectTime(calendar);
                } else {
                    mOnScheduleSuccess.onSelectTime(Calendar.getInstance());
                }
            }
        });

        tvTableName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnScheduleSuccess.scheduleSelectTable();
            }
        });

        tvMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomMethod.addNote(getContext(), tvMark);
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPhone.getText().toString().isEmpty() && !etScheduleCount.getText().toString().isEmpty() && !tvScheduleTime.getText().toString().isEmpty() && !tvTableName.getText().toString().isEmpty()) {
                    String phoneNumber = etPhone.getText().toString();
                    long mealsTime = CustomMethod.parseTime(tvScheduleTime.getText().toString(), "yyyy-MM-dd HH:mm");
                    int seatCount = Integer.valueOf(etScheduleCount.getText().toString());
                    String name = etName.getText().toString();
                    String mark = tvMark.getText().toString();
                    String orderId = DBHelper.getInstance(getActivity().getApplicationContext()).insertOneSchedule(getActivity().getApplicationContext(), mTableEntity, name, phoneNumber, mealsTime, seatCount, 0, mark);
                    mOnScheduleSuccess.onScheduleSuccess();
                    resetView();
                    String data = "{\"mobile\":\"" + phoneNumber + "\",\"eatTime\":\"" + CustomMethod.parseTime(mealsTime, "yyyy年MM月dd日 HH:mm") + "\",\"op\":\"" + 0 + "\"}";
                    DBHelper.getInstance(getContext().getApplicationContext()).insertUploadData(orderId, data, 18);
                } else {
                    CustomMethod.showMessage(getContext(), "请填写完整预定信息");
                }
            }
        });
    }

    private void resetView() {
        etPhone.setText("");
        tvMark.setText("");
        etName.setText("");
        etScheduleCount.setText("");
        tvScheduleTime.setText("");
        tvTableName.setText("");
    }
}
