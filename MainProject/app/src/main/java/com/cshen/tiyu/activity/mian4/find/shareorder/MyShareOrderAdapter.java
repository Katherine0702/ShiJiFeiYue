package com.cshen.tiyu.activity.mian4.find.shareorder;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cshen.tiyu.R;
import com.cshen.tiyu.domain.ErrorMsg;
import com.cshen.tiyu.domain.find.MyShareOrderBean.ThesunlistBean;
import com.cshen.tiyu.net.https.ServiceABase.CallBack;
import com.cshen.tiyu.net.https.ServiceShareOrder;
import com.cshen.tiyu.net.https.xUtilsImageUtils;
import com.cshen.tiyu.utils.ToastUtils;


public class MyShareOrderAdapter extends BaseAdapter {
	private ArrayList<ThesunlistBean> orderLists;
	private Activity _this;
	// 展开阅读全文相关
	private final int MAX_LINE_COUNT = 4;
	private final int STATE_UNKNOW = -1;
	private final int STATE_NOT_OVERFLOW = 1; // 文本行数不超过限定行数
	private final int STATE_COLLAPSED = 2; // 文本行数超过限定行数,处于折叠状态
	private final int STATE_EXPANDED = 3; // 文本行数超过限定行数,被点击全文展开
	private SparseArray<Integer> mTextStateList;

	public void clearState() {
		mTextStateList.clear();
	}
	
	public MyShareOrderAdapter(Activity activity, int type) {
		mTextStateList=new SparseArray<>();
		_this = activity;
	}
	public void setDate(ArrayList<ThesunlistBean> orderListsFrom){
		this.orderLists = orderListsFrom;
	}
	@Override
	public int getCount() {
		if(orderLists == null){
			return 0; 
		}else{
			return orderLists.size();
		}
	}
	@Override
	public ThesunlistBean getItem(int position) {
		if(orderLists == null){
			return null; 
		}else{
			return orderLists.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ThesunlistBean order = orderLists.get(position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(_this, R.layout.item_list_my_shareorder,null);
			holder.tv_item_submittime = (TextView) convertView.findViewById(R.id.tv_item_submittime);
			holder.tv_item_process = (TextView) convertView.findViewById(R.id.tv_item_process);
			holder.iv_item_delete = (ImageView) convertView.findViewById(R.id.iv_item_delete);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.gv_photo = (ImageView) convertView.findViewById(R.id.gv_photo);
			holder.tv_expand_or_collapse = (TextView) convertView.findViewById(R.id.tv_expand_or_collapse);
			holder.tv_reason=(TextView) convertView.findViewById(R.id.tv_reason);//审核不通过原因
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(order!=null){
			holder.tv_content.setText(order.getContent().replace("\n", "\b"));
			holder.tv_item_submittime.setText(order.getAddTime());
			if (order.getLook_status()==0) {
				holder.tv_item_process.setText("等待审核");
				holder.tv_reason.setText("");
			}else if (order.getLook_status()==1) {
				holder.tv_item_process.setText("审核通过");
				holder.tv_reason.setText("");
			}else if (order.getLook_status()==2) {
				holder.tv_item_process.setText("审核不通过");
				holder.tv_reason.setText("原因："+order.getReason());
			}
			
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
									holder.tv_content
									.setMaxLines(MAX_LINE_COUNT);
									holder.tv_expand_or_collapse
									.setVisibility(View.VISIBLE);
									holder.tv_expand_or_collapse.setText("全文");
									mTextStateList.put(position,
											STATE_COLLAPSED);
								} else {
									holder.tv_expand_or_collapse
									.setVisibility(View.GONE);
									mTextStateList.put(position,
											STATE_NOT_OVERFLOW);
								}
								return true;
							}
						});
				holder.tv_content.setMaxLines(Integer.MAX_VALUE);
				holder.tv_content.setText(order.getContent() == null ? ""
						: order.getContent().replace("\n", "\b"));
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
				holder.tv_content.setText(order.getContent() == null ? ""
						: order.getContent().replace("\n", "\b"));
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

			
			

			//处理图片路径
			if (!TextUtils.isEmpty(order.getImageurl())) {//image以;分割，需处理
				xUtilsImageUtils.display(holder.gv_photo,R.mipmap.ic_error,order.getImageurl());
				holder.gv_photo.setVisibility(View.VISIBLE);
			}else {
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

			holder.iv_item_delete.setOnClickListener(new OnClickListener() {//删除数据

				@Override
				public void onClick(View v) {
					showDeleteDialog(position);
				}
			});
		}
		return convertView;

	}
	class ViewHolder {
		public TextView tv_item_submittime;
		public TextView tv_item_process;
		public ImageView iv_item_delete;
		public TextView tv_content,tv_expand_or_collapse,tv_reason;
		public ImageView gv_photo;

	}
	/**
	 * 返回时是否退出的提示
	 */
	private void showDeleteDialog(final int position) {
		final AlertDialog	alertDialog = new AlertDialog.Builder(_this).create();
		alertDialog.setCancelable(true);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog);
		window.findViewById(R.id.title).setVisibility(View.VISIBLE);
		TextView  title = (TextView) window.findViewById(R.id.title);
		title.setText("温馨提示");
		TextView updatesize = (TextView) window.findViewById(R.id.updatesize);
		updatesize.setText("删除本条数据？");
		TextView cancle = (TextView) window.findViewById(R.id.cancle);
		cancle.setText("取消");
		TextView ok = (TextView) window.findViewById(R.id.ok);
		ok.setText("确定");
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				deleteData(position);
			}
		});
		cancle.setOnClickListener(new View.OnClickListener() {
			/**
			 * @param v
			 */
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

	}
	/**
	 * 删除数据
	 * @param position
	 */
	public void deleteData(final int position){
		String sunId=orderLists.get(position).getTheSunId();
		ServiceShareOrder.getInstance().PostDeleteMyShareOrder(_this,sunId, new CallBack<String>() {

			@Override 
			public void onSuccess(String t) {

				orderLists.remove(position);
				notifyDataSetChanged();
				ToastUtils.showShort(_this, "删除成功");

			}

			@Override
			public void onFailure(ErrorMsg errorMessage) {
				ToastUtils.showShort(_this, errorMessage.msg);
			}
		});

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

}
