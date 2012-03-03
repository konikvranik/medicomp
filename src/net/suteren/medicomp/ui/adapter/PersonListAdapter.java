package net.suteren.medicomp.ui.adapter;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.plugin.person.PersonListActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

public class PersonListAdapter extends AbstractListAdapter<Person> {

	private Dao<Person, Integer> personDao;

	public PersonListAdapter(PersonListActivity context) throws SQLException {
		super(context);
		personDao = MediCompDatabaseFactory.getInstance(context).createDao(
				Person.class);
		update();
	}

	@Override
	public void update() throws SQLException {
		collection = personDao.queryForAll();
		Log.d(this.getClass().getCanonicalName(), "PersonListAdapter: "
				+ collection.size());
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.person_list_row,
					parent, false);
			convertView.setFocusable(false);
			convertView.setClickable(false);
		}
		Person person = getItem(position);
		if (person != null) {
			((TextView) convertView.findViewById(R.id.name)).setText(person
					.getName());
			if (person.getBirthDate() != null)
				((TextView) convertView.findViewById(R.id.birthday))
						.setText(DateFormat.getDateFormat(context).format(
								person.getBirthDate()));
		}
		return convertView;
	}

	@Override
	public Person getItemById(int id) {
		try {
			return personDao.queryForId(id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
