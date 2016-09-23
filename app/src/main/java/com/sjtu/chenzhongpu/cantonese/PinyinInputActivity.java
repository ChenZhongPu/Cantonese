package com.sjtu.chenzhongpu.cantonese;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PinyinInputActivity extends AppCompatActivity {

    private ImageView playSoundImage;

    private AVLoadingIndicatorView avLoadingIndicatorView;

    private boolean canPlay;

    private String lastValidShen;
    private String lastValidYun;
    private String lastValidDiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinyin_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        playSoundImage = (ImageView) findViewById(R.id.sound_play);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.sound_avi);
        avLoadingIndicatorView.hide();

        canPlay = false;
        lastValidShen = "j";
        lastValidYun = "yut";
        lastValidDiao = "6";

        final MaterialSpinner spinnerShen = (MaterialSpinner) findViewById(R.id.spinner_shen);
        final String[] shenItems = {"無聲母", "b", "c", "d", "f", "g", "gw", "h", "j", "k", "kw",
                "l", "m", "n", "ng", "p", "s", "t", "w", "z"};
        spinnerShen.setItems(shenItems);

        final MaterialSpinner spinnerYun = (MaterialSpinner) findViewById(R.id.spinner_yun);
        final String[] yunItems = {"無韻母", "aa", "aai", "aau", "aam", "aan", "aang", "aap", "aat",
                "aak", "ai", "au", "am", "an", "ang", "ap", "at", "e", "ei", "eu", "em",
                "eng", "ep", "ek", "i", "iu", "im", "in", "ing", "ip", "it", "ik", "o",
                "oi", "ou", "on", "ong", "ot", "ok", "oe", "oeng", "oek", "eoi", "eon", "eot",
                "u", "ui", "un", "ung", "ut", "uk", "yu", "yun", "yut", "m", "ng"};
        spinnerYun.setItems(yunItems);

        final MaterialSpinner spinnerDiao = (MaterialSpinner) findViewById(R.id.spinner_diao);
        final String[] diaoItems = {"1", "2", "3", "4", "5", "6"};
        spinnerDiao.setItems(diaoItems);

        Button getSound = (Button) findViewById(R.id.getSound);

        getSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avLoadingIndicatorView.hide();
                avLoadingIndicatorView.show();
                canPlay = false;

                String shen = shenItems[spinnerShen.getSelectedIndex()];
                String yun = yunItems[spinnerYun.getSelectedIndex()];
                String diao = diaoItems[spinnerDiao.getSelectedIndex()];

                if (shen.equals("無聲母")) shen = "";
                if (yun.equals("無韻母")) yun = "";
                new FetchSoundFromWeb().execute(shen, yun, diao);
            }
        });

        playSoundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canPlay) {
                    String weburl = Utils.WEBURL + "sound/" + lastValidShen + lastValidYun + lastValidDiao + ".wav";
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(weburl);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        Snackbar.make(playSoundImage.getRootView(), R.string.network_err, Snackbar.LENGTH_SHORT)
                                .setDuration(3000).show();
                    }

                }
            }
        });

    }

    class FetchSoundFromWeb extends AsyncTask<String, Void, Boolean> {

        private String mShen;
        private String mYun;
        private String mDiao;

        protected Boolean doInBackground (String... urls) {
            mShen = urls[0];
            mYun = urls[1];
            mDiao = urls[2];
            String url = Utils.WEBURL + "pho-rel.php?s1=" + urls[0] + "&s2=" + urls[1] + "&s3=" + urls[2];
            try {
                Document doc  = Jsoup.connect(url).get();
                if (doc.select("table").size() == 0) {
                    // not ound
                    return false;
                }
            } catch (IOException e) {
                Log.d("error", "error of check sound");
                return null;
            }
            return true;
        }

        protected void onPostExecute(Boolean b) {
            avLoadingIndicatorView.hide();
            if (b == null) {
                playSoundImage.setImageResource(R.drawable.ic_volume_dead);
                Snackbar.make(playSoundImage.getRootView(), R.string.network_err, Snackbar.LENGTH_SHORT)
                        .setDuration(3000).show();
            } else if (b) {
                canPlay = true;
                playSoundImage.setImageResource(R.drawable.ic_volume_ok);
                lastValidShen = mShen;
                lastValidYun = mYun;
                lastValidDiao = mDiao;
                Snackbar.make(playSoundImage.getRootView(), R.string.play_hint, Snackbar.LENGTH_SHORT)
                        .setDuration(3000).show();
            } else {
                playSoundImage.setImageResource(R.drawable.ic_volume_dead);
                Snackbar.make(playSoundImage.getRootView(), R.string.invalid_sound, Snackbar.LENGTH_SHORT)
                        .setDuration(3000).show();
            }
        }
    }


}
