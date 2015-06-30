package com.jzs.common.plugin;

public interface IPluginService {
    public boolean applyPlugin();
    public boolean applyAssignPlugin(IPlugin plugin, IPlugin oldplugin);
    public boolean resetPlugin(IPlugin plugin);
    
    public void setPluginCallback(IPluginServiceCallback callback);
}
