package net.suteren.medicomp.ui.widget;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import net.suteren.medicomp.ui.activity.ListActivity;
import net.suteren.medicomp.ui.activity.MedicompActivity;
import net.suteren.medicomp.ui.adapter.AbstractListAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CommonWidgetManager extends AbstractListAdapter<Widget> implements
		WidgetManager {

	private ListActivity context;

	private class DataSetObserver extends android.database.DataSetObserver {

		@Override
		public void onChanged() {
			Log.d(MedicompActivity.LOG_TAG, "onChanged");
			CommonWidgetManager widgetManager = CommonWidgetManager.this;
			if (widgetManager.isEmpty()) {
				Log.d(MedicompActivity.LOG_TAG, "is empty");
				widgetManager
						.registerWidget(new EmptyWidget(context), 0, false);
			} else {
				for (int i = 0; i < collection.size(); i++) {
					Widget w = collection.get(i);
					if (w instanceof EmptyWidget && collection.size() > 1)
						collection.remove(i);
				}
			}
		}

		@Override
		public void onInvalidated() {
			Log.d(MedicompActivity.LOG_TAG, "onInvalidated");
			onChanged();
		}

	}

	public CommonWidgetManager(ListActivity context) throws SQLException {
		super(context);
		collection = new ArrayList<Widget>();
		this.context = context;
		loadWidgets();
		DataSetObserver observer = new DataSetObserver();
		registerDataSetObserver(observer);
	}

	private void loadWidgets() {
		SharedPreferences prefs = getWidgetStore();

		Map<Integer, Widget> w = new TreeMap<Integer, Widget>();

		for (String key : prefs.getAll().keySet()) {

			String[] pair = key.split(",");
			int widgetId = Integer.parseInt(pair[0]);
			int widgetPos = Integer.parseInt(pair[1]);
			String widgetClassName = prefs.getString(key, null);

			try {
				@SuppressWarnings("unchecked")
				Constructor<Widget> c = (Constructor<Widget>) Class.forName(
						widgetClassName).getConstructor(Context.class);
				Widget widget = c.newInstance(context);
				widget.setId(widgetId);
				w.put(widgetPos, widget);
			} catch (InstantiationException e) {
				Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
			} catch (IllegalAccessException e) {
				Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
			} catch (ClassCastException e) {
				Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
			} catch (SecurityException e) {
				Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
			} catch (InvocationTargetException e) {
				Log.e(MedicompActivity.LOG_TAG, e.getMessage(), e);
			}
		}
		for (Integer i : new TreeSet<Integer>(w.keySet())) {
			registerWidget(w.get(i), i, false);
		}
	}

	private boolean registerWidget(Widget widget, Integer position, boolean b) {
		boolean result = widget.onRegister(this);
		if (position == null || position > collection.size())
			position = collection.size();
		if (position < 0)
			position = 0;
		widget.setId(getNewId());
		if (result) {
			collection.add(position, widget);
			if (b) {
				saveWidgets();
			}
			notifyDataSetChanged();
		}
		return true;
	}

	private void saveWidgets() {
		SharedPreferences p = getWidgetStore();
		Editor editor = p.edit();
		editor.clear();
		for (String key : p.getAll().keySet())
			editor.remove(key);
		for (int i = 0; i < collection.size(); i++) {
			Widget widget = collection.get(i);
			editor.putString(widget.getId() + "," + i, widget.getClass()
					.getCanonicalName());
		}
		editor.commit();
	}

	private int getNewId() {
		Integer last = 0;
		TreeSet<Widget> widgets = new TreeSet<Widget>(new Comparator<Widget>() {
			public int compare(Widget arg0, Widget arg1) {
				if (arg0 == null)
					return -1;
				if (arg1 == null)
					return 1;
				return arg1.getId() - arg0.getId();
			}
		});
		widgets.addAll(collection);
		for (Widget widgetId : widgets) {
			if (widgetId.getId() - last > 1)
				return widgetId.getId() - 1;
			last = widgetId.getId();
		}
		return last + 1;
	}

	private SharedPreferences getWidgetStore() {
		return context.getSharedPreferences(context.getClass()
				.getCanonicalName()
				+ ":"
				+ this.getClass().getCanonicalName()
				+ "#activeWidgets", Context.MODE_PRIVATE);
	}

	public boolean registerWidget(Widget widget, Integer position) {
		return registerWidget(widget, position, true);
	}

	public boolean unRegisterWidget(int widgetId) {
		for (int i = 0; i < collection.size(); i++) {
			if (collection.get(i).getId() == widgetId) {
				collection.remove(i);
				saveWidgets();
				notifyDataSetInvalidated();
				return true;
			}
		}
		return false;
	}

	public Integer moveWidgetToPosition(int from, int to) {

		Widget w = collection.get(from);
		collection.remove(from);
		if (to < 0)
			to = 0;
		if (to > collection.size())
			to = collection.size();
		collection.add(to, w);
		saveWidgets();
		notifyDataSetChanged();
		return to;
	}

	@Override
	public void update() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Widget getItemById(int id) {
		for (int i = 0; i < collection.size(); i++) {
			Widget w = collection.get(i);
			if (w.getId() == id) {
				return w;
			}
		}
		return null;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = collection.get(position).getView(convertView, parent);
		view.setClickable(false);
		view.setLongClickable(false);
		view.setFocusableInTouchMode(false);
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return collection.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).getType();
	}

}
