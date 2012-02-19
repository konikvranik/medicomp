package net.suteren.medicomp.ui.adapter;

import net.suteren.medicomp.domain.PersistableWithId;
import android.database.DataSetObserver;
import android.widget.ListAdapter;

public abstract class ProfileAdapter<T extends PersistableWithId> implements
		ListAdapter {

	private T profileObject;

	@Override
	public T getItem(int arg0) {
		return profileObject;
	}

	@Override
	public long getItemId(int position) {
		if (profileObject == null)
			return -1;
		return profileObject.getId();
	}

	@Override
	public int getItemViewType(int position) {
		return Math.min(position, getViewTypeCount() - 1);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return profileObject != null;
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
}
