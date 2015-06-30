package com.jzs.internal.widget;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.IccCardConstants.State;
import com.android.internal.widget.LockPatternUtils;

public class MediatekCarrierText extends LinearLayout {
	private final static String TAG = "MediatekCarrierText";
	
	private LockPatternUtils mLockPatternUtils;
	private static CharSequence mSeparator;
	
    private TextView mCarrierView;
    private TextView mCarrierGeminiView;
    private TextView mCarrierDivider;
    
    private StatusMode mStatusMode;
    private StatusMode mStatusModeGemini;
    
    private CharSequence mPlmn;
    private CharSequence mSpn;
    private IccCardConstants.State mSimState;
    
    /// M: Add for gemini support @{
    private CharSequence mPlmnGemini;
    private CharSequence mSpnGemini;
    private IccCardConstants.State mSimStateGemini;
    
    public static enum StatusMode {
        Normal, // Normal case (sim card present, it's not locked)
        NetworkLocked, // SIM card is 'network locked'.
        SimMissing, // SIM card is missing.
        SimMissingLocked, // SIM card is missing, and device isn't provisioned; don't allow access
        SimPukLocked, // SIM card is PUK locked because SIM entered wrong too many times
        SimLocked, // SIM card is currently locked
        SimPermDisabled, // SIM card is permanently disabled due to PUK unlock failure
        SimNotReady, // SIM is not ready yet. May never be on devices w/o a SIM.
        
        /// M: mediatek add sim state
        SimUnknown,
        NetworkSearching;  //The sim card is ready, but searching network
    }
    
	public MediatekCarrierText(Context context) {
        this(context, null);
    }

    public MediatekCarrierText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLockPatternUtils = new LockPatternUtils(context);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        mSeparator = getResources().getString(com.android.internal.R.string.kg_text_message_separator);
        mCarrierView = (TextView)this.findViewWithTag("carrier");
        mCarrierGeminiView = (TextView)this.findViewWithTag("carrierGemini");
        mCarrierDivider = (TextView)this.findViewWithTag("carrierDivider");
        
