package com.cshen.tiyu.activity.mian4.find.shareorder;



import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.net.https.xUtilsImageUtils;


/**
 * 图片查看器，用于图片缩放
 */
public class ImagePagerActivity extends FragmentActivity {
	public static final String EXTRA_IMAGE_URL = "image_url";

	private String pagerUrl;
	private ImageView mPager;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);

		pagerUrl  = getIntent().getStringExtra(EXTRA_IMAGE_URL);

		mPager = (ImageView) findViewById(R.id.pager);
		xUtilsImageUtils.displayFitCenter(mPager, R.mipmap.ic_error,pagerUrl);
		mPager.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImagePagerActivity.this.finish();
			}
		});
	}
}
