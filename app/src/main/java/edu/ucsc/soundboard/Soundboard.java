package edu.ucsc.soundboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import org.json.JSONObject;
import org.json.JSONArray;

public class Soundboard extends AppCompatActivity {

    boolean isSaved = false;
    JSONObject boardJSON;

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

    public static JSONObject emptyBoardJSON() {
        JSONObject newJSON = new JSONObject();
        try {
            newJSON.put("title", "new board");
            JSONArray buttonArray = new JSONArray();
            for (int i = 0; i < 30; i++) {
                JSONObject newButtonJSON = new JSONObject();
                newButtonJSON.put("tag", "");
                newButtonJSON.put("text", String.valueOf(i));
                buttonArray.put(newButtonJSON);
            }
            newJSON.put("buttons", buttonArray);
        } catch (Exception e) {
        }
        return newJSON;
    }

    public void saveBoard(View view) {
        if (!isSaved) {
            //ask for board name
        }
        try {
            JSONArray buttons = boardJSON.getJSONArray("buttons");
            for (int i = 1; i <= 30; i++) {
                int id = getResources().getIdentifier("button_" + i, "id", getPackageName());
                Button button = (Button) findViewById(id);
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
                int id = getResources().getIdentifier("button_" + (i + 1), "id", getPackageName());
                Button button = (Button) findViewById(id);
                JSONObject buttonJSON = buttons.getJSONObject(i);
                button.setText(buttonJSON.getString("text"));
                button.setTag(buttonJSON.get("tag"));
            }
        } catch (Exception e) {

        }
    }
}
