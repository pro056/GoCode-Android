package com.example.i317409.gocode;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ContestsPage extends ActionBarActivity implements ContestFragment.OnFragmentInteractionListener, ActionBar.TabListener {


//    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager viewPager;
    SectionPagerAdapter mSectionPagerAdapter;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contests_page);
        final ActionBar acbar = getSupportActionBar();
        acbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        email = getIntent().getStringExtra("email");
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), email);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mSectionPagerAdapter);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                acbar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        String[] conteststatus = {"Running", "Past", "Present"};
        for (int i = 0; i < mSectionPagerAdapter.getCount(); i++) {
            acbar.addTab(acbar.newTab().setText(conteststatus[i]).setTabListener(this));

        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contests_page, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


    @Override
    public void onFragmentInteraction(String id) {

    }
}
class SectionPagerAdapter extends FragmentPagerAdapter {

    String email;
    public SectionPagerAdapter(FragmentManager fm, String email) {

        super(fm);
        this.email = email;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle data = new Bundle();
        data.putString("email", email);
        if (position == 0) {
            data.putString("url", "https://gocode-programmingsport.rhcloud.com/getcurrentcontest/");
        } else if (position == 1) {
            data.putString("url", "https://gocode-programmingsport.rhcloud.com/getpastcontest/");
        } else {
            data.putString("url", "https://gocode-programmingsport.rhcloud.com/getfuturecontest/");
        }
        ContestFragment frag = new ContestFragment();
        frag.setArguments(data);

        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
