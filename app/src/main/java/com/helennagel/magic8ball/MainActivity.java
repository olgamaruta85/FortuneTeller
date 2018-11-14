package com.helennagel.magic8ball;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView txtAnswer;
    private SensorManager sensorManager;
   private float acelValue;
   private float acelLast;
   private float shake;
   private String[] answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtAnswer = findViewById(R.id.txtAnwser);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE); // access to the sensor service of the device
        //get a reference to the system's accelerometer by invoking getDefaultSensor on the sensor manager
        //registerListener accepts three arguments, the activity's context, a sensor, and the rate at which sensor events are delivered to us.
       sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

        //Earth gravity value setting 9,8
        acelValue = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f; // value determination
        answer = getResources().getStringArray(R.array.answers); // get answer from declared strings in strings.xml

    }

    // menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // activity launching depending on selected menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.magic:
                startActivity(new Intent(this, ShakeActivity.class));
                return true;
        }
        return false;
    }

    // function calculate changes in device position according to three axis. If shake is weak or device ignore one of the axis, user does not get
    // response from application
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            acelLast = acelValue;
            acelValue = (float)Math.sqrt((double)(x*x + y*y + z*z));
            float delta = acelValue - acelLast;
            shake = shake * 0.9f + delta;
            if (shake > 12){
                int randomInt = new Random().nextInt(answer.length); // generate a random number from answer quantity 0...length
                String randomAnswer = answer[randomInt]; // getting answer by the index
                txtAnswer.setText(randomAnswer);// put answer tekst to TextView
            }
        }
        //Called when the accuracy of the registered sensor has changed.
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
