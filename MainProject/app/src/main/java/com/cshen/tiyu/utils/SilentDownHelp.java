package com.cshen.tiyu.utils;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.cshen.tiyu.base.ConstantsBase;


/**
 * 默默下载的工具类
 * 
 * @author ying.zhao
 * 
 */
public class SilentDownHelp
{
	private static byte[] codes = new byte[256];
	private Thread sendUrlThread;
	private  static String  mSdCardPath;
	private static final String KEY_STORE_TYPE_BKS = "BKS";
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";
	/**
	 * 一般的instance 用于下载附件
	 */
	private static SilentDownHelp instance;
	/**
	 * 图片的下载工具类，用于静默下载图片的，和附件的下载不做冲突
	 */
	private static SilentDownHelp Imageinstance;

	/**
	 * 获取对应下载实体类
	 * 
	 * @param isImageDown
	 *            true表示图片下载，false表示文件下载
	 * @return
	 */
	public static SilentDownHelp getInstance(boolean isImageDown,String sdCardPath)
	{
		if (isImageDown)
		{
			if (Imageinstance == null)
			{
				Imageinstance = new SilentDownHelp(true);
				mSdCardPath =  sdCardPath;
			}
			return Imageinstance;
		}
		else
		{
			if (instance == null)
			{
				instance = new SilentDownHelp(false);
				mSdCardPath =  sdCardPath;

			}
			return instance;
		}



	}


	public void setmSdCardPath(String sdCardPath) {
		mSdCardPath = sdCardPath;
	}
	/**
	 * 构造函数
	 * 
	 * @param isImageDown
	 */
	private SilentDownHelp(boolean isImageDown) {
		if (isImageDown)
		{
			Imageinstance = this;
		}
		else
		{
			instance = this;
		}
	}

	/**
	 * 下载对应广告信息
	 * 
	 * @author ying.zhao
	 * 
	 */
	class MessageInfo
	{
		private Long keyID = 0L;

		boolean isFromRec;// true 表示这个文件放置到 photos 目录下

		boolean needOpen;
		public boolean isNeedOpen()
		{
			return needOpen;
		}

		public void setNeedOpen(boolean isneedOpen)
		{
			this.needOpen = isneedOpen;
		}


		public boolean isFromRec()
		{
			return isFromRec;
		}

		public void setFromRec(boolean isFromRec)
		{
			this.isFromRec = isFromRec;
		}

		public Long getKeyID()
		{
			return keyID;
		}

		public void setKeyID(Long keyID)
		{
			this.keyID = keyID;
		}

		/**
		 * 对应传递回去的imageView 用于下载图片
		 */
		private ArrayList<ImageView> image;

		public ArrayList<ImageView> getImage()
		{
			return image;
		}

		public void setImage(ArrayList<ImageView> image)
		{
			this.image = image;
		}

		/**
		 * 对应请求 从activity传递的hander
		 */
		private Handler handler;

		public Handler getHandler()
		{
			return handler;
		}

		public void setHandler(Handler handler)
		{
			this.handler = handler;
		}

		private String sendUrl;

		public String getSendUrl()
		{
			return sendUrl;
		}

		public void setSendUrl(String sendUrl)
		{
			this.sendUrl = sendUrl;
		}

		private boolean isUrl;

		public boolean isUrl()
		{
			return isUrl;
		}

		public void setUrl(boolean isUrl)
		{
			this.isUrl = isUrl;
		}

		private long attachMentId;

		public long getAttachMentId()
		{
			return attachMentId;
		}

		public void setAttachMentId(long attachMentId)
		{
			this.attachMentId = attachMentId;
		}

		public String getAttachMentName()
		{
			return attachMentName;
		}

		public void setAttachMentName(String attachMentName)
		{
			this.attachMentName = attachMentName;
		}

		private String attachMentName;

	}

	private Context context;

	public static ArrayList<MessageInfo> data = new ArrayList<SilentDownHelp.MessageInfo>();

	private byte[] synchronizedObject = new byte[] { '1' };

	Runnable sendable = new Runnable()
	{
		@Override
		public void run()
		{

			synchronized (synchronizedObject)
			{

				synchronizedObject.notifyAll();

				if (sendUrlThread == null || !sendUrlThread.isAlive())
				{

					sendUrlThread = new SendUrlthread();
					sendUrlThread.start();
				}
			}

		}
	};

