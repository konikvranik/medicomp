package net.suteren.medicomp.ui.widget;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.suteren.medicomp.ui.activity.ListActivity;
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

	private int typesCount;

	private class DataSetObserver extends android.database.DataSetObserver {

		@Override
		public void onChanged() {
			Log.d(this.getClass().getCanonicalName(), "onChanged");
			notifyOnChange();
		}

		private void notifyOnChange() {
			CommonWidgetManager widgetManager = CommonWidgetManager.this;
			if (widgetManager.isEmpty()) {
				Log.d(this.getClass().getCanonicalName(), "is empty");
				widgetManager
						.registerWidget(new EmptyWidget(context), 0, false);
			} else {
				for (int i = 0; i < collection.size(); i++) {
					Widget w = getItem(i);
					if (w instanceof EmptyWidget && collection.size() > 1)
						collection.remove(i);
				}
			}
		}

		@Override
		public void onInvalidated() {
			Log.d(this.getClass().getCanonicalName(), "onInvalidated");
			notifyOnChange();
		}

	}

	public CommonWidgetManager(ListActivity context) throws SQLException {
		super(context);
		collection = new ArrayList<Widget>();
		this.context = context;
		DataSetObserver observer = new DataSetObserver();
		registerDataSetObserver(observer);
		loadWidgets();
	}

	private void loadWidgets() {

		Log.d(this.getClass().getCanonicalName(), "Load widgets start...");
		SharedPreferences prefs = getWidgetStore();

		Map<Integer, Widget> w = new TreeMap<Integer, Widget>();

		for (String key : prefs.getAll().keySet()) {

			String[] pair = key.split(",");
			int widgetId = Integer.parseInt(pair[0]);
			int widgetPos = Integer.parseInt(pair[1]);
			String widgetClassName = prefs.getString(key, null);
			Log.d(this.getClass().getCanonicalName(), "loadwidgets pahse 0 @"
					+ widgetPos + "#" + widgetId + ": " + widgetClassName);
			try {
				@SuppressWarnings("unchecked")
				Constructor<Widget> c = (Constructor<Widget>) Class.forName(
						widgetClassName).getConstructor(Context.class);
				Widget widget = c.newInstance(context);
				widget.setId(widgetId);
				Log.d(this.getClass().getCanonicalName(),
						"loadwidgets pahse 1 @" + widgetPos + "#"
								+ widget.getId() + ": " + widget.getName());
				w.put(widgetPos, widget);
			} catch (InstantiationException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			} catch (IllegalAccessException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			} catch (ClassNotFoundException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			} catch (ClassCastException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			} catch (SecurityException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			} catch (InvocationTargetException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			}
		}
		for (Integer i : new TreeSet<Integer>(w.keySet())) {
			Widget widget = w.get(i);
			Log.d(this.getClass().getCanonicalName(), "loadwidgets pahse 2 @"
					+ i + "#" + widget.getId() + ": " + widget.getName());

			registerWidget(widget, i, false);
		}
		Log.d(this.getClass().getCanonicalName(), "Load widgets done...");
	}

	private boolean registerWidget(Widget widget, Integer position, boolean b) {
		Log.d(this.getClass().getCanonicalName(), "Registering widget "
				+ widget.getName() + ": " + widget.getId() + "@" + position);
		typesCount = Math.max(typesCount, widget.getType());
		if (widget.getId() < 1)
			widget.setId(getNewId());
		boolean result = widget.onRegister(this);
		if (position == null || position > collection.size())
			position = collection.size();
		if (position < 0)
			position = 0;
		if (result) {
			collection.add(position, widget);
			if (b) {
				saveWidgets();
			}
			update(b);
		}
		Log.d(this.getClass().getCanonicalName(),
				"Registered widget " + widget.getName() + ": #"
						+ widget.getId() + "@" + position);
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

	private synchronized int getNewId() {
		Integer last = 0;
		int newId = 0;
		SortedSet<Widget> widgets = new TreeSet<Widget>(
				new Comparator<Widget>() {
					public int compare(Widget o1, Widget o2) {
						if (o1 == null)
							return -1;
						if (o2 == null)
							return 1;
						return o1.getId() - o2.getId();
					}
				});
		widgets.addAll(collection);
		for (Widget widgetId : widgets) {
			newId = widgetId.getId() - 1;
			if (newId > last)
				break;
			last = widgetId.getId();
			newId = last + 1;
		}
		Log.d(this.getClass().getCanonicalName(), "New Id: " + newId);
		return newId;
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
				update(true);
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
		update(true);
		return to;
	}

	@Override
	public void update() {
		Log.d(this.getClass().getCanonicalName(), "update");
		notifyDataSetChanged();
		notifyDataSetInvalidated();

	};

	public void update(boolean b) {
		update();
		if (b)
			saveWidgets();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(this.getClass().getCanonicalName(), "get view @" + position);
		View view = getItem(position).getView(convertView, parent);
		return view;
	}

	@Override
	public int getViewTypeCount() {
		Log.d(this.getClass().getCanonicalName(), "getViewTypeCount: "
				+ (typesCount + 1));
		return typesCount + 1;
	}

	@Override
	public int getItemViewType(int position) {
		Log.d(this.getClass().getCanonicalName(), "getItemViewType @"
				+ position + ": " + getItem(position).getType());
		return getItem(position).getType();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	public Widget getItem(int position) {
		Widget item = collection.get(position);
		Log.d(this.getClass().getCanonicalName(), "getItem @" + position + "#"
				+ item.getId() + ": " + item.getName());
		return item;
	}
}
