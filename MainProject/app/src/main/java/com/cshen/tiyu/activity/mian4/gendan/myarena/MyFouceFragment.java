package com.cshen.tiyu.activity.mian4.gendan.myarena;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.gendan.RenZhengUtils;
import com.cshen.tiyu.activity.mian4.gendan.niuren.NiuRenActivity;
import com.cshen.tiyu.activity.mian4.gendan.niuren.NiuRenListActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.NiuRen;
import com.cshen.tiyu.domain.gendan.NiuRenList;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;


public class MyFouceFragment extends Fragment {
	private Activity _this;
	private View nodata,guanzhu;
	private RefreshListView refreshListView;

	private FouceListAdapter myListAdapter = null;
	private ArrayList<NiuRen> orderLists;

	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private String userId,userPwd;
	private ServiceGenDan gendanService ;
	private boolean isGetting = false;
	Bundle savedInstanceState;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;
		View view = inflater.inflate(R.layout.myfouce, container,false);
		_this = this.getActivity();
		initView(view);// 初使化控件
		initdata(); // 获取数据
		return view;
	}
	private void initView(View view) {
		nodata = view.findViewById(R.id.nodata);
		guanzhu  = view.findViewById(R.id.guanzhu);
		guanzhu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(_this,NiuRenListActivity.class);
				startActivity(intent);
				_this.finish();
			}
		});
		refreshListView = (RefreshListView) view.findViewById(R.id.rl_listview);
		myListAdapter = new FouceListAdapter();
		refreshListView.setAdapter(myListAdapter);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				if(position<orderLists.size()&&position>=0){
					Intent intent = new Intent(_this,NiuRenActivity.class);
					intent.putExtra("id", orderLists.get(position).getId());
					startActivity(intent);
				}
			}
		});
		// 添加监听器
		refreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 自动生成的方法存根
				requestDataFromServer(false); // 冲服务器中获取刷新的数据
			}

			@Override
			public void onLoadMore() {
				// TODO 自动生成的方法存根
				requestDataFromServer(true);
			}

			private void requestDataFromServer(boolean isLoadingMore) {
				// TODO 自动生成的方法存根
				if (isLoadingMore) {
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					gendanService.MyFouces(_this,start,step,userId,userPwd,new CallBack<NiuRenList>() {

						@Override
						public void onSuccess(NiuRenList t) {
							// TODO 自动生成的方法存根
							dodata(t,false);
						}

						@Override
						public void onFailure(ErrorMsg errorMessage) {
							// TODO 自动生成的方法存根
							more--;
							ToastUtils.showShort(_this,errorMessage.msg);
							if(myListAdapter!=null){
								myListAdapter.notifyDataSetChanged();// 重新刷新数据
							}
							refreshListView.onRefreshComplete(false);
						}
					});
				} else {
					initAllOrdersForList();
				}

			}
		});
	}

	void initdata() {
		if(!isGetting){
			isGetting = true;
			userId = MyDbUtils.getCurrentUser().getUserId();
			userPwd = MyDbUtils.getCurrentUser().getUserPwd();
			gendanService = ServiceGenDan.getInstance();
			initAllOrdersForList();
		}
	}

	private void initAllOrdersForList() {
		more = 0;
		gendanService.MyFouces(_this,more,step,userId,userPwd, 
				new CallBack<NiuRenList>() {

			@Override
			public void onSuccess(NiuRenList t) {
				// TODO 自动生成的方法存根	
				dodata(t,true);
				isGetting = false;
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this,errorMessage.msg+"");
				if(myListAdapter!=null){
					myListAdapter.notifyDataSetChanged();// 重新刷新数据
				}
				refreshListView.onRefreshComplete(false);
				isGetting = false;
			}
		});
	}
	public synchronized void dodata(NiuRenList t,boolean isRefresh){
		if(isRefresh){
			if(orderLists== null){
				orderLists = new ArrayList<NiuRen>();
			}else{
				orderLists.clear();
			}
			orderLists.addAll(t.getResultList());               
			if (orderLists.size()==0) {
				nodata.setVisibility(View.VISIBLE);
				refreshListView.setVisibility(View.GONE);
			}else{
				nodata.setVisibility(View.GONE);
				refreshListView.setVisibility(View.VISIBLE);
			}
			myListAdapter.setDate(orderLists);
		}else{
			ArrayList<NiuRen> newlists = t.getResultList();// 加载数据结束
			if (newlists.size()==0) {
				ToastUtils.showShort(_this, "已无更多数据");
				more--;
			}
			if(orderLists== null){
				orderLists = new ArrayList<NiuRen>();
			}
			orderLists.addAll(newlists);

		}
		if(myListAdapter!=null){
			myListAdapter.notifyDataSetChanged();// 重新刷新数据
		}
		refreshListView.onRefreshComplete(false);
	}
	class FouceListAdapter extends BaseAdapter{
		private ArrayList<NiuRen> orderLists;
		public void setDate(ArrayList<NiuRen> orderListsFrom){
			this.orderLists = orderListsFrom;
		}
		@Override
		public int getCount() {
			if(orderLists == null){
				return 0; 
			}else{
				return orderLists.size();
			}
		}
		@Override
		public NiuRen getItem(int position) {
			if(orderLists == null){
				return null; 
			}else{
				return orderLists.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NiuRen niuren = orderLists.get(position);

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(_this, R.layout.myfouce_item,null);
				holder.head = (ImageView) convertView.findViewById(R.id.head);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.v = (ImageView) convertView.findViewById(R.id.v);
				holder.tuijiannum = (TextView) convertView.findViewById(R.id.tuijiannum);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			xUtilsImageUtils.display(holder.head,R.mipmap.defaultniu,
					niuren.getUserPic());
			holder.name.setText(niuren.getUserNameTemp());
			String level = niuren.getUserLevelNew();
			if(TextUtils.isEmpty(level)){
				holder.v.setVisibility(View.INVISIBLE);
			}else{
				if(RenZhengUtils.getRenZhengUtils().getV(level)!=0){
					holder.v.setImageResource(RenZhengUtils.getRenZhengUtils().getV(level));
					holder.v.setVisibility(View.VISIBLE);
				}else{
					holder.v.setVisibility(View.INVISIBLE);
				}
			}
			if(niuren.getCurrentCopiedSchemeNumber()>0){
				holder.tuijiannum.setText(niuren.getCurrentCopiedSchemeNumber()+"");;
				holder.tuijiannum.setVisibility(View.VISIBLE);
			}else{
				holder.tuijiannum.setVisibility(View.INVISIBLE);
			}
			return convertView;

		}
		class ViewHolder {
			public ImageView head;
			public TextView name;
			public ImageView v;
			public TextView tuijiannum;

		}
	}
}
