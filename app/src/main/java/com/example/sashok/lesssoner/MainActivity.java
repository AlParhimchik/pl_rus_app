package com.example.sashok.lesssoner;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.sashok.lesssoner.R.id.fab;
import static com.example.sashok.lesssoner.R.id.view_pager_layout;

public class MainActivity extends AppCompatActivity  {
    Toolbar toolbar;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ListAdapter adapter;
    CardViewAdapter mAdapter;
    ViewPager mPager;
    ScreenSlidePagerAdapter mPagerAdapter;
    FloatingActionButton add_button;
    HashMap<Integer,CardViewFragment> mPageReferenceMap;
    List<Word> words;
    SQLiteDatabase chatDBlocal;
    Language currentLanguage;
    public final String DB_NAME="words.db";
    public final String CREATE_PL_WORDS_TABLE="CREATE TABLE IF NOT EXISTS "+ Word.TABLE_NAME_PL_WORD +
            " (_id integer primary key  AUTOINCREMENT unique,"+ Word.TABLE_COLUMN_NAME_PL_WORD+" text not null,"+ Word.TABLE_COLUMN_PL_FAVOURITE+" INTEGER DEFAULT 0,"+ Word.TABLE_COLUMN_DEGREE+" INTEGER DEFAULT 0 )";
    public final String CREATE_RUS_WORDS_TABLE="CREATE TABLE IF NOT EXISTS "+ Word.TABLE_NAME_RUS_WORD +
            " (_id integer primary key AUTOINCREMENT unique ,"+ Word.TABLE_COLUMN_NAME_RUS_WORD+" text not null,"+Word.TABLE_COLUMN_PL_ID+" Integer not null,"+
            " CONSTRAINT receive_key FOREIGN key("+Word.TABLE_COLUMN_PL_ID+") REFERENCES "+Word.TABLE_NAME_PL_WORD+"("+Word.TABLE_COLUMN_NAME_ID+") ON DELETE SET NULL)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StoreWords store=new StoreWords();
        store.execute();
        currentLanguage=Language.Polish;
        mPageReferenceMap=new HashMap<>();
        if (savedInstanceState!=null) {
            mPageReferenceMap= (HashMap<Integer,CardViewFragment>)savedInstanceState.getSerializable("FRAGMENTS");
            Boolean lang=savedInstanceState.getBoolean("language");
            if (lang) currentLanguage=Language.Polish;
            else currentLanguage=Language.Russian;
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("FRAGMENTS",(Serializable)mPageReferenceMap);
        Boolean lang=false;
        if (currentLanguage==Language.Polish) lang=true;
        outState.putBoolean("language",lang);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        if (currentLanguage==Language.Polish) menu.findItem(R.id.ru_pl).setIcon(R.drawable.russia);
        else
            menu.findItem(R.id.ru_pl).setIcon(R.drawable.poland);

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
        if (item.getItemId()==R.id.ru_pl){
            if (item.getIcon().getConstantState().equals(ResourcesCompat.getDrawable(getResources(), R.drawable.poland, null).getConstantState())){
                mPagerAdapter.hello(Language.Polish);
                item.setIcon(R.drawable.russia);
                currentLanguage=Language.Polish;
            }

            else {
                item.setIcon(R.drawable.poland);
                mPagerAdapter.hello(Language.Russian);
                currentLanguage=Language.Russian;
            }
            return true;
        }
        return false;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<Word> words;
        FragmentManager fm;
        public ScreenSlidePagerAdapter(FragmentManager fm,List<Word> words) {
            super(fm);
            this.words=words;
            this.fm=fm;

        }
        public void hello(Language lang)
        {

        CardViewFragment fr=findFragment(mPager.getCurrentItem());
            if (fr!=null){
                fr.hello(lang);
            }

//            FragmentManager fm = getSupportFragmentManager();
//            if (fm != null) {
//                List<Fragment> fragments = fm.getFragments();
//
//                for(int i = fragments.size()-1 ; i >= 0; i--){
//                    Fragment fragment = fragments.get(i);
//                    if(fragment != null) {
//                        // found the current fragment
////                        ((CardViewFragment)fragment).hello(lang);
//                        // if you want to check for specific fragment class
//                        break;
//                    }
//                }
            //}


        }

        public CardViewFragment findFragment(int position){
            return mPageReferenceMap.get(position);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
            Log.i("TAG","delete");
        }

        @Override
        public Fragment getItem(int position) {

            CardViewFragment fr=new CardViewFragment(words.get(position),currentLanguage);
            fr.setRetainInstance(true);
            mPageReferenceMap.put(position, fr);
            return fr;
        }

        @Override
        public int getCount() {
            return words.size();
        }



    }

    class StoreWords extends AsyncTask<Void,Void,List<Word>>{

        @Override
        protected List<Word> doInBackground(Void[] params) {
            List <Word> words=new ArrayList<>();
            chatDBlocal =openOrCreateDatabase(DB_NAME,
                    Context.MODE_PRIVATE, null);
            chatDBlocal.execSQL(CREATE_PL_WORDS_TABLE);
            chatDBlocal.execSQL(CREATE_RUS_WORDS_TABLE);
            Cursor cursor = chatDBlocal.query(Word.TABLE_NAME_PL_WORD, null, null, null , null, null, null);
            if (cursor.moveToFirst()){
                String translation;
                do{
                    Word word=new Word();
                    word.pl_word=cursor.getString(cursor.getColumnIndex(Word.TABLE_COLUMN_NAME_PL_WORD));
                    int bool=cursor.getInt(cursor.getColumnIndex(Word.TABLE_COLUMN_PL_FAVOURITE));
                    if (bool==0) word.favourite=false;
                    else word.favourite=true;
                    word.degree=cursor.getInt(cursor.getColumnIndex(Word.TABLE_COLUMN_DEGREE));
                    int id=cursor.getInt(cursor.getColumnIndex(word.TABLE_COLUMN_NAME_ID));


                    Cursor cursor_word=chatDBlocal.query(Word.TABLE_NAME_RUS_WORD,null,Word.TABLE_COLUMN_PL_ID+" = ? ",new String[]{String.valueOf(id)},null,null,null );
                    if (cursor_word.moveToFirst()){
                        do{
                            translation=cursor_word.getString(cursor_word.getColumnIndex(Word.TABLE_COLUMN_NAME_RUS_WORD));
                            word.translation.add(translation);
                        }while(cursor_word.moveToNext());

                    }
                    words.add(word);
                }while (cursor.moveToNext());
            }
            return words;
        }

        @Override
        protected void onPostExecute(List<Word> words) {
            super.onPostExecute(words);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),words);
            mPager.setAdapter(mPagerAdapter);

        }
    }
}

