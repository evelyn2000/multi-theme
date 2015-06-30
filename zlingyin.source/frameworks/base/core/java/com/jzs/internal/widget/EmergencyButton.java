/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jzs.internal.widget;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.IccCardConstants.State;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.R;

import com.jzs.common.widget.QsLockScreen;

/**
 * This class implements a smart emergency button that updates itself based
 * on telephony state.  When the phone is idle, it is an emergency call button.
 * When there's a call in progress, it presents an appropriate message and
 * allows the user to return to the call.
 */
public class EmergencyButton extends Button {

    private static final int EMERGENCY_CALL_TIMEOUT = 10000; // screen timeout after starting e.d.
    private static final String ACTION_EMERGENCY_DIAL = "com.android.phone.EmergencyDialer.DIAL";
    /// M: CTA new feature
    private static final String TAG = "EmergencyButton";

    public void onSimStateChanged(IccCardConstants.State simState) {
        int phoneState = mQsLockScreen != null ? mQsLockScreen.getPhoneState() : 0;
        updateEmergencyCallButton(phoneState);
    }

    public void onPhoneStateChanged(int phoneState) {
        updateEmergencyCallButton(phoneState);
    }
    
    //@Override
    public void onSimStateChanged(IccCardConstants.State simState, int simId) {
        int phoneState = mQsLockScreen != null ? mQsLockScreen.getPhoneState() : 0;
        updateEmergencyCallButton(phoneState);
    }

    /// M: CTA new feature @{
    //@Override
    public void onRefreshCarrierInfo(CharSequence plmn, CharSequence spn) {
        //KeyguardUtils.xlogD(TAG, "onRefreshCarrierInfo plmn=" + plmn + ", spn=" + spn);
        int phoneState = mQsLockScreen != null ? mQsLockScreen.getPhoneState() : 0;
        //String simState = mQsLockScreen != null ? mQsLockScreen.getSimState() : null;
        updateEmergencyCallButton(phoneState);
    }

    //@Override
    public void onRefreshCarrierInfo(CharSequence plmn, CharSequence spn, int simId) {
        //KeyguardUtils.xlogD(TAG, "onRefreshCarrierInfoGemini plmn=" + plmn + ", spn=" + spn + ", simId=" + simId);
        int phoneState = mQsLockScreen != null ? mQsLockScreen.getPhoneState() : 0;
        updateEmergencyCallButton(phoneState);
    }
    
    protected LockPatternUtils mLockPatternUtils;
    protected PowerManager mPowerManager;
    protected QsLockScreen mQsLockScreen;
    public void setQsLockScreen(QsLockScreen lock){
        mQsLockScreen = lock;
    }

    public EmergencyButton(Context context) {
        this(context, null);
    }

    public EmergencyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLockPatternUtils = new LockPatternUtils(mContext);
        mPowerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                takeEmergencyCallAction();
            }
        });
//        int phoneState = KeyguardUpdateMonitor.getInstance(mContext).getPhoneState();
//        State simState = KeyguardUpdateMonitor.getInstance(mContext).getSimState();
        
        /// M: Save secure query result here, when lockscreen is created, secure result should
        /// stay unchanged @{
        mIsSecure = mLockPatternUtils.isSecure();
        /// @}
        
