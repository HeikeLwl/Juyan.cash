package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.OrderStatisticLeftAdapter;
import jufeng.juyancash.bean.DishTypeModel;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by 15157_000 on 2016/6/16 0016.
 */
public class LFragmentOrderLeftStatistic extends BaseFragment {
    private TextView tvName;
    private Button btnPrint;
    private RecyclerView mRecyclerView;
    private OrderStatisticLeftAdapter adapter;
    private int shift;
    private int date;
    private MainFragmentListener mMainFragmentListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    DishTypeModel dishTypeModel = msg.getData().getParcelable("dishTypeModel");
                    shift = msg.getData().getInt("shift", -1);
                    date = msg.getData().getInt("date", -1);
                    if (dishTypeModel != null) {
                        tvName.setText(dishTypeModel.getDishTypeModelTypeName());
                    } else {
                        tvName.setText("");
                    }
                    setAdapter(dishTypeModel);
                    break;
                case 1:
                    tvName.setText("");
                    setAdapter(null);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.setOrderLeftStatisticHandler(mHandler);
        try {
            mMainFragmentListener = (MainFragmentListener) context;
        } catch (Exception e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_left_statistic, container, false);
        initView(mView);
        setAdapter(null);
        setListener();
        return mView;
    }

    private void initView(View view) {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        btnPrint = (Button) view.findViewById(R.id.btn_print);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        shift = getArguments().getInt("shift", -1);
        date = getArguments().getInt("date", -1);
    }

    private void setAdapter(DishTypeModel dishTypeModel) {
        if (adapter == null) {
            adapter = new OrderStatisticLeftAdapter(getActivity().getApplicationContext(), dishTypeModel, shift, date);
            mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(dishTypeModel, shift, date);
        }
    }

    private void setListener() {
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getDishTypeModel() == null) {
                    CustomMethod.showMessage(getContext(), "请选择需要打印销售统计单的菜品分类");
                } else {
                    mMainFragmentListener.printStatistic(date, adapter.getDishTypeModel(), adapter.getDishModels());
                }
            }
        });
    }
}
