package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;

import net.suteren.medicomp.R;
import net.suteren.medicomp.plugin.Plugin;
import net.suteren.medicomp.ui.adapter.OrderableListAdapter;
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
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
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
		SubMenu subMenu = menu.findItem(R.id.add_widget).getSubMenu();

		for (Plugin plugin : getPluginManager().getActivePlugins()) {
			if (plugin.hasWidget()) {
				final Widget widget = plugin.newWidgetInstance(this);
				MenuItem item = subMenu.add(widget.getName());
				item.setIcon(widget.getIcon());
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
			return getWidgetManager().getItem((int) info.position)
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
	protected OrderableListAdapter getAdapter() {
		return getWidgetManager();
	}

	@Override
	protected boolean onItemClick(View view, int position, int id2) {
		return getWidgetManager().getItem((int) position).onClick(view,
				position, id2);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		registerForContextMenu(listView);
	}
}
