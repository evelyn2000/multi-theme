package com.jzs.common;



import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.util.AttributeSet;
import android.view.View;

/**
 * {@hide}
 */
public interface IContextHelper {
    
    public final static String JZSCONTEXT_HELPER = "JZSCONTEXT_HELPER";
    
	int getUiStyle();
	int getMainUiStyle();
	int getSubUiStyle();
	//void setUiStyle(IContextHelper context);
	void setUiStyle(int nStyle);
	//void setUiStyle(int nMainStyle, int nSubStyle);
	void applyCurrentQsUiStyle();
	int selectDefaultTheme(int curTheme, int targetSdkVersion);
	int selectSystemTheme(int curTheme, int targetSdkVersion,
            int orig, int holo, int deviceDefault);
	
	Context getContext();
	

	IPreferenceHelper createPreferenceHelper(Preference perference, AttributeSet attrs);
	IViewHelper createViewHelper(View view, AttributeSet attrs, int defStyle);
	
	IHeaderHelper getHeaderHelper();
	
	IPreferenceGroupAdapterHelper getPreferenceGroupAdapterHelper(PreferenceGroup preferenceGroup, 
			IPreferenceGroupAdapterHelperCallback callback);
	
//	IPreferenceGroupHelper getPreferenceGroupHelper(PreferenceGroup preference, 
//			AttributeSet attrs);
//	
//	IPreferenceHelper getPreferenceHelper(Preference perference, AttributeSet attrs);
}
