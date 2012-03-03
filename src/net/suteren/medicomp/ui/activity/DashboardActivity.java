package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.Plugin;
import net.suteren.medicomp.ui.widget.CommonWidgetManager;
import net.suteren.medicomp.ui.widget.EmptyWidget;
import net.suteren.medicomp.ui.widget.Widget;
import android.content.Intent;
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
			if (widgetManager.isEmpty())
				widgetManager.registerWidget(new EmptyWidget(this), 0);
		} catch (SQLException e) {
			Log.d(this.getClass().getCanonicalName(), e.getMessage(), e);
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

		Log.d(this.getClass().getCanonicalName(),
				"Creating dashboart context menu @"
						+ ((AdapterContextMenuInfo) menuInfo).position + "#"
						+ ((AdapterContextMenuInfo) menuInfo).id);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dashboard_contextmenu, menu);
		SubMenu subMenu = menu.addSubMenu(R.string.add_widget);

		for (Plugin plugin : getPluginManager().getActivePlugins()) {
			if (plugin.hasWidget()) {
				final Widget widget = plugin.newWidgetInstance(this);
				Log.d(this.getClass().getCanonicalName(),
						"plugin: " + plugin.getName());
				Log.d(this.getClass().getCanonicalName(),
						"widget: " + widget.getName() + ", " + widget.getId());
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
		Log.d(this.getClass().getCanonicalName(), "onContextItemSelected #"
				+ item.getItemId() + ": " + item.getTitle());

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		Log.d(this.getClass().getCanonicalName(),
				"onContextItemSelected info: #" + info.id + "@" + info.position);

		switch (item.getItemId()) {
		case R.id.preferences:
			return getWidgetManager().getItem((int) info.position)
					.showPreferencesPane();
		case R.id.remove:
			Log.d(this.getClass().getCanonicalName(), "Removing widget "
					+ getWidgetManager().getItemById((int) info.id).getName()
					+ ", #" + info.id + "@" + info.position);
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
		return getWidgetManager().getItem((int) position).onClick(view,
				position, id2);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.d(this.getClass().getCanonicalName(),
				"New DashboardActivity intent");
		super.onNewIntent(intent);
		registerForContextMenu(listView);
	}
}
