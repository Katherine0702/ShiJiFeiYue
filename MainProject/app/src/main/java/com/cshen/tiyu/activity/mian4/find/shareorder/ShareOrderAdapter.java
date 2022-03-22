package com.cshen.tiyu.activity.mian4.find.shareorder;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.activity.mian4.gendan.niuren.NiuRenActivity;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.find.MyShareOrderBean.ThesunlistBean;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceShareOrder;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.PreferenceUtil;
import com.cshen.tiyu.utils.ToastUtils;
import com.cshen.tiyu.widget.ShapedImageView;


public class ShareOrderAdapter extends BaseAdapter {
	private ArrayList<ThesunlistBean> orderLists;
	private Activity _this;
	private updatePraiseStatus mPraiseStatus;// 记录点赞状态
	// 展开阅读全文相关
	private final int MAX_LINE_COUNT = 4;
	private final int STATE_UNKNOW = -1;
	private final int STATE_NOT_OVERFLOW = 1; // 文本行数不超过限定行数
	private final int STATE_COLLAPSED = 2; // 文本行数超过限定行数,处于折叠状态
	private final int STATE_EXPANDED = 3; // 文本行数超过限定行数,被点击全文展开
	private SparseArray<Integer> mTextStateList;
	private int type;//我的晒单模块1，跟单模块0
	public interface updatePraiseStatus {
		public void updateDatas();
		public void toLoginActivity();

	}
	public void clearState() {
		mTextStateList.clear();
	}

	public ShareOrderAdapter(Activity activity,updatePraiseStatus updatePraiseStatus,int type) {
		_this = activity;
		this.mPraiseStatus = updatePraiseStatus;
		mTextStateList = new SparseArray<>();
		this.type =type;

	}

	public void setDate(ArrayList<ThesunlistBean> orderListsFrom) {
		this.orderLists = orderListsFrom;
	}

	@Override
	public int getCount() {
		if (orderLists == null) {
			return 0;
		} else {
			return orderLists.size();
		}
	}

