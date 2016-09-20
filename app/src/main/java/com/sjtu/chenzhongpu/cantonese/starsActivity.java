package com.sjtu.chenzhongpu.cantonese;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.sjtu.chenzhongpu.cantonese.sql.WordDao;
import com.sjtu.chenzhongpu.cantonese.sql.WordDbHelper;

import java.util.List;

public class StarsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private WordDbHelper mDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.stars_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDBHelper = new WordDbHelper(toolbar.getContext());

        initData();

    }

    private void initData() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        List<WordBean> wordBeanList = WordDao.getAllStarBean(db);
        db.close();
        if (wordBeanList.size() > 0) {
            mAdapter = new StarsAdapter(wordBeanList, getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            LayoutInflater inflater = getLayoutInflater();
            CardView cardView = (CardView) inflater.inflate(R.layout.empty_star, null);

            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.starts_reletive_layout);
            relativeLayout.addView(cardView);
        }
    }

}
