package com.jzs.common.widget;

import android.view.View;

public interface QsLockScreenOnTriggerListener {
	
	public static final int STATE_NORMAL = 0;

	public static final int STATE_PRESSED = 1;

	public static final int STATE_ACTIVE = 2;

    public static final int STATE_MOVED = 3;
    public static final int STATE_LONGPRESSED = 4;

    public static final int NO_HANDLE = 0;
    public static final int UNLOCK_HANDLE = 1;

    
    public final static int ACTION_NONE = NO_HANDLE;
    public final static int ACTION_UNLOCK = UNLOCK_HANDLE;				// tag="unlock"
	public final static int ACTION_MMS = (ACTION_UNLOCK + 1);			// tag="mms"
	public final static int ACTION_DIAL = (ACTION_UNLOCK + 2);			// tag="dial"
	public final static int ACTION_CALLLOG = (ACTION_UNLOCK + 3);		// tag="calllog"
	public final static int ACTION_CAMERA = (ACTION_UNLOCK + 4);		// tag="contacts"
    public final static int ACTION_SILENT = (ACTION_UNLOCK + 5);		// tag="camera"
    public final static int ACTION_ANSWER_CALL = (ACTION_UNLOCK + 6);	// tag="silent"
    public final static int ACTION_END_CALL = (ACTION_UNLOCK + 7);		// tag="answercall"
    public final static int ACTION_CONTACTS = (ACTION_UNLOCK + 8);		// tag="endcall"
    public final static int ACTION_BROWSER = (ACTION_UNLOCK + 9);		// tag="browser"
    public final static int ACTION_EMAIL = (ACTION_UNLOCK + 10);		// tag="email"

    public final static int ACTION_CUSTOM_INTENT = (ACTION_UNLOCK + 100);

    
    /**
     * Called when the user moves a handle beyond the threshold.
     *
     * @param v The view that was triggered.
     * @param whichHandle  Which "dial handle" the user grabbed,
     *        either {@link #LEFT_HANDLE}.
     */

    public void onTrigger(View v, int whichHandle, String data);

    /**
     * Called when the "grabbed state" changes (i.e. when the user either grabs or releases
     * one of the handles.)
     *
     * @param v the view that was triggered
     * @param grabbedState the new state: {@link #NO_HANDLE}, {@link #LEFT_HANDLE},
     * 
     */
    public void onGrabbedStateChange(View v, int grabbedState);
    
    public void dismissTimeOut();
}
