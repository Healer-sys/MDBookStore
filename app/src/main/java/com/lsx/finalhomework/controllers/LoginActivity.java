package com.lsx.finalhomework.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lsx.finalhomework.MyAuth;
import com.lsx.finalhomework.R;
import com.lsx.finalhomework.entities.AESUtil;

import java.util.Date;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView loginTitle;
    Button loginButton;
    TextView toggleLoginReg;
    EditText username;
    EditText password;

    boolean login = true;
    ImageView ivlogin;
    long time, count = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginTitle = (TextView) findViewById(R.id.loginTitle);
        loginButton = (Button) findViewById(R.id.loginButton);
        toggleLoginReg = (TextView) findViewById(R.id.toggleLoginReg);
        username = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        ivlogin = (ImageView) findViewById(R.id.iv_login);
        toggleLoginReg.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("preferences", MODE_PRIVATE);
        String defaultUsername = sp.getString("username", "");
        if (!defaultUsername.equals("")) {
            this.username.setText(defaultUsername);
        }
        // 图片按钮设置点击事件处理逻辑
        ivlogin.setOnClickListener(view -> {
            // 记录当前时间戳
            long currentTimestamp = new Date().getTime();

            // 判断是否在1秒内重复点击
            if (currentTimestamp - time < 300) {
                // 若重复点击，增加计数并提示用户
                count++;
                Toast Mag = new Toast(getApplicationContext());
                Mag.setDuration(Toast.LENGTH_SHORT);
                Mag.setText("继续点击" + (5 - count) + "次,进入管理模式");
                Mag.show();
                Mag.cancel();
            } else {
                // 超过1秒视为新的点击序列，重置计数
                count = 1;
            }
            // 更新上次点击时间戳
            time = currentTimestamp;
            // 达到5次连续快速点击，提示用户暂停操作
            if (count >= 5) {
                Intent intent = new Intent(LoginActivity.this, User_MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleLoginReg:
                login = !login;
                loginTitle.setText(login ? "登录" : "注册");
                loginButton.setText(login ? "登录" : "注册");
                toggleLoginReg.setText(login ? "立即注册" : "立即登录");
                break;
            case R.id.loginButton:
                String user = username.getText().toString();
                String pwd = password.getText().toString();
                MyAuth auth = new MyAuth(LoginActivity.this);
                if (login) {
                    switch (auth.authUser(user, pwd)) {
                        case SUCCESS:
                            SharedPreferences sp = getSharedPreferences("preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("username", user);
                            editor.apply();
                            goToMain();
                            break;
                        case INVALID_USERNAME_OR_PWD:
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            break;
                        case USER_EXISTED:
                            break;
                        case TOKEN_TOO_LONG:
                            Toast.makeText(LoginActivity.this, "用户名太长", Toast.LENGTH_SHORT).show();
                            break;
                        case UNKNOWN_ERROR:
                            Toast.makeText(LoginActivity.this, "出现未知错误", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    // 注册
                    switch (auth.addUser(user, pwd)) {
                        case SUCCESS:
                            SharedPreferences sp = getSharedPreferences("preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("username", user);
                            editor.apply();
                            goToMain();
                            break;
                        case INVALID_USERNAME_OR_PWD:
                            break;
                        case USER_EXISTED:
                            Toast.makeText(LoginActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                            break;
                        case TOKEN_TOO_LONG:
                            Toast.makeText(LoginActivity.this, "用户名太长", Toast.LENGTH_SHORT).show();
                            break;
                        case UNKNOWN_ERROR:
                            Toast.makeText(LoginActivity.this, "出现未知错误", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }
}