package com.jzs.common.plugin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public interface IPluginTarget extends IPluginBase {

	boolean isSupportSystemTheme();

}
