package net.suteren.medicomp.plugin.temperature;

import java.sql.SQLException;
import java.text.DateFormat;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.enums.Type;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import net.suteren.medicomp.ui.adapter.RecordListAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.CloseableIterable;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

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
		CloseableIterator<Field> fields = ((CloseableIterable) record
				.getFields()).closeableIterator();
		while (fields.hasNext()) {
			Field f = fields.next();
			if (f.getType() == Type.TEMPERATURE) {
				Log.d(this.getClass().getCanonicalName(),
						"Temperature: " + f.getValue());
				valueField.setText(nf.format(f.getValue()));
				if (f.getUnit() != null)
					unitField.setText(f.getUnit().getUnit());
				break;
			}
		}
		try {
			fields.close();
		} catch (SQLException e) {
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
		}
		return convertView;
	}

	@Override
	public void update() throws SQLException {

		if (recordDao == null)
			return;

		collection = recordDao.queryBuilder().orderBy("timestamp", false)
				.where().eq(Record.COLUMN_NAME_PERSON, person).and()
				.eq(Record.COLUMN_NAME_TYPE, Type.TEMPERATURE).query();
		recordDao.closeLastIterator();
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
