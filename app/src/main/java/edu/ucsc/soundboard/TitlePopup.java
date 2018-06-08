package edu.ucsc.soundboard;

import android.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;


import android.content.Intent;
import android.widget.TextView;


public class TitlePopup extends AppCompatActivity {
    Intent resultIntent = new Intent();
    TextView titleEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_popup);
        DisplayMetrics dm = new DisplayMetrics();
        this.setTitle("Name for this new board");
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int) (dm.widthPixels * 0.8), (int) (dm.heightPixels * 0.4));
        titleEntry = findViewById(R.id.editTitle);
        titleEntry.setText("new board");
    }

    public void save(View view) {
        resultIntent.putExtra("title", titleEntry.getText());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    public void cancel(View view){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

}


