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
import android.widget.Toast;

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
					Log.d(this.getClass().getCanonicalName(),
							"list item click " + id);
					ListActivity.this.onItemClick(view, position, (int) id);
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
