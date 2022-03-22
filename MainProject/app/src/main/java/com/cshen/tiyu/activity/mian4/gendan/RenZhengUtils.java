package com.cshen.tiyu.activity.mian4.gendan;

import com.cshen.tiyu.R;

public class RenZhengUtils {
	private static RenZhengUtils rzutils;
	public static RenZhengUtils getRenZhengUtils() {
		if (rzutils == null) {
			rzutils = new RenZhengUtils();
		}
		return rzutils;
	}
	public int getV(String level){
		int url = R.mipmap.v1;
		switch(level){
		case "M":
			url = R.mipmap.v1;
			break;
		case "LEVEL1":
			url = R.mipmap.v1;
			break;
		case "LEVEL2":
			url = R.mipmap.v2;
			break;
		case "LEVEL3":
			url = R.mipmap.v3;
			break;
		case "LEVEL4":
			url = R.mipmap.v4;
			break;
		case "LEVEL5":
			url = R.mipmap.v5;
			break;
		default:
			url = 0;
			break;
		}
		return url;
	}
}
