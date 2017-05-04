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
import jufeng.juyancash.adapter.StoreMessageAdapter;
import jufeng.juyancash.dao.StoreMessageEntity;
import jufeng.juyancash.myinterface.MainFragmentListener;
import jufeng.juyancash.ui.activity.MainActivity;
import jufeng.juyancash.ui.customview.WrapContentLinearLayoutManager;

public class StoreNofityFragment extends BaseFragment {
	private RecyclerView mRecyclerView;
	private StoreMessageAdapter adapter;
	private MainFragmentListener mOnStoreMessageChangeListener;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					adapter.updateData();
					break;
				case 1:
					StoreMessageEntity storeMessageEntity = (StoreMessageEntity) msg.obj;
					if(storeMessageEntity != null){
						adapter.addItem(storeMessageEntity);
					}
					break;
			}
		}
	};

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		MainActivity mainActivity = (MainActivity) context;
		mainActivity.setStoreMsgHandler(mHandler);
		try{
			mOnStoreMessageChangeListener = (MainFragmentListener) context;
		}catch (ClassCastException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View mView = inflater.inflate(R.layout.message_tab_store, container,false);
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
		mRecyclerView.setHasFixedSize(true);
	}

	private void setAdapter(){
		adapter = new StoreMessageAdapter(getActivity().getApplicationContext());
		mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
		mRecyclerView.setAdapter(adapter);
		adapter.setStoreMessageItemClickListener(new StoreMessageAdapter.OnStoreMessageItemClickListener() {
			@Override
			public void onStoreMessageItemClick() {
				mOnStoreMessageChangeListener.onStoreMessageChange();
			}
		});
	}
}
