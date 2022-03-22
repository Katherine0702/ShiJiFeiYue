package com.cshen.tiyu.utils;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {
	/**
	 * 将Bitmap转换为Base64字符串
	 * @param bm
	 * @return
	 */
	public static String BitmapToBase64String(Bitmap bitmap){
		if(bitmap == null){
			return "";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();    
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return new String(Base64.encode(baos.toByteArray()));
	}  
	/**
	 * 将Base64字符串转换为Bitmap
	 * @param bm
	 * @return
	 */
	public static Bitmap Base64StringToBimap(String base64String){ 
		if(base64String == null){
			return null;
		}
		byte[] bytes = Base64.decode(base64String);
		if(bytes != null && bytes.length != 0){  
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  
		}  
		return null;
	}  


	/** 
	 * @param 将图片内容解析成字节数组 
	 * @param inStream 
	 * @return byte[] 
	 * @throws Exception 
	 */  
	public static byte[] readStream(InputStream inStream) throws Exception {  
		byte[] buffer = new byte[1024];  
		int len = -1;  
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		while ((len = inStream.read(buffer)) != -1) {  
			outStream.write(buffer, 0, len);  
		}  
		byte[] data = outStream.toByteArray();  
		outStream.close();  
		inStream.close();  
		return data;  

	}  
	public static  Bitmap setImageOptions(String path)
	{
		Bitmap imageBitmap = null;
		try
		{

			BitmapFactory.Options opt = new BitmapFactory.Options();
			File file = new File(path);
			long fileSize = file.length();
			int maxSize = 500 * 1024;//500k
			if(fileSize <= maxSize){//小于500k就保持原大小
				opt.inSampleSize = 1;
			}else{
				long times = fileSize / maxSize;
				opt.inSampleSize = (int)(Math.log(times) / Math.log(2.0)) + 1; //Math.log返回以e为底的对数</strong>
			}

			imageBitmap = BitmapFactory.decodeFile(path, opt);



		}
		catch (OutOfMemoryError e)
		{
			//FileLog.fLogException(e + "");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			//FileLog.fLogException(e + "");
			e.printStackTrace();
		}

		return imageBitmap;

	}

}
