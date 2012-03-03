package net.suteren.medicomp.ui.adapter;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.suteren.medicomp.domain.WithId;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListAdapter;

public abstract class AbstractListAdapter<T extends WithId> implements
		ListAdapter {

	protected Context context;
	protected NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

	protected List<T> collection;

	protected Set<DataSetObserver> observers = new HashSet<DataSetObserver>();

	LayoutInflater layoutInflater;

	public AbstractListAdapter(Context context) throws SQLException {
		this.context = context;

		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public abstract void update() throws SQLException;

	public int getCount() {
		Log.d(this.getClass().getCanonicalName(),
				"getCount: " + collection.size());
		return collection.size();
	}

	public T getItem(int position) {
		T item = collection.get(position);
		Log.d(this.getClass().getCanonicalName(), "getItem @" + position + "#"
				+ item.getId());
		return item;
	}

	public long getItemId(int position) {
		int id = getItem(position).getId();
		Log.d(this.getClass().getCanonicalName(), "getItemId @" + position
				+ "#" + id);
		return id;
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
		Log.d(this.getClass().getCanonicalName(), "registerDataSetObserver: "
				+ observer.getClass().getCanonicalName());
		observers.add(observer);

	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		Log.d(this.getClass().getCanonicalName(), "unregisterDataSetObserver: "
				+ observer.getClass().getCanonicalName());
		observers.remove(observer);
	}

	public boolean areAllItemsEnabled() {
		return true;
	}

	public boolean isEnabled(int position) {
		return true;
	}

	public T getItemById(int id) {
		Log.d(this.getClass().getCanonicalName(), "getItemById #" + id);
		for (T w : collection)
			if (id == w.getId())
				return w;
		return null;
	}

	public int getPosition(T object) throws SQLException {
		int position = collection.indexOf(object);
		Log.d(this.getClass().getCanonicalName(),
				"getPosition #" + object.getId() + ": @" + position);
		return position;
	}

	public int getPosition(int id) throws SQLException {
		Log.d(this.getClass().getCanonicalName(), "getPosition #" + id);
		for (int i = 0; i < collection.size(); i++) {
			if (collection.get(i).getId() == id)
				return i;

		}
		throw new IllegalArgumentException("ID not found.");
	}

	public void notifyDataSetChanged() {
		for (DataSetObserver observer : observers) {
			observer.onChanged();
		}
	}

	public void notifyDataSetInvalidated() {
		for (DataSetObserver observer : observers) {
			observer.onInvalidated();
		}
	}

}
