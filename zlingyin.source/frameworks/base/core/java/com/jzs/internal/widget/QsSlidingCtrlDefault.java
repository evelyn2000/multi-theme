package com.jzs.internal.widget;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jzs.common.widget.QsLockScreenOnTriggerListener;
import com.jzs.common.widget.QsSlidingCtrl;
import com.jzs.internal.widget.multiwaveview.GlowPadView;
import com.jzs.internal.widget.multiwaveview.MultiWaveView;
import com.android.internal.widget.LockPatternUtils;
import com.jzs.internal.R;

public class QsSlidingCtrlDefault extends QsSlidingCtrl {
	private final static String TAG = "QsSlidingCtrlDefault";
	
	private static final int ON_RESUME_PING_DELAY = 500; // delay first ping until the screen is on
	
	private LockPatternUtils mLockPatternUtils;
	private boolean mSilentMode;
    private AudioManager mAudioManager;
    private View mUnlockWidget;
    
    private UnlockWidgetCommonMethods mUnlockWidgetMethods;
	
	public QsSlidingCtrlDefault(Context context) {
        this(context, null);
    }
	
	public QsSlidingCtrlDefault(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public QsSlidingCtrlDefault(Context context, AttributeSet attrs, int defStyle){
    	super(context, attrs, defStyle);
    	
    	mLockPatternUtils = new LockPatternUtils(context);
    	
    	mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mSilentMode = isSilentMode();
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        mUnlockWidget = findViewById(R.id.unlock_widget);
        if(mUnlockWidget != null){
        	if(mUnlockWidget instanceof GlowPadView){
        		mUnlockWidgetMethods = new GlowPadViewMethods((GlowPadView) mUnlockWidget);
        	} else {
		        //MultiWaveView multiWaveView = (MultiWaveView) mUnlockWidget;
        		mUnlockWidgetMethods = new MultiWaveViewMethods((MultiWaveView) mUnlockWidget);
		        //mUnlockWidgetMethods = multiWaveViewMethods;
        	}
	        
	        mUnlockWidgetMethods.updateResources();
        }
    }
    
    public void onPause() {
    	super.onPause();
    	if(mUnlockWidgetMethods != null)
    		mUnlockWidgetMethods.reset(false);
    }

    private final Runnable mOnResumePing = new Runnable() {
        public void run() {
        	if(mUnlockWidgetMethods != null)
        		mUnlockWidgetMethods.ping();
        }
    };

    public void onResume() {
    	super.onResume();
        postDelayed(mOnResumePing, ON_RESUME_PING_DELAY);

    }
    
    public void reset(){
    	super.reset();
    	if(mUnlockWidgetMethods != null)
    		mUnlockWidgetMethods.reset(false);
    }

    /** {@inheritDoc} */
    public void cleanUp() {
    	super.cleanUp();
        //mUpdateMonitor.removeCallback(this); // this must be first
        mLockPatternUtils = null;
        //mUpdateMonitor = null;
        //mCallback = null;
        if(mUnlockWidgetMethods != null)
    		mUnlockWidgetMethods.reset(false);
    }
    
    public void refresh(){
    	boolean silent = isSilentMode();
        if (silent != mSilentMode) {
            mSilentMode = silent;
            if(mUnlockWidgetMethods != null)
            	mUnlockWidgetMethods.updateResources();
        }
    }
    
    private boolean isSilentMode() {
        return mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL;
    }
    
    private void toggleRingMode() {
        // toggle silent mode
        mSilentMode = !mSilentMode;
        if (mSilentMode) {
            final boolean vibe = (Settings.System.getInt(
                getContext().getContentResolver(),
                Settings.System.VIBRATE_IN_SILENT, 1) == 1);

            mAudioManager.setRingerMode(vibe
                ? AudioManager.RINGER_MODE_VIBRATE
                : AudioManager.RINGER_MODE_SILENT);
        } else {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
    
    private interface UnlockWidgetCommonMethods {
        // Update resources based on phone state
        public void updateResources();

        // Get the view associated with this widget
        public View getView();

        // Reset the view
        public void reset(boolean animate);

        // Animate the widget if it supports ping()
        public void ping();
    }
    
    class MultiWaveViewMethods implements MultiWaveView.OnTriggerListener,
	    UnlockWidgetCommonMethods {
	
		private final MultiWaveView mMultiWaveView;
		private boolean mCameraDisabled;
		
		MultiWaveViewMethods(MultiWaveView multiWaveView) {
		    mMultiWaveView = multiWaveView;
		    multiWaveView.setOnTriggerListener(this);
		    
		    final boolean cameraDisabled = mLockPatternUtils.getDevicePolicyManager()
		            .getCameraDisabled(null);
		    if (cameraDisabled) {
		        Log.v(TAG, "Camera disabled by Device Policy");
		        mCameraDisabled = true;
		    } else {
		        // Camera is enabled if resource is initially defined for MultiWaveView
		        // in the lockscreen layout file
		        mCameraDisabled = mMultiWaveView.getTargetResourceId()
		                != R.array.lockscreen_targets_with_camera;
		    }
		}
		
		public void updateResources() {
		    int resId;
		    if (mCameraDisabled) {
		        // Fall back to showing ring/silence if camera is disabled by DPM...
		        resId = mSilentMode ? R.array.lockscreen_targets_when_silent
		            : R.array.lockscreen_targets_when_soundon;
		    } else {
		        resId = R.array.lockscreen_targets_with_camera;
		    }
		    mMultiWaveView.setTargetResources(resId);
		}
		
		public void onGrabbed(View v, int handle) {
		
		}
		
		public void onReleased(View v, int handle) {
		
		}
		
		public void onTrigger(View v, int target) {
			//android.util.Log.i("QsLog", "onTrigger()==target:"+target);
			if(mMultiWaveView != null){
				switch(mMultiWaveView.getResourceIdForTarget(target)){
				case R.drawable.ic_lockscreen_unlock:
				case R.drawable.ic_lockscreen_unlock_phantom:
					goToUnlockScreen();
					return;
				case R.drawable.ic_lockscreen_send_sms:
					dispatchTriggerEvent(QsLockScreenOnTriggerListener.ACTION_MMS);
					return;
				default:
					break;
				}
			}
			if (target == 0 || target == 1) { // 0 = unlock/portrait, 1 = unlock/landscape
		        goToUnlockScreen();
		    } else if (target == 2 || target == 3) { // 2 = alt/portrait, 3 = alt/landscape
		        if (!mCameraDisabled) {
		            // Start the Camera
		            Intent intent = new Intent(Intent.ACTION_CAMERA_BUTTON, null);
		            getContext().sendOrderedBroadcast(intent, null);
		            goToUnlockScreen();
		        } else {
		            toggleRingMode();
		            mUnlockWidgetMethods.updateResources();
		            pokeWakelock();
		        }
		    }
		}
		
		public void onGrabbedStateChange(View v, int handle) {
		    // Don't poke the wake lock when returning to a state where the handle is
		    // not grabbed since that can happen when the system (instead of the user)
		    // cancels the grab.
		    if (handle != MultiWaveView.OnTriggerListener.NO_HANDLE) {
		        pokeWakelock();
		    }
		    //android.util.Log.i("QsLog", "onGrabbedStateChange()==handle:"+handle);
		}
		
		public void onFinishFinalAnimation(){
			
		}
		
		public View getView() {
		    return mMultiWaveView;
		}
		
		public void reset(boolean animate) {
		    mMultiWaveView.reset(animate);
		}
		
		public void ping() {
		    mMultiWaveView.ping();
		}
	}
    
    private class GlowPadViewMethods implements GlowPadView.OnTriggerListener, UnlockWidgetCommonMethods {
    	private final GlowPadView mGlowPadView;
		private boolean mCameraDisabled;
		
		GlowPadViewMethods(GlowPadView view) {
			mGlowPadView = view;
			view.setOnTriggerListener(this);
		}
		
        public void onTrigger(View v, int target) {
            final int resId = mGlowPadView.getResourceIdForTarget(target);

            switch (resId) {
//                case R.drawable.ic_action_assist_generic:
//                    Intent assistIntent =
//                            ((SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE))
//                            .getAssistIntent(mContext, true, UserHandle.USER_CURRENT);
//                    if (assistIntent != null) {
//                        mActivityLauncher.launchActivity(assistIntent, false, true, null, null);
//                    } else {
//                        Log.w(TAG, "Failed to get intent for assist activity");
//                    }
//                    mCallback.userActivity(0);
//                    break;

                case R.drawable.ic_lockscreen_camera:
                	dispatchTriggerEvent(QsLockScreenOnTriggerListener.ACTION_CAMERA);
                    break;

                case R.drawable.ic_lockscreen_unlock_phantom:
                case R.drawable.ic_lockscreen_unlock:
                	goToUnlockScreen();
                	break;
                    
                /// M: Add a special case for incoming indicator feature, when indicator view launches activity, go to unlockscreen
                /// only if necessary, or you may see homescreen before activity is launched
                case -1:
                	pokeWakelock();
//                    if (isSecure()) {
//                        mCallback.dismiss(false);
//                    }
                	break;
            }
        }
        
        public void onGrabbed(View v, int handle) {
    		
		}
		
		public void onReleased(View v, int handle) {
		
		}
        
        public void onGrabbedStateChange(View v, int handle) {
		    // Don't poke the wake lock when returning to a state where the handle is
		    // not grabbed since that can happen when the system (instead of the user)
		    // cancels the grab.
		    if (handle != MultiWaveView.OnTriggerListener.NO_HANDLE) {
		        pokeWakelock();
		    }
		}
		
		public void onFinishFinalAnimation(){
			
		}
		
		public void updateResources() {
//		    int resId;
//		    if (mCameraDisabled) {
//		        // Fall back to showing ring/silence if camera is disabled by DPM...
//		        resId = mSilentMode ? R.array.lockscreen_targets_when_silent
//		            : R.array.lockscreen_targets_when_soundon;
//		    } else {
//		        resId = R.array.lockscreen_targets_with_camera;
//		    }
//		    mGlowPadView.setTargetResources(resId);
		}
		
		public View getView() {
		    return mGlowPadView;
		}
		
		public void reset(boolean animate) {
			mGlowPadView.reset(animate);
		}
		
		public void ping() {
			mGlowPadView.ping();
		}
    }
}
