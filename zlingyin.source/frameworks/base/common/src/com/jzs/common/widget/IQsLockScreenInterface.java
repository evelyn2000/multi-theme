package com.jzs.common.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;

public interface IQsLockScreenInterface {
	
	Context getContext();
	View getRootView();
	void onFinishInflate(View root);
	
	QsLockScreenCtrlInterface getQsLockScreenCtrlInterface(View rootView);
	QsLockScreenCtrlInterface getQsLockScreenCtrlInterface();
	int getLayoutResource();
	String getDateFormatString();
	boolean dispatchKeyEvent(KeyEvent event);
	
	void resetBackground();
	void refreshDateTime();
	void refreshAlarmStatus();
	boolean needsInput();
	void onAttachedToWindow();
	void onDetachedFromWindow();
	boolean isFullScreenStyle();
	
	boolean isEnableScreenRotation();
	int getLockWindowAnimations();
	
	void reset();
	void onPause();
	void onResume(int reason);
	void cleanUp();
	void onRingerModeChanged(int state);
	void onPhoneStateChanged(String newState);
	
	//void setOnDismissAction(OnDismissAction action)
	
	void beforeUnlockAction();
	void dismissTimeOut();
	
	void setLockScreenWallpaper(boolean useWallpaper);
	
	void setLockScreenWallpaper(Drawable dr, boolean useWallpaper);
	
	void startAction(int action, String data);
}
