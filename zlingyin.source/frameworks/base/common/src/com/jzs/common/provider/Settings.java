package com.jzs.common.provider;

import java.net.URISyntaxException;
import java.util.HashMap;

import com.jzs.common.os.JzsSystemProperties;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
//import android.content.IContentProvider;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
//import android.os.UserHandle;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.AndroidException;
import android.util.Log;

public class Settings {
    
    
    public static final String PARAMETER_NOTIFY = "notify";
	/**
     * @hide - Private call() method on SettingsProvider to read from 'system' table.
     */
    public static final String CALL_METHOD_GET_SYSTEM = "GET_system";

    /**
     * @hide - Private call() method on SettingsProvider to read from 'secure' table.
     */
    public static final String CALL_METHOD_GET_PLUGINS = "GET_plugin";

    /**
     * @hide - Private call() method on SettingsProvider to read from 'global' table.
     */
    public static final String CALL_METHOD_GET_ICON = "GET_icon";
    
    /**
     * @hide - Private call() method on SettingsProvider to read from 'global' table.
     */
    public static final String CALL_METHOD_GET_TITLE = "GET_title";
    

    /**
     * @hide - User handle argument extra to the fast-path call()-based requests
     */
    public static final String CALL_METHOD_USER_KEY = "_user";

    /** @hide - Private call() method to write to 'system' table */
    public static final String CALL_METHOD_PUT_SYSTEM = "PUT_system";

    /** @hide - Private call() method to write to 'secure' table */
    public static final String CALL_METHOD_PUT_PLUGINS = "PUT_plugin";

    /** @hide - Private call() method to write to 'global' table */
    public static final String CALL_METHOD_PUT_ICON= "PUT_icon";
    
    /** @hide - Private call() method to write to 'global' table */
    public static final String CALL_METHOD_PUT_TITLE= "PUT_title";
    
	public static final String AUTHORITY = "jzs.settings";
	
	/**
     * @hide 
     */
	public static final Uri SYSTEM_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/jzssystem");
	
