package com.jzs.internal;

import android.content.Context;
import android.os.PowerManager;

import com.jzs.common.os.JzsSystemProperties;
import com.jzs.common.plugin.IPlugin;
import com.jzs.common.plugin.IPluginManager;
import com.jzs.common.plugin.IPluginServiceCallback;
import com.jzs.common.plugin.PluginService;
import com.jzs.common.theme.ThemeManager;

public class JzsSupportSystemThemes extends PluginService {
    
    public JzsSupportSystemThemes(Context context){
        super(context);
    }
    
    @Override
    public boolean applyAssignPlugin(IPlugin plugin, IPlugin oldplugin){
        super.applyAssignPlugin(plugin, oldplugin);
//      android.util.Log.i("QsLog", "applyAssignPlugin(11)=plugin:"+plugin
//              +"=old:"+oldplugin
//              +"=this:"+this);
        if(plugin != null){
            JzsSystemProperties.setJzsTheme(plugin.getPluginForThemeMainStyle(), plugin.getPluginForThemeSubStyle());
            String animfile = "", shutanim = "";
            animfile = String.format("boot%d.zip", plugin.getPluginForThemeMainStyle());
            shutanim = String.format("shut%d.zip", plugin.getPluginForThemeMainStyle());
            JzsSystemProperties.set(ThemeManager.BOOT_ANIMATION_KEY, animfile);
            JzsSystemProperties.set(ThemeManager.SHUT_ANIMATION_KEY, shutanim);
            if(getPluginCallback() != null) getPluginCallback().resetDefaultPluginInternal(IPluginManager.PLUGIN_FOR_LOCKSCREEN, IPluginManager.PLUGIN_TYPE_STYLE);
        }
        
        
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        if(pm != null)
            pm.reboot("theme");
        
        return true;
    }
    
    @Override
    public boolean resetPlugin(IPlugin plugin){
        return super.resetPlugin(plugin);
    }
}
