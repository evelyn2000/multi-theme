package com.jzs.common.widget;

import java.util.Calendar;
import java.util.Date;

//import com.jzs.internal.widget.ClockView;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Handler;
import android.provider.Settings;

public abstract class QsLockScreen implements QsLockScreenOnTriggerListener{
	
	protected final static int SWITCH_RINGER_MODE = -100;
	public static final int SCREEN_ON = 1;
	public static final int VIEW_REVEALED = 2;
    
	
	public interface OnDismissAction {
        /* returns true if the dismiss should be deferred */
        boolean onDismiss();
    }
	
	private JzsLockScreenCallback mLockScreenCallback;
	private Context mContext;
	protected QsLockScreenCtrlInterface mQsLockScreenCtrlInterface;
	protected View mRootView;
	
	private TextView mDateView;
	//private ClockView mClockView;
	private TextView mAlarmStatusView;
    private final String mDateFormatString;
	
	public QsLockScreen(Context context){
		mContext = context;
		mDateFormatString = getDateFormatString();
//		if(TextUtils.isEmpty(str))
//			mDateFormatString = "EEE, MMM d, yyyy";
//		else
//			mDateFormatString = str;
	}
	
	public Context getContext(){
		return mContext;
	}

	public View getRootView(){
		return mRootView;
	}
	
	public final View inflateLockscreenView(ViewGroup parent, JzsLockScreenCallback callback){
		mLockScreenCallback = callback;
		mRootView = inflateLockscreenView(parent); 
		if(mRootView != null){
			mDateView = (TextView) mRootView.findViewWithTag("ctrl_date_textview");
			mAlarmStatusView = (TextView) mRootView.findViewWithTag("ctrl_alarm_status_textview");
			//mClockView = (ClockView) mRootView.findViewWithTag("ctrl_clock_view");
			mQsLockScreenCtrlInterface = getQsLockScreenCtrlInterface(mRootView);
			if(mQsLockScreenCtrlInterface == null)
				mQsLockScreenCtrlInterface = (QsLockScreenCtrlInterface)mRootView.findViewWithTag("zzzz_qs_touch_lock_ctrl");
			
			if(mQsLockScreenCtrlInterface != null){
				mQsLockScreenCtrlInterface.setOnTriggerListener(this);
			}
			onFinishInflate(mRootView);
		}
		return mRootView;
	}
	
	protected View inflateLockscreenView(ViewGroup parent){
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		if(getLayoutResource() > 0){
			View view = inflater.inflate(getLayoutResource(), parent, false);			
			return view;
		}
        return null;
	}
	
	protected void onFinishInflate(View root){
		
	}
	
	protected abstract QsLockScreenCtrlInterface getQsLockScreenCtrlInterface(View rootView);
	
	public abstract int getLayoutResource();
	
	//public abstract View getDateTimeView();
	
	public String getDateFormatString(){
        return null;/*Settings.System.getString(mContext.getContentResolver(),
                Settings.System.DATE_FORMAT)*/
    }
	
	public boolean dispatchKeyEvent(KeyEvent event){
    	return false;
    }
	
	public boolean resetBackground(View baseView){
		//setLockScreenWallpaper(false);
	    return false;
	}
	
	public void refresh() {
        refreshDateTime();
        refreshAlarmStatus(); 
    }
	
	public void refreshDateTime(){
		//if(mClockView != null)
        //    mClockView.updateTime();
//		android.util.Log.i("QsLog", "refreshDateTime()==="+mDateFormatString);
        if(mDateView != null){
            if(TextUtils.isEmpty(mDateFormatString))
                mDateView.setText(DateFormat.getDateFormat(mContext).format(Calendar.getInstance().getTime()));
            else
                mDateView.setText(DateFormat.format(mDateFormatString, new Date()));
           //     mDateView.setText(DateFormat.getDateFormatForSetting(mContext, mDateFormatString).format(new Date()));
        }
            //mDateView.setText(DateFormat.format(mDateFormatString, new Date()));
	}
	
