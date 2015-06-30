package com.jzs.common;

import android.os.Parcel;
import com.jzs.common.content.JzsContext;

/**
 * {@hide}
 */
public abstract class JzsFirstLastState {
	
	public abstract IFirstLastState getJzsHelper();
    
	public boolean isSupportFirstLastState(){
		if(getJzsHelper() != null)
			return getJzsHelper().isSupportFirstLastState();
		return false;
	}    
    
	public void setQsViewFirstLast(int value){
		if(getJzsHelper() != null)
			getJzsHelper().setViewFirstLast(value);
    }

    public int getQsViewFirstLast(){
    	if(getJzsHelper() != null)
			return getJzsHelper().getViewFirstLast();
    	return 0;
    }
    
    public void setQsViewFirst(){
    	if(getJzsHelper() != null)
    		getJzsHelper().setViewFirst();
    }

    public void setQsViewLast(){
    	if(getJzsHelper() != null)
    		getJzsHelper().setViewLast();
    }

    public void revertQsViewFirst(){
    	if(getJzsHelper() != null)
    		getJzsHelper().revertViewFirst();
    }

    public void revertQsViewLast(){
    	if(getJzsHelper() != null)
    		getJzsHelper().revertViewLast();
    }

    public boolean isQsViewFirst(){
    	if(getJzsHelper() != null)
			return getJzsHelper().isViewFirst();
    	return false;
    }

    public boolean isQsViewLast(){
    	if(getJzsHelper() != null)
			return getJzsHelper().isViewLast();
        return false;
    }
    
    public void writeToParcel(Parcel dest, int flags){
    	if(getJzsHelper() != null)
    		getJzsHelper().writeToParcel(dest, flags);
	}
    
    public void readFromParcel(Parcel in){
    	if(getJzsHelper() != null)
    		getJzsHelper().readFromParcel(in);
	}
}
