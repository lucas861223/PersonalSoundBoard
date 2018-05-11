package edu.ucsc.soundboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static edu.ucsc.soundboard.R.layout.activity_soundboard;
import static edu.ucsc.soundboard.R.layout.activity_settings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_settings);
    }
}
