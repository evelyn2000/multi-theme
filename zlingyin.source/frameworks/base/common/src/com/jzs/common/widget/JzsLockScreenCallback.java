package com.jzs.common.widget;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.jzs.common.widget.QsLockScreen.OnDismissAction;

public interface JzsLockScreenCallback {
	void gotoUnlock();
	void dismissTimeOut();
	void setOnDismissAction(OnDismissAction action);
	
	void setLockScreenWallpaper(Drawable dr, boolean useWallpaper);
	
	void launchCamera(/*Handler worker, Runnable onSecureCameraStarted*/);
	void launchActivity(final Intent intent/*,
            boolean showsWhileLocked,
            boolean useDefaultAnimations,
            final Handler worker,
            final Runnable onStarted*/);
	
	int getPhoneState();
	String getSimState();
	// com.android.internal.telephony.PhoneConstants.GEMINI_SIM_1
	String getSimState(int simid);
	CharSequence getTelephonyPlmn(int simid);
	CharSequence getTelephonySpn(int simid);
	CharSequence getTelephonyPlmn();
    CharSequence getTelephonySpn();
	boolean dmIsLocked();
	boolean isSimLocked();
}
