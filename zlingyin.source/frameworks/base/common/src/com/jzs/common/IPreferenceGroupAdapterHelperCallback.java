package com.jzs.common;

import java.util.List;

import android.preference.Preference;

/**
 * {@hide}
 */
public interface IPreferenceGroupAdapterHelperCallback {

	void flattenJzsPreferenceGroup(List<Preference>preferences, Preference preference);

}
