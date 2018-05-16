package edu.ucsc.soundboard;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class BoardList extends AppCompatActivity {

    public JSONObject jobj = null;
    public JSONArray jarr = null;
    private static final String TAG = "JSON_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
    }

    // Reads in from the JSON File and attempts to make a list using the object titles
    protected void onResume(){
        super.onResume();
        ListView list = findViewById(R.id.board_list_view);
        TextView text = findViewById(R.id.text);
        text.setVisibility(View.INVISIBLE);
        Log.d(TAG, ""+getFilesDir());
        jobj = null;
        //Attempt to read an existing file
        try{
            File f = new File(getFilesDir(), "Boards.ser");
            FileInputStream fin = new FileInputStream(f);
            ObjectInputStream oin = new ObjectInputStream(fin);
            String j = null;
            try{
                j = (String) oin.readObject();
            }
            catch(ClassNotFoundException c){
                c.printStackTrace();
            }
            try{
                jobj = new JSONObject(j);
                jarr = jobj.getJSONArray("boards");
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            //Display list
            final ArrayList<BoardData> aList = new ArrayList<BoardData>();
            for(int i = 0; i < jarr.length(); i++){

                BoardData bd = new BoardData();
                try {
                    bd.titleText = jarr.getJSONObject(i).getString("title");
                    bd.buttonArray = jarr.getJSONObject(i).getJSONArray("buttons");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                aList.add(bd);
            }
            // Create the array and assign titles
            String[] listItems = new String[aList.size()];
            for(int i=0; i < aList.size(); i++){
                BoardData boardD = aList.get(i);
                listItems[i] = boardD.titleText;
            }
            // Display list
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
            list.setAdapter(adapter);

            // Set onClick functionality for the list
            final Context context = this;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BoardData selected = aList.get(position);
                    Intent detailIntent = new Intent(context, Soundboard.class);
                    detailIntent.putExtra("title", selected.titleText);
                    detailIntent.putExtra("buttonarray", selected.buttonArray.toString());
                    /* JSONArray is being converted to a string^. In Soundboard, change it back with the following:

                    String bArray = getIntent().getStringExtra("buttonarray");
                    try {
                        JSONArray buttons = new JSONArray(bArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    */
                    startActivity(detailIntent);
                }
            });
        }
        catch(IOException e){
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
        }
    }
}
