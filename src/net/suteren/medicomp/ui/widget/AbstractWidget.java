package net.suteren.medicomp.ui.widget;

import net.suteren.medicomp.domain.Person;
import android.content.Context;
import android.view.LayoutInflater;

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
}
