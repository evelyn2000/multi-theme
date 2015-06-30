package com.jzs.common;

import android.os.Parcel;

/**
 * {@hide}
 */
public interface IFirstLastState extends IJzsHelper {
	void setSupportFirstLastState(IContextHelper context);
	//void setSupportFirstLastState(boolean enable);
	boolean isSupportFirstLastState();
	void setViewFirstLast(int value);
	int getViewFirstLast();
	void setViewFirst();
	void setViewLast();
	void revertViewFirst();
	void revertViewLast();
	boolean isViewFirst();
	boolean isViewLast();
	void writeToParcel(Parcel dest, int flags);
	void readFromParcel(Parcel in);
}
