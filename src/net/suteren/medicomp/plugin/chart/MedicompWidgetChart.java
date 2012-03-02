package net.suteren.medicomp.plugin.chart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.achartengine.chart.CubicLineChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Paint;

public class MedicompWidgetChart extends CubicLineChart {

	private static final long serialVersionUID = -4145688213648261036L;

	public static final String TYPE = "Time";
	public static final long DAY = 86400000L;
	private String mDateFormat;

	public MedicompWidgetChart() {
		// TODO Auto-generated constructor stub
	}

	MedicompWidgetChart(XYMultipleSeriesDataset xymultipleseriesdataset,
			XYMultipleSeriesRenderer xymultipleseriesrenderer) {
		super(xymultipleseriesdataset, xymultipleseriesrenderer, .7f);
	}

	public String getDateFormat() {
		return mDateFormat;
	}

	public void setDateFormat(String s) {
		mDateFormat = s;
	}

	protected void drawXLabels(List list, Double adouble[], Canvas canvas,
			Paint paint, int i, int j, int k, double d, double d1, double d2) {
		int l = list.size();
		if (l > 0) {
			boolean flag = mRenderer.isShowLabels();
			boolean flag1 = mRenderer.isShowGrid();
			DateFormat dateformat = getDateFormat(
					((Double) list.get(0)).doubleValue(),
					((Double) list.get(l - 1)).doubleValue());
			for (int i1 = 0; i1 < l; i1++) {
				long l1 = Math.round(((Double) list.get(i1)).doubleValue());
				float f = (float) ((double) i + d * ((double) l1 - d1));
				if (flag) {
					paint.setColor(mRenderer.getLabelsColor());
					canvas.drawLine(f, k, f,
							(float) k + mRenderer.getLabelsTextSize() / 3F,
							paint);
					drawText(canvas, dateformat.format(new Date(l1)), f,
							(float) k + (mRenderer.getLabelsTextSize() * 4F)
									/ 3F, paint, mRenderer.getXLabelsAngle());
				}
				if (flag1) {
					paint.setColor(mRenderer.getGridColor());
					canvas.drawLine(f, k, f, j, paint);
				}
			}

		}
		drawXTextLabels(adouble, canvas, paint, true, i, j, k, d, d1, d2);
	}

	private DateFormat getDateFormat(double d, double d1) {
		if (mDateFormat != null) {
			SimpleDateFormat simpledateformat = new SimpleDateFormat(
					mDateFormat);
			return simpledateformat;
		} else {
			DateFormat dateformat = SimpleDateFormat.getDateInstance(2,
					Locale.getDefault());
			double d2 = d1 - d;
			if (d2 > 86400000D && d2 < 432000000D)
				dateformat = SimpleDateFormat.getDateTimeInstance(3, 3,
						Locale.getDefault());
			else if (d2 < 86400000D)
				dateformat = SimpleDateFormat.getTimeInstance(2,
						Locale.getDefault());
			return dateformat;
		}
	}

	public String getChartType() {
		return "MedicompWidget";
	}
}
