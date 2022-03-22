package com.cshen.tiyu.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by lingcheng on 15/10/2.
 */
public class NewToast {

	protected static Toast toast = null;

	public static void makeText(Context context, int resId) {
		makeText(context, context.getString(resId), Toast.LENGTH_SHORT);
	}

	public static void makeText(Context context, String str) {
		makeText(context, str, Toast.LENGTH_SHORT);
	}

	/**
	 * 文字的弹框 *
	 */
	public static void makeText(Context context, String s, int duration) {
		try {
			if (null == context || null == s) {
				return;
			}
//			View v = LayoutInflater.from(context).inflate(
//					R.layout.toast_layout, null);
//			LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);
////			layout.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.dp10) * 12;
//			layout.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.dp10) * 7;
//			layout.setMinimumWidth(context.getResources().getDimensionPixelSize(R.dimen.dp10) * 12);
//			TextView txt = (TextView) v.findViewById(R.id.text_message);
//			txt.setText(s);

			if (toast == null) {
				toast = Toast.makeText(context, s, duration);
			}
			toast.setText(s);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.setView(v);
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * 带菊花的弹框 *
//	 */
//	public static void makeProgress(Context context, String s, int duration) {
//		try {
//			if (null == context || null == s) {
//				return;
//			}
//			View v = LayoutInflater.from(context).inflate(
//					R.layout.toast_layout, null);
//			LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);
//			layout.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.dp10) * 12;
//			layout.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.dp10) * 7;
//			TextView txt = (TextView) v.findViewById(R.id.text_message);
//			FontManager.changeFonts(txt, context);
//			txt.setText(s);
//
//			if (toast == null) {
//				toast = Toast.makeText(context, s, duration);
//			}
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.setView(v);
//			toast.show();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 带图片的弹框 默认图片imgId = 0*
//	 */
//	public static void makeImage(Context context, String s, int imgId, int duration) {
//		try {
//			if (null == context || null == s) {
//				return;
//			}
//			View v = LayoutInflater.from(context).inflate(
//					R.layout.toast_image_layout, null);
//			LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);
//			layout.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.dp10) * 12;
//			layout.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.dp10) * 7;
//			TextView txt = (TextView) v.findViewById(R.id.text_message);
//			FontManager.changeFonts(txt, context);
//			txt.setText(s);
//			if (imgId != 0) {
//				ImageView image_message = (ImageView) v.findViewById(R.id.image_message);
//				image_message.setImageResource(imgId);
//			}
//
//			if (toast == null) {
//				toast = Toast.makeText(context, s, duration);
//			}
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.setView(v);
//			toast.show();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//

	/**
	 * 带菊花的对话框 *
	 */
	public static Dialog makeDialog(final Context context, String msg, boolean cancelable, DialogInterface.OnCancelListener listener) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.toast_progress_layout, null);
//
//		LinearLayout dialog_view = (LinearLayout) v.findViewById(R.id.layout);
//		dialog_view.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.dp10) * 12;
//		dialog_view.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.dp10) * 7;
//		TextView txt = (TextView) v.findViewById(R.id.text_message);
//		if (!TextUtils.isEmpty(msg))
//			txt.setText(msg);
//		final Dialog loadingDialog = new Dialog(context, R.style.dialog);
//		loadingDialog.setCancelable(cancelable);
//		loadingDialog.setCanceledOnTouchOutside(false);
//		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT));
		final Dialog loadingDialog = ProgressDialog.show(context, "", "正在加载，请稍候...");
//		dialog_view.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					if (loadingDialog != null && loadingDialog.isShowing() && !((Activity) context).isFinishing()) {
//						loadingDialog.dismiss();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				try {
					if (loadingDialog != null && loadingDialog.isShowing() && !((Activity) context).isFinishing()) {
						loadingDialog.dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				try {
					if (loadingDialog != null && loadingDialog.isShowing() && !((Activity) context).isFinishing()) {
						loadingDialog.dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		if (listener != null)
			loadingDialog.setOnCancelListener(listener);
		return loadingDialog;
	}
//
//	/**
//	 * 带选择的对话框 默认图片imgId = 0*
//	 */
//	public static Dialog makeAlertDialog(final Context context, String msg, int imgId, boolean cancelable,
//	                                     final DialogInterface.OnCancelListener listener,
//	                                     final NewToast.OnSubmitClickListener submitListener) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.toast_alert_layout, null);
//
//		LinearLayout dialog_view = (LinearLayout) v.findViewById(R.id.layout);
//		dialog_view.getLayoutParams().width = App.getInstance().getSizeUtil().getScreenWidth() - context.getResources().getDimensionPixelSize(R.dimen.dp10) * 5;
//		TextView txt = (TextView) v.findViewById(R.id.text_message);
//		if (!TextUtils.isEmpty(msg))
//			txt.setText(msg);
//		ImageView img_icon = (ImageView) v.findViewById(R.id.img_icon);
//		if (imgId != 0)
//			img_icon.setImageResource(imgId);
//		Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
//		Button btn_submit = (Button) v.findViewById(R.id.btn_submit);
//
//		final Dialog loadingDialog = new Dialog(context, R.style.Theme_MyDialog);
//		loadingDialog.setCancelable(cancelable);
//		loadingDialog.setCanceledOnTouchOutside(false);
//		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT));
//		btn_submit.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				try {
//					if (loadingDialog != null && loadingDialog.isShowing() && !((Activity) context).isFinishing()) {
//						loadingDialog.dismiss();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if (submitListener != null) {
//					submitListener.submitClick();
//				}
//			}
//		});
//		btn_cancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if (listener != null)
//					loadingDialog.setOnCancelListener(listener);
//				try {
//					if (loadingDialog != null && loadingDialog.isShowing() && !((Activity) context).isFinishing()) {
//						loadingDialog.dismiss();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		dialog_view.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					if (loadingDialog != null && loadingDialog.isShowing() && !((Activity) context).isFinishing()) {
//						loadingDialog.dismiss();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		return loadingDialog;
//	}
//
//	private static OnSubmitClickListener OnSubmitClickListener;
//	public interface OnSubmitClickListener {
//		void submitClick();
//	}
//
//	/**
//	 * titlebar下的tip提示,2秒后消失*
//	 */
//	public static void makeTips(final Context context, String tips) {
//		int w = App.getInstance().getSizeUtil().getScreenWidth();
//		int h = App.getInstance().getSizeUtil().dip2px(30);
//		int oy = context.getResources().getDimensionPixelOffset(R.dimen.dp44)
//				+ App.getInstance().getSizeUtil().getBarHeight();
//		View layout = LayoutInflater.from(context).inflate(
//				R.layout.toast_tips_layout, null);
//		TextView txt_tips = (TextView) layout.findViewById(R.id.txt_tips);
//		txt_tips.setText(tips);
//		final PopupWindow window = new PopupWindow(layout, w, h);
//		window.setContentView(layout);
//		window.setFocusable(false);
////		window.setBackgroundDrawable(new BitmapDrawable()); // 响应返回键必须的语句
//		window.setOutsideTouchable(true);
//		window.setAnimationStyle(R.style.popwin_tip_anim_style);
//		layout.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				window.dismiss();
//			}
//		});
//		window.showAtLocation(((Activity) context).getCurrentFocus(), Gravity.TOP, 0, oy);
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				if (!((Activity) context).isFinishing())
//					window.dismiss();
//			}
//		}, 2000);
//	}
//
//	/**
//	 * titlebar下的tip提示,不消失*
//	 */
//	public static PopupWindow makePopup(final Context context, String tips, boolean focus) {
//		int w = App.getInstance().getSizeUtil().getScreenWidth();
//		int h = App.getInstance().getSizeUtil().dip2px(30);
//		int oy = context.getResources().getDimensionPixelOffset(R.dimen.dp44)
//				+ App.getInstance().getSizeUtil().getBarHeight();
//		View layout = LayoutInflater.from(context).inflate(
//				R.layout.toast_tips_layout, null);
//		TextView txt_tips = (TextView) layout.findViewById(R.id.txt_tips);
//		txt_tips.setText(tips);
//		final PopupWindow window = new PopupWindow(layout, w, h);
//		window.setContentView(layout);
//		window.setFocusable(focus);
//		window.setBackgroundDrawable(new BitmapDrawable()); // 响应返回键必须的语句
//		window.setOutsideTouchable(true);
//		window.setAnimationStyle(R.style.popwin_tip_anim_style);
//		layout.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				window.dismiss();
//			}
//		});
//		window.showAtLocation(((Activity) context).getCurrentFocus(), Gravity.TOP, 0, oy);
//		return window;
//	}
//
//	/**
//	 * titlebar下的refresh页面*
//	 */
//	public static PopupWindow makeRefreshPopup(final Context context, final boolean focus, final NewToast.OnRefreshClickListener refreshListener) {
//		int w = App.getInstance().getSizeUtil().getScreenWidth();
//		int h = App.getInstance().getSizeUtil().getScreenHeight() - context.getResources().getDimensionPixelOffset(R.dimen.dp44);
//		final int oy = context.getResources().getDimensionPixelOffset(R.dimen.dp44)
//				+ App.getInstance().getSizeUtil().getBarHeight();
//		View layout = LayoutInflater.from(context).inflate(
//				R.layout.toast_refresh_layout, null);
//		final LinearLayout layout_refresh = (LinearLayout) layout.findViewById(R.id.layout_refresh);
//		layout_refresh.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if (refreshListener != null)
//					refreshListener.refreshClick();
//			}
//		});
//		final PopupWindow window = new PopupWindow(layout, w, h);
//		window.setContentView(layout);
//		window.setFocusable(false);
////		window.setBackgroundDrawable(new BitmapDrawable()); // 响应返回键必须的语句
//		window.setOutsideTouchable(false);
//		window.showAtLocation(((Activity) context).getCurrentFocus(), Gravity.TOP, 0, oy);
//		return window;
//	}
//
//	/**
//	 * titlebar下的refresh页面*
//	 */
//	public static PopupWindow makeRefreshLayout(final Context context, final View body, final NewToast.OnRefreshClickListener refreshListener) {
//		int w = App.getInstance().getSizeUtil().getScreenWidth();
//		int h = App.getInstance().getSizeUtil().getScreenHeight() - context.getResources().getDimensionPixelOffset(R.dimen.dp44);
//		final int oy = context.getResources().getDimensionPixelOffset(R.dimen.dp44)
//				+ App.getInstance().getSizeUtil().getBarHeight();
//		View layout = LayoutInflater.from(context).inflate(
//				R.layout.toast_refresh_layout, null);
//		final LinearLayout layout_refresh = (LinearLayout) layout.findViewById(R.id.layout_refresh);
//		layout_refresh.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if (refreshListener != null)
//					refreshListener.refreshClick();
//			}
//		});
//		final PopupWindow window = new PopupWindow(layout, w, h);
//		return window;
//	}

	private static OnRefreshClickListener OnRefreshClickListener;

	public interface OnRefreshClickListener {
		void refreshClick();
	}
}