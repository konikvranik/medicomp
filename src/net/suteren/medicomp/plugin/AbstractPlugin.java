package net.suteren.medicomp.plugin;

import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public abstract class AbstractPlugin implements Plugin {

	private boolean active;
	private SharedPreferences preferences;
	private Context context;

	public boolean onRegister(PluginManager pluginManager) {
		preferences = pluginManager.getPluginPreferences(this);
		if (pluginManager instanceof MediCompPluginManager)
			context = ((MediCompPluginManager) pluginManager).getContext();
		return true;
	}

	public boolean onUnregister(PluginManager pluginManager) {
		return true;
	}

	public boolean onActivate(PluginManager pluginManager) {
		return true;
	}

	public boolean onDeactivate(PluginManager pluginManager) {
		return true;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	protected SharedPreferences getPreferences() {
		return preferences;
	}

	protected Context getContext() {
		return context;
	}

	public Intent newActivityInstance(Context context) {
		return null;
	}

	public Widget newWidgetInstance(Context context) {
		return null;
	}

	public boolean hasWidget() {
		return false;
	}

	public boolean hasActivity() {
		return false;
	}

	protected String getName(int resourceId) {
		return context.getResources().getString(resourceId);
	}

	public String getId() {
		return this.getClass().getCanonicalName();
	}
}
