package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.ApplicationContextHolder;
import net.suteren.medicomp.domain.Person;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;

public abstract class MedicompActivity extends Activity {

	private final class ChangeReceiver extends BroadcastReceiver {

		private ChangeReceiver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			listView.invalidateViews();
		}
	}

	public static String LOG_TAG = "MEDICOMP";
	public static final String PERSON_ID = "personId";
	public static final String MEDICOMP_PREFS = "medicomp_preferences";
	public static final String PERSON_DATA_CHANGE_ACTION = "net.suteren.medicomp.CHANGE";
	public static final String PERSON_DATA_CHANGE_CATEGORY = "net.suteren.medicomp.PERSON";
	protected Dao<Person, Integer> personDao;
	protected Person person;
	protected ListView listView;
	protected ChangeReceiver changeReceiver;
	protected Context context;
	protected MediCompDatabaseFactory dbf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		Log.d(LOG_TAG, "Activity: " + this.getClass().getName());

		ApplicationContextHolder.setContext(this);
		MediCompDatabaseFactory.init(this);
		dbf = MediCompDatabaseFactory.getInstance();
		try {
			personDao = dbf.createDao(Person.class);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			throw new RuntimeException(e);
		}

		setContentView(getContentViewId());

		listView = requestListView();

		if (listView != null) {
			changeReceiver = new ChangeReceiver();
			IntentFilter intFilter = new IntentFilter();
			intFilter.addCategory(MedicompActivity.PERSON_DATA_CHANGE_CATEGORY);
			intFilter.addAction(MedicompActivity.PERSON_DATA_CHANGE_ACTION);
			registerReceiver(changeReceiver, intFilter);
		}

	}

	protected abstract int getContentViewId();

	protected abstract ListView requestListView();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.choosePerson:
			startActivity(new Intent(this, PersonListActivity.class));
			break;

		case R.id.addPerson:
			Intent intent = new Intent(this, PersonProfileActivity.class);
			Editor prefs = getSharedPreferences(MEDICOMP_PREFS,
					Context.MODE_WORLD_WRITEABLE).edit();
			prefs.remove(PERSON_ID);
			prefs.commit();
			this.startActivity(intent);
			break;

		case R.id.showRecords:
			startActivity(new Intent(this, RecordListActivity.class));
			break;

		default:
			break;
		}

		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	protected boolean setupPerson() {
		try {
			Integer personId = determinePersonId();
			Log.d(MedicompActivity.LOG_TAG, "PersonId: " + personId);

			if (personId == null) {
				return false;
			}
			Person personQuery = new Person();
			personQuery.setId(personId);
			Log.d(MedicompActivity.LOG_TAG,
					"Person before: " + personQuery.getId());
			person = personDao.queryForSameId(personQuery);
			if (person == null)
				return false;
			Log.d(MedicompActivity.LOG_TAG, "Person after: " + person.getId()
					+ ", " + person.getName());

			return true;
		} catch (SQLException e) {
			Log.e(MedicompActivity.LOG_TAG, "Failed: ", e);
			return false;
		}
	}

	protected void redirectToPersonList() {
		startActivity(new Intent(this, PersonListActivity.class));
		finish();
	}

	protected Integer determinePersonId() {
		Integer personId = null;
		Intent intent = getIntent();
		Bundle extras = null;
		if (intent != null)
			extras = intent.getExtras();
		if (extras != null)
			personId = extras.getInt("person");
		if (personId != null && personId > 0)
			return personId;

		personId = getSharedPreferences(MEDICOMP_PREFS,
				Context.MODE_WORLD_WRITEABLE).getInt(PERSON_ID, 0);
		if (personId != 0)
			return personId;

		return null;

	}

	protected abstract ListAdapter getAdapter();

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		actualizeList();
	}

	@Override
	protected void onResume() {
		super.onResume();
		actualizeList();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		actualizeList();
	}

	@Override
	protected void onDestroy() {
		if (changeReceiver != null)
			unregisterReceiver(changeReceiver);
		super.onDestroy();
	}

	protected void actualizeList() {
		setupPerson();
		if (listView != null) {
			listView.setAdapter(getAdapter());
			listView.invalidateViews();
		}
	}

}
