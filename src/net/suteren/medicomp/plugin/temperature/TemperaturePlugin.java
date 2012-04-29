package net.suteren.medicomp.plugin.temperature;

import java.util.Collection;
import java.util.Locale;

import net.suteren.medicomp.R;
import net.suteren.medicomp.format.RecordFormatter;
import net.suteren.medicomp.plugin.AbstractPlugin;
import net.suteren.medicomp.plugin.PluginActivity;
import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class TemperaturePlugin extends AbstractPlugin {

	public String getName() {
		return getString(R.string.temperature_plugin_name);
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
	public Widget newWidgetInstance(Context context) {
		return new TemperatureWidget(context, this);
	}

	public String getTitle() {
		return getString(R.string.temperature_plugin_title);
	}

	public String getSummary() {
		return getString(R.string.temperature_plugin_summary);
	}

	public Drawable getIcon() {
		return getDrawable(R.drawable.ic_menu_ic_menu_thermomether);
	}

	public PluginActivity newActivityInstance(Context context) {
		return new TemperaturePluginActivity(context);
	}

	@Override
	public Collection<RecordFormatter> getRecordFormatters() {
		Collection<RecordFormatter> result = super.getRecordFormatters();
		result.add(new TemperatureFormatter(getContext(), Locale.getDefault()));
		return result;
	}

}
