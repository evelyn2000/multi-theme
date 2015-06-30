package com.jzs.internal.widget;

import com.jzs.common.widget.JzsBatteryStatus;

import android.content.Context;
import android.os.BatteryManager;
import android.util.AttributeSet;
import android.widget.TextView;
import libcore.util.MutableInt;

public class KeyguardMessageArea extends TextView {
	
	private CharSequence mSeparator;
	// are we showing battery information?
	private boolean mShowingBatteryInfo = false;

    // last known plugged in state
    private boolean mPluggedIn = false;

    // last known battery level
    private int mBatteryLevel = 100;
    private boolean mBatteryCharged;
    private boolean mBatteryIsLow;
    
    private int mBatteryDetialStatus;
	
	public KeyguardMessageArea(Context context) {
        this(context, null);
    }

    public KeyguardMessageArea(Context context, AttributeSet attrs) {
        super(context, attrs);

        // This is required to ensure marquee works
        setSelected(true);

        mSeparator = getResources().getString(com.android.internal.R.string.kg_text_message_separator);

        update();
    }
    
    public void refreshBatteryStringAndIcon(JzsBatteryStatus status){
    	refreshBatteryStringAndIcon(status.isPluggedIn(), 
				status.isCharged(), status.isBatteryLow(), status.level, status.status);
    }    
    
    public void refreshBatteryStringAndIcon(boolean isPluggedIn, boolean isCharged, 
    		boolean isBatteryLow, int level, int detailStatus){
    	mShowingBatteryInfo = isPluggedIn || isBatteryLow;
        mPluggedIn = isPluggedIn;
        mBatteryLevel = level;
        mBatteryCharged = isCharged;
        mBatteryIsLow = isBatteryLow;
        mBatteryDetialStatus = detailStatus;
        
        update();
    }
    
    protected void update() {
        MutableInt icon = new MutableInt(0);
        CharSequence status = getChargeInfo(icon);//concat(getChargeInfo(icon), getOwnerInfo(), getCurrentMessage());
        setCompoundDrawablesWithIntrinsicBounds(icon.value, 0, 0, 0);
        
        setText(status);
    }
    
    protected CharSequence getChargeInfo(MutableInt icon) {
        CharSequence string = null;
        if (mShowingBatteryInfo/* && !mShowingMessage*/) {
            // Battery status
            /// M: Add a new condition, if device is not
            if (mPluggedIn && isDeviceCharging()) {
                // Charging, charged or waiting to charge.
                string = getContext().getString(mBatteryCharged ?
                        com.jzs.internal.R.string.lockscreen_charged
                        :com.jzs.internal.R.string.lockscreen_plugged_in, mBatteryLevel);
                icon.value = 0;
            } else if (mBatteryIsLow) {
                // Battery is low
                string = getContext().getString(
                        com.jzs.internal.R.string.lockscreen_low_battery);
                icon.value = 0;
            }
        }
        return string;
    }
    
    public boolean isCharged(){
    	return mBatteryCharged;
    }
    
    public boolean isPluggedIn(){
    	return mPluggedIn;
    }
    
    public boolean isBatteryLow(){
    	return mBatteryIsLow;
    }
    
    public int getBatteryLevel(){
    	return mBatteryLevel;
    }
    
    public boolean isDeviceCharging() {
        return mBatteryDetialStatus != BatteryManager.BATTERY_STATUS_DISCHARGING
                && mBatteryDetialStatus != BatteryManager.BATTERY_STATUS_NOT_CHARGING;
    }
}
