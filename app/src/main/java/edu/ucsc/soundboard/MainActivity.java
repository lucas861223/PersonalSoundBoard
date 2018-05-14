package edu.ucsc.soundboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToSetting(View view){
        Intent settingScreen = new Intent(getApplicationContext(), Settings.class);
        startActivity(settingScreen);
    }

    public void makeNewBoard(View view){
        Intent board = new Intent(getApplicationContext(), Soundboard.class);
        board.putExtra("boardjson", Soundboard.emptyBoardJSON().toString());
        startActivity(board);
    }

    public void loadBoard(View view){
        Intent settingScreen = new Intent(getApplicationContext(), Settings.class);
        startActivity(settingScreen);
    }
}
