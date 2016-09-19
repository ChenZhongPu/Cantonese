package com.sjtu.chenzhongpu.cantonese;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_setting);
        getFragmentManager().beginTransaction().replace(R.id.setting_layout, new RrefsFragment()).commit();
    }

    public static class RrefsFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

    }
}
