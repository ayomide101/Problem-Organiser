package udacity.gas.com.solveaproblem.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.fragments.attach.AudiosFragment;
import udacity.gas.com.solveaproblem.fragments.attach.FilesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.ImagesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.LinksFragment;
import udacity.gas.com.solveaproblem.fragments.attach.NotesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.RelevantAttachmentFragment;
import udacity.gas.com.solveaproblem.fragments.attach.VideosFragment;
import udacity.gas.com.solveaproblem.fragments.detail.DetailFragment;
import udacity.gas.com.solveaproblem.fragments.home.DefaultFragment;

/**
 * Created by Fagbohungbe on 09/03/2015.
 */
public class MainDetailFragment extends Fragment implements MaterialTabListener {

	private String[] tabs;
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

	public static MainDetailFragment getInstance(long problemId) {
		MainDetailFragment myFragment = new MainDetailFragment();
		Bundle args = new Bundle();
		args.putLong(PailContract.ProblemEntry.BUNDLE_KEY, problemId);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tabs = getResources().getStringArray(R.array.detail_activity_tabs);
		Log.e("MainDetailFragment", tabs.toString());
		setupTabs();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if (bundle != null) {
			mProblemid = bundle.getLong(PailContract.ProblemEntry.BUNDLE_KEY);
		}
		setRetainInstance(true);
		return inflater.inflate(R.layout.fragment_main_detail, container, false);
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

	private void setupTabs() {
		mTabs = (MaterialTabHost) getActivity().findViewById(R.id.tabs);
		mPager = (ViewPager) getActivity().findViewById(R.id.pager);
		mPagerAdapter = new TabsAdapter(getActivity().getSupportFragmentManager());
		//The arraylist of the mFragments
		setupArrayTabs();
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
	}

	private void setupArrayTabs() {
		mFragments.add(_id_detail_fragment, DetailFragment.getInstance(mProblemid));
		mFragments.add(_id_relevant_fragment, RelevantAttachmentFragment.getInstance(mProblemid));
		mFragments.add(_id_notes_fragment, NotesFragment.getInstance(mProblemid));
		mFragments.add(_id_links_fragment, LinksFragment.getInstance(mProblemid));
		mFragments.add(_id_images_fragment, ImagesFragment.getInstance(mProblemid));
		mFragments.add(_id_videos_fragment, VideosFragment.getInstance(mProblemid));
		mFragments.add(_id_audios_fragment, AudiosFragment.getInstance(mProblemid));
		mFragments.add(_id_files_fragment, FilesFragment.getInstance(mProblemid));
	}

	class TabsAdapter extends FragmentStatePagerAdapter {

		public TabsAdapter(FragmentManager fm) {
			super(fm);
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
			} else if (position == _id_images_fragment) {
				return mFragments.get(_id_images_fragment);
			} else if (position == _id_audios_fragment) {
				return mFragments.get(_id_audios_fragment);
			} else if (position == _id_links_fragment) {
				return mFragments.get(_id_links_fragment);
			} else if (position == _id_notes_fragment) {
				return mFragments.get(_id_notes_fragment);
			} else if (position == _id_videos_fragment) {
				return mFragments.get(_id_videos_fragment);
			} else if (position == _id_files_fragment)	{
				return mFragments.get(_id_files_fragment);
			} else {
				return DefaultFragment.getInstance(position);
			}
		}

		@Override
		public int getCount() {
			return 8;
		}
	}
}