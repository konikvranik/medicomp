package net.suteren.medicomp.ui.widget.temperature;

import static net.suteren.medicomp.ui.activity.MedicompActivity.LOG_TAG;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.suteren.medicomp.FieldFormatter;
import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Category;
import net.suteren.medicomp.domain.DoubleValue;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import net.suteren.medicomp.ui.widget.AbstractWidget;
import net.suteren.medicomp.ui.widget.Widget;
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
		}

		temp = (TextView) convertView.findViewById(R.id.textView2);
		Log.d(LOG_TAG,
				"TemperatureWidget Type: "
						+ (((RelativeLayout) convertView).getId() == R.id.temperatureWidget));
		Log.d(LOG_TAG, "Person in TemperatureWidget: " + person.getId());
		Collection<Record> rs = person.getRecords();
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
			temp.setText(ff == null ? "--,-" : ff.getValue());
			double min = 36.3;
			double high = 37;
			double max = 38;
			int red = 0;
			int green = 0;
			int blue = 0;

			if (val <= min) {
				blue = 255;
				green = 128;
			} else if (val < high) {
				green = (255);
			} else if (val < max) {
				red = (255);
				green = 200;
			} else {
				red = 255;
				green = 70;
			}

			temp.setTextColor(Color.rgb(red, green, blue));

		}
		return convertView;
	}

	@Override
	public int getId() {
		return 3;
	}

	@Override
	public boolean onClick(View view, long position, long id) {
		context.startActivity(new Intent(context, RecordListActivity.class));
		return true;
	}

}
