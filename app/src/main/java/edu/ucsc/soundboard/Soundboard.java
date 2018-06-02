package edu.ucsc.soundboard;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.graphics.drawable.ColorDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Soundboard extends AppCompatActivity {

    boolean isSaved = false;
    boolean editMode = false;
    JSONObject boardJSON;
    JSONArray inButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);
        try {
            boardJSON = new JSONObject(getIntent().getStringExtra("boardjson"));
        } catch (Exception e) {
        }
        this.loadBoard(boardJSON);
    }



//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Get the passed-in board data
//        Intent i = getIntent();
//        String title = i.getStringExtra("title");
//        String bArray = i.getStringExtra("buttonarray");
//        try {
//            inButtons = new JSONArray(bArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        // Set the proper title
//        setTitle(title);
//        // Set button values
//        for(int j=1; j<=30; j++) {
//            int bid = getResources().getIdentifier("button" + j, "id", getPackageName());
//            Button b = findViewById(bid);
//            try {
//                /* ========== ADD BUTTON VALUES HERE ========== */
//                b.setText(inButtons.getJSONObject(j).getString("text"));
//                //b.color = inButtons.getJSONObject(j).getString("color");
//                //b.soundfile = inButtons.getJSONObject(j).getString("soundfile")
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static JSONObject emptyBoardJSON() {
        JSONObject newJSON = new JSONObject();
        try {
            newJSON.put("title", "new board");
            JSONArray buttonArray = new JSONArray();
            for (int i = 0; i < 30; i++) {
                JSONObject newButtonJSON = new JSONObject("button"+i);
                newButtonJSON.put("tag", "");
                newButtonJSON.put("text", String.valueOf(i+1));
                buttonArray.put(newButtonJSON);
            }
            newJSON.put("buttons", buttonArray);
        } catch (Exception e) {
        }
        return newJSON;
    }

    public void enterEditMode(View view){
        editMode = !editMode;
        Button editButton = findViewById(R.id.edit_button);
        if (editMode) {
            editButton.setText("Exit Edit");
        } else {
            editButton.setText("edit");
        }
    }

    public void playSound(View view){
        Button clickedButton = findViewById(view.getId());
        if (editMode){
            Intent editButtonScreen = new Intent(getApplicationContext(), Button_Popup.class);
            editButtonScreen.putExtra("text", clickedButton.getText());
            ColorDrawable buttonColor = (ColorDrawable) clickedButton.getBackground();
            editButtonScreen.putExtra("color", buttonColor.getColor());
            editButtonScreen.putExtra("id", view.getId());
            startActivityForResult(editButtonScreen, 0);

        } else {
            //play sound from clickedButton.getTag()
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Button clickedButton = findViewById(data.getIntExtra("id", 0));
                clickedButton.setText(data.getStringExtra("text"));
                ColorDrawable buttonColor = (ColorDrawable) clickedButton.getBackground();
                buttonColor.mutate();
                buttonColor.setColor(data.getIntExtra("color", 0));

            }
        }
    }

    public void saveBoard(View view) {
        if (!isSaved) {
            //ask for board name
        }
        try {
            JSONArray buttons = boardJSON.getJSONArray("buttons");
            for (int i = 1; i <= 30; i++) {
                int id = getResources().getIdentifier("button" + i, "id", getPackageName());
                Button button = findViewById(id);
                buttons.getJSONObject(i - 1).put("text", button.getText());
                buttons.getJSONObject(i - 1).put("tag", button.getTag());
            }
        } catch (Exception e) {

        }
    }


    private void loadBoard(JSONObject board) {
        try {
            this.setTitle(board.getString("title"));
            JSONArray buttons = board.getJSONArray("buttons");
            for (int i = 0; i < buttons.length(); i++) {
                int id = getResources().getIdentifier("button" + (i + 1), "id", getPackageName());
                Button button = findViewById(id);
                JSONObject buttonJSON = buttons.getJSONObject(i);
                button.setText(buttonJSON.getString("text"));
                button.setTag(buttonJSON.get("tag"));
            }
        } catch (Exception e) {

        }
    }

    public void loadBoardScreen(View view) {
        //load borad screen
    }

    public void newBoard(View view) {
        boardJSON = this.emptyBoardJSON();
        this.loadBoard(boardJSON);
    }
}
