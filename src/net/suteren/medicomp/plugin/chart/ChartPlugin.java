package net.suteren.medicomp.plugin.chart;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.AbstractPlugin;
import net.suteren.medicomp.plugin.PluginActivity;
import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class ChartPlugin extends AbstractPlugin {

	public String getName() {
		return getString(R.string.chart_plugin_name);
	}

	@Override
	public boolean hasActivity() {
		return false;
	}

	@Override
	public boolean hasWidget() {
		return true;
	}

	@Override
	public Widget newWidgetInstance(Context context) {
		return new ChartWidget(context, this);
	}

	public String getTitle() {
		return getString(R.string.chart_plugin_title);
	}

	public String getSummary() {
		return getString(R.string.chart_plugin_summary);
	}

	public Drawable getIcon() {
		return getDrawable(R.drawable.ic_menu_ic_menu_cardiograph);
	}

	public PluginActivity newActivityInstance(Context context) {
		return new ChartPluginActivity(context);
	}
}
