package net.suteren.medicomp.plugin;

import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public interface Plugin {

	String getName();

	String getTitle();

	String getSummary();

	Drawable getIcon();

	boolean hasWidget();

	boolean hasActivity();

	boolean onActivate(PluginManager pluginManager);

	boolean onDeactivate(PluginManager pluginManager);

	boolean onRegister(PluginManager pluginManager);

	boolean onUnregister(PluginManager pluginManager);

	Widget newWidgetInstance(Context context);

	PluginActivity newActivityInstance(Context context);

	String getId();

}
