package com.example.appsotietkiem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appsotietkiem.fragment.HomeFragment;


public class LoginActivity extends AppCompatActivity {
    EditText edtTenDangNhap, edtPassword;
    Button btnLogin, btnSignUp;
    MyDatabase myDatabase;
    CheckBox isadmincheckbox, isusercheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDatabase = new MyDatabase(LoginActivity.this);

        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);
        isadmincheckbox = findViewById(R.id.isAdmincheckbox);
        isusercheckbox = findViewById(R.id.isUserCheckBox);
//        myDatabase.createAdminAccount();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        // Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tendangnhap = edtTenDangNhap.getText().toString();
                String password = edtPassword.getText().toString();
                boolean isAdmin = isadmincheckbox.isChecked();
                boolean isUser = isusercheckbox.isChecked();

                if (tendangnhap.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (!isAdmin && !isUser) {
                    Toast.makeText(LoginActivity.this, "Hãy chọn một quyền (Admin hoặc User).", Toast.LENGTH_SHORT).show();
                } else if (isAdmin && isUser) {
                    Toast.makeText(LoginActivity.this, "Chỉ lựa chọn 1 quyền duy nhất", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean isValidUser = false;
                    int userID = -1; // Khởi tạo UserID với giá trị mặc định là -1
                    if (isAdmin) {
                        isValidUser = myDatabase.checkAdminCredentials(tendangnhap, password);
                    } else if (isUser) {
                        isValidUser = myDatabase.checkUserCredentials(tendangnhap, password);
                    }

                    if (isValidUser) {
                         userID =myDatabase.getUserIdByUsernameAndPassword(tendangnhap,password);
                        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("userID", userID);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công với quyền " + (isAdmin ? "Admin" : "User"), Toast.LENGTH_SHORT).show();
                        // Chuyển hướng tới màn hình tương ứng với quyền admin hoặc user
                        Intent intent;
                        if (isAdmin) {
                            intent = new Intent(LoginActivity.this, AdminActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Tên tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}


