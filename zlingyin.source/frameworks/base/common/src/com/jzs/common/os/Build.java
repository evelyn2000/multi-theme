package com.jzs.common.os;

import com.jzs.utils.ConfigOption;

/*
 * @hide
 */
public final class Build {

    public static final boolean IS_CUSTOMAPPABLE = (JzsSystemProperties.getInt("ro.build.jzs.customapp", 0) != 0);
    
    public static final String getProjectName(){
        return ConfigOption.QS_PRJ_NAME;
    }
    
    public static final int getFrameworkVersion(){
        return ConfigOption.JZS_GLOBAL_FRAMEWORK_VERSION;
    }
    
    public static final String getVersionName(){
        return JzsSystemProperties.get("ro.custom.build.version", "unknow");
    }
    
    public static final boolean isSupportCustomAppIcons(){
        return ConfigOption.JZS_ENABLE_CUSTOM_APPS;
    }
    
    public static final boolean isSupportPolicy(){
        return ConfigOption.ENABLE_JZS_POLICY;
    }
    
    public static final boolean isSupportCustomJar(){
        return ConfigOption.ENABLE_JZS_JARS;
    }
    
    public static final boolean isSupportPolicyJar(){
        return ConfigOption.ENABLE_JZS_JARS;
    }
    
    public static final boolean isSupportPhoneCover(){
        return ConfigOption.QS_SUPPORT_COVER;
    }
    
    public static final boolean isSupportFloatShortcutKey(){
        return ConfigOption.QS_SUPPORT_FLOAT_SHORTCUT_KEY;
    }
    
    public static final boolean isSupportRgbLedLights(){
        return ConfigOption.QS_SUPPORT_RGB_LED_LIGHTS;
    }
    
    public static final boolean isSupportLockscreenWallpaper(){
        return ConfigOption.QS_SUPPORT_LOCKSCREEN_WALLPAPER;
    }
    
    public static final boolean isSupportGesture(){
        return ConfigOption.QS_TPD_SUPPORT_GESTURE;
    }
    
    public static final boolean isSupportHardwareStereo(){
        return ConfigOption.QS_S3D_SUPPORT;
    }
}
