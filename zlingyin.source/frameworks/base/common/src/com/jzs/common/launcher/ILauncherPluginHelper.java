package com.jzs.common.launcher;

import android.app.Activity;
import android.content.Context;

import com.jzs.common.manager.IIconUtilities;

public interface ILauncherPluginHelper {
	
	public final static String EXT_HELPER_TARGET_PROVIDER = "LauncherProvider";
	public final static String EXT_HELPER_TARGET_LAUNCHERMODEL = "LauncherModel";
	public final static String EXT_HELPER_TARGET_LAUNCHER = "Launcher";
	public final static String EXT_HELPER_TARGET_INSTALL_SHORTCUT_RECEIVER = "InstallShortcutReceiver";
	
	
	void ReleaseInstance();
	void onTrimMemory(int level);
	
	Context getLocalContext();
	ILauncherApplication getLauncherApplication();
	
	IIconUtilities getIconUtilities();
	ISharedPrefSettingsManager getSharedPrefSettingsManager();
	IResConfigManager getResConfigManager();
	ILauncherProvider getLauncherProvider();
	
	void addExtHelper(String target, IExtHelperBase helper);
	void removeExtHelper(String target);
	IExtHelperBase getExtHelper(String target);
	
	Activity getActivity();
}
