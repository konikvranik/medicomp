package net.suteren.medicomp.ui.adapter;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import net.suteren.medicomp.domain.PersistableWithId;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.widget.ListAdapter;

public abstract class AbstractListAdapter<T extends PersistableWithId>
		implements ListAdapter {

	protected Context context;
	protected NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

	protected List<T> collection;

	LayoutInflater layoutInflater;

	public AbstractListAdapter(Context context) throws SQLException {
		this.context = context;

		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public abstract void update() throws SQLException;

	public int getCount() {
		return collection.size();
	}

	public T getItem(int position) {
		return collection.get(position);
	}

	public long getItemId(int position) {
		return collection.get(position).getId();
	}

	public int getItemViewType(int position) {
		return 0;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isEmpty() {
		return collection == null || collection.isEmpty();
	}

	public void registerDataSetObserver(DataSetObserver observer) {

		// TODO Auto-generated method stub

	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	public boolean areAllItemsEnabled() {
		return true;
	}

	public boolean isEnabled(int position) {
		return true;
	}

	public abstract T getItemById(int id) throws Exception;

	public int getPosition(T object) throws SQLException {
		return collection.indexOf(object);
	}

	public int getPosition(int id) throws SQLException {
		for (int i = 0; i < collection.size(); i++) {
			if (collection.get(i).getId() == id)
				return i;

		}
		throw new IllegalArgumentException("ID not found.");
	}
}
