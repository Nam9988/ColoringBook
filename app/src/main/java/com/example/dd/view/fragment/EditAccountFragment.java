package com.example.dd.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dd.FileUtil;
import com.example.dd.PermissionUtil;
import com.example.dd.R;
import com.example.dd.util.TextUtil;
import com.example.dd.view.activity.ContainerActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.dd.view.fragment.PaintFragment.GET_FILE_REQUEST_CODE;
import static com.example.dd.view.fragment.PaintFragment.REQUEST_PERMISSION;

public class EditAccountFragment extends BottomSheetDialogFragment {
    @BindView(R.id.img_avt)
    CircleImageView imgAvt;
    @BindView(R.id.img_change_avt)
    ImageView imgChangeAvt;
    @BindView(R.id.btn_change_avt)
    LinearLayout btnChangeAvt;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.rd_male)
    RadioButton rdMale;
    @BindView(R.id.rd_female)
    RadioButton rdFemale;
    @BindView(R.id.edt_dob)
    TextView edtDob;
    @BindView(R.id.btn_calendar)
    ImageView btnCalendar;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private View view;
    private String mCameraPhotoPath;
    private Bitmap bm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_edit_account, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void selectDob() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(getActivity()),
                (datePicker, year1, month1, day1) ->
                        edtDob.setText(TextUtil.generateDay(day1, month1+1,year1)), year, month, day);
        datePickerDialog.show();
    }

    @OnClick(R.id.btn_change_avt)
    public void onBtnChangeAvtClicked() {
        checkPermissionOS6();
    }

    @OnClick(R.id.edt_dob)
    public void onEdtDobClicked() {
        selectDob();
    }

    @OnClick(R.id.btn_register)
    public void onBtnRegisterClicked() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void checkPermissionOS6() {
        if (PermissionUtil.isCameraPermissionOn(getActivity())
                && PermissionUtil.isReadExternalPermissionOn(getActivity())
                && PermissionUtil.isWriteExternalPermissionOn(getActivity())) {
            getPhoto();
            return;
        }
        String[] permissions = {
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_PERMISSION);
    }

    private void getPhoto() {
        // Camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = FileUtil.createImageFile(getActivity());
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
    public void onResume() {
        super.onResume();
        ContainerActivity.getInstance().tbTitle.setText("Chỉnh sửa");
        ContainerActivity.getInstance().btnChangePhoto.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    bm=MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), results[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // bm = BitmapFactory.decodeFile(mCameraPhotoPath);
                mCameraPhotoPath = null;
            }
        } else {
            String imagePath="";
            Uri dataUri = data.getData();
            Hashtable<String, Object> info = FileUtil.getFileInfo(requireActivity(), dataUri);
            imagePath = (String) info.get(FileUtil.ARG_PATH);
            // results = new Uri[]{Uri.fromFile(new File(imagePath))};


            bm = BitmapFactory.decodeFile(imagePath);
        }
        imgAvt.setImageBitmap(bm);
    }
}
