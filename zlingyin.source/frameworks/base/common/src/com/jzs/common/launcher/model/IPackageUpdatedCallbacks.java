package com.jzs.common.launcher.model;

import java.util.ArrayList;
import java.util.List;

public interface IPackageUpdatedCallbacks {
    public void bindAppsAdded(List<ApplicationInfo> apps);
    public void bindAppsUpdated(List<ApplicationInfo> apps);
    public void bindAppsRemoved(List<String> packageNames, boolean permanent);
    public void bindPackagesUpdated();
}
