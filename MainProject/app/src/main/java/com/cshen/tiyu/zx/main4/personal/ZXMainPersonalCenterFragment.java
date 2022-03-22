package com.cshen.tiyu.zx.main4.personal;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.personcenter.message.MessageListActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.AboutUsActivity;
import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.login.User;

import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.DirsUtil;
import com.cshen.tiyu.widget.PersonalItemView;
import com.cshen.tiyu.widget.ShapedImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

public class ZXMainPersonalCenterFragment extends Fragment 
implements OnClickListener{
	private Activity _this;
//	private RelativeLayout rl_pic;
	private TextView tv_setting;
	private ShapedImageView namePic;
	private TextView tv_userName;
	private PersonalItemView rl_history,rl_feedback,rl_aboutus,rl_message;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_zx_personal,container, false);
		_this = this.getActivity();
		EventBus.getDefault().register(this);
		initView(view);
		initValue();
		return view;
	}
	private void initView(View view) {
		tv_setting=(TextView) view.findViewById(R.id.tv_setting);
		namePic=(ShapedImageView) view.findViewById(R.id.name_im);
		rl_history=(PersonalItemView) view.findViewById(R.id.rl_history);
		rl_feedback=(PersonalItemView) view.findViewById(R.id.rl_feedback);
		rl_message=(PersonalItemView) view.findViewById(R.id.rl_message);
		rl_aboutus=(PersonalItemView) view.findViewById(R.id.rl_aboutus);
		tv_userName=(TextView) view.findViewById(R.id.tv_userName);
		tv_setting.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
		rl_history.setOnClickListener(this);	
		rl_aboutus.setOnClickListener(this);
		rl_message.setOnClickListener(this);
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO 自动生成的方法存根
		if (!hidden) {
		    initValue();
		}
		super.onHiddenChanged(hidden);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_setting://修改頭像
			startActivity(new Intent(_this,ZXSettingActivity.class));
			break;
		case R.id.rl_history://浏览历史
			startActivity(new Intent(_this,ZXHistoryActivity.class));
			break;
		case R.id.rl_feedback://用户反馈
		   //FeedbackAPI.openFeedbackActivity();
			break;
		case R.id.rl_aboutus://关于我们
			startActivity(new Intent(_this, AboutUsActivity.class));
			break;
		case R.id.rl_message://消息中心
			startActivity(new Intent(_this, MessageListActivity.class));
			break;
		default:
			break;
		}
	}
	private void initValue() {
		setUserPic();//头像
		updateUserName();//昵称	
	}
	
	private void setUserPic(){
		if(MyDbUtils.getCurrentUser()!=null){
			String url = MyDbUtils.getCurrentUser().getUserPic();
			xUtilsImageUtils.display(namePic,url, R.mipmap.ssq);
			namePic.invalidate(); 
		}
	}
	private void updateUserName() {
		tv_userName.setText("");
		User user = MyDbUtils.getCurrentUser();
		if (user != null && (!TextUtils.isEmpty(user.getUserName()))) {
			tv_userName.setText(user.getUserName());
		} else {
			tv_userName.setText("");
		}
	}
	
	public void onEventMainThread(String event) {

		if (!TextUtils.isEmpty(event)) {
			
			if ("updateUserName".equals(event)) {
				updateUserName();
			}
			
			if ("updateUserPic".equals(event)) {
				setUserPic();
			}
			
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}
	
}
