package net.suteren.medicomp.plugin.temperature;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.AbstractPlugin;
import net.suteren.medicomp.plugin.PluginManager;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.content.Intent;

public class TemperaturePlugin extends AbstractPlugin {

	public String getName() {
		return getName(R.string.temperature_plugin_name);
	}

	@Override
	public boolean hasActivity() {
		return true;
	}

	@Override
	public boolean hasWidget() {
		return true;
	}

	@Override
	public Intent newActivityInstance(Context context) {
		// TODO - replace by TemperatureListActivity
		return new Intent(context, RecordListActivity.class);
	}

	@Override
	public Widget newWidgetInstance(Context context) {
		return new TemperatureWidget(context);
	}

}
