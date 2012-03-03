package net.suteren.medicomp.plugin.chart;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.AbstractPlugin;
import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;

public class ChartPlugin extends AbstractPlugin {

	public String getName() {
		return getName(R.string.chart_plugin_name);
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
}
