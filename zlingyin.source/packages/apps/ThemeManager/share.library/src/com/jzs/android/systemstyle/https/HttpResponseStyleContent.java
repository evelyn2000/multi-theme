package com.jzs.android.systemstyle.https;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jzs.android.systemstyle.utils.ThemeStyleOnlineInfo;

import android.text.TextUtils;

public class HttpResponseStyleContent extends HttpResponseBaseContent {
	public int mTotalCount;
//	public boolean parseResult(String result) throws JSONException{
//		return (!TextUtils.isEmpty(result) && parseResult(new JSONObject(result)));
//	}
	
	public boolean parseResult(JSONObject jo, ArrayList<ThemeStyleOnlineInfo> list) throws JSONException{
		if(super.parseResult(jo)){
			mTotalCount = 0;  
			if(jo.getInt("totalcout") > 0){
				JSONArray listArray = jo.getJSONArray("thumblist");  
				if(listArray != null){
					int length = listArray.length();
					for (int i = 0; i < length; i++) {  
                        JSONObject objectItem = (JSONObject) listArray.opt(i); 
                        ThemeStyleOnlineInfo item = ThemeStyleOnlineInfo.valueOf(objectItem);
                        if(item != null){
                        	list.add(item);
                        	mTotalCount++;
                        }
					}
				}
			}
			return true;
		}
		return false;
	}
}
