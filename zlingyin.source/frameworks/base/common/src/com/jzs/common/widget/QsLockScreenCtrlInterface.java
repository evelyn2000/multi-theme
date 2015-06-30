package com.jzs.common.widget;

public interface QsLockScreenCtrlInterface {
	
	public static final int HORIZONTAL = 0; // as defined in attrs.xml
    public static final int VERTICAL = 1;
    
	public static final long VIBRATE_SHORT = 30;
	public static final long VIBRATE_LONG = 40;
		
    public void setOnTriggerListener(QsLockScreenOnTriggerListener listener);

    public void dispatchTriggerEvent(int whichHandle);
    
    
    public void onPause();
    public void onResume();
    public void cleanUp();
    public void reset();
    public void refresh();
    
    public void enableCircleUnlockRippleStyle(boolean enable, boolean init);
}
