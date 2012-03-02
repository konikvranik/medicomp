package net.suteren.medicomp.ui.adapter;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.suteren.medicomp.domain.WithId;
import net.suteren.medicomp.ui.activity.MedicompActivity;
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
		Log.d(MedicompActivity.LOG_TAG, "registerDataSetObserver: "
				+ observer.getClass().getCanonicalName());
		observers.add(observer);

	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		Log.d(MedicompActivity.LOG_TAG, "unregisterDataSetObserver: "
				+ observer.getClass().getCanonicalName());
		observers.remove(observer);
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
