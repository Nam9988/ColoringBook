package com.example.dd.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dd.R;
import com.example.dd.model.UserDTO;
import com.example.dd.util.TextUtil;
import com.example.dd.util.ValidateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_visible)
    ImageView btnVisible;
    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @OnClick(R.id.btn_visible)
    public void onBtnVisibleClicked() {
    }

    @OnClick(R.id.btn_register)
    public void onBtnRegisterClicked() {
        String email = TextUtil.getValue(edtEmail);
        String password = TextUtil.getValue(edtPassword);
        String name = TextUtil.getValue(edtName);
        String confirmPassword = TextUtil.getValue(edtConfirmPassword);
        if (validateSubmit(name, email, password, confirmPassword)) {
            UserDTO userDTO = new UserDTO(name, email, password);
            Intent intent = new Intent();
            intent.putExtra("user", userDTO);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    private boolean validateSubmit(String name, String email, String password, String confirmPassword) {
        if (name.length() == 0) {
            Toast.makeText(this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.length() == 0) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ email!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!ValidateUtil.isEmail(email)){
            Toast.makeText(this, "Email nhập không chính xác!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() == 0) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!ValidateUtil.checkPassword(password)){
                Toast.makeText(this, "Mật khẩu chưa đủ mạnh!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}