package com.jzs.android.systemstyle.controls;

public interface PageViewIndicatorLister {
	
	public int getCurrentPageScreen();
	public int getPageViewCount();
	public void setPageViewIndicatorCallback(PageViewIndicatorCallback callback);
}
