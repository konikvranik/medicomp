package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.Plugin;
import net.suteren.medicomp.plugin.person.PersonWidget;
import net.suteren.medicomp.ui.widget.CommonWidgetManager;
import net.suteren.medicomp.ui.widget.Widget;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DashboardActivity extends ListActivity {

	private CommonWidgetManager widgetManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (!setupPerson()) {
			redirectToPersonList();
			return;
		}

		try {
			widgetManager = new CommonWidgetManager(this);
			widgetManager.registerWidget(new PersonWidget(this), 0);
		} catch (SQLException e) {
			Log.d(LOG_TAG, e.getMessage(), e);
		}
	}

	@Override
	protected ListView requestListView() {
		return (ListView) getWindow().findViewById(R.id.dashboard);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.dashboard;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			final ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dashboard_contextmenu, menu);
		SubMenu subMenu = menu.addSubMenu(R.string.add_widget);

		for (Plugin plugin : getPluginManager().getActivePlugins()) {
			if (plugin.hasWidget()) {
				final Widget widget = plugin.newWidgetInstance(this);
				Log.d(LOG_TAG, "plugin: " + plugin.getName());
				Log.d(LOG_TAG, "widget: " + widget.getName());
				MenuItem item = subMenu.add(widget.getName());
				item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem menuitem) {
						getWidgetManager().registerWidget(widget,
								((AdapterContextMenuInfo) menuInfo).position);
						return true;
					}
				});
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.preferences:
			return getWidgetManager().getItemById((int) info.id)
					.showPreferencesPane();
		case R.id.remove:
			return getWidgetManager().unRegisterWidget((int) info.id);
		default:
			return super.onContextItemSelected(item);
		}
	}

	CommonWidgetManager getWidgetManager() {
		return widgetManager;
	}

	@Override
	protected ListAdapter getAdapter() {
		return getWidgetManager();
	}

	@Override
	protected boolean onItemClick(View view, int position, int id2) {
		return getWidgetManager().getItemById((int) id2).onClick(view,
				position, id2);
	}

}
