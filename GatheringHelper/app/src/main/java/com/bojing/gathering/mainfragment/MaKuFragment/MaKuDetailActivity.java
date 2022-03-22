package com.bojing.gathering.mainfragment.MaKuFragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bojing.gathering.R;
import com.bojing.gathering.domain.BackCommomString;
import com.bojing.gathering.domain.ErrorMsg;
import com.bojing.gathering.domain.MoneyOther;
import com.bojing.gathering.domain.MoneyOtherListBack;
import com.bojing.gathering.domain.MoneyOtherPic;
import com.bojing.gathering.domain.MoneyOtherPicListBack;
import com.bojing.gathering.domain.OrderDetailBack;
import com.bojing.gathering.mainfragment.OrderFragment.OrderDetailActivity;
import com.bojing.gathering.net.ServiceABase;
import com.bojing.gathering.net.ServiceMain;
import com.bojing.gathering.net.ServiceMaku;
import com.bojing.gathering.net.ServiceUser;
import com.bojing.gathering.util.ImageZoom;
import com.bojing.gathering.util.PreferenceUtil;
import com.bojing.gathering.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by admin on 2018/10/23.
 */

public class MaKuDetailActivity extends Activity {
    private static final int REQUEST_IMAGE = 2;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    MaKuDetailActivity _this;
    String price;
    View back,add;
    TextView money;
    MaKuDetail1Adapter adapter1;
    MaKuDetail2Adapter adapter2;
    TextView listview1view,listview2view;
    ListView listview1,listview2;
    private boolean isTasking1 = false;
    private boolean isTasking2 = false;
    ArrayList<MoneyOther> moneyotherlist;
    ArrayList<MoneyOtherPic> moneyotherpiclist;

