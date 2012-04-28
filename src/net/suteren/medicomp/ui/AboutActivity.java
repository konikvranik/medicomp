package net.suteren.medicomp.ui;

import net.suteren.medicomp.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		displayVersionName();
	}

	private void displayVersionName() {
		String versionName = "";
		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(),
					0);
			versionName = "v " + packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		TextView tv = (TextView) findViewById(R.id.versionNameTextView);
		tv.setText(versionName);
	}

}
