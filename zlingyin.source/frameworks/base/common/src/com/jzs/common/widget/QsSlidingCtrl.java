package com.jzs.common.widget;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.os.Handler;

public class QsSlidingCtrl extends FrameLayout implements QsLockScreenCtrlInterface {
    protected static final String LOG_TAG = "QsSlidingCtrl";
    protected static final boolean DBG = false;
    protected static final boolean DEBUG = DBG;
    
    private int mOrientation = VERTICAL;
    
    private Vibrator mVibrator;
    
    protected float mDensity; // used to scale dimensions for bitmaps.
	
	//protected Rect mTmpRect;
	private boolean mIsAttached = false;
	
	protected QsLockScreenOnTriggerListener mOnTriggerListener = null;
	protected int mGrabbedState = QsLockScreenOnTriggerListener.NO_HANDLE;
	protected boolean mTriggered = false;
	
	private static Map<String, Integer> tagActionCmdMap = new HashMap<String, Integer>();
	static {

		tagActionCmdMap.put("unlock", QsLockScreenOnTriggerListener.ACTION_UNLOCK);
		tagActionCmdMap.put("mms", QsLockScreenOnTriggerListener.ACTION_MMS);
		tagActionCmdMap.put("dial", QsLockScreenOnTriggerListener.ACTION_DIAL);
		tagActionCmdMap.put("calllog", QsLockScreenOnTriggerListener.ACTION_CALLLOG);
		tagActionCmdMap.put("contacts", QsLockScreenOnTriggerListener.ACTION_CONTACTS);
		tagActionCmdMap.put("camera", QsLockScreenOnTriggerListener.ACTION_CAMERA);
		tagActionCmdMap.put("silent", QsLockScreenOnTriggerListener.ACTION_SILENT);
		tagActionCmdMap.put("answercall", QsLockScreenOnTriggerListener.ACTION_ANSWER_CALL);
		tagActionCmdMap.put("endcall", QsLockScreenOnTriggerListener.ACTION_END_CALL);
		tagActionCmdMap.put("browser", QsLockScreenOnTriggerListener.ACTION_BROWSER);
		tagActionCmdMap.put("email", QsLockScreenOnTriggerListener.ACTION_EMAIL);
    }
	
    public final static int getAction(Object o){
    	//android.util.Log.w("QsLog", LOG_TAG+"::getAction(up)==getAction=="+o);
    	if(o != null){
    		final String str = o.toString();
    		if(str.startsWith("intent:") || str.startsWith("#Intent;"))
                return QsLockScreenOnTriggerListener.ACTION_CUSTOM_INTENT;
    		
    		if(tagActionCmdMap.containsKey(str))
    			return tagActionCmdMap.get(str);
//	    	if(o.equals("unlock"))
//	    		return QsLockScreenOnTriggerListener.ACTION_UNLOCK;
//	    	
//	    	if(o.equals("mms"))
//	    		return QsLockScreenOnTriggerListener.ACTION_MMS;
//	    	
//	    	if(o.equals("dial"))
//	    		return QsLockScreenOnTriggerListener.ACTION_DIAL;
//	    	
//	    	if(o.equals("calllog"))
//	    		return QsLockScreenOnTriggerListener.ACTION_CALLLOG;
//
//            if(o.equals("contacts"))
//	    		return QsLockScreenOnTriggerListener.ACTION_CONTACTS;
//	    	
//	    	if(o.equals("camera"))
//	    		return QsLockScreenOnTriggerListener.ACTION_CAMERA;
//
//            if(o.equals("silent"))
//	    		return QsLockScreenOnTriggerListener.ACTION_SILENT;
//
//            if(o.equals("answercall"))
//	    		return QsLockScreenOnTriggerListener.ACTION_ANSWER_CALL;
//
//            if(o.equals("endcall"))
//	    		return QsLockScreenOnTriggerListener.ACTION_END_CALL;
//
//            if(o.equals("browser"))
//	    		return QsLockScreenOnTriggerListener.ACTION_BROWSER;
//
//            if(o.equals("email"))
//	    		return QsLockScreenOnTriggerListener.ACTION_EMAIL;
//            
            
            
    	}
    	
    	return QsLockScreenOnTriggerListener.ACTION_NONE;
    }

	public QsSlidingCtrl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QsSlidingCtrl(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        
        mOrientation = VERTICAL;
        
        setFocusable(true);
        setFocusableInTouchMode(true);
        //setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        
        mDensity = context.getResources().getDisplayMetrics().density;
        
    }
    
	protected boolean isHorizontal() {
        return mOrientation == HORIZONTAL;
    }

