package com.blogspot.zongjia.pttmarvel;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

public class MySettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        SeekBarPreference contentFontSizePref = (SeekBarPreference) findPreference("contentFontSize");
        contentFontSizePref.setShowSeekBarValue(true);

    }


}
