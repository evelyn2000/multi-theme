package com.jzs.common.launcher;

import android.app.Activity;

import com.jzs.common.manager.IIconUtilities;

public interface ILauncherApplication {
	
//	LauncherHelper setLauncherActivity(Activity launcherActivity);
//	LauncherHelper getLauncherHelper();
	
	IIconCache createIconCache();
	ILauncherModel createLauncherModel();
	IGlobalStaticFunc getGlobalStaticFunctions();
	ILauncherProvider getLauncherProvider();
	
	IIconUtilities createIconUtilities(ISharedPrefSettingsManager pref, IResConfigManager res);
	ISharedPrefSettingsManager getSharedPrefSettingsManager();
	IResConfigManager getDefaultResConfigManager(ISharedPrefSettingsManager pref);
	
	void showAppIconAndTitleCustomActivity();
}
