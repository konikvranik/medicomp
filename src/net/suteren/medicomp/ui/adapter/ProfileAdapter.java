package net.suteren.medicomp.ui.adapter;

import java.text.NumberFormat;

import net.suteren.medicomp.domain.WithId;
import android.database.DataSetObserver;
import android.widget.ListAdapter;

public abstract class ProfileAdapter<T extends WithId> implements
		ListAdapter {

	private T profileObject;
	protected NumberFormat nf = NumberFormat.getInstance();

	public T getItem(int arg0) {
		return profileObject;
	}

	public long getItemId(int position) {
		if (profileObject == null)
			return -1;
		return profileObject.getId();
	}

	public int getItemViewType(int position) {
		return Math.min(position, getViewTypeCount() - 1);
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isEmpty() {
		return profileObject != null;
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
}
