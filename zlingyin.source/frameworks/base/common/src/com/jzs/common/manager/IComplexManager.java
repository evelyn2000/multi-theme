package com.jzs.common.manager;

import com.jzs.common.plugin.IPluginManager;

public interface IComplexManager {
    public final static String MANAGER_SERVICE_PREFIX = "Jzs";
    public final static String MANAGER_SERVICE = "LyComplexMgrSev";
    
    IAppsManager getAppsManager();
    IPluginManager getPluginManager();
    
    Object getSystemService(String name);
    
    ISystemVersionContent checkSystemNewVersion();
    ISystemVersionContent checkSystemNewVersion(String url, int timeout);
    
    int sendCommand(String cmd);
    byte[] readFile(String file, int buffsize);
    
    long getSystemCookiesFilesSize();
    long startSystemCleaner();
}
