package net.suteren.medicomp.ui.adapter;

import android.widget.ListAdapter;

public interface OrderableListAdapter extends ListAdapter {

	int move(int fromPosition, int toPosition);
}
