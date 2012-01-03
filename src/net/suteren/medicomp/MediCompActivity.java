package net.suteren.medicomp;

import net.suteren.medicomp.ui.PersonAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

public class MediCompActivity extends Activity {
	public static String LOG_TAG = "MEDICOMP";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ListView listView = (ListView) getWindow()
				.findViewById(R.id.personList);

		try {
			listView.setAdapter(new PersonAdapter(getApplicationContext()));
		} catch (Exception e) {
			Log.e(LOG_TAG, "Failed: ", e);
		}

	}

}