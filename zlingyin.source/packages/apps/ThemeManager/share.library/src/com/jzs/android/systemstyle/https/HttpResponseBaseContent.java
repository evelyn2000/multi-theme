package com.jzs.android.systemstyle.https;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class HttpResponseBaseContent {
	public int mStatus;
	public int mDeviceId;
	
	public HttpResponseBaseContent(){
		
	}
	
	public HttpResponseBaseContent(String result) throws JSONException {
		if(!TextUtils.isEmpty(result)){
			if(!parseResult(result)){
				mDeviceId = DownloadInfo.UNKNOWN_DEVICES_ID;
			}
		} else {
			mStatus = HttpManager.HTTP_RESPONSE_EMPTY_RESULT;
			mDeviceId = DownloadInfo.UNKNOWN_DEVICES_ID;
		}
	}
	
	public boolean parseResult(String result) throws JSONException {
		return (!TextUtils.isEmpty(result) && parseResult(new JSONObject(result)));
	}
	
	public boolean parseResult(JSONObject jo) throws JSONException{
		mStatus = jo.getInt("status");
		if(isResponseSuccess()){
			if(jo.has("deviceid"))
				mDeviceId = jo.getInt("deviceid");
			else
				mDeviceId = DownloadInfo.UNKNOWN_DEVICES_ID;
			
			return true;
		}
		return false;
	}
	
	public boolean isResponseSuccess(){
		return (mStatus == HttpManager.HTTP_RESPONSE_SUCCESS);
	}
}
