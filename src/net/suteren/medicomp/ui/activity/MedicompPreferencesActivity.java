package net.suteren.medicomp.ui.activity;

import java.util.Arrays;

import net.suteren.medicomp.R;
import net.suteren.medicomp.enums.Quantity;
import net.suteren.medicomp.enums.Unit;
import net.suteren.medicomp.plugin.MediCompPluginManager;
import net.suteren.medicomp.plugin.Plugin;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

public class MedicompPreferencesActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager prefMgr = getPreferenceManager();
		prefMgr.setSharedPreferencesName(MedicompActivity.MEDICOMP_PREFS);
		prefMgr.setSharedPreferencesMode(MODE_PRIVATE);

		addPreferencesFromResource(R.xml.medicomp_preferences);
		setupPlugins((PreferenceScreen) findPreference("plugins"),
				MediCompPluginManager.getInstance(this));
		setupUnits((PreferenceScreen) findPreference("units"));

	}

	private void setupUnits(PreferenceScreen unitPreferenceScreen) {

		setupUnit(
				(ListPreference) unitPreferenceScreen
						.findPreference("temperature_unit"),
				Quantity.TEMPERATURE, Unit.CELSIUS.name());

		setupUnit(
				(ListPreference) unitPreferenceScreen
						.findPreference("length_unit"),
				Quantity.LENGTH, Unit.CENTIMETER.name());

		setupUnit(
				(ListPreference) unitPreferenceScreen
						.findPreference("mass_unit"),
				Quantity.MASS, Unit.KILOGRAM.name());

	}

	private void setupUnit(ListPreference temp, Quantity quantity, String def) {
		Unit[] units = quantity.getUnits();
		String[] values = new String[units.length];
		String[] titles = new String[units.length];

		for (int i = 0; i < units.length; i++) {
			titles[i] = units[i].getUnit();
			values[i] = units[i].name();
			Log.d(this.getClass().getCanonicalName(), "Unit " + i + ": "
					+ units[i] + " ... " + values[i] + " ... " + titles[i]);
		}

		temp.setEntryValues(values);
		temp.setEntries(titles);
		temp.setDefaultValue(def);

		Log.d(this.getClass().getCanonicalName(),
				"Entries: " + Arrays.toString(temp.getEntries()) + " ... "
						+ Arrays.toString(temp.getEntryValues()));
	}

	private void setupPlugins(PreferenceScreen pref,
			final MediCompPluginManager pm) {
		for (final Plugin plugin : pm.getRegisteredPlugins()) {
			CheckBoxPreference p = new CheckBoxPreference(this);
			p.setTitle(plugin.getName());
			p.setChecked(pm.isActive(plugin));
			p.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				public boolean onPreferenceChange(Preference preference,
						Object obj) {
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
