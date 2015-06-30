package com.jzs.common.content;

import android.content.Intent;
/**
 * {@hide}
*/
public class QsIntent extends Intent {
	
	/**
     * @hide
     */
	public static final String ACTION_JZS_LOCKSCREEN_CHANGED = 
			"android.intent.jzs.ACTION_JZS_LOCKSCREEN_CHANGED";
	/**
     * @hide
     */
//    public static final String ACTION_QS_HOME_KEY_PRESSED = 
//			"android.intent.qs.ACTION_QS_HOME_KEY_PRESSED";
    /**
     * @hide
     */
//    public static final String ACTION_QS_LOCKSCREEN_WALLPAPER_CHANGED = 
//            "com.mediatek.lockscreen.action.WALLPAPER_SET";
    /**
     * @hide
     */
    public static final String QS_LOCKSCREEN_EXTRA_COMPLETE = 
            "com.mediatek.lockscreen.extra.COMPLETE";

    /**
     * @hide
     */
    public static final String ACTION_JZS_SHOW_FLOATBAR = 
			"android.intent.jzs.action.ACTION_JZS_SHOW_FLOATBAR";

    /**
     * @hide
     */
    public static final String ACTION_JZS_TIME_CHANGED = 
			"android.intent.jzs.action.ACTION_JZS_TIME_CHANGED";
    /**
     * @hide
     */
    public static final String ACTION_JZS_PLUGIN = 
			"android.intent.jzs.action.ACTION_JZS_PLUGIN";
    /**
     * @hide
     */
    public static final String CATEGORY_JZS_PLUGIN = 
			"android.intent.jzs.category.CATEGORY_JZS_PLUGIN";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_SUPPORT_PLUGIN = 
			"android.intent.jzs.action.ACTION_JZS_SUPPORT_PLUGIN";
    /**
     * @hide
     */
    public static final String CATEGORY_JZS_SUPPORT_PLUGIN = 
			"android.intent.jzs.category.CATEGORY_JZS_SUPPORT_PLUGIN";
    
    /**
     * @hide
     */
    public static final String CATEGORY_JZS_PLUGIN_LOCKSCREEN = 
			"android.intent.jzs.category.CATEGORY_JZS_PLUGIN_LOCKSCREEN";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_PLUGIN_CHANGED = 
			"android.intent.jzs.action.ACTION_JZS_PLUGIN_CHANGED";
    /**
     * @hide
     */
    public static final String EXTRA_JZS_PLUGIN_FOR = "EXTRA_JZS_PLUGIN_FOR";
    
    
    /*
     * @hide
     * getIntExtra(EXTTRA_JZS_LIST_PLUGINS)
     */
    public static final String ACTION_JZS_LIST_PLUGINS = 
            "android.intent.jzs.action.ACTION_JZS_LIST_PLUGINS";
    
    /*
     * @hide
     */
    public static final String EXTTRA_JZS_LIST_PLUGINS = "PLUGIN_TARGET_KEY";
    
    
    /*
     * @hide
     * getIntExtra(EXTTRA_JZS_LIST_PLUGINS)
     */
    public static final String ACTION_DUI_ENABLE_PLUGIN = 
            "android.intent.jzs.action.ACTION_DUI_ENABLE_PLUGIN";
    
    /*
     * @hide
     */
    public static final String EXTTRA_DUI_PLUGIN_KEY = "PLUGIN_KEY";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_PACKAGE_CHANGED = 
                            "android.intent.jzs.action.ACTION_JZS_PACKAGE_CHANGED";
    
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_KEYGUARD_UNLOCKED = 
                            "android.intent.jzs.action.ACTION_KEYGUARD_UNLOCKED";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_GESTURE_SETTINGS = 
                            "android.qishang.intent.jzs.GESTURE_SETTINGS";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_GESTURE_POWER = 
                            "android.qishang.intent.jzs.GESTURE_POWER_SETTINGS";
    
