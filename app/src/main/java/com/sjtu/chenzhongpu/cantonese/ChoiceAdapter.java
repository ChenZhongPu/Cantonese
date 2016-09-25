package com.sjtu.chenzhongpu.cantonese;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chenzhongpu on 24/09/2016.
 */

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ViewHolder>{

    private List<String> mDataSet;
    private Activity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView choiceTV;
        View mV;

        public ViewHolder(View v) {
            super(v);
            mV = v;
            choiceTV = (TextView) v.findViewById(R.id.choice_tv);
        }
    }

    public ChoiceAdapter(List<String> data, Activity activity) {
        mDataSet = data;
        mActivity = activity;
    }

    @Override
    public ChoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choice_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String string = mDataSet.get(position);
        holder.choiceTV.setText(string);

        holder.mV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mActivity).performTraditionalQuery(string);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
