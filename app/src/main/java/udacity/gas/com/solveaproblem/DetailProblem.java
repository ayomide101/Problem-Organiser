package udacity.gas.com.solveaproblem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.fragments.attach.LinksFragment;
import udacity.gas.com.solveaproblem.fragments.attach.NotesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.RelevantAttachmentFragment;
import udacity.gas.com.solveaproblem.fragments.detail.DetailFragment;
import udacity.gas.com.solveaproblem.fragments.home.DefaultFragment;
import udacity.gas.com.solveaproblem.fragments.home.HomeFragment;
import udacity.gas.com.solveaproblem.utilities.SetupUI;

public class DetailProblem extends ActionBarActivity implements MaterialTabListener {

	private MaterialTabHost mTabs;
	private ViewPager mPager;
	private TabsAdapter mPagerAdapter;
	private ArrayList<Fragment> mFragments = new ArrayList<Fragment>(7);
	private int _id_detail_fragment = 0;
	private int _id_relevant_fragment = 1;
	private int _id_notes_fragment = 2;
	private int _id_links_fragment = 3;
	private int _id_images_fragment = 4;
	private int _id_videos_fragment = 5;
	private int _id_audios_fragment = 6;
	private int _id_files_fragment = 7;
	private long mProblemid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_problem);
		Intent intent = getIntent();
		mProblemid = intent.getLongExtra(HomeFragment.EXTRA_ID, 0);
		SetupUI setupUI = new SetupUI(this);
		setupUI.setupToolbar();
		setupTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_detail_problem, menu);
		return true;
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
		if (id == R.id.action_edit) {
			Intent intent = new Intent(this, EditProblem.class);
			intent.putExtra(HomeFragment.EXTRA_ID, mProblemid);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_delete) {
			getContentResolver()
				.delete(PailContract.ProblemEntry.buildProblemWithIdUri(mProblemid), null, null);
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(MaterialTab materialTab) { mPager.setCurrentItem(materialTab.getPosition()); }

	@Override
	public void onTabReselected(MaterialTab materialTab) {}

	@Override
	public void onTabUnselected(MaterialTab materialTab) {}

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
		setupArrayTabs();
	}

	private void setupArrayTabs() {
		mFragments.add(_id_detail_fragment, DetailFragment.getInstance(mProblemid));
		mFragments.add(_id_relevant_fragment, RelevantAttachmentFragment.getInstance(mProblemid));
		mFragments.add(_id_notes_fragment, NotesFragment.getInstance(mProblemid));
		mFragments.add(_id_links_fragment, LinksFragment.getInstance(mProblemid));
//		mFragments.add(_id_images_fragment, ImagesFragment.getInstance(mProblemid));
//		mFragments.add(_id_videos_fragment, VideosFragment.getInstance(mProblemid));
//		mFragments.add(_id_audios_fragment, AudiosFragment.getInstance(mProblemid));
//		mFragments.add(_id_files_fragment, FilesFragment.getInstance(mProblemid));
	}

	class TabsAdapter extends FragmentStatePagerAdapter {

		String[] tabs;

		public TabsAdapter(FragmentManager fm) {
			super(fm);
			tabs = getResources().getStringArray(R.array.detail_activity_tabs);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tabs[position];
		}

		@Override
		public Fragment getItem(int position) {
			//if the user is not on the home page, show the fragment page
			if (position == _id_detail_fragment) {
				return mFragments.get(_id_detail_fragment);
			} else if (position == _id_relevant_fragment) {
				return mFragments.get(_id_relevant_fragment);
//			} else if (position == _id_images_fragment) {
//				return mFragments.get(_id_images_fragment);
//			} else if (position == _id_audios_fragment) {
//				return mFragments.get(_id_audios_fragment);
			} else if (position == _id_links_fragment) {
				return mFragments.get(_id_links_fragment);
			} else if (position == _id_notes_fragment) {
				return mFragments.get(_id_notes_fragment);
//			} else if (position == _id_videos_fragment) {
//				return mFragments.get(_id_videos_fragment);
//			} else if (position == _id_files_fragment)	{
//				return mFragments.get(_id_files_fragment);
			} else {
				return DefaultFragment.getInstance(position);
			}
		}

		@Override
		public int getCount() {
			return 4;
		}
	}
}
