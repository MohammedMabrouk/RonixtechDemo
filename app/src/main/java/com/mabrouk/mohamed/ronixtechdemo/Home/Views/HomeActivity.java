package com.mabrouk.mohamed.ronixtechdemo.Home.Views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mabrouk.mohamed.ronixtechdemo.Home.Presenters.MqttPresenter;
import com.mabrouk.mohamed.ronixtechdemo.Home.Presenters.SSIDpresenter;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.User;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Views.LoginActivity;
import com.mabrouk.mohamed.ronixtechdemo.R;
import com.mabrouk.mohamed.ronixtechdemo.Utils.UserLogin;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends AppCompatActivity implements SSIDpresenter.OnSSIDrequestListener, MqttPresenter.mqttListener {
    private final static String TAG = "HomeActivity" + "TAGG";

    private static final int GROUP_REQUEST_NUM = 0;
    private static final String[] PERMISSIONS_GROUP = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CHANGE_NETWORK_STATE};

    TextView userNameTextView;
    Button logoutBtn, retryBtn, wifiConnectBtn;

    TextView connectingTextView, errorTextView, ssidTextView, mqttMessageTextView, mqttSectionTextView, wifiStatusTv;
    View mqttLayout, mainLayout, connectLayout;

    Activity mActivity;

    User currentUser;
    SSIDpresenter mPresenter;
    MqttPresenter mqttPresenter;
    private String ssid, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            getSupportActionBar().setTitle(R.string.home_activity_title);
        } catch (Exception e) {
            Log.v(TAG, "error: " + e);
        }

        // init
        mActivity = this;

        mainLayout = findViewById(R.id.main_layout);
        userNameTextView = findViewById(R.id.tv_user_name);
        logoutBtn = findViewById(R.id.btn_logout);
        connectingTextView = findViewById(R.id.tv_connecting);
        errorTextView = findViewById(R.id.tv_connection_err);
        ssidTextView = findViewById(R.id.tv_ssid);
        retryBtn = findViewById(R.id.btn_retry_connection);
        mqttLayout = findViewById(R.id.mqtt_layout);
        wifiConnectBtn = findViewById(R.id.btn_connect_to_wifi);
        mqttMessageTextView = findViewById(R.id.tv_mqtt_message);
        mqttSectionTextView = findViewById(R.id.tv_mqtt_sction_title);
        connectLayout = findViewById(R.id.connect_layout);
        wifiStatusTv = findViewById(R.id.tv_wifi_status);


        mPresenter = new SSIDpresenter(this, this);
        mqttPresenter = new MqttPresenter(this, this);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getSSID();
            }
        });

        wifiConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // connect to wifi

                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mActivity,
                        Manifest.permission.CHANGE_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "no permissions");

                    requestPermissions();
                } else {
                    connectToWifi(ssid, pass);
                }
            }
        });


        Intent intent = getIntent();
        if (intent.getSerializableExtra("user") != null) {
            currentUser = (User) intent.getSerializableExtra("user");
            userNameTextView.setText(currentUser.getUserName());
        } else {
            currentUser = UserLogin.getLoggedUser(this);
            if (currentUser != null) {
                userNameTextView.setText(currentUser.getUserName());
            }
        }

        // get SSID
        mPresenter.getSSID();
    }

    private void logout() {
        UserLogin.logout(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStartConnection() {
        Log.v(TAG, "start connection");
    }


    @Override
    public void onSucess(String SSID) {
        ssid = SSID;
        Log.v(TAG, "success, ssid: " + SSID);
        connectingTextView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        retryBtn.setVisibility(View.GONE);

        ssidTextView.setText(SSID);
        ssidTextView.setVisibility(View.VISIBLE);
        // start MQTT
        mqttPresenter.subscribe();
        mqttLayout.setVisibility(View.VISIBLE);
        mqttSectionTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(String error) {
        Log.v(TAG, "connection error: " + error);
        connectingTextView.setVisibility(View.GONE);
        ssidTextView.setVisibility(View.GONE);

        errorTextView.setText(error);
        errorTextView.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMqttStartConnection() {
        Log.v(TAG, "connection started,.......");
        mqttMessageTextView.setText(R.string.connection_started);
    }

    @Override
    public void onMqttMessageArrived(String message) {
        Log.v(TAG, "messageeeee: " + message);
        mqttMessageTextView.setText(message);

        try {
            JSONObject json = new JSONObject(message);
            pass = json.getString("PASS");
            Log.v(TAG, "pass: " + pass);


            // show connect button
            connectLayout.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMqttFailure(String error) {
        Log.v(TAG, "failure: " + error);
        mqttMessageTextView.setText(R.string.connection_lost);
    }

    private void connectToWifi(String ssid, String pass) {
        Log.v(TAG, "connect to wifi, ssid: " + ssid + " , pass: " + pass);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", ssid);
        conf.preSharedKey = String.format("\"%s\"", pass);

        int netId = 0;
        if (wifiManager != null) {
            netId = wifiManager.addNetwork(conf);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

            wifiStatusTv.setVisibility(View.VISIBLE);
        }


    }

    private void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_NETWORK_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CHANGE_NETWORK_STATE)) {

            Log.v(TAG, "case1");
            Snackbar.make(mainLayout, R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.allow, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(mActivity, PERMISSIONS_GROUP,
                                    GROUP_REQUEST_NUM);
                        }
                    })
                    .show();
        } else {
            Log.v(TAG, "case2");
            ActivityCompat.requestPermissions(this, PERMISSIONS_GROUP, GROUP_REQUEST_NUM);

        }
        connectToWifi(ssid, pass);
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == GROUP_REQUEST_NUM) {

            if (com.invent.mohamed.cardscanner.utils.PermissionsUtilities.verifyPermissions(grantResults)) {

                Snackbar.make(mainLayout, R.string.permissions_granted,
                        Snackbar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        connectToWifi(ssid, pass);
                    }
                }).show();

            } else {

                Snackbar.make(mainLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/
}
