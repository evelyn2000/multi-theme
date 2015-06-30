package com.jzs.common.launcher.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jzs.common.launcher.IIconCache;
import com.jzs.common.launcher.model.IconCacheEntry;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class ApplicationInfo extends ItemInfo {
	
	/**
     * The intent used to start the application.
     */
	public Intent intent;

    /**
     * A bitmap version of the application icon.
     */
	public Bitmap iconBitmap;

    /**
     * The time at which the app was first installed.
     */
	public long firstInstallTime;

    public ComponentName componentName;

    public static final int DOWNLOADED_FLAG = 1;
    public static final int UPDATED_SYSTEM_APP_FLAG = 2;

    public int flags = 0;
    
    public int launchedFreq = 0;
    public long lastLaunchTime = 0;
    
    public ApplicationInfo() {
        itemType = LauncherSettingsCommon.BaseLauncherColumns.ITEM_TYPE_APPLICATION;
    }
    
    /**
     * Must not hold the Context.
     */
    public ApplicationInfo(PackageManager pm, ResolveInfo info, IIconCache iconCache,
            HashMap<Object, CharSequence> labelCache) {
        final String packageName = info.activityInfo.applicationInfo.packageName;

        this.componentName = new ComponentName(packageName, info.activityInfo.name);
        this.container = ItemInfo.NO_ID;
        this.setActivity(componentName,
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        try {
            int appFlags = pm.getApplicationInfo(packageName, 0).flags;
            if ((appFlags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0) {
                flags |= DOWNLOADED_FLAG;

                if ((appFlags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    flags |= UPDATED_SYSTEM_APP_FLAG;
                }
            }
            firstInstallTime = pm.getPackageInfo(packageName, 0).firstInstallTime;
        } catch (NameNotFoundException e) {
        	android.util.Log.d("ApplicationInfo", "PackageManager.getApplicationInfo failed for " + packageName);
        }
		if(iconCache != null){
	        iconCache.remove(componentName);
	        iconCache.getTitleAndIcon(this, info, labelCache);
		}
    }

    public ApplicationInfo(ApplicationInfo info) {
        super(info);
        componentName = info.componentName;
        title = info.title.toString();
        intent = new Intent(info.intent);
        flags = info.flags;
        firstInstallTime = info.firstInstallTime;
        
        launchedFreq = info.launchedFreq;
        lastLaunchTime = info.lastLaunchTime;
    }

    /** Returns the package name that the shortcut's intent will resolve to, or an empty string if
     *  none exists. */
    public String getPackageName() {
    	if(componentName != null)
    		return componentName.getPackageName();
        return super.getPackageName(intent);
    }
    
    public String getClassName() {
    	if(componentName != null)
    		return componentName.getClassName();
    	
    	if (intent != null) {
            if(intent.getComponent() != null) {
                return intent.getComponent().getClassName();
            }
        }
        return "";
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
    public String toString() {
        return "ApplicationInfo(title=" + title.toString() + (componentName != null ? componentName.toShortString() : "") + ")";
    }

    public static void dumpApplicationInfoList(final String tag, final String label,
            final List<ApplicationInfo> list) {
    	android.util.Log.d(tag, label + " size=" + list.size());
        for (ApplicationInfo info: list) {
        	android.util.Log.d(tag, "   title=\"" + info.title + "\" iconBitmap="
                    + info.iconBitmap + " firstInstallTime="
                    + info.firstInstallTime);
        }
    }
    
    public boolean isDownloadApp(){
    	return flags > 0;
    }
    
    public static boolean isDownloadApp(PackageManager pm, String packageName){
        if(pm != null && !TextUtils.isEmpty(packageName)){
            try {
                int appFlags = pm.getApplicationInfo(packageName, 0).flags;
                if ((appFlags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0) {
                    return true;
    
    //                if ((appFlags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
    //                    flags |= UPDATED_SYSTEM_APP_FLAG;
    //                }
                }
                //firstInstallTime = pm.getPackageInfo(packageName, 0).firstInstallTime;
            } catch (NameNotFoundException e) {
                android.util.Log.d("ApplicationInfo", "PackageManager.getApplicationInfo failed for " + packageName);
            }
        }
        return false;
    }

    @Override
    public void onAddToDatabase(ContentValues values) {
        super.onAddToDatabase(values);
        
        values.put(LauncherSettingsCommon.Favorites.LAUNCH_FREQ, launchedFreq);
        values.put(LauncherSettingsCommon.Favorites.LAST_LAUNCH_TIME, lastLaunchTime);
    }
    
    public ShortcutInfo makeShortcut() {
        return new ShortcutInfo(this);
    }
    
    public ApplicationInfo(Parcel in){
    	super(in);
    	firstInstallTime = in.readLong();
    	flags = in.readInt();
    	launchedFreq = in.readInt();
    	lastLaunchTime = in.readLong();
    	if(in.readInt() > 0)
    		componentName = ComponentName.CREATOR.createFromParcel(in);
    	else
    		componentName = null;
    	if(in.readInt() > 0)
    		intent = Intent.CREATOR.createFromParcel(in);
    	else
    		intent = null;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	super.writeToParcel(dest, flags);
		dest.writeLong(firstInstallTime);
		dest.writeInt(flags);
		dest.writeInt(launchedFreq);
		dest.writeLong(lastLaunchTime);
		if(componentName != null){
			dest.writeInt(1);
			componentName.writeToParcel(dest, flags);
		} else {
			dest.writeInt(0);
		}
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
    
	public static final Parcelable.Creator<ApplicationInfo> CREATOR = new Parcelable.Creator<ApplicationInfo>() {
		public ApplicationInfo createFromParcel(Parcel source) {
			return new ApplicationInfo(source);
		}

		public ApplicationInfo[] newArray(int size) {
			return new ApplicationInfo[size];
		}
	};
}
