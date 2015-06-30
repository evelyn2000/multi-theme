package com.jzs.android.systemstyle.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ThemeStyleBaseInfo implements Parcelable {
	public final static int NO_ID = 0;
	
	public static final int THEME_PATH_UNKNOWN = -1;
    public static final int THEME_PATH_LOCAL = 0;
    public static final int THEME_PATH_ONLINE = 1;
    
	public int mId;
	public String mTitle;
	public int mPathType;
	public Bitmap mIcon;
	//public String mDetailPath;

	public ThemeStyleBaseInfo(String title, Bitmap icon, int type){
		this(title, icon, type, NO_ID);
	}
	
	public ThemeStyleBaseInfo(String title, Bitmap icon, int type, int id){
		this.mTitle = title;
		this.mPathType = type;
		this.mIcon = icon; 
		this.mId = id;
	}
	
	public boolean isLocalStyle(){
		if(mPathType == THEME_PATH_LOCAL)
			return true;
		return false;
	}
	
	public void onAddToDatabase(ContentValues values) { 
		values.put(ThemesSettings.THEME_TITLE, mTitle);
        writeBitmap(values, mIcon);
        values.put(ThemesSettings.THEME_PATH_TYPE, mPathType);
	}
	
	public ThemeStyleBaseInfo(Parcel pl){ 
		mId = pl.readInt();
		mTitle = pl.readString(); 
		mPathType = pl.readInt();
		//mIcon =  Bitmap.CREATOR.createFromParcel(pl);
	} 

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);
		dest.writeString(mTitle);
		dest.writeInt(mPathType);
		//mIcon.writeToParcel(dest, 0);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<ThemeStyleBaseInfo> CREATOR = new Parcelable.Creator<ThemeStyleBaseInfo>() {

		@Override
		public ThemeStyleBaseInfo createFromParcel(Parcel source) {
			return new ThemeStyleBaseInfo(source);
		}

		@Override
		public ThemeStyleBaseInfo[] newArray(int size) {
			return new ThemeStyleBaseInfo[size];
		}

	};
	
	public static byte[] flattenBitmap(Bitmap bitmap) {
        // Try go guesstimate how much space the icon will take when serialized
        // to avoid unnecessary allocations/copies during the write.
    	if(bitmap == null)
    		return null;
    	
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            //Xlog.w("Favorite", "Could not write icon");
            return null;
        }
    }

	public static boolean writeBitmap(ContentValues values, Bitmap bitmap) {
        if (bitmap != null) {
            byte[] data = flattenBitmap(bitmap);
            if(data != null){
            	values.put(ThemesSettings.THEME_PREVIEW_ICON, data);
            	return true;
            }
        }
        
        return false;
    }
}
