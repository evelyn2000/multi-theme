package com.jzs.common.keyguard;


public abstract class JzsKeyguardSecurityModel {
	
	public static enum JzsSecurityMode {
        Invalid, // NULL state
        None, // No security enabled
        Pattern, // Unlock by drawing a pattern.
        Password, // Unlock by entering an alphanumeric password
        PIN, // Strictly numeric password
        Biometric, // Unlock with a biometric key (e.g. finger print or face unlock)
        Account, // Unlock by entering an account's login and password.
        AlarmBoot, // add for power-off alarm.
        SimPinPukMe, // Unlock by entering a sim pin/puk/me for sim.
        Voice, // Unlock with voice password
        AntiTheft, // Antitheft feature
        UnkownMode
    }
	
	public static JzsSecurityMode ParseSecurityMode(String mode){
		if(mode != null){
			if(mode.startsWith(JzsSecurityMode.SimPinPukMe.name())){
				return JzsSecurityMode.SimPinPukMe;
			}
			
			for(JzsSecurityMode item : JzsSecurityMode.values()){
				if(mode.equals(item.name())){
					return item;
				}
			}
		}
		return JzsSecurityMode.UnkownMode;
	}
	
	//public abstract LockPatternUtils getLockPatternUtils();
	protected abstract String getSecurityModeString();	
	public abstract boolean isBiometricUnlockEnabled();
	public abstract boolean isAlarmBoot();
	protected abstract boolean isBiometricUnlockSuppressed();
	protected abstract boolean isUsingVoiceWeakd();
	
	public boolean isPinPukOrMeRequired(int simId){
		return false;
	}
	
	public JzsSecurityMode getJzsSecurityMode(){
		return ParseSecurityMode(getSecurityModeString());
	}
	
	public JzsSecurityMode getJzsAlternateFor(){
		return getJzsAlternateFor(getJzsSecurityMode());
	}
	
	public JzsSecurityMode getJzsAlternateFor(String strSecurityMode){
		return getJzsAlternateFor(ParseSecurityMode(strSecurityMode));
	}
	
	public JzsSecurityMode getJzsAlternateFor(JzsSecurityMode mode){
		if (!isBiometricUnlockSuppressed() && (mode == JzsSecurityMode.Password
		                || mode == JzsSecurityMode.PIN
		                || mode == JzsSecurityMode.Pattern)) {
		    if (isBiometricUnlockEnabled()) {
		        return JzsSecurityMode.Biometric;
		    } else if (isUsingVoiceWeakd()) { ///M add for voice unlock
		        return JzsSecurityMode.Voice;
		    }
		}
		return mode; // no alternate, return what was given
	}
    
	public JzsSecurityMode getJzsBackupSecurityMode(String strSecurityMode) {
		return getJzsBackupSecurityMode(ParseSecurityMode(strSecurityMode));
	}
    /**
     * Some unlock methods can have a backup which gives the user another way to get into
     * the device. This is currently only supported for Biometric and Pattern unlock.
     *
     * @return backup method or current security mode
     */
	public JzsSecurityMode getJzsBackupSecurityMode(JzsSecurityMode mode) {
        switch(mode) {
            case Biometric:
            case Voice: ///M: add for voice unlock
                return getJzsSecurityMode();
            case Pattern:
                return JzsSecurityMode.Account;
        }
        return mode; // no backup, return current security mode
    }
	
}
