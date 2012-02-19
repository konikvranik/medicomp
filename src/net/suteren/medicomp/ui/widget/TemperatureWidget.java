package net.suteren.medicomp.ui.widget;

import static net.suteren.medicomp.ui.activity.MedicompActivity.LOG_TAG;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Category;
import net.suteren.medicomp.domain.DoubleValue;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TemperatureWidget extends AbstractWidget implements Widget {

	private TextView temp;

	public TemperatureWidget(Context context, Person person) {
		super(context, person);
	}

	@Override
	public View getView(View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.dashboard_temperature, parent, false);
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					context.startActivity(new Intent(context,
							RecordListActivity.class));

					// TODO Auto-generated method stub
				}
			});
		}

		temp = (TextView) convertView.findViewById(R.id.textView2);
		Log.d(LOG_TAG,
				"TemperatureWidget Type: "
						+ (((RelativeLayout) convertView).getId() == R.id.temperatureWidget));
		Log.d(LOG_TAG, "Person in TemperatureWidget: " + person.getId());
		Collection<Record> rs = person.getRecords();
		NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
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

		if (r != null) {
			Collection<Field> fs = r.getFields();
			Iterator<Field> fi = fs.iterator();
			while (fi.hasNext()) {
				Field<?> f = fi.next();
				if (f.getType() == Type.TEMPERATURE) {
					val = (Double) f.getValue();
					break;
				}
			}
			try {
				List<DoubleValue> dv = MediCompDatabaseFactory.getInstance()
						.createDao(DoubleValue.class).queryForAll();
			} catch (SQLException e) {
				Log.e(LOG_TAG, e.getMessage(), e);
			}

		}

		if (val != null) {
			temp.setText(nf.format(val));
			double min = 30;
			double max = 38;
			double optimal = 36.6;
			int red = 0;
			int green = 0;
			int blue = 0;

			if (val <= min) {
				blue = 255;
			} else if (val <= optimal) {
				green = (int) ((val - min) / (optimal - min) * 255);
				blue = 256 - green;
			} else if (val <= max) {
				red = (int) ((val - optimal) / (max - optimal) * 255);
				green = 256 - red;
			} else {
				red = 255;
			}

			temp.setTextColor(Color.rgb(red, green, blue));

		}
		return convertView;
	}
}