    /**
     * @hide
     */
    public static final String EXTRA_JZS_GESTURE_POWER = "extra_power_status";
    /**
     * @hide
     */
    public static final String EXTRA_JZS_GESTURE_ENABLE = "extra_enable_status";
    /**
     * @hide
     */
    public static final String EXTRA_JZS_GESTURE_ORIENTATION = "extra_orientation_status";
    
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_APP_CUSTOMAPPS = 
                            "android.qishang.intent.jzs.ACTION_APP_CUSTOMAPPS";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_APP_SETTINGS = 
                            "android.settings.SETTINGS";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_APP_STYLE_CHANGED = 
                            "com.android.jzs.action.APP_STYLE_CHANGED";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_OTA_REBOOT = 
                            "com.jzs.action.systemupdate.RebootRecoveryService";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_OTA_UPDATE_FILE = 
                            "com.jzs.action.systemupdate.WriteCommandService";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_OTA_UPDATE = 
                            "com.jzs.action.systemupdate.OTA_UPDATE";
    /**
     * like "/sdcard/ota_package.zip"
     * @hide
     */
    public static final String EXTTRA_JZS_OTA_FILE_PATH = "JZS_OTA_FILE_PATH";
    
    /**
     * @hide
     */
    public static final String ACTION_JZS_OTA_SYSOPERATE = 
                            "com.jzs.action.systemupdate.SysOperService";
    
    
    /*
     * @hide
     * getIntExtra(EXTTRA_PHONECOVER_STATE)
     * depend feature : com.jzs.utils.ConfigOption.QS_SUPPORT_COVER
     */
    public static final String ACTION_PHONECOVER_CHANGED = 
            "com.jzs.android.action.ACTION_PHONECOVER_CHANGED";
    /*
     * @hide
     * 0 : closed
     * 1 : opened
     * -1 : unknow
     * depend feature : com.jzs.utils.ConfigOption.QS_SUPPORT_COVER
     */
    public static final String EXTTRA_PHONECOVER_STATE = "EXTTRA_PHONECOVER_STATE";
    
    /*
     * @hide
     * getIntExtra(EXTTRA_PHONECOVER_STATE)
     * depend feature : com.jzs.utils.ConfigOption.QS_SUPPORT_RGB_LED_LIGHTS
     */
    public static final String ACTION_LEDSTATUS_CHANGED = 
            "com.jzs.android.action.ACTION_LEDSTATUS_CHANGED";
    /*
     * @hide
     * 0 : closed
     * 1 : opened
     * -1 : unknow
     * depend feature : com.jzs.utils.ConfigOption.QS_SUPPORT_RGB_LED_LIGHTS
     */
    public static final String EXTTRA_LEDLIGHT_STATE = "EXTTRA_LEDLIGHT_STATE";
    
    /*
     * @hide
     */
    public static final String ACTION_WAKEUP = 
            					"com.jzs.android.action.ACTION_WAKEUP";
    
    /*
     * @hide
     */
    public static final String ACTION_DISMISS_KEYGUARD = 
            					"com.jzs.android.action.ACTION_DISMISS_KEYGUARD";

    /*
     * @hide
     */
    public static final String CATEGORY_HMG_APP_SOS = "com.hmg.intent.category.APP_SOS_KEY";
    
    /*
     * @hide
     */
    public static final String ACTION_HMG_APP_SOS = "com.hmg.intent.action.ACTION_SOS_KEY";
    
    /*
     * @hide
     */
    public static final String CATEGORY_APP_SOS = "com.jzs.intent.category.APP_SOS_KEY";
    
    /*
     * @hide
     */
    public static final String ACTION_APP_SOS = "com.jzs.intent.action.ACTION_SOS_KEY";
    
    /*
     * @hide
     */
    public static final String EXTTRA_SOS_CALL = "EXTTRA_SOS_CALL";
    
    /*
     * @hide
     */
    public static final String ACTION_DISPATCH_ALL_KEY_TO_USER = "android.intent.jzs.ACTION_DISPATCH_ALL_KEY_TO_USER";
    
    /*
     * @hide
     */
    public static final String EXTRA_DISPATCH_ALL_KEY_TO_USER = "DISPATCH_ALL_KEY_TO_USER";
}
