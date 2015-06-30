package com.jzs.common.launcher;

import java.util.HashMap;

import com.jzs.common.launcher.model.ApplicationInfo;
import com.jzs.common.launcher.model.IconCacheEntry;
import com.jzs.common.manager.IIconUtilities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface IIconCache {

    Bitmap getDefaultIcon();
	IIconUtilities getIconUtilities();
	Drawable getFullResDefaultActivityIcon();
	Drawable getFullResIcon(Resources resources, int iconId);
	Drawable getFullResIcon(String packageName, int iconId);
	Drawable getFullResIcon(ResolveInfo info);
	Drawable getFullResIcon(ComponentInfo info);
	void remove(ComponentName componentName);
	void flush();
	void dumpCacheEntry();
	
	void add(ComponentName componentName, IconCacheEntry entry);
	
	String getTitle(ComponentName component, ComponentInfo info);
	String getTitle(ComponentName component, ResolveInfo resolveInfo,
            HashMap<Object, CharSequence> labelCache);
	String getTitle(ComponentName component, int labelRes);
	
	IconCacheEntry getTitleAndIcon(ComponentName componentName);
	IconCacheEntry getTitleAndIcon(ComponentName componentName, ComponentInfo info);
	IconCacheEntry getTitleAndIcon(ComponentName componentName, ResolveInfo info,
            HashMap<Object, CharSequence> labelCache);
	void getTitleAndIcon(ApplicationInfo application, ResolveInfo info,
            HashMap<Object, CharSequence> labelCache);
	Bitmap getIcon(Intent intent);
	Bitmap getIcon(ComponentName component);
	Bitmap getIcon(ComponentName component, int iconRes);
	Bitmap getIcon(ComponentName component, ResolveInfo resolveInfo,
            HashMap<Object, CharSequence> labelCache);
	boolean isDefaultIcon(Bitmap icon);
	HashMap<ComponentName,Bitmap> getAllIcons();
	
	Bitmap createIconBitmap(String packageName, Resources resources, int iconResId);
	Bitmap createIconBitmap(ComponentName componentName, int iconResId);
	Bitmap createIconBitmap(ComponentName componentName, ComponentInfo info);
	Bitmap createIconBitmap(String packageName, Bitmap orgIcon);
	Bitmap createIconBitmap(String packageName, Drawable orgIcon);
}
