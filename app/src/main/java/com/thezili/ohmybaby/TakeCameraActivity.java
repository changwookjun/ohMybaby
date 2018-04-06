package com.thezili.ohmybaby;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TakeCameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private final static String TAG = "TakeCameraActivity";

    private Context mContext = null;
    //Note2 해상도에 맞춤.
/*    private static final int   IMAGE_WIDTH = 1280; // 찍을 넓이
    private static final int   IMAGE_HEIGHT = 720; // 찍을 높이*/
    //private static final int   IMAGE_WIDTH = 1440; // 찍을 넓이
    //private static final int   IMAGE_HEIGHT = 2560; // 찍을 높이

    private RelativeLayout mHelpGuideActivity = null;
    private RelativeLayout mTakeCameraActivity = null;

    private ImageView mGuideImg = null;
    private ImageView mTakePicBtn = null;
    private ImageView mBackBtn = null;
    private ImageView mTipBtn = null;
    private ImageView mResultView = null;

    private Camera.Parameters parameters;
    List<int[]> mFpsRange;
//    private ImageView mHeadGuideImg = null;

    @SuppressWarnings("deprecation")
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private String str;
    private int mWidth = 0;
    private int mHeight = 0;

    private Boolean isFirst = false;
    private SharedPreferences mSharedPref = null;
    private Camera.Size optimalSize;

    LayoutInflater controlInflater = null;

    @SuppressWarnings("deprecation")
    Camera.PictureCallback mJpegCallback;
    Camera.ShutterCallback mShutterCallback;
    Camera.AutoFocusCallback mAutoFocusCallback;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);*/

        setContentView(R.layout.take_camera_activity);

        mContext = this;

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);

        Intent intent = getIntent();
        isFirst = intent.getBooleanExtra("isFirst", false);

        initLayout();

        mBackBtn.setOnClickListener(this);
        mTakePicBtn.setOnClickListener(this);
        mTipBtn.setOnClickListener(this);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        mSurfaceView = (SurfaceView) findViewById(R.id.main_camera_view);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        //mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.setKeepScreenOn(true);

        controlInflater = LayoutInflater.from(getBaseContext());

        mShutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                Log.d(TAG, "onShutter!!");
            }
        };

        mAutoFocusCallback = new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {

            }
        };


        mJpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                FileOutputStream outStream = null;
                str = String.format("/sdcard/%d.jpg", System.currentTimeMillis());
                try {
                    outStream = new FileOutputStream(str);

                        //outStream.write(data);
                        //outStream.close();


                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    Log.e(TAG, " ==width : " + width + " ==height : " + height);

                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(),
                            rotateMatrix, false);

                    Bitmap resized = Bitmap.createScaledBitmap(rotatedBitmap, mWidth, mHeight, true);
                    //ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    resized.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // convert new compressed bitmap in a byte []
                /*byte[] array = bos.toByteArray();

                Uri uriTarget = getContentResolver().insert(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new ContentValues());

                OutputStream imageFileOS;
                try {
                    imageFileOS = getContentResolver().openOutputStream(
                            uriTarget);
                    imageFileOS.write(array);
                    imageFileOS.flush();
                    imageFileOS.close();
                    Toast.makeText(TakeCameraActivity.this,
                            "Image saved: " + uriTarget.toString(),
                            Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/

                /*Toast.makeText(getApplicationContext(),
                        "Picture Saved", Toast.LENGTH_LONG).show();*/

                Log.e(TAG, " mWidth : " + mWidth + " mHeight : " + mHeight);
                Intent intent = new Intent(TakeCameraActivity.this, ResultCameraACtivity.class);
                intent.putExtra("strParamName", str);
                intent.putExtra("intWidth", mWidth);
                intent.putExtra("intHeight", mHeight);

                startActivity(intent);

            }
        };
    }

    private void initLayout() {
        Log.d(TAG, ">> initLayout()");

//        mHelpGuideActivity = (RelativeLayout) findViewById(R.id.help_guide_view);
//        mTakeCameraActivity = (RelativeLayout) findViewById(R.id.take_camera_view);
//        mHeadGuideImg = (ImageView) findViewById(R.id.head_guide_img);

        mTakePicBtn = (ImageView) findViewById(R.id.take_pic_btn);
        mGuideImg = (ImageView) findViewById(R.id.guide_img);
        mBackBtn = (ImageView) findViewById(R.id.back_btn);
        mTipBtn = (ImageView) findViewById(R.id.tip_btn);
        mResultView = (ImageView) findViewById(R.id.result_view);

        mGuideImg.bringToFront();
        mBackBtn.bringToFront();

        if(isFirst) {

//            mHelpGuideActivity.setVisibility(View.VISIBLE);
//            mTakeCameraActivity.setVisibility(View.GONE)
//            mHeadGuideImg.setVisibility(View.GONE);

            mGuideImg.setVisibility(View.VISIBLE);
            mBackBtn.setVisibility(View.VISIBLE);
            mTakePicBtn.setVisibility(View.GONE);
            mTipBtn.setVisibility(View.GONE);

            isFirst = false;
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();

        } else {
            mGuideImg.setVisibility(View.GONE);
            mBackBtn.setVisibility(View.GONE);

            mTakePicBtn.setVisibility(View.VISIBLE);
            mTipBtn.setVisibility(View.VISIBLE);

//            mHeadGuideImg.setVisibility(View.VISIBLE);
//            mHelpGuideActivity.setVisibility(View.GONE);
//            mTakeCameraActivity.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_pic_btn :
                Log.d(TAG, ">> eunsook == take_pic_btn()");

                mCamera.autoFocus(mAutoFocusCallback);
                mCamera.takePicture(null, null, mJpegCallback);
                break;

            case R.id.back_btn :
                mGuideImg.setVisibility(View.GONE);
                mBackBtn.setVisibility(View.GONE);
                mTipBtn.setVisibility(View.VISIBLE);
                mTakePicBtn.setVisibility(View.VISIBLE);

//                mHeadGuideImg.setVisibility(View.VISIBLE);
//                mHelpGuideActivity.setVisibility(View.GONE);
//                mTakeCameraActivity.setVisibility(View.VISIBLE);
                break;

            case R.id.tip_btn :
                mGuideImg.setVisibility(View.VISIBLE);
                mBackBtn.setVisibility(View.VISIBLE);
                mTipBtn.setVisibility(View.GONE);
                mTakePicBtn.setVisibility(View.GONE);

//                mHeadGuideImg.setVisibility(View.GONE);
//                mHelpGuideActivity.setVisibility(View.VISIBLE);
//                mTakeCameraActivity.setVisibility(View.GONE);

                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        mGuideImg.setVisibility(View.GONE);
        mBackBtn.setVisibility(View.GONE);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) width / height;
        if (sizes == null) {
            return null;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue;
            }
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        Log.i("optimal size", ""+optimalSize.width+" x "+optimalSize.height);
        return optimalSize;
    }

        @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @SuppressWarnings("deprecation")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {}

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
        Log.e(TAG, " mWidth : " + mWidth + " mHeight : " + mHeight);
        if (mCamera != null) {
            freeCameraResource();
        }
        try {
            mCamera = Camera.open();
            if (mCamera == null)
                return;
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            parameters = mCamera.getParameters();// 获得相机参数

            List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
            List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
            optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                    mSupportedPreviewSizes, height, width);

            parameters.setPreviewSize(optimalSize.width, optimalSize.height); // 设置预览图像大小

            parameters.set("orientation", "portrait");
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mFpsRange =  parameters.getSupportedPreviewFpsRange();

            mCamera.setParameters(parameters);// 设置相机参数
            mCamera.startPreview();// 开始预览


        }catch (Exception io){
            io.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    private void freeCameraResource() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

}