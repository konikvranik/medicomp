package net.suteren.medicomp.ui.widget;

import net.suteren.medicomp.plugin.Plugin;

public interface PluginWidget extends Widget {

	Plugin getPlugin();

	void setPlugin(Plugin plugin);

}
