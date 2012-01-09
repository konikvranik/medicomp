package net.suteren.medicomp.ui.adapter;

import java.sql.SQLException;
import java.util.List;

import net.suteren.medicomp.domain.PersistableWithId;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.widget.ListAdapter;

public abstract class AbstractListAdapter<T extends PersistableWithId>
		implements ListAdapter {

	protected Context context;

	protected List<T> collection;

	LayoutInflater layoutInflater;

	public AbstractListAdapter(Context context) throws SQLException {
		this.context = context;

		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public abstract void update() throws SQLException;

	@Override
	public int getCount() {
		return collection.size();
	}

	@Override
	public T getItem(int position) {
		return collection.get(position);
	}

	@Override
	public long getItemId(int position) {
		return collection.get(position).getId();
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return collection == null || collection.isEmpty();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
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
