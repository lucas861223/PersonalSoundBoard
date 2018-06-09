package edu.ucsc.soundboard;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.graphics.drawable.ColorDrawable;
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

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Soundboard extends AppCompatActivity {

    boolean isNewBoard;
    boolean editMode = false;
    public JSONObject jobj = null; //For file access
    public JSONArray jarr = null; //For file access
    JSONObject boardJSON;
    Button buttonRecord, buttonStop, buttonSave, buttonEdit;

    String filename;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);
        // get references to the menu items
        buttonRecord = findViewById(R.id.record_button);
        buttonStop = findViewById(R.id.stop_button);
        buttonSave = findViewById(R.id.save_button);
        buttonEdit = findViewById(R.id.edit_button);
        buttonStop.setEnabled(false); //Turn off stop button on startup
        if(!checkPermission()){
            requestPermission();
        }
        try {
            boardJSON = new JSONObject(getIntent().getStringExtra("boardjson"));
        } catch (Exception e) {
        }
        this.loadBoard(boardJSON);
        isNewBoard = getIntent().hasExtra("jIndex");
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
                JSONObject newButtonJSON = new JSONObject();
                newButtonJSON.put("filepath", "");
                newButtonJSON.put("text", String.valueOf(i+1));
                newButtonJSON.put("color", Color.parseColor("#DCDCDC"));
                buttonArray.put(newButtonJSON);
            }
            newJSON.put("buttons", buttonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newJSON;
    }

    public void recordBoard(String s){
        filename = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+s+".3gp";
        MediaRecorderReady();
        try {
            // recording starts
            mediaRecorder.prepare();
            mediaRecorder.start();
            buttonStop.setEnabled(true);
            buttonSave.setEnabled(false);
            buttonRecord.setEnabled(false);
            buttonEdit.setEnabled(false);
            Toast.makeText(Soundboard.this, "Recording "+s+".3gp",
                    Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordBoardRedir(View view){
        startActivityForResult(new Intent(getApplicationContext(), filenamePopup.class), 2);
    }

    public void stopNewSound (View view) {
        // recording stops
        mediaRecorder.stop();
        buttonStop.setEnabled(false);
        buttonSave.setEnabled(true);
        buttonRecord.setEnabled(true);
        buttonEdit.setEnabled(true);
    }


    public void enterEditMode(View view){
        editMode = !editMode;
        Button editButton = findViewById(R.id.edit_button);
        if (editMode) {
            editButton.setText("Exit Edit");
        } else {
            editButton.setText("Edit");
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

        } else if (!clickedButton.getTag(R.id.filepath).toString().equals("")){
            //play sound from clickedButton.getTag()
            //not sure what the file path is
            //filepath =
                 //   Environment.getExternalStorageDirectory().getAbsolutePath();
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(clickedButton.getTag(R.id.filepath).toString());
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
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
                int color = data.getIntExtra("color", 0);
                buttonColor.setColor(color);
                clickedButton.setTextColor(Button_Popup.textColor(color));
            }
        } else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //this.setTitle(data.getStringExtra("newtitle"));
                isNewBoard = !isNewBoard;
                String newTitle = data.getStringExtra("newtitle");
                try {
                    boardJSON.put("title", newTitle);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                saveBoard();
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String newfile = data.getStringExtra("newfile");
                recordBoard(newfile);
            }
        }
    }

    public void saveBoard(View view) {
        if (!isNewBoard) {
            startActivityForResult(new Intent(getApplicationContext(), TitlePopup.class), 1);
        } else {
            saveBoard();
        }
    }

    public void saveBoard() {
        // Put the current button info into the buttons array of boardJSON
        try {
            JSONArray bArr = new JSONArray();
            for (int i = 1; i <= 30; i++) {
                int id = getResources().getIdentifier("button" + i, "id", getPackageName());
                Button button = findViewById(id);
                JSONObject bObj = new JSONObject();
                bObj.put("text", button.getText());
                bObj.put("filepath", button.getTag(R.id.filepath));
                ColorDrawable buttonColor = (ColorDrawable) button.getBackground();
                bObj.put("color", buttonColor.getColor());
                bArr.put(bObj);
            }
            boardJSON.put("buttons", bArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Open/create file, load the main object (jobj) and the array of boards (jarr)
        try{
            File f = new File(getFilesDir(), "Boards.ser");
            FileInputStream fin = new FileInputStream(f);
            ObjectInputStream oin = new ObjectInputStream(fin);
            String j = null;
            try{
                j = (String) oin.readObject();
            }
            catch(ClassNotFoundException c) {
                c.printStackTrace();
            }
            try{
                jobj = new JSONObject(j);
                jarr = jobj.getJSONArray("boards");
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            fin.close();
            oin.close();
        }
        catch(IOException e){
            //No JSONObject found; make a new one, and add in the blank boards array
            jobj = new JSONObject();
            jarr = new JSONArray();
            try{
                jobj.put("boards", jarr);
            }
            catch(JSONException j){
                j.printStackTrace();
            }
        }
        // Add new content to the JSONarray, or append if it doesn't exist
        try {
            if (getIntent().hasExtra("jIndex")) {
                // Position in the array was passed in through the list view, meaning this is NOT a new array. overwrite the previous version.
                int idx = getIntent().getIntExtra("jIndex", 0);
                jarr.put(idx, boardJSON);
            } else {
                // Position was not passed in. This is a new array; append it to the end of the JSONarray.
                jarr.put(boardJSON);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Write to the file.
        try{
            File f = new File(getFilesDir(), "Boards.ser");
            FileOutputStream fout = new FileOutputStream(f);
            ObjectOutputStream oout = new ObjectOutputStream(fout);
            String j = jobj.toString();
            oout.writeObject(j);
            oout.close();
            fout.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        // Exit to BoardList. This is to prevent the user from saving multiple copies of the same board, might find a more elegant way to do this later.
        Intent i = new Intent(Soundboard.this, BoardList.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
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
                button.setTag(R.id.filepath, buttonJSON.getString("filepath"));
                ColorDrawable buttonColor = (ColorDrawable) button.getBackground();
                buttonColor.mutate();
                buttonColor.setColor(Integer.parseInt(buttonJSON.getString("color")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(filename);
    }
    // method to create a random file name

    // callback method

    // permissions from user
    private void requestPermission() {
        ActivityCompat.requestPermissions(Soundboard.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
    // callback method
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(Soundboard.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Soundboard.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}
