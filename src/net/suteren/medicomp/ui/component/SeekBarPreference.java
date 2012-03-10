package net.suteren.medicomp.ui.component;

import java.text.NumberFormat;
import java.util.Locale;

import net.suteren.medicomp.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekBarPreference extends Preference implements
		OnSeekBarChangeListener {

	public float maximum = 100;
	public float interval = 5;

	private float oldValue = 50;
	private TextView monitorBox;
	private float minimum = 0;
	private NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale
			.getDefault());
	private int order = 0;
	private SeekBar bar;

	private String minText = null;
	private String maxText = null;

	public SeekBarPreference(Context context) {
		super(context);
	}

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyAttributes(attrs);
	}

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		applyAttributes(attrs);
	}

	private void applyAttributes(AttributeSet attrs) {
		maximum = attrs.getAttributeFloatValue(
				"http://schemas.android.com/apk/res/net.suteren.medicomp",
				"maximum", maximum);
		interval = attrs.getAttributeFloatValue(
				"http://schemas.android.com/apk/res/net.suteren.medicomp",
				"interval", interval);
		minimum = attrs.getAttributeFloatValue(
				"http://schemas.android.com/apk/res/net.suteren.medicomp",
				"minimum", minimum);
		order = attrs.getAttributeIntValue(
				"http://schemas.android.com/apk/res/android", "order", order);
		minText = translateResource(attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/net.suteren.medicomp",
				"minimumText"));
		maxText = translateResource(attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/net.suteren.medicomp",
				"maximumText"));
	}

	private String translateResource(String text) {
		if (text != null && text.matches("@\\d+")) {
			text = getContext().getResources().getString(
					Integer.parseInt(text.substring(1)));
		}
		return text;
	}

	@Override
	protected View onCreateView(ViewGroup parent) {

		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
				R.layout.seek_bar_preference, parent, false);

		TextView view = (TextView) layout.findViewById(R.id.title);
		view.setText(getTitle());

		view = (TextView) layout.findViewById(R.id.summary);
		view.setText(getSummary());

		bar = (SeekBar) layout.findViewById(R.id.bar);
		bar.setMax(Math.round((maximum - minimum) / interval));
		bar.setProgress(getProgressOfValue(this.oldValue));
		bar.setOnSeekBarChangeListener(this);

		this.monitorBox = (TextView) layout.findViewById(R.id.value);
		setValueText(bar.getProgress());

		bar.setProgress(getProgressOfValue(this.oldValue));

		return layout;
	}

	private void setValueText(float value) {

		Log.d(this.getClass().getCanonicalName(), "Value: " + value + ", min: "
				+ minimum + ", minText: " + minText + ", max: " + maximum
				+ ", maxText: " + maxText);

		if (minText != null && value <= minimum)
			this.monitorBox.setText(minText);
		else if (maxText != null && value >= maximum)
			this.monitorBox.setText(maxText);
		else
			this.monitorBox.setText(numberFormat.format(value));
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		if (!callChangeListener(getValueOfProgress(progress))) {
			seekBar.setProgress(getProgressOfValue(this.oldValue));
			return;
		}

		seekBar.setProgress(progress);
		this.oldValue = getValueOfProgress(progress);
		setValueText(this.oldValue);
		updatePreference(this.oldValue);

	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object onGetDefaultValue(TypedArray ta, int index) {
		float dValue = (float) ta.getFloat(index, 50);
		return validateValue(dValue);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

		float temp;
		if (restoreValue) {
			temp = getPersistedFloat(50);
		} else {
			temp = (Float) defaultValue;
			persistFloat(temp);
		}
		this.oldValue = temp;
	}

	private float validateValue(float value) {

		if (value > maximum)
			value = maximum;
		else if (value < minimum)
			value = minimum;
		else if (value % interval != 0)
			value = Math.round(((float) value) / interval) * interval;

		return value;
	}

	private void updatePreference(float oldValue2) {

		SharedPreferences.Editor editor = getEditor();
		editor.putFloat(getKey(), oldValue2);
		editor.commit();
	}

	private float getValueOfProgress(int progress) {
		return minimum + progress * interval;
	}

	private int getProgressOfValue(float value) {
		return Math.round((value - minimum) / interval);
	}

}
