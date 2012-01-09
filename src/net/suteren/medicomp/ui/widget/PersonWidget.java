package net.suteren.medicomp.ui.widget;

import static net.suteren.medicomp.ui.activity.MedicompActivity.LOG_TAG;
import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Person;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PersonWidget extends AbstractWidget implements Widget {

	private TextView name;

	public PersonWidget(Context context, Person person) {
		super(context, person);
	}

	@Override
	public View getView(View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = layoutInflater.inflate(R.layout.dashboard_person,
					parent, false);

		name = (TextView) convertView.findViewById(R.id.textView1);

		name.setText(person.getName());

		Log.d(LOG_TAG, "Setting name: " + name.getText());

		return convertView;
	}

}
