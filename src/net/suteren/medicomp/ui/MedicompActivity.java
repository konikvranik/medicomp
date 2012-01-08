package net.suteren.medicomp.ui;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.ApplicationContextHolder;
import net.suteren.medicomp.domain.Person;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

public class MedicompActivity extends Activity {

	public static String LOG_TAG = "MEDICOMP";
	static final String PERSON_ID = "personId";
	protected static final String MEDICOMP_PREFS = "medicomp_preferences";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ApplicationContextHolder.setContext(getApplicationContext());
	}

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
			this.startActivity(intent);
			break;
		default:
			break;
		}

		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

}
