package jufeng.juyancash.ui.customview.WheeelView_dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import jufeng.juyancash.R;

/**
 * Created by Administrator on 2016/5/21 0021.
 */
public class The_New_WheelView {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private WheelView mWheelView1;
    private WheelView mWheelView2;
    private Button byn_ok;
    private Button btn_no;
    private TextView title_tv_1;
    private View mMenuView;
    private Context context;
    private OkOnClickListener okOnClickListener;
    private RightOnClickListener rightOnClickListener;

    public The_New_WheelView(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.the_dialog, null);
        init();

        builder = new AlertDialog.Builder(context, R.style.CustomDialog2);
        dialog = builder.create();
        dialog.setView(mMenuView);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.mystyle);  //添加动画

    }

    public void cancel() {
        dialog.cancel();
    }

    public void setViewAcion(ArrayList<String> left, ArrayList<String> right, String title1,
                             String title2, OkOnClickListener okOnClickListener) {
        this.okOnClickListener = okOnClickListener;

        if (left != null) {
            mWheelView1.setVisibility(View.VISIBLE);
            mWheelView1.setCyclic(false);
            mWheelView1.setViewAdapter(new ArrayWheelAdapter(context, left));
        }
        if (right != null) {
            mWheelView2.setVisibility(View.VISIBLE);
            mWheelView2.setCyclic(false);
            mWheelView2.setViewAdapter(new ArrayWheelAdapter(context, right));
        }
        if (title2 == null) {
            title_tv_1.setText(title1);
            title_tv_1.setGravity(Gravity.CENTER);
        } else {
            title_tv_1.setText(title1);
        }
        dialog.show();
    }

    public void setRightClick(RightOnClickListener rightClick) {
        this.rightOnClickListener = rightClick;
    }

    private void init() {
        mWheelView1 = (WheelView) mMenuView.findViewById(R.id.wheelView1);
        mWheelView2 = (WheelView) mMenuView.findViewById(R.id.wheelView2);
        btn_no = (Button) mMenuView.findViewById(R.id.btn_no);
        byn_ok = (Button) mMenuView.findViewById(R.id.btn_ok);
        title_tv_1 = (TextView) mMenuView.findViewById(R.id.title_tv_1);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        byn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWheelView2.getVisibility() == View.VISIBLE) {
                    okOnClickListener.click(mWheelView1.getCurrentItem(), mWheelView2.getCurrentItem());
                } else {
                    okOnClickListener.click(mWheelView1.getCurrentItem(), 0);
                }

            }
        });
    }

    public interface OkOnClickListener {
        void click(int leftNum, int rightNum);
    }

    public interface RightOnClickListener {
        void click();
    }
}


