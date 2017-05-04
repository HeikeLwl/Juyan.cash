package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import jufeng.juyancash.R;
import jufeng.juyancash.ui.customview.WheeelView_dialog.ArrayWheelAdapter;
import jufeng.juyancash.ui.customview.WheeelView_dialog.OnWheelChangedListener;
import jufeng.juyancash.ui.customview.WheeelView_dialog.WheelView;

/**
 * Created by Administrator102 on 2016/7/16.
 */
public class CustomDateTimeSelectDialog<T> {
    private WheelView mWheelView1;
    private WheelView mWheelView2;
    private WheelView mWheelView3;
    private WheelView mWheelView4;
    private WheelView mWheelView5;
    private Button byn_ok;
    private Button btn_no;
    private TextView title_tv_1;
    private Window window;
    private Context context;
    private int startYear, endYear;
    private int curYear,curMonth,curDay,curHour,curMin;
    private ArrayList<T> list1;
    private ArrayList<T> list2;
    private ArrayList<T> list3;
    private ArrayList<T> list4;
    private ArrayList<T> list5;
    private OnDateTimeSelectListener mOnDateTimeSelectListener;
    android.app.AlertDialog ad;

    public CustomDateTimeSelectDialog(Context context) {
        this.context = context;
        ad = new android.app.AlertDialog.Builder(context,R.style.dialog).create();
        ad.show();
        // 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        window = ad.getWindow();
        window.setContentView(R.layout.the_dialog);
        window.setWindowAnimations(R.style.mystyle);
        init();
    }

    private void init() {
        mWheelView1 = (WheelView) this.window.findViewById(R.id.wheelView1);
        mWheelView2 = (WheelView) this.window.findViewById(R.id.wheelView2);
        mWheelView3 = (WheelView) this.window.findViewById(R.id.wheelView3);
        mWheelView4 = (WheelView) this.window.findViewById(R.id.wheelView4);
        mWheelView5 = (WheelView) this.window.findViewById(R.id.wheelView5);
        btn_no = (Button) this.window.findViewById(R.id.btn_no);
        byn_ok = (Button) this.window.findViewById(R.id.btn_ok);
        title_tv_1 = (TextView) this.window.findViewById(R.id.title_tv_1);
        mWheelView1.setVisibleItems(3);
        mWheelView2.setVisibleItems(3);
        mWheelView3.setVisibleItems(3);
        mWheelView4.setVisibleItems(3);
        mWheelView5.setVisibleItems(3);
    }

    public void showDialog(int startYear,int endYear,Calendar calendar,OnDateTimeSelectListener listener){
        this.mOnDateTimeSelectListener = listener;
        this.startYear = startYear;
        this.endYear = endYear;
        this.curYear = calendar.get(Calendar.YEAR);
        this.curMonth = calendar.get(Calendar.MONTH);
        this.curDay = calendar.get(Calendar.DAY_OF_MONTH);
        this.curHour = calendar.get(Calendar.HOUR_OF_DAY);
        this.curMin = calendar.get(Calendar.MINUTE);
        list1 = new ArrayList<>();
        for (int i = 0; i < endYear - startYear + 1; i++) {
            list1.add((T) String.valueOf(startYear + i));
        }
        list2 = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            list2.add((T) String.valueOf(i));
        }

        list3 = new ArrayList<>();
        for (int i = 1; i < getMonthDays(curYear,curMonth)+1;i++){
            list3.add((T) String.valueOf(i));
        }
        list4 = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list4.add((T) String.valueOf(i));
        }
        list5 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list5.add((T) String.valueOf(i));
        }
        setAdapter(list1,list2,list3,list4,list5);
        setListener();
    }

    public void setAdapter(ArrayList<T> list1, ArrayList<T> list2, ArrayList<T> list3, ArrayList<T> list4, ArrayList<T> list5) {

        mWheelView1.setCyclic(false);
        mWheelView1.setViewAdapter(new ArrayWheelAdapter(context, list1));

        mWheelView2.setCyclic(false);
        mWheelView2.setViewAdapter(new ArrayWheelAdapter(context, list2));

        mWheelView3.setCyclic(false);
        mWheelView3.setViewAdapter(new ArrayWheelAdapter(context, list3));

        mWheelView4.setCyclic(false);
        mWheelView4.setViewAdapter(new ArrayWheelAdapter(context, list4));

        mWheelView5.setCyclic(false);
        mWheelView5.setViewAdapter(new ArrayWheelAdapter(context, list5));

        //设置默认选项
        mWheelView1.setCurrentItem(curYear-startYear);
        mWheelView2.setCurrentItem(curMonth);
        mWheelView3.setCurrentItem(curDay-1);
        mWheelView4.setCurrentItem(curHour);
        mWheelView5.setCurrentItem(curMin);
    }

    public void setListener() {
        mWheelView1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                curYear = Integer.valueOf((String)list1.get(newValue));
                list3.clear();
                int dayCount = getMonthDays(curYear,curMonth);
                for (int i = 1; i<dayCount+1;i++){
                    list3.add((T) String.valueOf(i));
                }
                mWheelView3.setViewAdapter(new ArrayWheelAdapter(context, list3));
                if(curDay > dayCount){
                    mWheelView3.setCurrentItem(dayCount-1,true);
                }
            }
        });

        mWheelView2.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                curMonth = Integer.valueOf((String)list2.get(newValue))-1;
                list3.clear();
                int dayCount = getMonthDays(curYear,curMonth);
                for (int i = 1; i<dayCount+1;i++){
                    list3.add((T) String.valueOf(i));
                }
                mWheelView3.setViewAdapter(new ArrayWheelAdapter(context, list3));
                if(curDay > dayCount){
                    mWheelView3.setCurrentItem(dayCount-1);
                }
            }
        });

        mWheelView3.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                curDay = Integer.valueOf((String)list3.get(newValue));
            }
        });

        mWheelView4.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                curHour = Integer.valueOf((String)list4.get(newValue));
            }
        });

        mWheelView5.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                curMin = Integer.valueOf((String)list5.get(newValue));
            }
        });

        byn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR,curYear);
                calendar.set(Calendar.MONTH,curMonth);
                calendar.set(Calendar.DAY_OF_MONTH,curDay);
                calendar.set(Calendar.HOUR_OF_DAY,curHour);
                calendar.set(Calendar.MINUTE,curMin);
                mOnDateTimeSelectListener.onDateTimeConfirm(calendar);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDateTimeSelectListener.onDateTimeCancle();
            }
        });
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        if (ad.isShowing())
            ad.dismiss();
    }

    private int getMonthDays(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    public interface OnDateTimeSelectListener{
        void onDateTimeConfirm(Calendar calendar);
        void onDateTimeCancle();
    }
}