package net.suteren.medicomp.ui.activity;

import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.adapter.OrderableListAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

public abstract class ListActivity extends MedicompActivity {

	int moveFromPosition;

	private final class actionClickListener implements
			AdapterView.OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ListActivity.this.onItemClick(view, position, (int) id);
		}
	}

	private final class moveClickListener implements
			AdapterView.OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			((OrderableListAdapter) getAdapter()).move(moveFromPosition,
					position);
			parent.setOnItemClickListener(new actionClickListener());
		}
	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		listView = requestListView();
		if (listView != null) {
			registerForContextMenu(listView);
			listView.setClickable(false);
			listView.setItemsCanFocus(false);
			listView.setOnItemClickListener(new actionClickListener());
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_contextmenu, menu);

		if (!(this instanceof OrderableListAdapter))
			menu.removeItem(R.id.move);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.edit:
			edit((int) info.id);
			return true;
		case R.id.delete:
			delete((int) info.id);
			return true;
		case R.id.move:
			listView.setOnItemClickListener(new moveClickListener());
			moveFromPosition = info.position;
			Toast.makeText(this, R.string.move_list_item_info,
					Toast.LENGTH_LONG).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	protected void edit(int id) {
		Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT)
				.show();
	}

	protected void delete(int id) {
		Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT)
				.show();
	}

	protected boolean onItemClick(View view, int id, int id2) {
		return false;
	}
}
