package com.example.dd.view.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dd.FileUtil;
import com.example.dd.PaintView;
import com.example.dd.R;
import com.example.dd.view.activity.ContainerActivity;

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
import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yuku.ambilwarna.AmbilWarnaDialog;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaintFragment extends Fragment {
    @BindView(R.id.paint_view1)
    PaintView paintView;
    @BindView(R.id.btn_reset)
    LinearLayout lnreset;
    @BindView(R.id.btn_undo)
    LinearLayout lnundo;
    @BindView(R.id.btn_redo)
    LinearLayout lnredo;
    @BindView(R.id.btn_pick)
    LinearLayout lnpick;
    @BindView(R.id.btn_save)
    LinearLayout lnsave;
    @BindView(R.id.btn_share)
    LinearLayout lnshare;
    @BindView(R.id.frm_paint1)
    FrameLayout fm;
    @BindView(R.id.mode1)
    ImageView mode;
    @BindView(R.id.color)
    ImageView color1;
    @BindView(R.id.size)
    SeekBar size;

    int DefaultColor;
    DisplayMetrics displayMetric;


    private Bitmap bm;
    private Bitmap rsbm;
    String TAG = "OPCV";
    public static final int GET_FILE_REQUEST_CODE = 101;
    public static final int REQUEST_PERMISSION = 102;
    private String mCameraPhotoPath;
    private View view;


    public PaintFragment() {
        // Required empty public constructor
//        lnreset.setOnClickListener(PaintFragment.this);
//        lnundo.setOnClickListener(PaintFragment.this);
//        lnredo.setOnClickListener(PaintFragment.this);
//        lnpick.setOnClickListener(PaintFragment.this);
//        lnsave.setOnClickListener(PaintFragment.this);
//        lnshare.setOnClickListener(PaintFragment.this);

    }

    @OnClick(R.id.btn_reset)
    public void rs() {
        paintView.clear();
    }

    @OnClick(R.id.btn_undo)
    public void undo() {
        paintView.undo();
    }

    @OnClick(R.id.btn_redo)
    public void redo() {
        paintView.redo();
    }

    @OnClick(R.id.btn_pick)
    public void pick() {
        OpenColorPickerDialog(false);
        ;
    }

    @OnClick(R.id.btn_save)
    public void save() {
        saveFrameLayout(fm);
        paintView.clear();
    }

    @OnClick(R.id.btn_share)
    public void share() {

    }

    @OnClick(R.id.mode1)
    public void ChangeMode() {
        paintView.modeFlood();
    }


    //  @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_reset:
//
//                break;
//            case R.id.btn_undo:
//                paintView.undo();
//                break;
//            case R.id.btn_redo:
//                paintView.redo();
//                break;
//            case R.id.btn_pick:
//                break;
//            case R.id.btn_save:
//                break;
//            case R.id.btn_share:
//                break;
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_paint, container, false);
            ButterKnife.bind(this, view);
            displayMetric = new DisplayMetrics();
            displayMetric = getContext().getResources().getDisplayMetrics();
            int a = fm.getHeight();
//        displayMetric.heightPixels=fm.getHeight();
//        displayMetric.widthPixels=fm.getWidth();
            paintView.init(displayMetric);

            size.setMax(100);
            size.setProgress(25);
            size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int i = size.getProgress();
                    paintView.resize(i - 25);
                }
            });
            DefaultColor = paintView.DEFAULT_COLOR;
        }
        return view;

    }

    @Override
    public void onResume() {
        ContainerActivity.getInstance().btnLogout.setVisibility(View.GONE);
        ContainerActivity.getInstance().tbTitle.setText("Paint");
        ContainerActivity.getInstance().btnChangePhoto.setVisibility(View.VISIBLE);
        super.onResume();
    }


    ////Save frame
    public void saveFrameLayout(FrameLayout frameLayout) {
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
            Toast.makeText(getContext(),
                    "Save success: " + newFile.getName(),
                    Toast.LENGTH_LONG).show();
            System.out.println("Name: " + NameFile);
            //quét hình ảnh để hiển thị trong album
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{newFile.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Something wrong 1: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Something wrong 2: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }


    //Color picker

    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(getContext(), DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                DefaultColor = color;

                paintView.setColorPaint(color);
                color1.setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

                Toast.makeText(getContext(), "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }

    public void openSelectPhotoFragment(){
        SelectPhotoFragment fragment = SelectPhotoFragment.newInstance(new SelectPhotoFragment.OnPhotoSelected() {
            @Override
            public void onSelected(int res) {
                Drawable drawable = requireActivity().getDrawable(res);
                if (drawable != null) {
                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                    displayMetric.heightPixels = fm.getHeight();
                    displayMetric.widthPixels = fm.getWidth();
                    paintView.init(displayMetric);
                    paintView.setbm(bitmap);
                }
            }
        });
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,fragment)
                .addToBackStack(this.getClass().getName())
                .commitAllowingStateLoss();
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
            //  onClickSelectPhoto(null);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
//                try {
//                    bm=MediaStore.Images.Media.getBitmap(this.getContentResolver(), results[0]);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                // bm = BitmapFactory.decodeFile(mCameraPhotoPath);
                mCameraPhotoPath = null;
            }
        } else {
            String imagePath = "";
            Uri dataUri = data.getData();
            Hashtable<String, Object> info = FileUtil.getFileInfo(getContext(), dataUri);
            imagePath = (String) info.get(FileUtil.ARG_PATH);
            // results = new Uri[]{Uri.fromFile(new File(imagePath))};

            bm = BitmapFactory.decodeFile(imagePath);
        }
        // Fill root photo into image view
        paintView.clear();

        //  bm = BitmapFactory.decodeFile(imagePath);

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, getContext(), mOpenCVCallBack);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        //  canny.detectEdges(bm);
        //  Bitmap rsbm =detectEdges(bm);

        // Glide.with(this)
        //        .load(rsbm).fitCenter()
        //       .into(imgBackground);
        displayMetric.heightPixels = fm.getHeight();
        displayMetric.widthPixels = fm.getWidth();
        paintView.init(displayMetric);
        paintView.setbm(rsbm);
        // paintView.init();
    }


    private Bitmap detectEdges(Bitmap bitmap) {
        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, 80, 100);

        Core.bitwise_not(edges, edges);


        // Don't do that at home or work it's for visualization purpose.
        // BitmapHelper.showBitmap(this, bitmap, imageView);
        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, resultBitmap);


        return resultBitmap;
    }

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    rsbm = detectEdges(bm);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


}
