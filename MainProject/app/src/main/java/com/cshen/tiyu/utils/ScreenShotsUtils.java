package com.cshen.tiyu.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;



/**
 * 截屏
 * 
 * @author Administrator
 * 
 */
public class ScreenShotsUtils {
	public static int sizeLimit = 100;
	public static String captureScreen(Activity activity) {
		String strImgPath = "";
		Bitmap bitmap = null;
		try{
			if(activity!=null){
				activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
				bitmap=activity.getWindow().getDecorView().getDrawingCache();
			}else{
				return strImgPath;
			}
			if (bitmap!=null) {
				strImgPath= DirsUtil.getSD_PHOTOS() + "/"+DateUtils.getNowDate("yyyyMMddHHmmssSSS") + ".jpg";// 存放照片的文件夹
				File file=new File(strImgPath);//将要保存图片的路径 
				try { 
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)); 
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); 
					bos.flush(); 
					bos.close(); 
				} catch (IOException e) { 
					e.printStackTrace(); 
				} 
			}
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		return strImgPath;
	}

	/**
	 * 截屏view
	 * @param v
	 * @return
	 */
	public static String captureView(View v) {
		String strImgPath="";
		try {
			int w = v.getWidth();
			int h = v.getHeight();

			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
			Canvas c = new Canvas(bitmap);     
			c.drawColor(Color.WHITE);
			v.layout(0, 0, w, h);
			v.draw(c);
			if (bitmap!=null) {
				strImgPath= DirsUtil.getSD_PHOTOS() + "/"+DateUtils.getNowDate("yyyyMMddHHmmssSSS") + ".jpg";// 存放照片的文件夹
				File file=new File(strImgPath);//将要保存图片的路径 
				try { 
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)); 
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); 
					bos.flush(); 
					bos.close(); 
				} catch (IOException e) { 
					e.printStackTrace(); 
					return "";
				} 
			}
		} catch (OutOfMemoryError error) {
			error.printStackTrace();
			return null;
		}
		return strImgPath;
	}
	/**
	 * 截屏view加图尾
	 * @param v
	 * @return
	 */
	/*public static String captureView(View v,Bitmap baseHead) {
		  String strImgPath="";
      try {
          int w = v.getWidth();
          int h1 = v.getHeight();
          int h2 = baseHead.getHeight();
          int h = h1+h2;

          Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);//创建一个两个图高的画框
          Canvas c = new Canvas(bitmap);    //画框上挂上画布
          c.drawColor(Color.WHITE);
          v.layout(0, 0, w, h1);
          v.draw(c);//画布上从左边0，上边h2的地方画上主画
          c.drawBitmap(baseHead, 0, h1, null);  //画布上从左边0，上边0的地方画上画头
          if (bitmap!=null) {
  			strImgPath= DirsUtil.getSD_PHOTOS() + "/"+DateUtils.getNowDate("yyyyMMddHHmmssSSS") + ".jpg";// 存放照片的文件夹
  			File file=new File(strImgPath);//将要保存图片的路径 
  			try { 
  				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)); 
  				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); 
  				bos.flush(); 
  				bos.close(); 
  			} catch (IOException e) { 
  				e.printStackTrace(); 
  				return "";
  			} 
 		}
      } catch (OutOfMemoryError error) {
          error.printStackTrace();
          return null;
      }
      return strImgPath;
  }*/


	//放在截图尾巴上可以直接加，放在头上就不行；
	public static String captureView(View v,Bitmap bitmapHead) {
		String strImgPath=""; 
		Bitmap bitmapTail = null ;
		Canvas cTail = null;
		Bitmap bitmap = null ;
		Bitmap newBitmapHead = null;
		Canvas c = null;
		try {
			int w1 = v.getWidth();
			int h1 = v.getHeight();

			int w2 = bitmapHead.getWidth();
			int h2 = bitmapHead.getHeight();

			float bili = ((float) w1) / w2;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(bili, bili);
			newBitmapHead = Bitmap.createBitmap(bitmapHead, 0, 0, w2, h2, matrix, true);

			int w3 = newBitmapHead.getWidth();
			int h3 = newBitmapHead.getHeight();

			int w = w1;
			int h = h1+h3;

			bitmapTail = Bitmap.createBitmap(w, h1, Bitmap.Config.RGB_565);//创建一个两个图高的画框
			cTail = new Canvas(bitmapTail);    //画框上挂上画布
			cTail.drawColor(Color.WHITE);
			v.layout(0, 0, w, h1);
			v.draw(cTail);//画布上从左边0，上边h2的地方画上主画

			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);//创建一个两个图高的画框
			c = new Canvas(bitmap);    //画框上挂上画布
			c.drawColor(Color.WHITE);

			// 取得想要缩放的matrix参数
			c.drawBitmap(newBitmapHead,0,0, null);  
			c.drawBitmap(bitmapTail, 0,h3, null); 
			if (bitmap!=null) {
				strImgPath= DirsUtil.getSD_PHOTOS() + "/"+DateUtils.getNowDate("yyyyMMddHHmmssSSS") + ".jpg";// 存放照片的文件夹
				File file=new File(strImgPath);//将要保存图片的路径 
				try { 
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)); 
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); 
					bos.flush(); 
					bos.close(); 
				} catch (IOException e) { 
					e.printStackTrace(); 
					return "";
				} 
			}
		} catch (OutOfMemoryError error) {
			error.printStackTrace();
			return null;
		}finally{
			if(bitmapTail != null ){  
				cTail = null;  
				if(!bitmapTail.isRecycled()){
					bitmapTail.recycle();
				}  
				bitmapTail = null;  
			}
			if(bitmap != null ){  
				c = null;  
				if(!bitmap.isRecycled()){
					bitmap.recycle();
				}  
				bitmap = null;  
			}
		}
		return strImgPath;
	}

	public static Bitmap captureViewBitmap(View v,Bitmap bitmapHead) {
		Bitmap bitmapTail = null ;
		Canvas cTail = null;
		Bitmap bitmap = null ;
		Canvas c = null;
		try {
			int w = v.getWidth();
			int h1 = v.getHeight();
			int h2 = bitmapHead.getHeight();
			int h = h1+h2;

			bitmapTail = Bitmap.createBitmap(w, h1, Bitmap.Config.RGB_565);//创建一个两个图高的画框
			cTail = new Canvas(bitmapTail);    //画框上挂上画布
			cTail.drawColor(Color.WHITE);
			v.layout(0, 0, w, h1);
			v.draw(cTail);//画布上从左边0，上边h2的地方画上主画

			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);//创建一个两个图高的画框
			c = new Canvas(bitmap);    //画框上挂上画布
			c.drawColor(Color.WHITE);
			c.drawBitmap(bitmapHead, 0, 0, null);  //画布上从左边0，上边0的地方画上画头
			c.drawBitmap(bitmapTail, 0, h2, null);  //画布上从左边0，上边0的地方画上画头
		} catch (OutOfMemoryError error) {
			error.printStackTrace();
		}finally{
			if(bitmapTail != null ){  
				cTail = null;  
				if(!bitmapTail.isRecycled()){
					bitmapTail.recycle();
				}  
				bitmapTail = null;  
			}
		}
		return bitmap;
	}
	/**
	 * 生成某个LinearLayout的图片
	 *
	 * @author gengqiquan
	 * @date 2017/3/20 上午10:34
	 */
	public static String getLinearLayoutBitmap(LinearLayout linearLayout) {
		int h = 0;
		String strImgPath = "";
		Bitmap bitmap = null;
		// 获取LinearLayout实际高度
		for (int i = 0; i < linearLayout.getChildCount(); i++) {
			linearLayout.getChildAt(i).measure(0, 0);
			h += linearLayout.getChildAt(i).getMeasuredHeight();
		}
		linearLayout.measure(0, 0);
		// 创建对应大小的bitmap
		bitmap = Bitmap.createBitmap(linearLayout.getMeasuredWidth(), h,
				Bitmap.Config.RGB_565);
		final Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		linearLayout.draw(canvas);
		if (bitmap!=null) {
			strImgPath= DirsUtil.getSD_PHOTOS() + "/"+DateUtils.getNowDate("yyyyMMddHHmmssSSS") + ".jpg";// 存放照片的文件夹
			File file=new File(strImgPath);//将要保存图片的路径 
			try { 
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)); 
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); 
				bos.flush(); 
				bos.close(); 
			} catch (IOException e) { 
				e.printStackTrace(); 
				return "";
			} 
		}
		return strImgPath;
	}


	public static String create(View v) {
		String strImgPath="";
		try {
			int w = v.getWidth();
			int h = v.getHeight();

			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
			Canvas c = new Canvas(bitmap);
			c.drawColor(Color.WHITE);
			v.layout(0, 0, w, h);
			v.draw(c);
			if (bitmap!=null) {
				strImgPath= DirsUtil.getSD_PHOTOS() + "/"+DateUtils.getNowDate("yyyyMMddHHmmssSSS") + ".jpg";// 存放照片的文件夹
				File file=new File(strImgPath);//将要保存图片的路径 
				try { 
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)); 
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); 
					bos.flush(); 
					bos.close(); 
				} catch (IOException e) { 
					e.printStackTrace(); 
					return "";
				} 
			}
		} catch (OutOfMemoryError error) {
			error.printStackTrace();
			return null;
		}
		return strImgPath;
	}



	public  static String toZoom(String fileAllPath) {
		String filePath = fileAllPath;
		if(TextUtils.isEmpty(fileAllPath)){
			return "";
		}
		String[] names = fileAllPath.split("\\.");
		StringBuffer small = new StringBuffer();
		if (names.length > 0) {
			String ex = names[names.length - 1];
			small = small.append(fileAllPath.substring(0,fileAllPath.length() - ex.length() - 1)).append("_Small.").append(ex);
			boolean isZoom = false;
			if (null != fileAllPath) {
				isZoom = yasuo(fileAllPath,small.toString());
			}
			if (isZoom) {
				deleteFile(fileAllPath);
				filePath = small.toString();				
			}
		}

		return filePath;
	}
	public static  boolean yasuo(String largerImagePath,String smallImagePath){
		//将文件变成bitmap
		InputStream is = null;
		Bitmap bitmap = null;
		ByteArrayOutputStream baos = null; 
		//将文件变成bitmap
		File file = null; 
		FileOutputStream  fos = null;
		BufferedOutputStream bufferedOutputStream = null;

		int quality = 100;
		int qualityMinus = 5;
		try {
			//将文件读入图片
			is = new FileInputStream(largerImagePath);
			byte [] data=readStream(is);  
			if (data!= null ){  
				bitmap = BitmapFactory.decodeByteArray(data, 0 , data.length);  
			} 
			baos = new ByteArrayOutputStream(); 
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			int base =  baos.toByteArray().length / 1024;
			System.out.println("file1.完整文件 ="+base);	
			if(base<5000&&base>1000){
				qualityMinus = 20;
			}else if(base>5000){
				qualityMinus = 50;
			}
			// 循环判断压缩后图片是否超过限制大小
			while(baos.toByteArray().length / 1024 > sizeLimit) {
				System.out.println("file1.压缩A ="+baos.toByteArray().length / 1024+",quality = "+quality );
				// 清空baos
				if (quality == 0)//如果图片的质量已降到最低则，不再进行压缩
					break;
				quality -= qualityMinus;
				baos.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
				System.out.println("file1.压缩B="+baos.toByteArray().length / 1024 );
			}

			//将图片读入文件
			file = new File(smallImagePath); 
			fos = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(fos);
			bufferedOutputStream.write(baos.toByteArray());
			bufferedOutputStream.flush();
			System.out.println("file1.压缩后 ="+file.length()/ 1024);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != bitmap && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
		return true;

	}
	/*  
	 * 得到图片字节流 数组大小  
	 * */   
	public   static   byte [] readStream(InputStream inStream)  throws  Exception{        
		ByteArrayOutputStream outStream = new  ByteArrayOutputStream();        
		byte[] buffer =  new  byte [1024];        
		int  len =  0 ;        
		while ( (len=inStream.read(buffer)) != - 1 ){        
			outStream.write(buffer, 0 , len);        
		}        
		outStream.close();        
		inStream.close();        
		return  outStream.toByteArray();        
	}  
	
	public static void deleteFilesByDirectory(String directoryPath)
	{
		File directory = new File(directoryPath);
		if (directory != null && directory.exists() && directory.isDirectory()
				&&directory.listFiles()!=null&&directory.listFiles().length>0)
		{
			for (File item : directory.listFiles())
			{
				item.delete();
			}
		}
	}
	public static void deleteFile(String filePath)
	{
		File file = new File(filePath);
		if (file != null && file.exists()&&file.isFile())
		{
			file.delete();

		}
	}
}
