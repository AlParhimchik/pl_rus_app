package com.example.sashok.lesssoner;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import static com.example.sashok.lesssoner.R.id.fab;

public class MainActivity extends AppCompatActivity  {
    Toolbar toolbar;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ListAdapter adapter;
    CardViewAdapter mAdapter;
    ViewPager mPager;
    ScreenSlidePagerAdapter mPagerAdapter;
    FloatingActionButton add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        add_button=(FloatingActionButton)findViewById(fab);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWordDialogFragment dialogFragment=new AddWordDialogFragment(MainActivity.this);
                dialogFragment.getWindow().getAttributes().windowAnimations=R.style.RegistrationDialogAnimation;
                dialogFragment.show();
            }
        });
        mPager = (ViewPager) findViewById(R.id.view_pager_card_view);
        String [] data=new String[] {"Hello","blaa","bee","qqq"};
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),data);
        mPager.setAdapter(mPagerAdapter);

        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        final ImageView success=(ImageView)findViewById(R.id.succcess_btn);
        final ImageView error=(ImageView)findViewById(R.id.error_btn);
        final Animation disapear = AnimationUtils.loadAnimation(this, R.anim.disapear_in);
        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currPos=mPager.getCurrentItem();
                mPager.setCurrentItem(currPos+1);
                 error.startAnimation(disapear);
            }
        });

        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currPos=mPager.getCurrentItem();
                mPager.setCurrentItem(currPos+1);
                success.startAnimation(disapear);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                final RelativeLayout view = (RelativeLayout) findViewById(R.id.view_pager_layout);
                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_right);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(View.INVISIBLE);
                        add_button.setVisibility(View.VISIBLE);
                    }
                });

                view.startAnimation(animation);
                return true;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (null != searchManager) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search_hint));
        return  true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.search){
            final RelativeLayout view=(RelativeLayout)findViewById(R.id.view_pager_layout);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            //use this to make it longer:  animation.setDuration(1000);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationRepeat(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.VISIBLE);
                    add_button.setVisibility(View.INVISIBLE);
                }
            });

            view.startAnimation(animation);
            return true;
        }
        return false;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        String[] dataset;
        public ScreenSlidePagerAdapter(FragmentManager fm,String [] dataset) {
            super(fm);
            this.dataset=dataset;

        }

        @Override
        public Fragment getItem(int position) {
            return new CardViewFragment(dataset[position]);
        }

        @Override
        public int getCount() {
            return dataset.length;
        }
    }
}

