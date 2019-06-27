package com.sjtu.chenzhongpu.cantonese;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by chenzhongpu on 9/18/16.
 */
public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private List<WordMean> mWordSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView yinJieTextView;
        public TextView meansTextView;
        public ImageView soundImage;
        public ViewHolder(View v) {
            super(v);
            yinJieTextView = (TextView) v.findViewById(R.id.yinjie_mean);
            meansTextView = (TextView) v.findViewById(R.id.mean_text);
            soundImage = (ImageView) v.findViewById(R.id.word_audio);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WordAdapter(List<WordMean> _wordSet) {
        mWordSet = _wordSet;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_mean, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final WordMean wordMean = mWordSet.get(position);
        holder.yinJieTextView.setText(wordMean.getPronunce());
        holder.meansTextView.setText(wordMean.getMean());
        holder.soundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weburl = Utils.WEBURL + "sound/" + wordMean.getPronunce() + ".wav";
                // find audio in local first
                File audioFile = v.getContext().getFileStreamPath(wordMean.getPronunce() + ".wav");
                boolean isCached = audioFile.exists();

                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                FileInputStream fileInputStream;
                try {
                    if (isCached) {
                        System.out.println("found cached audio ...");
                        fileInputStream = v.getContext().openFileInput(wordMean.getPronunce() + ".wav");
                        mediaPlayer.setDataSource(fileInputStream.getFD());
                        fileInputStream.close();
                    } else {
                        mediaPlayer.setDataSource(weburl);
                    }
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar.make(holder.soundImage, R.string.network_err, Snackbar.LENGTH_SHORT)
                            .setDuration(3000).show();
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mWordSet.size();
    }
}

