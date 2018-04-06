package com.thezili.ohmybaby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by cwjun on 17. 11. 5.
 */

public class FinalResultActivity extends Activity {

    private static final String TAG = "FinalResultActivity";

    private TextView mFinalResultTxt = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_result_activity);

        Log.d(TAG, ">> onCreate()");

        mFinalResultTxt = (TextView) findViewById(R.id.final_result_txt);

        Intent intent = getIntent();
        String finalResult = intent.getStringExtra("strFinalResult");
        mFinalResultTxt.setText(finalResult);

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, ">> onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, ">> onDestroy()");
    }
}
