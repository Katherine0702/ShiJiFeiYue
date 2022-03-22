package com.cshen.tiyu.activity.mian4.gendan.niuren;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.gendan.RenZhengUtils;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.NiuRen;
import com.cshen.tiyu.domain.gendan.NiuRenList;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;


public class NiuRenListActivity extends BaseActivity{
	private TextView tv_head_title;
	private View nodata;
	private RefreshListView refreshListView;
	private NiuRenListActivity _this;
	private RenZhengPeopleAdapter adapter;
	private ArrayList<NiuRen> renzhenglist;
	private String userId = "",userPwd = "";
	private boolean isGetting = false;
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数
	private String keyword = "";
	private boolean isSearch;
	String gendanniuren = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		gendanniuren = getIntent().getStringExtra("from");
		setContentView(R.layout.niurenlist);
		initView(); 
		initdata();
	}
	public void initView(){
		tv_head_title = (TextView) findViewById(R.id.tv_head_title);
		if("bangdan".equals(gendanniuren)){
			tv_head_title.setText("方案发起人");
		}else{
			tv_head_title.setText("认证牛人");
		}
		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_this.finish();
			}
		});
		nodata = findViewById(R.id.nodata);
		refreshListView = (RefreshListView) findViewById(R.id.listview);
		adapter = new RenZhengPeopleAdapter();
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(renzhenglist!=null&&position<renzhenglist.size()&&position>=0){
					Intent intent = new Intent(_this,NiuRenActivity.class);
					intent.putExtra("id", renzhenglist.get(position).getId());
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

			private   void requestDataFromServer(boolean isLoadingMore) {
				// TODO 自动生成的方法存根
				if(!isGetting){
					isGetting = true;
				}

				if (isLoadingMore) {
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					ServiceGenDan.getInstance().pastNiuRenList(_this,start,step,keyword,userId,userPwd,gendanniuren,new CallBack<NiuRenList>() {

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
							if(adapter!=null){
								adapter.notifyDataSetChanged();// 重新刷新数据
							}
							refreshListView.onRefreshComplete(false);
							isGetting = false;
						}
					});
				} else {
					initAllOrdersForList();
				}
			}
		});
	}
	void initdata() {
		keyword = getIntent().getStringExtra("keyword");
		if(keyword == null){
			keyword = "";
		}
		isSearch = getIntent().getBooleanExtra("isSearch", false);
		if (PreferenceUtil.getBoolean(_this,"hasLogin")) {
			userId = MyDbUtils.getCurrentUser().getUserId();
			userPwd = MyDbUtils.getCurrentUser().getUserPwd();
		}else{
			userId = "";
			userPwd = "";
		}
		if(!isGetting){
			isGetting = true;
			initAllOrdersForList();
		}
	}
	public void initAllOrdersForList(){
		more = 0;

		ServiceGenDan.getInstance().pastNiuRenList(_this,more,step,keyword,userId,userPwd,gendanniuren,
				new CallBack<NiuRenList>() {
			@Override
			public void onSuccess(NiuRenList t) {
				// TODO 自动生成的方法存根
				dodata(t,true);
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
				isGetting = false;
			}
		});
	}
	public synchronized void dodata(NiuRenList t,boolean isRefresh){
		if(isRefresh){
			if(renzhenglist== null){
				renzhenglist = new ArrayList<NiuRen>();
			}else{
				renzhenglist.clear();
			}
			renzhenglist.addAll(t.getResultList());               
			if (renzhenglist.size()==0) {
				nodata.setVisibility(View.VISIBLE);
				refreshListView.setVisibility(View.GONE);
			}else{
				nodata.setVisibility(View.GONE);
				refreshListView.setVisibility(View.VISIBLE);
			}
			adapter.setData(renzhenglist);
		}else{
			ArrayList<NiuRen> newlists = t.getResultList();// 加载数据结束
			if (newlists.size()==0) {
				ToastUtils.showShort(_this, "已无更多数据");
				more--;
			}
			if(renzhenglist== null){
				renzhenglist = new ArrayList<NiuRen>();
			}
			renzhenglist.addAll(newlists);
		}
		if(adapter!=null){
			adapter.notifyDataSetChanged();// 重新刷新数据
		}
		refreshListView.onRefreshComplete(false);
		isGetting = false;
	}
	class RenZhengPeopleAdapter extends BaseAdapter {
		ArrayList<NiuRen> niurenList;
		public void setData(ArrayList<NiuRen> niurenlist) {
			niurenList = niurenlist;
		}
		@Override
		public int getCount() {
			if(niurenList == null){
				return 0;
			}else{
				return niurenList.size();
			}
		}

		@Override
		public NiuRen getItem(int position) {
			if(niurenList == null){
				return new NiuRen();
			}else{
				return (NiuRen)niurenList.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			NiuRen niuren = getItem(position);
			if (convertView == null) {  
				holder = new ViewHolder();  
				convertView = View.inflate(_this, R.layout.niuren_item,null);
				holder.number_tx = (TextView) convertView.findViewById(R.id.number_tx);
				holder.number_im = (ImageView) convertView.findViewById(R.id.number_im);
				holder.head  = (ImageView) convertView.findViewById(R.id.head);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.v = (ImageView) convertView.findViewById(R.id.v);

				holder.mingzhonglv = (TextView) convertView.findViewById(R.id.mingzhonglv);
				holder.yinglilv = (TextView) convertView.findViewById(R.id.yinglilv);
				holder.tuijiannum = (TextView) convertView.findViewById(R.id.tuijiannum);
				convertView.setTag(holder);
			} else {  
				holder = (ViewHolder)convertView.getTag();  
			} 
			if(position == 0){
				if(isSearch){
					holder.number_tx.setText((position+1)+"");
					holder.number_im.setVisibility(View.INVISIBLE);
					holder.number_tx.setVisibility(View.VISIBLE);
				}else{
					holder.number_im.setImageResource(R.mipmap.top1);
					holder.number_tx.setVisibility(View.INVISIBLE);
					holder.number_im.setVisibility(View.VISIBLE);
				}
			}else if(position == 1){
				if(isSearch){
					holder.number_tx.setText((position+1)+"");
					holder.number_im.setVisibility(View.INVISIBLE);
					holder.number_tx.setVisibility(View.VISIBLE);
				}else{
					holder.number_im.setImageResource(R.mipmap.top2);
					holder.number_tx.setVisibility(View.INVISIBLE);
					holder.number_im.setVisibility(View.VISIBLE);
				}
			}else if(position == 2){
				if(isSearch){
					holder.number_tx.setText((position+1)+"");
					holder.number_im.setVisibility(View.INVISIBLE);
					holder.number_tx.setVisibility(View.VISIBLE);
				}else{
					holder.number_im.setImageResource(R.mipmap.top3);
					holder.number_tx.setVisibility(View.INVISIBLE);
					holder.number_im.setVisibility(View.VISIBLE);
				}
			}else{
				holder.number_tx.setText((position+1)+"");
				holder.number_im.setVisibility(View.INVISIBLE);
				holder.number_tx.setVisibility(View.VISIBLE);
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
			if(TextUtils.isEmpty(niuren.getHitRate())
					||"0".equals(niuren.getHitRate())){
				holder.mingzhonglv.setText("100%");
			}else{
				holder.mingzhonglv.setText(niuren.getHitRate());
			}
			if(TextUtils.isEmpty(niuren.getEarningsRate())
					||"0".equals(niuren.getEarningsRate())){
				holder.yinglilv.setText("100%");
			}else{
				holder.yinglilv.setText(niuren.getEarningsRate());
			}


			if(niuren.getCurrentCopiedSchemeNumber()>0){
				holder.tuijiannum.setText(niuren.getCurrentCopiedSchemeNumber()+"");;
				holder.tuijiannum.setVisibility(View.VISIBLE);
			}else{
				holder.tuijiannum.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}
	}
	class ViewHolder {
		public TextView number_tx;
		public ImageView number_im,head;
		public TextView name;
		public ImageView v;

		public TextView mingzhonglv;
		public TextView yinglilv;
		public TextView tuijiannum;
	}
}
