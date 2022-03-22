package com.cshen.tiyu;

import java.util.ArrayList;

import com.cshen.tiyu.db.MyDbUtils;
import com.cshen.tiyu.domain.main.PreLoadPage;
import com.cshen.tiyu.domain.main.PreLoadPageData;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.StatusBarUtil;
import com.cshen.tiyu.zx.ZXMainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class GuideActivity extends Activity {
	private ViewPager mAdView;
	ImageCycleAdapter pagerAdapter ;
	public int stype = 2;

	private boolean isMaJia=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		StatusBarUtil.setTranslucentForImageViewInFragment(this, null);//状态栏
		PreferenceUtil.putBoolean(this, "is_user_guide_showed", true);

		isMaJia=PreferenceUtil.getBoolean(this, "isMaJia");
		initView();
		initData();

	}

	private void initView() {
		// TODO 自动生成的方法存根
		mAdView = (ViewPager) findViewById(R.id.ad_view);
		pagerAdapter = new ImageCycleAdapter();
	}

	private void initData() {
		if (isMaJia) {//马甲包 引导页本地
			ArrayList<String> mImageUrl = new ArrayList<String>();
			ArrayList<String> mImageTitle = new ArrayList<String>();
			mImageUrl.add(R.mipmap.zx_guide1+"");
			mImageUrl.add(R.mipmap.zx_guide2+"");
			mImageUrl.add(R.mipmap.zx_guide3+"");
			mImageTitle.add("");
			mImageTitle.add("");
			mImageTitle.add("");
			pagerAdapter.setData(mImageUrl,mImageTitle);
			mAdView.setAdapter(pagerAdapter);  
			mAdView.setCurrentItem(0);
			pagerAdapter.notifyDataSetChanged();
		}else {
			ArrayList<String> mImageUrl = new ArrayList<String>();
			ArrayList<String> mImageTitle = new ArrayList<String>();
			mImageUrl.add(R.mipmap.guide11+"");
			mImageUrl.add(R.mipmap.guide22+"");
			mImageUrl.add(R.mipmap.guide33+"");
			mImageTitle.add("");
			mImageTitle.add("");
			mImageTitle.add("");
			pagerAdapter.setData(mImageUrl,mImageTitle);
			mAdView.setAdapter(pagerAdapter);
			mAdView.setCurrentItem(0);
			pagerAdapter.notifyDataSetChanged();
			// // 获取本地的数据
			/*PreLoadPageData preLoadPageData = MyDbUtils.getPreLoadPageData();
			if (preLoadPageData != null) {
				ArrayList<PreLoadPage> preLoadPages = preLoadPageData.getPreLoadPages(); // 获取list数据
				ArrayList<PreLoadPage> guidePages = new ArrayList<PreLoadPage>(); // 获取list数据
				for (int i = 0; i < preLoadPages.size(); i++) {
					if (preLoadPages.get(i).getType() == 2) {
						guidePages.add(preLoadPages.get(i));//属于引导页面的数据给guidepage
					}
				}
				ArrayList<String> mImageUrl = new ArrayList<String>();
				ArrayList<String> mImageTitle = new ArrayList<String>();

				for (int j = 0; j < guidePages.size(); j++) {
					mImageUrl.add(guidePages.get(j).getIcon());// 获取图片地址
					mImageTitle.add(guidePages.get(j).getUrl());//
				}
				pagerAdapter.setData(mImageUrl,mImageTitle);
				mAdView.setAdapter(pagerAdapter);  
				mAdView.setCurrentItem(0);
				pagerAdapter.notifyDataSetChanged();
			}*/
		}
	}
	private class ImageCycleAdapter extends PagerAdapter {
		private ArrayList<ImageView> mImageViewCacheList = new ArrayList<ImageView>();
		private ArrayList<String> mAdUrl = new ArrayList<String>();
		private ArrayList<String> mAdTitle = new ArrayList<String>();
		public void setData(ArrayList<String> mAdUrl,ArrayList<String> mAdTitle) {  
			// TODO Auto-generated method stub  
			this.mAdUrl = mAdUrl;  
			this.mAdTitle = mAdTitle;
		}  
		@Override  
		public boolean isViewFromObject(View arg0, Object arg1) {  
			// TODO Auto-generated method stub  
			return arg0 == arg1;  
		}  

		@Override  
		public int getCount() {  
			// TODO Auto-generated method stub  
			return mAdUrl.size();  
		}  

		@Override  
		public void destroyItem(ViewGroup container, int position,  
				Object object) {  
			// TODO Auto-generated method stub  
			ImageView view = (ImageView) object;
			container.removeView(view);
			mImageViewCacheList.add(view);
		}  

		@Override  
		public Object instantiateItem(ViewGroup container, int position) {  
			// TODO Auto-generated method stub  
			ImageView imageView = null;
			if (mImageViewCacheList.isEmpty()) {
				imageView = new ImageView(GuideActivity.this);
				imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			} else {
				imageView = mImageViewCacheList.remove(0);
			}
			if (isMaJia) {
				imageView.setImageResource(Integer.parseInt(mAdUrl.get(position)));
			}else {
				imageView.setImageResource(Integer.parseInt(mAdUrl.get(position)));
				//xUtilsImageUtils.display(imageView,R.mipmap.ic_error,mAdUrl.get(position));
			}

			container.addView(imageView);
			if(position == mAdUrl.size()-1){
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (isMaJia) {
							startActivity(new Intent(GuideActivity.this, ZXMainActivity.class));
						}else {
							startActivity(new Intent(GuideActivity.this, MainActivity.class));
						}

						finish();
					}
				});	
			}		
			return imageView;  
		} 

	}

}
