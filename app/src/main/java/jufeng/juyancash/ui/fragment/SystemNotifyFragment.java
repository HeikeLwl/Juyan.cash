package jufeng.juyancash.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jufeng.juyancash.R;
import jufeng.juyancash.adapter.SystemMessageAdapter;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;

public class SystemNotifyFragment extends BaseFragment {
	private RecyclerView mRecyclerView;
	private SystemMessageAdapter adapter;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter.updateData();
		}
	};

	@Override
	public void onDestroyView() {

		super.onDestroyView();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		MainActivity mainActivity = (MainActivity) context;
		mainActivity.setSystemMsgHandler(mHandler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View mView = inflater.inflate(R.layout.message_tab_system, container,false);
		initView(mView);
		setAdapter();
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.updateData();
	}

	private void initView(View view){
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
	}

	private void setAdapter(){
		adapter = new SystemMessageAdapter(getActivity().getApplicationContext());
		mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
		mRecyclerView.setAdapter(adapter);
	}
}
