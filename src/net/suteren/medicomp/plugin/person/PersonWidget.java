package net.suteren.medicomp.plugin.person;

import net.suteren.medicomp.R;
import net.suteren.medicomp.ui.widget.AbstractWidget;
import net.suteren.medicomp.ui.widget.Widget;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PersonWidget extends AbstractWidget implements Widget {

	private TextView name;

	public PersonWidget(Context context) {
		super(context);
	}

	public View getView(View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.dashboard_person,
					parent, false);
		}
		name = (TextView) convertView.findViewById(R.id.textView1);
		name.setText(getPerson().getName());

		Log.d(this.getClass().getCanonicalName(),
				"Setting name: " + name.getText());

		return convertView;
	}

	@Override
	public boolean onClick(View view, long position, long id) {
		context.startActivity(new Intent(context, PersonListActivity.class));
		return true;
	}

	public String getName() {
		return getName(R.string.person_widget_name);
	}

	public int getType() {
		return 1;
	}

}
