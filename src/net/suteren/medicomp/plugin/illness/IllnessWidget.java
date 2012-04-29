package net.suteren.medicomp.plugin.illness;

import java.util.Collection;
import java.util.Iterator;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.record.Record;
import net.suteren.medicomp.enums.Category;
import net.suteren.medicomp.enums.Type;
import net.suteren.medicomp.plugin.Plugin;
import net.suteren.medicomp.ui.activity.RecordListActivity;
import net.suteren.medicomp.ui.widget.AbstractWidget;
import net.suteren.medicomp.ui.widget.PluginWidget;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.ViewGroup;
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

		temp = (TextView) convertView.findViewById(R.id.illness);
		Collection<Record> rs = getPerson().getRecords();
		Iterator<Record> ri = rs.iterator();
		Record r = null;
		while (ri.hasNext()) {
			Record rx = ri.next();

			if (rx.getType() == Type.DISEASE
					&& rx.getCategory() == Category.STATE
					&& (r == null || r.getTimestamp().compareTo(
							rx.getTimestamp()) < 0))
				r = rx;
		}
		temp.setText(r.getTitle());

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
