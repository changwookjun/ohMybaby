package com.thezili.ohmybaby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button camera_btn = null;
    private Button opencv_btn = null;
    private Button upload_btn = null;
    private Button shake_btn = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_view);

        camera_btn = (Button) findViewById(R.id.camera);
        opencv_btn = (Button) findViewById(R.id.opencv);
        upload_btn = (Button) findViewById(R.id.upload);
        shake_btn = (Button) findViewById(R.id.shake);

        camera_btn.setOnClickListener(this);
        opencv_btn.setOnClickListener(this);
        upload_btn.setOnClickListener(this);
        shake_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.camera :
                Intent cIntent = new Intent(this, TakeCameraActivity.class);
                startActivity(cIntent);
                break;

            case R.id.opencv :
                Intent oIntent = new Intent(this, ResultCameraACtivity.class);
                startActivity(oIntent);
                break;

            case R.id.upload :
                Intent uIntent = new Intent(this, SelectUploadActivity.class);
                startActivity(uIntent);
                break;

            case R.id.shake :
                Intent sIntent = new Intent(this, ShakeActivity.class);
                startActivity(sIntent);
                break;
        }
    }
}