//        updateEmergencyCallButton(simState, phoneState);
    }

    /**
     * Shows the emergency dialer or returns the user to the existing call.
     */
    public void takeEmergencyCallAction() {
        // TODO: implement a shorter timeout once new PowerManager API is ready.
        // should be the equivalent to the old userActivity(EMERGENCY_CALL_TIMEOUT)
        mPowerManager.userActivity(SystemClock.uptimeMillis(), true);
        if (TelephonyManager.getDefault().getCallState()
                == TelephonyManager.CALL_STATE_OFFHOOK) {
            mLockPatternUtils.resumeCall();
        } else {
            Intent intent = new Intent(ACTION_EMERGENCY_DIAL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            getContext().startActivity(intent);
        }
    }

    protected void updateEmergencyCallButton(/*String simState, */int phoneState) {
        boolean enabled = false;
        if (phoneState == TelephonyManager.CALL_STATE_OFFHOOK) {
            enabled = true; // always show "return to call" if phone is off-hook
        } else if (mLockPatternUtils.isEmergencyCallCapable()) {
            boolean simLocked = mQsLockScreen != null ? mQsLockScreen.isSimLocked() : false;
            if (simLocked) {
                // Some countries can't handle emergency calls while SIM is locked.
                enabled = mLockPatternUtils.isEmergencyCallEnabledWhileSimLocked();
            } else {
                // True if we need to show a secure screen (pin/pattern/SIM pin/SIM puk);
                // hides emergency button on "Slide" screen if device is not secure.
                /// M: Optimization, do not query db for secure state in every ECC update, 
                /// only query once when view creation is done @{
                // enabled = mLockPatternUtils.isSecure();
                enabled = mIsSecure;
                /// @}
            }
        }
        /// M: If dm lock is on, we should also show ECC button @{
        boolean dmLocked = mQsLockScreen != null ? mQsLockScreen.dmIsLocked() : false;
        /// M: CMCC test case, always show ECC button if we can
        boolean keyguardUtilShowEcc = false;//mKeyguardUtilExt.shouldShowEmergencyBtnForVoiceOn();
        /// M:CTA new feature
        boolean eccShouldShow = eccButtonShouldShow();
        enabled = (enabled || keyguardUtilShowEcc || dmLocked) && eccShouldShow;
        Log.i(TAG, "enabled= " + enabled + ", dmLocked=" + dmLocked + ", keyguardUtilShowEcc="
                + keyguardUtilShowEcc + ", eccShouldShow=" + eccShouldShow);
        /// @}
        mLockPatternUtils.updateEmergencyCallButtonState(this, phoneState, enabled,
                false);
    }

    /// M: CTA new feature
    public boolean eccButtonShouldShow(){
        // /M: new feature CTA  return true and show emc @{
        String plmnDefaultStr = getContext().getResources().getText(R.string.lockscreen_carrier_default).toString();
        Log.i(TAG, "plmnDefaultStr = " + plmnDefaultStr);
        if (com.mediatek.common.featureoption.FeatureOption.MTK_GEMINI_SUPPORT) {
            CharSequence mPlmn = mQsLockScreen != null ? mQsLockScreen.getTelephonyPlmn(PhoneConstants.GEMINI_SIM_1) : null;
            CharSequence mSpn = mQsLockScreen != null ? mQsLockScreen.getTelephonySpn(PhoneConstants.GEMINI_SIM_1) : null;
            boolean mPlmnIsNotDefault = mPlmn!=null && !mPlmn.toString().equals(plmnDefaultStr);
            boolean mSpnIsNotDefault = mSpn!=null && !mSpn.toString().equals(plmnDefaultStr);
            CharSequence mPlmnGemini = mQsLockScreen != null ? mQsLockScreen.getTelephonyPlmn(PhoneConstants.GEMINI_SIM_2) : null;
            CharSequence mSpnGemini = mQsLockScreen != null ? mQsLockScreen.getTelephonySpn(PhoneConstants.GEMINI_SIM_2) : null;
            boolean mPlmnGeminiIsNotDefault = mPlmnGemini!=null && !mPlmnGemini.toString().equals(plmnDefaultStr);
            boolean mSpnGeminiIsNotDefault = mSpnGemini!=null && !mSpnGemini.toString().equals(plmnDefaultStr);
            if(mPlmnIsNotDefault || mSpnIsNotDefault || mPlmnGeminiIsNotDefault || mSpnGeminiIsNotDefault){
            return true;
            }
        } else {
            CharSequence mPlmn = mQsLockScreen != null ? mQsLockScreen.getTelephonyPlmn() : null;
            CharSequence mSpn = mQsLockScreen != null ? mQsLockScreen.getTelephonySpn() : null;
            boolean mPlmnIsNotDefault = mPlmn!=null && !mPlmn.toString().equals(plmnDefaultStr);
            boolean mSpnIsNotDefault = mSpn!=null && !mSpn.toString().equals(plmnDefaultStr);
            if (mPlmnIsNotDefault || mSpnIsNotDefault) {
                return true;
            }
        }
        return false;
    }
    ///  @}

    
    /// M: Optimization, save lockpatternUtils's isSecure state
    protected boolean mIsSecure;

}
