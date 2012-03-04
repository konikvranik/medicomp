package net.suteren.medicomp.plugin.illness;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.AbstractPluginActivity;
import net.suteren.medicomp.plugin.PluginActivity;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class IllnessPluginActivity extends AbstractPluginActivity implements
		PluginActivity {

	public IllnessPluginActivity(Context context) {
		super(context);
	}

	public String getName() {
		return getString(R.string.illness_list_plugin_activity);
	}

	public String getTitle() {
		return getString(R.string.illness_list_plugin_activity_title);
	}

	public String getSummary() {

		return getString(R.string.illness_list_plugin_activity_summary);
	}

	public Drawable getIcon() {
		return getDrawable(R.drawable.ic_menu_ic_menu_thermomether);
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return IllnessListActivity.class;
	}

}
