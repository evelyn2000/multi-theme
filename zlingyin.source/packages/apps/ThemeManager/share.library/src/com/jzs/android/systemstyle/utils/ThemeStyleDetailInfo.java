package com.jzs.android.systemstyle.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class ThemeStyleDetailInfo {
	public String mExplain;
	public String mIconUrl;
	
	public ThemeStyleDetailInfo(String img, String explain){
		mExplain = explain;
		mIconUrl = img; 
	}
	
	public static ThemeStyleDetailInfo valueOf(JSONObject jo) throws JSONException {
		if(jo != null){
	    	String iconsrc = jo.getString("iconsrc");
	    	if(!TextUtils.isEmpty(iconsrc)){
		    	String title = jo.getString("title");
		    	return new ThemeStyleDetailInfo(iconsrc, title);
	    	}
		}
    	
		return null;
	}
}
