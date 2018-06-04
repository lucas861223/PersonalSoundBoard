package edu.ucsc.soundboard;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Button_Popup extends AppCompatActivity {
    Intent resultIntent = new Intent();
    TextView buttonName;
    int buttonColor;
    MediaRecorder mediaRecorder;
    String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_popup);
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getWindow().setLayout((int) (dm.widthPixels * 0.8), (int) (dm.heightPixels * 0.6));
        buttonName = findViewById(R.id.editText);
        buttonName.setText(getIntent().getStringExtra("text"));
        buttonColor = getIntent().getIntExtra("color", 0);
    }

    public void launchColorPicker(View view) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, buttonColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) { }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                 buttonColor = color;
            }
        });
        colorPicker.show();
    }

    public void recordNewSound (View view) {
        //filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        MediaRecorderReady();
        try {
            // recording starts
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopNewSound (View view) {
            // recording stops
            mediaRecorder.stop();
    }

    public void save(View view) {
        resultIntent.putExtra("color", buttonColor);
        resultIntent.putExtra("text", buttonName.getText().toString());
        resultIntent.putExtra("id", getIntent().getIntExtra("id", 0));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    public void cancel(View view){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(filepath);
    }
    // method to create a random file name

    // callback method

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}