	public void refreshAlarmStatus() {
		if(mAlarmStatusView == null)
            return;
		
		String nextAlarm = getNextAlarm();
        if (!TextUtils.isEmpty(nextAlarm)) {
            mAlarmStatusView.setText(nextAlarm);
            mAlarmStatusView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_lock_idle_alarm, 0, 0, 0);
            mAlarmStatusView.setVisibility(View.VISIBLE);
        } else {
            mAlarmStatusView.setVisibility(View.GONE);
        }
	}
	
	public boolean needsInput() {
		return false;
	}

	public void onAttachedToWindow(){
		
	}
	
	public void onDetachedFromWindow(){
		
	}
	
	public boolean isEnableSystemWallpaper(){
	    return true;
	}
	
	public boolean isFullScreenStyle(){
		return false;
	}
	
	public boolean isEnableScreenRotation(){
		return false;
	}
	
	public int getLockWindowAnimations(){
    	return 0;
    }
	
	public void reset(){
		if(mQsLockScreenCtrlInterface != null){
			mQsLockScreenCtrlInterface.reset();
		}
	}

	public void onPause(){
		if(mQsLockScreenCtrlInterface != null){
			mQsLockScreenCtrlInterface.onPause();
		}
	}
	
	public void onResume(int reason){
		if(mQsLockScreenCtrlInterface != null){
			mQsLockScreenCtrlInterface.onResume();
		}
	}
	
	public void cleanUp() {
		if(mQsLockScreenCtrlInterface != null){
			mQsLockScreenCtrlInterface.cleanUp();
		}
    }
	
	public void onRefreshCarrierInfo(CharSequence plmn, CharSequence spn, int simId) {
    }
    
//    public void onRefreshCarrierInfoGemini(CharSequence plmn, CharSequence spn, int simId){
//    }
    
//	//com.android.internal.telephony.IccCardConstants.State
	public void onSimStateChanged(String simState, int simId) {
        
    }
	//com.android.internal.telephony.IccCardConstants.State
