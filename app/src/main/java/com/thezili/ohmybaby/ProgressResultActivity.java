package com.thezili.ohmybaby;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by cwjun on 17. 11. 5.
 */

public class ProgressResultActivity extends Activity {

    private static final String TAG = "ProgressResultActivity";

    private String mPhotoPath = null;
    private long totalSize = 0;

    private ImageView mProgressImg = null;
//    private TextView mProgressResultTxt = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_result_activity);

        Log.d(TAG, ">> onCreate()");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

//        mProgressImg = (ImageView) findViewById(R.id.progress_img);

        Intent intent = getIntent();
        String photoPath = intent.getStringExtra("Photo_Path");
        mPhotoPath = photoPath;

        Toast.makeText(getApplicationContext(), "Recv PhotoPath", Toast.LENGTH_SHORT).show();
        new UploadFileToServer().execute();
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

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
//            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
//            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
//            progressBar.setProgress(progress[0]);

            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(String... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
//            Toast.makeText(getApplicationContext(), "uploadFile", Toast.LENGTH_SHORT).show();

            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://182.221.33.99:9999/upload_image");

            //HttpResponse response;
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(mPhotoPath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.thezili.com"));
                entity.addPart("email", new StringBody("thezilie.changwook@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                //response = httpclient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    //Log.d("Response of GET request", response.toString());
                    //Log.d("Response of POST request", responseString);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
//            showAlert(result);

            Toast.makeText(getApplicationContext(), "onPostExecute", Toast.LENGTH_SHORT).show();

//            mProgressResultTxt.setText("Loding...");
            Intent intent = new Intent(ProgressResultActivity.this, FinalResultActivity.class);
            intent.putExtra("strFinalResult", result);
            startActivity(intent);
            super.onPostExecute(result);
        }
    }
}
