package com.example.htokutomi.dice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final String DEBUG = "LIFE_CYCLE";

    TextView txtPlyResult, txtComResult, txtResult;
    Button button;
    ImageView imgPlayer, imgComputer;
    int countPly = 0;
    int countCom = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set the first picture
        // player
        imgPlayer = (ImageView) findViewById(R.id.imgPlayer);
        imgPlayer.setImageResource(R.drawable.player);

        // computer
        imgComputer = (ImageView) findViewById(R.id.imgComputer);
        imgComputer.setImageResource(R.drawable.computer);


        // set the start button
        button = (Button)findViewById(R.id.button);

        // set the result text
        txtResult = (TextView)findViewById(R.id.txtResult);
        txtResult.setText("Let's play");

        txtPlyResult = (TextView)findViewById(R.id.txtPlyResult);
        txtComResult = (TextView)findViewById(R.id.txtComResult);

    }

    public void playGames(View v)
    {
        Log.d(DEBUG,"start button");

        Random rand = new Random();
        int numPly = rand.nextInt(5) + 1;
        int numCom = rand.nextInt(5) + 1;

        Log.d(DEBUG,"debug");

        int[] aryImg = new int[6];
        aryImg[0] = R.drawable.dice1;
        aryImg[1] = R.drawable.dice2;
        aryImg[2] = R.drawable.dice3;
        aryImg[3] = R.drawable.dice4;
        aryImg[4] = R.drawable.dice5;
        aryImg[5] = R.drawable.dice6;

        Log.d(DEBUG,"debug");

        Log.d(DEBUG, numPly+"");
        Log.d(DEBUG, numCom + "");

        Log.d(DEBUG, aryImg[numPly - 1] + "");

        imgPlayer.setImageResource(aryImg[numPly - 1]);
        imgComputer.setImageResource(aryImg[numCom-1]);

        Log.d(DEBUG, "debug");

        if (numPly < numCom)
        {
            countCom += 1;
            txtResult.setText("Computer win");

        }
        else if (numPly > numCom)
        {
            countPly += 1;
            txtResult.setText("Player win");
        }
        else{
            txtResult.setText("Draw");
        }

        txtPlyResult.setText(countPly+"");
        txtComResult.setText(countCom+"");

    }

    public void clearGames(View v)
    {
        countCom = 0;
        countPly = 0;
        imgPlayer.setImageResource(R.drawable.player);
        imgComputer.setImageResource(R.drawable.computer);
        txtPlyResult.setText(countPly+"");
        txtComResult.setText(countCom+"");
        txtResult.setText("Let's play");
    }


}
