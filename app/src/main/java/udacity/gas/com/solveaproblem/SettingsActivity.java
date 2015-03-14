package udacity.gas.com.solveaproblem;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import udacity.gas.com.solveaproblem.data.PailUtilities;
import udacity.gas.com.solveaproblem.utilities.SetupUI;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends ActionBarActivity {

	public static String PREF_FILE_NAME = "settings_file";
	private Switch swDeleteAttachments;
	public static String DELETE_ATTACHMENTS = "del_attach";
	private SettingsActivity mObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		SetupUI setupUI = new SetupUI(this);
		setupUI.setupToolbar();
		//Setup the UI

		swDeleteAttachments = (Switch) findViewById(R.id.delete_attachments);
		mObject = this;
		String sDelAttach = PailUtilities.readFromPreference(mObject, PREF_FILE_NAME, DELETE_ATTACHMENTS, Boolean.toString(false));
		PailUtilities.saveToPreference(mObject, PREF_FILE_NAME, DELETE_ATTACHMENTS, sDelAttach);

		swDeleteAttachments.setChecked(Boolean.parseBoolean(sDelAttach));
		swDeleteAttachments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PailUtilities.saveToPreference(mObject, PREF_FILE_NAME, DELETE_ATTACHMENTS, Boolean.toString(isChecked));
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public Intent getSupportParentActivityIntent() {
		return super.getSupportParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	@Nullable
	@Override
	public Intent getParentActivityIntent() {
		return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}
}
