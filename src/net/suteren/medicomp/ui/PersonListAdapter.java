package net.suteren.medicomp.ui;

import java.sql.SQLException;
import java.util.List;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

public class PersonListAdapter implements ListAdapter {

	SQLiteDatabase database = null;
	private Dao<Person, Integer> personDao;
	private Context context;

	List<Person> persons;

	LayoutInflater layoutInflater;

	public PersonListAdapter(Context context) throws SQLException {
		this.context = context;

		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		update();

	}

	private void update() throws SQLException {
		MediCompDatabaseFactory dbf = MediCompDatabaseFactory
				.getInstance(context);
		personDao = dbf.createDao(Person.class);
		persons = personDao.queryForAll();

	}

	@Override
	public int getCount() {
		return persons.size();
	}

	@Override
	public Person getItem(int position) {
		return persons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return persons.get(position).getId();
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// convertView = View.inflate(context, R.layout.person_list_row,
			// parent);

			convertView = layoutInflater.inflate(R.layout.person_list_row,
					parent, false);
			convertView.setFocusable(false);
			convertView.setClickable(false);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Person person = getItem(position);
					Editor prefs = context.getSharedPreferences(
							MedicompActivity.MEDICOMP_PREFS,
							Context.MODE_WORLD_WRITEABLE).edit();
					prefs.putInt(MedicompActivity.PERSON_ID, person.getId());
					prefs.commit();
					Intent intent = new Intent(context, DashboardActivity.class);
					intent.putExtra("person", person.getId());
					context.startActivity(intent);

				}
			});
			convertView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					Person p = (Person) getItem(position);
					Intent intent = new Intent(context,
							PersonProfileActivity.class);
					intent.putExtra("person", p.getId());
					context.startActivity(intent);
					return true;
				}
			});
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
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return persons == null || persons.isEmpty();
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
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	public Person getItemById(int id) throws SQLException {
		return personDao.queryForId(id);
	}

	public int getPosition(Person person) throws SQLException {
		return persons.indexOf(persons);
	}

	public int getPosition(int id) throws SQLException {
		for (int i = 0; i < persons.size(); i++) {
			if (persons.get(i).getId() == id)
				return i;

		}
		throw new IllegalArgumentException("ID not found.");
	}

}
