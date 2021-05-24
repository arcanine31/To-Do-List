package com.example.to_do_list

import android.os.Bundle
import androidx.preference.*

// Fragment to display when Setting is selected from the mainActivity

class SettingsFragment : PreferenceFragmentCompat()
{
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preference, rootKey)
    }

}
