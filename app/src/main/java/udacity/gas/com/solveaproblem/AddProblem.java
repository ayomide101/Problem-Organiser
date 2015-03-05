package udacity.gas.com.solveaproblem;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.Date;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.data.PailDbHelper;
import udacity.gas.com.solveaproblem.data.PailUtilities;
import udacity.gas.com.solveaproblem.fragments.attach.AudiosFragment;
import udacity.gas.com.solveaproblem.fragments.attach.FilesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.ImagesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.LinksFragment;
import udacity.gas.com.solveaproblem.fragments.attach.NotesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.RelevantAttachmentFragment;
import udacity.gas.com.solveaproblem.fragments.attach.VideosFragment;
import udacity.gas.com.solveaproblem.fragments.home.DefaultFragment;


public class AddProblem extends ActionBarActivity implements MaterialTabListener, ViewStub.OnClickListener {

	private Toolbar toolbar;

	private SlidingDrawer slidingDrawer;
	private ImageButton bt_close_attach_window;
	private ImageButton bt_confirm_attach_window;
	private LinearLayout handle;

	private ViewPager mPager;
	private MaterialTabHost mTabs;
	private ArrayList<Fragment> mFragments;
	private TabsAdapter mPagerAdapter;
	private SQLiteDatabase writer;

	private Switch swPrivacy;
	private int etPrivacy;
	private EditText etDescription;
	private EditText etTitle;
	private int etProblemStatus;
	public static final String TAG_NAME = AddProblem.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_problem);
		//Setup Db
		setupDb();
		//Toolbar
		setupToolbar();
		//Drawer
		setupDrawer();
		//Setup Tabs
		setupTabs();
		//Setup Form
		setupForm();
	}

	private void setupDb () {
		writer = new PailDbHelper(this).getWritableDatabase();
	}

	private void setupForm() {
		swPrivacy = (Switch) findViewById(R.id.swPrivacy);
		swPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					etPrivacy = PailContract.ProblemEntry.VAL_PRIVACY_PRIVATE;
				} else {
					etPrivacy = PailContract.ProblemEntry.VAL_PRIVACY_PUBLIC;
				}
			}
		});
		etDescription = (EditText) findViewById(R.id.etDescription);
		etTitle = (EditText) findViewById(R.id.etTitle);
		etProblemStatus = PailContract.ProblemEntry.VAL_PROBLEM_STATUS_PENDING;
	}

	/*Adds the problem to the database*/
	private void createProblem() {
		//Show loading screen and perform query
		ContentValues cn = new ContentValues();
		cn.put(PailContract.ProblemEntry.COLUMN_TITLE, etTitle.getText().toString());
		cn.put(PailContract.ProblemEntry.COLUMN_DESCRIPTION, etDescription.getText().toString());
		cn.put(PailContract.ProblemEntry.COLUMN_PRIVACY, etPrivacy);
		cn.put(PailContract.ProblemEntry.COLUMN_PROBLEM_STATUS, etProblemStatus);
		cn.put(PailContract.ProblemEntry.COLUMN_DATE, new Date().getTime());
		cn.put(PailContract.ProblemEntry.COLUMN_DATE_MODIFIED, new Date().getTime());


		//Use cursor to insert into data
		getContentResolver().insert(PailContract.ProblemEntry.buildProblemsUri(), cn);

		Cursor probCursor = getContentResolver()
				.query(
						PailContract.ProblemEntry.buildProblemsUri(),
						null,
						null,
						null,
						null,
						null
						);

		if (probCursor.moveToFirst()) {
			int id = probCursor.getColumnIndex(PailContract.ProblemEntry._ID);
			Log.e(TAG_NAME, "Insert Id : "+id);
			Toast.makeText(this, "Problem has been added", Toast.LENGTH_LONG).show();
			//finish the activity
			finish();
		} else {
			Log.e(TAG_NAME, "Insert using content provider failed");
		}
	}

	/*Attaches the attaches to the problem with or the attachment id*/
	private void confirmAttach() {
		//Perform attach here
	}

	private void setupToolbar() {
		//Toolbar
		toolbar = (Toolbar) findViewById(R.id.app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_cancel);
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
		}

		//Close the sliding drawer with the custom x button
		bt_close_attach_window.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingDrawer.animateClose();
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
			}
		});
		final AddProblem activity = this;
		slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				bt_close_attach_window.setVisibility(View.VISIBLE);
				bt_confirm_attach_window.setVisibility(View.VISIBLE);
				slidingDrawer.lock(); //Do not allow the main holder to be clickable
				handle.setClickable(false);
				//Hide the keyboard
				PailUtilities.hideKeyBoardFromScreen(activity, etDescription);
			}
		});
	}

	private void setupTabs() {
		mTabs = (MaterialTabHost) findViewById(R.id.tabs);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new TabsAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mTabs.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			mTabs.addTab(mTabs.newTab().setText(mPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

		//The arraylist of the mFragments
		mFragments = new ArrayList<Fragment>(7);
		mFragments.add(RelevantAttachmentFragment.ID, new RelevantAttachmentFragment());
		mFragments.add(NotesFragment.ID, new NotesFragment());
		mFragments.add(LinksFragment.ID, new LinksFragment());
		mFragments.add(ImagesFragment.ID, new ImagesFragment());
		mFragments.add(VideosFragment.ID, new VideosFragment());
		mFragments.add(AudiosFragment.ID, new AudiosFragment());
		mFragments.add(FilesFragment.ID, new FilesFragment());
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
			case android.R.id.home:
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
	public void onTabSelected(MaterialTab materialTab) {
		mPager.setCurrentItem(materialTab.getPosition());
	}

	@Override
	public void onTabReselected(MaterialTab materialTab) {

	}

	@Override
	public void onTabUnselected(MaterialTab materialTab) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == bt_confirm_attach_window.getId()) {
			confirmAttach();
		}
	}

	class TabsAdapter extends FragmentStatePagerAdapter {

		String[] tabs;

		public TabsAdapter(FragmentManager fm) {
			super(fm);
			tabs = getResources().getStringArray(R.array.attach_activity_tabs);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tabs[position];
		}

		@Override
		public Fragment getItem(int position) {
			//if the user is not on the home page, show the fragment page
			if (position == RelevantAttachmentFragment.ID) {
				return mFragments.get(RelevantAttachmentFragment.ID);

			} else if (position == ImagesFragment.ID) {
				return mFragments.get(ImagesFragment.ID);

			} else if (position == AudiosFragment.ID) {
				return mFragments.get(AudiosFragment.ID);

			} else if (position == LinksFragment.ID) {
				return mFragments.get(LinksFragment.ID);

			} else if (position == NotesFragment.ID) {
				return mFragments.get(NotesFragment.ID);

			} else if (position == VideosFragment.ID) {
				return mFragments.get(VideosFragment.ID);

			} else if (position == FilesFragment.ID)	{
				return mFragments.get(FilesFragment.ID);
			} else {
				return DefaultFragment.getInstance(position);
			}
		}

		@Override
		public int getCount() {
			return 7;
		}
	}
}