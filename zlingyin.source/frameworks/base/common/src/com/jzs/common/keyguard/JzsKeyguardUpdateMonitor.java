package com.jzs.common.keyguard;

import android.graphics.Bitmap;

public abstract class JzsKeyguardUpdateMonitor {
	
	public void registerCallback(JzsKeyguardUpdateMonitorCallback callback) {
		
	}
	
	public void removeCallback(JzsKeyguardUpdateMonitorCallback callback) {
		
	}
	
	public boolean hasBootCompleted(){
		return false;
	}
	
	public boolean isKeyguardVisible() {
        return false;
    }

    public boolean isSwitchingUser() {
        return false;
    }
	
	public boolean isDeviceProvisioned(){
		return false;
	}
	
	public int getFailedUnlockAttempts(){
		return 0;
	}
	
	public void clearFailedUnlockAttempts(){
		
	}
	
	public void reportFailedUnlockAttempt(){
		
	}
	
	public boolean isClockVisible(){
		return false;
	}
	
	public abstract int getPhoneState();
	public abstract void reportFailedBiometricUnlockAttempt();
	public abstract boolean getMaxBiometricUnlockAttemptsReached();
	public abstract boolean isAlternateUnlockEnabled();
	public abstract void setAlternateUnlockEnabled(boolean enabled);
	public abstract boolean isSimLocked();
	public abstract boolean isSimPinSecure();
	
	public abstract void dispatchScreenTurnedOn();
	public abstract void dispatchScreenTurndOff(int why);
	public abstract boolean isScreenOn();

	public abstract void dispatchSetBackground(Bitmap bmp);
	
	public abstract int getSimMeCategory(int simId);
	public abstract int getSimMeLeftRetryCount(int simId);
	public abstract void minusSimMeLeftRetryCount(int simId);
	
	public abstract CharSequence getTelephonyPlmn(int simId);
	public abstract CharSequence getTelephonySpn(int simId);
	public abstract CharSequence getTelephonyHnbName(int simId);
	public abstract CharSequence getTelephonyCsgId(int simId);
	
	//public abstract IccCardConstants.State getSimStateString(int simId);
	public abstract String getSimStateString(int simId);
}
