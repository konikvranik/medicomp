package net.suteren.medicomp.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public abstract class AbstractPluginActivity implements PluginActivity {

	private Context context;

	public AbstractPluginActivity(Context context) {
		this.context = context;
	}

	public Intent newIntent() {
		return new Intent(getContext(), getActivityClass());
	}

	protected Context getContext() {
		return context;
	}

	public abstract Class<? extends Activity> getActivityClass();

	protected String getString(int resourceId) {
		return context.getResources().getString(resourceId);
	}

	protected Drawable getDrawable(int resourceId) {
		return context.getResources().getDrawable(resourceId);
	}

}
