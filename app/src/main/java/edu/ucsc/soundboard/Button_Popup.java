package edu.ucsc.soundboard;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
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

public class Button_Popup extends AppCompatActivity {
    Intent resultIntent = new Intent();
    TextView buttonName;
    int buttonColor;
    Button colorButton;
//    ColorDrawable ColorPickerButtonDrawable;
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
        colorButton = findViewById(R.id.colorButton);
//        ColorPickerButtonDrawable = (ColorDrawable) colorButton.getBackground();
//        ColorPickerButtonDrawable.mutate();
//        ColorPickerButtonDrawable.setColor(buttonColor);
//        colorButton.setTextColor(textColor(buttonColor));
    }

    public static int textColor(int color) {
        color += 16777216;
        int r = color / 65536;
        int g = color % 65536 / 256;
        int b = color % 63356 % 256;
        double y = 0.2126 * Math.pow(r / 255.0, 2.2)  +  0.7151 * Math.pow( g / 255.0, 2.2)  +  0.0721 * Math.pow(b / 255.0, 2.2);
        if (y > 0.18) return -16777216;
        else return -1;
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
//        ColorPickerButtonDrawable.setColor(buttonColor);
//        colorButton.setTextColor(textColor(buttonColor));
    }

    public void chooseSoundFile(View view) {

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
}


