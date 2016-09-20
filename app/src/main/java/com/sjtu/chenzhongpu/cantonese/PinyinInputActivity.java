package com.sjtu.chenzhongpu.cantonese;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class PinyinInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinyin_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialSpinner spinnerShen = (MaterialSpinner) findViewById(R.id.spinner_shen);
        spinnerShen.setItems("無聲母", "b", "c", "d", "f", "g", "gw", "h", "j", "k", "kw",
                "l", "m", "n", "ng", "p", "s", "t", "w", "z");
        spinnerShen.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        MaterialSpinner spinnerYun = (MaterialSpinner) findViewById(R.id.spinner_yun);
        spinnerYun.setItems("無韻母", "aa", "aai", "aau", "aam", "aan", "aang", "aap", "aat",
                "aak", "ai", "au", "am", "an", "ang", "ap", "at", "e", "ei", "eu", "em",
                "eng", "ep", "ek", "i", "iu", "im", "in", "ing", "ip", "it", "ik", "o",
                "oi", "ou", "on", "ong", "ot", "ok", "oe", "oeng", "oek", "eoi", "eon", "eot",
                "u", "ui", "un", "ung", "ut", "uk", "yu", "yun", "yut", "m", "ng");
    }

}
