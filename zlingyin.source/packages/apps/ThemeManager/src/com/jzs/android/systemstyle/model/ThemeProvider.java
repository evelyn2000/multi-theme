package com.jzs.android.systemstyle.model;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jzs.android.systemstyle.utils.ThemeStyleBaseInfo;
import com.jzs.android.systemstyle.utils.ThemeStyleLocalInfo;
import com.jzs.android.systemstyle.utils.ThemesSettings;
import com.jzs.android.systemstyle.utils.Util;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.jzs.android.systemstyle.R;
import com.jzs.common.theme.ThemeManager;

/**
 * Provider to manager information of themes in current system in database.
 */
public class ThemeProvider extends ContentProvider {
    private SQLiteDatabase sqlDB;
    private DatabaseHelper dbHelper;
    private static final String DATABASE_NAME = "themes.db";
    private static final int DATABASE_VERSION = 2;
    
    private static final String TAG = "ThemeProvider";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private Context mContext;
        private final Collator sCollator = Collator.getInstance();

        private final Comparator<PackageInfo> THEME_NAME_COMPARATOR = new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo a, PackageInfo b) {
                String aThemeName = "";//ThemeManager.getThemeName(mContext, a.packageName, a.themeNameId);
                String bThemeName = "";//ThemeManager.getThemeName(mContext, b.packageName, b.themeNameId);
                return sCollator.compare(aThemeName, bThemeName);
            }
        };

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	Util.Log.d(TAG, "Enter DatabaseHelper.onCreate()");
            db.execSQL("Create table " + ThemesSettings.TABLE_NAME 
            		+ "( " + ThemesSettings.THEME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ThemesSettings.THEME_TITLE + " TEXT," 
            		+ ThemesSettings.THEME_PREVIEW_ICON + " BLOB," 
            		+ ThemesSettings.THEME_PREVIEW_ICON_URL + " TEXT," 
                    + ThemesSettings.THEME_SERVER_ID + " TEXT,"
                    + ThemesSettings.THEME_VERSION + " TEXT," 
                    + ThemesSettings.THEME_LAST_UPDATE_DATE + " INTEGER NOT NULL DEFAULT 0,"
                    + ThemesSettings.THEME_DOWNLOAD_COMPLETED + " INTEGER NOT NULL DEFAULT 0,"
                    + ThemesSettings.THEME_EXT_OPTIONS + " INTEGER NOT NULL DEFAULT 0,"
                    + ThemesSettings.THEME_JZS_ID + " INTEGER NOT NULL DEFAULT 0,"
                    + ThemesSettings.THEME_JZC_ID + " INTEGER NOT NULL DEFAULT 0,"
                    + ThemesSettings.THEME_PATH_TYPE + " INTEGER NOT NULL DEFAULT " + ThemeStyleBaseInfo.THEME_PATH_LOCAL
                    + ");");

            initDatabase(db);

            Util.Log.d(TAG, "Leave DatabaseHelper.onCreate()");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	Util.Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS" + ThemesSettings.TABLE_NAME);
            onCreate(db);
        }

        public void initDatabase(SQLiteDatabase db) {
        	
        	mContext.startService(new Intent(Util.Action.ACTION_INITTYLESERVICE));
        	
//        	List<ThemeStyleBaseInfo> all = new ArrayList<ThemeStyleBaseInfo>();
//        	all.add(new ThemeStyleLocalInfo(0, 0, 0, "Android", 
//		    		Util.drawable2Bitmap(mContext.getResources().getDrawable(R.drawable.dr_01))
//		    		));
//        	
//    		if(com.mediatek.common.featureoption.FeatureOption.QS_FRAMEWORK_SUPPORT_STYLE_SAMSUNG){
//    			all.add(new ThemeStyleLocalInfo(0, ThemeManager.STYLE_SAMSUNG, ThemeManager.SUB_STYLE_I9500, "Samsung", 
//    					Util.drawable2Bitmap(mContext.getResources().getDrawable(R.drawable.ss_01))
//    					));
//    		}
//    		
//    		if(com.mediatek.common.featureoption.FeatureOption.QS_FRAMEWORK_SUPPORT_STYLE_IPHONE){
//    			all.add(new ThemeStyleLocalInfo(0, ThemeManager.STYLE_IPHONE, 0, "Iphone", 
//    					Util.drawable2Bitmap(mContext.getResources().getDrawable(R.drawable.ip_01))
//    					));
//    		}
//    		
//    		if(com.mediatek.common.featureoption.FeatureOption.QS_FRAMEWORK_SUPPORT_STYLE_HTC){
//    			all.add(new ThemeStyleLocalInfo(0, ThemeManager.STYLE_HTC, 0, "Htc", 
//    					Util.drawable2Bitmap(mContext.getResources().getDrawable(R.drawable.htc_01))
//    					));
//    		}
//
//    		for (ThemeStyleBaseInfo item : all) {
//    		    ContentValues values = new ContentValues();
//    		    item.onAddToDatabase(values);
//    		    db.insert(ThemesSettings.TABLE_NAME, null, values);
//    		}
        	
            /*List<PackageInfo> allApps = new ArrayList<PackageInfo>();
            List<PackageInfo> selectedApps = new ArrayList<PackageInfo>();
            PackageInfo defaultApp = new PackageInfo();
            PackageManager pmg = mContext.getPackageManager();
            allApps = pmg.getInstalledPackages(0);
            for (PackageInfo app : allApps) {
                Log.d(TAG, "initDatabase: packageName = " + app.packageName + ",isThemePackage = "
                        + app.isThemePackage + ",themeNameId = " + app.themeNameId);
                if (app.packageName.equals("android")) {
                    defaultApp = app;
                    defaultApp.themeNameId = R.string.default_name;
                }
                if (app.isThemePackage == 1) {                    
                     selectedApps.add(app);
                }
            }
            Collections.sort(selectedApps, THEME_NAME_COMPARATOR);
            selectedApps.add(0, defaultApp);

            for (PackageInfo item : selectedApps) {
                ContentValues values = new ContentValues();
                values.put(Themes.PACKAGE_NAME, item.packageName);
                values.put(Themes.THEME_PATH, item.applicationInfo.sourceDir);
                values.put(Themes.THEME_NAME_ID, item.themeNameId);
                db.insert(TABLE_NAME, null, values);
            }

            allApps.clear();
            selectedApps.clear();*/
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
    	Util.Log.d(TAG, "Enter delete()");
        sqlDB = dbHelper.getWritableDatabase();
        int count = sqlDB.delete(ThemesSettings.TABLE_NAME, selection, selectionArgs); 
        if (count > 0) sendNotify(uri);
        
        return count;
    }

    @Override
    public String getType(Uri url) {
    	String table = "";
    	String where = null;
    	if (url.getPathSegments().size() == 1) {
    		table = url.getPathSegments().get(0);
        } else if (url.getPathSegments().size() != 2) {
            return null;//throw new IllegalArgumentException("Invalid URI: " + url);
        } else if (!TextUtils.isEmpty(where)) {
            return null;
        } else {
        	table = url.getPathSegments().get(0);
        	where = "_id=" + ContentUris.parseId(url);                
        }
    	if (!TextUtils.isEmpty(table)){
	        if (TextUtils.isEmpty(where)) {
	            return "vnd.android.cursor.dir/" + table;
	        } else {
	            return "vnd.android.cursor.item/" + table;
	        }
    	}
    	
    	return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues arg) {
    	Util.Log.d(TAG, "Enter insert()==uri:"+uri);
        sqlDB = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues(arg);
        long rowId = sqlDB.insert(ThemesSettings.TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri rowUri = ContentUris.withAppendedId(uri, rowId);//ContentUris.appendId(uri.buildUpon(), rowId).build();
            Util.Log.d(TAG, "Leave insert()==rowUri:"+rowUri);
            sendNotify(rowUri);
            return rowUri;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
    	Util.Log.d(TAG, "Enter query()");
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        qb.setTables(ThemesSettings.TABLE_NAME);
        Util.Log.d(TAG, "query(): uri: " + uri.toString());

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        Util.Log.d(TAG, "Leave query()");
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues arg1, String arg2, String[] arg3) {
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.update(ThemesSettings.TABLE_NAME, arg1, arg2, arg3);
        if (count > 0) sendNotify(uri);

        return count;
    }
    
    private void sendNotify(Uri uri) {
        String notify = uri.getQueryParameter(ThemesSettings.PARAMETER_NOTIFY);
        if (notify == null || "true".equals(notify)) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }
    
    static class SqlArguments {
        public final String table;
        public final String where;
        public final String[] args;

        SqlArguments(Uri url, String where, String[] args) {
            if (url.getPathSegments().size() == 1) {
                this.table = url.getPathSegments().get(0);
                this.where = where;
                this.args = args;
            } else if (url.getPathSegments().size() != 2) {
                throw new IllegalArgumentException("Invalid URI: " + url);
            } else if (!TextUtils.isEmpty(where)) {
                throw new UnsupportedOperationException("WHERE clause not supported: " + url);
            } else {
                this.table = url.getPathSegments().get(0);
                this.where = "_id=" + ContentUris.parseId(url);                
                this.args = null;
            }
        }

        SqlArguments(Uri url) {
            if (url.getPathSegments().size() == 1) {
                table = url.getPathSegments().get(0);
                where = null;
                args = null;
            } else {
                throw new IllegalArgumentException("Invalid URI: " + url);
            }
        }
    }
}
