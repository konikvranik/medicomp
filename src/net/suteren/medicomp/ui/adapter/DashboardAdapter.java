package net.suteren.medicomp.ui.adapter;

import static net.suteren.medicomp.ui.activity.MedicompActivity.LOG_TAG;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.suteren.medicomp.domain.Person;
import net.suteren.medicomp.plugin.chart.ChartWidget;
import net.suteren.medicomp.plugin.person.PersonWidget;
import net.suteren.medicomp.plugin.temperature.TemperatureWidget;
import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class DashboardAdapter extends AbstractListAdapter<Widget> {

	private Person person;

	private List<Widget> widgets = new ArrayList<Widget>();

	public DashboardAdapter(Context context, Person person) throws SQLException {
		super(context);
		if (person == null)
			throw new NullPointerException("Person == null!");
		this.person = person;

		update();

	}

	@Override
	public int getCount() {
		return widgets.size();
	}

	@Override
	public Widget getItem(int position) {
		return widgets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return widgets.get(position).getId();
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(LOG_TAG, "Person in DashboardAdapter: " + person.getId());
		View view = widgets.get(position).getView(convertView, parent);
		view.setClickable(false);
		view.setLongClickable(false);
		view.setFocusableInTouchMode(false);
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return widgets.size();
	}

	@Override
	public boolean hasStableIds() {
		return true;
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

	@Override
	public void update() throws SQLException {
		widgets = new ArrayList<Widget>();
		widgets.add(new PersonWidget(context));
		widgets.add(new TemperatureWidget(context));
		widgets.add(new ChartWidget(context));

	}

	@Override
	public Widget getItemById(int id) {
		Widget wi = null;
		for (Widget w : widgets) {
			int i = w.getId();
			if (i == id) {
				Log.d(LOG_TAG, "ID: " + id);
				wi = w;
				break;
			}
		}
		return wi;
	}

}