	/**
     * Triggers haptic feedback.
     */
	protected synchronized void vibrate(long duration) {
        if (mVibrator == null) {
            mVibrator = (android.os.Vibrator)
                    getContext().getSystemService(Context.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(duration);
    }
    
    /**
     * Sets the current grabbed state, and dispatches a grabbed state change
     * event to our listener.
     */
    protected void setGrabbedState(int newState) {
        //if (newState != mGrabbedState) {
            mGrabbedState = newState;//(newState > QsSlidingCtrlOnTriggerListener.ACTION_NONE ? QsSlidingCtrlOnTriggerListener.ACTION_UNLOCK : QsSlidingCtrlOnTriggerListener.ACTION_NONE);
            if (mOnTriggerListener != null) {
                mOnTriggerListener.onGrabbedStateChange(this, newState);
            }
        //}
    }

    public void notifyScreenTouchDown(){
        Handler handler = getHandler();
        if(handler != null){
            handler.removeCallbacks(mGrabbedStateChanged);
            handler.removeCallbacks(mRemoveStateChanged);
            handler.postDelayed(mGrabbedStateChanged, 1000);
        }
        
        setGrabbedState(QsLockScreenOnTriggerListener.STATE_PRESSED);
    }

    public void notifyScreenTouchUpOrCancel(){
        Handler handler = getHandler();
        if(DBG)
            android.util.Log.w("QsLog", "QsSlidingCtrl::notifyScreenTouchUpOrCancel()===");
        if(handler != null){
            handler.postDelayed(mRemoveStateChanged, 5000*2);
        }
    }

    protected boolean isAttached(){
        return mIsAttached;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsAttached = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if(DBG)
            android.util.Log.e("QsLog", "QsSlidingCtrl::onDetachedFromWindow(3)==");
        mIsAttached = false;
        super.onDetachedFromWindow();
        Handler handler = getHandler();
        if(handler != null){
            handler.removeCallbacks(mRemoveStateChanged);
            handler.removeCallbacks(mGrabbedStateChanged);
        }
    }
    
    public void pokeWakelock(){
    	setGrabbedState(QsLockScreenOnTriggerListener.ACTION_NONE);
    }
    
    public void goToUnlockScreen(){
    	dispatchTriggerEvent(QsLockScreenOnTriggerListener.ACTION_UNLOCK);
    }
    
    public void goToUnlockScreen(View view){
    	dispatchTriggerEvent(view);
    }

    /**
     * Registers a callback to be invoked when the user triggers an event.
     *
     * @param listener the OnDialTriggerListener to attach to this view
     */
    public void setOnTriggerListener(QsLockScreenOnTriggerListener listener) {
        mOnTriggerListener = listener;
    }

    /**
     * Dispatches a trigger event to listener. Ignored if a listener is not set.
     * @param whichHandle the handle that triggered the event.
     */
    public void dispatchTriggerEvent(int whichHandle) {
        dispatchTriggerEvent(whichHandle, null);
    }

    protected void dispatchTriggerEvent(View view){
        Object tag = view.getTag();
        int action = getAction(tag);
        if(action != QsLockScreenOnTriggerListener.ACTION_NONE){
            dispatchTriggerEvent(action, tag.toString());
        }
    }

    private void dispatchTriggerEvent(int whichHandle, String data) {
        vibrate(VIBRATE_LONG);
        if (mOnTriggerListener != null) {
            mOnTriggerListener.onTrigger(this, whichHandle, data);
        }
    }
    
    private final Runnable mGrabbedStateChanged = new Runnable() {
        public void run() {
            setGrabbedState(QsLockScreenOnTriggerListener.ACTION_NONE);
            //final Handler handler = getHandler();
            if(getHandler() != null){
                getHandler().postDelayed(mGrabbedStateChanged, 1000);
            }
        }
    };

    private final Runnable mRemoveStateChanged = new Runnable() {
        public void run() {
            //final Handler handler = getHandler();
            if(getHandler() != null){
                getHandler().removeCallbacks(mGrabbedStateChanged);
            }
        }
    };

    
    public void changeUnreadMmsCount(int nCount){
        
    }

    public void changeMissedCallCount(int nCount){
        
    }

    public void onPause(){
        if(DBG)
            android.util.Log.e("QsLog", "QsSlidingCtrl::onPause(3)==");
        //getHandler().removeCallbacks(mRemoveStateChanged);
        //getHandler().removeCallbacks(mGrabbedStateChanged);
        final Handler handler = getHandler();
        if(handler != null){
            handler.removeCallbacks(mRemoveStateChanged);
            handler.removeCallbacks(mGrabbedStateChanged);
        }
    }
    
    public void onResume(){
        if(DBG)
            android.util.Log.e("QsLog", "QsSlidingCtrl::onResume(3)==");
    }
    
    public void cleanUp() {

    }
    
    public void reset(){
    	
    }
    
    public void refresh(){
    	
    }

    public void enableCircleUnlockRippleStyle(boolean enable, boolean init){
    }
}
