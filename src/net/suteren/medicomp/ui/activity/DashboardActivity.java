package net.suteren.medicomp.ui.activity;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import net.suteren.medicomp.R;
import net.suteren.medicomp.domain.Category;
import net.suteren.medicomp.domain.Field;
import net.suteren.medicomp.domain.Record;
import net.suteren.medicomp.domain.Type;
import net.suteren.medicomp.ui.adapter.DashboardAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class DashboardActivity extends MedicompActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (!setupPerson()) {
			redirectToPersonList();
			return;
		}

	}

	@Override
	protected ListAdapter getAdapter() {
		try {
			Log.d(LOG_TAG, "Calling getAdapter: " + person.getId() + ", "
					+ person.getName());
			return new DashboardAdapter(DashboardActivity.this, person);
		} catch (SQLException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected ListView requestListView() {
		return (ListView) getWindow().findViewById(R.id.dashboard);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.dashboard;
	}
}
