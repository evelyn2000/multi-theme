package com.jzs.common.launcher;

import java.util.HashMap;
import java.util.Map;

import com.jzs.common.launcher.model.ILauncherModelCallbacks;
import com.jzs.common.manager.IIconUtilities;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

public abstract class LauncherHelper extends ContextThemeWrapper implements ILauncherModelCallbacks {
	
	/** The different states that Launcher can be in. */
//	protected enum State { NONE, WORKSPACE, APPS_CUSTOMIZE, APPS_CUSTOMIZE_WIDGET, 
//							APPS_CUSTOMIZE_SPRING_LOADED, 
//							APPS_CUSTOMIZE_WIDGET_SPRING_LOADED, 
//							WORKSPACE_PREVIEW,
//							SCREEN_CUSTOM_SETTINGS };
	public static class State{
		public final static int NONE = 0;
		public final static int APPS_CUSTOMIZE_SPRING_LOADED = (0x10|NONE);;
		public final static int APPS_CUSTOMIZE_WIDGET_SPRING_LOADED = (0x20|NONE);
		public final static int WORKSPACE_SPRING_LOADED = (0x30|NONE);

		public final static int WORKSPACE = 0x01;
        public final static int WORKSPACE_PREVIEW = (0x10|WORKSPACE);
        public final static int CUSTOM_SETTINGS = (0x20|WORKSPACE);
        
		public final static int APPS_CUSTOMIZE = 0x02;
		public final static int APPS_CUSTOMIZE_WIDGET = (0x10|APPS_CUSTOMIZE);
	}

	public final static String EXT_HELPER_TARGET_PROVIDER = "LauncherProvider";
	public final static String EXT_HELPER_TARGET_LAUNCHERMODEL = "LauncherModel";
	public final static String EXT_HELPER_TARGET_LAUNCHER = "Launcher";
	public final static String EXT_HELPER_TARGET_GLOBAL_STATIC_FUNC = "GlobalStaticFunctions";
	
	private IIconUtilities sIconUtilities;
	private ISharedPrefSettingsManager sSharedPrefSettingsManager;
	private IResConfigManager sResConfigManager;
	private IIconCache sIconCache;
	private ILauncherModel sLauncherModel;
	
	private static Map<String, IExtHelperBase> sHelperInterfaceMap = new HashMap<String, IExtHelperBase>();
	
	private final Context mLocalContext;
	private ILauncherApplication mMainApp;
	private Activity mLauncherActivity;
	
	public LauncherHelper(Context localContext){
		this(localContext, null);
	}
	
	public LauncherHelper(Context localContext, ILauncherApplication mainApp){
		super(localContext, 0);
		
		mLocalContext = localContext;
		initialise(mainApp);
	}
	
	public final void initialise(ILauncherApplication mainApp){
		if(mMainApp != null || mainApp == null)
			return;		
		mMainApp = mainApp;
		
		sSharedPrefSettingsManager = mainApp.getSharedPrefSettingsManager();		
		sResConfigManager = createResConfigManager(sSharedPrefSettingsManager,
					mainApp.getDefaultResConfigManager(sSharedPrefSettingsManager));
				
		sSharedPrefSettingsManager.setResConfigManager(sResConfigManager);		
		sIconUtilities = createIconUtilities();
		
		addExtHelper(EXT_HELPER_TARGET_PROVIDER, createExtHelper(EXT_HELPER_TARGET_PROVIDER));		
		addExtHelper(EXT_HELPER_TARGET_GLOBAL_STATIC_FUNC, mainApp.getGlobalStaticFunctions());
	}
	
	protected IExtHelperBase createExtHelper(String target){
		return null;
	}

	public void onTrimMemory(int level){
		if(sSharedPrefSettingsManager != null)
			sSharedPrefSettingsManager.onTrimMemory(level);
		if(sResConfigManager != null)
			sResConfigManager.onTrimMemory(level);
		
		for(IExtHelperBase helper : sHelperInterfaceMap.values()){
			helper.onTrimMemory(level);
		}
	}
	
	public void ReleaseInstance(){

		if(sLauncherModel != null){
			sLauncherModel.ReleaseInstance();
			sLauncherModel = null;
		}
		
		if(sIconCache != null){
			sIconCache.flush();
			sIconCache = null;
		}
		
		if(sIconUtilities != null){
			sIconUtilities.ReleaseInstance();
			sIconUtilities = null;
		}
		
		for(IExtHelperBase helper : sHelperInterfaceMap.values()){
			helper.ReleaseInstance();
		}
		
		sHelperInterfaceMap.clear();
	}
	
	public final void addExtHelper(String target, IExtHelperBase helper){
		if(helper != null)
			sHelperInterfaceMap.put(target, helper);
		else
			sHelperInterfaceMap.remove(target);
	}
	
	public final void removeExtHelper(String target){
		sHelperInterfaceMap.remove(target);
	}
	
	public final IExtHelperBase getExtHelper(String target){
		return sHelperInterfaceMap.get(target);
	}
	
//	public final LauncherHelper getLauncherHelper(){
//		return (LauncherHelper)getExtHelper(EXT_HELPER_TARGET_LAUNCHER);
//	}
	
