package com.cshen.tiyu.activity.lottery.ball.football;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.MainActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.ball.HttpZcMatchListResult;
import com.cshen.tiyu.domain.ball.ZcMatchResultDTO;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.DisplayUtil;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.MyListView;
import com.cshen.tiyu.widget.PeriodsJCZQItemView;
import com.cshen.tiyu.widget.PullToRefreshView;
import com.cshen.tiyu.widget.PeriodsJCZQItemView.OnPeriodJczqClickListener;
import com.cshen.tiyu.widget.PullToRefreshView.OnFooterRefreshListener;


import de.greenrobot.event.EventBus;


public class JCZQPeriodsRefreshActivity extends BaseActivity implements  OnFooterRefreshListener {
	private List<PeriodsJCZQItemView> titleList;
	private List<MyListView> contentList;
	private Context _this;
	private ScrollView sv;
	private PullToRefreshView main_pull_refresh_view;
	private LinearLayout scroll_ll;
	private PeriodsJCZQItemView tvjing;
	private int more = 0;// 第几次更多加载
	private int count = 15;// 每次步数
	int start = 0;
	private LinearLayout.LayoutParams llp;// listView 边距
	private boolean httpTask=false;
	private String httpDataTemp;
	private JczqAdapter currentAdapter;
	private PeriodsJCZQItemView currentTitleItem;
	private MyListView currentContrentItem;
	private List<ZcMatchResultDTO> currentList;

	private PeriodsJCZQItemView currentTitleTemp_for_location;
	private int tempI;


