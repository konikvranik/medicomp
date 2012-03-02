package net.suteren.medicomp.plugin.person;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.AbstractPlugin;
import net.suteren.medicomp.plugin.PluginManager;
import net.suteren.medicomp.ui.activity.PersonListActivity;
import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.content.Intent;

public class PersonPlugin extends AbstractPlugin {

	public String getName() {
		return getName(R.string.person_plugin_name);
	}

	@Override
	public boolean onRegister(PluginManager pluginManager) {
		super.onRegister(pluginManager);
		return pluginManager.activatePlugin(this);
	}

	@Override
	public boolean onUnregister(PluginManager pluginManager) {
		pluginManager.registerPlugin(this);
		return false;
	}

	@Override
	public boolean onDeactivate(PluginManager pluginManager) {
		return false;
	}

	@Override
	public Widget newWidgetInstance(Context context) {
		// TODO Auto-generated method stub
		return super.newWidgetInstance(context);
	}

	@Override
	public Intent newActivityInstance(Context context) {
		return new Intent(context, PersonListActivity.class);
	}

	@Override
	public boolean hasWidget() {
		return true;
	}

	@Override
	public boolean hasActivity() {
		return true;
	}
}
