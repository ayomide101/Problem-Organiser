package udacity.gas.com.solveaproblem;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.Switch;
import android.widget.Toast;

import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.data.PailUtilities;
import udacity.gas.com.solveaproblem.utilities.SetupUI;


public class AddProblem extends ActionBarActivity implements ViewStub.OnClickListener {

	private SlidingDrawer slidingDrawer;
	private ImageButton bt_close_attach_window;
	private ImageButton bt_confirm_attach_window;
	private LinearLayout handle;

	private Switch swPrivacy;
	private int etPrivacy;
	private EditText etDescription;
	private EditText etTitle;
	private int etProblemStatus;
	public static final String TAG_NAME = AddProblem.class.getSimpleName();
	private SetupUI ui;
	private long PROBLEM_ID = PailContract.ProblemEntry.generateProblemId();

	private boolean mProblemSaved = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_problem);
		//Setup toolbar
		ui = new SetupUI(this);
		ui.setupToolbar();
		ui.setupTabs(PROBLEM_ID);
		//setup drawer
		setupDrawer();
		//SetupUI Form
		setupForm();
	}

	private void setupForm() {
		swPrivacy = (Switch) findViewById(R.id.swPrivacy);
		swPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					etPrivacy = PailContract.VAL_PRIVACY_PRIVATE;
				} else {
					etPrivacy = PailContract.VAL_PRIVACY_PUBLIC;
				}
			}
		});
		etDescription = (EditText) findViewById(R.id.etDescription);
		etTitle = (EditText) findViewById(R.id.etTitle);
		etProblemStatus = PailContract.VAL_PROBLEM_STATUS_PENDING;
	}

	/*Adds the problem to the database*/
	//call finish
	private void createProblem() {
		//Show loading screen and perform query
		ContentValues cn = getData();
		if (mProblemSaved) {
			mProblemSaved = true;
			updateProblem();
			return;
		}
		//problem saved
		//Use cursor to insert into data
		getContentResolver().insert(PailContract.ProblemEntry.buildProblemsUri(), cn);
		Cursor probCursor = getContentResolver().query(PailContract.ProblemEntry.buildProblemsUri(), null, null, null, null, null);
		if (probCursor.moveToFirst()) {
			Toast.makeText(this, "Problem has been added", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Problem could not be added", Toast.LENGTH_SHORT).show();
		}
		finish();
	}

	//call finish
	private void updateProblem() {
		//Show loading screen and perform query
		ContentValues cn = getData();
		//problem not saved
		if (!mProblemSaved) {
			mProblemSaved = false;
			createProblem();
			return;
		}
		//Use cursor to insert into data
		int d = getContentResolver().update(PailContract.ProblemEntry.buildProblemWithIdUri(PROBLEM_ID), cn , null, null);
		if (d != -1) {
			Toast.makeText(this, "Problem has been updated", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Problem could not be updated", Toast.LENGTH_SHORT).show();
		}
		finish();
	}

	private void createProblemWithoutFinish() {
		//Show loading screen and perform query
		if (mProblemSaved) { //Problem has previously been saved
			mProblemSaved = true;
			updateProblemWithoutFinish();
			return;
		}
		ContentValues cn = getData();
		//Use cursor to insert into data
		getContentResolver().insert(PailContract.ProblemEntry.buildProblemsUri(), cn);
		Cursor probCursor = getContentResolver().query(PailContract.ProblemEntry.buildProblemsUri(), null, null, null, null, null);
		if (probCursor.moveToFirst()) {
			mProblemSaved = true;
			Toast.makeText(this, "Problem has been added", Toast.LENGTH_SHORT).show();
			//finish the activity
		} else {
			mProblemSaved = false;
			Log.e(TAG_NAME, "Insert using content provider failed");
		}
	}

	private void updateProblemWithoutFinish() {
		//Show loading screen and perform query
		if (!mProblemSaved) {
			mProblemSaved = false;
			createProblemWithoutFinish();
			return;
		}
		ContentValues cn = getData();
		//Use cursor to insert into data
		int d = getContentResolver().update(PailContract.ProblemEntry.buildProblemWithIdUri(PROBLEM_ID), cn, null, null);
		if (d != -1) {
			mProblemSaved = true;
			Toast.makeText(this, "Problem has been updated", Toast.LENGTH_SHORT).show();
		} else {
			mProblemSaved = false;
			Toast.makeText(this, "Problem could not be updated", Toast.LENGTH_SHORT).show();
		}
	}

	private ContentValues getData () {
		ContentValues cn = new ContentValues();
		cn.put(PailContract.ProblemEntry.COLUMN_PROB_ID, PROBLEM_ID);
		cn.put(PailContract.ProblemEntry.COLUMN_PRIVACY, etPrivacy);
		cn.put(PailContract.ProblemEntry.COLUMN_PROBLEM_STATUS, etProblemStatus);
		if (etTitle.getText().length() <= 0) {
			cn.put(PailContract.ProblemEntry.COLUMN_TITLE, "p");
		} else {
			cn.put(PailContract.ProblemEntry.COLUMN_TITLE, etTitle.getText().toString());
		}
		if (etTitle.getText().length() <= 0) {
			cn.put(PailContract.ProblemEntry.COLUMN_DESCRIPTION, "d");
		} else {
			cn.put(PailContract.ProblemEntry.COLUMN_DESCRIPTION, etDescription.getText().toString());
		}
		return cn;
	}

	/*Attaches the attaches to the problem with or the attachment id*/
	private void confirmAttach() {
		//Perform attach here
	}

	private void setupDrawer() {
		slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
		bt_close_attach_window = (ImageButton) findViewById(R.id.bt_close_attach_window);
		bt_confirm_attach_window = (ImageButton) findViewById(R.id.bt_confirm_attach_window);
		handle = (LinearLayout) findViewById(R.id.handle);

		//Do not show the slidingDrawer
		if (slidingDrawer.isOpened()) {
			slidingDrawer.toggle();
			bt_close_attach_window.setVisibility(View.GONE);
			bt_confirm_attach_window.setVisibility(View.GONE);
			hideKeyboard();
		}

		//Close the sliding drawer with the custom x button
		bt_close_attach_window.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingDrawer.animateClose();
				hideKeyboard();
			}
		});

		bt_confirm_attach_window.setOnClickListener(this);
		slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				bt_close_attach_window.setVisibility(View.GONE);
				bt_confirm_attach_window.setVisibility(View.GONE);
				slidingDrawer.unlock();
				handle.setClickable(true);
				//Hide the keyboard
				hideKeyboard();
			}
		});
		slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				bt_close_attach_window.setVisibility(View.VISIBLE);
				bt_confirm_attach_window.setVisibility(View.VISIBLE);
				slidingDrawer.lock(); //Do not allow the main holder to be clickable
				handle.setClickable(false);
				//Hide the keyboard
				hideKeyboard();
				//save or create the problem
				createProblemWithoutFinish();
			}
		});
	}

	private void hideKeyboard() {
		PailUtilities.hideKeyBoardFromScreen(this, etDescription);
		PailUtilities.hideKeyBoardFromScreen(this, etTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_add_problem, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();

		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_settings:
				return true;
			case R.id.btAdd:
				createProblem();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		//check if the sliding drawer is opened
		//if user presses back when the sliding drawer is opened
		//close sliding drawer
		if (slidingDrawer.isOpened()) {
			slidingDrawer.animateClose();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == bt_confirm_attach_window.getId()) {
			confirmAttach();
		}
	}
}