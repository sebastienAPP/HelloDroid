package com.example.hellodroid;

import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.widget.Button;
import android.view.View;
import android.view.SurfaceView;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;
//import android.content.Context;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Object;
import android.util.Log;

public class HelloDroidActivity extends AppCompatActivity {
    private TextView message;
    private int counter = 0;


    //Context context = this; // or HelloDroidActivity.this
    // User Interface
    private TextView text;


    // QREader
    private SurfaceView mySurfaceView;
    private QREader qrEader;
    private boolean hasCameraPermission = false;
    private static final String cameraPerm = Manifest.permission.CAMERA;



    //System.setOut(new PrintStream(new FileOutputStream("output.txt")));

            //(String.format("%s","k k kk k k k "));



    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return super.openFileOutput(name, mode);
    }

    @Override
    public void setFinishOnTouchOutside(boolean finish) {
        super.setFinishOnTouchOutside(finish);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("********************************","je ne comprends rien");
        // 1. When the app starts, request Camera permission
        //Context context =  HelloDroidActivity.this;

        hasCameraPermission = RuntimePermissionUtil.checkPermissonGranted(HelloDroidActivity.this, cameraPerm);

        message = findViewById(R.id.clickCounter);
        message.setText(String.format("++++++++++++++++++++++++++++%s", "eoeoeoe"));
        // Setup SurfaceView
        // -----------------
        mySurfaceView = findViewById(R.id.camera_view_original);





        text = findViewById(R.id.seb_text);

        System.out.print(hasCameraPermission);

        System.out.print("*************************************************");
        //text = findViewById(R.id.clickCounter);



        // message = findViewById(R.id.clickCounter);
        ImageView droid = findViewById(R.id.houseImage);



        // 2. Handle action button text. With this button you will start/stop the reader
        final Button stateBtn = findViewById(R.id.btn_start_stop);
        message.setText(String.format("%s", " x 0 x 0 x 0"));

        // change of reader state in dynamic
        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   qrEader.hashCode();
                message.setText(String.format("%s", " + 1 + 1 + 1"));
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setupQREader();

                if (qrEader.isCameraRunning()){
                    stateBtn.setText("Start QREader");
                   // qrEader.stop();
                } else {
                    stateBtn.setText("Stop QREader");
                   // qrEader.start();
                }
            }
        });


        stateBtn.setVisibility(View.VISIBLE);

        Button restartButton = findViewById(R.id.btn_restart_activity);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // restartActivity();
            }
        });



        //Define and attach click listener
    droid.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tapDroid();
        }
    });
    }

    void setupQREader() {
        // Init QREader
        // ------------
        Log.i("********************************","on lance le setupQREader  !!");
        message.setText(String.format("%s", " +++++++ ------"));
        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QREader", "Value : " + data);
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(data);
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();
        message.setText(String.format("%s", " xxxxxxxxxxxxxxxxxxx"));
    }

    void restartActivity() {
        startActivity(new Intent(HelloDroidActivity.this, HelloDroidActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (hasCameraPermission) {

            // Cleanup in onPause()
            // --------------------
            qrEader.releaseAndCleanup();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasCameraPermission) {

            // Init and Start with SurfaceView
            // -------------------------------
            qrEader.initAndStart(mySurfaceView);
        }
    }

    private void tapDroid() {
        counter++;
        String countAsText;
        /*
         * In real applications you should not write switch like the one below.
         * Use resource of type "Quantity strings (plurals)" instead.
         * See https://developer.android.com/guide/topics/resources/string-resource#Plurals
         */
        switch (counter) {
            case 1:
                countAsText = "once";
                break;
            case 2:
                countAsText = "twice";
                break;
            default:
                countAsText = String.format("%d times", counter);
        }
        message.setText(String.format("You touched the droid %s", countAsText));
    }

    // Method that handles the result of the permission request made at the beginning of the application
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull final String[] permissions,
            @NonNull final int[] grantResults
    ) {


        Log.i("********************************","on est allé ici !!");

        message.setText(String.format("++++++++++++++++++++%s", "eoeoeoe"));
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted() {
                    if ( RuntimePermissionUtil.checkPermissonGranted(HelloDroidActivity.this, cameraPerm)) {
                        restartActivity();
                    }
                }

                @Override
                public void onPermissionDenied() {
                    // Do nothing
                }
            });
        }
    }
}