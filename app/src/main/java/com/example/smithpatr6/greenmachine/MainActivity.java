package com.example.smithpatr6.greenmachine;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import static android.R.attr.button;


public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private static final String TAG = "Main Activity";


    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;
    private MediaPlayer media;
    private Button startstop;
    private CheckBox soundbox;
    private CheckBox vibratebox;

    private Boolean buttonstatus = false;
    private Boolean sound = true;
    private Boolean vibrate = true;
    private ImageView green;
    private ImageView yellow;
    private ImageView red;

    private AssetManager assets;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private final float greenThreshold = 2;
    private final float yellowThreshold = 5;
    private final float redThreshold = 19;
    private MediaPlayer mediaPlayer;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ, maxrange;

    public Vibrator v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        mediaPlayer= MediaPlayer.create(this, R.raw.losers);



        yellow = (ImageView) findViewById(R.id.yellow);
        yellow.setImageResource(R.drawable.yellow);

        red = (ImageView) findViewById(R.id.red);
        red.setImageResource(R.drawable.red);

        green = (ImageView) findViewById(R.id.green);
        green.setImageResource(R.drawable.green);
        green.bringToFront();

        soundbox = (CheckBox) findViewById(R.id.sound);
        soundbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(soundbox.isChecked()){
                    sound = true;
                }else{
                    sound = false;
                }
            }
        }
        );
        soundbox.setChecked(true);

        vibratebox = (CheckBox) findViewById(R.id.vibrate);
        vibratebox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(vibratebox.isChecked()){
                    vibrate = true;
                }else{
                    vibrate = false;
                }
            }
        }
        );
        vibratebox.setChecked(true);
        startstop = (Button) findViewById(R.id.startstop);


        startstop.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (buttonstatus == false){
                    Log.i(TAG, "OnClick");
                    buttonstatus = true;
                    startstop.setText("End Journey");
                }
                else
                {
                    Log.i(TAG, "OnClick");
                    buttonstatus = false;
                    startstop.setText("Start Journey");
                }
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);



        } else {
            // fai! we dont have an accelerometer!
        }

        //initialize vibration
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void initializeViews() {







    }

    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);



    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {



        if(buttonstatus == true) {
            // clean current values


            // get the change of the x,y,z values of the accelerometer
            deltaX = Math.abs(lastX - event.values[0]);
            deltaY = 0;
            //deltaZ = Math.abs(lastZ - event.values[2]);
            deltaZ = 0;
            // if the change is below 2, it is just plain noise
            if (deltaX < 2)
                deltaX = 0;

            if (((deltaX >= greenThreshold) && (deltaX < yellowThreshold))) {


                yellow.bringToFront();
                //display yellow
            } else if (((deltaX >= yellowThreshold) && (deltaX < redThreshold))) {

                if(vibrate == true) {
                    v.vibrate(50);
                }
                //display red
                if(sound == true){
                    mediaPlayer.start();

                }
                red.bringToFront();

            } else {
                //display green
               green.bringToFront();

            }
        }
    }

}
