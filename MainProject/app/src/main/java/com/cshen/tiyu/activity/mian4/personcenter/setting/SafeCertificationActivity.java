package com.cshen.tiyu.activity.mian4.personcenter.setting;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.login.FindLoginPwdActivity;
import com.cshen.tiyu.activity.login.ForgotPwdActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.login.Question;
import com.cshen.tiyu.net.https.ServiceUser;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.utils.PostHttpInfoUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.utils.Util;
import com.cshen.tiyu.widget.TopViewLeft;
import com.cshen.tiyu.widget.TopViewLeft.TopClickItemListener;
import com.google.gson.Gson;

/**
 * 安全问题认证
 * 
 * @author yx
 * 
 */
public class SafeCertificationActivity extends BaseActivity implements
		OnClickListener {

	private Context mContext;
	private TopViewLeft tv_head;
	private TextView tv_question1, tv_question2, tv_question3;// 问题
	private EditText edt_answer1, edt_answer2, edt_answer3;// 答案
	private ImageView iv_arrow1, iv_arrow2, iv_arrow3;// 箭头
	private Button btn_sure;
	private PopupWindow ppw;
	private ArrayList<Question> question1List = new ArrayList<>();// 问题1集合
	private ArrayList<Question> question2List = new ArrayList<>();// 问题2集合
	private ArrayList<Question> question3List = new ArrayList<>();// 问题3集合
	private String key1 ="", key2 ="", key3 ="";
	private boolean isTasking;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_setting_safe_certification);
		mContext = this;
		initHead();
		initView();
		getAllSafeQuestions();
	}

	private void initView() {
		tv_question1 = (TextView) findViewById(R.id.tv_question1);
		tv_question2 = (TextView) findViewById(R.id.tv_question2);
		tv_question3 = (TextView) findViewById(R.id.tv_question3);
		edt_answer1 = (EditText) findViewById(R.id.edt_answer1);
		edt_answer2 = (EditText) findViewById(R.id.edt_answer2);
		edt_answer3 = (EditText) findViewById(R.id.edt_answer3);
		iv_arrow1 = (ImageView) findViewById(R.id.iv_arrow1);
		iv_arrow2 = (ImageView) findViewById(R.id.iv_arrow2);
		iv_arrow3 = (ImageView) findViewById(R.id.iv_arrow3);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		iv_arrow1.setOnClickListener(this);
		iv_arrow2.setOnClickListener(this);
		iv_arrow3.setOnClickListener(this);
		btn_sure.setOnClickListener(this);

	}

	private void initHead() {
		tv_head = (TopViewLeft) findViewById(R.id.tv_head);

		tv_head.setResourceVisiable(true, false, false);
		tv_head.setTopClickItemListener(new TopClickItemListener() {

			@Override
			public void clickLoginView(View view) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void clickContactView(View view) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void clickBackImage(View view) {
				// TODO 自动生成的方法存根
				finish();
			}
		});

	}

	private void getAllSafeQuestions() {
		ServiceUser.getInstance().getAllSafeQuestions(mContext,
				new CallBack<String>() {

					@Override
					public void onSuccess(String result) {
						dealQuestion(result);
					}

					@Override
					public void onFailure(ErrorMsg errorMessage) {
						ToastUtils.showShortCenter(mContext, "获取安全问题失败！");
					}
				});
	}

	/**
	 * 处理数据
	 */
	@SuppressWarnings("unchecked")
	private void dealQuestion(String result) {
		try {
			JSONObject object = new JSONObject(result);
			String problem1 = object.getString("problem1").replaceAll("\\\\\"",
					" ");
			String problem2 = object.getString("problem2").replaceAll("\\\\\"",
					" ");
			String problem3 = object.getString("problem3").replaceAll("\\\\\"",
					" ");
			Gson gson = new Gson();
			LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
			map1 = gson.fromJson(problem1, map1.getClass());
			traverseMap(map1, question1List);
			LinkedHashMap<String, String> map2 = new LinkedHashMap<>();
			map2 = gson.fromJson(problem2, map1.getClass());
			traverseMap(map2, question2List);
			LinkedHashMap<String, String> map3 = new LinkedHashMap<>();
			map3 = gson.fromJson(problem3, map1.getClass());
			traverseMap(map3, question3List);
			//默认显示第0条
			key1 = question1List.get(0).getKey();
			key2 = question2List.get(0).getKey();
			key3 = question3List.get(0).getKey();
			tv_question1.setText(question1List.get(0).getValue());
			tv_question2.setText(question2List.get(0).getValue());
			tv_question3.setText(question3List.get(0).getValue());
		} catch (JSONException e) {
			e.printStackTrace();
			key1 = "";
			key2 = "";
			key3 = "";
		}
	}

	public void traverseMap(LinkedHashMap<String, String> map,
			ArrayList<Question> problem) {
		for (String key : map.keySet()) { // 遍历取出key，再遍历map取出value。
			String value = map.get(key);
			Question question = new Question();
			question.setKey(key);
			question.setValue(value);
			problem.add(question);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_arrow1:
			showQuestionPPW(question1List, tv_question1,new getKeyCallBack() {
				
				@Override
				public void getKey(String key) {
					key1=key;
				}
			} );
			break;
		case R.id.iv_arrow2:
            showQuestionPPW(question2List, tv_question2,new getKeyCallBack() {
				
				@Override
				public void getKey(String key) {
					key2=key;
				}
			} );
			break;
		case R.id.iv_arrow3:
            showQuestionPPW(question3List, tv_question3,new getKeyCallBack() {
				
				@Override
				public void getKey(String key) {
					key3=key;
				}
			} );
			break;
		case R.id.btn_sure:
			if (isTasking) {// 任务在运行中不再登入
				return;
			}
			submitQuestions();
			break;

		default:
			break;
		}
	}

	// 提交
	private void submitQuestions() {
		String answer1 = edt_answer1.getText().toString();
		String answer2 = edt_answer2.getText().toString();
		String answer3 = edt_answer3.getText().toString();
		if (TextUtils.isEmpty(answer1)||TextUtils.isEmpty(answer2)||TextUtils.isEmpty(answer3)) {
			ToastUtils.showShortCenter(mContext, "问题答案不可为空！");
			return;
		}
		if (answer1.length() < 2 || answer2.length() < 2|| answer3.length()<2
				|| !Util.textNameTemp_NoSpace(answer1) || !Util.textNameTemp_NoSpace(answer2)||!Util.textNameTemp_NoSpace(answer3)) {// 答案格式不正确
			ToastUtils.showShortCenter(mContext, "2-15位汉字,字母,数字答案！");
			return;
		}
		if (answer1.length() >15 || answer2.length() >15|| answer3.length()>15) {
			ToastUtils.showShortCenter(mContext, "答案最多15位！");
			return;
		}
		String problemJson="";
		JSONObject object=new JSONObject();
		try {
			object.put(key1.trim(), answer1);		
			object.put(key2.trim(), answer2);
			object.put(key3.trim(), answer3);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		problemJson=object.toString();
		isTasking=true;
		ServiceUser.getInstance().submitSafeQuestions(mContext, problemJson, new CallBack<String>() {

			@Override
			public void onSuccess(String t) {
               isTasking=false;	
               ToastUtils.showShortCenter(mContext, "安全认证成功！");
             
               finish();
			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				isTasking=false;
				ToastUtils.showShortCenter(mContext, "提交安全问题失败！");
			}
		});

	}

	// 显示安全问题的pupupwindow
	private void showQuestionPPW(final ArrayList<Question> mDatas,
			 final TextView tv,final getKeyCallBack callBack) {
	
		if (mDatas == null||mDatas.size()==0) {
			return;
		}
		if (ppw != null && ppw.isShowing()) {
			ppw.dismiss();
		} else {
			WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
			int pw_width=width-dip2px(mContext, 120);
			
			View view = getLayoutInflater().inflate(R.layout.ppw_safe_question,
					null);
			ListView listView = (ListView) view
					.findViewById(R.id.lv_ppw_question);
			listView.setAdapter(new QuestionAdapter(mDatas));
			ppw = new PopupWindow(view,pw_width,dip2px(mContext, 200));
			ppw.setBackgroundDrawable(new BitmapDrawable());
			// 因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键
			ppw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			ppw.update();
			ppw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			ppw.setTouchable(true); // 设置popupwindow可点击
			ppw.setOutsideTouchable(true); // 设置popupwindow外部可点击
			ppw.setFocusable(true); // 获取焦点
			// 设置popupwindow的位置
			
			ppw.showAsDropDown(tv,0,dip2px(mContext, 10));
			ppw.setTouchInterceptor(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// 如果点击了popupwindow的外部，popupwindow也会消失
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						ppw.dismiss();
						return true;
					}
					return false;
				}
			});

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					tv.setText(mDatas.get(arg2).getValue());
					String key = mDatas.get(arg2).getKey();
					if (callBack!=null) {
						callBack.getKey(key);
					}
					
				ppw.dismiss();
				}
			});
		}

	}
	
	public interface  getKeyCallBack{
		void getKey(String key);
	}
	

	public class QuestionAdapter extends BaseAdapter {
		ArrayList<Question> mQuestions;

		public QuestionAdapter(ArrayList<Question> mDatas) {
			this.mQuestions = mDatas;
		}

		@Override
		public int getCount() {
			return mQuestions.size();
		}

		@Override
		public Object getItem(int position) {
			return mQuestions.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = getLayoutInflater().inflate(R.layout.item_ppw_safe,
					parent, false);
			TextView tv = (TextView) view.findViewById(R.id.tv_item);
			tv.setText(mQuestions.get(position).getValue());
			
			return view;
		}

	}
	
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
    
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
