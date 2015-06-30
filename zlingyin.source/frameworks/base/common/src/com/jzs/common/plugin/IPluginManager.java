package com.jzs.common.plugin;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteStatement;

import com.jzs.common.manager.IComplexManager;

/**
 * {@hide}
 */
public interface IPluginManager {
	
	public final static int THEME_PACKAGE_PLUGIN_IDENTIFY = 0x10000;	
	public final static String MANAGER_SERVICE = IComplexManager.MANAGER_SERVICE_PREFIX+"PluginManagerServ";
	public final static String PLUGIN_MANAGER_SERVICE = MANAGER_SERVICE;
	
	public final static String THEME_PLUGIN_PERMISSION = 
										"com.jzs.permission.THEME_PLUGIN_PERMISSION"; 
	public final static String SUPPORT_THEME_PLUGIN_PERMISSION = 
										"com.jzs.permission.SUPPORT_THEME_PLUGIN_PERMISSION"; 
	
		
	
	public final static String PLUGIN_FOR_UNKOWN = "";
	public final static String PLUGIN_FOR_LOCKSCREEN = "lockscreen";
	public final static String PLUGIN_FOR_LOCKSCREEN_SKIN = "lockscreen.skin";
	public final static String PLUGIN_FOR_SYSTEMTHEMESTYLE = "systemthemestyle";
	public final static String PLUGIN_FOR_SYSTEMTHEMESTYLE_SKIN = "systemthemestyle.skin";
	
	
	final static int PLUGIN_MSG_APPLY = 5000;
	final static int PLUGIN_MSG_APPLY_ASSIGN_PLUGIN = 5001;
	final static int PLUGIN_MSG_RESET_PLUGIN = 5002;
	
	
	public static final String PLUGIN_META_DATA_KEY = "com.jzs.plugin.meta.PLUGIN_META_DATA_KEY";
	public static final String PLUGIN_META_PRIVATE_DATA_KEY = "com.jzs.plugin.meta.PLUGIN_META_PRIVATE_DATA_KEY";
	//public static final String PLUGIN_TARGET_META_DATA_KEY = "com.jzs.plugin.meta.PLUGIN_TARGET_META_DATA_KEY";
	
	/*
     * <meta-data android:name="com.jzs.plugin.meta.META_PLUGIN_LAUNCHE_REMOTE_KEY" 
                    android:value="false" />
     */
	public final static String META_PLUGIN_LAUNCHE_REMOTE_KEY = 
            "com.jzs.plugin.meta.META_PLUGIN_LAUNCHE_REMOTE_KEY";
	
	/*
	 *<meta-data android:name="com.jzs.plugin.meta.META_TARGET_CLASS_KEY" 
                    android:value="lockscreen" />
      <meta-data android:name="com.jzs.plugin.meta.META_TARGET_CLASS_KEY" 
                    android:value="@com.jzs:string/PluginFor_Lockscreen" />
	 */
	public final static String META_TARGET_CLASS_KEY = 
			"com.jzs.plugin.meta.META_TARGET_CLASS_KEY";
	public final static String META_TARGET_IDENTIFY_KEY = 
            "com.jzs.plugin.meta.META_TARGET_IDENTIFY_KEY";
	/*
	 * <meta-data android:name="com.jzs.plugin.meta.PLUGIN_FOR_TARGET_META_KEY" 
                    android:value="com.android.Launcher2" />
       <meta-data android:name="com.jzs.plugin.meta.PLUGIN_FOR_TARGET_META_KEY" 
                    android:value="@com.jzs:string/PluginFor_Lockscreen" />
	 */
	public final static String PLUGIN_FOR_TARGET_META_KEY = 
				"com.jzs.plugin.meta.PLUGIN_FOR_TARGET_META_KEY";
	
	
	
	/*
	 * <meta-data android:name="com.jzs.plugin.meta.PLUGIN_DETAIL_RESOURCE_META_KEY" 
                    android:resource="@array/support_themes_array_detail_default" />
       <array name="support_themes_array_detail_default">
				<!-- preview images -->
				<item>@array/support_themes_array_detail_icon_default</item>
				<!-- title -->
		        <item>@array/support_themes_array_detail_title_default</item>
		        <!-- description -->
		        <item>@array/support_themes_array_detail_description_default</item>
    	</array>
	 */
	public final static String PLUGIN_DETAIL_RESOURCE_META_KEY = 
			"com.jzs.plugin.meta.PLUGIN_DETAIL_RESOURCE_META_KEY";
	
	
	public final static int PLUGIN_TYPE_UNKOWN = 0;
	public final static int PLUGIN_TYPE_SKIN = 1;
	public final static int PLUGIN_TYPE_STYLE = 2;
	public final static int PLUGIN_TYPE_STYLE_AND_SKIN = (PLUGIN_TYPE_SKIN|PLUGIN_TYPE_STYLE);
	/*
	 * <meta-data android:name="com.jzs.plugin.meta.PLUGIN_TYPE_META_KEY" 
                    android:value="@com.jzs:integer/PluginType_Skin" />
	 */
	public final static String PLUGIN_TYPE_META_KEY = 
				"com.jzs.plugin.meta.PLUGIN_TYPE_META_KEY";
	
	
	
