package com.example.htokutomi.calculator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // txtView : input, output
    TextView txtView;

    // rl : layout
    LinearLayout rl;

    // alt : temporary number (to combine numbers)
    String alt = "";

    // tmpa : It's number A before pressing mark(+, -, *, /)
    // tmpb : It's number B after pressing mark(+, -, *, /)
    float tmpa = 0;
    float tmpb = 0;

    // mark : +, -, *, /
    String mark = "";

    // checkColor : change the back ground color (white or black)
    // white is false, black is true
    Boolean checkColor = true;
    

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = (LinearLayout)findViewById(R.id.myLayout);
        rl.setBackgroundColor(Color.WHITE);

        // define textView id
        txtView = (TextView)findViewById(R.id.txtView);

        // if saved data is not null,
        // get a data (input and output data), and then this data is displayed
        if (savedInstanceState != null){
            txtView.setText(savedInstanceState.getString("data"));
        }

    }

    // *** combine numbers
    // when number button is pressed, number is combined.
    // for example
    // "1" is pressed first, and then "2" is pressed.
    // Result is "12"
    public boolean addNumbers(View v)
    {
        Button btn = (Button)v;

        if ((btn.getText()).equals("0") && alt.equals("")) {
            // if first number is 0, this number is not added.
            return false;
        }

        System.out.println(btn.getText());
        System.out.println(alt);
        System.out.println("TEST");

        alt += btn.getText();
        txtView.setText(alt);

        return true;
    }

    // *** save number A and mark
    // when mark(+, -, *, /) is pressed, save the numberA and mark
    public void markSet(View v)
    {
        Button btn = (Button)v;

        String check = (txtView.getText()).toString();

        // not to set first number and mark first
        // R.string.app_name : CalQlator
        if (!check.equals(getString(R.string.label_name))){
            tmpa = Float.parseFloat(check);
            mark = (btn.getText()).toString();
            alt = "";
        }
    }

    // *** clear method ***
    // when "C" is pressed, every data is initialized
    public void clear(View v)
    {
        alt = "";
        tmpa = 0;
        tmpb = 0;
        txtView.setText(R.string.label_name);
    }

    // *** compute method ***
    // when "=" is pressed, compute number a and b
    public void calculate(View v)
    {
        float result = 0;
        if (!mark.equals("")){
            tmpb = Float.parseFloat(alt);

            if (mark.equals("+"))
            {
                result = tmpa + tmpb;
            }else if (mark.equals("-"))
            {
                result = tmpa - tmpb;
            }else if (mark.equals("*"))
            {
                result = tmpa * tmpb;
            }else if (mark.equals("/"))
            {
                result = tmpa / tmpb;
            }
            txtView.setText(result+"");
        }

        tmpa = 0;
        tmpb = 0;
        alt = "";
        mark = "";
    }

    // *** change color method ***
    // when image is pressed, back ground color is changed
    public void changeColor(View v){

        if (checkColor){
            rl.setBackgroundColor(Color.DKGRAY);
            txtView.setTextColor(Color.YELLOW);
            checkColor = false;
        }
        else{
            rl.setBackgroundColor(Color.WHITE);
            txtView.setTextColor(Color.BLACK);
            checkColor = true;
        }
    }

    // *** save the data ***
    // if a gadget is changed orientation,
    // input and output data is saved
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the values you need from your textview into "outState"-object
        super.onSaveInstanceState(outState);
        outState.putString("data", (txtView.getText()).toString());
    }


}
