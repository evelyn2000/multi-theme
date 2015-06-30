package com.jzs.common.launcher;

public interface ILauncherProvider {
	
	long generateNewId();
	void loadDefaultFavoritesIfNecessary(int origWorkspaceResId);
	
}