	@Override
	public ThesunlistBean getItem(int position) {
		if (orderLists == null) {
			return null;
		} else {
			return orderLists.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int Position) {
		// TODO Auto-generated method stub
		return type;
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ThesunlistBean order = orderLists.get(position);

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
			case 0:
				convertView = View.inflate(_this, R.layout.item_list_sharorder_gendan,null);
				break;
			case 1:
				convertView = View.inflate(_this, R.layout.item_list_sharorder,null);
				holder.iv_icon = (ShapedImageView) convertView.findViewById(R.id.iv_icon);
				holder.tv_item_username = (TextView) convertView.findViewById(R.id.tv_item_username);
				holder.rl_toDetail=(RelativeLayout) convertView.findViewById(R.id.rl_content);
				holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
				holder.tv_currentRate = (TextView) convertView.findViewById(R.id.tv_currentRate);
				break;
			default:
				break;
			}

			holder.tv_item_time = (TextView) convertView.findViewById(R.id.tv_item_time);
			holder.tv_praise = (TextView) convertView.findViewById(R.id.tv_praise);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.iv_praise = (ImageView) convertView.findViewById(R.id.iv_praise);
			holder.gv_photo = (ImageView) convertView.findViewById(R.id.gv_photo);
			holder.ll_praise = (LinearLayout) convertView.findViewById(R.id.ll_praise);
			holder.tv_expand_or_collapse = (TextView) convertView.findViewById(R.id.tv_expand_or_collapse);
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (order != null) {
			if(type == 1){
				xUtilsImageUtils.display(holder.iv_icon, R.mipmap.defaultniu,order.getUserImg());
				holder.tv_item_username.setText(order.getUserNama() == null ? "": order.getUserNama());
				if (TextUtils.isEmpty(order.getOrderRate())) {//本单盈利率
					holder.tv_rate.setVisibility(View.GONE);
					holder.tv_currentRate.setVisibility(View.GONE);
				}else {
					holder.tv_rate.setVisibility(View.VISIBLE);
					holder.tv_currentRate.setVisibility(View.VISIBLE);
					holder.tv_rate.setText(order.getOrderRate()==null?"":order.getOrderRate());
				}
				
				//去个人中心页面
				holder.rl_toDetail.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(_this,NiuRenActivity.class);
						intent.putExtra("id", order.getAuthId());
						intent.putExtra("arg0", 1);
						_this.startActivity(intent);
						
					}
				});
				
			}
			holder.tv_item_time.setText(order.getAddTime() == null ? "" : order.getAddTime());
			holder.tv_praise.setText(order.getViewCount() + "");

			
			
			
			int state = mTextStateList.get(position, STATE_UNKNOW);
			// 如果该item是第一次初始化，则去获取文本的行数
			if (state == STATE_UNKNOW) {
				holder.tv_content.getViewTreeObserver().addOnPreDrawListener(
						new ViewTreeObserver.OnPreDrawListener() {
							@Override
							public boolean onPreDraw() {
								// 这个回调会调用多次，获取完行数记得注销监听
								holder.tv_content.getViewTreeObserver().removeOnPreDrawListener(this);
								if (holder.tv_content.getLineCount() > MAX_LINE_COUNT) {
									holder.tv_content.setMaxLines(MAX_LINE_COUNT);
									holder.tv_expand_or_collapse.setVisibility(View.VISIBLE);
									holder.tv_expand_or_collapse.setText("全文");
									mTextStateList.put(position,STATE_COLLAPSED);
								} else {
									holder.tv_expand_or_collapse.setVisibility(View.GONE);
									mTextStateList.put(position,STATE_NOT_OVERFLOW);
								}
								return true;
							}
						});
				holder.tv_content.setMaxLines(Integer.MAX_VALUE);

				if(!TextUtils.isEmpty(order.getContent())){
					holder.tv_content.setText(order.getContent().replace("\n", "\b"));
					holder.tv_content.setVisibility(View.VISIBLE);
				}else{
					holder.tv_content.setText("");
					holder.tv_content.setVisibility(View.GONE);
				}
			} else {
				// 如果之前已经初始化过了，则使用保存的状态，无需再获取一次
				switch (state) {
				case STATE_NOT_OVERFLOW:
					holder.tv_expand_or_collapse.setVisibility(View.GONE);
					break;
				case STATE_COLLAPSED:
					holder.tv_content.setMaxLines(MAX_LINE_COUNT);
					holder.tv_expand_or_collapse.setVisibility(View.VISIBLE);
					holder.tv_expand_or_collapse.setText("全文");
					break;
				case STATE_EXPANDED:
					holder.tv_content.setMaxLines(Integer.MAX_VALUE);
					holder.tv_expand_or_collapse.setVisibility(View.VISIBLE);
					holder.tv_expand_or_collapse.setText("收起");
					break;
				}
				if(!TextUtils.isEmpty(order.getContent())){
					holder.tv_content.setText(order.getContent().replace("\n", "\b"));
					holder.tv_content.setVisibility(View.VISIBLE);
				}else{
					holder.tv_content.setText("");
					holder.tv_content.setVisibility(View.GONE);
				}
			}

			holder.tv_expand_or_collapse
			.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int state = mTextStateList.get(position,STATE_UNKNOW);
					if (state == STATE_COLLAPSED) {
						holder.tv_content.setMaxLines(Integer.MAX_VALUE);
						holder.tv_expand_or_collapse.setText("收起");
						mTextStateList.put(position, STATE_EXPANDED);
					} else if (state == STATE_EXPANDED) {
						holder.tv_content.setMaxLines(MAX_LINE_COUNT);
						holder.tv_expand_or_collapse.setText("全文");
						mTextStateList.put(position, STATE_COLLAPSED);
					}
				}
			});

			if (order.getDianzan() == 2) {// 未点赞
				holder.iv_praise.setImageResource(R.mipmap.ic_unpraise);
			} else if (order.getDianzan() == 1) {
				holder.iv_praise.setImageResource(R.mipmap.ic_praise);
			}
			holder.ll_praise.setOnClickListener(new OnClickListener() {// 点赞

				@Override
				public void onClick(View v) {
					boolean hasLongin = PreferenceUtil.getBoolean(
							_this, "hasLogin");
					if (hasLongin) {
						String theSunId = order.getTheSunId();
						ServiceShareOrder.getInstance()
						.PostPraiseOrder(_this, theSunId,
								new CallBack<String>() {

							@Override
							public void onSuccess(
									String t) {
								if (t.equals("3")) {// 成功
									holder.iv_praise.setImageResource(R.mipmap.ic_praise);
									holder.tv_praise.setText(order.getViewCount()+ 1 + "");
									order.setDianzan(1);
									order.setViewCount(order.getViewCount() + 1);
									notifyDataSetChanged();
									if (mPraiseStatus != null) {
										mPraiseStatus.updateDatas();
									}
								} else if (t.equals("1")) {// 已经点过
									order.setDianzan(1);
									holder.iv_praise.setImageResource(R.mipmap.ic_praise);
									holder.tv_praise.setText(order.getViewCount()+ "");
									notifyDataSetChanged();
								}
							}

							@Override
							public void onFailure(ErrorMsg errorMessage) {
								ToastUtils.showShort(_this,errorMessage.msg);
							}
						});
					} else {
						if (mPraiseStatus != null) {
							mPraiseStatus.toLoginActivity();
						}

					}
				}

			});

			// 处理图片路径
			if (!TextUtils.isEmpty(order.getImageurl())) {// image以;分割，需处理
				xUtilsImageUtils.display(holder.gv_photo,R.mipmap.ic_error,order.getImageurl());
				holder.gv_photo.setVisibility(View.VISIBLE);					
			} else {
				holder.gv_photo.setVisibility(View.GONE);
			}
			holder.gv_photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 查看大图？
					imageBrower(order.getImageurl());					
				}
			});
			
		}
		return convertView;

	}

	/**
	 * 打开图片查看器          
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(String url) {
		Intent intent = new Intent(_this, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URL, url);
		_this.startActivity(intent);
	}

	class ViewHolder {
		public ShapedImageView iv_icon;
		public TextView tv_item_username;
		public TextView tv_item_time;
		public ImageView iv_praise;
		public TextView tv_praise;
		public TextView tv_rate,tv_currentRate;
		public TextView tv_content, tv_expand_or_collapse;
		private LinearLayout ll_praise;
		public ImageView gv_photo;
		public RelativeLayout rl_toDetail;

	}

}