        if(mCarrierView != null)
        	mCarrierView.setSelected(true);
        if(mCarrierGeminiView != null)
        	mCarrierGeminiView.setSelected(true);
    }
    
    public void onRefreshCarrierInfo(CharSequence plmn, CharSequence spn){
    	mPlmn = plmn;
        mSpn = spn;
    	updateCarrierText(mSimState, mPlmn, mSpn);
    }
    
    public void onSimStateChanged(IccCardConstants.State simState){
    	mSimState = simState;
        updateCarrierText(mSimState, mPlmn, mSpn);
    }
    
    public void onRefreshCarrierInfo(CharSequence plmn, CharSequence spn, int simId) {
    	if (PhoneConstants.GEMINI_SIM_1 == simId) {
            mPlmn = plmn;
            mSpn = spn;
            updateCarrierTextGemini(mSimState, mPlmn, mSpn, simId);
        } else {
            mPlmnGemini = plmn;
            mSpnGemini = spn;
            updateCarrierTextGemini(mSimStateGemini, mPlmnGemini, mSpnGemini, simId);
        }
    }
    
    public void onSimStateChanged(IccCardConstants.State simState, int simId){
    	if (PhoneConstants.GEMINI_SIM_1 == simId) {
            mSimState = simState;
            updateCarrierTextGemini(mSimState, mPlmn, mSpn, simId);
        } else {
            mSimStateGemini = simState;
            updateCarrierTextGemini(mSimStateGemini, mPlmnGemini, mSpnGemini, simId);
        }
    }
    
    public void onSearchNetworkUpdate(int simId, boolean switchOn){
    	if (switchOn) {
            String carrierText = getContext().getString(com.jzs.internal.R.string.network_searching);
            if (mCarrierGeminiView != null) {
                if (PhoneConstants.GEMINI_SIM_1 == simId) {
                    mStatusMode = StatusMode.NetworkSearching;
                } else {
                    mStatusModeGemini = StatusMode.NetworkSearching;
                }
                updateCarrierTextGeminiForSearchNetwork(carrierText, simId);
            } else {
                mStatusMode = StatusMode.NetworkSearching;
                updateCarrierTextForSearchNetwork(carrierText);
            }
        } else {
            if (mCarrierGeminiView != null) {
                if (PhoneConstants.GEMINI_SIM_2 == simId) {
                    mStatusModeGemini = getStatusForIccState(mSimStateGemini);
                    updateCarrierTextGemini(mSimStateGemini, mPlmnGemini, mSpnGemini, simId);
                } else {
                    mStatusMode = getStatusForIccState(mSimState);
                    updateCarrierTextGemini(mSimState, mPlmn, mSpn, simId);
                }
            } else {
                mStatusMode = getStatusForIccState(mSimState);
                updateCarrierText(mSimState, mPlmn, mSpn);
            }
        }
    }
    
    protected void updateCarrierText(State simState, CharSequence plmn, CharSequence spn) {
        if (StatusMode.NetworkSearching == mStatusMode) {
            //KeyguardUtils.xlogD(TAG, "updateCarrierText, searching network now, don't interrupt it, simState=" + simState);
            return;
        }
        mStatusMode = getStatusForIccState(simState);
        if (isWifiOnlyDevice()) {
            //KeyguardUtils.xlogD(TAG, "updateCarrierText WifiOnly");
            mCarrierView.setVisibility(View.GONE);
            return;
        } else {
            CharSequence text = getCarrierTextForSimState(simState, plmn, spn);
            mCarrierView.setText(text != null ? text.toString().toUpperCase() : null);
        }
    }
    
    protected void updateCarrierTextGemini(State simState, CharSequence plmn, CharSequence spn, int simId) {
        Log.d(TAG, "updateCarrierTextGemini, simState=" + simState + ", plmn=" + plmn + ", spn=" + spn + ", simId=" + simId);
        TextView toSetCarrierView;
        if (PhoneConstants.GEMINI_SIM_1 == simId) {
            toSetCarrierView = mCarrierView;
            if (StatusMode.NetworkSearching == mStatusMode) {
            	Log.d(TAG, "updateCarrierTextGemini, searching network now, don't interrupt it, simState=" + simState);
                return;
            }
            /// M: save statu mode, which will be used to decide show or hide carrier view
            mStatusMode = getStatusForIccState(simState);
        } else {
            toSetCarrierView = mCarrierGeminiView;
            if (StatusMode.NetworkSearching == mStatusModeGemini) {
            	Log.d(TAG, "updateCarrierTextGemini, searching network now, don't interrupt it, simState=" + simState);
                return;
            }
            /// M: save statu mode, which will be used to decide show or hide carrier view
            mStatusModeGemini = getStatusForIccState(simState);
        }
        showOrHideCarrier();
        if(toSetCarrierView != null){
	        CharSequence text = getCarrierTextForSimState(simState, plmn, spn);
	        toSetCarrierView.setText(text != null ? text.toString().toUpperCase() : null);
        }
    }
    
    /**
     * Top-level function for creating carrier text. Makes text based on simState, PLMN
     * and SPN as well as device capabilities, such as being emergency call capable.
     *
     * @param simState
     * @param plmn
     * @param spn
     * @return
     */
    protected CharSequence getCarrierTextForSimState(IccCardConstants.State simState,
            CharSequence plmn, CharSequence spn) {
        
        /// M: Onle set plmn to default value if both plmn and spn are null
        if (plmn == null && spn == null) {
            plmn = getDefaultPlmn();
        }

        CharSequence carrierText = null;
        StatusMode status = getStatusForIccState(simState);
        switch (status) {
            case SimUnknown:
            case Normal:
                carrierText = concatenate(plmn, spn);
                break;

            case SimNotReady:
                carrierText = concatenate(plmn, spn);
                break;

            case NetworkLocked:
                carrierText = makeCarrierStringOnEmergencyCapable(
                        getContext().getText(com.jzs.internal.R.string.lockscreen_network_locked_message), plmn);
                break;

            case SimMissing:
                // Shows "No SIM card | Emergency calls only" on devices that are voice-capable.
                // This depends on mPlmn containing the text "Emergency calls only" when the radio
                // has some connectivity. Otherwise, it should be null or empty and just show
                // "No SIM card"
                carrierText =  makeCarrierStringOnEmergencyCapable(
                        getContext().getText(com.android.internal.R.string.lockscreen_missing_sim_message_short),
                        plmn);
                break;

            case SimPermDisabled:
                carrierText = getContext().getText(com.jzs.internal.R.string.lockscreen_permanent_disabled_sim_message_short);
                break;

            case SimMissingLocked:
                carrierText =  makeCarrierStringOnEmergencyCapable(
                        getContext().getText(com.android.internal.R.string.lockscreen_missing_sim_message_short),
                        plmn);
                break;

            case SimLocked:
                carrierText = makeCarrierStringOnEmergencyCapable(
                        getContext().getText(com.jzs.internal.R.string.lockscreen_sim_locked_message),
                        plmn);
                break;

            case SimPukLocked:
                carrierText = makeCarrierStringOnEmergencyCapable(
                        getContext().getText(com.jzs.internal.R.string.lockscreen_sim_puk_locked_message),
                        plmn);
                break;
        }

        Log.d(TAG, "getCarrierTextForSimState simState=" + simState + ", PLMN=" + plmn + ", SPN=" + spn + " carrierText=" + carrierText);
        return carrierText;
    }

    /*
     * Add emergencyCallMessage to carrier string only if phone supports emergency calls.
     */
    private CharSequence makeCarrierStringOnEmergencyCapable(
            CharSequence simMessage, CharSequence emergencyCallMessage) {
        if (mLockPatternUtils.isEmergencyCallCapable()) {
            return concatenate(simMessage, emergencyCallMessage);
        }
        return simMessage;
    }

    /**
     * Determine the current status of the lock screen given the SIM state and other stuff.
     */
    protected StatusMode getStatusForIccState(IccCardConstants.State simState) {
        // Since reading the SIM may take a while, we assume it is present until told otherwise.
        if (simState == null) {
            return StatusMode.SimUnknown;
        }

        final boolean missingAndNotProvisioned =
                /*!KeyguardUpdateMonitor.getInstance(mContext).isDeviceProvisioned()
                && */(simState == IccCardConstants.State.ABSENT ||
                        simState == IccCardConstants.State.PERM_DISABLED);

        // Assume we're NETWORK_LOCKED if not provisioned
        simState = missingAndNotProvisioned ? IccCardConstants.State.NETWORK_LOCKED : simState;
        switch (simState) {
            case ABSENT:
                return StatusMode.SimMissing;
            case NETWORK_LOCKED:
                return StatusMode.SimMissingLocked;
            case NOT_READY:
                return StatusMode.SimNotReady;
            case PIN_REQUIRED:
                return StatusMode.SimLocked;
            case PUK_REQUIRED:
                return StatusMode.SimPukLocked;
            case READY:
                return StatusMode.Normal;
            case PERM_DISABLED:
                return StatusMode.SimPermDisabled;
            case UNKNOWN:
                return StatusMode.SimUnknown;
        }
        return StatusMode.SimMissing;
    }

    private static CharSequence concatenate(CharSequence plmn, CharSequence spn) {
        final boolean plmnValid = !TextUtils.isEmpty(plmn);
        final boolean spnValid = !TextUtils.isEmpty(spn);
        if (plmnValid && spnValid) {
            return new StringBuilder().append(plmn).append(mSeparator).append(spn).toString();
        } else if (plmnValid) {
            return plmn;
        } else if (spnValid) {
            return spn;
        } else {
            return "";
        }
    }
    
    protected boolean isWifiOnlyDevice() {
        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return  !(cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE));
    }
    
    /**
     * M: Used to control carrier TextView visibility in Gemini.
     * (1) if the device is wifi only, we hide both carrier TextView.
     * (2) if both sim are missing, we shwon only one carrier TextView center.
     * (3) if either one sim is missing, we shwon the visible carrier TextView center.
     * (4) if both sim are not missing, we shwon boteh TextView, one in the left the other right.
     */
    private void showOrHideCarrier() {
        boolean sim1Missing = false;
        boolean sim2Missing = false;

        if (isWifiOnlyDevice()) {
            Log.d(TAG, "showOrHideCarrier wifi only device, hide all");
            if(mCarrierDivider != null)
            	mCarrierDivider.setVisibility(View.GONE);
            if(mCarrierView != null)
            	mCarrierView.setVisibility(View.GONE);
            if(mCarrierGeminiView != null)
            	mCarrierGeminiView.setVisibility(View.GONE);
        } else {
            if (mStatusMode == StatusMode.SimMissing || mStatusMode == StatusMode.SimMissingLocked) {
                sim1Missing = true;
            }
            if (mStatusModeGemini == StatusMode.SimMissing || mStatusModeGemini == StatusMode.SimMissingLocked) {
                sim2Missing = true;
            }
            Log.d(TAG, "mSIMOneMissing=" + sim1Missing + ",mSIMTwoMissing=" + sim2Missing);
            if (sim1Missing && sim2Missing) { // both of the two sim missing, only to display SIM1
                if (mCarrierDivider != null) {
                    mCarrierDivider.setVisibility(View.GONE);
                }
                mCarrierView.setVisibility(View.VISIBLE);
                mCarrierView.setGravity(Gravity.CENTER);
                if (mCarrierGeminiView != null) {
                    mCarrierGeminiView.setVisibility(View.GONE);
                }
            } else if (sim1Missing && !sim2Missing) { //only sim one missing
                if (mCarrierDivider != null) {
                    mCarrierDivider.setVisibility(View.GONE);
                }
                mCarrierView.setVisibility(View.GONE);
                if (mCarrierGeminiView != null) {
                    mCarrierGeminiView.setVisibility(View.VISIBLE);
                    mCarrierGeminiView.setGravity(Gravity.CENTER);
                }
            } else if (!sim1Missing && sim2Missing) { //only sim two missing
                if (mCarrierDivider != null) {
                    mCarrierDivider.setVisibility(View.GONE);
                }
                mCarrierView.setVisibility(View.VISIBLE);
                mCarrierView.setGravity(Gravity.CENTER);
                if (mCarrierGeminiView != null) {
                    mCarrierGeminiView.setVisibility(View.GONE);
                }
            } else { //both of them are not missing
                if (mCarrierDivider != null) {
                    mCarrierDivider.setVisibility(View.VISIBLE);
                    mCarrierDivider.setText(" | ");
                }
                mCarrierView.setVisibility(View.VISIBLE);
                mCarrierView.setGravity(Gravity.RIGHT);
                if (mCarrierGeminiView != null) {
                    mCarrierGeminiView.setVisibility(View.VISIBLE);
                    mCarrierGeminiView.setGravity(Gravity.LEFT);
                }
            }
            
            //if (KeyguardUtils.isGemini()) {
                if (mStatusMode == StatusMode.SimUnknown) {
                	if(mCarrierDivider != null)
                		mCarrierDivider.setVisibility(View.GONE);
                    mCarrierView.setVisibility(View.GONE);
                    if(mCarrierGeminiView != null)
                    	mCarrierGeminiView.setGravity(Gravity.CENTER);
                }
                if (mCarrierGeminiView != null && mStatusModeGemini == StatusMode.SimUnknown) {
                	if(mCarrierDivider != null)
                		mCarrierDivider.setVisibility(View.GONE);
                    mCarrierGeminiView.setVisibility(View.GONE);
                    mCarrierView.setGravity(Gravity.CENTER);
                }
                Log.d(TAG, "showOrHideCarrier mCarrierView.text=" + mCarrierView.getText()  + ", mCarrierView.visibility=" + mCarrierView.getVisibility()
                        + ", mCarrierDivider.text=" + mCarrierDivider.getText() + ", mCarrierDivider.visibility=" + mCarrierDivider.getVisibility()
                        + ", mCarrierGeminiView.text=" + mCarrierGeminiView.getText() + ", mCarrierGeminiView.visibility=" + mCarrierGeminiView.getVisibility());
//            } else {
//                if (mStatusMode == StatusMode.SimUnknown) {
//                    mCarrierView.setVisibility(View.GONE);
//                }
//            }
        }
    }
    
    protected void updateCarrierTextForSearchNetwork(String carrierText) {
    	Log.d(TAG, "updateCarrierTextForSearchNetwork  carrierText=" + carrierText);
        if (isWifiOnlyDevice()) {
        	Log.d(TAG, "updateCarrierTextForSearchNetwork WifiOnly");
            mCarrierView.setVisibility(View.GONE);
        } else {
            mCarrierView.setVisibility(View.VISIBLE);
            mCarrierView.setText(carrierText);
        }
    }
    
    protected void updateCarrierTextGeminiForSearchNetwork(String carrierText, int simId) {
        Log.d(TAG, "updateCarrierTextGeminiForSearchNetwork carrierText=" + carrierText + ", simId=" + simId);
        if (isWifiOnlyDevice()) {
        	Log.d(TAG, "updateCarrierTextGeminiForSearchNetwork WifiOnly");
            mCarrierView.setVisibility(View.GONE);
        } else {
            if (PhoneConstants.GEMINI_SIM_1 == simId) {
                mCarrierView.setText(carrierText);
            } else if(mCarrierGeminiView != null) {
                mCarrierGeminiView.setText(carrierText);
            }
            showOrHideCarrier();
        }
    }
    
    public void onPhoneStateChanged(int phoneState) { 
        
    }
    
    private CharSequence getDefaultPlmn() {
        return getContext().getResources().getText(com.android.internal.R.string.lockscreen_carrier_default);
    }
}
