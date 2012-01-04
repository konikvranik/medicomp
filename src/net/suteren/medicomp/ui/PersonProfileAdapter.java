package net.suteren.medicomp.ui;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Person;
import android.content.ContentValues;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import static net.suteren.medicomp.PersonListActivity.LOG_TAG;

public class PersonProfileAdapter implements ListAdapter {

	private LayoutInflater layoutInflater;
	private Context context;
	private Person person;

	public PersonProfileAdapter(Context context, Person person) {
		if (person == null)
			throw new NullPointerException("Person == null!");
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.person = person;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stubtext
		return 6;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return person;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		Log.d(LOG_TAG, "Item View Type: " + position);
		return Math.min(position, 3);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d(LOG_TAG, "View Position: " + position);
		
		switch (position) {
		case 0:
			if (convertView == null)
				convertView = layoutInflater.inflate(R.layout.name_edit,
						parent, false);
			EditText name = (EditText) convertView.findViewById(R.id.editText1);
			name.setText(person.getName());
			break;

		default:
			if (convertView == null)
				convertView = new View(context);
		}
		// TODO Auto-generated method stub
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

}
