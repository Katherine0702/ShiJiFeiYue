package com.bojing.gathering.util;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5SignUtil {

	/**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara,String key) {
    	String prestr = createLinkString(paraFilter(sPara)); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = MD5SignUtil.sign(prestr, "&key=" + key, "utf-8");
        return mysign;
    }
	
	/**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara,String key,String joinLabel) {
    	String prestr = createLinkString(paraFilter(sPara),joinLabel); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = MD5SignUtil.sign(prestr, "&key=" + key, "utf-8");
        return mysign;
    }
	
	 /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equals("signKey")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    
	  /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params,String joinLabel) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + joinLabel;
            }
        }

        return prestr;
    }
    public static String createLinkString(Map<String, String> params) {
    	return createLinkString(params,"&");
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text + key;
        return  new String(Hex.encodeHex(DigestUtils.md5(getContentBytes(text, input_charset))));
       // return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

	public static String buildSign(Map<String, Object> sPara, String key) {
    	String prestr = createLinkString(para(sPara)); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = MD5SignUtil.sign(prestr, "&key=" + key, "utf-8");
        return mysign;
    }
	
    public static Map<String, String> para(Map<String, Object> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key).toString();
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equals("signKey")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }
    
	public static Map<String,String> str2Map(String urlStr) {
    	Map<String,String> paramMap = new TreeMap<String,String>();
    	String[] paramArray = urlStr.split("&");
    	if((null != paramArray) && paramArray.length>0){
    		for(String param : paramArray){
    			String[] pArr = param.split("=");
    			if(null != pArr && pArr.length>1){
    				//paramMap.put(pArr[0], pArr[1]);
    				paramMap.put(pArr[0], param.substring(param.indexOf("=")+1));
    			}
    		}
    	}
    	return paramMap;
	}
}
