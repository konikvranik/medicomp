package net.suteren.medicomp.ui.widget;

import net.suteren.medicomp.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public class EmptyWidget extends AbstractWidget implements Widget {

	public EmptyWidget(Context context) {
		super(context);
	}

	public View getView(View convertView, ViewGroup parent) {
		if (convertView == null || true) {
			convertView = layoutInflater.inflate(R.layout.empty_widget, parent,
					false);
		}
		return convertView;
	}

	public String getName() {
		return context.getResources().getString(R.string.empty_widget);
	}

	@Override
	public boolean onRegister(WidgetManager widgetManager) {
		return super.onRegister(widgetManager);
	}

	public int getType() {
		return 0;
	}

	@Override
	public boolean onClick(View view, long position, long id) {
		view.showContextMenu();
		return true;
	}

	public String getTitle() {
		return context.getResources().getString(R.string.empty_widget);
	}

	public String getSummary() {
		return context.getResources().getString(R.string.empty_widget);
	}

	public Drawable getIcon() {
		// TODO - add an empty icon
		return null;
	}

}
