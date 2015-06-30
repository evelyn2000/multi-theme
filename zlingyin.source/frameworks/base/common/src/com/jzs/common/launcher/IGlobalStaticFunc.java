package com.jzs.common.launcher;

public interface IGlobalStaticFunc extends IExtHelperBase {
	public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String NEW_APPS_PAGE_KEY = "apps.new.page";
    public static final String NEW_APPS_LIST_KEY = "apps.new.list";
    
    public static final int NEW_SHORTCUT_BOUNCE_DURATION = 450;
    public static final int NEW_SHORTCUT_STAGGER_DELAY = 75;

    // for InstallShortcutReceiver
	void enableInstallQueue();
	void disableAndFlushInstallQueue();
	void flushInstallQueue();
	
	// for UnInstallShortcutReceiver
	void enableUninstallQueue();
	void disableAndFlushUninstallQueue();
}
