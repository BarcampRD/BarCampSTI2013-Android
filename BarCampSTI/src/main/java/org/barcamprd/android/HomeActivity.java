package org.barcamprd.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.barcamprd.android.fragments.AboutFragment;
import org.barcamprd.android.fragments.HomeFragment;
import org.barcamprd.android.fragments.ScheduleFragment;
import org.barcamprd.android.fragments.SpeakersFragment;
import org.barcamprd.android.fragments.SwipePagerFragment;
import org.barcamprd.android.services.BarCampService;

public class HomeActivity extends ActionBarActivity implements BarCampService.BarCampServiceListener {

    private CollectionPagerAdapter mCollectionPagerAdapter;
    private ViewPager mViewPager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.loading_label));

        BarCampService.getInstance().start(this);


        Log.d("HomeActivity", "Creating instance of Home Activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            BarCampService.getInstance().reload(this);
            mCollectionPagerAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadingStarted() {
        mProgressDialog.show();
    }

    @Override
    public void loadingFinished() {
        mProgressDialog.hide();
        mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
    }

    @Override
    public void loadingFailed() {
        mProgressDialog.hide();
    }

    public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
        SwipePagerFragment[] fragments;
        CharSequence[] titles;

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new SwipePagerFragment[]{new HomeFragment(), new ScheduleFragment(), new SpeakersFragment(), new AboutFragment()};
            for (Fragment f : fragments){
                f.setRetainInstance(true);
            }
            titles = new CharSequence[]{getString(R.string.start_label), getString(R.string.schedule_label), getString(R.string.speakers_label), getString(R.string.about_label)};
        }

        @Override
        public Fragment getItem(int i) {
            return fragments[i];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position].toString().toUpperCase();
        }
    }
}
