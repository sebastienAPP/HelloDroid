// \app\src\main\java\com\yourcompany\yourapp\MainActivity.java
package com.example.hellodroid;

        import android.Manifest;
        import android.content.Intent;
        import android.os.Bundle;
        //import android.support.annotation.NonNull;
        import androidx.annotation.NonNull;
        //import androidx.annotation.StringRes;
        //import android.support.v7.app.AppCompatActivity;
        import androidx.appcompat.app.AppCompatActivity;
        import android.util.Log;
        import android.widget.TextView;
        import android.widget.Button;
        import android.view.View;
        import android.view.SurfaceView;
        import github.nisrulz.qreader.QRDataListener;
        import github.nisrulz.qreader.QREader;

public class MainActivity extends AppCompatActivity {

    // User Interface
    private TextView text;

    // QREader
    private SurfaceView mySurfaceView;
    private QREader qrEader;
    private boolean hasCameraPermission = false;
    private static final String cameraPerm = Manifest.permission.CAMERA;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondview);

        // 1. When the app starts, request Camera permission
        hasCameraPermission = RuntimePermissionUtil.checkPermissonGranted(this, cameraPerm);

        text = findViewById(R.id.code_info_original);


        // 2. Handle action button text. With this button you will start/stop the reader
        final Button stateBtn = findViewById(R.id.btn_start_stop);
        // change of reader state in dynamic
        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrEader.isCameraRunning()) {
                    stateBtn.setText("Start QREader");
                    qrEader.stop();
                } else {
                    stateBtn.setText("Stop QREader");
                    qrEader.start();
                }
            }
        });


        stateBtn.setVisibility(View.VISIBLE);

        Button restartButton = findViewById(R.id.btn_restart_activity);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartActivity();
            }
        });

        // Setup SurfaceView
        // -----------------
        mySurfaceView = findViewById(R.id.camera_view_original);

        if (hasCameraPermission) {
            // Setup QREader
            setupQREader();
        } else {
            RuntimePermissionUtil.requestPermission(
                    MainActivity.this,
                    cameraPerm,
                    100
            );
        }
    }

    void restartActivity() {
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    void setupQREader() {
        // Init QREader
        // ------------
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

    // Method that handles the result of the permission request made at the beginning of the application
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull final String[] permissions,
            @NonNull final int[] grantResults
    ) {
        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted() {
                    if ( RuntimePermissionUtil.checkPermissonGranted(MainActivity.this, cameraPerm)) {
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


