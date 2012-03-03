package net.suteren.medicomp.ui.widget;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public abstract class WidgetPreferenceActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String prefKey = getIntent().getStringExtra("preferences");
		Log.d(this.getClass().getCanonicalName(), "preferences: " + prefKey);
		PreferenceManager prefMgr = getPreferenceManager();
		prefMgr.setSharedPreferencesName(prefKey);
		prefMgr.setSharedPreferencesMode(MODE_PRIVATE);
		addPreferencesFromResource(getPreferencesResourceId());
	}

	protected abstract Integer getPreferencesResourceId();

}
