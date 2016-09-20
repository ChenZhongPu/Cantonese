package com.sjtu.chenzhongpu.cantonese;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sjtu.chenzhongpu.cantonese.sql.WordDao;
import com.sjtu.chenzhongpu.cantonese.sql.WordDbHelper;

public class StarDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private WordDbHelper mDBHelper;

    private TextView canjieTV;
    private TextView englishTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String big5 = intent.getStringExtra(Utils.BIG5_MESSAGE);
        String word = intent.getStringExtra(Utils.WORD_MESSAGE);

        ((TextView) findViewById(R.id.word_detail_text)).setText(word);

        canjieTV = (TextView) findViewById(R.id.canjie_detail_mean);
        englishTV = (TextView) findViewById(R.id.english_detail_mean);

        mRecyclerView = (RecyclerView) findViewById(R.id.star_detail_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDBHelper = new WordDbHelper(toolbar.getContext());

        new GetWordBeanTask().execute(big5, word);

    }


    class GetWordBeanTask extends AsyncTask<String, Void, WordBean> {

        protected WordBean doInBackground(String... urls){
            // look up in cache first
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            WordBean wordBean = WordDao.queryWord(db, urls[0]);
            db.close();
            // word must be starred
            if (wordBean.getCanjie() == null) {
                // wordinfo, and wordmean need to fetch in web
                return Utils.getWordBeanFromWeb(urls[0], urls[1]);
            } else {
                return wordBean;
            }
        }

        protected void onPostExecute(final WordBean wordBean) {
            if (wordBean == null || wordBean.getCanjie() == null) {
                Snackbar.make(mRecyclerView.getRootView(), R.string.network_err, Snackbar.LENGTH_SHORT)
                        .setDuration(3000).show();
            } else {
                canjieTV.setText(wordBean.getCanjie());
                englishTV.setText(wordBean.getEnglish());
                mAdapter = new WordAdapter(wordBean.getWordMeenList());
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

}
