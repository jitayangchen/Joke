package com.pepoc.joke.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pepoc.joke.R;
import com.pepoc.joke.presenter.LoginPresenter;
import com.pepoc.joke.util.Preference;
import com.pepoc.joke.view.iview.ILoginView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseSwipeBackActivity implements View.OnClickListener, ILoginView {

    @Bind(R.id.et_account_number)
    EditText etAccountNumber;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this);
        init();
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitle(R.string.activity_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
                String accountNumber = etAccountNumber.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(accountNumber) || TextUtils.isEmpty(password)) {
                    Toast.makeText(context, "account number or password null", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginPresenter.login(accountNumber, password);
                break;
            case R.id.btn_register:
                Intent registerIntent = new Intent(context, RegisterActivity.class);
                startActivity(registerIntent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(context, "login success", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onLoginFail() {
        Toast.makeText(context, "login failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

}
