package com.cshen.tiyu.utils;



import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignatureData {

	public static String getPar() {
		try {
			String prikeyvalue = "30820154020100300d06092a864886f70d01010105000482013e3082013a0201000241009c90788ae7870fda60a0b00cb28c690cbfbe423cc38f25934a60d38558402ff92116181c38095797dfb9b33333920f85cfa0d1c32bb376361305e983d6171eed020301000102406f6eb02d052eef0c99dba491d4fef4c1db331a47cf5472050c5a30126746801d6da3cd02590c3dc0a09cb8a43ddf10a52173ed29117e8c2904672e15584511cd022100dcbdb2365b398d39ae5faf3bf2faf4d1f087adf9183634a174a75933eec96def022100b59288199567f9c282bf02549da5c58779f4674b8630ca44a29552abaa539ce3022100b0bb744eced5123c275f569681e0e95898e298a8c1f8cc44a47844142f4fb8a302206c4727d669dc897acf516ce85ce2c07adbe53dbc3217e2672fb5708962975e1502205b273116d697f997181a0ed6916ca5a6f1a8e253f46bd376f2d80e5302516da8";// 这是输出的私钥编码，不需要修改
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(hexStrToBytes(prikeyvalue));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
//			String myinfo = "SBSUserID=" + ConstantsBase.getUser().id + "&SiteID=1&mobile=" + ConstantsBase.getUser().phonenumber;
			String myinfo = "SBSUserID=1263779&SiteID=1&mobile=18913174570"; // 要签名的信息
			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature.getInstance("MD5withRSA");
			signet.initSign(myprikey);
			signet.update(myinfo.getBytes("ISO-8859-1"));
			byte[] signed = signet.sign(); // 对信息的数字签名

			
			String myinfoAES = Base64.encode(myinfo.getBytes("utf-8"));
			String myinfoafter = new String(Base64.decode(myinfoAES), "UTF-8");

			return "Value=" + (myinfoAES + "&Key=" + SignatureData.bytesToHexStr(signed));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Transform the specified byte into a Hex String form.
	 */
	public static final String bytesToHexStr(byte[] bcd) {
		StringBuffer s = new StringBuffer(bcd.length * 2);

		for (int i = 0; i < bcd.length; i++) {
			s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
			s.append(bcdLookup[bcd[i] & 0x0f]);
		}

		return s.toString();
	}

	/**
	 * Transform the specified Hex String into a byte array.
	 */
	public static final byte[] hexStrToBytes(String s) {
		byte[] bytes;

		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
					16);
		}

		return bytes;
	}

	private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		SignatureData s = new SignatureData();
//		s.run();

	}
}
