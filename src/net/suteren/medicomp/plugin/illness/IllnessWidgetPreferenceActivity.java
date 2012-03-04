package net.suteren.medicomp.plugin.illness;

import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.widget.WidgetPreferenceActivity;

public class IllnessWidgetPreferenceActivity extends
		WidgetPreferenceActivity {
	@Override
	protected Integer getPreferencesResourceId() {
		return R.xml.temperature_widget_preferences;
	}
}
