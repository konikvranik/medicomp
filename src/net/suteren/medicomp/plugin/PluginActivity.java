package net.suteren.medicomp.plugin;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public interface PluginActivity {

	String getName();

	String getTitle();

	String getSummary();

	Drawable getIcon();

	Intent newIntent();
	
}
