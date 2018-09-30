package com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mabrouk.mohamed.ronixtechdemo.Home.Views.HomeActivity;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.User;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Presenters.RegisterPresenter;
import com.mabrouk.mohamed.ronixtechdemo.R;
import com.mabrouk.mohamed.ronixtechdemo.Utils.UserLogin;

public class RegisterActivity extends AppCompatActivity implements RegisterPresenter.OnUserRegisteredListener{
    private final static String TAG = "Register Activity" + "TAGG";

    // UI
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private ProgressBar loadingLayout;

    // presenter
    RegisterPresenter mRegisterPresenter;

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try{
            getSupportActionBar().setTitle(R.string.register_activity_title);
        }catch (Exception e){
            Log.v(TAG, "error: " + e);
        }

        // init
        nameEditText = findViewById(R.id.input_name);
        emailEditText = findViewById(R.id.input_email);
        passwordEditText = findViewById(R.id.input_password);
        confirmPasswordEditText = findViewById(R.id.input_confirm_password);
        loadingLayout = findViewById(R.id.loading_progressBar);
        Button registerButton = findViewById(R.id.btn_register);

        mRegisterPresenter = new RegisterPresenter(this, getApplicationContext());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser = new User(nameEditText.getText().toString()
                        , emailEditText.getText().toString()
                        , passwordEditText.getText().toString());
                if(validate()){
                    mRegisterPresenter.registerUser(currentUser);
                }
            }
        });


    }

    private boolean validate(){
        boolean ready = true;

        // name
        if(nameEditText.getText().toString().length() < 3){
            nameEditText.setError(getString(R.string.err_short_name) );
            ready = false;
        }
        // email
        if(!emailEditText.getText().toString().contains("@") || !emailEditText.getText().toString().contains(".")){
            emailEditText.setError(getString(R.string.err_invalid_mail) );
            ready = false;
        }
        // password
        if(passwordEditText.getText().toString().length() < 3){
            passwordEditText.setError(getString(R.string.err_short_name) );
            ready = false;
        }
        // confirm password
        if(!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())){
            confirmPasswordEditText.setError(getString(R.string.err_confirm_password) );
            ready = false;
        }
        return ready;
    }

    @Override
    public void onStartRegistration() {
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessfulRegistration() {
        loadingLayout.setVisibility(View.GONE);
        UserLogin.login(this, currentUser);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user", currentUser);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
