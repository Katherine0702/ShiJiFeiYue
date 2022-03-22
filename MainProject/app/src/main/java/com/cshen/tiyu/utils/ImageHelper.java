package com.cshen.tiyu.utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class ImageHelper {
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
				.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap getBitmapByPix(Bitmap bm,Context context) {

		// 获得图片的宽高    
		int width = bm.getWidth();    
		int height =bm.getHeight();
		Log.e("bm.getWidth()", bm.getWidth()+"");
		// 设置想要的大小    
		int newWidth = dip2px(context, 66);    
		int newHeight = dip2px(context, 66);//66dp是自己想要的大小，大家随意


		// 计算缩放比例    
		float scaleWidth = ((float) newWidth) / width;    
		float scaleHeight = ((float) newHeight) / height;

		// 取得想要缩放的matrix参数    
		Matrix matrix = new Matrix();    
		matrix.postScale(scaleWidth, scaleHeight);

		// 得到新的图片    
		return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);


	}

	/** 
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	 */  
	public static int dip2px(Context context, float dpValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  

		return (int) (dpValue * scale + 0.5f);  
	}  

	/** 
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	 */  
	public static int px2dip(Context context, float pxValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (pxValue / scale + 0.5f);  
	}  
	public static Bitmap createCircleImage(Bitmap source, int min)  
	{  
		final Paint paint = new Paint();  
		paint.setAntiAlias(true);  
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);  
		/** 
		 * 产生一个同样大小的画布 
		 */  
		Canvas canvas = new Canvas(target);  
		/** 
		 * 首先绘制圆形 
		 */  
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);  
		/** 
		 * 使用SRC_IN 
		 */  
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
		/** 
		 * 绘制图片 
		 */  
		canvas.drawBitmap(source, 0, 0, paint);  
		return target;  
	}  
    /* 转换图片成圆形
    * @param bitmap 传入Bitmap对象
    * @return
    */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
           int width = bitmap.getWidth();
           int height = bitmap.getHeight();
           float roundPx;
           float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
           if (width <= height) {
                   roundPx = width / 2;
                   top = 0;
                   bottom = width;
                   left = 0;
                   right = width;
                   height = width;
                   dst_left = 0;
                   dst_top = 0;
                   dst_right = width;
                   dst_bottom = width;
           } else {
                   roundPx = height / 2;
                   float clip = (width - height) / 2;
                   left = clip;
                   right = width - clip;
                   top = 0;
                   bottom = height;
                   width = height;
                   dst_left = 0;
                   dst_top = 0;
                   dst_right = height;
                   dst_bottom = height;
           }
            
           Bitmap output = Bitmap.createBitmap(width,
                           height, Config.ARGB_8888);
           Canvas canvas = new Canvas(output);
            
           final int color = 0xff424242;
           final Paint paint = new Paint();
           final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
           final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
           final RectF rectF = new RectF(dst);

           paint.setAntiAlias(true);
            
           canvas.drawARGB(0, 0, 0, 0);
           paint.setColor(color);
           canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

           paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
           canvas.drawBitmap(bitmap, src, dst, paint);
           return output;
   }
}
