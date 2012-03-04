package net.suteren.medicomp.ui.widget;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;

import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.ui.activity.MedicompActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public abstract class AbstractWidget implements Widget {

	protected LayoutInflater layoutInflater;
	protected Context context;
	private int id;

	public AbstractWidget(Context context) {
		this.context = context;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public boolean showPreferencesPane() {
		Class<? extends PreferenceActivity> clazz = getPreferenceActivityClass();
		if (clazz == null) {
			Toast.makeText(context, R.string.no_preferences, Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			Intent intent = new Intent(context, clazz);
			intent.putExtra("preferences", getPreferenceName());
			context.startActivity(intent);
			return true;
		}
	}

	protected Class<? extends PreferenceActivity> getPreferenceActivityClass() {
		return null;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public boolean onClick(View view, long position, long id) {
		return false;
	}

	protected String getString(int resourceId) {
		return context.getResources().getString(resourceId);
	}
	
	protected Drawable getDrawable(int resourceId) {
		return context.getResources().getDrawable(resourceId);
	}


	public boolean onRegister(WidgetManager widgetManager) {
		return true;
	}

	public boolean onUnregister(WidgetManager widgetManager) {
		return true;
	}

	protected SharedPreferences getWidgetPreferences() {
		return context.getSharedPreferences(getPreferenceName(),
				Context.MODE_PRIVATE);
	}

	protected Person getPerson() {
		Dao<Person, Integer> personDao;

		MediCompDatabaseFactory dbf = MediCompDatabaseFactory.getInstance();
		try {
			personDao = dbf.createDao(Person.class);

			return personDao.queryForId(context.getSharedPreferences(
					MedicompActivity.MEDICOMP_PREFS, Context.MODE_PRIVATE)
					.getInt(MedicompActivity.PERSON_ID_EXTRA, 0));
		} catch (SQLException e) {
		}

		return null;
	}

	public String getPreferenceName() {
		return getClass().getCanonicalName() + "#" + getId();
	}

}
