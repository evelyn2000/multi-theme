package com.jzs.android.systemstyle;


import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;

import com.jzs.android.systemstyle.model.LauncherModel;
import com.jzs.android.systemstyle.utils.ThemesSettings;
import com.jzs.android.systemstyle.utils.Util;

public class ThemeStyleApplication extends Application {
	private final static String TAG = "ThemeStyleApplication";
	private LauncherModel mModel;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Util.Log.d(TAG, "onCreate()");
	
		mModel = new LauncherModel(this);
	//
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.jzs.android.action.JZS_UPDATE_ITEM");
		filter.addAction("com.jzs.android.action.JZS_ADD_ITEM");
        registerReceiver(mModel, filter);
        
		// Register for changes to the favorites
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(ThemesSettings.CONTENT_URI, true,
                mStyleDatabaseObserver);
	}
	
	@Override
    public void onTerminate() {
        super.onTerminate();
        Util.Log.d(TAG, "onTerminate()");
        
        unregisterReceiver(mModel);

        ContentResolver resolver = getContentResolver();
        resolver.unregisterContentObserver(mStyleDatabaseObserver);
    }
	
	public LauncherModel getModel() {
        return mModel;
    }
	
	private final ContentObserver mStyleDatabaseObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
        	if (Util.DEBUG) {
        		Util.Log.d(TAG, "mFavoritesObserver onChange: selfChange = " + selfChange);
        	}
            mModel.startLoader(ThemeStyleApplication.this, false);
        }
    };
}
