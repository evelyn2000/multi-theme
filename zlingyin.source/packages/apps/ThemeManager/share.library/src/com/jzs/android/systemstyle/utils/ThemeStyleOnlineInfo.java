package com.jzs.android.systemstyle.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.text.TextUtils;

public class ThemeStyleOnlineInfo extends ThemeStyleBaseInfo {
	public final static int STYLE_UPDATE_OPTION_FORMAT = 0x10;
	public final static int STYLE_UPDATE_OPTION_FULLPKG = 0x01;
	public String mServerKey;
	public String mVersion;
	public String mIconUrl;
	public int mExtOption;
	public int mLastUpdateDate;
		
	public ThemeStyleOnlineInfo(Parcel pl){ 
		super(pl);
		mServerKey = pl.readString(); 
		mVersion = pl.readString(); 
		mIconUrl = pl.readString(); 
		mExtOption = pl.readInt(); 
		mLastUpdateDate = pl.readInt(); 
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(mServerKey);
		dest.writeString(mVersion);
		dest.writeString(mIconUrl);
		dest.writeInt(mExtOption);
		dest.writeInt(mLastUpdateDate);
	}
	
	public ThemeStyleOnlineInfo(String serid, String ver, int opt, int date, String title, String iconsrc){
		this(serid, ver, opt, date, title, null, NO_ID, iconsrc);
	}
	
//	public ThemeStyleOnlineInfo(String serid, String ver, String title, Bitmap icon){
//		this(serid, ver, title, icon, NO_ID);
//	}
	
	public ThemeStyleOnlineInfo(String serid, String ver, int opt, int date, String title, Bitmap icon, int id, String imgUrl){
		super(title, icon, THEME_PATH_ONLINE, id);
		
		mServerKey = serid;
		mVersion = ver; 
		mExtOption = opt;
		mLastUpdateDate = date;
		mIconUrl = imgUrl;
	}
	
	public static ThemeStyleOnlineInfo valueOf(JSONObject jo) throws JSONException {
		if(jo != null){
			String serid = jo.getString("servid");
	    	if(!TextUtils.isEmpty(serid)){
	    		String title = jo.getString("title");
	    		String iconsrc = jo.getString("iconsrc");
	    		if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(iconsrc)){
		        	String version = jo.getString("version");
		        	int opt = jo.getInt("option");
		        	int lastupdatedate = jo.getInt("updatedate");
			    	return new ThemeStyleOnlineInfo(serid, version, opt, lastupdatedate, title, iconsrc);
	    		}
	    	}
		}
    	
		return null;
	}
	
	public boolean isFullPackage(){
		return ((mExtOption&STYLE_UPDATE_OPTION_FULLPKG) == STYLE_UPDATE_OPTION_FULLPKG);
	}
	
	public void onAddToDatabase(ContentValues values) { 
		super.onAddToDatabase(values);
        values.put(ThemesSettings.THEME_SERVER_ID, mServerKey);
        values.put(ThemesSettings.THEME_VERSION, mVersion);
        if(!TextUtils.isEmpty(mIconUrl))
        	values.put(ThemesSettings.THEME_PREVIEW_ICON_URL, mIconUrl);
        
        values.put(ThemesSettings.THEME_LAST_UPDATE_DATE, mLastUpdateDate);
        values.put(ThemesSettings.THEME_EXT_OPTIONS, mExtOption);
    }
}
