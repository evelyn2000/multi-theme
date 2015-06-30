package com.jzs.common.plugin;

public interface IPluginServiceCallback {
    void setDefaultPluginInternal(String pluginKey);
    void resetDefaultPluginInternal(String targetKey, int pluginType);
}
