package com.jzs.common.os;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * @author Jz
 * {@hide}
 */
public final class Util {
    private final static String TAG = "Jzs.Cmd.Util";
    
    public static boolean isNetWorkAvailable(Context context) {
        boolean ret = false;
        if(context == null) {
            android.util.Log.e(TAG, "Util:isNetWorkAvailable context = null");
            return ret;
        }
        try {
            ConnectivityManager connetManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connetManager == null) {
                android.util.Log.e(TAG, "Util:isNetWorkAvailable connetManager = null");
                return ret;
            }
            NetworkInfo[] infos=connetManager.getAllNetworkInfo();
            if (infos == null) {
                return ret;
            }
            for (int i = 0; i < infos.length && infos[i] != null; i++) {
                if(infos[i].isConnected() && infos[i].isAvailable()) {
                    ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Util:isNetWorkAvailable Exception", e);
            e.printStackTrace();
        }
        //android.util.Log.e(TAG, "Util:isNetWorkAvailable network is ok: "+ret);
        return ret;
    }
    
    public static boolean isSpecifiedNetWorkAvailable(Context context, String typeName) {
        boolean ret = false;
        if(context == null) {
            android.util.Log.e(TAG, "Util:isSpecifiedNetWorkAvailable context = null");
            return ret;
        }
        
        if (typeName == null || typeName.length() <= 0) {
            android.util.Log.e(TAG, "Util:isSpecifiedNetWorkAvailable typeName = "+typeName+", a wrong network");
            return ret;
        }
        
        try {
            ConnectivityManager connetManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connetManager == null) {
                android.util.Log.e(TAG, "Util:isSpecifiedNetWorkAvailable connetManager = null");
                return ret;
            }
            NetworkInfo[] infos=connetManager.getAllNetworkInfo();
            if (infos == null) {
                return ret;
            }
            for (int i = 0; i < infos.length && infos[i] != null; i++) {
                if(infos[i].getTypeName().endsWith(typeName) && infos[i].isConnected() && infos[i].isAvailable()) {
                    ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Util:isSpecifiedNetWorkAvailable Exception", e);
            e.printStackTrace();
        }
        return ret;
    }
    
    public static boolean isSpecifiedNetWorkAvailable(Context context, int type) {
        boolean ret = false;
        if(context == null) {
            android.util.Log.e(TAG, "Util:isSpecifiedNetWorkAvailable context = null");
            return ret;
        }
        
        if (!ConnectivityManager.isNetworkTypeValid(type)) {
            android.util.Log.e(TAG, "Util:isSpecifiedNetWorkAvailable typeName = "+type+", a wrong network");
            return ret;
        }
        
        try {
            ConnectivityManager connetManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connetManager == null) {
                android.util.Log.e(TAG, "Util:isSpecifiedNetWorkAvailable connetManager = null");
                return ret;
            }
            NetworkInfo[] infos=connetManager.getAllNetworkInfo();
            if (infos == null) {
                return ret;
            }
            for (int i = 0; i < infos.length && infos[i] != null; i++) {
                if((infos[i].getType() == type) && infos[i].isConnected() && infos[i].isAvailable()) {
                    ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Util:isSpecifiedNetWorkAvailable Exception", e);
            e.printStackTrace();
        }
        return ret;
    }
    
    public static boolean isWifiNetWorkAvailable(Context context) {
        return Util.isSpecifiedNetWorkAvailable(context, ConnectivityManager.TYPE_WIFI);
    }
    
    public static int getWifiMacAddressId(){
//    	String ver = JzsSystemProperties.get("ro.mediatek.version.release", "");
//    	if(ver.startsWith("ALPS.")){
//    		ver = ver.substring(5);
//    	}
    	if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN_MR1){
    		return 30;
    	}
    	
    	return 29;
    }
    
    public static List<String> getSystemCookieFilePath(){
    	return null;
    }
}
