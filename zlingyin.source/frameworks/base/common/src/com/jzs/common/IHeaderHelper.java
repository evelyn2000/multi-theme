package com.jzs.common;

/**
 * {@hide}
 */
public interface IHeaderHelper extends IFirstLastState {
	public final static int QS_SUMMARY_NONE = 0;
    public final static int QS_SUMMARY_BOTTOM = 1;
    public final static int QS_SUMMARY_RIGHT = 2;
    
	int getSummaryStyle();
	void setSummaryStyle(int style);
}
