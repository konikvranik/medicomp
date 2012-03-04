package net.suteren.medicomp.plugin.illness;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.suteren.medicomp.FieldFormatter;
import net.suteren.medicomp.R;
import net.suteren.medicomp.dao.MediCompDatabaseFactory;
import net.suteren.medicomp.domain.Category;
import net.suteren.medicomp.domain.DoubleValue;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import net.suteren.medicomp.plugin.Plugin;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import net.suteren.medicomp.ui.widget.AbstractWidget;
import net.suteren.medicomp.ui.widget.PluginWidget;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IllnessWidget extends AbstractWidget implements PluginWidget {

	private TextView temp;
	private Plugin plugin;

	public IllnessWidget(Context context) {
		super(context);
	}

	public IllnessWidget(Context context, Plugin plugin) {
		super(context);
		this.plugin = plugin;
	}

	public View getView(View convertView, ViewGroup parent) {
		if (convertView == null || true) {
			convertView = layoutInflater.inflate(R.layout.dashboard_illness,
					parent, false);
			TextView title = (TextView) convertView
					.findViewById(R.id.widgetTitle);
			title.setText(getTitle());
		}

		temp = (TextView) convertView.findViewById(R.id.textView2);
		Collection<Record> rs = getPerson().getRecords();
		Double val = null;

		Iterator<Record> ri = rs.iterator();
		Record r = null;
		while (ri.hasNext()) {
			Record rx = ri.next();

			if (rx.getType() == Type.DISEASE
					&& rx.getCategory() == Category.MEASURE
					&& (r == null || r.getTimestamp().compareTo(
							rx.getTimestamp()) < 0))
				r = rx;
		}

		FieldFormatter ff = null;
		if (r != null) {
			Collection<Field> fs = r.getFields();
			Iterator<Field> fi = fs.iterator();
			while (fi.hasNext()) {
				Field<?> f = fi.next();
				if (f.getType() == Type.DISEASE) {
					ff = new FieldFormatter(f);
					val = (Double) f.getValue();
					break;
				}
			}
			try {
				List<DoubleValue> dv = MediCompDatabaseFactory.getInstance()
						.createDao(DoubleValue.class).queryForAll();
			} catch (SQLException e) {
				Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
			}

		}

		return convertView;
	}

	@Override
	public boolean onClick(View view, long position, long id) {
		context.startActivity(new Intent(context, RecordListActivity.class));
		return true;
	}

	@Override
	protected Class<? extends PreferenceActivity> getPreferenceActivityClass() {
		return IllnessWidgetPreferenceActivity.class;
	}

	public String getName() {
		return getString(R.string.illness_widget_name);
	}

	public int getType() {
		return 2;
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public String getTitle() {
		return getString(R.string.illness_widget_title);
	}

	public String getSummary() {
		return getString(R.string.illness_widget_summary);
	}

	public Drawable getIcon() {
		return getDrawable(R.drawable.ic_menu_ic_menu_thermomether);
	}

}
