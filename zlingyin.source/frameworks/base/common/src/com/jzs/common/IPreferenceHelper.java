package com.jzs.common;

import android.preference.Preference;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@hide}
 */
public interface IPreferenceHelper extends IHeaderHelper {

	void onCreateView(ViewGroup parent, IViewHelper layout, ViewGroup widgetFrame);
	void onBindView(IViewHelper view, View title, View summary, View icon);
	
	boolean changeOrder(int order, int curOrder);
	int compareTo(int order, int another);
	Preference getPreference();
	int getDefaultLayoutResId(int dr);
}
