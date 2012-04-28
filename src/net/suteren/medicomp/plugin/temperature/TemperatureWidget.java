package net.suteren.medicomp.plugin.temperature;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import net.suteren.medicomp.FieldFormatter;
import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.enums.Category;
import net.suteren.medicomp.enums.Type;
import net.suteren.medicomp.plugin.Plugin;
import net.suteren.medicomp.ui.widget.AbstractWidget;
import net.suteren.medicomp.ui.widget.PluginWidget;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.CloseableIterator;

public class TemperatureWidget extends AbstractWidget implements PluginWidget {

	private TextView temp;
	private Plugin plugin;
	private TextView unit;

	public TemperatureWidget(Context context) {
		super(context);
	}

	public TemperatureWidget(Context context, Plugin plugin) {
		super(context);
		this.plugin = plugin;
	}

	public View getView(View convertView, ViewGroup parent) {
		if (convertView == null || true) {
			convertView = layoutInflater.inflate(
					R.layout.dashboard_temperature, parent, false);
			TextView title = (TextView) convertView
					.findViewById(R.id.widgetTitle);
			title.setText(getTitle());
		}

		temp = (TextView) convertView.findViewById(R.id.value);
		unit = (TextView) convertView.findViewById(R.id.unit);
		Collection<Record> rs = getPerson().getRecords();
		Double val = null;

		Iterator<Record> ri = rs.iterator();
		Record r = null;
		while (ri.hasNext()) {
			Record rx = ri.next();

			if (rx.getType() == Type.TEMPERATURE
					&& rx.getCategory() == Category.MEASURE
					&& (r == null || r.getTimestamp().compareTo(
							rx.getTimestamp()) < 0))
				r = rx;
		}

		FieldFormatter ff = null;
		if (r != null) {
			Collection<Field> fs = r.getFields();
			Iterator<Field> fi = fs.iterator();
			while (fi.hasNext()) {
				Field<?> f = fi.next();
				if (f.getType() == Type.TEMPERATURE) {
					ff = new FieldFormatter(f);
					val = (Double) f.getValue();
					unit.setText(f.getUnit() == null ? "" : f.getUnit()
							.getUnit());
					break;
				}
			}
			if (fi instanceof CloseableIterator)
				try {
					((CloseableIterator) fi).close();
				} catch (SQLException e) {
					Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
				}
		}

		if (val != null) {
			temp.setText(ff == null ? "--,-" : ff.getValue());
			double min = getWidgetPreferences().getFloat(
					"lowerTemperatureBound", 35);
			double high = getWidgetPreferences().getFloat(
					"higherTemperatureBound", 37);
			double max = getWidgetPreferences().getFloat(
					"upperTemperatureBound", 38);

			if (val <= min) {
				temp.setTextColor(getWidgetPreferences().getInt(
						"hypothermiaColor",
						context.getResources().getColor(
								R.color.hypothermiaColor)));
			} else if (val < high) {
				temp.setTextColor(getWidgetPreferences().getInt(
						"rightTemperatureColor",
						context.getResources().getColor(
								R.color.rightTemperatureColor)));
			} else if (val < max) {
				temp.setTextColor(getWidgetPreferences().getInt("heatColor",
						context.getResources().getColor(R.color.heatColor)));
			} else {
				temp.setTextColor(getWidgetPreferences().getInt("feverColor",
						context.getResources().getColor(R.color.feverColor)));
			}

		}

		return convertView;
	}

	@Override
	public boolean onClick(View view, long position, long id) {
		context.startActivity(new Intent(context, TemperatureListActivity.class));
		return true;
	}

	@Override
	protected Class<? extends PreferenceActivity> getPreferenceActivityClass() {
		return TemperatureWidgetPreferenceActivity.class;
	}

	public String getName() {
		return getString(R.string.temperature_widget_name);
	}

	public int getType() {
		return 2;
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public String getTitle() {
		return getString(R.string.temperature_widget_title);
	}

	public String getSummary() {
		return getString(R.string.temperature_widget_summary);
	}

	public Drawable getIcon() {
		return getDrawable(R.drawable.ic_menu_ic_menu_thermomether);
	}

}
