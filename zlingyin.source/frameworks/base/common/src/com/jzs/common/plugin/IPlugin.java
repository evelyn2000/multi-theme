package com.jzs.common.plugin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * {@hide}
 */
public interface IPlugin extends IPluginBase {

	String getPluginFor();
	int getPluginForThemeMainStyle();
	int getPluginForThemeSubStyle();
	int getPluginForThemeStyle();
	
	int getPluginClassParameter();
	int getPriority();
	
	boolean getIsClearDataCache();
	int getIconResource();
	Drawable loadIcon(PackageManager pm);
	Drawable loadIcon(PackageManager pm, ApplicationInfo appinfo);
	<T> T createInstanceWithTargetParameter(Context context, Object... args);
	<T> T createInstance(Context context);
}
