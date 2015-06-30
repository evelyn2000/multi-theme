package com.jzs.common.launcher;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface IResConfigManager {
	
	///////////////////// layout  /////////////////////
	public final static int GLOBAL_SYS_TYPE_LAYOUT_START = 10;
	public final static int LAYOUT_USER_FOLDER = GLOBAL_SYS_TYPE_LAYOUT_START + 1;
	public final static int LAYOUT_FOLDER_ICON = GLOBAL_SYS_TYPE_LAYOUT_START + 2;
	public final static int LAYOUT_INSTALL_WIDGET_ADAPTER = GLOBAL_SYS_TYPE_LAYOUT_START + 3;
	public final static int LAYOUT_APP_WIDGET_ERROR = GLOBAL_SYS_TYPE_LAYOUT_START + 4;
	public final static int LAYOUT_ADD_LIST_ITEM = GLOBAL_SYS_TYPE_LAYOUT_START + 5;
	public final static int LAYOUT_APPLICATION_SHORTCUT = GLOBAL_SYS_TYPE_LAYOUT_START + 6;
	public final static int LAYOUT_APPS_CUSTOMIZE_PAGE_SCREEN = GLOBAL_SYS_TYPE_LAYOUT_START + 7;
	public final static int LAYOUT_APPS_CUSTOMIZE_APP_WITH_UNREAD = GLOBAL_SYS_TYPE_LAYOUT_START + 8;
	public final static int LAYOUT_APPS_CUSTOMIZE_WIDGET = GLOBAL_SYS_TYPE_LAYOUT_START + 9;
	public final static int LAYOUT_ADD_WORKSPACE_PREVIEW_ITEM = GLOBAL_SYS_TYPE_LAYOUT_START + 10;
	public final static int LAYOUT_APPLICATION_SHORTCUT_NORESIZE = GLOBAL_SYS_TYPE_LAYOUT_START + 11;
	
	///////////////////// image  /////////////////////
	public final static int GLOBAL_SYS_TYPE_IMG_START = 200;
	public final static int IMG_FOLDER_RING_ANIMATOR_OUTER = GLOBAL_SYS_TYPE_IMG_START + 0;
	public final static int IMG_FOLDER_RING_ANIMATOR_INNER = GLOBAL_SYS_TYPE_IMG_START + 1;
	public final static int IMG_FOLDER_RING_ANIMATOR_SHARED_OUTER = GLOBAL_SYS_TYPE_IMG_START + 2;
	public final static int IMG_FOLDER_RING_ANIMATOR_SHARED_INNER = GLOBAL_SYS_TYPE_IMG_START + 3;
	public final static int IMG_FOLDER_RING_ANIMATOR_SHARED_LEAVE = GLOBAL_SYS_TYPE_IMG_START + 4;
	
	public final static int IMG_APP_WIDGET_RESIZE_BG = GLOBAL_SYS_TYPE_IMG_START + 10;
	public final static int IMG_APP_WIDGET_RESIZE_HANDLE_LEFT = GLOBAL_SYS_TYPE_IMG_START + 11;
	public final static int IMG_APP_WIDGET_RESIZE_HANDLE_RIGHT = GLOBAL_SYS_TYPE_IMG_START + 12;
	public final static int IMG_APP_WIDGET_RESIZE_HANDLE_TOP = GLOBAL_SYS_TYPE_IMG_START + 13;
	public final static int IMG_APP_WIDGET_RESIZE_HANDLE_BOTTOM = GLOBAL_SYS_TYPE_IMG_START + 14;
	
	public final static int IMG_IC_LAUNCHER_WALLPAPER = GLOBAL_SYS_TYPE_IMG_START + 15;
	public final static int IMG_WIDGET_PREVIEW_TILE = GLOBAL_SYS_TYPE_IMG_START + 16;
	public final static int IMG_FOLDER_DEFAULT_ICON = GLOBAL_SYS_TYPE_IMG_START + 17;
	public final static int IMG_WORKSPACE_GRID_SIZE_DEF = GLOBAL_SYS_TYPE_IMG_START + 18;
	public final static int IMG_DRAGLAYER_LEFTHOVER_DRAWABLE = GLOBAL_SYS_TYPE_IMG_START + 19;
	public final static int IMG_DRAGLAYER_RIGHTHOVER_DRAWABLE = GLOBAL_SYS_TYPE_IMG_START + 20;
	public final static int IMG_FOLDER_PREVIEW_BG_ICON = GLOBAL_SYS_TYPE_IMG_START + 21;
	
	
	
	///////////////////// Dimension  /////////////////////
	public final static int GLOBAL_SYS_TYPE_DIM_START = 500;
	public final static int DIM_FOLDER_ICON_PREVIEW_SIZE = GLOBAL_SYS_TYPE_DIM_START + 0;
	public final static int DIM_FOLDER_ICON_PREVIEW_PADDING = GLOBAL_SYS_TYPE_DIM_START + 1;
	public final static int DIM_SCROLL_ZONE = GLOBAL_SYS_TYPE_DIM_START + 2;
	public final static int DIM_DRAG_VIEW_OFFSETX = GLOBAL_SYS_TYPE_DIM_START + 5;
	public final static int DIM_DRAG_VIEW_OFFSETY = GLOBAL_SYS_TYPE_DIM_START + 6;
	public final static int DIM_DRAG_VIEW_SCALE = GLOBAL_SYS_TYPE_DIM_START + 7;
	
	public final static int DIM_APPS_CELL_WIDTH = GLOBAL_SYS_TYPE_DIM_START + 8;
	public final static int DIM_APPS_CELL_HEIGHT = GLOBAL_SYS_TYPE_DIM_START + 9;
	//public final static int DIM_APPS_CELL_WIDTH_GAP = GLOBAL_SYS_TYPE_DIM_START + 10;
	//public final static int DIM_APPS_CELL_HEIGHT_GAP = GLOBAL_SYS_TYPE_DIM_START + 11;
	public final static int DIM_APPS_CELL_MAX_GAP = GLOBAL_SYS_TYPE_DIM_START + 10;
	public final static int DIM_APP_ICON_SIZE = GLOBAL_SYS_TYPE_DIM_START + 11;
	
	public final static int DIM_WORKSPACE_LEFT_PADDING_LAND = GLOBAL_SYS_TYPE_DIM_START + 12;
	public final static int DIM_WORKSPACE_RIGHT_PADDING_LAND = GLOBAL_SYS_TYPE_DIM_START + 13;
	public final static int DIM_WORKSPACE_TOP_PADDING_LAND = GLOBAL_SYS_TYPE_DIM_START + 14;
	public final static int DIM_WORKSPACE_BOTTOM_PADDING_LAND = GLOBAL_SYS_TYPE_DIM_START + 15;
	public final static int DIM_WORKSPACE_LEFT_PADDING_PORT = GLOBAL_SYS_TYPE_DIM_START + 16;
	public final static int DIM_WORKSPACE_RIGHT_PADDING_PORT = GLOBAL_SYS_TYPE_DIM_START + 17;
	public final static int DIM_WORKSPACE_TOP_PADDING_PORT = GLOBAL_SYS_TYPE_DIM_START + 18;
	public final static int DIM_WORKSPACE_BOTTOM_PADDING_PORT = GLOBAL_SYS_TYPE_DIM_START + 19;
	
	public final static int DIM_WORKSPACE_CELL_WIDTH_LAND = GLOBAL_SYS_TYPE_DIM_START + 20;
	public final static int DIM_WORKSPACE_CELL_HEIGHT_LAND = GLOBAL_SYS_TYPE_DIM_START + 21;
	public final static int DIM_WORKSPACE_WIDTH_GAP_LAND = GLOBAL_SYS_TYPE_DIM_START + 22;
	public final static int DIM_WORKSPACE_HEIGHT_GAP_LAND = GLOBAL_SYS_TYPE_DIM_START + 23;
	public final static int DIM_CELLLAYOUT_LEFT_PADDING_LAND = GLOBAL_SYS_TYPE_DIM_START + 24;
	public final static int DIM_CELLLAYOUT_RIGHT_PADDING_LAND = GLOBAL_SYS_TYPE_DIM_START + 25;
	public final static int DIM_CELLLAYOUT_TOP_PADDING_LAND = GLOBAL_SYS_TYPE_DIM_START + 26;
	public final static int DIM_CELLLAYOUT_BOTTOM_PADDING_LAND = GLOBAL_SYS_TYPE_DIM_START + 27;
	
	public final static int DIM_WORKSPACE_CELL_WIDTH_PORT = GLOBAL_SYS_TYPE_DIM_START + 28;
	public final static int DIM_WORKSPACE_CELL_HEIGHT_PORT = GLOBAL_SYS_TYPE_DIM_START + 29;
	public final static int DIM_WORKSPACE_WIDTH_GAP_PORT = GLOBAL_SYS_TYPE_DIM_START + 30;
	public final static int DIM_WORKSPACE_HEIGHT_GAP_PORT = GLOBAL_SYS_TYPE_DIM_START + 31;
	public final static int DIM_CELLLAYOUT_LEFT_PADDING_PORT = GLOBAL_SYS_TYPE_DIM_START + 32;
	public final static int DIM_CELLLAYOUT_RIGHT_PADDING_PORT = GLOBAL_SYS_TYPE_DIM_START + 33;
	public final static int DIM_CELLLAYOUT_TOP_PADDING_PORT = GLOBAL_SYS_TYPE_DIM_START + 34;
	public final static int DIM_CELLLAYOUT_BOTTOM_PADDING_PORT = GLOBAL_SYS_TYPE_DIM_START + 35;
	public final static int DIM_WORKSPACE_MAX_GAP = GLOBAL_SYS_TYPE_DIM_START + 36;
	public final static int DIM_WORKSPACE_CELL_WIDTH = GLOBAL_SYS_TYPE_DIM_START + 37;
	public final static int DIM_WORKSPACE_CELL_HEIGHT = GLOBAL_SYS_TYPE_DIM_START + 38;
	public final static int DIM_HOTSEAT_UNREAD_MARGIN_RIGHT = GLOBAL_SYS_TYPE_DIM_START + 39;
	public final static int DIM_WORKSPACE_UNREAD_MARGIN_RIGHT = GLOBAL_SYS_TYPE_DIM_START + 40;
	public final static int DIM_APP_ICON_PADDING_TOP = GLOBAL_SYS_TYPE_DIM_START + 41;
	public final static int DIM_SHORTCUT_PREVIEW_PADDING_TOP = GLOBAL_SYS_TYPE_DIM_START + 43;
	public final static int DIM_SHORTCUT_PREVIEW_PADDING_LEFT = GLOBAL_SYS_TYPE_DIM_START + 44;
	public final static int DIM_SHORTCUT_PREVIEW_PADDING_RIGHT = GLOBAL_SYS_TYPE_DIM_START + 45;
	public final static int DIM_BUTTON_BAR_HOTSEAT_HEIGHT = GLOBAL_SYS_TYPE_DIM_START + 46;
	public final static int DIM_BUTTON_BAR_SCREENINDICATOR_HEIGHT = GLOBAL_SYS_TYPE_DIM_START + 47;
	
	///////////////////// config Integer  /////////////////////
	public final static int GLOBAL_SYS_TYPE_CONFIG_START = 900;
	public final static int CONFIG_DROP_ANIM_MIN_DURATION = GLOBAL_SYS_TYPE_CONFIG_START + 0;
	public final static int CONFIG_DROP_ANIM_MAX_DURATION = GLOBAL_SYS_TYPE_CONFIG_START + 1;
	public final static int CONFIG_DROP_ANIM_MAX_DIST = GLOBAL_SYS_TYPE_CONFIG_START + 2;
	public final static int CONFIG_FLING_TO_DELETE_MIN_VELOCITY = GLOBAL_SYS_TYPE_CONFIG_START + 3;
	public final static int CONFIG_WORKSPACE_SCREEN_COUNT = GLOBAL_SYS_TYPE_CONFIG_START + 4;
	public final static int CONFIG_WORKSPACE_DEFAULT_SCREEN = GLOBAL_SYS_TYPE_CONFIG_START + 5;
	public final static int CONFIG_HOTSET_ALLAPP_INDEX = GLOBAL_SYS_TYPE_CONFIG_START + 6;
	public final static int CONFIG_HOTSET_CELL_COUNT = GLOBAL_SYS_TYPE_CONFIG_START + 7;
	public final static int CONFIG_ALLOW_SCREEN_ROTATION = GLOBAL_SYS_TYPE_CONFIG_START + 8;
	public final static int CONFIG_DRAGOUTLINE_FADETIME = GLOBAL_SYS_TYPE_CONFIG_START + 9;
	public final static int CONFIG_DRAGOUTLINE_MAXALPHA = GLOBAL_SYS_TYPE_CONFIG_START + 10;
	public final static int CONFIG_FOLDER_ANIM_DURATION = GLOBAL_SYS_TYPE_CONFIG_START + 11;
	public final static int CONFIG_WORKSPACE_CELL_COUNTX = GLOBAL_SYS_TYPE_CONFIG_START + 12;
	public final static int CONFIG_WORKSPACE_CELL_COUNTY = GLOBAL_SYS_TYPE_CONFIG_START + 13;
	public final static int CONFIG_WORKSPACE_LOADSHRINKPERCENT = GLOBAL_SYS_TYPE_CONFIG_START + 14;
	public final static int CONFIG_WORKSPACE_FADEADJACENTSCREENS = GLOBAL_SYS_TYPE_CONFIG_START + 15;
	public final static int CONFIG_WORKSPACE_MAX_SCREENCOUNT = GLOBAL_SYS_TYPE_CONFIG_START + 16;
	public final static int CONFIG_WORKSPACE_MIN_SCREENCOUNT = GLOBAL_SYS_TYPE_CONFIG_START + 17;
	public final static int CONFIG_APPS_CUSTOMIZE_DRAGSLOPETHRESHOLD = GLOBAL_SYS_TYPE_CONFIG_START + 18;
	public final static int CONFIG_ENABLE_STATIC_WALLPAPER = GLOBAL_SYS_TYPE_CONFIG_START + 19;
	public final static int CONFIG_WORKSPACE_SUPPORT_CYCLYSLIDING = GLOBAL_SYS_TYPE_CONFIG_START + 20;
	public final static int CONFIG_APPS_SUPPORT_CYCLYSLIDING = GLOBAL_SYS_TYPE_CONFIG_START + 21;
	public final static int CONFIG_APPS_WIDGET_SLIDE_TOGETHER = GLOBAL_SYS_TYPE_CONFIG_START + 22;
	public final static int CONFIG_SHOW_BAR_SCREENINDICATOR = GLOBAL_SYS_TYPE_CONFIG_START + 23;
	public final static int CONFIG_SHOW_BAR_HOTSEAT = GLOBAL_SYS_TYPE_CONFIG_START + 24;
	public final static int CONFIG_SHOW_WORKSPACE_LABEL = GLOBAL_SYS_TYPE_CONFIG_START + 25;
	public final static int CONFIG_IS_FULLSCREEN = GLOBAL_SYS_TYPE_CONFIG_START + 26;
	public final static int CONFIG_SUPPORT_GESTURE = GLOBAL_SYS_TYPE_CONFIG_START + 27;
	public final static int CONFIG_MAX_WORKSPACE_CELL_COUNTX = GLOBAL_SYS_TYPE_CONFIG_START + 28;
	public final static int CONFIG_MAX_WORKSPACE_CELL_COUNTY = GLOBAL_SYS_TYPE_CONFIG_START + 29;
	public final static int CONFIG_FOLDER_MAXNUM_ITEM_IN_PREVIEW = GLOBAL_SYS_TYPE_CONFIG_START + 30;
	public final static int CONFIG_DEFAULT_ANIMATE_EFFECT_TYPE = GLOBAL_SYS_TYPE_CONFIG_START + 31;
	public final static int CONFIG_DEFAULT_APP_ICON_SORT_TYPE = GLOBAL_SYS_TYPE_CONFIG_START + 32;
	
	
	///////////////////// String  /////////////////////
	public final static int GLOBAL_SYS_TYPE_STRING_START = 1200;
	public final static int STR_FOLDER_NAME_FORMAT = GLOBAL_SYS_TYPE_STRING_START + 0;
	public final static int STR_FOLDER_TAP_TO_CLOSE = GLOBAL_SYS_TYPE_STRING_START + 1;
	public final static int STR_FOLDER_TAP_TO_RENAME = GLOBAL_SYS_TYPE_STRING_START + 2;
	public final static int STR_INSTALL_WIDGET_PICK_FORMAT = GLOBAL_SYS_TYPE_STRING_START + 3;
	public final static int STR_UNINSTALL_SYSTEM_APP = GLOBAL_SYS_TYPE_STRING_START + 4;
	public final static int STR_DELETE_TARGET_UNINSTALL_LABEL = GLOBAL_SYS_TYPE_STRING_START + 5;
	public final static int STR_DELETE_TARGET_LABEL = GLOBAL_SYS_TYPE_STRING_START + 6;
	public final static int STR_GROUP_WALLPAPERS = GLOBAL_SYS_TYPE_STRING_START + 7;
	public final static int STR_ACTIVITY_NOT_FOUND = GLOBAL_SYS_TYPE_STRING_START + 8;
	public final static int STR_CHOOSE_WALLPAPER = GLOBAL_SYS_TYPE_STRING_START + 9;
	public final static int STR_FOLDER_DEFAULT_NAME = GLOBAL_SYS_TYPE_STRING_START + 10;
	public final static int STR_FOLDER_RENAMED_FORMAT = GLOBAL_SYS_TYPE_STRING_START + 11;
	public final static int STR_FOLDER_OPENED_FORMAT = GLOBAL_SYS_TYPE_STRING_START + 12;
	public final static int STR_FOLDER_CLOSED_FORMAT = GLOBAL_SYS_TYPE_STRING_START + 13;
	public final static int STR_FOLDER_HINT_TEXT = GLOBAL_SYS_TYPE_STRING_START + 14;
	public final static int STR_DEFAULT_SCROLL_FORMAT = GLOBAL_SYS_TYPE_STRING_START + 15;
	public final static int STR_HOTSEAT_OUT_OF_SPACE = GLOBAL_SYS_TYPE_STRING_START + 16;
	public final static int STR_OUT_OF_SPACE = GLOBAL_SYS_TYPE_STRING_START + 17;
	public final static int STR_GROUP_APPLICATIONS = GLOBAL_SYS_TYPE_STRING_START + 18;
	public final static int STR_TITLE_SELECT_APPLICATION = GLOBAL_SYS_TYPE_STRING_START + 19;
	public final static int STR_LONG_PRESS_WIDGET_TO_ADD = GLOBAL_SYS_TYPE_STRING_START + 20;
	public final static int STR_APPS_CUSTOMIZE_APPS_SCROLL_FORMAT = GLOBAL_SYS_TYPE_STRING_START + 21;
	public final static int STR_APPS_CUSTOMIZE_WIDGET_SCROLL_FORMAT = GLOBAL_SYS_TYPE_STRING_START + 22;
	public final static int STR_MENU_CUSTOM_SETTINGS = GLOBAL_SYS_TYPE_STRING_START + 23;
	public final static int STR_MENU_CUSTOM_ICON_SETTINGS = GLOBAL_SYS_TYPE_STRING_START + 24;
	public final static int STR_ALL_APPS_BTN_LABEL = GLOBAL_SYS_TYPE_STRING_START + 25;
	public final static int STR_CUSTOM_SETTINGS_LABEL = GLOBAL_SYS_TYPE_STRING_START + 26;
	public final static int STR_MENU_CREATE_FOLDER = GLOBAL_SYS_TYPE_STRING_START + 27;
	public final static int STR_MENU_SORT_APPS_ICON = GLOBAL_SYS_TYPE_STRING_START + 28;
	public final static int STR_MENU_ANIMATE_EFFECT = GLOBAL_SYS_TYPE_STRING_START + 29;   
	
	///////////////////// Colors  /////////////////////
	public final static int GLOBAL_SYS_TYPE_COLOR_START = 1700;
	public final static int CLR_INFO_TARGET_HOVER_HINT = GLOBAL_SYS_TYPE_COLOR_START + 0;
	
	///////////////////// Animation  /////////////////////
	public final static int GLOBAL_SYS_TYPE_OTHER_START = 2000;
	public final static int OTHER_WORKSPACE_DEFAULT_XML = GLOBAL_SYS_TYPE_OTHER_START + 0;
	public final static int OTHER_UNREAD_SHORTCUTS_XML = GLOBAL_SYS_TYPE_OTHER_START + 1;
	
	public final static int ARRAY_WORKSPACE_GRID_SIZE_PREVIEWS = GLOBAL_SYS_TYPE_OTHER_START + 2;
	public final static int ARRAY_WORKSPACE_GRID_SIZE_ENTRIES = GLOBAL_SYS_TYPE_OTHER_START + 3;
	public final static int ARRAY_WORKSPACE_GRID_SIZE_VALUES = GLOBAL_SYS_TYPE_OTHER_START + 4;
	public final static int ARRAY_APP_ICONS_SORT_ENTRIES = GLOBAL_SYS_TYPE_OTHER_START + 5;
	public final static int ARRAY_ANIMATE_EFFECT_ENTRIES = GLOBAL_SYS_TYPE_OTHER_START + 6;
	public final static int ARRAY_ANIMATE_EFFECT_VALUES = GLOBAL_SYS_TYPE_OTHER_START + 7;
	
	public final static int IGNORE_APP_LIST_XML = GLOBAL_SYS_TYPE_OTHER_START + 8;
	
	/////////////////////
	public final static int GLOBAL_USER_CUSTOM_TYPE_START = 3000;
	

	Resources getResources();
	Resources getBaseResources();
	IResConfigManager getResConfigManagerBase();
	LayoutInflater getLayoutInflater();
	void attachBaseContext(Context context);
	
	float getScreenDensity();
	boolean isLandscape();
	
	int getResourceId(int type);
	
	View inflaterView(int type);
	View inflaterView(int type, ViewGroup parent);
	View inflaterView(int type, ViewGroup parent, boolean attachToRoot);
	
	Drawable getDrawable(int type);
	int getDimensionPixelSize(int type);
	int getDimensionPixelSize(int type, int defValue);
	int getDimensionPixelOffset(int type);
	int getDimensionPixelOffset(int type, int defValue);
	float getDimension(int type);
	float getDimension(int type, float defValue);
	int getInteger(int type);
	int getInteger(int type, int defValue);
	int[] getIntArray(int type);
	boolean getBoolean(int type);
	boolean getBoolean(int type, boolean defValue);
	int getColor(int type);
	int getColor(int type, int defValue);
	int getIntegerWithDensity(int type);
	int getIntegerWithDensity(int type, int defValue);
	String getString(int type);
	String getString(int type, String defValue);
	CharSequence getText(int type);
	CharSequence getText(int type, CharSequence def);
	CharSequence[] getTextArray(int type);
	String[] getStringArray(int type);
	XmlResourceParser getAnimation(int type);
	XmlResourceParser getXml(int type);
	TypedArray obtainTypedArray(int type);
	
	void ReleaseInstance();
	void onTrimMemory(int level);
}
