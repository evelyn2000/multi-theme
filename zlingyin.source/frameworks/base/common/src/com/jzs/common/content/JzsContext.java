package com.jzs.common.content;

import android.os.Build;

import com.jzs.common.IContextHelper;
import com.jzs.common.plugin.IPluginManager;
import com.jzs.common.theme.ThemeManager;
import android.content.Context;
import android.content.res.Resources;
/**
 * {@hide}
*/
public abstract class JzsContext{
	
	public final static String JZSCONTEXT_HELPER = "JZSCONTEXT_HELPER";
//	protected JzsContext getJzsBaseContext(){
//		return null;
//	}
	
	public com.jzs.common.IContextHelper getContextHelper(){
//		if(getJzsBaseContext() != null)
//			return getJzsBaseContext().getContextHelper();
        return null;
    }
	
//	public int getUiStyle(){
//        if(getContextHelper() != null)
//            return getContextHelper().getUiStyle();//.getUiStyle();
//        
//        return com.jzs.common.theme.ThemeManager.STYLE_DEFAULT;
//    }
//    
//    public int getMainUiStyle(){
//        if(getContextHelper() != null)
//            return getContextHelper().getMainUiStyle();
//        return com.jzs.common.theme.ThemeManager.STYLE_DEFAULT;
//    }
//    
//    public int getSubUiStyle(){
//        if(getContextHelper() != null)
//            return getContextHelper().getSubUiStyle();
//        return com.jzs.common.theme.ThemeManager.SUB_STYLE_ANY;
//    }
//    
//    public void setUiStyle(JzsContext context){
//        setUiStyle(context.getContextHelper());
//    }
//    
//    public void setUiStyle(IContextHelper context){
//        if(context != null)
//            setUiStyle(context.getMainUiStyle(), context.getSubUiStyle());
//    }
//    
//    public void setUiStyle(int nMainStyle, int nSubStyle){
//        setUiStyle(ThemeManager.getThemeStyleIdentify(nMainStyle, nSubStyle));
//    }
//    
//    public void setUiStyle(int nStyle){
//        if(getContextHelper() != null)
//            getContextHelper().setUiStyle(nStyle);
//    }
//
//    public void applyCurrentUiStyle(){
//        if(getContextHelper() != null)
//            getContextHelper().applyCurrentQsUiStyle();
//    }
	
	public int getQsUiStyle(){
		if(getContextHelper() != null)
			return getContextHelper().getMainUiStyle();//.getUiStyle();
		
		return com.jzs.common.theme.ThemeManager.STYLE_DEFAULT;
	}
	
	public int getQsMainUiStyle(){
		if(getContextHelper() != null)
			return getContextHelper().getMainUiStyle();
		return com.jzs.common.theme.ThemeManager.STYLE_DEFAULT;
	}
    
	public int getQsSubUiStyle(){
		if(getContextHelper() != null)
			return getContextHelper().getSubUiStyle();
		return com.jzs.common.theme.ThemeManager.SUB_STYLE_ANY;
	}
    
	public void setQsUiStyle(JzsContext context){
		setQsUiStyle(context.getContextHelper());
    }
	
	public void setQsUiStyle(IContextHelper context){
		if(context != null)
			setQsUiStyle(context.getMainUiStyle(), context.getSubUiStyle());
    }
	
	public void setQsUiStyle(int nMainStyle, int nSubStyle){
    	setQsUiStyle(ThemeManager.getThemeStyleIdentify(nMainStyle, nSubStyle));
    }
    
    public void setQsUiStyle(int nStyle){
        if(getContextHelper() != null)
	        getContextHelper().setUiStyle(nStyle);
    }

    public void applyCurrentQsUiStyle(){
		if(getContextHelper() != null)
	        getContextHelper().applyCurrentQsUiStyle();
    }
    
    public int selectDefaultTheme(int curTheme, int targetSdkVersion) {
        if(getContextHelper() != null)
            return getContextHelper().selectDefaultTheme(curTheme, targetSdkVersion);

        return selectSystemTheme(curTheme, targetSdkVersion,
                android.R.style.Theme,
                android.R.style.Theme_Holo,
                android.R.style.Theme_DeviceDefault);
    }
    
    public int selectSystemTheme(int curTheme, int targetSdkVersion,
            int orig, int holo, int deviceDefault) {
    	
        if(getContextHelper() != null)
            return getContextHelper().selectSystemTheme(curTheme, targetSdkVersion, orig, holo, deviceDefault);

        if (curTheme != 0) {
            return curTheme;
        }
        if (targetSdkVersion < Build.VERSION_CODES.HONEYCOMB) {
            return orig;
        }
        if (targetSdkVersion < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return holo;
        }
        return deviceDefault;
    }

    public IPluginManager getJzsPluginManager(){
    	return (IPluginManager)getSystemService(IPluginManager.PLUGIN_MANAGER_SERVICE);
    }
    
    public abstract Object getSystemService(String name);
    public abstract Context getJzsContext();
}
