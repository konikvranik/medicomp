package net.suteren.medicomp.ui.activity;

import net.suteren.medicomp.R;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public abstract class ListActivity extends MedicompActivity {

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ListView lv = requestListView();
		if (lv != null) {
			registerForContextMenu(lv);
			lv.setClickable(false);
			lv.setItemsCanFocus(false);
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.d(LOG_TAG, "list item click " + id);
					ListActivity.this.onItemClick(view, position,(int) id);
				}
			});
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_contextmenu, menu);
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
		default:
			return super.onContextItemSelected(item);
		}
	}

	protected abstract void edit(int id);

	protected abstract void delete(int id);

	protected abstract boolean onItemClick(View view, int id, int id2);
}
