package net.suteren.medicomp.ui.adapter;

import static net.suteren.medicomp.ui.activity.MedicompActivity.LOG_TAG;

import java.sql.SQLException;
import java.text.DateFormat;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

public class RecordListAdapter extends AbstractListAdapter<Record> {

	private Dao<Record, Integer> recordDao;

	private Person person;

	RecordListActivity recordListActivity;

	public RecordListAdapter(RecordListActivity recordListActivity,
			Person person) throws SQLException {
		super(recordListActivity);
		this.recordListActivity = recordListActivity;
		recordDao = MediCompDatabaseFactory.getInstance(recordListActivity)
				.createDao(Record.class);
		Log.d(LOG_TAG, "REcordListAdapter.............");
		if (person == null)
			throw new NullPointerException("Person == null!");
		if (person.getId() < 1)
			throw new IllegalArgumentException("Person has no valid ID");
		Log.d(LOG_TAG, "RecordDao: " + recordDao);
		this.person = person;
		update();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.record_list_row,
					parent, false);
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
			sb.append(nf.format(f.getValue()));
		}
		valueField.setText(sb.toString());
		return convertView;
	}

	@Override
	public void update() throws SQLException {
		if (recordDao == null)
			return;
		collection = recordDao.queryBuilder().where()
				.eq(Record.COLUMN_NAME_PERSON, person).query();
		Log.d(LOG_TAG, "RecordListAdapter: " + collection.size());

	}

	@Override
	public Record getItemById(int id) throws Exception {
		return recordDao.queryForId(id);
	}

}
