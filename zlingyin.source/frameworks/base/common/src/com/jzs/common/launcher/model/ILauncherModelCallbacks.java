package com.jzs.common.launcher.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public interface ILauncherModelCallbacks {
	
	public boolean setLoadOnResume();
    public int getCurrentWorkspaceScreen();
    public void startBinding();
    public void bindItems(List<ItemInfo> shortcuts, int start, int end);
    public void bindFolders(HashMap<Long,FolderInfo> folders);
    public void finishBindingItems();
    public void bindAppWidget(AppWidgetInfo info);
    public void bindAllApplications(List<ApplicationInfo> apps);
    public void bindAppsAdded(List<ApplicationInfo> apps);
    public void bindAppsUpdated(List<ApplicationInfo> apps);
    public void bindAppsRemoved(List<String> packageNames, boolean permanent);
    public void bindPackagesUpdated();
    public boolean isAllAppsVisible();
    public boolean isAllAppsButtonRank(int rank);
    public void bindSearchablesChanged();
    public void onPageBoundSynchronously(int page);
    /// M: added the new callback fun for the scene feature.
    //public void switchScene();
    /// M: added the new callback fun for remove appWidget.
    public void bindAppWidgetRemoved(List<String> appWidget, boolean permanent);

    /// M: set flag to re-sync apps pages if orientation changed.
    public void notifyOrientationChanged();
}
