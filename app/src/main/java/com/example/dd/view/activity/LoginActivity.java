package com.example.dd.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dd.R;
import com.example.dd.model.UserDTO;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edt_account)
    EditText edtAccount;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_visible)
    ImageView btnVisible;
    @BindView(R.id.btn_forgot_password)
    TextView btnForgotPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_login_facebook)
    ImageView btnLoginFacebook;
    @BindView(R.id.btn_login_google)
    ImageView btnLoginGoogle;
    @BindView(R.id.txt_register)
    TextView txtRegister;

    private boolean isShowPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @OnClick(R.id.btn_visible)
    void onBtnVisibleClicked() {
        if (isShowPassword) {
            btnVisible.setImageResource(R.drawable.ic_show_password);
            edtPassword.setSelection(edtPassword.getText().toString().length());
            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
            isShowPassword = false;
        } else {
            btnVisible.setImageResource(R.drawable.ic_hide_password);
            edtPassword.setTransformationMethod(null);
            edtPassword.setSelection(edtPassword.getText().toString().length());
            isShowPassword = true;
        }
    }

    @OnClick(R.id.btn_forgot_password)
    void onBtnForgotPasswordClicked() {

    }

    @OnClick(R.id.btn_login)
    void onBtnLoginClicked() {
        startActivity(new Intent(this, ContainerActivity.class));
    }

    @OnClick(R.id.btn_login_facebook)
    void onBtnLoginFacebookClicked() {
    }

    @OnClick(R.id.txt_register)
    void onTxtRegisterClicked() {
        startActivityForResult(new Intent(this,RegisterActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            if (data != null) {
                UserDTO userDTO = (UserDTO) data.getSerializableExtra("user");
                edtAccount.setText(userDTO.getEmail());
                edtPassword.setText(userDTO.getPassword());
            }
        }
    }
}
