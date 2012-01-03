package net.suteren.medicomp.ui;

import static net.suteren.medicomp.MediCompActivity.LOG_TAG;

import java.sql.SQLException;
import java.util.List;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import android.content.Context;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

public class PersonAdapter implements ListAdapter {

	SQLiteDatabase database = null;
	private Dao<Person, Integer> personDao;
	private Context context;

	public PersonAdapter(Context context) throws SQLException {
		this.context = context;
		MediCompDatabaseFactory dbf = MediCompDatabaseFactory
				.getInstance(context);

		personDao = dbf.createDao(Person.class);
	}

	@Override
	public int getCount() {
		try {
			return (int) personDao.countOf();
		} catch (Exception e) {
			Log.e(LOG_TAG, "Failed: ", e);
			return -1;
		}
	}

	@Override
	public Person getItem(int position) {
		List<Person> persons = null;
		try {
			persons = personDao.queryForAll();
			return persons.get(position);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Failed: ", e);
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		List<Person> persons = null;
		try {
			persons = personDao.queryForAll();
			return persons.get(position).getId();
		} catch (Exception e) {
			Log.e(LOG_TAG, "Failed: ", e);
			return -1;
		}
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// convertView = View.inflate(context, R.layout.person_list_row,
			// parent);
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.person_list_row, parent, false);
		}
		Person person = getItem(position);
		if (person != null) {
			((TextView) convertView.findViewById(R.id.name)).setText(person
					.getName());
			((TextView) convertView.findViewById(R.id.birthday))
					.setText(DateFormat.getDateFormat(context).format(
							person.getBirthDate()));
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEmpty() {
		try {
			return personDao.countOf() < 1;
		} catch (SQLException e) {
			Log.e(LOG_TAG, "Failed: ", e);
			return true;
		}
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

}
