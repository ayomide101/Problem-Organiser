package udacity.gas.com.solveaproblem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import udacity.gas.com.solveaproblem.data.PailDbHelper;
import udacity.gas.com.solveaproblem.drawer.NavigationDrawerFragment;
import udacity.gas.com.solveaproblem.fragments.home.AttachmentFragment;
import udacity.gas.com.solveaproblem.fragments.home.DefaultFragment;
import udacity.gas.com.solveaproblem.fragments.home.HomeFragment;
import udacity.gas.com.solveaproblem.fragments.main.MainDetailFragment;

public class MainActivity extends ActionBarActivity implements MaterialTabListener, HomeFragment.Callback {

	private final static String TAG_NAME = "MainActivity";

    private Toolbar toolbar;
    private ViewPager mPager;
	private boolean mTowPane = false;

	public Toolbar getToolbar() {
		return toolbar;
	}
	private MaterialTabHost mTabs;
	private ArrayList<Fragment> mFragments;
	private static String DETAIL_FRAGMENT_TAG = "1";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		setupDb();
        setupToolbar();
		setupDrawer();
		setupTabs();
		if (findViewById(R.id.main_detail_fragment) != null) {
			mTowPane = true;
			if (savedInstanceState == null) {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_detail_fragment, new MainDetailFragment(), DETAIL_FRAGMENT_TAG).commit();
			}
		} else {
			mTowPane = false;
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	private void setupDb() {
		PailDbHelper mSqLiteDatabase = new PailDbHelper(this);
		mSqLiteDatabase.getReadableDatabase();
	}

	private void setupTabs() {
		mTabs = (MaterialTabHost) findViewById(R.id.tabs);
		mPager = (ViewPager) findViewById(R.id.pager);
		HomeTabsAdapter mPagerAdapter = new HomeTabsAdapter(getSupportFragmentManager());
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
		mFragments = new ArrayList<>(2);
	}

	private void setupDrawer() {
		//SetupUI a drawer fragment
		NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
	}

	private void setupToolbar() {
		//Toolbar
		toolbar = (Toolbar) findViewById(R.id.app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
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
	public void onItemClicked(long probid) {
		if (mTowPane) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.main_detail_fragment, MainDetailFragment.getInstance(probid), DETAIL_FRAGMENT_TAG)
					.commit();
			;
		} else {
			Intent intent = new Intent(this, DetailProblem.class);
			intent.putExtra(HomeFragment.EXTRA_ID, probid);
			startActivity(intent);
		}
	}

	class HomeTabsAdapter extends FragmentStatePagerAdapter {

        String[] tabs;

        public HomeTabsAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {
            //if the user is not on the home page, show the fragment page
            if (position == AttachmentFragment.ID) {
				if (mFragments.contains(new AttachmentFragment())) {
					return mFragments.get(AttachmentFragment.ID);
				} else {
					AttachmentFragment attachmentFragment = AttachmentFragment.getInstance(position);
					mFragments.add(AttachmentFragment.ID, attachmentFragment);
					return attachmentFragment;
				}
            } else if (position == HomeFragment.ID) {
				if (mFragments.contains(new HomeFragment())) {
					return mFragments.get(HomeFragment.ID);
				} else {
					HomeFragment homeFragment = HomeFragment.getInstance(position);
					mFragments.add(HomeFragment.ID, homeFragment);
					return homeFragment;
				}
            } else {
                return DefaultFragment.getInstance(position);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
