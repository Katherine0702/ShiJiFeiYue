package com.cshen.tiyu.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

public class DistinguishQRCode {
	private static DistinguishQRCode QrCode;
	public static DistinguishQRCode getDistinguishQRCode() {
		if (QrCode == null) {
			QrCode = new DistinguishQRCode();
		}
		return QrCode;
	}

	//这种方法状态栏是空白，显示不了状态栏的信息
	public void saveCurrentImage(Activity context)  
	{  
		//Result result = null;
	//获取当前屏幕的大小
	int width = context.getWindow().getDecorView().getRootView().getWidth();
	int height = context.getWindow().getDecorView().getRootView().getHeight();
	//生成相同大小的图片
	Bitmap temBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
	//找到当前页面的根布局
	View view =  context.getWindow().getDecorView().getRootView();
	//设置缓存
	view.setDrawingCacheEnabled(true);
	view.buildDrawingCache();
	//从缓存中获取当前屏幕的图片,创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
	temBitmap = view.getDrawingCache();
	SimpleDateFormat df = new SimpleDateFormat("yyyymmddhhmmss");
	String	time = df.format(new Date());
	if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen",time + ".png");
		if(!file.exists()){
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			temBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen/" + time + ".png";
		//result = parseQRcodeBitmap(path);			
		//禁用DrawingCahce否则会影响性能 ,而且不禁止会导致每次截图到保存的是缓存的位图
		view.setDrawingCacheEnabled(false);

		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		context.sendBroadcast(intent);
	}
	//return null!=result?result.toString():"无法识别";
	}

	//解析二维码图片,返回结果封装在Result对象中  
	/*private com.google.zxing.Result  parseQRcodeBitmap(String bitmapPath){  
		//解析转换类型UTF-8  
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();  
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8");  
		//获取到待解析的图片  
		BitmapFactory.Options options = new BitmapFactory.Options();   
		//如果我们把inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String path, Options opt)  
		//并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你  
		options.inJustDecodeBounds = true;  
		//此时的bitmap是null，这段代码之后，options.outWidth 和 options.outHeight就是我们想要的宽和高了  
		Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath,options);  
		//我们现在想取出来的图片的边长（二维码图片是正方形的）设置为400像素  
		*//** 
	            options.outHeight = 400; 
	            options.outWidth = 400; 
	            options.inJustDecodeBounds = false; 
	            bitmap = BitmapFactory.decodeFile(bitmapPath, options); 
		 *//*  
		//以上这种做法，虽然把bitmap限定到了我们要的大小，但是并没有节约内存，如果要节约内存，我们还需要使用inSimpleSize这个属性  
		options.inSampleSize = options.outHeight / 400;  
		if(options.inSampleSize <= 0){  
			options.inSampleSize = 1; //防止其值小于或等于0  
		}  
		*//** 
		 * 辅助节约内存设置 
		 *  
		 * options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888 
		 * options.inPurgeable = true;  
		 * options.inInputShareable = true;  
		 *//*  
		options.inJustDecodeBounds = false;  
		bitmap = BitmapFactory.decodeFile(bitmapPath, options);   
		//新建一个RGBLuminanceSource对象，将bitmap图片传给此对象  
		RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);  
		//将图片转换成二进制图片  
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));  
		//初始化解析对象  
		QRCodeReader reader = new QRCodeReader();  
		//开始解析  
		Result result = null;  
		try {  
			result = reader.decode(binaryBitmap, hints);  
		} catch (Exception e) {  
			// TODO: handle exception  
		}  

		return result;  
	}   */
}
