package net.suteren.medicomp.plugin.person;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.AbstractPluginActivity;
import net.suteren.medicomp.plugin.PluginActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class PersonPluginActivity extends AbstractPluginActivity implements PluginActivity {

	public PersonPluginActivity(Context context) {
		super(context);
	}

	public String getName() {
		return getString(R.string.person_list_plugin_activity);
	}

	public String getTitle() {
		return getString(R.string.person_list_plugin_activity_title);
	}

	public String getSummary() {

		return getString(R.string.person_list_plugin_activity_summary);
	}

	public Drawable getIcon() {
		return getDrawable(R.drawable.ic_menu_ic_menu_patient);
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return PersonListActivity.class;
	}

	

}
