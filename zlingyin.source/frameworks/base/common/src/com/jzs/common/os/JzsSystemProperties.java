package com.jzs.common.os;

/**
 * Gives access to the system properties store.  The system properties
 * store contains a list of string key-value pairs.
 *
 * {@hide}
*/
public final class JzsSystemProperties {
	
	public static final int PROP_NAME_MAX = 31;
    public static final int PROP_VALUE_MAX = 91;
    
	private static native String jzs_native_get(String key);
    private static native String jzs_native_get(String key, String def);
    private static native int jzs_native_get_int(String key, int def);
    private static native void jzs_native_set(String key, String def);
    public static native boolean jzs_check_permission();
    private static native int jzs_get_main_theme(int def);
    private static native int jzs_get_sub_theme(int def);
    private static native void jzs_set_theme(int mainStyle, int subStyle);
    private static native int jzs_get_lockscreen(int def);
    private static native void jzs_set_lockscreen(int def);
    
    public static String get(String key) {
        if (key.length() > PROP_NAME_MAX) {
            throw new IllegalArgumentException("key.length > " + PROP_NAME_MAX);
        }
        return jzs_native_get(key);
    }

    /**
     * Get the value for the given key.
     * @return if the key isn't found, return def if it isn't null, or an empty string otherwise
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static String get(String key, String def) {
        if (key.length() > PROP_NAME_MAX) {
            throw new IllegalArgumentException("key.length > " + PROP_NAME_MAX);
        }
        return jzs_native_get(key, def);
    }

    /**
     * Get the value for the given key, and return as an integer.
     * @param key the key to lookup
     * @param def a default value to return
     * @return the key parsed as an integer, or def if the key isn't found or
     *         cannot be parsed
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static int getInt(String key, int def) {
        if (key.length() > PROP_NAME_MAX) {
            throw new IllegalArgumentException("key.length > " + PROP_NAME_MAX);
        }
        return jzs_native_get_int(key, def);
    }
    
    /**
     * Set the value for the given key.
     * @throws IllegalArgumentException if the key exceeds 32 characters
     * @throws IllegalArgumentException if the value exceeds 92 characters
     */
    public static void set(String key, String val) {
        if (key.length() > PROP_NAME_MAX) {
            throw new IllegalArgumentException("key.length > " + PROP_NAME_MAX);
        }
        if (val != null && val.length() > PROP_VALUE_MAX) {
            throw new IllegalArgumentException("val.length > " +
                PROP_VALUE_MAX);
        }
        jzs_native_set(key, val);
    }
    
    public static boolean checkPermission(){
    	return jzs_check_permission();
    }
    
    public static int getMainTheme(int def){
    	return jzs_get_main_theme(def);
    }
    
    public static int getSubTheme(int def){
    	return jzs_get_sub_theme(def);
    }
	
	public static void setJzsTheme(int mainStyle, int subStyle){
		jzs_set_theme(mainStyle, subStyle);
    }

    public static int getLockscreenStyle(int def){
    	return jzs_get_lockscreen(def);
    }
    
    public static void setLockscreenStyle(int nStyle){
    	jzs_set_lockscreen(nStyle);
    }
    

}
