package net.suteren.medicomp.plugin.chart;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.enums.Category;
import net.suteren.medicomp.enums.Type;
import net.suteren.medicomp.plugin.Plugin;
import net.suteren.medicomp.ui.widget.AbstractWidget;
import net.suteren.medicomp.ui.widget.PluginWidget;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ChartWidget extends AbstractWidget implements PluginWidget {

	private static final String CHART_TEMPERATURE_PERIOD = "chartTemperaturePeriod";
	private static final String CHART_TEMPERATURE_COLOR = "chartTemperatureColor";
	private static final int GRAPH_PERIOD = 2;
	private Plugin plugin;

	public ChartWidget(Context context) {
		super(context);
	}

	public ChartWidget(Context context, Plugin plugin) {
		super(context);
		this.plugin = plugin;
	}

	public View getView(View convertView, ViewGroup parent) {

		if (convertView == null || true) {
			convertView = layoutInflater.inflate(
					R.layout.dashboard_temperature_graph, parent, false);
			TextView title = (TextView) convertView
					.findViewById(R.id.widgetTitle);
			title.setText(getTitle());
		}

		Collection<Record> rs = getRecords();
		if (rs != null && rs.size() > 1) {

			FrameLayout rl = (FrameLayout) convertView.findViewById(R.id.chart);

			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			TimeSeries temperatureSeries = makeSeries(rs);
			dataset.addSeries(temperatureSeries);

			XYMultipleSeriesRenderer renderer = makeMultipleRenderer();
			renderer.addSeriesRenderer(makeTemperatureRenderer());

			TimeChart chart = new TimeChart(dataset, renderer);
			// MedicompWidgetChart chart = new MedicompWidgetChart(dataset,
			// renderer);

			double diff = temperatureSeries.getMaxY()
					- temperatureSeries.getMinY();
			diff = diff * .1;
			double[] range = new double[] { temperatureSeries.getMinX(),
					temperatureSeries.getMaxX(), temperatureSeries.getMinY(),
					temperatureSeries.getMaxY() + diff };
			try {
				int period = Math.round(getWidgetPreferences().getFloat(
						CHART_TEMPERATURE_PERIOD, GRAPH_PERIOD));
				if (period > 0) {
					range[0] = new Date().getTime() - period * 24 * 3600 * 1000;
					range[1] = new Date().getTime();
				}
			} catch (Exception e) {
				getWidgetPreferences().edit().remove(CHART_TEMPERATURE_PERIOD)
						.commit();
			}
			renderer.setInitialRange(range);
			renderer.setRange(range);
			chart.setCalcRange(range, 1);

			rl.removeAllViews();

			rl.addView(new GraphicalView(context, chart));
		}

		return convertView;
	}

	private XYMultipleSeriesRenderer makeMultipleRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAntialiasing(true);
		// renderer.setApplyBackgroundColor(true);
		// renderer.setBackgroundColor(Color.TRANSPARENT);
		renderer.setClickEnabled(true);
		renderer.setExternalZoomEnabled(false);
		// renderer.setInScroll(true);
		renderer.setPanEnabled(true);
		renderer.setShowGrid(true);
		return renderer;
	}

	private XYSeriesRenderer makeTemperatureRenderer() {
		XYSeriesRenderer temperatureRenderer = new XYSeriesRenderer();
		temperatureRenderer
				.setColor(getWidgetPreferences().getInt(
						CHART_TEMPERATURE_COLOR,
						context.getResources().getColor(
								R.color.chartTemperatureColor)));
		temperatureRenderer.setGradientEnabled(true);
		temperatureRenderer.setGradientStart(35.9, Color.CYAN);
		temperatureRenderer.setGradientStop(40, Color.RED);
		temperatureRenderer.setDisplayChartValues(true);
		temperatureRenderer.setPointStyle(PointStyle.TRIANGLE);
		temperatureRenderer.setFillPoints(true);
		temperatureRenderer.setLineWidth(4);
		temperatureRenderer.setChartValuesTextSize(15f);
		temperatureRenderer.setChartValuesTextAlign(Align.RIGHT);

		return temperatureRenderer;
	}

	private TimeSeries makeSeries(Collection<Record> rs) {
		TimeSeries xyseries = new TimeSeries(context.getResources().getString(
				R.string.temperature_legend));

		Iterator<Record> it = rs.iterator();
		for (int i = 0; i < rs.size() && it.hasNext(); i++) {
			Double t = null;
			Record record = it.next();
			@SuppressWarnings("rawtypes")
			Iterator<Field> fit = record.getFields().iterator();
			while (fit.hasNext()) {
				@SuppressWarnings("rawtypes")
				Field f = fit.next();
				if (f.getType() == Type.TEMPERATURE) {
					t = (Double) f.getValue();
					break;
				}
			}
			if (t != null) {
				xyseries.add(record.getTimestamp(), t);

			}

		}
		return xyseries;
	}

	private Collection<Record> getRecords() {
		SortedSet<Record> rs = new TreeSet<Record>(new Comparator<Record>() {

			public int compare(Record lhs, Record rhs) {
				if (lhs != null && rhs != null)
					return lhs.getTimestamp().compareTo(rhs.getTimestamp());

				if (lhs == null && rhs == null)
					return 0;

				if (lhs == null)
					return -1;

				return 1;
			}
		});
		for (Record r : getPerson().getRecords()) {
			if (r.getType() == Type.TEMPERATURE
					&& r.getCategory() == Category.MEASURE)
				rs.add(r);
		}
		return rs;
	}

	protected Class<ChartWidgetPreferenceActivity> getPreferenceActivityClass() {
		return ChartWidgetPreferenceActivity.class;
	}

	public String getName() {
		return getString(R.string.chart_widget_name);
	}

	public int getType() {
		return 3;
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public String getTitle() {
		return getString(R.string.chart_widget_title);
	}

	public String getSummary() {
		return getString(R.string.chart_widget_summary);
	}

	public Drawable getIcon() {
		return getDrawable(R.drawable.ic_menu_ic_menu_cardiograph);
	}

}
