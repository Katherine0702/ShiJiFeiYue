package com.cshen.tiyu.activity.mian4.gendan.niuren;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.LoginActivity;
import com.cshen.tiyu.activity.mian4.gendan.FollowDetilActivity;
import com.cshen.tiyu.activity.mian4.gendan.bangdan.BangDanListAdapter;
import com.cshen.tiyu.activity.mian4.gendan.myarena.MyArenaActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.gendan.NiuRen;
import com.cshen.tiyu.domain.gendan.NiuRenList;
import com.cshen.tiyu.domain.gendan.RenZhengPerson;
import com.cshen.tiyu.domain.gendan.RenZhengPersonList;
import com.cshen.tiyu.net.https.ServiceGenDan;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.RefreshListView;
import com.cshen.tiyu.widget.RefreshListView.OnRefreshListener;

public class RenZhengMainFragment extends Fragment  
implements OnClickListener{
	private View renzhengniuren,wodeleitai;
	private View niurenlisttitle,niurenlist;
	private View person1,person2,person3,person4,person5;
	private ImageView head1,head2,head3,head4,head5;
	private TextView name1,name2,name3,name4,name5;
	private TextView name1_notice,name2_notice,name3_notice,name4_notice,name5_notice;
	private RefreshListView listview;
	private Activity _this;
	private BangDanListAdapter adapter;
	private boolean isGetting = false;
	private String userId,userPwd;
	private ArrayList<RenZhengPerson> renzhenglist;
	private ArrayList<NiuRen> top5;
	private int more = 0;// 第几次更多加载
	private int step = 10;// 每次步数

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gendan_main,container, false);
		_this = this.getActivity();
		initView(view);
		initData();
		return view;
	}
	public void initView(View view){
		/**************牛人************/
		View headListView = View.inflate(_this, R.layout.gendan_mainhead, null);
		listview = (RefreshListView) view.findViewById(R.id.listview);
		listview.addHeaderView(headListView);

		renzhengniuren = headListView.findViewById(R.id.renzhengniuren);
		renzhengniuren.setOnClickListener(this);
		wodeleitai = headListView.findViewById(R.id.wodeleitai);
		wodeleitai.setOnClickListener(this);


		niurenlisttitle =  headListView.findViewById(R.id.niurenlisttitle);
		niurenlist =  headListView.findViewById(R.id.niurenlist);

		person1 = headListView.findViewById(R.id.person1);
		person1.setOnClickListener(this);
		head1 = (ImageView) headListView.findViewById(R.id.head1);
		name1 = (TextView) headListView.findViewById(R.id.name1);
		name1_notice = (TextView) headListView.findViewById(R.id.name1_notice);

		person2 = headListView.findViewById(R.id.person2);
		person2.setOnClickListener(this);
		head2 = (ImageView) headListView.findViewById(R.id.head2);
		name2 = (TextView) headListView.findViewById(R.id.name2);
		name2_notice = (TextView) headListView.findViewById(R.id.name2_notice);

		person3 = headListView.findViewById(R.id.person3);
		person3.setOnClickListener(this);
		head3 = (ImageView) headListView.findViewById(R.id.head3);
		name3 = (TextView) headListView.findViewById(R.id.name3);
		name3_notice = (TextView) headListView.findViewById(R.id.name3_notice);

		person4 = headListView.findViewById(R.id.person4);
		person4.setOnClickListener(this);
		head4 = (ImageView) headListView.findViewById(R.id.head4);
		name4 = (TextView) headListView.findViewById(R.id.name4);
		name4_notice = (TextView) headListView.findViewById(R.id.name4_notice);

		person5 = headListView.findViewById(R.id.person5);
		person5.setOnClickListener(this);
		head5 = (ImageView) headListView.findViewById(R.id.head5);
		name5 = (TextView) headListView.findViewById(R.id.name5);
		name5_notice = (TextView) headListView.findViewById(R.id.name5_notice);

		adapter = new BangDanListAdapter(_this,4);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(renzhenglist!=null&&position<renzhenglist.size()&&position>=0){
					Intent intent = new Intent(_this,FollowDetilActivity.class);
					Bundle bundle = new Bundle();   
					bundle.putSerializable("match", renzhenglist.get(position));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}			
		});
		// 添加监听器
		listview.setOnRefreshListener(new OnRefreshListener() {

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

			private   void  requestDataFromServer(boolean isLoadingMore) {
				// TODO 自动生成的方法存根
				if (isLoadingMore) {
					if(!isGetting){
						isGetting = true;
					}
					// TODO 自动生成的方法存根
					// 加载更多
					more++;
					int start = more * step;
					ServiceGenDan.getInstance().pastRenZhengList(_this,start,step,1,2,0,
							new CallBack<RenZhengPersonList>() {

						@Override
						public void onSuccess(RenZhengPersonList t) {
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
							listview.onRefreshComplete(false);
							isGetting = false;
						}
					});
				} else {
					initData();
				}

			}
		});

		view.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
	}
	public void initData(){
		getTop5();
		if(!isGetting){
			isGetting = true;
			getRenZhengNiuRen();
		}
	}
	public void setDateTop5(){
		if(top5.size()>0&&top5.get(0)!=null){
			xUtilsImageUtils.display(head1,R.mipmap.defaultniu,
					top5.get(0).getUserPic());
			name1.setText(top5.get(0).getUserNameTemp());
			if(top5.get(0).getCurrentCopiedSchemeNumber()>0){
				name1_notice.setText(""+top5.get(0).getCurrentCopiedSchemeNumber());
				name1_notice.setVisibility(View.VISIBLE);
			}else{
				name1_notice.setVisibility(View.INVISIBLE);
			}
			person1.setVisibility(View.VISIBLE);
		}else{
			person1.setVisibility(View.INVISIBLE);
		}
		if(top5.size()>1&&top5.get(1)!=null){	
			xUtilsImageUtils.display(head2,R.mipmap.defaultniu,
					top5.get(1).getUserPic());
			name2.setText(top5.get(1).getUserNameTemp());
			if(top5.get(1).getCurrentCopiedSchemeNumber()>0){
				name2_notice .setText(""+top5.get(1).getCurrentCopiedSchemeNumber());
				name2_notice.setVisibility(View.VISIBLE);
			}else{
				name2_notice.setVisibility(View.INVISIBLE);
			}
			person2.setVisibility(View.VISIBLE);
		}else{
			person2.setVisibility(View.INVISIBLE);
		}
		if(top5.size()>2&&top5.get(2)!=null){
			xUtilsImageUtils.display(head3,R.mipmap.defaultniu,
					top5.get(2).getUserPic());
			name3.setText(top5.get(2).getUserNameTemp());
			if(top5.get(2).getCurrentCopiedSchemeNumber()>0){
				name3_notice .setText(""+top5.get(2).getCurrentCopiedSchemeNumber());
				name3_notice.setVisibility(View.VISIBLE);
			}else{
				name3_notice.setVisibility(View.INVISIBLE);
			}
			person3.setVisibility(View.VISIBLE);
		}else{
			person3.setVisibility(View.INVISIBLE);
		}
		if(top5.size()>3&&top5.get(3)!=null){
			xUtilsImageUtils.display(head4,R.mipmap.defaultniu,
					top5.get(3).getUserPic());
			name4.setText(top5.get(3).getUserNameTemp());
			if(top5.get(3).getCurrentCopiedSchemeNumber()>0){
				name4_notice .setText(""+top5.get(3).getCurrentCopiedSchemeNumber());
				name4_notice.setVisibility(View.VISIBLE);
			}else{
				name4_notice.setVisibility(View.INVISIBLE);
			}
			person4.setVisibility(View.VISIBLE);
		}else{
			person4.setVisibility(View.INVISIBLE);
		}
		if(top5.size()>4&&top5.get(4)!=null){
			xUtilsImageUtils.display(head5,R.mipmap.defaultniu,
					top5.get(4).getUserPic());
			name5.setText(top5.get(4).getUserNameTemp());
			if(top5.get(4).getCurrentCopiedSchemeNumber()>0){
				name5_notice .setText(""+top5.get(4).getCurrentCopiedSchemeNumber());
				name5_notice.setVisibility(View.VISIBLE);
			}else{
				name5_notice.setVisibility(View.INVISIBLE);
			}
			person5.setVisibility(View.VISIBLE);
		}else{
			person5.setVisibility(View.INVISIBLE);
		}
	}
	public void getTop5(){
		if (PreferenceUtil.getBoolean(_this,"hasLogin")) {
			userId = MyDbUtils.getCurrentUser().getUserId();
			userPwd = MyDbUtils.getCurrentUser().getUserPwd();
		}else{
			userId = "";
			userPwd = "";
		}
		ServiceGenDan.getInstance().pastNiuRenList(_this,0,5,"",userId,userPwd,"",
				new CallBack<NiuRenList>() {
			@Override
			public void onSuccess(NiuRenList t) {
				// TODO 自动生成的方法存根
				if(t.getResultList()!=null&& t.getResultList().size()>0){
					top5 = t.getResultList();
					setDateTop5();
					niurenlisttitle.setVisibility(View.VISIBLE);
					niurenlist.setVisibility(View.VISIBLE);
				}else{
					niurenlisttitle.setVisibility(View.GONE);
					niurenlist.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});
	}
	public void getRenZhengNiuRen(){
		more = 0;
		ServiceGenDan.getInstance().pastRenZhengList(_this,0,10,1,2,0,
				new CallBack<RenZhengPersonList>() {
			@Override
			public void onSuccess(RenZhengPersonList t) {
				// TODO 自动生成的方法存根
				dodata(t,true);
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO 自动生成的方法存根
				ToastUtils.showShort(_this, errorMessage.msg);
				if(adapter!=null){
					adapter.notifyDataSetChanged();// 重新刷新数据
				}
				listview.onRefreshComplete(false);
				isGetting = false;
			}
		});
	}

	public synchronized void dodata(RenZhengPersonList t,boolean isRefresh){
		if(isRefresh){
			if(renzhenglist== null){
				renzhenglist = new ArrayList<RenZhengPerson>();
			}else{
				renzhenglist.clear();
			}
			if(t.getResultList()!=null&& t.getResultList().size()>0){
				renzhenglist.addAll(t.getResultList());  
				adapter.setDate(renzhenglist);
			}
		}else{
			ArrayList<RenZhengPerson> newlists = t.getResultList();
			if (newlists.size()==0) {
				ToastUtils.showShort(_this, "已无更多数据");
				more--;
			}
			if(renzhenglist== null){
				renzhenglist = new ArrayList<RenZhengPerson>();
			}
			renzhenglist.addAll(newlists);
		}
		if(adapter!=null){
			adapter.notifyDataSetChanged();// 重新刷新数据
		}
		listview.onRefreshComplete(false);
		isGetting = false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.renzhengniuren:
			Intent intent = new Intent(_this,NiuRenListActivity.class);
			startActivity(intent);
			break;
		case R.id.wodeleitai:
			if (!PreferenceUtil.getBoolean(_this,"hasLogin")) {
				Intent intentLogin = new Intent(_this, LoginActivity.class); 
				intentLogin.putExtra("requestName", "intentLogin");
				startActivity(intentLogin);
			}else{
				Intent intent2 = new Intent(_this,MyArenaActivity.class);
				startActivity(intent2);
			}
			break;
		case R.id.person1:
			if(top5!=null&&top5.size()>0){
				Intent intentD1 = new Intent(_this,NiuRenActivity.class);
				intentD1.putExtra("id",top5.get(0).getId());
				startActivity(intentD1);
			}
			break;
		case R.id.person2:
			if(top5!=null&&top5.size()>1){
				Intent intentD2 = new Intent(_this,NiuRenActivity.class);
				intentD2.putExtra("id",top5.get(1).getId());
				startActivity(intentD2);
			}
			break;
		case R.id.person3:
			if(top5!=null&&top5.size()>2){
				Intent intentD3 = new Intent(_this,NiuRenActivity.class);
				intentD3.putExtra("id",top5.get(2).getId());
				startActivity(intentD3);
			}
			break;
		case R.id.person4:
			if(top5!=null&&top5.size()>3){
				Intent intentD4 = new Intent(_this,NiuRenActivity.class);
				intentD4.putExtra("id",top5.get(3).getId());
				startActivity(intentD4);
			}
			break;
		case R.id.person5:
			if(top5!=null&&top5.size()>4){
				Intent intentD5 = new Intent(_this,NiuRenActivity.class);
				intentD5.putExtra("id",top5.get(4).getId());
				startActivity(intentD5);
			}
			break;

		}
	}
}