	/**
     * @hide 
     */
	public static final Uri PLUGIN_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/jzsplugins"
                    + "?" + PARAMETER_NOTIFY + "=false");
	/**
     * @hide 
     */
	public static final Uri ICON_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/jzsicons"
                    + "?" + PARAMETER_NOTIFY + "=false");
	
	/**
     * @hide 
     */
    public static final Uri TITLE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/jzstitles"
                    + "?" + PARAMETER_NOTIFY + "=false");

	protected static final String TAG = "Jzs.Provider/Settings";
    
    private static final boolean LOCAL_LOGV = true;
    
    public static class SettingNotFoundException extends AndroidException {
        public SettingNotFoundException(String msg) {
            super(msg);
        }
    }

    /**
     * Common base for tables of name/value settings.
     */
    public static class NameValueTable implements BaseColumns {
        public static final String NAME = "name";
        public static final String VALUE = "value";

        protected static boolean putString(ContentResolver resolver, Uri uri,
                String name, String value) {
            // The database will take care of replacing duplicates.
            try {
                ContentValues values = new ContentValues();
                values.put(NAME, name);
                values.put(VALUE, value);
                resolver.insert(uri, values);
                return true;
            } catch (SQLException e) {
                Log.w(TAG, "Can't set key " + name + " in " + uri, e);
                return false;
            }
        }

        public static Uri getUriFor(Uri uri, String name) {
            return Uri.withAppendedPath(uri, name);
        }
    }

    // Thread-safe.
    private static class NameValueCache {
        //private final String mVersionSystemProperty;
        private final Uri mUri;

        private static final String[] SELECT_VALUE =
            new String[] { Settings.NameValueTable.VALUE };
        private static final String NAME_EQ_PLACEHOLDER = "name=?";

        // Must synchronize on 'this' to access mValues and mValuesVersion.
        private final HashMap<String, String> mValues = new HashMap<String, String>();
        //private long mValuesVersion = 0;

        // Initially null; set lazily and held forever.  Synchronized on 'this'.
        //private IContentProvider mContentProvider = null;

        // The method we'll call (or null, to not use) on the provider
        // for the fast path of retrieving settings.
        private final String mCallGetCommand;
        private final String mCallSetCommand;

        public NameValueCache(String versionSystemProperty, Uri uri,
                String getCommand, String setCommand) {
            //mVersionSystemProperty = versionSystemProperty;
            mUri = uri;
            mCallGetCommand = getCommand;
            mCallSetCommand = setCommand;
        }

        public boolean removeString(ContentResolver cr, String name){
            if(TextUtils.isEmpty(name))
                return false;
            
        	try {
        		if(LOCAL_LOGV){
        			Log.d(TAG,"remove string name = " + name);
        		}
                cr.delete(mUri, NAME_EQ_PLACEHOLDER, new String[]{name});
                synchronized (this) {
                    mValues.remove(name);//.clear();
                }
            } catch (Exception e) {
                Log.w(TAG, "Can't set key " + name + " in " + mUri, e);
                return false;
            }
            return true;
        }
        
        public boolean removeBeginWithString(ContentResolver cr, String name){
            if(TextUtils.isEmpty(name))
                return false;
            
            try {
                if(LOCAL_LOGV)
                    Log.d(TAG,"remove string name = " + name);
                cr.delete(mUri, NameValueTable.NAME+" like '"+name+"%'", null);
                synchronized (this) {
                    mValues.clear();
                }
            } catch (Exception e) {
                Log.w(TAG, "Can't set key " + name + " in " + mUri, e);
                return false;
            }
            return true;
        }
        
        public boolean putStringForUser(ContentResolver cr, String name, String value,
                final int userHandle) {
        	
            try {
            	if(LOCAL_LOGV){
            		Log.d(TAG,"put string name = " + name + " , value = " + value + " userHandle = " + userHandle);
            	}
                if(TextUtils.isEmpty(value))
                	return removeString(cr, name);
                
                Bundle arg = new Bundle();
                arg.putString(Settings.NameValueTable.VALUE, value);
                //arg.putInt(CALL_METHOD_USER_KEY, userHandle);
                cr.call(mUri, mCallSetCommand, name, arg);
                synchronized (this) {
                    mValues.remove(name);
                }
//                IContentProvider cp = lazyGetProvider(cr);
//                cp.call(mCallSetCommand, name, arg);
            } catch (Exception e) {
                Log.w(TAG, "Can't set key " + name + " in " + mUri, e);
                return false;
            }
            return true;
        }

        public String getStringForUser(ContentResolver cr, String name, final int userHandle) {
            final boolean isSelf = true;//(userHandle == UserHandle.myUserId());
            if (isSelf) {
                //long newValuesVersion = JzsSystemProperties.getInt(mVersionSystemProperty, 0);

                // Our own user's settings data uses a client-side cache
                synchronized (this) {
                    if (mValues.containsKey(name)) {
                        /// M: for more log to debug
                        String value = mValues.get(name);
                        Log.v(TAG," from settings cache , name = " + name + " , value = " + value);
                        return value;  // Could be null, that's OK -- negative caching
                    }
                }
            } else {
//                if (LOCAL_LOGV) Log.v(TAG, "get setting for user " + userHandle
//                        + " by user " + UserHandle.myUserId() + " so skipping cache");
            }

            Cursor c = null;
            try {
                c = cr.query(mUri, SELECT_VALUE, NAME_EQ_PLACEHOLDER, new String[]{name}, null, null);
                		//cp.query(mUri, SELECT_VALUE, NAME_EQ_PLACEHOLDER,
                         //    new String[]{name}, null, null);
                if (c == null) {
                    Log.w(TAG, "Can't get key " + name + " from " + mUri);
                    return null;
                }

                String value = c.moveToNext() ? c.getString(0) : null;
                synchronized (this) {
                    mValues.put(name, value);
                }
                if (LOCAL_LOGV) {
                    Log.v(TAG, "cache miss [" + mUri.getLastPathSegment() + "]: " +
                            name + " = " + (value == null ? "(null)" : value));
                    Log.v(TAG,"from db ,name = " + name+" , value = "+ value);
                }
                
                return value;           
            } finally {
                if (c != null) c.close();
            }
        }
    }
    
    public static final class System extends NameValueTable {
        public static final String SYS_PROP_SETTING_VERSION = "sys.settings_jzssystem_version";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/jzssystem");

        private static final NameValueCache sNameValueCache = new NameValueCache(
                SYS_PROP_SETTING_VERSION,
                CONTENT_URI,
                CALL_METHOD_GET_SYSTEM,
                CALL_METHOD_PUT_SYSTEM);
        
        public static String getString(ContentResolver resolver, String name) {
            return getStringForUser(resolver, name, 0);
        }

        /** @hide */
        public static String getStringForUser(ContentResolver resolver, String name,
                int userHandle) {
            return sNameValueCache.getStringForUser(resolver, name, userHandle);
        }

        /**
         * Store a name/value pair into the database.
         * @param resolver to access the database with
         * @param name to store
         * @param value to associate with the name
         * @return true if the value was set, false on database errors
         */
        public static boolean putString(ContentResolver resolver, String name, String value) {
            return putStringForUser(resolver, name, value, 0);
        }

        /** @hide */
        public static boolean putStringForUser(ContentResolver resolver, String name, String value,
                int userHandle) {
            return sNameValueCache.putStringForUser(resolver, name, value, userHandle);
        }

        /**
         * Construct the content URI for a particular name/value pair,
         * useful for monitoring changes with a ContentObserver.
         * @param name to look up in the table
         * @return the corresponding content URI, or null if not present
         */
        public static Uri getUriFor(String name) {
            return getUriFor(CONTENT_URI, name);
        }
        
        public static int getInt(ContentResolver cr, String name, int def) {
            return getIntForUser(cr, name, def, 0);
        }

        /** @hide */
        public static int getIntForUser(ContentResolver cr, String name, int def, int userHandle) {
            String v = getStringForUser(cr, name, userHandle);
            try {
                return v != null ? Integer.parseInt(v) : def;
            } catch (NumberFormatException e) {
                return def;
            }
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as an integer.  Note that internally setting values are always
         * stored as strings; this function converts the string to an integer
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr The ContentResolver to access.
         * @param name The name of the setting to retrieve.
         *
         * @throws SettingNotFoundException Thrown if a setting by the given
         * name can't be found or the setting value is not an integer.
         *
         * @return The setting's current value.
         */
        public static int getInt(ContentResolver cr, String name)
                throws SettingNotFoundException {
            return getIntForUser(cr, name, 0);
        }

        /** @hide */
        public static int getIntForUser(ContentResolver cr, String name, int userHandle)
                throws SettingNotFoundException {
            String v = getStringForUser(cr, name, userHandle);
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as an
         * integer. This will either create a new entry in the table if the
         * given name does not exist, or modify the value of the existing row
         * with that name.  Note that internally setting values are always
         * stored as strings, so this function converts the given value to a
         * string before storing it.
         *
         * @param cr The ContentResolver to access.
         * @param name The name of the setting to modify.
         * @param value The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putInt(ContentResolver cr, String name, int value) {
            return putIntForUser(cr, name, value, 0);
        }

        /** @hide */
        public static boolean putIntForUser(ContentResolver cr, String name, int value,
                int userHandle) {
            return putStringForUser(cr, name, Integer.toString(value), userHandle);
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a {@code long}.  Note that internally setting values are always
         * stored as strings; this function converts the string to a {@code long}
         * for you.  The default value will be returned if the setting is
         * not defined or not a {@code long}.
         *
         * @param cr The ContentResolver to access.
         * @param name The name of the setting to retrieve.
         * @param def Value to return if the setting is not defined.
         *
         * @return The setting's current value, or 'def' if it is not defined
         * or not a valid {@code long}.
         */
        public static long getLong(ContentResolver cr, String name, long def) {
            return getLongForUser(cr, name, def, 0);
        }

        /** @hide */
        public static long getLongForUser(ContentResolver cr, String name, long def,
                int userHandle) {
            String valString = getStringForUser(cr, name, userHandle);
            long value;
            try {
                value = valString != null ? Long.parseLong(valString) : def;
            } catch (NumberFormatException e) {
                value = def;
            }
            return value;
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a {@code long}.  Note that internally setting values are always
         * stored as strings; this function converts the string to a {@code long}
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr The ContentResolver to access.
         * @param name The name of the setting to retrieve.
         *
         * @return The setting's current value.
         * @throws SettingNotFoundException Thrown if a setting by the given
         * name can't be found or the setting value is not an integer.
         */
        public static long getLong(ContentResolver cr, String name)
                throws SettingNotFoundException {
            return getLongForUser(cr, name, 0);
        }

        /** @hide */
        public static long getLongForUser(ContentResolver cr, String name, int userHandle)
                throws SettingNotFoundException {
            String valString = getStringForUser(cr, name, userHandle);
            try {
                return Long.parseLong(valString);
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as a long
         * integer. This will either create a new entry in the table if the
         * given name does not exist, or modify the value of the existing row
         * with that name.  Note that internally setting values are always
         * stored as strings, so this function converts the given value to a
         * string before storing it.
         *
         * @param cr The ContentResolver to access.
         * @param name The name of the setting to modify.
         * @param value The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putLong(ContentResolver cr, String name, long value) {
            return putLongForUser(cr, name, value, 0);
        }

        /** @hide */
        public static boolean putLongForUser(ContentResolver cr, String name, long value,
                int userHandle) {
            return putStringForUser(cr, name, Long.toString(value), userHandle);
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a floating point number.  Note that internally setting values are
         * always stored as strings; this function converts the string to an
         * float for you. The default value will be returned if the setting
         * is not defined or not a valid float.
         *
         * @param cr The ContentResolver to access.
         * @param name The name of the setting to retrieve.
         * @param def Value to return if the setting is not defined.
         *
         * @return The setting's current value, or 'def' if it is not defined
         * or not a valid float.
         */
        public static float getFloat(ContentResolver cr, String name, float def) {
            return getFloatForUser(cr, name, def, 0);
        }

        /** @hide */
        public static float getFloatForUser(ContentResolver cr, String name, float def,
                int userHandle) {
            String v = getStringForUser(cr, name, userHandle);
            try {
                return v != null ? Float.parseFloat(v) : def;
            } catch (NumberFormatException e) {
                return def;
            }
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a float.  Note that internally setting values are always
         * stored as strings; this function converts the string to a float
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr The ContentResolver to access.
         * @param name The name of the setting to retrieve.
         *
         * @throws SettingNotFoundException Thrown if a setting by the given
         * name can't be found or the setting value is not a float.
         *
         * @return The setting's current value.
         */
        public static float getFloat(ContentResolver cr, String name)
                throws SettingNotFoundException {
            return getFloatForUser(cr, name, 0);
        }

        /** @hide */
        public static float getFloatForUser(ContentResolver cr, String name, int userHandle)
                throws SettingNotFoundException {
            String v = getStringForUser(cr, name, userHandle);
            if (v == null) {
                throw new SettingNotFoundException(name);
            }
            try {
                return Float.parseFloat(v);
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as a
         * floating point number. This will either create a new entry in the
         * table if the given name does not exist, or modify the value of the
         * existing row with that name.  Note that internally setting values
         * are always stored as strings, so this function converts the given
         * value to a string before storing it.
         *
         * @param cr The ContentResolver to access.
         * @param name The name of the setting to modify.
         * @param value The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putFloat(ContentResolver cr, String name, float value) {
            return putFloatForUser(cr, name, value, 0);
        }

        /** @hide */
        public static boolean putFloatForUser(ContentResolver cr, String name, float value,
                int userHandle) {
            return putStringForUser(cr, name, Float.toString(value), userHandle);
        }

        public static boolean removeString(ContentResolver cr, String name) {
            return sNameValueCache.removeString(cr, name);
        }
        
        public static boolean removeStringBeginWith(ContentResolver resolver, String prefix){
            return sNameValueCache.removeBeginWithString(resolver, prefix);
        }
        
        /**
         * @hide
         */
        public final static String FLOAT_ENABLE_SHORTCUT_KEY = "jzs_enable_circle_sc_key";

        /**
         * @hide
         */
		public static final String QS_ENABLE_LEDS_LIGHTS = "qs_enable_leds";
		
		/**
         * @hide
         */
		public static final String ENABLE_PHONE_COVER = "qs_enable_phone_cover";
		
		/**
         * @hide
         */
		public static final String ENABLE_TP_GESTURE = "dui_enable_tp_gesture";
		
		
		/**
         * @hide
         */
		public static final String PS_GESTURE_STATUS = "dui_ps_gesture_enable_status";
		
		/**
         * @hide
         */
		public static final String PS_GESTURE_PARAMETERS = "dui_ps_gesture_values";
		
    }

    /**
     * User-defined bookmarks and shortcuts.  The target of each bookmark is an
     * Intent URL, allowing it to be either a web page or a particular
     * application activity.
     *
     * @hide
     */
    public static final class ShortcutBookmarks implements BaseColumns
    {
        private static final String TAG = "ShortcutBookmarks";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/bookmarks");

        /**
         * The row ID.
         * <p>Type: INTEGER</p>
         */
        public static final String ID = "_id";

        /**
         * Descriptive name of the bookmark that can be displayed to the user.
         * If this is empty, the title should be resolved at display time (use
         * {@link #getTitle(Context, Cursor)} any time you want to display the
         * title of a bookmark.)
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String TITLE = "title";

        /**
         * Arbitrary string (displayed to the user) that allows bookmarks to be
         * organized into categories.  There are some special names for
         * standard folders, which all start with '@'.  The label displayed for
         * the folder changes with the locale (via {@link #getLabelForFolder}) but
         * the folder name does not change so you can consistently query for
         * the folder regardless of the current locale.
         *
         * <P>Type: TEXT</P>
         *
         */
        public static final String FOLDER = "folder";

        /**
         * The Intent URL of the bookmark, describing what it points to.  This
         * value is given to {@link android.content.Intent#getIntent} to create
         * an Intent that can be launched.
         * <P>Type: TEXT</P>
         */
        public static final String INTENT = "intent";

        /**
         * Optional shortcut character associated with this bookmark.
         * <P>Type: INTEGER</P>
         */
        public static final String SHORTCUT = "shortcut";

        /**
         * The order in which the bookmark should be displayed
         * <P>Type: INTEGER</P>
         */
        public static final String ORDERING = "ordering";
        
        public static final String USERRIGHT = "userright";

        private static final String[] sIntentProjection = { INTENT };
        public static final String[] sShortcutProjection = { ID, SHORTCUT, USERRIGHT};
        public static final String sShortcutSelection = SHORTCUT + "=?";

        /**
         * Convenience function to retrieve the bookmarked Intent for a
         * particular shortcut key.
         *
         * @param cr The ContentResolver to query.
         * @param shortcut The shortcut key.
         *
         * @return Intent The bookmarked URL, or null if there is no bookmark
         *         matching the given shortcut.
         */
        public static Intent getIntentForShortcut(ContentResolver cr, int shortcut)
        {
            Intent intent = null;

            Cursor c = cr.query(CONTENT_URI,
                    sIntentProjection, sShortcutSelection,
                    new String[] { String.valueOf((int) shortcut) }, ORDERING);
            // Keep trying until we find a valid shortcut
            try {
                while (intent == null && c.moveToNext()) {
                    try {
                        String intentURI = c.getString(c.getColumnIndexOrThrow(INTENT));
                        if(!TextUtils.isEmpty(intentURI)){
                        	intent = Intent.parseUri(intentURI, 0);
                        }
                    } catch (java.net.URISyntaxException e) {
                        // The stored URL is bad...  ignore it.
                    } catch (IllegalArgumentException e) {
                        // Column not found
                        Log.w(TAG, "Intent column not found", e);
                    }
                }
            } finally {
                if (c != null) c.close();
            }

            return intent;
        }
        
        public static Uri add(ContentResolver cr,
				                Intent intent,
				                int shortcut,
				                int ordering){
        	return add(cr, intent, null, null, shortcut, ordering);
        }

        /**
         * Add a new bookmark to the system.
         *
         * @param cr The ContentResolver to query.
         * @param intent The desired target of the bookmark.
         * @param title Bookmark title that is shown to the user; null if none
         *            or it should be resolved to the intent's title.
         * @param folder Folder in which to place the bookmark; null if none.
         * @param shortcut Shortcut that will invoke the bookmark; 0 if none. If
         *            this is non-zero and there is an existing bookmark entry
         *            with this same shortcut, then that existing shortcut is
         *            cleared (the bookmark is not removed).
         * @return The unique content URL for the new bookmark entry.
         */
        public static Uri add(ContentResolver cr,
                                           Intent intent,
                                           String title,
                                           String folder,
                                           int shortcut,
                                           int ordering)
        {
            // If a shortcut is supplied, and it is already defined for
            // another bookmark, then remove the old definition.
            if (shortcut > -1) {
                cr.delete(CONTENT_URI, sShortcutSelection,
                        new String[] { String.valueOf((int) shortcut) });
            }

            ContentValues values = new ContentValues();
            if (title != null) values.put(TITLE, title);
            if (folder != null) values.put(FOLDER, folder);
            if(intent != null)
				values.put(INTENT, intent.toUri(0));
			else
				values.put(INTENT, "");
            if (shortcut != 0) values.put(SHORTCUT, (int) shortcut);
            values.put(ORDERING, ordering);
            return cr.insert(CONTENT_URI, values);
        }
        
        public static int update(ContentResolver cr, int shortcut,
		                Intent intent, String title)
		{
        	if (shortcut < 0) return 0;
        	
			ContentValues values = new ContentValues();
			if (title != null) values.put(TITLE, title);
			//if (folder != null) values.put(FOLDER, folder);
			if(intent != null)
				values.put(INTENT, intent.toUri(0));
			else
				values.put(INTENT, "");
			
			return cr.update(CONTENT_URI, values, 
							sShortcutSelection,
	                    	new String[] { String.valueOf(shortcut) });
		}

        /**
         * Return the folder name as it should be displayed to the user.  This
         * takes care of localizing special folders.
         *
         * @param r Resources object for current locale; only need access to
         *          system resources.
         * @param folder The value found in the {@link #FOLDER} column.
         *
         * @return CharSequence The label for this folder that should be shown
         *         to the user.
         */
        public static CharSequence getLabelForFolder(Resources r, String folder) {
            return folder;
        }

        /**
         * Return the title as it should be displayed to the user. This takes
         * care of localizing bookmarks that point to activities.
         *
         * @param context A context.
         * @param cursor A cursor pointing to the row whose title should be
         *        returned. The cursor must contain at least the {@link #TITLE}
         *        and {@link #INTENT} columns.
         * @return A title that is localized and can be displayed to the user,
         *         or the empty string if one could not be found.
         */
        public static CharSequence getTitle(Context context, Cursor cursor) {
            int titleColumn = cursor.getColumnIndex(TITLE);
            
//            if (titleColumn == -1 || intentColumn == -1) {
//                throw new IllegalArgumentException(
//                        "The cursor must contain the TITLE and INTENT columns.");
//            }
            
            String title = "";
            if(titleColumn != -1){
	            title = cursor.getString(titleColumn);
	            if (!TextUtils.isEmpty(title)) {
	                return title;
	            }
            }

            int intentColumn = cursor.getColumnIndex(INTENT);
            if(intentColumn != -1){
	            String intentUri = cursor.getString(intentColumn);
	            if (TextUtils.isEmpty(intentUri)) {
	                return "";
	            }
	
	            Intent intent;
	            try {
	                intent = Intent.parseUri(intentUri, 0);
	            } catch (URISyntaxException e) {
	                return "";
	            }
	            
	
	            PackageManager packageManager = context.getPackageManager();
	            ResolveInfo info = packageManager.resolveActivity(intent, 0);
	            return info != null ? info.loadLabel(packageManager) : "";
            }
            
            return "";
        }
    }
}
