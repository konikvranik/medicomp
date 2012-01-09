package net.suteren.medicomp.ui.widget;

import static net.suteren.medicomp.ui.activity.MedicompActivity.LOG_TAG;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Category;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;

import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class TemperatureGraphWidget extends AbstractWidget implements Widget {

	public TemperatureGraphWidget(Context context, Person person) {
		super(context, person);
	}

	@Override
	public View getView(View convertView, ViewGroup parent) {

		if (convertView == null)
			convertView = layoutInflater.inflate(
					R.layout.dashboard_temperature_graph, parent, false);

		Log.d(LOG_TAG,
				"TemperatureGraph Type: "
						+ (((RelativeLayout) convertView).getId() == R.id.temperatureGraphWidget));
		Log.d(LOG_TAG,"Person in TemperatureGraphWidget: "+person.getId());
		
		Collection<Record> rs = getRecords();
		if (rs != null && rs.size() > 1) {

			FrameLayout rl = (FrameLayout) convertView
					.findViewById(R.id.frameLayout1);

			XYSeries xyseries = new XYSeries("Temp");

			Iterator<Record> it = rs.iterator();
			for (int i = 0; i < rs.size() && it.hasNext(); i++) {
				Double t = null;
				Record record = it.next();
				Iterator<Field> fit = record.getFields().iterator();
				while (fit.hasNext()) {
					Field f = fit.next();
					if (f.getType() == Type.TEMPERATURE) {
						t = (Double) f.getValue();
						break;
					}
				}
				if (t != null && record.getTimestamp() != null) {
					xyseries.add(record.getTimestamp().getTime(), t);

				}

			}


			// -------------------

			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			dataset.addSeries(xyseries);

			XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

			renderer.setAntialiasing(true);

			XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
//			seriesRenderer.setFillPoints(true);
//			seriesRenderer.setDisplayChartValues(true);
			seriesRenderer.setGradientEnabled(true);
			seriesRenderer.setGradientStart(35, Color.rgb(0, 255, 255));
			seriesRenderer.setGradientStop(39, Color.rgb(255, 0, 0));
//			seriesRenderer.setFillBelowLineColor(Color.argb(200, 255, 255, 0));
//			seriesRenderer.setFillBelowLine(false);
			seriesRenderer.setColor(Color.argb(200, 255, 255, 0));
			seriesRenderer.setLineWidth(6);
			BasicStroke basicstroke = new BasicStroke(Cap.ROUND, Join.ROUND,
					6f, new float[] { 6f, 0f }, .6f);
			seriesRenderer.setStroke(basicstroke);

			renderer.addSeriesRenderer(seriesRenderer);

			AbstractChart chart = new TimeChart(dataset, renderer);
			GraphicalView gv = new GraphicalView(context, chart);

			rl.removeAllViews();
			rl.addView(gv);
			// rl.addView(graph);
		}

		// TODO Auto-generated method stub
		return convertView;
	}

	private Collection<Record> getRecords() {
		SortedSet<Record> rs = new TreeSet<Record>(new Comparator<Record>() {

			@Override
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
}
