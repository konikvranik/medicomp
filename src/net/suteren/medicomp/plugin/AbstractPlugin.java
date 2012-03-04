package net.suteren.medicomp.plugin;

import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public abstract class AbstractPlugin implements Plugin {

	private boolean active;
	private SharedPreferences preferences;
	private Context context;
	protected Resources resources;

	public boolean onRegister(PluginManager pluginManager) {
		preferences = pluginManager.getPluginPreferences(this);
		if (pluginManager instanceof MediCompPluginManager) {
			context = ((MediCompPluginManager) pluginManager).getContext();
			resources = context.getResources();
		}
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

	public Widget newWidgetInstance(Context context) {
		return null;
	}

	public boolean hasWidget() {
		return false;
	}

	public boolean hasActivity() {
		return false;
	}

	protected String getString(int resourceId) {
		return context.getResources().getString(resourceId);
	}

	protected Drawable getDrawable(int resourceId) {
		return context.getResources().getDrawable(resourceId);
	}

	public String getId() {
		return this.getClass().getCanonicalName();
	}
}
