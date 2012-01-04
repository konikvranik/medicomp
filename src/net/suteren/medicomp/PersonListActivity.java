package net.suteren.medicomp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.ui.PersonListAdapter;
import net.suteren.medicomp.ui.PersonProfileActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

public class PersonListActivity extends Activity {
	public static String LOG_TAG = "MEDICOMP";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		createDemoData();

		setContentView(R.layout.person_list);
		final ListView listView = (ListView) getWindow().findViewById(
				R.id.personList);

		try {

			listView.setAdapter(new PersonListAdapter(getApplicationContext()));

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Log.d(LOG_TAG, "Click " + arg2);

					// TODO Auto-generated method stub

				}
			});

			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					Log.d(LOG_TAG, "Long press on " + arg2);
					Person p = (Person) arg0.getItemAtPosition(arg2);
					Intent intent = new Intent(getApplicationContext(),
							PersonProfileActivity.class);
					intent.putExtra("person", p.getId());
					startActivity(intent);
					return true;
				}
			});
		} catch (Exception e) {
			Log.e(LOG_TAG, "Failed: ", e);
		}

	}

	private void createDemoData() {
		MediCompDatabaseFactory.init(getApplicationContext());

		MediCompDatabaseFactory dbf = MediCompDatabaseFactory.getInstance();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Dao<Person, Integer> personDao = dbf.createDao(Person.class);

			Person person = new Person();
			person.setName("Daddy");
			person.setBirthDate(dateFormat.parse("1970-06-16"));

			personDao.create(person);

			person = new Person();
			person.setName("Baby");
			person.setBirthDate(dateFormat.parse("2011-06-30"));

			personDao.create(person);

		} catch (Exception e) {
			Log.e(LOG_TAG, "Failed:", e);
		}
	}
}