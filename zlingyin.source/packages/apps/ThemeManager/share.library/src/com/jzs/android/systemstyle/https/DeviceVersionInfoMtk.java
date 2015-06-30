package com.jzs.android.systemstyle.https;

import com.jzs.android.systemstyle.utils.Util;

import android.content.Context;
//import android.os.SystemProperties;

public class DeviceVersionInfoMtk extends DeviceVersionInfo {
	public String mRo_operator_optr;
	
	protected DeviceVersionInfoMtk(Context context, int cpuVendor){
		super(context, cpuVendor);
		//mPlatformVendor = PLATFORM_VENDOR_MTK;
		
		mRo_operator_optr = Util.getPropString("ro.operator.optr");
		
		mPlatform = Util.getPropString("ro.mediatek.platform");
		
		mSimCardCount = Util.getPropBoolean("ro.mediatek.gemini_support") ? 2 : 1;
	}
	
	@Override
	public String getVersionString(){
    	StringBuilder builder = new StringBuilder();
    	
    	builder.append(super.getVersionString());
    	builder.append("_").append(mRo_operator_optr.replaceAll("_", "\\$"));
    	
    	return builder.toString();
    }
	
	@Override
	public String toString(){
    	StringBuilder builder = new StringBuilder();
    	builder.append(super.toString());
    	return builder.toString();
    }
}
