package net.suteren.medicomp.plugin;

import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.content.Intent;

public interface Plugin {

	String getName();

	boolean hasWidget();

	boolean hasActivity();

	boolean onActivate(PluginManager pluginManager);

	boolean onDeactivate(PluginManager pluginManager);

	boolean onRegister(PluginManager pluginManager);

	boolean onUnregister(PluginManager pluginManager);

	Widget newWidgetInstance(Context context);

	Intent newActivityInstance(Context context);

	String getId();

}
