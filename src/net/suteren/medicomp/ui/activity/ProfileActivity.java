package net.suteren.medicomp.ui.activity;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Person;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class ProfileActivity extends MedicompActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {

			super.onCreate(savedInstanceState);

			if (!setupPerson()) {
				person = new Person();
			}

			ListAdapter adapter = getAdapter();
			if (adapter != null)
				listView.setAdapter(adapter);

			Button okButton = (Button) getWindow().findViewById(
					R.id.save_button);

			okButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onSaveEvent(v);
				}
			});

			Button cancelButton = (Button) getWindow().findViewById(
					R.id.cancel_button);
			cancelButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onCancelEvent(v);
				}
			});

			Button deleteButton = (Button) getWindow().findViewById(
					R.id.delete_button);
			deleteButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onDeleteEvent(v);
				}
			});

		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Failed: ", e);
		}

	}

	@Override
	protected ListView requestListView() {
		return (ListView) getWindow().findViewById(R.id.profile);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.profile;
	}

	protected abstract void onCancelEvent(View v);

	protected abstract void onSaveEvent(View v);

	protected abstract void onDeleteEvent(View v);

}
