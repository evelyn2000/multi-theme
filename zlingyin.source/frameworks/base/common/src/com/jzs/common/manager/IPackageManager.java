package com.jzs.common.manager;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface IPackageManager {
	
	public interface PackageManagerCallback{
		void installPluginPackage(String pkgName, String pkgPath, boolean isPublic);
	}
	
	void setPackageManagerCallback(PackageManagerCallback callback);
	void initializeShareLibraries(HashMap<String, String> sharedLibraries, HashSet<String> libFiles);
	
	void systemReady();
	
	void removePackage(String packageName, int appFlag, boolean chatty);
	void installPackage(String packageName, ApplicationInfo appInfo);
	void installPackage(String packageName, ApplicationInfo appInfo, List<ActivityInfo> listActInfo);
	
}