	int[] location = new int[2];
	int[] location2 = new int[2];
	int[] locationTemp = new int[2];


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refresh_jczq_periods);
		_this=this;
		llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		llp.setMargins(DisplayUtil.dip2px(_this,20), 0, DisplayUtil.dip2px(_this,20), 0);

		titleList= new LinkedList<PeriodsJCZQItemView>();
		contentList= new LinkedList<MyListView>();
		initHead();
		initView();
		initFirstData();

	}
	private void initHead() {
		// TODO 自动生成的方法存根

		TextView tvTextView = (TextView) findViewById(R.id.tv_do);

		tvTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent ;
				intent = new Intent(_this, FootballMainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		ImageView backImageView = (ImageView) findViewById(R.id.iv_back);
		backImageView.setVisibility(View.VISIBLE);

		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
//				Intent intent = new Intent(JCZQPeriodsRefreshActivity.this,
//						MainActivity.class);
//				startActivity(intent);
				EventBus.getDefault().post("updatePeriodsInfo");
				finish();
			}
		});

	}

	private void initFirstData() {
		httpTask = true;
		start = more * count;
		ServiceCaiZhongInformation.getInstance().new_getZc_Macth_Result(_this,null,start,count,
				ConstantsBase.JCZQ+"",
				new CallBack<HttpZcMatchListResult>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				httpTask = false;
				PostHttpInfoUtils.doPostFail(_this, errorMessage, "访问失败");
			}
			@Override
			public void onSuccess(HttpZcMatchListResult t) {
				httpTask = false;
				// TODO Auto-generated method stub
				if(t!=null && t.getResult()!=null){
					List<ZcMatchResultDTO> match=t.getResult().get("match");
					if (match!=null && match.size()>0) {
						more++;
						try {
							doDate(match);
							sv.setOnTouchListener(new MyOnTouchListener());

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
		});

	}


	private void initView() {
		main_pull_refresh_view = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		sv = (ScrollView) findViewById(R.id.sv);
		tvjing = (PeriodsJCZQItemView) findViewById(R.id.tvjing);
		scroll_ll=(LinearLayout) findViewById(R.id.scroll_ll);
		main_pull_refresh_view.setOnFooterRefreshListener(this);


	}


	class MyOnTouchListener implements OnTouchListener{
		private int lastY = 0;
		private int touchEventId = -9983761;
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == touchEventId) {
					if (lastY != sv.getScrollY()) {
						// scrollview一直在滚动，会触发
						handler.sendMessageDelayed(
								handler.obtainMessage(touchEventId, sv), 5);
						lastY = sv.getScrollY();
						titleList.get(0).getLocationOnScreen(location);
						tvjing.getLocationOnScreen(location2);
						// 动的到静的位置时，静的显示。动的实际上还是网上滚动，但我们看到的是静止的那个
						if (location[1] <= location2[1]) {
							tvjing.setVisibility(View.VISIBLE);
						} else {
							tvjing.setVisibility(View.GONE);
						}

						for (int i = 0; i < titleList.size(); i++) {
							final int tempI = i;
							final int[] temp = new int[2];

							titleList.get(i).getLocationOnScreen(temp);
							if (temp[1] <= location2[1]) {
								locationTemp = temp;
								currentTitleTemp_for_location=titleList.get(i);
								tvjing.setTitle(titleList.get(i).getTitle());
								tvjing.setPeriodNumber(titleList.get(i).getPeriodNumber());
								tvjing.setOnPeriodJczqClickListener(new OnPeriodJczqClickListener() {

									@Override
									public void onPeriodJczqClick() {
										tvjing.setVisibility(View.GONE);
										sv.post(new Runnable() {
											@Override
											public void run() {
												int[] location = new int[2];
												scroll_ll.getLocationOnScreen(location);
												int moveTemp = 0;
												if ( tempI == 0 ) {
													moveTemp= Math.abs(location[1]-locationTemp[1]) ;
												}else{
													titleList.get(tempI-1).getLocationOnScreen(temp);
													moveTemp = Math.abs(location[1]-temp[1]) ;
												}

												sv.smoothScrollTo(0,moveTemp);

											}
										});


									}
								});

							}

						}

					}
				}
			}
		};

		public boolean onTouch(View v, MotionEvent event) {
			// 必须两个都搞上，不然会有瑕疵。
			// 没有这段，手指按住拖动的时候没有效果
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				handler.sendMessageDelayed(
						handler.obtainMessage(touchEventId, v), 5);
			}
			// 没有这段，手指松开scroll继续滚动的时候，没有效果
			if (event.getAction() == MotionEvent.ACTION_UP) {
				handler.sendMessageDelayed(
						handler.obtainMessage(touchEventId, v), 5);
			}
			return false;
		}
	}



	@Override
	public void onFooterRefresh(PullToRefreshView view) {

		if (httpTask == false) {
			httpTask = true;
			start = more * count;
			ServiceCaiZhongInformation.getInstance().new_getZc_Macth_Result(_this,null,start,count,"17",new CallBack<HttpZcMatchListResult>() {
				@Override
				public void onFailure(ErrorMsg errorMessage) {
					httpTask = false;
					PostHttpInfoUtils.doPostFail(_this, errorMessage, "访问失败");
				}
				@Override
				public void onSuccess(HttpZcMatchListResult t) {
					httpTask = false;
					// TODO Auto-generated method stub
					if(t!=null && t.getResult()!=null){
						List<ZcMatchResultDTO> match=t.getResult().get("match");
						if (match!=null && match.size()>0) {
							more++;
							try {
								doDate(match);
								sv.setOnTouchListener(new MyOnTouchListener());
								main_pull_refresh_view.onFooterRefreshComplete();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}

			});


		}

	}
	private void doDate(List<ZcMatchResultDTO> match) throws Exception {
		String datainfo;
		for (int i = 0; i < match.size(); i++) {
			datainfo = match.get(i).getMatchKey().substring(0, 8);
			if (!datainfo.equals(httpDataTemp)) {
				PeriodsJCZQItemView	demoTitleItem=new PeriodsJCZQItemView(_this);
				httpDataTemp=datainfo;
				Calendar date01=Calendar.getInstance();   
				date01.setTime(DateUtils.strToDateNumber(httpDataTemp));
				StringBuilder tv_day01_text=new StringBuilder("");
				tv_day01_text.append(date01.get(Calendar.MONTH)+1).append("月").append(date01.get(Calendar.DAY_OF_MONTH)).append("日");
				tv_day01_text.append("(").append(Util.getDayForWeek(DateUtils.toDateFormat(date01.getTime()))).append(")");

				demoTitleItem.setTitle(tv_day01_text.toString());
				scroll_ll.addView(demoTitleItem);

				setPeriodNumber(demoTitleItem,datainfo);

				final MyListView demoContrentItem = new MyListView(_this);
				demoContrentItem.setLayoutParams(llp);
				currentAdapter = new JczqAdapter();
				demoContrentItem.setAdapter(currentAdapter);

				currentList = new LinkedList<ZcMatchResultDTO>();
				currentList.add(match.get(i));
				currentAdapter.setDataList(currentList);
				currentAdapter.notifyDataSetChanged();

				scroll_ll.addView(demoContrentItem);

				currentTitleItem = demoTitleItem;
				currentContrentItem = demoContrentItem;
				titleList.add(demoTitleItem);
				contentList.add(demoContrentItem);


				demoTitleItem.setOnPeriodJczqClickListener(new OnPeriodJczqClickListener() {

					@Override
					public void onPeriodJczqClick() {
						int temp=demoContrentItem.getVisibility();
						if (temp == View.VISIBLE) {
							demoContrentItem.setVisibility(View.GONE);
						}
						if (temp == View.GONE) {
							demoContrentItem.setVisibility(View.VISIBLE);
						}
					}
				});




			}else{

				if (currentList == null) {
					currentList = new LinkedList<ZcMatchResultDTO>();
				}
				currentList.add(match.get(i));
				currentAdapter.setDataList(currentList);
				currentAdapter.notifyDataSetChanged();

			}
		}
	}



	private void setPeriodNumber(final PeriodsJCZQItemView demoTitleItem,
			String matchDate) {
		ServiceCaiZhongInformation.getInstance().getZc_Macth_Number(_this, matchDate, new CallBack<String>() {

			@Override
			public void onSuccess(String t) {
				if (!TextUtils.isEmpty(t)) {
					demoTitleItem.setPeriodNumber("共"+t+"场比赛");
				}

			}
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				// TODO Auto-generated method stub

			}
		});

	}


	class JczqAdapter extends BaseAdapter {
		private List<ZcMatchResultDTO> dataList;

		public void setDataList(List<ZcMatchResultDTO> dataList) {
			this.dataList = dataList;
		}

		@Override
		public int getCount() {
			// How many items are in the data set represented by this
			// Adapter.(在此适配器中所代表的数据集中的条目数)
			if (dataList == null) {
				return 0;
			} else {
				return dataList.size();
			}
		}

		@Override
		public ZcMatchResultDTO getItem(int position) {
			// Get the data item associated with the specified position in the
			// data set.(获取数据集中与指定索引对应的数据项)
			if (dataList != null) {
				return dataList.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			// Get the row id associated with the specified position in the
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Get a View that displays the data at the specified position in
			// the data set.
			ZcMatchResultDTO data = null;
			if (dataList != null) {
				data = dataList.get(position);
			}
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(_this, R.layout.jczq_period_item,
						null);
				holder = new ViewHolder();
				holder.tv_gameName=(TextView) convertView.findViewById(R.id.tv_gameName);
				holder.tv_homeName=(TextView) convertView.findViewById(R.id.tv_homeName);
				holder.tv_bifen=(TextView) convertView.findViewById(R.id.tv_bifen);
				holder.tv_guestName=(TextView) convertView.findViewById(R.id.tv_guestName);
				holder.tv_text_rangqiu=(TextView) convertView.findViewById(R.id.tv_text_rangqiu);
				holder.tv_SPF=(TextView) convertView.findViewById(R.id.tv_SPF);
				holder.tv_RQSPF=(TextView) convertView.findViewById(R.id.tv_RQSPF);
				holder.tv_BF=(TextView) convertView.findViewById(R.id.tv_BF);
				holder.tv_JQS=(TextView) convertView.findViewById(R.id.tv_JQS);
				holder.tv_BQC=(TextView) convertView.findViewById(R.id.tv_BQC);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (data != null) {
				StringBuilder tv_gameName_txt= new StringBuilder("");
				if (!TextUtils.isEmpty(data.getGameName())) {
					tv_gameName_txt.append(data.getGameName());
					tv_gameName_txt.append("<br/>");
				}
				if (!TextUtils.isEmpty(data.getMatchKey()) && data.getMatchKey().length()>2) {
					tv_gameName_txt.append( data.getMatchKey().substring(data.getMatchKey().length()-3,data.getMatchKey().length()));
				}
				holder.tv_gameName.setText(Html.fromHtml(tv_gameName_txt.toString()));
				if (data.getHandicap()!=null ) {
					holder.tv_text_rangqiu.setText(Html.fromHtml("让球"+"<font color='#56a81f'>"+"("+data.getHandicap().intValue()+")"+"</font>"));
				}
				if (!TextUtils.isEmpty(data.getHomeTeamName())) {
					holder.tv_homeName.setText(data.getHomeTeamName());
				}
				if (!TextUtils.isEmpty(data.getGuestTeamName())) {
					holder.tv_guestName.setText(data.getGuestTeamName());
				}
				String result=data.getResult();
				String reslutSP=data.getResultSp();
				if (!TextUtils.isEmpty(result) && !TextUtils.isEmpty(reslutSP)) {
					String [] results=result.split("\\|");
					String [] resultSPs=reslutSP.split("\\|");
					if ("0:0".equals(results[2].trim())) {
						holder.tv_bifen.setText(Html.fromHtml("<font color='#3c9ef0'>"+results[2]+"</font>"));
					}else{
						holder.tv_bifen.setText(Html.fromHtml("<font color='#FF3232'>"+results[2]+"</font>"));
					}

					String SPF= getHtmlStr(results[0],resultSPs[0]);
					String JQS= getHtmlStr(results[1],resultSPs[1]);
					String BF= getHtmlStr(results[2],resultSPs[2]);
					String BQC= getHtmlStr(results[3],resultSPs[3]);
					String RQSPF= getHtmlStr(results[4],resultSPs[4]);
					holder.tv_SPF.setText(Html.fromHtml(SPF));
					holder.tv_JQS.setText(Html.fromHtml(JQS));
					holder.tv_BF.setText(Html.fromHtml(BF.replace(":", " : ")));
					holder.tv_BQC.setText(Html.fromHtml(BQC));
					holder.tv_RQSPF.setText(Html.fromHtml(RQSPF));
				}else{
					return null;
				}
			}
			return convertView;
		}


		class ViewHolder {
			public  TextView tv_gameName;
			public  TextView tv_homeName;
			public  TextView tv_bifen;
			public  TextView tv_guestName;
			public  TextView tv_text_rangqiu;
			public  TextView tv_SPF;
			public  TextView tv_RQSPF;
			public  TextView tv_BF;
			public  TextView tv_JQS;
			public  TextView tv_BQC;
		}

	}

	private String getHtmlStr(String string, String string2) {
		if (TextUtils.isEmpty(string) && TextUtils.isEmpty(string2)) {
			return "";
		}
		StringBuilder str=new StringBuilder("<font color='#000000'> <b>");
		if (!TextUtils.isEmpty(string)) {
			str.append(string);
		}
		str.append("</b> </font>");


		if (!TextUtils.isEmpty(string2)) {
			str.append("<br/>");
			str.append("<font size=14>");
			if("null".equals(string2)){
				str.append("0");
			}else{
				str.append(string2);
			}
			str.append("</font>");
		}
		return str.toString();
	}



}
