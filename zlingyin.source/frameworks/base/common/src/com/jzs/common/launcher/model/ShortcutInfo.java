package com.jzs.common.launcher.model;

import java.util.ArrayList;

import com.jzs.common.launcher.IIconCache;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ShortcutInfo extends ItemInfo {
	/**
     * The intent used to start the application.
     */
	public Intent intent;

    /**
     * Indicates whether the icon comes from an application's resource (if false)
     * or from a custom Bitmap (if true.)
     */
	public boolean customIcon;

    /**
     * Indicates whether we're using the default fallback icon instead of something from the
     * app.
     */
	public boolean usingFallbackIcon;

    /**
     * If isShortcut=true and customIcon=false, this contains a reference to the
     * shortcut icon as an application's resource.
     */
	public Intent.ShortcutIconResource iconResource;

    /**
     * The application icon.
     */
    private Bitmap mIcon;
    
    public int launchedFreq = 0;
    public long lastLaunchTime = 0;

    public ShortcutInfo() {
        itemType = LauncherSettingsCommon.BaseLauncherColumns.ITEM_TYPE_SHORTCUT;
    }
    
    public ShortcutInfo(ShortcutInfo info) {
        super(info);
        title = info.title.toString();
        intent = new Intent(info.intent);
        if (info.iconResource != null) {
            iconResource = new Intent.ShortcutIconResource();
            iconResource.packageName = info.iconResource.packageName;
            iconResource.resourceName = info.iconResource.resourceName;
        }
        mIcon = info.mIcon; // TODO: should make a copy here.  maybe we don't need this ctor at all
        customIcon = info.customIcon;
        
        launchedFreq = info.launchedFreq;
        lastLaunchTime = info.lastLaunchTime;
    }

    /** TODO: Remove this.  It's only called by ApplicationInfo.makeShortcut. */
    public ShortcutInfo(ApplicationInfo info) {
        super(info);
        launchedFreq = info.launchedFreq;
        lastLaunchTime = info.lastLaunchTime;
        title = info.title.toString();
        intent = new Intent(info.intent);
        customIcon = false;
    }

    public void setIcon(Bitmap b) {
        mIcon = b;
    }

    public Bitmap getIcon(IIconCache iconCache) {
        if (mIcon == null) {
            updateIcon(iconCache);
        }
        return mIcon;
    }

    /** Returns the package name that the shortcut's intent will resolve to, or an empty string if
     *  none exists. */
    public String getPackageName() {
        return super.getPackageName(intent);
    }
    
    public String getClassName() {
        return super.getClassName(intent);
    }

    public void updateIcon(IIconCache iconCache) {
        if(iconCache != null){
            mIcon = iconCache.getIcon(intent);
            usingFallbackIcon = iconCache.isDefaultIcon(mIcon);
        }
    }

    /**
     * Creates the application intent based on a component name and various launch flags.
     * Sets {@link #itemType} to {@link LauncherSettings.BaseLauncherColumns#ITEM_TYPE_APPLICATION}.
     *
     * @param className the class name of the component representing the intent
     * @param launchFlags the launch flags
     */
    public final void setActivity(ComponentName className, int launchFlags) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
        itemType = LauncherSettingsCommon.BaseLauncherColumns.ITEM_TYPE_APPLICATION;
    }

    @Override
    public void onAddToDatabase(ContentValues values) {
        super.onAddToDatabase(values);

        String titleStr = title != null ? title.toString() : null;
        values.put(LauncherSettingsCommon.BaseLauncherColumns.TITLE, titleStr);

        String uri = intent != null ? intent.toUri(0) : null;
        values.put(LauncherSettingsCommon.BaseLauncherColumns.INTENT, uri);
        
//        values.put(LauncherSettings.Favorites.LAUNCH_FREQ, launchedFreq);
//        values.put(LauncherSettings.Favorites.LAST_LAUNCH_TIME, lastLaunchTime);

        if (customIcon) {
            values.put(LauncherSettingsCommon.BaseLauncherColumns.ICON_TYPE,
            		LauncherSettingsCommon.BaseLauncherColumns.ICON_TYPE_BITMAP);
            writeBitmap(values, mIcon);
        } else {
            if (!usingFallbackIcon) {
                writeBitmap(values, mIcon);
            }
            values.put(LauncherSettingsCommon.BaseLauncherColumns.ICON_TYPE,
            		LauncherSettingsCommon.BaseLauncherColumns.ICON_TYPE_RESOURCE);
            if (iconResource != null) {
                values.put(LauncherSettingsCommon.BaseLauncherColumns.ICON_PACKAGE,
                        iconResource.packageName);
                values.put(LauncherSettingsCommon.BaseLauncherColumns.ICON_RESOURCE,
                        iconResource.resourceName);
            }
        }
    }

    @Override
    public String toString() {
        return "ShortcutInfo(title=" + (title == null ? "" : title.toString()) + "intent=" + intent + "id=" + this.id
                + " type=" + this.itemType + " container=" + this.container + " screen=" + screen
                + " cellX=" + cellX + " cellY=" + cellY + " spanX=" + spanX + " spanY=" + spanY
                + " dropPos=" + dropPos+")";
    }

    public static void dumpShortcutInfoList(String tag, String label,
            ArrayList<ShortcutInfo> list) {
        android.util.Log.d(tag, label + " size=" + list.size());
        for (ShortcutInfo info: list) {
        	android.util.Log.d(tag, "   title=\"" + info.title + " icon=" + info.mIcon
                    + " customIcon=" + info.customIcon);
        }
    }
    
    public ShortcutInfo(Parcel in){
    	super(in);
    	//firstInstallTime = in.readLong();
    	customIcon = in.readInt() > 0 ? true : false;
    	launchedFreq = in.readInt();
    	lastLaunchTime = in.readLong();
//    	if(in.readInt() > 0)
//    		componentName = ComponentName.CREATOR.createFromParcel(in);
//    	else
//    		componentName = null;
    	if(in.readInt() > 0)
    		intent = Intent.CREATOR.createFromParcel(in);
    	else
    		intent = null;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	super.writeToParcel(dest, flags);
		//dest.writeLong(firstInstallTime);
		dest.writeInt(customIcon ? 1 : 0);
		dest.writeInt(launchedFreq);
		dest.writeLong(lastLaunchTime);
		
		if(intent != null){
			dest.writeInt(1);
			intent.writeToParcel(dest, flags);
		} else {
			dest.writeInt(0);
		}
	}
    
    public int describeContents() {
        return 0;
    }
    
	public static final Parcelable.Creator<ShortcutInfo> CREATOR = new Parcelable.Creator<ShortcutInfo>() {
		public ShortcutInfo createFromParcel(Parcel source) {
			return new ShortcutInfo(source);
		}

		public ShortcutInfo[] newArray(int size) {
			return new ShortcutInfo[size];
		}
	};
}
