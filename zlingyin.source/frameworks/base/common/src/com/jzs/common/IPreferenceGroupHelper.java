package com.jzs.common;

import java.util.List;

import android.preference.Preference;
import android.preference.PreferenceGroup;

/**
 * {@hide}
 */
public interface IPreferenceGroupHelper extends IPreferenceHelper {

	PreferenceGroup getPreferenceGroup();
	void initAutoInsertFirstAndLast(boolean flag);
	//boolean isAutoInsertFirstAndLastSeperater();
	int getPreferenceCount(List<Preference> list);
	Preference getPreference(int index, List<Preference> list);
	boolean checkJzsIsCanRemove(Preference preference);
	int getFirstValidIndex();
	int getRealPreferenceCount(List<Preference> list);
	Preference getRealPreference(int index, List<Preference> list);
	void autoInsertFirstAndLastSeperater(List<Preference> list);
	
}
