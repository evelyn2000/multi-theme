package com.jzs.android.systemstyle.utils;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Parcel;


public class ThemeStyleLocalInfo extends ThemeStyleBaseInfo {
	
	public int mMainId;
	public int mExtId;

	public ThemeStyleLocalInfo(int id, int sid, int cid, String title, Bitmap icon){
		super(title, icon, THEME_PATH_LOCAL, id);
		
		mMainId = sid;
		mExtId = cid;
	}
	
	public ThemeStyleLocalInfo(Parcel pl){ 
		super(pl);
		mMainId = pl.readInt(); 
		mExtId = pl.readInt(); 
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(mMainId);
		dest.writeInt(mExtId);
	}

	public void onAddToDatabase(ContentValues values) { 
		super.onAddToDatabase(values);
        values.put(ThemesSettings.THEME_JZS_ID, mMainId);
        values.put(ThemesSettings.THEME_JZC_ID, mExtId);
    }

	
    
//    @Override
//    public String toString() {
//        return "Item(id=" + this.id + " title=" + this.title + " pathtype="+ mPathType +")";
//    }
}
