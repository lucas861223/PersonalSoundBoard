package edu.ucsc.soundboard;


import java.io.IOException;
import java.io.InputStream;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.content.Context;
import android.app.ListActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Button;

public class Settings extends AppCompatActivity {
    View view;
    ConstraintLayout ll;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.red);



        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String color = i.getStringExtra("color");
        String background = i.getStringExtra("background");
        //String d = i.getStringExtra("d");
        //String e = i.getStringExtra("e");
        //String f = i.getStringExtra("f");


        TextView t = (TextView)findViewById(R.id.textView);
        TextView col = (TextView)findViewById(R.id.textView2);
        TextView bg = (TextView)findViewById(R.id.textView3);
        //TextView d = (TextView)findViewById(R.id.textView6);
        //TextView e = (TextView)findViewById(R.id.textView4);
        //TextView f = (TextView)findViewById(R.id.textView5);

        t.setText("Title: "+title);
        col.setText("Color: "+color);
        bg.setText("Background: "+background);
        //d.setText("Time: "+d);
        //e.setText("Latitude: "+e);
        //f.setText("Longitude: "+f);

        ll = (ConstraintLayout)findViewById(R.id.ConstraintLayout);
        b = (Button)findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ll.setBackgroundColor(Color.RED);
            }
        });



        }

    //public void changeRed(View v) {
      //  view.setBackgroundResource(R.color.red);

    //}




    protected void onResume(Bundle savedInstanceState){
        //
    }
}