	public final IGlobalStaticFunc getInstallShortcutReceiver(){
		return (IGlobalStaticFunc)getExtHelper(EXT_HELPER_TARGET_GLOBAL_STATIC_FUNC);
	}

	public final void setLauncherActivity(Activity activity){
		mLauncherActivity = activity;
	}

	public final Activity getActivity(){
		return mLauncherActivity;
	}

	public final ILauncherProvider getLauncherProvider(){
		return mMainApp.getLauncherProvider();
	}
	
	public final ILauncherApplication getLauncherApplication(){
		return mMainApp;
	}

	public final Context getLocalContext(){
		return mLocalContext;
	}
	
	public final IIconCache getIconCache(){
		if(sIconCache == null)
			sIconCache = createIconCache();
		return sIconCache;
	}
	
	public final IIconUtilities getIconUtilities(){
		return sIconUtilities;
	}
	
	public final ILauncherModel getModel(){
		if(sLauncherModel == null)
			sLauncherModel = createLauncherModel();
		return sLauncherModel;
	}
	
	public final ISharedPrefSettingsManager getSharedPrefSettingsManager(){
		return sSharedPrefSettingsManager;
	}
	
	public final SharedPreferences getSharedPreferences(){
		return getSharedPrefSettingsManager().getSharedPreferences();
	}
	
	public final IResConfigManager getResConfigManager(){
		return sResConfigManager;
	}

	protected IIconUtilities createIconUtilities(){
		return mMainApp.createIconUtilities(getSharedPrefSettingsManager(), getResConfigManager());
	}
	
	protected IResConfigManager createResConfigManager(ISharedPrefSettingsManager pref, IResConfigManager base){
		return base;//new ResConfigManager(mLocalContext, pref, base);
	}
	
	protected ILauncherModel createLauncherModel(){
		return mMainApp.createLauncherModel();
	}
	
	protected IIconCache createIconCache(){
		return mMainApp.createIconCache();
	}
	
	public void triggerLoadingDatabaseManually() {
		if (getModel() != null) {
			getModel().resetLoadedState(false, true);
			getModel().startLoaderFromBackground();
		}
	}
	
	public final Intent getIntent() {
        return getActivity().getIntent();
    }
	
	public final Window getWindow() {
        return getActivity().getWindow();
    }

    public final Application getApplication() {
        return getActivity().getApplication();
    }
    
    public final WindowManager getWindowManager() {
    	return getActivity().getWindowManager();
    }
    
    public int getWorkspaceCellCountX(){
    	return getSharedPrefSettingsManager().getWorkspaceCountCellX();
    }
    
    public int getWorkspaceCellCountY(){
    	return getSharedPrefSettingsManager().getWorkspaceCountCellY();
    }
    
    public int getWorkspaceScreenCount(){
    	return getSharedPrefSettingsManager().getWorkspaceScreenCount();
    }
	
	public abstract void onCreate(Bundle savedInstanceState);
	public abstract boolean onNewIntent(Intent intent);
	public abstract void onResume();
	public abstract void onPause();
	public abstract void onStart();
	public abstract void onStop();
	public abstract void onUserLeaveHint();
	public abstract void onAttachedToWindow();
	public abstract void onDetachedFromWindow();
	public abstract void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo);
	public abstract boolean onContextItemSelected(MenuItem item);
	public abstract void onContextMenuClosed(Menu menu);
	public abstract Dialog onCreateDialog(int id);
	public abstract void onPrepareDialog(int id, Dialog dialog);
	public abstract void onConfigurationChanged(Configuration newConfig);
	public abstract void onAttachFragment(Fragment fragment);
	public abstract void onRestoreInstanceState(Bundle state);
	public abstract void onSaveInstanceState(Bundle outState);
	public abstract void onDestroy();
	public abstract boolean onActivityResult(final int requestCode, final int resultCode, final Intent data);
	public abstract boolean startSearch(String initialQuery, boolean selectInitialQuery,
            Bundle appSearchData, boolean globalSearch);
	public abstract boolean dispatchKeyEvent(KeyEvent event);
	public abstract boolean onBackPressed();
	public abstract boolean onCreateOptionsMenu(Menu menu);
	public abstract boolean onPrepareOptionsMenu(Menu menu);
	public abstract boolean onOptionsItemSelected(MenuItem item);
	public abstract void onClickSearchButton(View v);
	public abstract void onClickVoiceButton(View v);
	public abstract void onClickAllAppsButton(View v);
	public abstract void onTouchDownAllAppsButton(View v);
	public abstract void onClickAppMarketButton(View v);
	public abstract void onWindowFocusChanged(boolean hasFocus);
	public abstract boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event, boolean result);
	public abstract Object onRetainNonConfigurationInstance();
	public abstract boolean onKeyDown(int keyCode, KeyEvent event, boolean superResult);
	public abstract boolean onKeyUp(int keyCode, KeyEvent event, boolean superResult);
	public abstract boolean closeSystemDialogs();
	
	public abstract int getOrderInHotseat(final int cellX, final int cellY);
	
}