	/**
	 * 是否添加过对应的附件id 用于判断是否正在下载或者在下载队列中的附件队列
	 * 
	 * @param attachMentId
	 *            附件id
	 * @return true表示已经在下载队列 不需要再下载了 false表示不在下载队列中
	 */
	public boolean isAddDownTask(long attachMentId)
	{
		if (data != null)
		{

			// int len=data.size();
			MessageInfo tempData;
			// 长度会实时变动，所以不能先获取长度
			for (int i = 0; i < data.size(); i++)
			{
				tempData = data.get(i);
				if (tempData.getAttachMentId() == attachMentId)
				{
					return true;

				}
			}
		}
		return false;
	}

	/**
	 * 是否添加过对应请求的url
	 *
	 *            对应请求的url
	 * @return true表示已经在下载队列 不需要再下载了 false表示不在下载队列中
	 */
	public boolean isAddDownTask(String url)
	{
		if (data != null)
		{

			// int len=data.size();
			MessageInfo tempData;
			// 长度会实时变动，所以不能先获取长度
			for (int i = 0; i < data.size(); i++)
			{
				tempData = data.get(i);
				// 原来的 == 改为。equals syao 2014-01-23
				if (null != tempData.getSendUrl()
						&& tempData.getSendUrl().equals(url))
				{
					return true;

				}
			}
		}
		return false;
	}

	/**
	 * 	检查文件是否存在，存在就删掉
	 */
	public void checkHas(String fileName)
	{
		boolean ret = false;
		File file = new File(mSdCardPath);
		if (!file.exists())
		{
			file.mkdirs();
		}

		File filePath = new File(mSdCardPath + fileName);

		if (null != filePath && filePath.exists())
		{
			filePath.delete();
			ret = true;
			Log.d("文件存在了，干掉", "文件" + mSdCardPath + fileName
					+ "存在了，干掉");
		}
		else
		{
			Log.d("文件不存在", "文件不存在");
		}

	}

	/**
	 *
	检查文件是否存在
	 */
	public boolean checkHasSimple(String fileName)
	{
		boolean ret = false;
		File file = new File(mSdCardPath);
		if (!file.exists())
		{
			file.mkdirs();
		}

		File filePath = new File(mSdCardPath + fileName);

		if (null != filePath && filePath.exists())
		{

			ret = true;
			Log.d("文件存在了，", "文件" + mSdCardPath + fileName
					+ "存在了");
		}
		else
		{
			Log.d("文件不存在", "文件不存在");
		}
		return ret;

	}

	/**
	 * 下载普通文件
	 * @param downId
	 * @param url
	 * @param handler
	 * @return
	 */
	public boolean  addDownFile(Long downId, String url, Handler handler,boolean needOpen,Context c)
	{

		if (data != null)
		{

			// int len=data.size();
			MessageInfo tempData;
			// 长度会实时变动，所以不能先获取长度
			for (int i = 0; i < data.size(); i++)
			{
				tempData = data.get(i);
				// 原来的 == 改为。equals syao 2014-01-23
				if (null != tempData.getSendUrl()
						&& tempData.getSendUrl().equals(url))
				{
					tempData.setHandler(handler);
					return true;

				}
			}
		}

		MessageInfo temp = new MessageInfo();
		// temp.setFileSize(fileSize);
		url = url.replace("\\", "/");
		String attachmentName = url.substring(url.lastIndexOf("/") + 1);
		temp.setAttachMentId(downId);
		temp.setAttachMentName(attachmentName);
		temp.setSendUrl(url);
		temp.setUrl(true);
		temp.setNeedOpen(needOpen);

		temp.setHandler(handler);

		checkHas(temp.getAttachMentName());

		data.add(temp);
		if (handler != null)
		{
			handler.sendEmptyMessage(ConstantsBase.FILEDOWNSTART);
		}
		this.context = c;

		new Thread(sendable).start();
		return false;

	}

