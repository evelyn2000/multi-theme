package com.jzs.common.manager;

public interface ISystemVersionContent {
    public String getDownloadUrl();
    public String getDetailUrl();
    public String getVersionName();
    public String getReleaseNote();
    public long getFileSize();
    
    String toString();
}
