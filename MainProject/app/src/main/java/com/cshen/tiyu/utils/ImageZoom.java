package com.cshen.tiyu.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;


public class ImageZoom
{

	/**
	 * 压缩后保存图片
	 * 
	 * @param largerImagePath
	 *            原图路径
	 * @param smallImagePath
	 *            压缩后的图片路径
	 * 
	 */
	public boolean imageZoom(String largerImagePath, String smallImagePath)
	{
		if (largerImagePath.lastIndexOf(".jpg") > 0
				|| largerImagePath.lastIndexOf(".jpeg") > 0
				|| largerImagePath.lastIndexOf(".gif") > 0
				|| largerImagePath.lastIndexOf(".png") > 0
				|| largerImagePath.lastIndexOf(".bmp") > 0) {

		} else {
			return false;
		}

		// 对图片进行压缩
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 设为true，不分配内存空间

		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeFile(largerImagePath, options);// 此时返回bm为空
		if (options.outWidth <= 1536 && options.outHeight <= 1536) {
			return false;
		}

		if (options.outHeight > 1536 || options.outWidth > 1536) {
			options.inSampleSize = 2;
			bitmap = BitmapFactory.decodeFile(largerImagePath, options);// 此时返回bm为空
			if (options.outHeight > 1536 || options.outWidth > 1536) {
				options.inSampleSize = 4;
				bitmap = BitmapFactory.decodeFile(largerImagePath, options);// 此时返回bm为空
			}
		} else {
			options.inSampleSize = 2;
		}
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		try {
			// 重新读入图片，注意这次要把options.inJustDecodeBounds设为false哦
			InputStream is = new FileInputStream(largerImagePath);

			//bitmap = BitmapFactory.decodeFile(largerImagePath, options);
			bitmap =BitmapFactory.decodeStream(is,null, options);

			// 保存入sdCard
			File file2 = new File(smallImagePath); // 压缩后存到sd卡中的路径

			FileOutputStream out = new FileOutputStream(file2);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out)) {
				out.flush();
				out.close();
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			//FileLog.fLogException(e + "");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			//FileLog.fLogException(e);
			return false;
		} finally {
			if (null != bitmap && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
		return true;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight)
	{
		// 获取这个图片的宽和高
		Bitmap bitmap = null;
		try
		{
			float width = bgimage.getWidth();
			float height = bgimage.getHeight();
			// 创建操作图片用的matrix对象
			Matrix matrix = new Matrix();
			// 计算宽高缩放率
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 缩放图片动作
			matrix.postScale(scaleWidth, scaleHeight);
			bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
					(int) height, matrix, true);
		}
		catch (OutOfMemoryError e)
		{

		}
		catch (Exception e)
		{
			Log.e("",e.getMessage());
		}
		return bitmap;
	}

}
