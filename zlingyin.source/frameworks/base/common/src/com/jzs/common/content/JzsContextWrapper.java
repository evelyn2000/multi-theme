package com.jzs.common.content;

import com.jzs.common.IContextHelper;

import android.content.Context;
import android.os.Build;

/**
 * {@hide}
*/
public abstract class JzsContextWrapper extends Context {


	
	public com.jzs.common.IContextHelper getContextHelper(){
		if(getJzsBaseContext() != null)
			return getJzsBaseContext().getContextHelper();
        return null;
    }

	public void setQsUiStyle(IContextHelper context){

		if(getJzsBaseContext() != null)
			getJzsBaseContext().setQsUiStyle(context);
    }

	//@Override
    public void applyCurrentQsUiStyle(){
    	if(getJzsBaseContext() != null)
    		getJzsBaseContext().applyCurrentQsUiStyle();
    }
 
    public void setUiStyle(IContextHelper context){

        if(getJzsBaseContext() != null)
            getJzsBaseContext().setQsUiStyle(context);
    }
    
//    public void applyCurrentUiStyle(){
//        if(getJzsBaseContext() != null)
//            getJzsBaseContext().applyCurrentQsUiStyle();
//    }
    
    protected abstract JzsContext getJzsBaseContext();
}
