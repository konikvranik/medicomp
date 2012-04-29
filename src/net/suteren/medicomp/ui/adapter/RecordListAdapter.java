package net.suteren.medicomp.ui.adapter;

import java.sql.SQLException;
import java.text.DateFormat;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.record.Field;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

public class RecordListAdapter extends AbstractListAdapter<Record> {

	protected Dao<Record, Integer> recordDao;

	protected Person person;

	RecordListActivity recordListActivity;

	public RecordListAdapter(RecordListActivity recordListActivity,
			Person person) throws SQLException {
		super(recordListActivity);
		this.recordListActivity = recordListActivity;
		recordDao = MediCompDatabaseFactory.getInstance(recordListActivity)
				.createDao(Record.class);
		if (person == null)
			throw new NullPointerException("Person == null!");
		if (person.getId() < 1)
			throw new IllegalArgumentException("Person has no valid ID");
		this.person = person;
		update();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflateView(R.layout.record_list_row, parent);
		}
		DateFormat df = android.text.format.DateFormat.getDateFormat(context);
		DateFormat tf = android.text.format.DateFormat.getTimeFormat(context);
		Record record = getItem(position);
		TextView dateField = (TextView) convertView
				.findViewById(R.id.textView1);
		dateField.setText(df.format(record.getTimestamp()) + " "
				+ tf.format(record.getTimestamp()));
		TextView nameField = (TextView) convertView
				.findViewById(R.id.textView2);
		nameField.setText(record.getTitle());
		TextView valueField = (TextView) convertView
				.findViewById(R.id.textView3);
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Field<?> f : record.getFields()) {
			if (!first)
				sb.append(", ");
			first = false;
			try {
				sb.append(nf.format(f.getValue()));
			} catch (IllegalArgumentException e) {
				sb.append(df.format(f.getValue()));
			}
		}
		valueField.setText(sb.toString());
		return convertView;
	}

	@Override
	public void update() throws SQLException {
		if (recordDao == null)
			return;

		collection = recordDao.queryBuilder().orderBy("timestamp", false)
				.where().eq(Record.COLUMN_NAME_PERSON, person).query();
		recordDao.closeLastIterator();

	}

	@Override
	public Record getItemById(int id) {
		try {
			return recordDao.queryForId(id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				recordDao.closeLastIterator();
			} catch (SQLException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			}
		}
	}

}
