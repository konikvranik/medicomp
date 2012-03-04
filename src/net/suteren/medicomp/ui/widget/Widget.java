package net.suteren.medicomp.ui.widget;

import net.suteren.medicomp.domain.WithId;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public interface Widget extends WithId {

	View getView(View convertView, ViewGroup parent);

	boolean onClick(View view, long position, long id);

	boolean showPreferencesPane();

	String getName();

	String getTitle();

	String getSummary();

	Drawable getIcon();

	boolean onRegister(WidgetManager widgetManager);

	boolean onUnregister(WidgetManager widgetManager);

	int getType();
}