    private ArrayList<String> mSelectPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makudetail);
        _this = this;
        Bundle bundle = getIntent().getExtras();
        price = bundle.getString("price");
        initView();
        initData1();
    }
    public void initView(){
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                _this.finish();
            }
        });
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        money = findViewById(R.id.money);
        money.setText("¥"+price);
        listview1view = findViewById(R.id.listview1view);
        listview2view = findViewById(R.id.listview2view);
        listview1view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setTitleBack(true);
            }
        });
        listview2view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setTitleBack(false);
            }
        });
        adapter1 = new MaKuDetail1Adapter(_this);
        listview1 = findViewById(R.id.listview1);
        listview1.setAdapter(adapter1);
        adapter2 = new MaKuDetail2Adapter(_this);
        listview2 = findViewById(R.id.listview2);
        listview2.setAdapter(adapter2);
    }
    private void initData1() {
        if (isTasking1) {// 任务在运行中不再登入
            return;
        }
        isTasking1 = true;
        ServiceMaku.getInstance().GetMoneyOtherList(_this, PreferenceUtil.getString(_this, "username"), price,
                new ServiceABase.CallBack<MoneyOtherListBack>() {
                    @Override
                    public void onSuccess(MoneyOtherListBack otherprice) {
                        if (otherprice != null &&
                                otherprice.getData() != null&&
                                otherprice.getData().size()>=0) {
                            moneyotherlist = otherprice.getData();
                            adapter1.setDate(moneyotherlist);
                            adapter1.notifyDataSetInvalidated();
                            isTasking1 = false;
                        }
                    }


                    @Override
                    public void onFailure(ErrorMsg errorMsg) {
                        ToastUtils.showShort(_this, errorMsg.msg);
                        isTasking1 = false;
                    }
                });
    }
    private void initData2() {
        if (isTasking2) {// 任务在运行中不再登入
            return;
        }
        isTasking2 = true;
        ServiceMaku.getInstance().GetMoneyOtherPicList(_this, PreferenceUtil.getString(_this, "username"), price,
                new ServiceABase.CallBack<MoneyOtherPicListBack>() {
                    @Override
                    public void onSuccess(MoneyOtherPicListBack otherprice) {
                        if (otherprice != null &&
                                otherprice.getData() != null&&
                                otherprice.getData().size()>=0) {
                            moneyotherpiclist = otherprice.getData();
                            adapter2.setDate(moneyotherpiclist);
                            adapter2.notifyDataSetInvalidated();
                            isTasking2 = false;
                        }
                    }


                    @Override
                    public void onFailure(ErrorMsg errorMsg) {
                        ToastUtils.showShort(_this, errorMsg.msg);
                        isTasking2 = false;
                    }
                });
    }
    public void setTitleBack(boolean isErWeiMa){
        if(isErWeiMa){
            listview1view.setTextColor(getResources().getColor(R.color.white));
            listview1view.setBackgroundColor(getResources().getColor(R.color.lightblue));
            listview2view.setTextColor(getResources().getColor(R.color.textcolor));
            listview2view.setBackgroundColor(getResources().getColor(R.color.white));
            listview1.setVisibility(View.VISIBLE);
            listview2.setVisibility(View.GONE);
            initData1();
        }else{
            listview1view.setTextColor(getResources().getColor(R.color.textcolor));
            listview1view.setBackgroundColor(getResources().getColor(R.color.white));
            listview2view.setTextColor(getResources().getColor(R.color.white));
            listview2view.setBackgroundColor(getResources().getColor(R.color.lightblue));
            listview1.setVisibility(View.GONE);
            listview2.setVisibility(View.VISIBLE);
            initData2();
        }
    }
    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        }else {
            int maxNum = 9;
            MultiImageSelector selector = MultiImageSelector.create(_this);
            selector.showCamera(false);
            selector.count(maxNum);
            selector.multi();
            selector.origin(mSelectPath);
            selector.start(_this, REQUEST_IMAGE);
        }
    }
    private void requestPermission(final String permission, String rationale, final int requestCode){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(_this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickImage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                ArrayList<File> fileImages = new ArrayList<>();
                for(String p: mSelectPath){
                    File fileImage = new File(p);
                    fileImages.add(fileImage);
                    sb.append(p);
                    sb.append("\n");
                }
                money.setText(sb.toString());
                setPics(fileImages);
            }
        }
    }
    private void setPics(ArrayList<File> fileImages) {
        ServiceMaku.getInstance().PostPic(_this, PreferenceUtil.getString(_this, "username"),price,fileImages,
                new ServiceABase.CallBack<BackCommomString>() {

                    @Override
                    public void onSuccess(BackCommomString t) {
                        initData2();
                    }

                    @Override
                    public void onFailure(ErrorMsg errorMessage) {
                        // TODO 自动生成的方法存�?
                        ToastUtils.showShort(_this, errorMessage.msg);
                    }
                });
    }
    private String toZoom(String  fileAllPath) {
        String[] suffixs = fileAllPath.split("\\.");//用.来区分获取最后的后缀名
        if(suffixs.length<=1){//没有后缀名，直接上传；
            return fileAllPath;
        }
        String suffix = suffixs[suffixs.length-1];//有后缀名获取后缀名，如.jpg
        String[] names= fileAllPath.split("\\/");//用/来区分获取名字
        if(names.length<=0){//没有名字，直接上传；
            return fileAllPath;
        }
        String namesuffix = names[names.length-1];//有名字获取名字带后缀名
        String name = namesuffix.substring(0, namesuffix.length()-suffix.length()-1);//有名字获取名字
        int  length = fileAllPath.length()-namesuffix.length();//名字之前路径的长度
        if(length<0){
            return fileAllPath;
        }
        StringBuffer small = new StringBuffer();
        small = small.append(fileAllPath.substring(0,length)).append(name.replace(".", "")).append("_Small.").append(suffix);

        boolean isZoom = false;
        if (null != fileAllPath) {
            isZoom = new ImageZoom().imageZoom(fileAllPath,small.toString());// 照片压缩
        }
        if (isZoom) {
            fileAllPath = small.toString();
        }
        return fileAllPath;
    }


            /*if (data.getData() != null) {
                Uri selectedImageBenDi = data.getData();
                String lujinBenDi = "";
                Cursor cursorBenDi = _this.getContentResolver().query(selectedImageBenDi, null,
                        null, null, null);
                if (cursorBenDi != null && cursorBenDi.moveToFirst()) {
                    try {
                        lujinBenDi = cursorBenDi.getString(cursorBenDi.getColumnIndex("_data"));// 获取绝对路径
                        cursorBenDi.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        lujinBenDi = selectedImageBenDi.getPath();
                    }
                } else {
                    lujinBenDi = selectedImageBenDi.getPath();
                }
                lujinBenDi = toZoom(lujinBenDi);
                File fileImageBenDi = new File(lujinBenDi);
               // uploadPic(fileImageBenDi, lujinBenDi);
            } else {
                ToastUtils.showShort(_this, "文件不存在，请重新选择！");
            }*/
}
