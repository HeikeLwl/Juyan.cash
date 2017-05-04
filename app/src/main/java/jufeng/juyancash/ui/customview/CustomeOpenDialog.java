package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SpinnerAdapter1;
import jufeng.juyancash.dao.EmployeeEntity;
import jufeng.juyancash.dao.TableEntity;

/**
 * Created by 15157_000 on 2016/6/20 0020.
 */
public class CustomeOpenDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private TextView title;
    private ImageButton ibCancel;
    private EditText etPeopleCount;//就餐人数
//    private EditText etWaiter;//服务员
    private EditText etNote;//备注
    private Button btnOpen;//开台
    private AppCompatSpinner mSpinner;
    private OpenTableListener mOpenTableListener;
    private ArrayList<EmployeeEntity> mEmployeeEntities;

    public CustomeOpenDialog(Context context, TableEntity tableEntity){
        this.mContext = context;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_open_layout,null));
        mAlertDialog.show();
        Window window = mAlertDialog.getWindow();
        initView(window,tableEntity);
        setListener();
        setSpinner();
    }

    private void initView(Window window,TableEntity tableEntity){
        window.setContentView(R.layout.dialog_open_layout);
        title = (TextView) window.findViewById(R.id.title);
        ibCancel = (ImageButton) window.findViewById(R.id.ib_cancle);
        etPeopleCount = (EditText) window.findViewById(R.id.et_people_count);
//        etWaiter = (EditText) window.findViewById(R.id.et_waiter);
        etNote = (EditText) window.findViewById(R.id.et_note);
        btnOpen = (Button) window.findViewById(R.id.btn_open);
        mSpinner = (AppCompatSpinner) window.findViewById(R.id.spinner);
        title.setText(tableEntity.getTableName()+"("+tableEntity.getTableCode()+")");
        etPeopleCount.setText(String.valueOf(tableEntity.getTableSeat()));
    }

    private void setSpinner(){
        mEmployeeEntities = new ArrayList<>();
        mEmployeeEntities.addAll(DBHelper.getInstance(mContext).queryEmployeeData());
        SpinnerAdapter1 adapter = new SpinnerAdapter1(mContext.getApplicationContext(),mEmployeeEntities);
        mSpinner.setAdapter(adapter);
        mSpinner.setTag(null);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinner.setTag(mEmployeeEntities.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinner.setTag(null);
            }
        });
    }

    //获取就餐人数
    public String getMealsNum(){
        return etPeopleCount.getText().toString();
    }

    //获取备注信息
    public String getRemarkValue(){
        return etNote.getText().toString();
    }

    private void setListener(){
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenTableListener.onOpenClick();
            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenTableListener.onCancleClick();
            }
        });
    }

    //设置开台点击事件
    public void setOpenTableListener(OpenTableListener listener){
        mOpenTableListener = listener;
    }

    public interface OpenTableListener{
        void onOpenClick();
        void onCancleClick();
    }

    //关闭对话框
    public void dismiss() {
        mAlertDialog.dismiss();
        mAlertDialog = null;
    }
}
