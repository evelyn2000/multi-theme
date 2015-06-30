package com.jzs.common;

import java.util.List;

import android.preference.Preference;

/**
 * {@hide}
 */
public interface IPreferenceGroupAdapterHelper extends IJzsHelper {
	
	void flattenPreferenceGroup(List<Preference> preferences, IPreferenceGroupHelper group);
	boolean checkJzsIsIgnoreItemType(Preference preference);
}
