package com.bojing.gathering;

import com.bojing.gathering.domain.Back;
import com.bojing.gathering.domain.BackCommomString;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.UserBack;
import com.bojing.gathering.net.ServiceABase;
import com.bojing.gathering.net.ServiceUser;
import com.bojing.gathering.util.PreferenceUtil;
import com.bojing.gathering.util.ToastUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class LoginActivity extends Activity {
    private EditText mUsernameView;
    private EditText mPasswordView;
    private ImageView delete;
    private View mProgressView;
    private LoginActivity _this;
    private boolean isTasking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _this = this;
        if(PreferenceUtil.getBoolean(_this, "hasLogin")
                && !TextUtils.isEmpty(PreferenceUtil.getString(_this, "username"))){
            Intent intent = new Intent(_this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        delete = (ImageView) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPasswordView.setText("");
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id,
                                          KeyEvent keyEvent) {
                if (id == KeyEvent.ACTION_DOWN
                        || id == EditorInfo.IME_ACTION_NONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ee = s.toString();
                if (ee != null && ee.length() > 0) {
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                }
            }
        });
        TextView mSignInButton = (TextView) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
        String username = PreferenceUtil.getString(_this, "username");
        if (!TextUtils.isEmpty(username)) {
            mUsernameView.setText(username);
            mUsernameView.setSelection(username.length());
        }
    }

    public void attemptLogin() {
        if (isTasking) {// ??????????????????????????????
            return;
        }
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort(_this, "??????????????????????????????");
            mPasswordView.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showShort(_this, "?????????????????????????????????");
            mUsernameView.requestFocus();
            return;
        }

        showProgress(true);
        isTasking = true;
        ServiceUser.getInstance().PostLogin(_this, username, password,
                new ServiceABase.CallBack<UserBack>() {
                    @Override
                    public void onSuccess(UserBack userResultData) {
                        ToastUtils.showShort(_this, userResultData.getMsg());
                        PreferenceUtil.putBoolean(_this, "hasLogin", true);
                        PreferenceUtil.putString(_this, "username", username);
                        showProgress(false);
                        isTasking = false;// ???????????????????????????
                        // ??????????????????????????????
                        Intent intent = new Intent(_this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }


                    @Override
                    public void onFailure(ErrorMsg errorMsg) {
                        ToastUtils.showShort(_this, errorMsg.msg);
                        showProgress(false);
                        isTasking = false;
                    }
                });
    }

    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    // ???????????????
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // TODO ???????????????????????????
                finish();// ????????? ?????????activity ??????
                Intent intent = new Intent(_this, MainActivity.class);
                intent.putExtra("hasLogin", "cancel");
                startActivity(intent);
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
