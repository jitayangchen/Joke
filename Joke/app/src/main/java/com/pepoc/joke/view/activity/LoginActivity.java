package com.pepoc.joke.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pepoc.joke.R;
import com.pepoc.joke.net.http.HttpRequestManager;
import com.pepoc.joke.net.http.request.RequestLogin;
import com.pepoc.joke.util.Preference;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    @Bind(R.id.et_account_number)
    EditText etAccountNumber;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_register)
    Button btnRegister;

    private String accountNumber, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public void init() {
        super.init();

        etAccountNumber = (EditText) findViewById(R.id.et_account_number);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);

        String accountNumber = Preference.getAccountNumber();
        String password = Preference.getPassword();

        if (!TextUtils.isEmpty(accountNumber)) {
            etAccountNumber.setText(accountNumber);
        }
        if (!TextUtils.isEmpty(password)) {
            etPassword.setText(password);
        }

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                Intent registerIntent = new Intent(context, RegisterActivity.class);
                startActivity(registerIntent);
                break;

            default:
                break;
        }
    }

    private void login() {
        accountNumber = etAccountNumber.getText().toString();
        password = etPassword.getText().toString();
        if (TextUtils.isEmpty(accountNumber) || TextUtils.isEmpty(password)) {
            Toast.makeText(context, "account number or password null", Toast.LENGTH_SHORT).show();
            return ;
        }
        RequestLogin requestLogin = new RequestLogin(context, new HttpRequestManager.OnHttpResponseListener() {

            @Override
            public void onHttpResponse(Object result) {
                boolean isLoginSuccess = (Boolean) result;
                if (isLoginSuccess) {
                    Toast.makeText(context, "login success", Toast.LENGTH_SHORT).show();
//                    LoginObservable.getInstance().updateObserver(null);
                    finish();
                    Preference.saveIsLogin(true);

                } else {
                    Toast.makeText(context, "login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        });

        requestLogin.putParam("accountNumber", accountNumber);
        requestLogin.putParam("password", password);

        HttpRequestManager.getInstance().sendRequest(requestLogin);
    }

//    @Override
//    public void update(Observable observable, Object data) {
//        finish();
//    }
}
