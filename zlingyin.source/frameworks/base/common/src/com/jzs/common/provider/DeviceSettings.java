package com.jzs.common.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.ContentObserver;

import com.jzs.utils.ConfigOption;

/*
 * @hide
 */
public final class DeviceSettings {

	public static class DeviceStatusInfor {
		public final String key;
		//public final boolean enable;
		public final String drvfile;
		public final boolean startinitdrv;
		public int value;
		public DeviceStatusInfor(String k){
			this(k, null, false);
		}
		
		public DeviceStatusInfor(String k, String d, boolean init){
			this(k, d, init, 0);
		}
		
		public DeviceStatusInfor(String k, String d, boolean init, int defv){
			key = k;
			drvfile = d;
			if(drvfile != null && drvfile.length() > 0)
				startinitdrv = init;
			else
				startinitdrv = false;
			value = defv;
		}
	}
	
	public static List<DeviceStatusInfor> getDeviceSettingsList(){
		List<DeviceStatusInfor> list = new ArrayList<DeviceStatusInfor>();
		if(ConfigOption.QS_TPD_SUPPORT_GESTURE){
			list.add(new DeviceStatusInfor(Settings.System.ENABLE_TP_GESTURE, 
					"/sys/module/tpd_setting/parameters/tpd_gesture_support", true, 0));
		}
		
		return list.size() > 0 ? list : null;
	}
	
    public static void registerContentObserver(ContentResolver resolver, String key,
            ContentObserver observer){
        registerContentObserver(resolver, key, observer, false);
    }
    
    public static void registerContentObserver(ContentResolver resolver, String key,
            ContentObserver observer, boolean notifyForDescendents){
        resolver.registerContentObserver(android.provider.Settings.System.getUriFor(key), notifyForDescendents, observer);
    }
    
    public static void unregisterContentObserver(ContentResolver resolver, ContentObserver observer) {
        if(resolver != null && observer != null)
            resolver.unregisterContentObserver(observer);
    }
    
    public static int getProviderSettingsIntValue(ContentResolver resolver, String key, int def){
        return android.provider.Settings.System.getInt(resolver, key, def);
    }
    
    public static boolean getProviderSettingsBooleanValue(ContentResolver resolver, String key, boolean def){
        return android.provider.Settings.System.getInt(resolver, key, (def ? 1 : 0)) > 0;
    }
    
    public static void setProviderSettingsIntValue(ContentResolver resolver, String key, int enable){
        android.provider.Settings.System.putInt(resolver, key, enable);
    }
    
    public static void setProviderSettingsBooleanValue(ContentResolver resolver, String key, boolean enable){
        android.provider.Settings.System.putInt(resolver, key, (enable ? 1 : 0));
    }
    
    
    /*
     * ENABLE_PHONE_COVER
     */
    public static void registerPhoneCoverContentObserver(ContentResolver resolver, 
            ContentObserver observer){
        if(ConfigOption.QS_SUPPORT_COVER){
            registerContentObserver(resolver, Settings.System.ENABLE_PHONE_COVER, observer, false);
        }
    }

    public static void setPhoneCoverEnable(ContentResolver resolver, boolean enable){
        if(ConfigOption.QS_SUPPORT_COVER){
            android.provider.Settings.System.putInt(resolver, 
                    Settings.System.ENABLE_PHONE_COVER, (enable ? 1 : 0));
        }
    }
    
    public static boolean isPhoneCoverEnable(ContentResolver resolver){
        return isPhoneCoverEnable(resolver, ConfigOption.QS_SUPPORT_COVER);
    }
    
    public static boolean isPhoneCoverEnable(ContentResolver resolver, boolean def){
        if(ConfigOption.QS_SUPPORT_COVER){
            return getProviderSettingsBooleanValue(resolver, 
                            Settings.System.ENABLE_PHONE_COVER, def);
        }
        return false;
    }
    
    /*
     * FLOAT_ENABLE_SHORTCUT_KEY
     */
    public static void registerFloatShortcutKeyContentObserver(ContentResolver resolver, 
            ContentObserver observer){
        if(ConfigOption.QS_SUPPORT_FLOAT_SHORTCUT_KEY){
            registerContentObserver(resolver, Settings.System.FLOAT_ENABLE_SHORTCUT_KEY, observer, false);
        }
    }

    public static void setFloatShortcutKeyEnable(ContentResolver resolver, boolean enable){
        if(ConfigOption.QS_SUPPORT_FLOAT_SHORTCUT_KEY){
            android.provider.Settings.System.putInt(resolver, 
                    Settings.System.FLOAT_ENABLE_SHORTCUT_KEY, (enable ? 1 : 0));
        }
    }
    
    public static boolean isFloatShortcutKeyEnable(ContentResolver resolver){
        return isFloatShortcutKeyEnable(resolver, ConfigOption.QS_SUPPORT_FLOAT_SHORTCUT_KEY);
    }
    
    public static boolean isFloatShortcutKeyEnable(ContentResolver resolver, boolean def){
        if(ConfigOption.QS_SUPPORT_FLOAT_SHORTCUT_KEY){
            return getProviderSettingsBooleanValue(resolver, 
                            Settings.System.FLOAT_ENABLE_SHORTCUT_KEY, def);
        }
        return false;
    }
    
    
    /*
     * QS_SUPPORT_RGB_LED_LIGHTS
     */
    public static void registerRgbLedLightsContentObserver(ContentResolver resolver, 
            ContentObserver observer){
        if(ConfigOption.QS_SUPPORT_RGB_LED_LIGHTS){
            registerContentObserver(resolver, Settings.System.QS_ENABLE_LEDS_LIGHTS, observer, false);
        }
    }

    public static void setRgbLedLightsEnable(ContentResolver resolver, boolean enable){
        if(ConfigOption.QS_SUPPORT_RGB_LED_LIGHTS){
            android.provider.Settings.System.putInt(resolver, 
                    Settings.System.QS_ENABLE_LEDS_LIGHTS, (enable ? 1 : 0));
        }
    }
    
    public static boolean isRgbLedLightsEnable(ContentResolver resolver){
        return isRgbLedLightsEnable(resolver, ConfigOption.QS_SUPPORT_RGB_LED_LIGHTS);
    }
    
    public static boolean isRgbLedLightsEnable(ContentResolver resolver, boolean def){
        if(ConfigOption.QS_SUPPORT_RGB_LED_LIGHTS){
            return getProviderSettingsBooleanValue(resolver, 
                            Settings.System.QS_ENABLE_LEDS_LIGHTS, def);
        }
        return false;
    }
    
    /*
     * Proximity gestures 
     */
    public static void registerProximityGestureStatusObserver(ContentResolver resolver, 
            ContentObserver observer){
    	registerContentObserver(resolver, Settings.System.PS_GESTURE_STATUS, observer, false);
    }
    
    
}
