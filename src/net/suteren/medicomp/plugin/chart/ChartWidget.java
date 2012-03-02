package net.suteren.medicomp.plugin.chart;

import static net.suteren.medicomp.ui.activity.MedicompActivity.LOG_TAG;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Category;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import net.suteren.medicomp.ui.widget.AbstractWidget;
import net.suteren.medicomp.ui.widget.Widget;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class ChartWidget extends AbstractWidget implements Widget {

	private static final String CHART_TEMPERATURE_PERIOD = "chartTemperaturePeriod";
	private static final String CHART_TEMPERATURE_COLOR = "chartTemperatureColor";
	private static final int GRAPH_PERIOD = 2;
	private SharedPreferences preferences;

	public ChartWidget(Context context, Person person) {
		super(context, person);

		preferences = PreferenceManager.getDefaultSharedPreferences(context);

	}

	public View getView(View convertView, ViewGroup parent) {

		if (convertView == null)
			convertView = layoutInflater.inflate(
					R.layout.dashboard_temperature_graph, parent, false);

		Log.d(LOG_TAG,
				"TemperatureGraph Type: "
						+ (((RelativeLayout) convertView).getId() == R.id.temperatureGraphWidget));
		Log.d(LOG_TAG, "Person in TemperatureGraphWidget: " + person.getId());

		Collection<Record> rs = getRecords();
		if (rs != null && rs.size() > 1) {

			FrameLayout rl = (FrameLayout) convertView
					.findViewById(R.id.frameLayout1);

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
				int period = Math.round(preferences.getFloat(
						CHART_TEMPERATURE_PERIOD, GRAPH_PERIOD));
				if (period > 0) {
					range[0] = new Date().getTime() - period * 24 * 3600 * 1000;
					range[1] = new Date().getTime();
				}
			} catch (Exception e) {
				preferences.edit().remove(CHART_TEMPERATURE_PERIOD).commit();
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
				.setColor(preferences
						.getInt(CHART_TEMPERATURE_COLOR, context.getResources()
								.getColor(R.color.chartTemperatureColor)));
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
			Iterator<Field> fit = record.getFields().iterator();
			while (fit.hasNext()) {
				Field<?> f = fit.next();
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
		for (Record r : person.getRecords()) {
			if (r.getType() == Type.TEMPERATURE
					&& r.getCategory() == Category.MEASURE)
				rs.add(r);
		}
		return rs;
	}

	public int getId() {
		return 2;
	}

	@Override
	public boolean showPreferencesPane() {
		context.startActivity(new Intent(context,
				ChartWidgetPreferenceActivity.class));
		return true;
	}

	public String getName() {
		return getName(R.string.chart_widget_name);
	}

}
