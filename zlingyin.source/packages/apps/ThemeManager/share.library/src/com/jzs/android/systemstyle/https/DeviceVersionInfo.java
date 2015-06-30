package com.jzs.android.systemstyle.https;

import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
//import android.os.SystemProperties;

import org.apache.http.message.BasicNameValuePair;

import com.jzs.android.systemstyle.utils.Util;

public class DeviceVersionInfo {
	private final static String TAG = "DeviceVersionInfo";
	public final static int PLATFORM_VENDOR_MTK = 1; 
	public final static int PLATFORM_VENDOR_QCOM = 2; 
	public final static int PLATFORM_VENDOR_SC = 3; 
	public final static int PLATFORM_VENDOR_OMAP = 4; 
	
	public String mOem;
	public String mProduct;
	public String mModel;
	public String mLanguage;
	public String mBuildNumber;
	public String mFlavor;
	public int mVersionSdkId;
	public String mVersionRelease;
    
	public int mSimCardCount;
	public String mPlatform;
	public int mPlatformVendor;
    
	public String mSystemPartTotalSize;
	public String mSystemPartFreeSize;
    
	public String mJzsVersion; // 
    
    private static DeviceVersionInfo sDeviceVersionInfo = null;
    public static synchronized DeviceVersionInfo getInstance(Context context) {

        if (sDeviceVersionInfo == null) {
        	int cpuVendor = getPlatformVendorId(context);
        	switch(cpuVendor){
        	case PLATFORM_VENDOR_MTK:
        		sDeviceVersionInfo = new DeviceVersionInfoMtk(context, cpuVendor);
        		break;
        	case PLATFORM_VENDOR_QCOM:
        		sDeviceVersionInfo = new DeviceVersionInfo(context, cpuVendor);
        		break;
        	case PLATFORM_VENDOR_SC:
        		sDeviceVersionInfo = new DeviceVersionInfo(context, cpuVendor);
        		break;
        	case PLATFORM_VENDOR_OMAP:
        		sDeviceVersionInfo = new DeviceVersionInfo(context, cpuVendor);
        		break;
    		default:
    			break;
        	}
        }

        return sDeviceVersionInfo;
    }
    
    public static int getPlatformVendorId(Context context){
    	String platform = Util.getPropString("ro.board.platform");//;, 
        if (TextUtils.isEmpty(platform)) {
        	platform = Util.getPropString("ro.mediatek.platform");
        	if(platform != null && platform.startsWith("MT")){
        		return PLATFORM_VENDOR_MTK;
        	}
        }
        
        if (!TextUtils.isEmpty(platform)){
        	
        	platform = platform.toLowerCase();
        	if(platform.startsWith("msm")){
        		return PLATFORM_VENDOR_QCOM;
        	}
        	
        	if(platform.startsWith("omap")){
        		return PLATFORM_VENDOR_OMAP;
        	}
        }
    	return PLATFORM_VENDOR_MTK;
    }
    
    
    protected DeviceVersionInfo(Context context, int cpuVendor){
    	mPlatformVendor = cpuVendor;
    	
    	mOem = Util.getPropString("ro.product.manufacturer");
    	mProduct = Util.getPropString("ro.product.device");
    	mModel = Util.getPropString("ro.product.model");
    	
    	mLanguage = Util.getPropString("ro.product.locale.language");
    	mBuildNumber = Util.getPropString("ro.build.display.id");
        
    	mFlavor = Util.getPropString("ro.build.flavor");
        
    	mVersionRelease = Util.getPropString("ro.build.version.release");
        
        mVersionSdkId = Util.getPropInteger("ro.build.version.sdk");
        
        mPlatform = Util.getPropString("ro.board.platform", "unknown");
        if (TextUtils.isEmpty(mPlatform)) {
        	mPlatform = "unknown";
        }
        mSimCardCount = 1;

        StatFs fileStats = new StatFs(Environment.getRootDirectory().getPath());
        try {
        	mSystemPartTotalSize = String.format("%dMb", (fileStats.getBlockCount() * fileStats.getBlockSize() / (1024 * 1024)));
        	mSystemPartFreeSize = String.format("%dMb", (fileStats.getAvailableBlocks() * fileStats.getBlockSize() / (1024 * 1024)));
        } catch (IllegalArgumentException e) {
        	
        }
        
        mJzsVersion = Util.getPropString("ro.build.jzsversion.release");
    }
    
    public void appendBasicNameValuePair(ArrayList<BasicNameValuePair> bnvpa){
    	if(!TextUtils.isEmpty(mJzsVersion)){
    		bnvpa.add(new BasicNameValuePair("jzversion", mJzsVersion));
    	} else {
	    	bnvpa.add(new BasicNameValuePair("cpu", String.valueOf(mPlatformVendor)));
	    	bnvpa.add(new BasicNameValuePair("model", mModel));
	    	bnvpa.add(new BasicNameValuePair("simc", String.valueOf(mSimCardCount)));
	    	bnvpa.add(new BasicNameValuePair("pltf", mPlatform));
	    	bnvpa.add(new BasicNameValuePair("size", mSystemPartTotalSize+"_"+mSystemPartFreeSize));
	    	bnvpa.add(new BasicNameValuePair("version", getVersionString()));
    	}
    }
    
    public String getVersionString(){
    	StringBuilder builder = new StringBuilder();

        builder.append(mOem.replaceAll("_", "\\$"));
        builder.append("_").append(mVersionSdkId);
        //builder.append("_").append(mPlatform.replaceAll("_", "\\$"));
        
        builder.append("_").append(mProduct.replaceAll("_", "\\$"));
        if (!TextUtils.isEmpty(mFlavor)) {
            builder.append("[").append(mFlavor).append("]");
        }
        
        builder.append("_").append(mLanguage.replaceAll("_", "\\$"));
        builder.append("_").append(mBuildNumber.replaceAll("_", "\\$"));
        //builder.append("_").append(mSystemPartTotalSize);
        //builder.append("_").append(mSystemPartFreeSize);

//        String versionInfo = builder.toString();
//        Util.Log.i(TAG, "getDeviceVersionInfo = " + versionInfo);
        
    	return builder.toString();
    }
    
    public String toString(){
    	StringBuilder builder = new StringBuilder();
    	//builder.append(b)
    	return builder.toString();
    }
}
