package com.jzs.common.manager;

import android.content.Intent;

public interface IGestureManager {
	
	public final static String MANAGER_SERVICE = IComplexManager.MANAGER_SERVICE_PREFIX+"GestuMgrSer";
	
	public final static int GESTURE_TYPE_UNKNOWN 		= -1;
	
	public final static int GESTURE_TYPE_START 			= 0;
	public final static int GESTURE_TYPE_LEFT 			= GESTURE_TYPE_START;
	public final static int GESTURE_TYPE_RIGHT 			= 1;
	public final static int GESTURE_TYPE_UP 			= 2;
	public final static int GESTURE_TYPE_DOWN 			= 3;
	public final static int GESTURE_TYPE_DOUBLE_CLICK 	= 4;
	public final static int GESTURE_TYPE_Z 				= 5;
	public final static int GESTURE_TYPE_S 				= 6;
	public final static int GESTURE_TYPE_O 				= 7;
	public final static int GESTURE_TYPE_W 				= 8;
	public final static int GESTURE_TYPE_M 				= 9;
	public final static int GESTURE_TYPE_V 				= 10;
	public final static int GESTURE_TYPE_C 				= 11;
	public final static int GESTURE_TYPE_E 				= 12;
	
	public final static int GESTURE_TYPE_MAX 			= 13;
	
	public final static int PS_GESTURE_TYPE_NONE			= 0;
	public final static int PS_GESTURE_TYPE_SINGLE			= (1<<0);
	public final static int PS_GESTURE_TYPE_DOUBLE			= (1<<1);
	public final static int PS_GESTURE_TYPE_FOUR			= (1<<2);
	
	public int getSupportProximityGesture();
	public int getProximityGestureType();
	public boolean isProximityGestureEnabled();
	public void enableProximityGesture(boolean enable);
	public void enableProximityGesture(boolean enable, int type);
	public void setProximityGesturePowerStatus(boolean poweron);
	
	public void setEnable(boolean enable);
	public boolean isEnable();
	public void setAction(int type, Intent intent);
	public void clearAction(int type);
	public Intent getAction(int type);
	public int[] getSupportTypes();
	public boolean isSupportType(int type);
	
}
