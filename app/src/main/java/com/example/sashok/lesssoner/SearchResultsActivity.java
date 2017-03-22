package com.example.sashok.lesssoner;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by sashok on 22.3.17.
 */

public class SearchResultsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("TAG",query);
            //use the query to search your data somehow
        }
    }
}