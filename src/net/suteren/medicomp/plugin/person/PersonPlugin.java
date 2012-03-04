package net.suteren.medicomp.plugin.person;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.AbstractPlugin;
import net.suteren.medicomp.plugin.PluginActivity;
import net.suteren.medicomp.plugin.PluginManager;
import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class PersonPlugin extends AbstractPlugin {

	public String getName() {
		return getString(R.string.person_plugin_name);
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
		return new PersonWidget(context, this);
	}

	@Override
	public boolean hasWidget() {
		return true;
	}

	@Override
	public boolean hasActivity() {
		return true;
	}

	public String getTitle() {
		return getString(R.string.person_plugin_title);
	}

	public String getSummary() {
		return getString(R.string.person_plugin_summary);
	}

	public Drawable getIcon() {
		return getDrawable(R.drawable.ic_menu_ic_menu_patient);
	}

	public PluginActivity newActivityInstance(Context context) {
		return new PersonPluginActivity(context);
	}
}
