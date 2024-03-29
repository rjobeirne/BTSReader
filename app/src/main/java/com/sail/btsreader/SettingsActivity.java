package com.sail.btsreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // below line is used to check if
        // frame layout is empty or not.
        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // below line is to inflate our fragment.
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.idFrameLayout, new SettingsFragment())
                    .commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
             setPreferencesFromResource(R.xml.preferences, rootKey);

            androidx.preference.EditTextPreference editTextPreference1 =
                    getPreferenceManager().findPreference("prefs_listen_time");
            androidx.preference.EditTextPreference editTextPreference2 =
                    getPreferenceManager().findPreference("prefs_chapter_length");

            editTextPreference1.setOnBindEditTextListener(
                    new androidx.preference.EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });

            editTextPreference2.setOnBindEditTextListener(
                    new androidx.preference.EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });

            Preference button = findPreference(getString(R.string.restore_defaults));
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    button.setIcon(R.drawable.restore_grey);
                    restoreDefaultSettings();
                    return false;
                }
            });
        }

        private void restoreDefaultSettings() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, true);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }
    }
}
