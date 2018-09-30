package com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.mabrouk.mohamed.ronixtechdemo.Home.Views.HomeActivity;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.User;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Presenters.LoginPresenter;
import com.mabrouk.mohamed.ronixtechdemo.R;
import com.mabrouk.mohamed.ronixtechdemo.Utils.UserLogin;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.OnUserLoginListener{
    private final static String TAG = "Login Activity" + "TAGG";

    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar loadingLayout;
    private TextView errorTextView;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Testing Room DB
        Stetho.initializeWithDefaults(this);

        // check if user is logged in
        Log.v(TAG, "logged user: " +UserLogin.checkLogin(this) );
        if(!UserLogin.checkLogin(this).equals("")){
            goToHomeActivity();
        }

        try{
            getSupportActionBar().setTitle(R.string.login_activity_title);
        }catch (Exception e){
            Log.v(TAG, "error: " + e);
        }

        emailEditText = findViewById(R.id.input_email);
        passwordEditText = findViewById(R.id.input_password);
        loadingLayout = findViewById(R.id.loading_progressBar);
        errorTextView = findViewById(R.id.tv_error);
        Button loginButton = findViewById(R.id.btn_login);
        TextView noAccountTextView = findViewById(R.id.no_account);

        mPresenter = new LoginPresenter(this, this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorTextView.setVisibility(View.GONE);

                if(validate()){
                    mPresenter.login(emailEditText.getText().toString()
                    , passwordEditText.getText().toString());
                }
            }
        });

        noAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterActivity();
            }
        });

    }



    private boolean validate(){
        boolean ready = true;

        // email
        if(!emailEditText.getText().toString().contains("@") || !emailEditText.getText().toString().contains(".")){
            emailEditText.setError(getString(R.string.err_invalid_mail) );
            ready = false;
        }
        // password
        if(passwordEditText.getText().toString().length() == 0){
            passwordEditText.setError(getString(R.string.err_empty_password) );
            ready = false;
        }

        return ready;
    }

    private void goToRegisterActivity(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onStartLogin() {
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessfulLogin(User currentUser) {
        loadingLayout.setVisibility(View.GONE);
        Log.v(TAG, "login success, name is: " + currentUser.getUserName());
        UserLogin.login(this, currentUser);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user", currentUser);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure() {
        loadingLayout.setVisibility(View.GONE);
        errorTextView.setText(getString(R.string.err_worng_mail_or_password));
        errorTextView.setVisibility(View.VISIBLE);
        Log.v(TAG, "login fail");
    }
}
