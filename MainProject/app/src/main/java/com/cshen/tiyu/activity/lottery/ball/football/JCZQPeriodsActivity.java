package com.cshen.tiyu.activity.lottery.ball.football;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
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
import com.cshen.tiyu.activity.lottery.cai115.Main115Activity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.ball.HttpZcMatchListResult;
import com.cshen.tiyu.domain.ball.ZcMatchResultDTO;
import com.cshen.tiyu.net.https.ServiceCaiZhongInformation;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.DateUtils;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.MyListView;


public class JCZQPeriodsActivity extends BaseActivity {
    private ScrollView sv;
    private LinearLayout tvjing;
    int[] location = new int[2];
    int[] location2 = new int[2];
    
    
	private JCZQPeriodsActivity _this;
	private TextView tv_day01,tv_day02,tv_day03,tv_day04;
	private ImageView img_day01,img_day02,img_day03,img_day04;
	private MyListView lv_day01,lv_day02,lv_day03,lv_day04;
	private LinearLayout ll_day01,ll_day02,ll_day03,ll_day04;
	private Date currentDate;
	private Calendar date01,date02,date03,date04;
	private boolean lv_show01=true;
	private boolean lv_show02=true;
	private boolean lv_show03=true;
	private boolean lv_show04=true;
	private JczqAdapter adapter01, adapter02, adapter03,adapter04;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_periods_jczq02);
		_this = this;
		currentDate=new Date();
		date01=Calendar.getInstance();   
		date01.setTime(new Date());
		
		date02=Calendar.getInstance();   
		date02.setTime(new Date()); 
		date02.set(Calendar.DAY_OF_MONTH,date02.get(Calendar.DAY_OF_MONTH)-1);
		
		date03=Calendar.getInstance();   
		date03.setTime(new Date()); 
		date03.set(Calendar.DAY_OF_MONTH,date03.get(Calendar.DAY_OF_MONTH)-2);
		
		date04=Calendar.getInstance();   
		date04.setTime(new Date()); 
		date04.set(Calendar.DAY_OF_MONTH,date04.get(Calendar.DAY_OF_MONTH)-3);
		changHead();
		initView();
		httpDay01();
		
		httpDay03();
		
	}

	private void httpDay01() {
		String matchDate= DateUtils.getDateforint(date01.getTime());
		ServiceCaiZhongInformation.getInstance().getZc_Macth_Result(_this,matchDate,"17",new CallBack<HttpZcMatchListResult>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(_this, errorMessage, "访问失败");
			}
			@Override
			public void onSuccess(HttpZcMatchListResult t) {
				// TODO Auto-generated method stub
				if(t!=null && t.getResult()!=null){
					List<ZcMatchResultDTO> match=t.getResult().get("match");
					if (match!=null && match.size()>0) {
						ll_day01.setVisibility(View.VISIBLE);
						try {
							StringBuilder tv_day01_text=new StringBuilder("");
							tv_day01_text.append(date01.get(Calendar.MONTH)+1).append("月").append(date01.get(Calendar.DAY_OF_MONTH)).append("日");
							tv_day01_text.append("(").append(Util.getDayForWeek(DateUtils.toDateFormat(date01.getTime()))).append(")").append(" 共");
							tv_day01_text.append(match.size()).append("场比赛");
							tv_day01.setText(tv_day01_text.toString());
							adapter01.setDataList(match);
							adapter01.notifyDataSetChanged();
							httpDay02();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						ll_day01.setVisibility(View.GONE);
						httpDay02();
					}
				}

			}
		});
	}
	private void httpDay02() {
		String matchDate= DateUtils.getDateforint(date02.getTime());
		ServiceCaiZhongInformation.getInstance().getZc_Macth_Result(_this,matchDate,"17",new CallBack<HttpZcMatchListResult>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(_this, errorMessage, "访问失败");
			}
			@Override
			public void onSuccess(HttpZcMatchListResult t) {
				// TODO Auto-generated method stub
				if(t!=null && t.getResult()!=null){
					List<ZcMatchResultDTO> match=t.getResult().get("match");
					if (match!=null && match.size()>0) {
						ll_day02.setVisibility(View.VISIBLE);
						try {
							StringBuilder tv_day02_text=new StringBuilder("");
							tv_day02_text.append(date02.get(Calendar.MONTH)+1).append("月").append(date02.get(Calendar.DAY_OF_MONTH)).append("日");
							tv_day02_text.append("(").append(Util.getDayForWeek(DateUtils.toDateFormat(date02.getTime()))).append(")").append(" 共");
							tv_day02_text.append(match.size()).append("场比赛");
							tv_day02.setText(tv_day02_text.toString());
							adapter02.setDataList(match);
							adapter02.notifyDataSetChanged();
							httpDay03();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						ll_day02.setVisibility(View.GONE);
						httpDay03();
					}
				}
				
			}
		});
	}
	private void httpDay03() {
		String matchDate= DateUtils.getDateforint(date03.getTime());
		ServiceCaiZhongInformation.getInstance().getZc_Macth_Result(_this,matchDate,"17",new CallBack<HttpZcMatchListResult>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(_this, errorMessage, "访问失败");
			}
			@Override
			public void onSuccess(HttpZcMatchListResult t) {
				// TODO Auto-generated method stub
				if(t!=null && t.getResult()!=null){
					List<ZcMatchResultDTO> match=t.getResult().get("match");
					if (match!=null && match.size()>0) {
						ll_day03.setVisibility(View.VISIBLE);
						try {
							StringBuilder tv_day03_text=new StringBuilder("");
							tv_day03_text.append(date03.get(Calendar.MONTH)+1).append("月").append(date03.get(Calendar.DAY_OF_MONTH)).append("日");
							tv_day03_text.append("(").append(Util.getDayForWeek(DateUtils.toDateFormat(date03.getTime()))).append(")").append(" 共");
							tv_day03_text.append(match.size()).append("场比赛");
							tv_day03.setText(tv_day03_text.toString());
							adapter03.setDataList(match);
							adapter03.notifyDataSetChanged();
							httpDay04();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						ll_day03.setVisibility(View.GONE);
						httpDay04();
					}
				}
				
			}
		});
	}
	private void httpDay04() {
		String matchDate= DateUtils.getDateforint(date04.getTime());
		ServiceCaiZhongInformation.getInstance().getZc_Macth_Result(_this,matchDate,"17",new CallBack<HttpZcMatchListResult>() {
			@Override
			public void onFailure(ErrorMsg errorMessage) {
				PostHttpInfoUtils.doPostFail(_this, errorMessage, "访问失败");
			}
			@Override
			public void onSuccess(HttpZcMatchListResult t) {
				// TODO Auto-generated method stub
				if(t!=null && t.getResult()!=null){
					List<ZcMatchResultDTO> match=t.getResult().get("match");
					if (match!=null && match.size()>0) {
						ll_day04.setVisibility(View.VISIBLE);
						try {
							StringBuilder tv_day04_text=new StringBuilder("");
							tv_day04_text.append(date04.get(Calendar.MONTH)+1).append("月").append(date04.get(Calendar.DAY_OF_MONTH)).append("日");
							tv_day04_text.append("(").append(Util.getDayForWeek(DateUtils.toDateFormat(date04.getTime()))).append(")").append(" 共");
							tv_day04_text.append(match.size()).append("场比赛");
							tv_day04.setText(tv_day04_text.toString());
							adapter04.setDataList(match);
							adapter04.notifyDataSetChanged();
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						ll_day04.setVisibility(View.GONE);
					}
				}
				
			}
		});
	}

	private void initView() {
		
        //一开始是View.GONE的一个静止的TextView
        tvjing = (LinearLayout) findViewById(R.id.jing);
        sv = (ScrollView) findViewById(R.id.eee);
        tvjing.setVisibility(View.GONE);
		
		tv_day01=(TextView) findViewById(R.id.tv_day01);
		tv_day02=(TextView) findViewById(R.id.tv_day02);
		tv_day03=(TextView) findViewById(R.id.tv_day03);
		tv_day04=(TextView) findViewById(R.id.tv_day04);
		
		img_day01=(ImageView) findViewById(R.id.img_day01);
		img_day02=(ImageView) findViewById(R.id.img_day02);
		img_day03=(ImageView) findViewById(R.id.img_day03);
		img_day04=(ImageView) findViewById(R.id.img_day04);
		
		ll_day01=(LinearLayout) findViewById(R.id.ll_day01);
		ll_day02=(LinearLayout) findViewById(R.id.ll_day02);
		ll_day03=(LinearLayout) findViewById(R.id.ll_day03);
		ll_day04=(LinearLayout) findViewById(R.id.ll_day04);
		
		
		lv_day01=(MyListView) findViewById(R.id.lv_day01);
		lv_day02=(MyListView) findViewById(R.id.lv_day02);
		lv_day03=(MyListView) findViewById(R.id.lv_day03);
		lv_day04=(MyListView) findViewById(R.id.lv_day04);
		
		adapter01 = new JczqAdapter();
		lv_day01.setAdapter(adapter01);
		adapter02 = new JczqAdapter();
		lv_day02.setAdapter(adapter02);
		adapter03 = new JczqAdapter();
		lv_day03.setAdapter(adapter03);
		adapter04 = new JczqAdapter();
		lv_day04.setAdapter(adapter04);
		
		ll_day01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lv_show01=!lv_show01;
				if (lv_show01 == false) {
					lv_day01.setVisibility(View.GONE);
				}else{
					lv_day01.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		
		ll_day02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lv_show02=!lv_show02;
				if (lv_show02 == false) {
					lv_day02.setVisibility(View.GONE);
				}else{
					lv_day02.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		
		ll_day03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lv_show03=!lv_show03;
				if (lv_show03 == false) {
					lv_day03.setVisibility(View.GONE);
				}else{
					lv_day03.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		ll_day04.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lv_show04=!lv_show04;
				if (lv_show04 == false) {
					lv_day04.setVisibility(View.GONE);
				}else{
					lv_day04.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		

        sv.setOnTouchListener(new OnTouchListener() {
                private int lastY = 0;
                private int touchEventId = -9983761;
                Handler handler = new Handler() {
                        public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if (msg.what == touchEventId) {
                                        if (lastY != sv.getScrollY()) {
                                                 //scrollview一直在滚动，会触发
                                                handler.sendMessageDelayed(
                                                                handler.obtainMessage(touchEventId, sv), 5);
                                                lastY = sv.getScrollY();
                                                ll_day02.getLocationOnScreen(location);
                                                tvjing.getLocationOnScreen(location2);
                                                //动的到静的位置时，静的显示。动的实际上还是网上滚动，但我们看到的是静止的那个
                                                if (location[1] <= location2[1]) {
                                                        tvjing.setVisibility(View.VISIBLE);
                                                } else {
                                                        //静止的隐藏了
                                                        tvjing.setVisibility(View.GONE);
                                                }
                                        }
                                }
                        }
                };

                public boolean onTouch(View v, MotionEvent event) {
                         //必须两个都搞上，不然会有瑕疵。
                        //没有这段，手指按住拖动的时候没有效果
                        if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                handler.sendMessageDelayed(
                                                handler.obtainMessage(touchEventId, v), 5);
                        }
                        //没有这段，手指松开scroll继续滚动的时候，没有效果
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                                handler.sendMessageDelayed(
                                                handler.obtainMessage(touchEventId, v), 5);
                        }
                        return false;
                }
        });
		
		
		
		
	}

	private void changHead() {

		TextView tvTextView = (TextView) findViewById(R.id.tv_do);

		tvTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(JCZQPeriodsActivity.this,
						Main115Activity.class);
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
				Intent intent = new Intent(JCZQPeriodsActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
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
					holder.tv_BF.setText(Html.fromHtml(BF));
					holder.tv_BQC.setText(Html.fromHtml(BQC));
					holder.tv_RQSPF.setText(Html.fromHtml(RQSPF));
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

	public String getHtmlStr(String string, String string2) {
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
			str.append(string2);
			str.append("</font>");
		}
		return str.toString();
	}

}
