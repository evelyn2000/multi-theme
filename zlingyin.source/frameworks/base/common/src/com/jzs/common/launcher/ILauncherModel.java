package com.jzs.common.launcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.jzs.common.launcher.model.ApplicationInfo;
import com.jzs.common.launcher.model.FolderInfo;
import com.jzs.common.launcher.model.ILauncherModelCallbacks;
import com.jzs.common.launcher.model.IPackageUpdatedCallbacks;
import com.jzs.common.launcher.model.ItemInfo;
import com.jzs.common.launcher.model.ShortcutInfo;

public interface ILauncherModel extends IExtHelperBase {
	void ReleaseInstance();
	void onTrimMemory(int level);
	
	boolean isAllAppsLoaded();
	boolean isLoadingWorkspace();
	
	List<ApplicationInfo> getAllAppsList();
	
	ShortcutInfo getShortcutInfo(PackageManager manager, Intent intent, Context context);
	ShortcutInfo addShortcut(Intent data, long container, int screen,
            int cellX, int cellY, boolean notify);
	ShortcutInfo infoFromShortcutIntent(Context context, Intent data, Bitmap fallbackIcon);
	FolderInfo getFolderById(Context context, HashMap<Long,FolderInfo> folderList, long id);
	
	void setPackageUpdateCallback(IPackageUpdatedCallbacks callback);
	void setCallbacks(ILauncherModelCallbacks callbacks);
	void startLoader(boolean isLaunching, int synchronousBindPage);
	void startLoader(boolean isLaunching, int synchronousBindPage, boolean synchronousBindApps);
	void stopLoader();
	void forceReload();
	void resetLoadedState(boolean resetAllAppsLoaded, boolean resetWorkspaceLoaded);
	void startLoaderFromBackground();
	
	void setFlushCache();
	void flushCacheIfNeeded(HashMap<Object, CharSequence> labelCache);
	
	void bindRemainingSynchronousPages();
	void unbindItemInfosAndClearQueuedBindRunnables();
	void unbindWorkspaceItemsOnMainThread();
	
	void addOrMoveItemInDatabase(ItemInfo item, long container,
            int screen, int cellX, int cellY);
	void addItemToDatabase(final ItemInfo item, final long container,
            final int screen, final int cellX, final int cellY, final boolean notify);
	void modifyItemInDatabase(final ItemInfo item, final long container,
            final int screen, final int cellX, final int cellY, final int spanX, final int spanY);
	void moveItemInDatabase(final ItemInfo item, final long container,
            final int screen, final int cellX, final int cellY);
	void updateItemInDatabase(final ItemInfo item);
	
	void deleteItemFromDatabase(final ItemInfo item);
	void deleteFolderContentsFromDatabase(final FolderInfo info);
	
    void checkItemInfo(final ItemInfo item);

    ArrayList<ItemInfo> getWorkspaceShortcutItemInfosWithIntent(Intent intent);

    Comparator<ApplicationInfo> getApplicationComparator();
    void sortApplicationItems(List<ApplicationInfo> data);
    void reBindAllApplications();
    void reBindWorkspace();
}
