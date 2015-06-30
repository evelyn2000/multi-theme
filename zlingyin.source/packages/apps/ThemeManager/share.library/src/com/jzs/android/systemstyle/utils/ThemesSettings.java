package com.jzs.android.systemstyle.utils;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

public class ThemesSettings implements BaseColumns {

	public static final String AUTHORITY = "com.jzs.systemstyle.ThemeProvider";
	public static final String PARAMETER_NOTIFY = "notify";
	
	public static final String TABLE_NAME = "theme";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    public static final Uri CONTENT_URI_NO_NOTIFICATION = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME
    		+ "?" + PARAMETER_NOTIFY + "=false");

    public static final String THEME_ID = "_id";
    
    public static final String THEME_TITLE = "themes_title";
    
    public static final String THEME_PREVIEW_ICON = "theme_preview_icon";

    public static final String THEME_SERVER_ID = "theme_server_id";
    public static final String THEME_PREVIEW_ICON_URL = "theme_preview_icon_url";
    public static final String THEME_VERSION = "theme_version";
    //public static final String THEME_DETAIL_INFO = "theme_detail_info";
    public static final String THEME_LAST_UPDATE_DATE = "theme_update_date";
    public static final String THEME_DOWNLOAD_COMPLETED = "theme_dl_completed";
    public static final String THEME_EXT_OPTIONS = "theme_ext_opt";
    
    public static final String THEME_PATH_TYPE = "theme_path";
    
    
    
    public static final String THEME_JZS_ID = "theme_jzs_id";
    public static final String THEME_JZC_ID = "theme_jzc_id";
        
    
    public static Uri getContentUri(long id) {
        return Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME + "/" + id);
    }
    
    public static Uri getContentUri(long id, boolean notify) {
        return Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME + "/" + id + "?" +
                PARAMETER_NOTIFY + "=" + notify);
    }
    
    public static int addItemToDatabase(Context context, ThemeStyleBaseInfo item) {
		return addItemToDatabase(context, item, true);
	}
	
	public static int addItemToDatabase(Context context, ThemeStyleBaseInfo item, boolean notify) {
        
        final ContentValues values = new ContentValues();
        final ContentResolver cr = context.getContentResolver();

        item.onAddToDatabase(values);
        Uri result = cr.insert(notify ? CONTENT_URI : CONTENT_URI_NO_NOTIFICATION, values);

        if (result != null) {
            item.mId = Integer.parseInt(result.getPathSegments().get(1));
            return item.mId;
        }
        
        return ThemeStyleBaseInfo.NO_ID;
    }
	
    /**
     * Move an item in the DB to a new <container, screen, cellX, cellY>
     */
	public static void updateItemInDatabase(Context context, ThemeStyleBaseInfo item, boolean notify) {
		if(item.mId == ThemeStyleBaseInfo.NO_ID)
        	return;
        final Uri uri = getContentUri(item.mId, notify);
        final ContentValues values = new ContentValues();
        //final ContentResolver cr = context.getContentResolver();

        item.onAddToDatabase(values);
        context.getContentResolver().update(uri, values, null, null);
    }
	
	public static void updateItemInDatabase(Context context, int id, final ContentValues values, boolean notify) {
        if(id == ThemeStyleBaseInfo.NO_ID)
        	return;
        
        final Uri uri = getContentUri(id, notify);
        //final ContentValues values = new ContentValues();
        //final ContentResolver cr = context.getContentResolver();

        //item.onAddToDatabase(values);
        
        context.getContentResolver().update(uri, values, null, null);
    }
    
	public static void deleteItemFromDatabase(Context context, int id, boolean notify) {
        //final ContentResolver cr = context.getContentResolver();
        final Uri uriToDelete = getContentUri(id, notify);
        context.getContentResolver().delete(uriToDelete, null, null);
    }
}
