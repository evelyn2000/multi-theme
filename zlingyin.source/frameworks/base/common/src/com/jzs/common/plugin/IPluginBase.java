package com.jzs.common.plugin;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * {@hide}
 */
public interface IPluginBase {
	
	String getKey();
	int getType();
	
	String getTargetPackageName();
	String getTargetClass();
	int getPrivateExtenalInfoResourceId();
	boolean isNeedLauncheThePlugin();
	
	int getDetailResourceId();
	List<IPluginDetail> getDetailList(PackageManager pm);
	
	int getDescriptionResource();
	CharSequence loadDescription(PackageManager pm);
	
	int getTitleResource();
	CharSequence loadTitle(PackageManager pm);

	int getPreviewResource();
	Drawable loadPreview(PackageManager pm);
	
	
	Drawable loadDrawable(PackageManager pm, int resid);
	Drawable loadDrawable(PackageManager pm, int resid, ApplicationInfo info);
	Drawable loadDrawable(Resources res, int resid);
	
	CharSequence loadText(PackageManager pm, int resid);
	CharSequence loadText(PackageManager pm, int resid, ApplicationInfo info);
	
	CharSequence loadText(Resources res, int resid);
	
	int loadColor(PackageManager pm, int resid);
	int loadColor(Resources res, int resid);
	
	int loadInteger(PackageManager pm, int resid);
	int loadInteger(Resources res, int resid);
	
	int[] loadIntegerArray(PackageManager pm, int resid);
	int[] loadIntegerArray(Resources res, int resid);
	
	int[] loadResourceIdArray(PackageManager pm, int resid);
	int[] loadResourceIdArray(Resources res, int resid);
	
	String[] loadStringArray(PackageManager pm, int resid);
	String[] loadStringArray(Resources res, int resid);
	
	
	Resources getResources(PackageManager pm);
	ApplicationInfo getApplicationInfo(PackageManager pm);
	Context getTargetContext(Context context);
	
	String toString();
}
