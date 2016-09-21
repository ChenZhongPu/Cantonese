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
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sjtu.chenzhongpu.cantonese.sql.WordDao;
import com.sjtu.chenzhongpu.cantonese.sql.WordDbHelper;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TextView.OnEditorActionListener {

    private EditText searchText;
    AVLoadingIndicatorView avi;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private WordDbHelper mDBHelper;

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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.word_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDBHelper = new WordDbHelper(searchText.getContext());

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_star) {
            Intent intent = new Intent(this, StarsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_phonetic) {
            Intent intent = new Intent(this, LearnActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pinyin) {
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
            if (!TextUtils.isEmpty(query))
               performSeach(query);
            return true;
        }
        return false;
    }

    private void performSeach(String query) {
        try {
            String big5Query = Utils.getBig5FromString(query);

            avi.show();

            new FetchHtmlTask().execute(big5Query, query);

        } catch (UnsupportedEncodingException e) {
            Snackbar.make(this.findViewById(R.id.search_text).getRootView(), R.string.not_big5, Snackbar.LENGTH_SHORT)
                    .setDuration(2000).show();
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
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, 400);
                lp.addRule(RelativeLayout.BELOW, R.id.search_text);
                CardView cardView;
                if (findViewById(R.id.word_card_view) == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    cardView = (CardView) inflater.inflate(R.layout.word_card, null);
                    cardView.setLayoutParams(lp);
                    RelativeLayout content_layout = (RelativeLayout) findViewById(R.id.content_layout);
                    content_layout.addView(cardView, 0);
                } else {
                    cardView = (CardView) findViewById(R.id.word_card_view);
                }
                ((TextView) cardView.findViewById(R.id.word_text)).setText(wordBean.getWord());
                ((TextView) cardView.findViewById(R.id.canjie_mean)).setText(wordBean.getCanjie());
                ((TextView) cardView.findViewById(R.id.english_mean)).setText(wordBean.getEnglish());
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
            }
        }
    }
}
