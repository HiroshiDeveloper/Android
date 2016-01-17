package com.example.htokutomi.battlegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Setting extends AppCompatActivity {

    //************************************
    // define each components
    //************************************

    // linear layout (characters back ground)
    LinearLayout ll;

    // player A and player B
    ImageView imgA, imgB;

    // drop down list (Stage)
    Spinner spnStage;

    // drop down list (PlayerA)
    Spinner spnPlayerA;

    // drop down list (PlayerB)
    Spinner spnPlayerB;

    // save data
    SharedPreferences sharedPreference;

    // read saved data
    SharedPreferences.Editor editor;

    // Dialog
    AlertDialog.Builder builder;
    AlertDialog dialog;

    // Intent
    Intent mainIntent;

    // stageSharedPreference : stage name
    // plyASharedPreference : character name (player A)
    // plyBSharedPreference : character name (player B)
    String stageSharedPreferences, stageSelected; //default value
    String plyASharedPreference, plyASelected;
    String plyBSharedPreference, plyBSelected;

    // pos : indent of dropdown( stage )
    // pos A : indent of dropdown( playerA )
    // pos B : indent of dropdown( playerB )
    int pos = 0;
    int posA = 0;
    int posB = 0;


    //************************************
    // Life cycle
    //************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // *************************
        // set up each components
        // *************************

        // character's back ground
        ll = (LinearLayout)findViewById(R.id.layout);

        // character images
        imgA = (ImageView)findViewById(R.id.imageViewA);
        imgB = (ImageView)findViewById(R.id.imageViewB);

        // save text
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // write text
        editor = sharedPreference.edit();

        // (Spinner) setting stage
        spnStage = (Spinner)findViewById(R.id.spnStage);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.stage, R.layout.spinner_custom);
        adapter.setDropDownViewResource(R.layout.spinner_item_center);
        spnStage.setAdapter(adapter);
        spnStage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // when spinner is selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stageSelected = spnStage.getSelectedItem().toString();
                // change the stage
                selectStage(stageSelected);
                if (pos!=0){
                    Toast.makeText(getApplicationContext(), "Stage : "+stageSelected, Toast.LENGTH_SHORT).show();
                }
                pos=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spnStage.setSelection(sharedPreference.getInt("position", 0));


        // get orientation information
        int display_mode = getResources().getConfiguration().orientation;


        // (Spinner) setting playerA
        spnPlayerA = (Spinner)findViewById(R.id.spnPlayerA);

        ArrayAdapter adapterA;
        // orientation is vertical
        if (display_mode == 1){
            adapterA = ArrayAdapter.createFromResource(this, R.array.playerA, R.layout.spinner_item_left);
        }
        // orientation is horizontal
        else{
            adapterA = ArrayAdapter.createFromResource(this, R.array.playerA, R.layout.spinner_custom);
        }
        adapterA.setDropDownViewResource(R.layout.spinner_item_left);
        spnPlayerA.setAdapter(adapterA);
        spnPlayerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // when spinner is selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                plyASelected = spnPlayerA.getSelectedItem().toString();
                selectCharacter(plyASelected, imgA);
                if (posA!=0){
                    Toast.makeText(getApplicationContext(), "PlayerA : "+plyASelected, Toast.LENGTH_SHORT).show();
                }
                posA=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spnPlayerA.setSelection(sharedPreference.getInt("positionA", 0));


        // (Spinner) setting playerB
        spnPlayerB = (Spinner)findViewById(R.id.spnPlayerB);

        ArrayAdapter adapterB;
        // orientation is vertical
        if (display_mode == 1){
            adapterB = ArrayAdapter.createFromResource(this, R.array.playerB, R.layout.spinner_item_right);
        }
        // orientation is horizontal
        else{
            adapterB = ArrayAdapter.createFromResource(this, R.array.playerB, R.layout.spinner_custom);
        }

        adapterB.setDropDownViewResource(R.layout.spinner_item_right);
        spnPlayerB.setAdapter(adapterB);
        spnPlayerB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // when spinner is selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                plyBSelected = spnPlayerB.getSelectedItem().toString();
                selectCharacter(plyBSelected, imgB);
                if(posB!=0){
                    Toast.makeText(getApplicationContext(), "PlayerB : "+plyBSelected, Toast.LENGTH_SHORT).show();
                }
                posB=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spnPlayerB.setSelection(sharedPreference.getInt("positionB", 0));


        // *************************
        // instantiation
        // *************************

        // intent
        mainIntent = new Intent(this, Main.class);
    }


    //************************************
    // functions
    //************************************

    // *** OUTLINE ***
    // when stage's drop down is changed, stage is changed
    // *** PARAMETER ***
    // stage name : stage name (stage1, stage2 etc)
    public void selectStage(String stageName)
    {
        // make draw id of stage image
        int drawStageId;
        drawStageId = getResources().getIdentifier(stageName, "drawable",  getPackageName());
        // change back ground image
        ll.setBackgroundResource(drawStageId);
    }

    // *** OUTLINE ***
    // when character's drop down is changed, character is changed
    // *** PARAMETER ***
    // charName : character name
    // img : character A or character B
    public void selectCharacter(String charName, ImageView img)
    {
        // make draw id of character image
        int drawCharId;
        drawCharId = getResources().getIdentifier(charName, "drawable", getPackageName());

        // change character
        img.setImageResource(drawCharId);
    }

    // *** OUTLINE ***
    // confirmation dialog
    public void confirmDialog(View v)
    {
        // stage : stage name
        // plyA : character name (player A)
        // plyB : character name (player B)
        stageSharedPreferences = spnStage.getSelectedItem().toString();
        plyASharedPreference = spnPlayerA.getSelectedItem().toString();
        plyBSharedPreference = spnPlayerB.getSelectedItem().toString();

        // dialog
        builder = new AlertDialog.Builder(Setting.this);
        builder.setTitle(getString(R.string.confirm));
        builder.setMessage(
                "Stage : "+stageSharedPreferences
                +"\n\nPlayerA : "+plyASharedPreference
                +"\nPlayerB : "+plyBSharedPreference
                +"\n\n"+getString(R.string.confirmMessage)
        );

        // if "Yes" button is selected on this dialog
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                editor.putString("stage", stageSharedPreferences);
                editor.putString("playerA", plyASharedPreference);
                editor.putString("playerB", plyBSharedPreference);
                editor.putInt("position", pos);
                editor.putInt("positionA",posA);
                editor.putInt("positionB",posB);
                editor.commit();
                startActivity(mainIntent);
            }

        });
        // if "No" button is selected on this dialog
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }
}







