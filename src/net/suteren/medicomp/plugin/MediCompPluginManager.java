package net.suteren.medicomp.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.suteren.medicomp.ui.activity.MedicompActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class MediCompPluginManager implements PluginManager {

	private Context context;
	private SharedPreferences pluginStore;
	private Map<String, Plugin> registeredPlugins = new HashMap<String, Plugin>();

	public MediCompPluginManager(Context context) {
		this.context = context;
		loadPlugins();
	}

	private void loadPlugins() {
		Log.d(this.getClass().getCanonicalName(), "loadPlugins: start");
		SharedPreferences prefs = getPluginStore();

		Map<String, ?> pluginPrefs = prefs.getAll();
		for (String className : pluginPrefs.keySet()) {
			boolean active = prefs.getBoolean(className, true);

			Log.d(this.getClass().getCanonicalName(), "loadPlugins: "
					+ className + " is " + (active ? "" : "not ") + "active");

			boolean success = false;
			try {
				Plugin plugin = (Plugin) Class.forName(className).newInstance();
				registerPlugin(plugin, false);
				if (active) {
					activatePlugin(plugin);
				}
				success = true;
			} catch (ClassNotFoundException e) {
				Log.d(this.getClass().getCanonicalName(), "Plugin " + className
						+ " not found => unregistering.");
			} catch (InstantiationException e) {
				Log.d(this.getClass().getCanonicalName(), "Plugin " + className
						+ " can not be instantiated => unregistering.");
			} catch (IllegalAccessException e) {
				Log.d(this.getClass().getCanonicalName(), "Plugin " + className
						+ " has no permissions => unregistering.");
			} catch (ClassCastException e) {
				Log.d(this.getClass().getCanonicalName(), "Class" + className
						+ " is not a plugin=> unregistering.");
			}
			if (!success) {
				unregisterPlugin(className);
			}
		}
		Log.d(this.getClass().getCanonicalName(), "loadPlugins: done");
	}

	private SharedPreferences getPluginStore() {
		Log.d(this.getClass().getCanonicalName(), "context: "
				+ context.getClass().getName());

		pluginStore = context
				.getSharedPreferences(
						MedicompActivity.REGISTERED_PLUGINS_PREFS,
						Context.MODE_PRIVATE);
		return pluginStore;
	}

	public boolean registerPlugin(Plugin plugin) {
		registerPlugin(plugin, true);
		return true;
	}

	private void registerPlugin(Plugin plugin, boolean store) {
		if (store) {
			Editor editor = getPluginStore().edit();
			editor.putBoolean(plugin.getId(), isActive(plugin));
			editor.commit();
		}
		registeredPlugins.put(plugin.getId(), plugin);
		plugin.onRegister(this);
	}

	public boolean unregisterPlugin(Plugin plugin) {
		boolean result = deactivatePlugin(plugin);
		unregisterPlugin(plugin.getId());
		result = result && plugin.onUnregister(this);
		return result;
	}

	public boolean unregisterPlugin(String className) {
		Editor editor = getPluginStore().edit();
		editor.remove(className);
		editor.commit();
		return true;
	}

	public Plugin getPluginByClassName(String className) {
		return registeredPlugins.get(className);
	}

	public SharedPreferences getPluginPreferences(Plugin plugin) {
		return context.getSharedPreferences(plugin.getId(),
				Context.MODE_PRIVATE);
	}

	public Context getContext() {
		return context;
	}

	public boolean activatePlugin(Plugin plugin) {
		boolean result = plugin.onActivate(this);
		Log.d(this.getClass().getCanonicalName(),
				"activating: " + plugin.getName());
		if (result) {
			Log.d(this.getClass().getCanonicalName(), "activity: "
					+ isActive(plugin));
			Editor editor = getPluginStore().edit();
			editor.putBoolean(plugin.getId(), true);
			editor.commit();
			Log.d(this.getClass().getCanonicalName(),
					"activatePlugin: stored "
							+ plugin.getId()
							+ " => "
							+ getPluginStore()
									.getBoolean(plugin.getId(), false));
		}
		return result;
	}

	public boolean deactivatePlugin(Plugin plugin) {
		boolean result = plugin.onDeactivate(this);
		Log.d(this.getClass().getCanonicalName(),
				"deactivating: " + plugin.getName());
		if (result) {
			Log.d(this.getClass().getCanonicalName(), "activity: "
					+ isActive(plugin));
			Editor editor = getPluginStore().edit();
			editor.putBoolean(plugin.getId(), false);
			editor.commit();
			Log.d(this.getClass().getCanonicalName(),
					"deactivatePlugin: stored "
							+ plugin.getId()
							+ " => "
							+ getPluginStore()
									.getBoolean(plugin.getId(), false));
		}
		return result;
	}

	public Set<Plugin> getActivePlugins() {
		HashSet<Plugin> result = new HashSet<Plugin>();
		for (Plugin plugin : registeredPlugins.values()) {
			Log.d(this.getClass().getCanonicalName(),
					"plugin name " + plugin.getName());
			if (isActive(plugin)) {
				Log.d(this.getClass().getCanonicalName(), "active plugin name "
						+ plugin.getName());
				result.add(plugin);
			}
		}
		return result;
	}

	public Collection<Plugin> getRegisteredPlugins() {
		return registeredPlugins.values();
	}

	public boolean isActive(Plugin plugin) {
		return getPluginStore().getBoolean(plugin.getId(), true);
	}

}