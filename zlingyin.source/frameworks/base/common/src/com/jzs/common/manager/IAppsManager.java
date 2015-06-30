package com.jzs.common.manager;

import java.util.List;

import android.content.ComponentName;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageItemInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


public interface IAppsManager {
	public final static String MANAGER_SERVICE = IComplexManager.MANAGER_SERVICE_PREFIX+"AppsManagerSer";
	
	public final static String IC_FOLDER_PKG_NAME = "com.jzs.internal";
	public final static String IC_FOLDER_GAME_CLASS_NAME = "prebuild.foldericon.game";
	public final static String IC_FOLDER_SOCIAL_CLASS_NAME = "prebuild.foldericon.social";
	public final static String IC_FOLDER_MEDIA_CLASS_NAME = "prebuild.foldericon.media";
	public final static String IC_FOLDER_COMMON_CLASS_NAME = "prebuild.foldericon.common";
	
	public final static String IC_DEFAULT_ACTIVITYICON_CLASS_NAME = "prebuild.defaultappicon";
	
	public final static String APP_ICON_SIZE_KEY = "system_app_icon_size";
	
	public final static String DEFAULT_APPLICATION_CLASS = "application";
	public final static String SYSTEM_ICON_STYLE_KEY = "jzs_system_icon_style_key";
	public final static String SYSTEM_ICON_SCALEMODE_KEY = "jzs_system_icon_scalemode_key";

	IIconUtilities getIconUtilities();
	int getIconSize();
	void setIconSize(int size);

	
	void changeAppIconStyle(int styleIndex, boolean resetOldIcon);
    void changeAppIconStyle(int bgAndMaskArrayRes, int defIconXmlRes, boolean resetOldIcon);
    /**
     * resourceName for a given resource identifier.  This name is
     * a single string of the form "package:type/entry".
     * resourceName is Resource.getResourceName(int resid)
     * */
    void changeAppIconStyle(String bgAndMaskResourceName, String defIconXmlResourceName, boolean resetOldIcon);

    
	void deleteTitle(ComponentInfo packageInfo);
	void deleteTitle(ComponentInfo packageInfo, boolean isKillApp);
    void deleteTitles(List<ComponentInfo> packagesInfo);
    boolean setTitle(ComponentInfo packageInfo, String title);
    boolean setTitle(ComponentInfo packageInfo, String title, boolean isKillApp);
    
	String getTitle(ComponentName componentName);
	String getTitle(String packageName, String activityName, int labelRes);
    String getTitle(ComponentInfo packageInfo);
    
    Drawable getOriginalIconDrawable(ComponentInfo packageInfo);
    Drawable getDefaultActivityIcon();
    Drawable getDefaultActivityIcon(int resId);
    Drawable getIconDrawable(ComponentName componentName);
    Drawable getIconDrawable(ComponentInfo packageInfo);
    Drawable getIconDrawable(String packageName, String activityName, int iconRes);

    boolean deleteIcon(ComponentInfo packageInfo);
    boolean deleteIcon(ComponentInfo packageInfo, boolean isKillApp);
    void deleteIcons(List<ComponentInfo> packagesInfo);
    boolean setIcon(ComponentInfo packageInfo, Bitmap icon, Bitmap orgicon);
    boolean setIcon(ComponentInfo packageInfo, Bitmap icon, Bitmap orgicon, boolean isKillApp);
    boolean setDefaultIcon(ComponentInfo packageInfo, Bitmap icon);
    boolean setDefaultIcon(String packageName, String activityName, int iconRes, Bitmap icon);
    void removeAllExistingIcons(boolean sendNotify);
    
    
    void deleteIconAndTitle(ComponentInfo componentInfo);
    void deleteIconAndTitle(ComponentInfo componentInfo, boolean sendNotify, boolean isKillApp);
    void deleteIconAndTitles(List<ComponentInfo> componentInfos);
    void sendNotifyIconOrTitleChanged(List<String> packageNames);
    
    boolean setIconAndTitle(ComponentInfo componentInfo, Bitmap icon, Bitmap orgicon, String title);
    boolean setIconAndTitle(ComponentInfo componentInfo, Bitmap icon, Bitmap orgicon, String title, boolean isKillApp);
    boolean checkTitleModified(ComponentInfo componentInfo);//(String className);
    boolean checkIconModified(ComponentInfo componentInfo);//(String className);
    boolean checkIconOrTitleModified(ComponentInfo componentInfo);//(String className);
    
    String flattenApplicationToIdentifier(String packageName, String appClassName);
    String flattenToIdentifier(String packageName, String className);
    String flattenToIdentifier(String packageName, int resId);
    
    int sendCommand(String cmd);
}
