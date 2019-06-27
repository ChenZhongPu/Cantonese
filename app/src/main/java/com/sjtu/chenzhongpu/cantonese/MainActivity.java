package com.sjtu.chenzhongpu.cantonese;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sjtu.chenzhongpu.cantonese.sql.WordDao;
import com.sjtu.chenzhongpu.cantonese.sql.WordDbHelper;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.prefs.Preferences;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TextView.OnEditorActionListener {


    private EditText searchText;
    AVLoadingIndicatorView avi;

    private RecyclerView mRecyclerView;

    private RecyclerView mMultiChoiceRV;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapter2;

    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView.LayoutManager mLayoutManager2;

    private WordDbHelper mDBHelper;

    float scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchText = (EditText) findViewById(R.id.search_text);
        searchText.setOnEditorActionListener(this);
        searchText.clearFocus();

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                searchText.clearFocus();
                InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.word_recycler_view);
        mMultiChoiceRV =(RecyclerView) findViewById(R.id.multi_choices);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager2 = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mMultiChoiceRV.setLayoutManager(mLayoutManager2);

        mDBHelper = new WordDbHelper(searchText.getContext());

        mAdapter = new WordAdapter(new ArrayList<WordMean>());
        mAdapter2 = new ChoiceAdapter(new ArrayList<String>(), this);

        mRecyclerView.setAdapter(mAdapter);
        mMultiChoiceRV.setAdapter(mAdapter2);

        scale = searchText.getContext().getResources().getDisplayMetrics().density;


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean onlyTradition = sharedPreferences.getBoolean("pref_tradition", false);
        if (onlyTradition) {
            searchText.setHint("輸入繁體字");
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_star) {
            Intent intent = new Intent(this, StarsActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_reco) {
            Intent intent = new Intent(this, RecoActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_pinyin) {
            Intent intent = new Intent(this, PinyinInputActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feekback) {

            Intent intent = new Intent(Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto", "chenloveit@gmail.com", null));

            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.feedback_subject));


            startActivity(Intent.createChooser(intent, getResources().getString(R.string.feedback_intent)));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        v.clearFocus();
        InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
        if (v.getId() == R.id.search_text && actionId == EditorInfo.IME_ACTION_SEARCH) {
            String query = searchText.getText().toString();
            if (!TextUtils.isEmpty(query)) {
                performSeach(query.trim());
            }
            return true;
        }
        return false;
    }

    private void performSeach(String query) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean onlyTradition = sharedPreferences.getBoolean("pref_tradition", false);
        if (onlyTradition) {
            performTraditionalQuery(query);
        } else {
            // first check if it is simplify Chinese
            if (SimpleTraditionMap.oneToMultiMap.containsKey(query)) {

                mAdapter = new WordAdapter(new ArrayList<WordMean>());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                CardView cardView = (CardView) findViewById(R.id.word_card_view);
                ViewGroup.LayoutParams params = cardView.getLayoutParams();
                params.height = 0;
                cardView.setLayoutParams(params);
                // one (simplify Chinese) to multi
                // display the view to let user to choose
                mAdapter2 = new ChoiceAdapter(SimpleTraditionMap.oneToMultiMap.get(query), this);
                mMultiChoiceRV.setAdapter(mAdapter2);
                mAdapter2.notifyDataSetChanged();
            } else if (SimpleTraditionMap.simTraMap.containsKey(query)){
                // one (simplify Chinese) to one
                performTraditionalQuery(SimpleTraditionMap.simTraMap.get(query));
            } else {
                performTraditionalQuery(query);
            }
        }

    }

    public void performTraditionalQuery(String query) {
        mAdapter2 = new ChoiceAdapter(new ArrayList<String>(), this);
        mMultiChoiceRV.setAdapter(mAdapter2);
        mAdapter2.notifyDataSetChanged();
        // query char must be a Traditional char
        try {
            avi.show();
            String big5Query = Utils.getBig5FromString(query);
            new FetchHtmlTask().execute(big5Query, query);
        } catch (UnsupportedEncodingException e) {
            avi.hide();
            Snackbar.make(searchText.getRootView(), R.string.invalid_char, Snackbar.LENGTH_LONG).show();
        }

    }

    class FetchHtmlTask extends AsyncTask<String, Void, WordBean> {

        protected WordBean doInBackground(String... urls){

            // look up in cache first
            SQLiteDatabase db = mDBHelper.getWritableDatabase();

            WordBean wordBean = WordDao.queryWord(db, urls[0]);
            db.close();

            if (wordBean != null) {
                // star (must store big5, word)
                if (wordBean.getCanjie() == null) {
                    // but, not cached all, we should still fetch word from web
                    WordBean wordBean2 = Utils.getWordBeanFromWeb(urls[0], urls[1]);
                    if (wordBean2 != null) wordBean2.setStar(true);
                    return wordBean2;
                } else {
                    wordBean.setStar(true);
                    return wordBean;
                }
            } else {
                // not star
                WordBean wordBean2 = Utils.getWordBeanFromWeb(urls[0], urls[1]);
                if (wordBean2 != null) wordBean2.setStar(false);
                return wordBean2;
            }
        }

        protected void onPostExecute(final WordBean wordBean) {
            avi.hide();

            if (wordBean == null) {
                Snackbar.make(searchText.getRootView(), R.string.network_err, Snackbar.LENGTH_SHORT)
                    .setDuration(3000).show();
            } else {
                CardView cardView = (CardView) findViewById(R.id.word_card_view);
                ViewGroup.LayoutParams params = cardView.getLayoutParams();
                params.height = (int)(120 * scale + 0.5f);
                cardView.setLayoutParams(params);
                ((TextView) cardView.findViewById(R.id.word_text)).setText(wordBean.getWord());
                ((TextView) cardView.findViewById(R.id.canjie_mean)).setText(wordBean.getCanjie());
                ((TextView) cardView.findViewById(R.id.english_mean)).setText(wordBean.getEnglish());
                ((TextView) cardView.findViewById(R.id.english_mean)).setMovementMethod(new ScrollingMovementMethod());
                ToggleButton startToggle = (ToggleButton) cardView.findViewById(R.id.word_star);
                startToggle.setChecked(wordBean.isStar());

                cardView.findViewById(R.id.word_star).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToggleButton starToggle = (ToggleButton) v;
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (searchText.getContext());
                        boolean shall_cache = prefs.getBoolean("pref_cache", true);
                        SQLiteDatabase db = mDBHelper.getWritableDatabase();
                        if (starToggle.isChecked()) {
                            WordDao.insertWord(db, wordBean, searchText.getContext(), shall_cache);
                        } else {
                            // remove them if stored before
                            WordDao.deleteWord(db, wordBean, searchText.getContext());
                        }
                        db.close();
                    }
                });
                mAdapter = new WordAdapter(wordBean.getWordMeenList());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
