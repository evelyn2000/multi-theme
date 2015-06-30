package com.jzs.common.manager;

import android.os.Bundle;
import android.view.WindowManager;

import com.jzs.common.keyguard.JzsKeyguardSecurityModel;
import com.jzs.common.keyguard.JzsKeyguardUpdateMonitor;
import com.jzs.common.keyguard.JzsKeyguardViewBase;
import com.jzs.common.keyguard.IKeyguardViewManagerCallback;

public interface IKeyguardViewManager {
	
	public final static String MANAGER_SERVICE = IComplexManager.MANAGER_SERVICE_PREFIX+"KeyguardViewMgr";
	
	public void initKeyguardViewManager(JzsKeyguardSecurityModel securitymodel, 
			IKeyguardViewManagerCallback callback);
	
	public void show(Bundle options);
	public void onShowed(Bundle options);
	public void inflateKeyguardView(Bundle options);
	public void onKeyguardViewInflated(Bundle options, JzsKeyguardViewBase keyguardView);
	
	public void maybeEnableScreenRotation(boolean enableScreenRotation, WindowManager.LayoutParams lp);
	public int getStatusBarVisiableFlag();
	
	public boolean checkIsForceCreateKeyguardLocked();
	public void updateKeyguardHostLayoutParams(WindowManager.LayoutParams lp);
	
	public boolean getInflateKeyguardView(int[] id);
}
