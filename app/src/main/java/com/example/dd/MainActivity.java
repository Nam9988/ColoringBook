package com.example.dd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PaintView paintView;
    private ImageView imgBackground;
    private ImageView btnPaint;
    private ImageView btnClear;
    private ImageView btnUndo;
    private ImageView btnSave;
    private FrameLayout fmLayout;

    private ColorAdapter colorAdapter;
    private RecyclerView rvColor;
    private List<Integer> colors;
    private Bitmap bm;
    private Bitmap rsbm;
    String TAG="OPCV";
    private int size=0;

    private static final int GET_FILE_REQUEST_CODE = 101;
    private static final int REQUEST_PERMISSION = 102;


    private String mCameraPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mImgRootPhoto = (ImageView) findViewById(R.id.img_root_photo);
//        mImgResultPhoto = (ImageView) findViewById(R.id.img_result_photo);
        paintView = findViewById(R.id.paint_view);
        imgBackground = findViewById(R.id.img_background);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
        paintView.normal();
        init();
        initRecyclerView();

    }

    public void onClickSelectPhoto(View view) {
        checkPermissionOS6();
    }

    private void checkPermissionOS6() {
        if (PermissionUtil.isCameraPermissionOn(this)
                && PermissionUtil.isReadExternalPermissionOn(this)
                && PermissionUtil.isWriteExternalPermissionOn(this)) {
            getPhoto();
            return;
        }
        String[] permissions = {
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
    }

    private void getPhoto() {
        // Camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = FileUtil.createImageFile(this);
            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
        } catch (IOException e) {
            mCameraPhotoPath = null;
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

        // Gallery
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.addCategory(Intent.CATEGORY_OPENABLE);
        gallery.setType("image/*");

        Intent[] intents;
        if (cameraIntent != null && mCameraPhotoPath != null) {
            intents = new Intent[]{cameraIntent};
        } else {
            intents = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, gallery);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);

        startActivityForResult(chooserIntent, GET_FILE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            onClickSelectPhoto(null);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != GET_FILE_REQUEST_CODE || resultCode != RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            mCameraPhotoPath = null;
            return;
        }
        Uri[] results = null;



        if (data == null || data.getData() == null) {
            // If there is not data, then we may have taken a photo
            if (mCameraPhotoPath != null) {
                results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                try {
                    bm=MediaStore.Images.Media.getBitmap(this.getContentResolver(), results[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
               // bm = BitmapFactory.decodeFile(mCameraPhotoPath);
                mCameraPhotoPath = null;
            }
        } else {
            String imagePath="";
            Uri dataUri = data.getData();
            Hashtable<String, Object> info = FileUtil.getFileInfo(this, dataUri);
            imagePath = (String) info.get(FileUtil.ARG_PATH);
           // results = new Uri[]{Uri.fromFile(new File(imagePath))};


            bm = BitmapFactory.decodeFile(imagePath);
        }
        // Fill root photo into image view
        paintView.clear();

      //  bm = BitmapFactory.decodeFile(imagePath);

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mOpenCVCallBack);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
      //  canny.detectEdges(bm);
      //  Bitmap rsbm =detectEdges(bm);

        Glide.with(this)
                .load(rsbm).fitCenter()
                .into(imgBackground);
    }



    private Bitmap detectEdges(Bitmap bitmap) {
        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, 80, 100);

        Core.bitwise_not(edges,edges);


        // Don't do that at home or work it's for visualization purpose.
       // BitmapHelper.showBitmap(this, bitmap, imageView);
        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, resultBitmap);
       // BitmapHelper.showBitmap(this, resultBitmap, detectEdgesImageView);
        Bitmap resultBitmap1 = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Bitmap resultBitmap2 = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);



//        for(int y = 0; y < resultBitmap.getHeight(); y++){
//            for(int x = 0; x < resultBitmap.getWidth(); x++){
//
//                int pixel = resultBitmap.getPixel(x,y);
//
//                if (pixel == Color.WHITE){
//                    resultBitmap1.setPixel(x, y, Color.BLACK);
//                } else {
//                    resultBitmap1.setPixel(x, y, Color.WHITE);
//                }
//            }
//        }


        return resultBitmap;
    }



    private void getColors() {
        colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.BLACK);
        colors.add(Color.RED);
        colors.add(Color.CYAN);
        colors.add(Color.WHITE);
    }

    private void init() {
        btnPaint = findViewById(R.id.btn_paint);
        btnClear = findViewById(R.id.btn_clear);
        btnUndo = findViewById(R.id.btn_undo);
        btnSave= findViewById(R.id.btn_save);
        fmLayout=findViewById(R.id.frm_paint);
        btnPaint.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnUndo.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        rvColor = findViewById(R.id.rv_color);

    }

    private void initRecyclerView() {
        getColors();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        colorAdapter =new ColorAdapter(this, colors);
        colorAdapter.setOnColorSelected(new ColorAdapter.OnColorSelected() {
            @Override
            public void onSelected(int color) {
             //   Toast.makeText(MainActivity.this,String.valueOf(color),Toast.LENGTH_LONG).show();
                paintView.setColorPaint(color);
            }
        });
        rvColor.setLayoutManager(llm);
        rvColor.setAdapter(colorAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_save:
                saveFrameLayout(fmLayout);
                //Toast.makeText(MainActivity.this,"Click",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_paint:
                size++;
                size=size%3;
                paintView.resize(size);
                Toast.makeText(MainActivity.this,String.valueOf(size),Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_clear:
                paintView.clear();
                break;
            case R.id.btn_undo:
                paintView.undo();
                break;


        }
    }



    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    rsbm=detectEdges(bm);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    public  void saveFrameLayout(FrameLayout frameLayout) {
        frameLayout.setDrawingCacheEnabled(true);
        frameLayout.buildDrawingCache();
        Bitmap cache = frameLayout.getDrawingCache();
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/DCIM/DemoSVMC");
        dir.mkdir();
        String NameFile = "" + System.currentTimeMillis();
        File newFile = new File(dir, NameFile + ".jpg");

        try {
            OutputStream fileOutputStream = new FileOutputStream(newFile);
            cache.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(MainActivity.this,
                    "Save success: " + newFile.getName(),
                    Toast.LENGTH_LONG).show();
            System.out.println("Name: " + NameFile);
            //quét hình ảnh để hiển thị trong album
            MediaScannerConnection.scanFile(this,
                    new String[]{newFile.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,
                    "Something wrong 1: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,
                    "Something wrong 2: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.normal:
//                paintView.normal();
//                return true;
//            case R.id.emboss:
//                paintView.emboss();
//                return true;
//            case R.id.blur:
//                paintView.blur();
//                return true;
//            case R.id.clear:
//                paintView.clear();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
