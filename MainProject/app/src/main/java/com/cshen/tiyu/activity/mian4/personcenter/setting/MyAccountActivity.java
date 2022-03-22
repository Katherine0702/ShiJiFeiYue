package com.cshen.tiyu.activity.mian4.personcenter.setting;

import java.math.BigDecimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.LotteryTypeActivity;
import com.cshen.tiyu.activity.mian4.personcenter.DrawingsActivity;
import com.cshen.tiyu.activity.mian4.personcenter.account.AccountDetailActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.BankCardActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.NameAuthActivity;
import com.cshen.tiyu.activity.mian4.personcenter.setting.binding.PayPwdActivity;
import com.cshen.tiyu.activity.pay.PayMoneyActivity;
import com.cshen.tiyu.base.BaseActivity;
import com.cshen.tiyu.base.ConstantsBase;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;

public class MyAccountActivity extends BaseActivity implements OnClickListener 
{
	TextView title,accountView;
	View network,padnetwork;
	String account;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myaccount);
		title= (TextView) findViewById(R.id.tv_head_title);
		title.setText("我的账户");

		account = getIntent().getStringExtra("account");
		if(account.indexOf("元>")==(account.length()-2)){
			account = account.substring(0, account.length()-2);
		}
		accountView = (TextView)findViewById(R.id.account);
		accountView.setText(account);
		findViewById(R.id.iv_back).setVisibility(View.VISIBLE);
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.accountdetil).setOnClickListener(this);
		findViewById(R.id.pv_recharge).setOnClickListener(this);
		findViewById(R.id.pv_drawings).setOnClickListener(this);
	}

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.iv_back:
				this.finish();
				break;
			case R.id.accountdetil:
				startActivity(new Intent(this, AccountDetailActivity.class));
				break;
			case R.id.pv_recharge:
//				if(!PreferenceUtil.getBoolean(this, "hasRealName")){
//					ToastUtils.showShort(this, "亲，请您先进行实名认证");
//					Intent intent = new Intent(this, NameAuthActivity.class);
//					startActivity(intent);
//					return;
//				}
	//			Intent intent = new Intent(this, LotteryTypeActivity.class);
	//			intent.putExtra("url", ConstantsBase.IPQT + "/#4/account/recharge");
	//			startActivity(intent);
	//			break;
				Intent intent = new Intent(this, PayMoneyActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.pv_drawings:
				if(!PreferenceUtil.getBoolean(this, "hasRealName")){
					ToastUtils.showShort(this, "亲，请您先进行实名认证");
					Intent intentRealName = new Intent(this, NameAuthActivity.class);
					startActivity(intentRealName);
					return;
				}
				if(!PreferenceUtil.getBoolean(this, "hasBindBankCard")){
					ToastUtils.showShort(this, "亲，请您先绑定银行卡后再进行提款");
					Intent intentBankCard = new Intent(this, BankCardActivity.class);
					startActivity(intentBankCard);
					return;
				}if(!PreferenceUtil.getBoolean(this, "hasPayPassword")){
					ToastUtils.showShort(this, "亲，请您先设置支付密码再进行提款");
					Intent intentBankCard = new Intent(this, PayPwdActivity.class);
					startActivity(intentBankCard);
					return;
				}
				startActivityForResult(new Intent(this,DrawingsActivity.class), 2);
				break;
			default:
				break;
			}
		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			if (requestCode == 2) {
				String remainMoney = data.getStringExtra("remainMoney");
				try{
					float old = Float.valueOf(accountView.getText().toString());
					float remain = Float.valueOf(remainMoney);
					BigDecimal oldF = new BigDecimal(Float.toString(old));
					BigDecimal remainF = new BigDecimal(Float.toString(remain));
					
					accountView.setText((oldF.subtract(remainF).floatValue())+"");
				}catch(Exception e){
					e.printStackTrace();
				}
				startActivity(new Intent(this, AccountDetailActivity.class));
			}
		}
	}
}
