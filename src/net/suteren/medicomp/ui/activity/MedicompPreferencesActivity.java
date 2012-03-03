package net.suteren.medicomp.ui.activity;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.Plugin;
import net.suteren.medicomp.plugin.PluginManagerMediCompImpl;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

public class MedicompPreferencesActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.medicomp_preferences);
		PreferenceScreen pref = (PreferenceScreen) findPreference("plugins");
		final PluginManagerMediCompImpl pm = PluginManagerMediCompImpl
				.getInstance(this);
		for (final Plugin plugin : pm.getRegisteredPlugins()) {
			CheckBoxPreference p = new CheckBoxPreference(this);
			p.setTitle(plugin.getName());
			p.setChecked(plugin.isActive());
			p.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				public boolean onPreferenceChange(Preference preference,
						Object obj) {
					Log.d(this.getClass().getCanonicalName(), "obj: "
							+ obj.getClass().getCanonicalName() + " " + obj);
					if ((Boolean) obj) {
						pm.activatePlugin(plugin);
					} else {
						pm.deactivatePlugin(plugin);
					}
					return true;
				}
			});
			pref.addPreference(p);
		}

	}

}
