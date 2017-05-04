package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SelectEmployeeAdapter;
import jufeng.juyancash.dao.SendPersonEntity;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/24 0024.
 */
public class CustomeSelectEmployeeDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private RecyclerView mRecyclerView;
    private TextView tvCancel, tvConfirm;
    private EditText etPhone, etName;
    private SelectEmployeeAdapter mAdapter;
    private OnEmployeeSelectedListener mOnPaymentSelectedListener;

    public CustomeSelectEmployeeDialog(Context context,int type) {
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_layout_select_employee);
        initView(window,type);
        setAdapter();
        setListener();
    }

    private void initView(Window window,int type) {
        mRecyclerView = (RecyclerView) window.findViewById(R.id.recyclerview);
        etPhone = (EditText) window.findViewById(R.id.et_phone);
        tvCancel = (TextView) window.findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) window.findViewById(R.id.tv_confirm);
        etName = (EditText) window.findViewById(R.id.et_name);
        tvConfirm.setTag(null);
        if(type == 0){
            tvCancel.setVisibility(TextView.VISIBLE);
        }else{
            tvCancel.setVisibility(TextView.GONE);
        }
    }

    private void setAdapter() {
        mAdapter = new SelectEmployeeAdapter(mContext);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnSelectEmployeeListener(new SelectEmployeeAdapter.OnSelectEmployeeListener() {
            @Override
            public void onSelectEmployee(SendPersonEntity employeeEntity) {
                if (employeeEntity.getSendPersonPhone() != null) {
                    etPhone.setText(employeeEntity.getSendPersonPhone());
                }
                if (employeeEntity.getSendPersonName() != null) {
                    etName.setText(employeeEntity.getSendPersonName());
                }
                tvConfirm.setTag(employeeEntity);
            }
        });
    }

    private void setListener() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPaymentSelectedListener.onUnuseEmployee();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvConfirm.getTag() != null) {
                    mOnPaymentSelectedListener.onUseEmployee((SendPersonEntity) tvConfirm.getTag());
                }else{
                    CustomMethod.showMessage(mContext,"请选择配送员");
                }
            }
        });
    }

    public void dismiss() {
        mAlertDialog.dismiss();
    }

    public void setOnPaymentSelectedListener(OnEmployeeSelectedListener listener) {
        mOnPaymentSelectedListener = listener;
    }

    public interface OnEmployeeSelectedListener {
        void onUseEmployee(SendPersonEntity sendPersonEntity);

        void onUnuseEmployee();
    }
}
