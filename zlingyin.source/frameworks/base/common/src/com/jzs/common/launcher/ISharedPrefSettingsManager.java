package com.jzs.common.launcher;


import android.content.SharedPreferences;

public interface ISharedPrefSettingsManager {
	
	public static final String sCountCellXKey = "Key_CellX_Count";
	public static final String sCountCellYKey = "Key_CellY_Count";
	public static final String sCountScreenKey = "Key_Screens_Count";
	public static final String sMaxCountScreenKey = "Key_Screens_Max_Count";
	public static final String sMinCountScreenKey = "Key_Screens_Min_Count";
	public static final String sDefaultScreenKey = "Key_Screens_Default";
	public static final String sAppIconSizeKey = "Key_Apps_Icon_Size";
	public static final String sAppBtnIndexKey = "Key_Apps_Btn_Index";
	public static final String sStaticWallpaperKey = "Key_Static_Wallpaper";
	public static final String sWorkspaceCycleSlidingKey = "Key_Workspace_CycleSliding";
	public static final String sAppsCycleSlidingKey = "Key_Apps_CycleSliding";
	public static final String sAppsWidgetSlideTogetherKey = "Key_Apps_WidgetTogether";
	public static final String sShowScreenIndicatorBarKey = "Key_Workspace_ShowScreenIndicator";
	public static final String sShowHotseatBarKey = "Key_Workspace_ShowHotseat";
	public static final String KEY_WORKSPACE_GRID_SIZE = "Key_Workspace_GridSize";
	public static final String KEY_WORKSPACE_SHOW_LABEL = "Key_Workspace_ShowLabel";
	public static final String KEY_IS_FULLSCREEN = "Key_Is_Fullscreen";
	public static final String KEY_WORKSPACE_LOCK_PAGES = "Key_Workspace_LockPages";

	void onTrimMemory(int level);
	
	SharedPreferences getSharedPreferences();
	void setResConfigManager(IResConfigManager res);
	
	int getInt(String key, int defResKey, int defValue);
	int getInt(String key, int defValue);
	void setInt(String key, int value);
		
	float getFloat(String key, float defValue);
	void setFloat(String key, float value);
	
	Boolean getBoolean(String key, int defResKey, Boolean defValue);
	Boolean getBoolean(String key, Boolean defValue);
	void setBoolean(String key, Boolean value);
	
	String getString(String key, int defResKey, String defValue);
	String getString(String key, String defValue);
	void setString(String key, String value);
	
	int getWorkspaceCountCellX();
	int getWorkspaceCountCellX(int defValue);
	void setWorkspaceCountCellX(int count);
	int getWorkspaceCountCellY();
	int getWorkspaceCountCellY(int defValue);
	void setWorkspaceCountCellY(int count);
	
	int getWorkspaceScreenCount();
	int getWorkspaceScreenCount(int defValue);
	void setWorkspaceScreenCount(int count);
	
	int getWorkspaceScreenMaxCount();
	int getWorkspaceScreenMaxCount(int defValue);
	void setWorkspaceScreenMaxCount(int count);
	
	int getWorkspaceScreenMinCount();
	int getWorkspaceScreenMinCount(int defValue);
	void setWorkspaceScreenMinCount(int count);
	
	int getWorkspaceDefaultScreen();
	int getWorkspaceDefaultScreen(int defValue);
	void setWorkspaceDefaultScreen(int value);
	
	int getAppIconSize();
	int getAppIconSize(int defValue);
	void setAppIconSize(int size);
	
	public final static int ALL_APPS_BTN_RANK_INDEX_NONE = -1;
	//public final static int ALL_APPS_BTN_RANK_INDEX_NONE = -2;
	int getAllAppsButtonRank();
	int getAllAppsButtonRank(int defValue);
	void setAllAppsButtonRank(int value);
	
	boolean getEnableStaticWallpaper();
	boolean getEnableStaticWallpaper(boolean defValue);
	void setEnableStaticWallpaper(boolean enable);
	
	boolean getWorkspaceSupportCycleSliding();
	boolean getWorkspaceSupportCycleSliding(boolean defValue);
	void setWorkspaceSupportCycleSliding(boolean enable);
	
	boolean getAppsSupportCycleSliding();
	boolean getAppsSupportCycleSliding(boolean defValue);
	void setAppsSupportCycleSliding(boolean enable);
	
	boolean getSlideAppAndWidgetTogether();
	boolean getSlideAppAndWidgetTogether(boolean defValue);
	void setSlideAppAndWidgetTogether(boolean enable);
	
	boolean getWorkspaceEnableScreenIndicatorBar();
	boolean getWorkspaceEnableScreenIndicatorBar(boolean defValue);
	void setWorkspaceEnableScreenIndicatorBar(boolean enable);
	
	boolean getWorkspaceShowHotseatBar();
	boolean getWorkspaceShowHotseatBar(boolean defValue);
	void setWorkspaceShowHotseatBar(boolean enable);
}