//	public void onSimStateChangedGemini(String simState, int simId){
//	    
//    }
    
    public void onSearchNetworkUpdate(int simId, boolean switchOn){
    }
	
	public void onRingerModeChanged(int state){
		
	}
	
	public void onPhoneStateChanged(int phoneState) {
    }
	
	public void refreshBatteryStringAndIcon(JzsBatteryStatus status){
		refreshBatteryStringAndIcon(status.isPluggedIn(), 
				status.isCharged(), status.isBatteryLow(), status.level);
    }

    public void refreshBatteryStringAndIcon(boolean isPluggedIn, boolean isCharged, boolean isBatteryLow, int level){
        
    }
    
    public void setOnDismissAction(OnDismissAction action) {
    	if(mLockScreenCallback != null){
			mLockScreenCallback.setOnDismissAction(action);
		}
    }
    
    protected void beforeUnlockAction(){
    	
    }
    
    public final int getPhoneState(){
        if(mLockScreenCallback != null){
            return mLockScreenCallback.getPhoneState();
        }
        return 0;
    }
    public final String getSimState(){
        if(mLockScreenCallback != null){
            return mLockScreenCallback.getSimState();
        }
        return null;
    }
    // com.android.internal.telephony.PhoneConstants.GEMINI_SIM_1
    public final String getSimState(int simid){
        if(mLockScreenCallback != null){
            return mLockScreenCallback.getSimState(simid);
        }
        return null;
    }
    public final CharSequence getTelephonyPlmn(){
        if(mLockScreenCallback != null){
            return mLockScreenCallback.getTelephonyPlmn();
        }
        return null;
    }
    public final CharSequence getTelephonyPlmn(int simid){
        if(mLockScreenCallback != null){
            return mLockScreenCallback.getTelephonyPlmn(simid);
        }
        return null;
    }
    public final CharSequence getTelephonySpn(){
        if(mLockScreenCallback != null){
            return mLockScreenCallback.getTelephonySpn();
        }
        return null;
    }
    public final CharSequence getTelephonySpn(int simid){
        if(mLockScreenCallback != null){
            return mLockScreenCallback.getTelephonySpn(simid);
        }
        return null;
    }
    public final boolean dmIsLocked(){
        if(mLockScreenCallback != null){
            return mLockScreenCallback.dmIsLocked();
        }
        return false;
    }
    
    public boolean isSimLocked(){
        if(mLockScreenCallback != null){
            return mLockScreenCallback.isSimLocked();
        }
        return false;
    }

	protected void gotoUnlock(){
		if(mLockScreenCallback != null){
			(new Handler()).post(mBeforeUnlockRunnable);
			
			mLockScreenCallback.gotoUnlock();
		}
	}
	
	public final void dismissTimeOut(){
		if(mLockScreenCallback != null){
			mLockScreenCallback.dismissTimeOut();
		}
	}
	
	protected void setLockScreenWallpaper(boolean useWallpaper){
		setLockScreenWallpaper(null, useWallpaper);
    }

	protected void setLockScreenWallpaper(Drawable dr, boolean useWallpaper){
		if(mLockScreenCallback != null){
			mLockScreenCallback.setLockScreenWallpaper(dr, useWallpaper);
		}
    }
	
	public final void onTrigger(View v, int whichHandle, String data){
		
		if (whichHandle  > QsLockScreenOnTriggerListener.ACTION_NONE) {//== QsSlidingCtrl.OnTriggerListener.UNLOCK_HANDLE) {
            // delay the unlock so that the animation can complete
            if(whichHandle == QsLockScreenOnTriggerListener.ACTION_SILENT){
            	
            	onRingerModeChanged(SWITCH_RINGER_MODE);            	
                
                dismissTimeOut();
                return;
            }else if(whichHandle == QsLockScreenOnTriggerListener.ACTION_UNLOCK){
            	gotoUnlock();
                return;
            } else {
                startAction(whichHandle, data);
            }

        } else {
        	dismissTimeOut();
        }
	}
	
	public final void onGrabbedStateChange(View v, int grabbedState){
		dismissTimeOut();
	}
	
	protected void startAction(int action, String data){
		final Intent intent;
		//android.util.Log.w("QsLog", "QsLockScreen::startAction()==whichHandle:"+action+"====");
		switch(action){
		case QsLockScreenOnTriggerListener.ACTION_CAMERA:

			if(mLockScreenCallback != null){
				(new Handler()).post(mBeforeUnlockRunnable);		
				mLockScreenCallback.launchCamera();
			}
            dismissTimeOut();
			return;
            
		case QsLockScreenOnTriggerListener.ACTION_DIAL:
			intent = new Intent(Intent.ACTION_DIAL);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			break;
            
		case QsLockScreenOnTriggerListener.ACTION_MMS:
			intent = new Intent(Intent.ACTION_MAIN);
			intent.setType("vnd.android-dir/mms-sms");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			break;
            
		case QsLockScreenOnTriggerListener.ACTION_CALLLOG:
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setType("vnd.android.cursor.dir/calls");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            break;

        case QsLockScreenOnTriggerListener.ACTION_CONTACTS:
			intent = new Intent(Intent.ACTION_VIEW);
    		intent.setType("vnd.android.cursor.dir/person");
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            break;

        case QsLockScreenOnTriggerListener.ACTION_BROWSER:
			intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_BROWSER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            break;
        case QsLockScreenOnTriggerListener.ACTION_CUSTOM_INTENT:
            try{
                if(data != null){
                    intent = Intent.getIntent(data);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    intent = null;
                }
            } catch (java.net.URISyntaxException e){
            	dismissTimeOut();
                return;
            }
            
            break;
        case QsLockScreenOnTriggerListener.ACTION_EMAIL:
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            break;
		default:
            intent = null;
			break;
		}

		dismissTimeOut();
		if(intent != null && mLockScreenCallback != null){
			(new Handler()).post(mBeforeUnlockRunnable);
			mLockScreenCallback.launchActivity(intent);
		}
	}
	
	protected String getNextAlarm() {
        String nextAlarm = Settings.System.getString(mContext.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
        return nextAlarm;
    }
	
	Runnable mBeforeUnlockRunnable = new Runnable() {
        public void run() {
        	beforeUnlockAction();
        }
    };
}