	/*
	 * <meta-data android:name="com.jzs.plugin.meta.PLUGIN_TARGET_SUPPORT_SYSTEMTHEME_META_KEY" 
                    android:value="false" />
	 */
	public final static String PLUGIN_TARGET_SUPPORT_SYSTEMTHEME_META_KEY = 
			"com.jzs.plugin.meta.PLUGIN_TARGET_SUPPORT_SYSTEMTHEME_META_KEY";
	
	/*
	 * <meta-data android:name="com.jzs.plugin.meta.PLUGIN_PRIORITY_META_KEY" 
                    android:value="2" />
	 */
	public final static String PLUGIN_PRIORITY_META_KEY = 
			"com.jzs.plugin.meta.PLUGIN_PRIORITY_META_KEY";
	


	
	

	public final static int PLUGIN_PARAM_EMPTY = 0;
	public final static int PLUGIN_PARAM_CONTEXT = 1;
	/*
	 * <meta-data android:name="com.jzs.plugin.meta.PLUGIN_CLASS_PARAMETER_META_KEY" 
                    android:value="@com.jzs:integer/PluginParam_Context" />
	 */
	public final static String PLUGIN_CLASS_PARAMETER_META_KEY = 
				"com.jzs.plugin.meta.PLUGIN_CLASS_PARAMETER_META_KEY";
	
	
	/*
	 * <meta-data android:name="com.jzs.plugin.meta.PLUGIN_FOR_THEME_STYLE_META_KEY" 
                    android:value="@com.jzs:integer/STYLE_SAMSUNG" />
	 */
	public final static String PLUGIN_FOR_THEME_STYLE_META_KEY = 
				"com.jzs.plugin.meta.PLUGIN_FOR_THEME_STYLE_META_KEY";
	
	
	/*
	 * <meta-data android:name="com.jzs.plugin.meta.PLUGIN_FOR_THEME_SUBSTYLE_META_KEY" 
                    android:value="@com.jzs:integer/SUB_STYLE_ANY" />
	 */
	public final static String PLUGIN_FOR_THEME_SUBSTYLE_META_KEY = 
				"com.jzs.plugin.meta.PLUGIN_FOR_THEME_SUBSTYLE_META_KEY";
	
	/*
	 * <meta-data android:name="com.jzs.plugin.meta.PLUGIN_IS_CLEAR_DATA_CACHE_META_KEY" 
                    android:value="false" />
	 */
	public final static String PLUGIN_IS_CLEAR_DATA_CACHE_META_KEY = 
				"com.jzs.plugin.meta.PLUGIN_IS_CLEAR_DATA_CACHE_META_KEY";
	
	int getPluginCount();
	IPlugin getPlugin(String key);
	
	String getCurrentApplyedPluginKey(String pluginfor, int pluginType);
	//IPlugin getCurrentApplyedPlugin(String pluginfor);
	IPlugin getCurrentApplyedPlugin(String pluginfor, int pluginType);
	
	IPlugin getDefaultPlugin(String pluginfor);
	IPlugin getDefaultPlugin(String pluginfor, int pluginType);

	void resetDefaultPlugin(IPluginTarget plugin);
	void resetDefaultPlugin(IPluginTarget plugin, int pluginType);
	void setDefaultPlugin(IPlugin plugin);
	void setDefaultPlugin(String pluginfor, String pluginPkg, String pluginClz, int pluginType);
	void setDefaultPlugin(SQLiteStatement stmt, String pluginfor, String pluginPkg, String pluginClz, int pluginType);
	IPlugin getPluginForThemeStyle(String pluginfor, int mainStyle, int pluginType);
	IPlugin getPluginForThemeStyle(String pluginfor, int mainStyle, int subStyle, int pluginType);
	
	List<IPlugin> getAllPlugins();
	List<IPlugin> getPluginListByTargetType(String pluginfor);
	List<IPlugin> getPluginListByTargetType(String pluginfor, int pluginType);
	List<IPlugin> getPluginListByTargetPackage(String pkg);
	List<IPlugin> getPluginListForThemeStyle(String pluginfor, int mainStyle, int pluginType);
	List<IPlugin> getPluginListForThemeStyle(String pluginfor, int mainStyle, int subStyle, int pluginType);
	
	void dumpPlugins();
	
	List<IPluginTarget> getPluginTargetList();
	IPluginTarget getPluginTarget(String key);
	
	void changeSystemStyle(int nMainStyle, int nSubStyle, boolean resetAllTarget);
	void changeSystemStyle(int nMainStyle, int nSubStyle);
	
	
}
