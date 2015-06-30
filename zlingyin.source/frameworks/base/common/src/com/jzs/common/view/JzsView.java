package com.jzs.common.view;

import android.util.AttributeSet;
import android.view.View;

import com.jzs.common.IFirstLastState;
import com.jzs.common.IViewHelper;
import com.jzs.common.JzsFirstLastState;
import com.jzs.common.content.JzsContext;

/**
 * {@hide}
*/
public abstract class JzsView extends JzsFirstLastState {
	
	private com.jzs.common.IViewHelper mJzsViewHelper;
	
	public void initializeJzsHelper(JzsContext context){
		initializeJzsHelper(context, null);
	}
	
	public void initializeJzsHelper(JzsContext context, AttributeSet attrs){
		initializeJzsHelper(context, attrs, 0);
	}
	
	public void initializeJzsHelper(JzsContext context, AttributeSet attrs, int defStyle){
		if(context.getContextHelper() != null){
            mJzsViewHelper = context.getContextHelper().createViewHelper(getJzsView(), attrs, defStyle);
		}
	}
	
	//@Override
	public IFirstLastState getJzsHelper(){
		return mJzsViewHelper;//getJzsHelper(mJzsViewHelper);
	}
	
	public IViewHelper getViewHelper(){
		return (IViewHelper)getJzsHelper();
	}
	
	protected int[] onCreateDrawableState(int extraSpace, int viewFlags, int privateFlags){
		if(getViewHelper() != null)
			return getViewHelper().onCreateDrawableState(extraSpace, viewFlags, privateFlags);
		return null;
	}
	
	public boolean getQsDisableParentPadding(){
		if(getViewHelper() != null)
			return getViewHelper().getQsDisableParentPadding();
        return false;
    }
    
    public void setQsDisableParentPadding(boolean enable){
    	
    }
    
    public abstract int getQsBackgroundResourceId();
    public abstract void setBackgroundResource(int resid);
    public abstract boolean isFocused();
	public abstract void refreshDrawableState();
	public abstract View getJzsView();
	
}