	/**
	 * 仅仅是 下载  图片
	 * 
	 * @param downId
	 * @param url
	 * @param etomExchangea
	 * @param handler
	 * @param imageView
	 * @return
	 */
	public boolean addDownFile(Context context, Long downId, String url, Handler handler, ImageView imageView)
	{

		if (data != null)
		{

			// int len=data.size();
			MessageInfo tempData;
			// 长度会实时变动，所以不能先获取长度
			for (int i = 0; i < data.size(); i++)
			{
				tempData = data.get(i);
				// 原来的 == 改为。equals syao 2014-01-23
				if (null != tempData.getSendUrl()
						&& tempData.getSendUrl().equals(url))
				{
					tempData.setHandler(handler);
					return true;

				}
			}
		}

		MessageInfo temp = new MessageInfo();

		String AttachMentName = "xxx.png";

		String[] name = url.split("/");
		if (name != null && name.length > 1)
		{
			AttachMentName = name[name.length - 1];
		}

		// temp.setAttachMentName(url.replace("/Resource/UserPhoto/", "")
		// + ComConstantsNEW.ICON);

		temp.setAttachMentName(AttachMentName);
		temp.setSendUrl(url);
		temp.setAttachMentId(downId);
		temp.setUrl(true);

		ArrayList<ImageView> dataImage = new ArrayList<ImageView>();
		dataImage.add(imageView);
		temp.setImage(dataImage);
		temp.setHandler(handler);

		checkHas(temp.getAttachMentName());

		// if (checkHasSimple(temp.getAttachMentName()))
		// {
		// return true;
		// }

		data.add(temp);
		if (handler != null)
		{
			handler.sendEmptyMessage(ConstantsBase.FILEDOWNSTART);
		}
		this.context = context;

		new Thread(sendable).start();

		return false;

	}

	/**
	 * 传入对应的大小数据 里面会默认会创建线程，所以在外面不需要传递线程，如果不在队列中，则添加到下载队列中
	 * 
	 * @param attachMentId
	 *            附件id
	 * @param fileSize
	 *            附件大小 可以传递为0
	 * @param attachMentName
	 *            附件名称 必须要传递
	 * @param etomExchange
	 * @param handler
	 *            如果异常等情况发送消息请求队列
	 * @param imageView
	 *            imageView 可以不传递的
	 * @return boolean 如果返回true 表示已经在下载队列中不需要在添加了，返回false 表示没有在下载队列中，添加到下载队列
	 */
	public boolean addDownFile(long attachMentId, long fileSize,
			String attachMentName,  Handler handler,
			ImageView imageView, boolean isFromRec)
	{

		if (data != null)
		{

			// int len=data.size();
			MessageInfo tempData;
			// 长度会实时变动，所以不能先获取长度
			for (int i = 0; i < data.size(); i++)
			{
				tempData = data.get(i);
				if (tempData.getAttachMentId() == attachMentId)
				{
					tempData.setHandler(handler);
					ArrayList<ImageView> tempImage = tempData.getImage();
					tempImage.add(imageView);
					tempData.setImage(tempImage);
					return true;

				}
			}
		}

		MessageInfo temp = new MessageInfo();
		// temp.setFileSize(fileSize);
		temp.setUrl(false);
		temp.setFromRec(isFromRec);
		temp.setAttachMentId(attachMentId);
		temp.setAttachMentName(attachMentName);
		temp.setHandler(handler);
		ArrayList<ImageView> dataImage = new ArrayList<ImageView>();
		dataImage.add(imageView);
		temp.setImage(dataImage);

		temp.setKeyID(attachMentId);
		data.add(temp);
		if (handler != null)
		{
			handler.sendEmptyMessage(ConstantsBase.FILEDOWNSTART);
		}

		new Thread(sendable).start();
		return false;

	}

	MessageInfo tempMessage;

