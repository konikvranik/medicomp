package net.suteren.medicomp.plugin.temperature;

import java.sql.SQLException;
import java.text.DateFormat;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import net.suteren.medicomp.ui.adapter.RecordListAdapter;

public class TemperatureListAdapter extends RecordListAdapter {

	public TemperatureListAdapter(RecordListActivity recordListActivity,
			Person person) throws SQLException {
		super(recordListActivity, person);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflateView(R.layout.temperature_list_row, parent);
		}
		DateFormat df = android.text.format.DateFormat.getDateFormat(context);
		DateFormat tf = android.text.format.DateFormat.getTimeFormat(context);
		Record record = getItem(position);
		TextView dateField = (TextView) convertView.findViewById(R.id.time);
		dateField.setText(df.format(record.getTimestamp()) + " "
				+ tf.format(record.getTimestamp()));
		TextView valueField = (TextView) convertView.findViewById(R.id.value);
		TextView unitField = (TextView) convertView.findViewById(R.id.unit);
		for (Field<?> f : record.getFields()) {
			if (f.getType() == Type.TEMPERATURE) {
				Log.d(this.getClass().getCanonicalName(),"Temperature: " + f.getValue());
				valueField.setText(nf.format(f.getValue()));
				if (f.getUnit() != null)
					unitField.setText(f.getUnit().getUnit());
				break;
			}
		}
		return convertView;
	}

	@Override
	public void update() throws SQLException {
		if (recordDao == null)
			return;
		collection = recordDao.queryBuilder().where()
				.eq(Record.COLUMN_NAME_PERSON, person).and()
				.eq(Record.COLUMN_NAME_TYPE, Type.TEMPERATURE).query();
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}
}
