package com.example.htokutomi.battlegame;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Start extends AppCompatActivity {

    //************************************
    // define each components
    //************************************

    // save data
    SharedPreferences sharedPreference;

    // linear layout (characters back ground)
    LinearLayout ll;

    // player A and player B
    ImageView imgA, imgB;

    // animation beam
    ImageView imgBeamA, imgBeamB;

    // timer
    TextView timer;

    // voice of Fight
    TextToSpeech fightSpeech;
    String FIGHT = "Fight";

    // voice of each characters
    TextToSpeech playerA, playerB;
    VoiceClass tempA, tempB;
    // voice words
    String wordsA, wordsB;

    // progress bar
    ProgressBar progressA;
    ProgressBar progressB;

    // Dialog
    AlertDialog.Builder builder;
    AlertDialog dialog;

    // intent
    Intent startIntent, mainIntent;

    // countDownTimer
    CountDownTimer mainTimer, counterTimer;

    // mediaService : hold music
    Intent mediaService;

    // translateAnimation
    Animation animationA, animationB;

    // life points
    int lifeA = 100;
    int lifeB = 100;

    // TIME : fighting time (36sec)
    // INTERVAL : interval (1sec)
    // COUNTDOWN : countdown (4sec)
    // CHECKTIME : checktime (31sec)
    // DAMAGEPOINT : attack point
    // TEXTSIZE_START : text font size (start) -> count down time
    // TEXTSIZE_TIME : text font size (left time)
    // TEXTSIZE_END : text font size (end) -> time up
    final long TIME = 36000;
    final long INTERVAL = 1000;
    final long COUNTDOWN = 4000;
    final long CHECKTIME = 31000;
    final int DAMAGE_POINT = 70;
    final int TEXTSIZE_START = 70;
    final int TEXTSIZE_TIME = 50;
    final int TEXTSIZE_END = 70;

    // flg : attack flg (playerA or playerB)
    boolean flg = true;

    // firstFlg : first animation flag
    boolean firstFlg = true;

    // notificationNum : notification number
    int notificationNum = 0;

    int positionA, positionB;


    //************************************
    // Life cycle
    //************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // *************************
        // set up each components
        // *************************

        // character's back ground
        ll = (LinearLayout)findViewById(R.id.layout);

        // character images
        imgA = (ImageView)findViewById(R.id.imageViewA);
        imgB = (ImageView)findViewById(R.id.imageViewB);

        // beam images
        imgBeamA = (ImageView)findViewById(R.id.beamA);
        imgBeamB = (ImageView)findViewById(R.id.beamB);

        // progressBarA, progressbarB
        progressA = (ProgressBar)findViewById(R.id.progressBarA);
        progressB = (ProgressBar)findViewById(R.id.progressBarB);

        // timer
        timer = (TextView)findViewById(R.id.timer);

        // save text
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        // *************************
        // instantiation
        // *************************

        // (TextToSpeech) setting fight voice
        fightSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener(){
            public void onInit(int status){
                fightSpeech.setLanguage(Locale.CANADA);
                fightSpeech.setPitch(1.0f);
                fightSpeech.setSpeechRate(2.0f);
            }
        });

        // start page and main page
        startIntent = new Intent(this, Start.class);
        mainIntent = new Intent(this, Main.class);

        // music
        mediaService = new Intent(this, MyService.class);

        // get a setting data of character's imdex
        positionA = sharedPreference.getInt("positionA", 0);
        positionB = sharedPreference.getInt("positionB", 0);

        // count timer (4sec)
        counterTimer = new CountDownTimer(COUNTDOWN, INTERVAL) {

            // set up count timer
            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / INTERVAL);
                timer.setTextColor(getResources().getColor(R.color.startFinish));
                timer.setTextSize(TEXTSIZE_START);
            }

            // when this count timer is finished,
            // main timer is started. -> main timer is inside this count timer
            public void onFinish() {

                // "Fight" anounce
                fightSpeech.speak(FIGHT, TextToSpeech.QUEUE_FLUSH, null, null);

                // start music
                startService(mediaService);

                tempA =new VoiceClass(getApplicationContext(), positionA);
                wordsA = tempA.getWords();
                playerA = tempA.textToSpeech();

                tempB =new VoiceClass(getApplicationContext(), positionB + 3);
                wordsB = tempB.getWords();
                playerB = tempB.textToSpeech();

                Log.d("COUNT", "PASS");

                // set up main timer
                timer.setTextColor(getResources().getColor(R.color.timer));
                timer.setTextSize(TEXTSIZE_TIME);

                    // main timer (36sec)
                    mainTimer = new CountDownTimer(TIME, INTERVAL) {
                    // result message for dialog
                    String resultMessage;

                    // main timer starts
                    public void onTick(long millisUntilFinished) {

                        // change time form
                        String showTime = String.format("%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));
                        // show the time
                        timer.setText(showTime);

                        // the fight game is started,
                        // player b is attacked first
                        if (millisUntilFinished > CHECKTIME && firstFlg) {
                            animationB = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_left);
                            imgBeamB.startAnimation(animationB);  // start animation
                            Log.d("SPEECH", "TEST");
                            firstFlg = false;
                        }
                        // every 5 sec, each character is attacked
                        else if ((millisUntilFinished/INTERVAL)%5 == 0 && millisUntilFinished <= CHECKTIME)
                        {
                            // player A's turn
                            if (flg){
                                lifeA = attackPoint(lifeA, progressA);

                                // player A's life point is less then 0
                                if (lifeA <= 0) {
                                    resultMessage = getString(R.string.plyBwin);
                                    // notification
                                    notification(resultMessage);
                                    // result dialog
                                    resultDialog(resultMessage);
                                    mainTimer.cancel();
                                }else{
                                    animationA = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_right);
                                    imgBeamA.startAnimation(animationA);  // start animation
                                    playerA.speak(wordsA, TextToSpeech.QUEUE_FLUSH, null, null);
                                }

                                flg = false;
                            }
                            // player B's turn
                            else{
                                lifeB = attackPoint(lifeB, progressB);

                                // player B's life point is less then 0
                                if (lifeB <= 0 ){
                                    resultMessage = getString(R.string.plyAwin);
                                    // notification
                                    notification(resultMessage);
                                    // result dialog
                                    resultDialog(resultMessage);
                                    mainTimer.cancel();
                                }else{
                                    imgBeamB.startAnimation(animationB);  // start animation
                                    playerB.speak(wordsB, TextToSpeech.QUEUE_FLUSH, null, null);
                                }
                                flg = true;
                            }
                        }
                    }

                    public void onFinish() {
                        timer.setTextColor(getResources().getColor(R.color.startFinish));
                        timer.setText(getString(R.string.timeUp));
                        timer.setTextSize(TEXTSIZE_END);
                        resultMessage = getString(R.string.draw);
                        notification(resultMessage);
                        resultDialog(resultMessage);
                    }
                }.start();
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFECYCLE","onPause");
        // if count down timer keeps going
        if (counterTimer!=null)
            counterTimer.cancel();
        // if main timer keeps going
        if (mainTimer!=null)
            mainTimer.cancel();
        stopService(mediaService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LIFECYCLE", "onResume");
        lifeA = 100;
        lifeB = 100;
        firstFlg = true;
        progressA.setProgress(lifeA);
        progressB.setProgress(lifeB);
        setting();
        // if count down timer keeps going
        if (counterTimer!=null)
            counterTimer.start();
    }

    protected void onDestroy() {
        Log.d("LIFECYCLE", "onDestroy");
        super.onDestroy();
        stopService(mediaService);
    }


    //************************************
    // functions
    //************************************

    // *** OUTLINE ***
    // set up Player A, B and stage
    public void setting()
    {
        String stage = sharedPreference.getString("stage", "");
        String playerA = sharedPreference.getString("playerA", "");
        String playerB = sharedPreference.getString("playerB", "");

        selectStage(stage);
        selectCharacter(playerA, imgA, imgBeamA);
        selectCharacter(playerB, imgB, imgBeamB);
    }

    // *** OUTLINE ***
    // change life point -> reduce the progress bar (life point)
    // *** PARAMETER ***
    // life : life point -> max is 100
    // progress : progress component
    // *** RETURN ***
    // life : left life point
    public int attackPoint(int life, ProgressBar progress)
    {
        Random random = new Random();
        // life point is reduced at random
        life -= random.nextInt(DAMAGE_POINT);
        progress.setProgress(life);
        return life;
    }

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
    // imgBeam : character A's beam or character B's beam image
    public void selectCharacter(String text, ImageView img, ImageView imgBeam){

        // make draw id of stage and beam
        int drawId, drawBeamId;
        String beamName = text + "_beam";

        Log.d("DEBUG", beamName);

        drawId = getResources().getIdentifier(text, "drawable", getPackageName());
        drawBeamId = getResources().getIdentifier(beamName, "drawable", getPackageName());

        // set back ground image and beam image
        img.setImageResource(drawId);
        imgBeam.setImageResource(drawBeamId);

        // beam invisible
        imgBeam.setVisibility(View.INVISIBLE);
    }

    // *** OUTLINE ***
    // result dialog
    // *** PARAMETER ***
    // text : result message
    public void resultDialog(String text)
    {
        // dialog setup
        builder = new AlertDialog.Builder(Start.this);
        builder.setTitle(getString(R.string.result));
        builder.setMessage(text + "\n\n" + getString(R.string.tryAgain));

        // if "Yes" button is selected on this dialog
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

            // reset life point and start the timer
            public void onClick(DialogInterface dialog, int which) {
                lifeA = 100;
                lifeB = 100;
                firstFlg = true;
                progressA.setProgress(lifeA);
                progressB.setProgress(lifeB);
                counterTimer.start();
                dialog.dismiss();
            }
        });

        // if "No" button is selected on this dialog
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
                stopService(mediaService);
                startActivity(mainIntent);
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    // *** OUTLINE ***
    // notification
    // *** PARAMETER ***
    // result : result message
    public void notification(String result)
    {
        notificationNum += 1;

        // get a saved data by setting page
        String stage = sharedPreference.getString("stage", "");
        String playerA = sharedPreference.getString("playerA", "");
        String playerB = sharedPreference.getString("playerB", "");

        // notification message
        String text = "Stage : " + stage
                + "\nplayerA : " + playerA
                + "\nplayerB : " + playerB
                + "\n\n" + result;

        int imageId = R.mipmap.ic_launcher;

        // parent intent
        Intent resultIntent = new Intent(this, Start.class);

        // prepare pending intent
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), notificationNum, resultIntent, 0);

        // prepare notification buider
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(imageId)
                .setContentTitle("Notification")
                .setContentText(text)
                .setContentIntent(resultPendingIntent);

        // prepare notification manager
        NotificationManager nManager = (NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        // notify
        nManager.notify(notificationNum, nBuilder.build());
    }


    //************************************
    // class
    //************************************

    // the setting of character's voice
    public class VoiceClass {

        Context context;
        Locale locale;
        float pitch,rate;
        String words;
        TextToSpeech textToSpeech;

        public VoiceClass(Context context, int position) {
            switch(position)
            {
                // ryu
                case 0:
                    this.locale = Locale.CANADA;
                    this.pitch = 0.4f;
                    this.rate = 1.0f;
                    this.words = "hadoken";
                    break;
                // blanka
                case 1:
                    this.locale = Locale.ENGLISH;
                    this.pitch = 1.5f;
                    this.rate = 1.2f;
                    this.words = "pikachu";
                    break;
                // chunlee
                case 2:
                    this.locale = Locale.CANADA;
                    this.pitch = 1.5f;
                    this.rate = 1.5f;
                    this.words = "senkyakuken";
                    break;
                // akuma
                case 3:
                    this.locale = Locale.ENGLISH;
                    this.pitch = 0.2f;
                    this.rate = 2.5f;
                    this.words = "satuinohado";
                    break;
                // ken
                case 4:
                    this.locale = Locale.CANADA;
                    this.pitch = 0.5f;
                    this.rate = 0.8f;
                    this.words = "shoryuken";
                    break;
                // dhalsim
                case 5:
                    this.locale = Locale.CANADA;
                    this.pitch = 0.3f;
                    this.rate = 1.0f;
                    this.words = "yogaframe";
                    break;
            }
            this.context = context;
        }

        public String getWords() {
            return words;
        }

        public TextToSpeech textToSpeech()
        {
             textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    textToSpeech.setLanguage(locale);
                    textToSpeech.setPitch(pitch);
                    textToSpeech.setSpeechRate(rate);
                }
            });
            return textToSpeech;

        }
    }



}
