package net.suteren.medicomp.plugin;

import java.util.Collection;
import java.util.Set;

import android.content.SharedPreferences;

public interface PluginManager {

	boolean registerPlugin(Plugin plugin);

	boolean unregisterPlugin(String pluginClassName);

	boolean unregisterPlugin(Plugin plugin);

	public boolean activatePlugin(Plugin plugin);

	public boolean deactivatePlugin(Plugin plugin);

	Plugin getPluginByClassName(String id);

	SharedPreferences getPluginPreferences(Plugin plugin);

	Set<Plugin> getActivePlugins();

	public Collection<Plugin> getRegisteredPlugins();

	public boolean isActive(Plugin plugin);
	
}
