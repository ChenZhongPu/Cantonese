package com.sjtu.chenzhongpu.cantonese;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sjtu.chenzhongpu.cantonese.sql.WordDao;
import com.sjtu.chenzhongpu.cantonese.sql.WordDbHelper;

import java.util.List;

/**
 * Created by chenzhongpu on 9/20/16.
 */
public class StarsAdapter extends RecyclerView.Adapter<StarsAdapter.ViewHolder>{

    private List<WordBean> mWordSet;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView wordTextView;
        public ToggleButton startToggle;
        public View mV;
        public ViewHolder(View v) {
            super(v);
            mV = v;
            wordTextView = (TextView) v.findViewById(R.id.star_word_tv);
            startToggle = (ToggleButton) v.findViewById(R.id.word_star_toggle);
        }
    }

    public StarsAdapter(List<WordBean> _wordSet, Context context) {
        mWordSet = _wordSet;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StarsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.star_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WordBean wordBean = mWordSet.get(position);
        holder.wordTextView.setText(wordBean.getWord());
        holder.startToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // since it is starre already, if clicked, we delete it
                mWordSet.remove(position);
                WordDbHelper wordDbHelper = new WordDbHelper(mContext);
                SQLiteDatabase db = wordDbHelper.getWritableDatabase();
                WordDao.deleteWordByGig5(db, wordBean.getBig5(), mContext);
                StarsAdapter.this.notifyDataSetChanged();
            }
        });

        holder.mV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StarDetailActivity.class);
                intent.putExtra(Utils.BIG5_MESSAGE, wordBean.getBig5());
                intent.putExtra(Utils.WORD_MESSAGE, wordBean.getWord());
                mContext.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mWordSet.size();
    }

}
