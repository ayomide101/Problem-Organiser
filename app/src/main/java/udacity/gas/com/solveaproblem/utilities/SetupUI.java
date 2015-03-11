package udacity.gas.com.solveaproblem.utilities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.fragments.attach.AudiosFragment;
import udacity.gas.com.solveaproblem.fragments.attach.FilesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.ImagesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.LinksFragment;
import udacity.gas.com.solveaproblem.fragments.attach.NotesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.RelevantAttachmentFragment;
import udacity.gas.com.solveaproblem.fragments.attach.VideosFragment;
import udacity.gas.com.solveaproblem.fragments.home.DefaultFragment;

/**
 * Created by Fagbohungbe on 05/03/2015.
 */
public class SetupUI implements MaterialTabListener {

	private Toolbar toolbar;
	private Activity mAcivity;
	private ActionBarActivity mActionBarActivity;
	private MaterialTabHost mTabs;
	private ViewPager mPager;
	private TabsAdapter mPagerAdapter;
	private ArrayList<Fragment> mFragments;

	public SetupUI(Activity activity) {
		mAcivity = activity;
		mActionBarActivity = (ActionBarActivity) mAcivity;
	}

	public void setupToolbarModify() {
		//Toolbar
		toolbar = (Toolbar) mActionBarActivity.findViewById(R.id.app_bar);
		mActionBarActivity.setSupportActionBar(toolbar);
		mActionBarActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
		mActionBarActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mActionBarActivity.getSupportActionBar().setHomeButtonEnabled(true);
		mActionBarActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_cancel);
	}

	public void setupToolbar() {
		//Toolbar
		toolbar = (Toolbar) mActionBarActivity.findViewById(R.id.app_bar);
		mActionBarActivity.setSupportActionBar(toolbar);
		mActionBarActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void setupTabs() {
		mTabs = (MaterialTabHost) mActionBarActivity.findViewById(R.id.tabs);
		mPager = (ViewPager) mActionBarActivity.findViewById(R.id.pager);
		mPagerAdapter = new TabsAdapter(mActionBarActivity.getSupportFragmentManager());
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

	public void setupTabs(long problemid) {
		mTabs = (MaterialTabHost) mActionBarActivity.findViewById(R.id.tabs);
		mPager = (ViewPager) mActionBarActivity.findViewById(R.id.pager);
		mPagerAdapter = new TabsAdapter(mActionBarActivity.getSupportFragmentManager());
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
		mFragments.add(RelevantAttachmentFragment.ID, RelevantAttachmentFragment.getInstance(problemid));
		mFragments.add(NotesFragment.ID, NotesFragment.getInstance(problemid));
		mFragments.add(LinksFragment.ID, LinksFragment.getInstance(problemid));
		mFragments.add(ImagesFragment.ID, ImagesFragment.getInstance(problemid));
		mFragments.add(VideosFragment.ID, VideosFragment.getInstance(problemid));
		mFragments.add(AudiosFragment.ID, AudiosFragment.getInstance(problemid));
		mFragments.add(FilesFragment.ID, FilesFragment.getInstance(problemid));
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

	class TabsAdapter extends FragmentStatePagerAdapter {

		String[] tabs;

		public TabsAdapter(FragmentManager fm) {
			super(fm);
			tabs = mActionBarActivity.getResources().getStringArray(R.array.attach_activity_tabs);
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
			return 3;
		}
	}

	public Toolbar getToolbar() {
		return toolbar;
	}

	public Activity getmAcivity() {
		return mAcivity;
	}

	public ActionBarActivity getmActionBarActivity() {
		return mActionBarActivity;
	}

	public MaterialTabHost getmTabs() {
		return mTabs;
	}

	public ViewPager getmPager() {
		return mPager;
	}

	public TabsAdapter getmPagerAdapter() {
		return mPagerAdapter;
	}

	public ArrayList<Fragment> getmFragments() {
		return mFragments;
	}
}