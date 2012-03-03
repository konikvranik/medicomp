package net.suteren.medicomp.plugin.temperature;

import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.widget.WidgetPreferenceActivity;

public class TemperatureWidgetPreferenceActivity extends
		WidgetPreferenceActivity {
	@Override
	protected Integer getPreferencesResourceId() {
		return R.xml.temperature_widget_preferences;
	}
}
