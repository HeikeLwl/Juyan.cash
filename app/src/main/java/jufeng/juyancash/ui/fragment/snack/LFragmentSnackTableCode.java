package jufeng.juyancash.ui.fragment.snack;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jufeng.juyancash.DBHelper;
import jufeng.juyancash.R;
import jufeng.juyancash.adapter.TableCodeAdapter;
import jufeng.juyancash.bean.TableCodeBean;
import jufeng.juyancash.eventbus.AddTableCodeSuccessEvent;
import jufeng.juyancash.eventbus.OnTableCodeItemClickEvent;
import jufeng.juyancash.eventbus.SnackCancelOrderSuccessEvent;
import jufeng.juyancash.eventbus.SnackChangeTableCodeSuccessEvent;
import jufeng.juyancash.eventbus.TableCodeCallEvent;
import jufeng.juyancash.myinterface.OnRecyclerViewItemClickListener;
import jufeng.juyancash.ui.customview.DividerItemDecoration;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;
import jufeng.juyancash.ui.fragment.BaseFragment;
import jufeng.juyancash.util.CustomMethod;

/**
 * Created by Administrator102 on 2017/3/20.
 */

public class LFragmentSnackTableCode extends BaseFragment {
    @BindView(R.id.recyclerview)
    public RecyclerView mRecyclerView;
    private Unbinder mUnbinder;
    private TableCodeAdapter mAdapter;
    private ArrayList<TableCodeBean> mTableCodeBeanArrayList;

    public static LFragmentSnackTableCode newInstance() {
        LFragmentSnackTableCode fragment = new LFragmentSnackTableCode();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snack_table_code, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initData();
        setAdapter();
        return view;
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null)
            mUnbinder.unbind();
        super.onDestroyView();
    }

    private void initData(){
        mTableCodeBeanArrayList = new ArrayList<>();
        mTableCodeBeanArrayList.addAll(DBHelper.getInstance(getContext()).getAllTableCodeBean());
    }

    private void setAdapter(){
        mAdapter = new TableCodeAdapter(getActivity().getApplicationContext(),mTableCodeBeanArrayList);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnRecyclerViewItemClickListener(mRecyclerView) {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                TableCodeBean tableCodeBean = (TableCodeBean) holder.itemView.getTag();
                boolean isTableCodeUsed = DBHelper.getInstance(getContext()).isTableCodeUsed(tableCodeBean.getTableCode());
                if(isTableCodeUsed){
                    //正在使用的牌号点击时进行呼叫牌号
                    EventBus.getDefault().post(new TableCodeCallEvent(tableCodeBean.getTableCode()));
                }else {
                    EventBus.getDefault().post(new OnTableCodeItemClickEvent(tableCodeBean.getTableCode()));
                }
            }

            @Override
            public void onLongPress(RecyclerView.ViewHolder holder, int position) {
                super.onLongPress(holder, position);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback(){
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                TableCodeBean tableCodeBean = (TableCodeBean) viewHolder.itemView.getTag();
                if(tableCodeBean.getAreaId().equals("自定义")) {
                    final int dragFlags;
                    final int swipeFlags;
                    if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                        swipeFlags = 0;
                    } else {
                        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                        swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    }
                    return makeMovementFlags(dragFlags, swipeFlags);
                }else{
                    return 0;
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                TableCodeBean tableCodeBean = (TableCodeBean) viewHolder.itemView.getTag();
                int position = mTableCodeBeanArrayList.indexOf(tableCodeBean);
                if(tableCodeBean.getAreaId().equals("自定义")) {
                    mTableCodeBeanArrayList.remove(tableCodeBean);
                    mAdapter.deleteItem(tableCodeBean);
                }
                boolean isTableCodeUsed = DBHelper.getInstance(getContext()).isTableCodeUsed(tableCodeBean.getTableCode());
                if(isTableCodeUsed){
                    mTableCodeBeanArrayList.add(position,tableCodeBean);
                    mAdapter.addItem(position,tableCodeBean);
                    CustomMethod.showMessage(getContext(),"该牌号正在使用，不允许删除！");
                }else {
                    showDeleteDialog("删除自定义牌号", "是否要彻底删除自定义牌号【" + tableCodeBean.getTableCode() + "】，确定后该牌号下次登录时将不存在", "取消", "确定", tableCodeBean,position);
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddTableCodeSuccess(AddTableCodeSuccessEvent event){
        if(event != null && event.getTableCode() != null){
            TableCodeBean tableCodeBean = new TableCodeBean("自定义","自定义",1,event.getTableCode());
            mTableCodeBeanArrayList.add(tableCodeBean);
            mAdapter.addItem(tableCodeBean);
        }
    }

    public void showDeleteDialog(String title, String message, String btnCancle, String btnConfirm, final TableCodeBean tableCodeBean, final int position){
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, btnCancle, new DialogInterface.OnClickListener() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                mTableCodeBeanArrayList.add(position,tableCodeBean);
                mAdapter.addItem(position,tableCodeBean);
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, btnConfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                DBHelper.getInstance(getContext()).deleteCustomTableCode(tableCodeBean.getTableCode());
            }
        });
        dialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCancelOrderSuccess(SnackCancelOrderSuccessEvent event){
        if(event != null){
            mAdapter.update();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChangeTableCodeSuccess(SnackChangeTableCodeSuccessEvent event){
        if(event != null){
            mAdapter.update();
        }
    }
}
