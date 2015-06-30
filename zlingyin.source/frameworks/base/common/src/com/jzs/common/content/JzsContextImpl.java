package com.jzs.common.content;

import com.jzs.common.IContextHelper;

import android.content.Context;
import android.util.Log;

/**
 * {@hide}
*/
public abstract class JzsContextImpl extends Context {
	private com.jzs.common.IContextHelper mJzsContextHelper;
	
	public void applyCurrentQsUiStyle(){
        if(mJzsContextHelper == null){
        	mJzsContextHelper = (IContextHelper)getSystemService(JzsContext.JZSCONTEXT_HELPER);
        }

        if(mJzsContextHelper != null){
            mJzsContextHelper.applyCurrentQsUiStyle();
            getJzsOuterContext().setQsUiStyle(mJzsContextHelper);
        }
    }
    
    //@Override
    public void setQsUiStyle(IContextHelper context){
		if(mJzsContextHelper == null && context != null)
	        mJzsContextHelper = context;
		
		if(mJzsContextHelper != null && context != null)
			mJzsContextHelper.setUiStyle(context.getUiStyle());
    }
    
    public void applyCurrentUiStyle(){
        if(mJzsContextHelper == null){
            mJzsContextHelper = (IContextHelper)getSystemService(JzsContext.JZSCONTEXT_HELPER);
        }

        if(mJzsContextHelper != null){
            mJzsContextHelper.applyCurrentQsUiStyle();
            getJzsOuterContext().setQsUiStyle(mJzsContextHelper);
        }
    }
    
    //@Override
    public void setUiStyle(IContextHelper context){
        if(mJzsContextHelper == null && context != null)
            mJzsContextHelper = context;
        
        if(mJzsContextHelper != null && context != null)
            mJzsContextHelper.setUiStyle(context.getUiStyle());
    }

    //@Override
    public com.jzs.common.IContextHelper getContextHelper(){
        return mJzsContextHelper;
    }
    
    
    protected abstract JzsContext getJzsOuterContext();
}
