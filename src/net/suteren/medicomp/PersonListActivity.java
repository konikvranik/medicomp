package net.suteren.medicomp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.ui.PersonListAdapter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

public class PersonListActivity extends Activity {
	public static final String PERSON_DATA_CHANGE_CATEGORY = "net.suteren.medicomp.PERSON";
	public static final String PERSON_DATA_CHANGE_ACTION = "net.suteren.medicomp.CHANGE";
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

			IntentFilter intFilter = new IntentFilter();
			intFilter.addCategory(PERSON_DATA_CHANGE_CATEGORY);
			intFilter.addAction(PERSON_DATA_CHANGE_ACTION);
			registerReceiver(new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					listView.invalidateViews();
					Log.d(LOG_TAG, "Broadcast recieved.");
				}
			}, intFilter);

			listView.setAdapter(new PersonListAdapter(this));
			listView.setItemsCanFocus(false);

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