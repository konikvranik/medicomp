package net.suteren.medicomp.ui;

import java.util.ArrayList;
import java.util.List;

import net.suteren.medicomp.domain.Person;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import static net.suteren.medicomp.ui.MedicompActivity.LOG_TAG;

public class DashboardAdapter implements ListAdapter {

	private Person person;
	private Context context;
	private LayoutInflater layoutInflater;

	private List<Widget> widgets = new ArrayList<Widget>();

	public DashboardAdapter(Context context, Person person) {
		if (context == null)
			throw new NullPointerException("Context == null");
		if (person == null)
			throw new NullPointerException("Person == null!");
		this.context = context;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.person = person;

		widgets.add(new TemperatureWidget(context, person));
		widgets.add(new TemperatureGraphWidget(context, person));
	}

	@Override
	public int getCount() {
		return widgets.size();
	}

	@Override
	public Object getItem(int position) {
		return person;
	}

	@Override
	public long getItemId(int position) {
		return person.getId();
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(LOG_TAG, "View: " + position + ", " + convertView);
		return widgets.get(position).getView(convertView, parent);
	}

	@Override
	public int getViewTypeCount() {
		return widgets.size();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return widgets == null || widgets.isEmpty();
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
