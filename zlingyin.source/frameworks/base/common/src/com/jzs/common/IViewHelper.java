package com.jzs.common;

import android.view.View;

/**
 * {@hide}
 */
public interface IViewHelper extends IFirstLastState {
	
	public static final int[] QS_FIRST_STATE_SET = {android.R.attr.state_first};
    public static final int[] QS_LAST_STATE_SET = {android.R.attr.state_last};
    
    int[] onCreateDrawableState(int extraSpace, int viewFlags, int privateFlags);
    boolean getQsDisableParentPadding();
    
    int getQsBackgroundResourceId();
    
    View getView();
    //void refreshDrawableState();
}
