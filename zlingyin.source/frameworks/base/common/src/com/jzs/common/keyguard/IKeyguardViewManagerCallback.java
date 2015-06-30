package com.jzs.common.keyguard;

import android.graphics.drawable.Drawable;

public interface IKeyguardViewManagerCallback {
	
	public JzsKeyguardViewBase getJzsKeyguardViewBase();
	public boolean isAlarmBoot();
	public void setCustomBackground(Drawable d);
	public JzsKeyguardUpdateMonitor getKeyguardUpdateMonitor();
	public void updateKeyguardHostViewLayout();
}
