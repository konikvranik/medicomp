package net.suteren.medicomp.ui.widget;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Person;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public abstract class AbstractWidget implements Widget {

	protected LayoutInflater layoutInflater;
	protected Person person;
	protected Context context;

	public AbstractWidget(Context context, Person person) {
		this.person = person;
		this.context = context;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public boolean showPreferencesPane() {
		Toast.makeText(context, R.string.no_preferences, Toast.LENGTH_SHORT).show();
		return false;
	}

	public void setId(int id) {

	}

	public boolean onClick(View view, long position, long id) {
		return false;
	}

}
