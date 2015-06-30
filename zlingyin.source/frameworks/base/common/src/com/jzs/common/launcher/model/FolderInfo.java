package com.jzs.common.launcher.model;

import java.util.ArrayList;

import com.jzs.common.launcher.IIconCache;
import com.jzs.common.manager.IAppsManager;

import android.content.ComponentName;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.text.TextUtils;

public class FolderInfo extends ItemInfo {

	 /**
     * Whether this folder has been opened
     */
	public boolean opened;
	public ComponentName folderTypeKey;
	private Bitmap mIcon;

    /**
     * The apps and shortcuts
     */
	public ArrayList<ShortcutInfo> contents = new ArrayList<ShortcutInfo>();

	public ArrayList<FolderListener> listeners = new ArrayList<FolderListener>();

    public FolderInfo() {
    	this(null, null);
    }
    
    public FolderInfo(String packageName, String className) {
        itemType = LauncherSettingsCommon.Favorites.ITEM_TYPE_FOLDER;
        if(TextUtils.isEmpty(packageName) || TextUtils.isEmpty(className)){
            folderTypeKey = new ComponentName(IAppsManager.IC_FOLDER_PKG_NAME, IAppsManager.IC_FOLDER_COMMON_CLASS_NAME);
        } else {
            folderTypeKey = new ComponentName(packageName, className);
        }
    }
    
    public void setIcon(Bitmap b) {
        mIcon = b;
    }

    public Bitmap getIcon(IIconCache iconCache) {
        if (mIcon == null) {
        	mIcon = iconCache.getIcon(folderTypeKey);
        }
        return mIcon;
    }

    /**
     * Add an app or shortcut
     *
     * @param item
     */
    public void add(ShortcutInfo item) {
        contents.add(item);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onAdd(item);
        }
        itemsChanged();
    }

    /**
     * Remove an app or shortcut. Does not change the DB.
     *
     * @param item
     */
    public void remove(ShortcutInfo item) {
        contents.remove(item);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onRemove(item);
        }
        itemsChanged();
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onTitleChanged(title);
        }
    }

    @Override
    public void onAddToDatabase(ContentValues values) {
        super.onAddToDatabase(values);
        values.put(LauncherSettingsCommon.Favorites.TITLE, title.toString());
        if(folderTypeKey != null){
            values.put(LauncherSettingsCommon.Favorites.ICON_PACKAGE, folderTypeKey.getPackageName());
        	values.put(LauncherSettingsCommon.Favorites.ICON_RESOURCE, folderTypeKey.getClassName());
        }
    }

    public void addListener(FolderListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FolderListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void itemsChanged() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onItemsChanged();
        }
    }

    @Override
    public void unbind() {
        super.unbind();
        listeners.clear();
    }

    public interface FolderListener {
        public void onAdd(ShortcutInfo item);
        public void onRemove(ShortcutInfo item);
        public void onTitleChanged(CharSequence title);
        public void onItemsChanged();
    }
    
}
