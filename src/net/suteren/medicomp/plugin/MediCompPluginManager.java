package net.suteren.medicomp.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.suteren.medicomp.format.RecordFormatter;
import net.suteren.medicomp.ui.activity.MedicompActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class MediCompPluginManager implements PluginManager {

	private Context context;
	private SharedPreferences pluginStore;
	private Map<String, Plugin> registeredPlugins = new HashMap<String, Plugin>();
	private Map<Plugin, Collection<RecordFormatter>> recordFormatters = new HashMap<Plugin, Collection<RecordFormatter>>();
	private static MediCompPluginManager self;
	private Set<PluginChangeObserver> observers = new HashSet<PluginChangeObserver>();

	public static MediCompPluginManager getInstance(Context context) {
		if (self == null)
			self = new MediCompPluginManager(context);
		return self;
	}

	private MediCompPluginManager(Context context) {
		this.context = context;
		loadPlugins();
	}

	private void loadPlugins() {
		SharedPreferences prefs = getPluginStore();

		Map<String, ?> pluginPrefs = prefs.getAll();
		try {
			for (String className : pluginPrefs.keySet()) {
				boolean active = prefs.getBoolean(className, true);

				boolean success = false;
				try {
					Plugin plugin = (Plugin) Class.forName(className)
							.newInstance();
					registerPlugin(plugin, false, false);
					if (active) {
						activatePlugin(plugin, false);
					}
					success = true;
				} catch (ClassNotFoundException e) {
					Log.e(this.getClass().getCanonicalName(), "Plugin "
							+ className + " not found => unregistering.");
				} catch (InstantiationException e) {
					Log.e(this.getClass().getCanonicalName(), "Plugin "
							+ className
							+ " can not be instantiated => unregistering.");
				} catch (IllegalAccessException e) {
					Log.e(this.getClass().getCanonicalName(), "Plugin "
							+ className
							+ " has no permissions => unregistering.");
				} catch (ClassCastException e) {
					Log.e(this.getClass().getCanonicalName(), "Class"
							+ className + " is not a plugin=> unregistering.");
				}
				if (!success) {
					unregisterPlugin(className);
				}
			}
		} finally {
			notifyPluginChange();
		}
	}

	private SharedPreferences getPluginStore() {

		pluginStore = context
				.getSharedPreferences(
						MedicompActivity.REGISTERED_PLUGINS_PREFS,
						Context.MODE_PRIVATE);
		return pluginStore;
	}

	public boolean registerPlugin(Plugin plugin) {
		registerPlugin(plugin, true, true);
		return true;
	}

	private void registerPlugin(Plugin plugin, boolean store, boolean notify) {
		try {
			if (store) {
				Editor editor = getPluginStore().edit();
				editor.putBoolean(plugin.getId(), isActive(plugin));
				editor.commit();
			}
			registeredPlugins.put(plugin.getId(), plugin);
			plugin.onRegister(this);
		} finally {
			notifyPluginChange();
		}
	}

	public boolean unregisterPlugin(Plugin plugin) {
		return unregisterPlugin(plugin, true);
	}

	private boolean unregisterPlugin(Plugin plugin, boolean notify) {
		try {
			boolean result = deactivatePlugin(plugin);
			unregisterPlugin(plugin.getId());
			result = result && plugin.onUnregister(this);
			return result;
		} finally {
			notifyPluginChange();
		}
	}

	public boolean unregisterPlugin(String className) {
		return unregisterPlugin(className, true);
	}

	private boolean unregisterPlugin(String className, boolean notify) {
		try {
			Editor editor = getPluginStore().edit();
			editor.remove(className);
			editor.commit();
			return true;
		} finally {
			notifyPluginChange();
		}
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
		return activatePlugin(plugin, true);
	}

	private boolean activatePlugin(Plugin plugin, boolean notify) {
		try {
			boolean result = plugin.onActivate(this);
			if (result) {
				Editor editor = getPluginStore().edit();
				editor.putBoolean(plugin.getId(), true);
				editor.commit();
			}
			return result;
		} finally {
			notifyPluginChange();
		}
	}

	public boolean deactivatePlugin(Plugin plugin) {
		return deactivatePlugin(plugin, true);
	}

	private boolean deactivatePlugin(Plugin plugin, boolean notify) {
		try {
			boolean result = plugin.onDeactivate(this);
			if (result) {
				Editor editor = getPluginStore().edit();
				editor.putBoolean(plugin.getId(), false);
				editor.commit();
			}
			return result;
		} finally {
			notifyPluginChange();
		}
	}

	public Set<Plugin> getActivePlugins() {
		HashSet<Plugin> result = new HashSet<Plugin>();
		for (Plugin plugin : registeredPlugins.values()) {
			if (isActive(plugin)) {
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

	public Collection<RecordFormatter> getRecordFormatters() {
		Collection<RecordFormatter> rfs = new HashSet<RecordFormatter>();
		for (Collection<RecordFormatter> c : recordFormatters.values()) {
			rfs.addAll(c);
		}
		return rfs;
	}

	public void registerRecordFormatters(Plugin plugin,
			Collection<RecordFormatter> formatters) {

		recordFormatters.put(plugin, formatters);
	}

	public void unregisterRecordFormatters(Plugin plugin) {
		recordFormatters.remove(plugin);
	}

	public void registerPluginChangeObserver(PluginChangeObserver observer) {
		observers.add(observer);
	}

	public void unregisterPluginChangeObserver(PluginChangeObserver observer) {
		observers.remove(observer);
	}

	protected void notifyPluginChange() {
		for (PluginChangeObserver pco : observers)
			pco.pluginChangeNotify();
	}
}