	class SendUrlthread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				synchronized (synchronizedObject)
				{
					while (true)
					{
						if (data != null && data.size() > 0)
						{
							tempMessage = data.get(0);
							InputStream input = null;
							{

								Object[] response = getImageStream(tempMessage
										.getSendUrl());
								if (response != null)
								{
									int code = (Integer) response[0];
									if (code == HttpURLConnection.HTTP_OK)
									{
										input = (InputStream) response[1];
										outFile(input,context,(Integer)response[2]);
									}else {
										String error = (String) response[1];
										android.os.Message message = new android.os.Message();
										message.what = ConstantsBase.FILEDOWNERROR;
										message.obj = new Object[] { error,
												tempMessage.getKeyID()
										};
										if(null != tempMessage.getHandler())
											tempMessage.getHandler().sendMessage(
													message);
									}
								}
								else
								{
									String error = (String) response[1];
									android.os.Message message = new android.os.Message();
									message.what = ConstantsBase.FILEDOWNERROR;
									message.obj = new Object[] { error,
											tempMessage.getKeyID()
									};
									if(null != tempMessage.getHandler())
										tempMessage.getHandler().sendMessage(
												message);
								}
								if (null != data && data.size() > 0)
								{
									data.remove(tempMessage);
								}
							}

						}

						if (data == null || data.size() == 0)
						{
							synchronizedObject.wait();
						}

					}

				}
			}
			catch (MalformedURLException e)
			{
				if (tempMessage.getHandler() != null)
				{
					android.os.Message message = new android.os.Message();
					message.what = ConstantsBase.FILEDOWNERROR;
					message.obj = new Object[] { "下载链接错误！",
							tempMessage.getKeyID() };

					tempMessage.getHandler().sendMessage(message);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();

				if (tempMessage.getHandler() != null)
				{
					String MSG = e.toString();
					if(null !=MSG&&MSG.length()>21){
						MSG = MSG.substring(21, MSG.length()-1);
					}

					android.os.Message message = new android.os.Message();
					message.what = ConstantsBase.FILEDOWNERROR;
					message.obj = new Object[] {MSG ,
							tempMessage.getKeyID() };

					tempMessage.getHandler().sendMessage(message);
				}
			}
			finally
			{
				if (null != data && data.size() > 0)
				{// 下载失败时也要将这一项移除
					data.remove(0);
				}
			}

		}
	}

	public void outFile(InputStream input,Context cont,int length) throws IOException, Exception
	{
		byte[] tmp = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int bytesRead = 0;
		int  nowlength = 0;
		while ((bytesRead = input.read(tmp)) != -1)
		{
			out = new ByteArrayOutputStream();
			out.write(tmp, 0, bytesRead);
			out.flush();
			Log.d("下载线程 打印mSdCardPath", "-->" + mSdCardPath);
			base64OrBytetoFile(null, out.toByteArray(), false,
					tempMessage.getAttachMentName(), mSdCardPath);
			nowlength+=bytesRead;
			android.os.Message message = new android.os.Message();
			message.what = ConstantsBase.FILEDOWNNOW;
			message.obj = new Object[] {
					length,nowlength};
			tempMessage.getHandler().sendMessage(message);

		}

		//打开这个文件
		String  fullfile =mSdCardPath+tempMessage.getAttachMentName();
		if(tempMessage.isNeedOpen()){
			File file = new File(fullfile);
			if (!file.exists())
			{
				return;
			}

			FileUtils.openFile(file,cont);
			return;

		}
		if (tempMessage.getHandler() != null)
		{
			if (tempMessage.getImage() == null)
			{
				Log.d("lllllll","lllllll  1111"+fullfile);
				android.os.Message message = new android.os.Message();
				message.what = ConstantsBase.FILEDOWNOK;
				message.obj = new Object[] {
						fullfile,
						tempMessage.getAttachMentName(), tempMessage.getImage(),
						tempMessage.getAttachMentId() };
				tempMessage.getHandler().sendMessage(message);
			}
			else
			{
				int len = tempMessage.getImage().size();
				for (int i = 0; i < len; i++)
				{
					Log.d("lllllll","lllllll 2222"+fullfile);
					android.os.Message message = new android.os.Message();
					message.what = ConstantsBase.FILEDOWNOK;
					message.obj = new Object[] {
							fullfile,
							tempMessage.getAttachMentName(),
							tempMessage.getImage().get(i),
							tempMessage.getAttachMentId() };
					tempMessage.getHandler().sendMessage(message);

				}
			}

		}
		out.close();
		input.close();

		data.remove(0);

	}
	/**
	 *
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public Object[] getImageStream(String path) throws Exception
	{
		//path = formatUrl(path);
		if(path.contains("https://api.fox008.com")
				||path.contains("https://data.fox008.com")){
			URL url = new URL(path);

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  
			conn.setSSLSocketFactory(HTTPS());  
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(15 * 1000);
			conn.setRequestMethod("GET");
			int fileSize = conn.getContentLength();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				return new Object[] { HttpURLConnection.HTTP_OK,
						conn.getInputStream(),fileSize };
			}
			else
			{
				return new Object[] { conn.getResponseCode(),
						"网络异常:返回码" + conn.getResponseCode(),fileSize };
			}	
		}else{
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(15 * 1000);
			conn.setRequestMethod("GET");
			int fileSize = conn.getContentLength();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				return new Object[] { HttpURLConnection.HTTP_OK,
						conn.getInputStream(),fileSize };
			}
			else
			{
				return new Object[] { conn.getResponseCode(),
						"网络异常:返回码" + conn.getResponseCode(),fileSize };
			}	
		}
	}
	public SSLSocketFactory HTTPS(){	
			InputStream trust_input = null;//服务器授信证书
			InputStream client_input = null;//服务器授信证书
			try {
				trust_input = context.getResources().getAssets().open("server.bks");
				client_input = context.getResources().getAssets().open("foxClient.p12");//客户端证书

				SSLContext sslContext = SSLContext.getInstance("TLS");

				KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
				trustStore.load(trust_input, "123456".toCharArray());

				KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
				keyStore.load(client_input, "123456".toCharArray());

				TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				trustManagerFactory.init(trustStore);

				KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				keyManagerFactory.init(keyStore,"123456".toCharArray());
				sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
				SSLSocketFactory factory = sslContext.getSocketFactory();
				return factory;

			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			/*	
		    SSLContext contextSSL = null;
		    CertificateFactory	cf = CertificateFactory.getInstance("X.509");
			InputStream in = context.getAssets().open("client.crt");  
			Certificate ca = cf.generateCertificate(in);  

			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());  
			keystore.load(null, null);  
			keystore.setCertificateEntry("ca", ca);  

			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();  
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);  
			tmf.init(keystore);  

			// Create an SSLContext that uses our TrustManager  
			contextSSL = SSLContext.getInstance("TLS");  
			contextSSL.init(null, tmf.getTrustManagers(), null);
			contextSSL..getSocketFactory() ; */
		}
		public String formatUrl(String url) throws UnsupportedEncodingException
		{

			int len = url.lastIndexOf("/");
			String dir = url.substring(0, len + 1);
			String dir2 = url.substring(len + 1, url.length());
			String dirsum = dir + URLEncoder.encode(dir2, "UTF-8");
			return dirsum;
		}

		public static void base64OrBytetoFile(String temp, byte[] item, boolean isbase64, String fileName,String filePaths) throws Exception {
			byte[] pdfAsBytes;
			if(isbase64) {
				if(temp == null) {
					temp = new String(item);
				}
				pdfAsBytes = decode(temp.toString());
			} else {
				pdfAsBytes = item;
			}

			File file = new File(filePaths);
			if(!file.exists()) {
				file.mkdirs();
			}

			File filePath = new File(filePaths + fileName);

			filePath.setWritable(Boolean.TRUE);
			FileOutputStream os = new FileOutputStream(filePath, true);
			os.write(pdfAsBytes);
			os.flush();
			os.close();
		}
		public static byte[] decode(String dataStr) {
			char[] data = dataStr.toCharArray();
			return decode(data);
		}

		public static byte[] decode(char[] data) {
			int len = (data.length + 3) / 4 * 3;
			if(data.length > 0 && data[data.length - 1] == 61) {
				--len;
			}

			if(data.length > 1 && data[data.length - 2] == 61) {
				--len;
			}

			byte[] out = new byte[len];
			int shift = 0;
			int accum = 0;
			int index = 0;

			for(int ix = 0; ix < data.length; ++ix) {
				byte value = codes[data[ix] & 255];
				if(value >= 0) {
					accum <<= 6;
					shift += 6;
					accum |= value;
					if(shift >= 8) {
						shift -= 8;
						out[index++] = (byte)(accum >> shift & 255);
					}
				}
			}

			return out;
		}

	}
