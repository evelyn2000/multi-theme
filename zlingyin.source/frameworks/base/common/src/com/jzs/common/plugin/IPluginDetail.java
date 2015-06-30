package com.jzs.common.plugin;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public interface IPluginDetail {

	String getDescription();
	String getTitle();

	int getPreviewResource();
	Drawable loadPreview(PackageManager pm);
	Drawable loadPreview(PackageManager pm, ApplicationInfo info);
	
}
