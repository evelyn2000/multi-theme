package com.jzs.common.theme;

import com.jzs.common.content.QsIntent;
import com.jzs.common.os.JzsSystemProperties;
//import com.jzs.common.utils.UtilHelper;

/* 
 * @hide 
 */
public abstract class ThemeManager {
	
	public final static String BOOT_ANIMATION_KEY = "persist.qs.bootanimation";
	public final static String SHUT_ANIMATION_KEY = "persist.qs.shutanimation";
    
    
	public static final int STYLE_UNKOWN = -1;
	public static final int STYLE_DEFAULT = 0;
    public static final int STYLE_DROID_ICS = STYLE_DEFAULT;
    public static final int STYLE_DROID_GB = 1;       // values-qss1
    public static final int STYLE_AUTOMATCH = 2;            // values-qss2
    public static final int STYLE_QISHANG = 3;        // values-qss3
    public static final int STYLE_IPHONE = 4;         // values-qss4
    public static final int STYLE_WPHONE = 5;         // values-qss5
    public static final int STYLE_HTC = 6;            // values-qss6
    public static final int STYLE_MEGOO = 7;          // values-qss7
    public static final int STYLE_MOTO = 8;           // values-qss8
    public static final int STYLE_MEIZU = 9;          // values-qss9
    public static final int STYLE_XIAOMI = 10;        // values-qss10
    public static final int STYLE_SAMSUNG = 11;       // values-qss11
    public static final int STYLE_IHD = 12;           // values-qss12
    public static final int STYLE_LAOREN = 13;           // values-qss13
    

    public static final int SUB_STYLE_ANY = 0;         
    public static final int SUB_STYLE_FY = 1;          // values-qsc1
    public static final int SUB_STYLE_ING = 2;         // values-qsc2
    public static final int SUB_STYLE_IHD = 3;         // values-qsc3
    public static final int SUB_STYLE_HY = 4;          // values-qsc4
    public static final int SUB_STYLE_JX = 5;          // values-qsc5
    public static final int SUB_STYLE_I9100 = 6;          // values-qsc6
    public static final int SUB_STYLE_I9500 = 7;          // values-qsc7
    
    public static final int SUB_STYLE_QS1 = 9;          // values-qsc9   for 3d
    public static final int SUB_STYLE_QS2 = 10;          // values-qsc9
    
    private final static int SUB_STYLE_ID_SHIFT = 16;
    public static int getThemeStyleIdentify(int nMainStyle, int nSubStyle){
    	return (nMainStyle > STYLE_UNKOWN ? ((nSubStyle <= 0 ? 0 : ((nSubStyle&0xFFFF) << SUB_STYLE_ID_SHIFT)) | (nMainStyle&0xFFFF)) : 0);
    }
    
    public static int getMainStyleByIdentify(int style){
    	return (style & 0xFFFF);
    }
    
    public static int getSubStyleByIdentify(int style){
    	return ((style >> SUB_STYLE_ID_SHIFT) & 0xFFFF);
    }
    
    public static final int GET_MAIN_STYLE = JzsSystemProperties.getMainTheme(STYLE_DEFAULT);
    public static final int GET_SUB_STYLE = JzsSystemProperties.getSubTheme(SUB_STYLE_ANY);
    
    public static final int GET_CUR_STYLE_IDENTIFY = getThemeStyleIdentify(GET_MAIN_STYLE, GET_SUB_STYLE);
    
    
    public final static int getLockscreenStyle(){
        return JzsSystemProperties.getLockscreenStyle(STYLE_DEFAULT);
    }
    
    public final static void changeLockscreenStyle(int nStyle){
    	JzsSystemProperties.setLockscreenStyle(nStyle);
    }

    public final static void changeLockscreenStyle(android.content.Context context, int nStyle){
    	changeLockscreenStyle(nStyle);
        context.sendBroadcast(new android.content.Intent(QsIntent.ACTION_JZS_LOCKSCREEN_CHANGED));
    }
}
