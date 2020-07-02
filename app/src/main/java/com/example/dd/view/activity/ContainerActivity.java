package com.example.dd.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.dd.PermissionUtil;
import com.example.dd.R;
import com.example.dd.view.fragment.AccountFragment;
import com.example.dd.view.fragment.NewFeedFragment;
import com.example.dd.view.fragment.PaintFragment;
import com.example.dd.view.fragment.SelectPhotoFragment;
import com.example.dd.view.fragment.SelectTypePhotoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dd.view.fragment.PaintFragment.GET_FILE_REQUEST_CODE;
import static com.example.dd.view.fragment.PaintFragment.REQUEST_PERMISSION;

public class ContainerActivity extends AppCompatActivity {
    BottomNavigationView bottomNav;
    private static ContainerActivity instance;
    @BindView(R.id.tb_title)
    public TextView tbTitle;
    @BindView(R.id.btn_change_photo)
    public ImageView btnChangePhoto;
    @BindView(R.id.tb)
    public RelativeLayout tb;
    private PaintFragment paintFragment;

    public static ContainerActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);
        instance = this;
        paintFragment = new PaintFragment();
        loadFragment(paintFragment);
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnu_paint:
                        loadFragment(paintFragment);
                        return true;
                    case R.id.mnu_news:
                        loadFragment(new NewFeedFragment());
                        return true;
                    case R.id.mnu_account:
                        loadFragment(new AccountFragment());
                        return true;


                }
                return false;
            }
        });

        btnChangePhoto.setOnClickListener(v ->{
            showSelectType();
        });
    }

    private void showSelectType() {
        SelectTypePhotoFragment fragment = new SelectTypePhotoFragment(new SelectTypePhotoFragment.OnTypeSelected() {
            @Override
            public void onTypeClicked(int type) {
                if (type == 1){
                    if (paintFragment != null){
                        paintFragment.openSelectPhotoFragment();
                    }
                }else {
                    checkPermissionOS6();
                }
            }
        });
        fragment.show(getSupportFragmentManager(), SelectTypePhotoFragment.class.getName());
    }

    private void getPhoto() {
        // Gallery
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.addCategory(Intent.CATEGORY_OPENABLE);
        gallery.setType("image/*");

        Intent[] intents;
        intents = new Intent[0];

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, gallery);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);

        startActivityForResult(chooserIntent, GET_FILE_REQUEST_CODE);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.bg_gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        paintFragment.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
