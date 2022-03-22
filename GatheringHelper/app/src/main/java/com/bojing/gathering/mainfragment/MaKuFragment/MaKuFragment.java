package com.bojing.gathering.mainfragment.MaKuFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bojing.gathering.R;
import com.bojing.gathering.domain.Back;
import com.bojing.gathering.domain.BackCommomString;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.MoneyList;
import com.bojing.gathering.domain.MoneyListBack;
import com.bojing.gathering.mainfragment.OrderFragment.OrderDetailActivity;
import com.bojing.gathering.mainfragment.OrderFragment.OrderListAdapter;
import com.bojing.gathering.net.ServiceABase;
import com.bojing.gathering.net.ServiceMaku;
import com.bojing.gathering.util.ImageZoom;
import com.bojing.gathering.util.NetWorkUtil;
import com.bojing.gathering.util.PreferenceUtil;
import com.bojing.gathering.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by admin on 2018/10/12.
 */

public class MaKuFragment extends Fragment{
    Activity _this;
    View view;
    View add;
    private Dialog alertDialog;
    EditText editText;

    private boolean isTasking = false;
    MaKuAdapter adapter;
    ListView listview;
    ArrayList<MoneyList> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _this = this.getActivity();
        if (view == null) {
            view = inflater.inflate(R.layout.makufragment, container, false);
            initView(view);
        }
        initData();
        return view;
    }

    private void initView(View view) {
        add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showNormalDialog();
            }
        });
        adapter = new MaKuAdapter(_this);
        listview = view.findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
               if(data!=null&&data.size()>0){
                    Intent intent = new Intent(_this,MaKuDetailActivity.class);
                    intent.putExtra("price", data.get(arg2).getPrice());
                    startActivity(intent);
               }
            }
        });
    }

    private void initData() {
        // 一些不涉及到传参数都请求都在预加载界面缓存到本地了
        if (!NetWorkUtil.isNetworkAvailable(this.getActivity())) {
            ToastUtils.showShort(this.getActivity(), "当前网络信号较差，请检查网络设置");
        }
        if (isTasking) {// 任务在运行中不再登入
            return;
        }
        initMoneyList();
    }

    private void initMoneyList() {
     ServiceMaku.getInstance().GetMoneyList(getActivity(), PreferenceUtil.getString(_this, "username"),
                new ServiceABase.CallBack<MoneyListBack>() {

                    @Override
                    public void onSuccess(MoneyListBack t) {
                        if(t!=null&&
                                t.getData()!=null&&
                                t.getData().size()>0){
                            data  = t.getData();
                            adapter.setDate(data);
                            adapter.notifyDataSetInvalidated();
                        }
                        isTasking = false;
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        // TODO 自动生成的方法存�?
                        ToastUtils.showShort(getActivity(), errorMessage.msg);
                        isTasking = false;
                    }
                });
    }
    private void showNormalDialog(){
        if(alertDialog!=null&&!alertDialog.isShowing()){
            alertDialog.show();
        }else{
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_this,R.style.dialog);
            View view = View.inflate(_this, R.layout.dialog_createerweima, null);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
            alertDialog.getWindow().setContentView(view);


            Window window = alertDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = 500;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            editText = view.findViewById(R.id.input);
            TextView cancle = view.findViewById(R.id.cancle);
            cancle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                    alertDialog.dismiss();
                }
            });
            TextView sure = view.findViewById(R.id.sure);
            sure.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    getNewPic();
                }
            });

        }
    }
    private void getNewPic() {
        ServiceMaku.getInstance().GetNewPic(getActivity(), PreferenceUtil.getString(_this, "username"),editText.getText().toString(),
                new ServiceABase.CallBack<BackCommomString>() {

                    @Override
                    public void onSuccess(BackCommomString t) {
                        ToastUtils.showShort(_this,"创建成功");
                        alertDialog.cancel();
                        alertDialog.dismiss();
                        initData();
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        // TODO 自动生成的方法存�?
                        ToastUtils.showShort(getActivity(), errorMessage.msg);
                        alertDialog.cancel();
                        alertDialog.dismiss();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(alertDialog!=null&&!alertDialog.isShowing()){
            alertDialog.dismiss();
            alertDialog.cancel();
        }
    }
}
