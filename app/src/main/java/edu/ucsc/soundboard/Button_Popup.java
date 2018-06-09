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
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    Spinner filespinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_popup);
        filespinner = findViewById(R.id.filespinner);
        addItemsOnSpinner();
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

    public void addItemsOnSpinner() {
        List<String> list = new ArrayList<String>();
        File sdCard = Environment.getExternalStorageDirectory();
        File listFile[] = sdCard.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (!listFile[i].isDirectory()) {// if its a directory need to get the files under that directory
                    list.add(listFile[i].getAbsolutePath());
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            Log.d("help", listFile[0].getAbsolutePath());
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filespinner.setAdapter(dataAdapter);
            filespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    Object item = adapterView.getItemAtPosition(position);
                    if (item != null) {
                        filepath = item.toString();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }

    public void save(View view) {
        resultIntent.putExtra("color", buttonColor);
        resultIntent.putExtra("text", buttonName.getText().toString());
        resultIntent.putExtra("id", getIntent().getIntExtra("id", 0));
        resultIntent.putExtra("filepath", filepath);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    public void cancel(View view){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}


