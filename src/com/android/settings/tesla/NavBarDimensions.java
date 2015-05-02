/*
 * Copyright (C) 2014 The Tesla OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.tesla;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.util.cm.ScreenType;

public class NavBarDimensions extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String LIST_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
    private static final String LIST_NAVIGATION_BAR_HEIGHT_LANDSCAPE = "navigation_bar_height_landscape";
    private static final String LIST_NAVIGATION_BAR_WIDTH = "navigation_bar_width";

    ListPreference mNavigationBarHeight;
    ListPreference mNavigationBarHeightLandcape;
    ListPreference mNavigationBarWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.navbar_dimensions_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        // Height
        mNavigationBarHeight = (ListPreference) findPreference(LIST_NAVIGATION_BAR_HEIGHT);
        mNavigationBarHeight.setOnPreferenceChangeListener(this);

        // Height Landscape
        mNavigationBarHeightLandcape = (ListPreference) findPreference(LIST_NAVIGATION_BAR_HEIGHT_LANDSCAPE);
        if (ScreenType.isPhone(getActivity())) {
            prefSet.removePreference(mNavigationBarHeightLandcape);
            mNavigationBarHeightLandcape = null;
        } else {
            mNavigationBarHeightLandcape.setOnPreferenceChangeListener(this);
        }
        // Width
        mNavigationBarWidth = (ListPreference) findPreference(LIST_NAVIGATION_BAR_WIDTH);
        if (!ScreenType.isPhone(getActivity())) {
            prefSet.removePreference(mNavigationBarWidth);
            mNavigationBarWidth = null;
        } else {
            mNavigationBarWidth.setOnPreferenceChangeListener(this);
        }

        updateDimension();
    }

    private void updateDimension() {
        int navigationBarHeight = Settings.TESLA.getInt(getContentResolver(),
                Settings.TESLA.NAVIGATION_BAR_HEIGHT, -2);
        if (navigationBarHeight == -2) {
            navigationBarHeight =
                    (int) (getResources().getDimension(com.android.internal.R.dimen.navigation_bar_height)
                    / getResources().getDisplayMetrics().density);
        }
        mNavigationBarHeight.setValue(String.valueOf(navigationBarHeight));

        if (mNavigationBarHeightLandcape == null) {
            return;
        }
        int navigationBarHeightLandcape = Settings.TESLA.getInt(getContentResolver(),
                Settings.TESLA.NAVIGATION_BAR_HEIGHT_LANDSCAPE, -2);
        if (navigationBarHeightLandcape == -2) {
            navigationBarHeightLandcape =
                    (int) (getResources().getDimension(com.android.internal.R.dimen.navigation_bar_height_landscape)
                    / getResources().getDisplayMetrics().density);
        }
        mNavigationBarHeightLandcape.setValue(String.valueOf(navigationBarHeightLandcape));

        if (mNavigationBarWidth == null) {
            return;
        }
        int navigationBarWidth = Settings.TESLA.getInt(getContentResolver(),
                Settings.TESLA.NAVIGATION_BAR_WIDTH, -2);
        if (navigationBarWidth == -2) {
            navigationBarWidth =
                    (int) (getResources().getDimension(com.android.internal.R.dimen.navigation_bar_width)
                    / getResources().getDisplayMetrics().density);
        }
        mNavigationBarWidth.setValue(String.valueOf(navigationBarWidth));

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNavigationBarHeight) {
            String newVal = (String) newValue;
            Settings.TESLA.putInt(getContentResolver(),
                    Settings.TESLA.NAVIGATION_BAR_HEIGHT,
                    Integer.parseInt(newVal));
            return true;
        } else if (preference == mNavigationBarHeightLandcape) {
            String newVal = (String) newValue;
            Settings.TESLA.putInt(getContentResolver(),
                    Settings.TESLA.NAVIGATION_BAR_HEIGHT_LANDSCAPE,
                    Integer.parseInt(newVal));
            return true;
        } else if (preference == mNavigationBarWidth) {
            String newVal = (String) newValue;
            Settings.TESLA.putInt(getContentResolver(),
                    Settings.TESLA.NAVIGATION_BAR_WIDTH,
                    Integer.parseInt(newVal));
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDimension();
    }

}
