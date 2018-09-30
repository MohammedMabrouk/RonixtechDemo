package com.mabrouk.mohamed.ronixtechdemo.Home.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mabrouk.mohamed.ronixtechdemo.Home.Presenters.SSIDpresenter;
import com.mabrouk.mohamed.ronixtechdemo.R;

import org.json.JSONObject;

public class Repository {
    private static final String TAG = Repository.class.getSimpleName() + "TAGG";
    private static final String ENDPOINT = "http://ronixtech.com/ronix_services/task/srv.php";

    private SSIDpresenter mPresenter;
    private Context mContext;
    private String ssid;

    private RequestQueue mRequestQueue;

    public Repository(Context mContext, SSIDpresenter mPresenter) {
        this.mPresenter = mPresenter;
        this.mContext = mContext;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public void getSSID() {
        mPresenter.startRequest();

        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            mPresenter.resultReceived(responseJSON.getString("SSID"));
                        } catch (Exception e) {
                            Log.v(TAG, "Error: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handling Error Cases
                if (error instanceof TimeoutError) {
                    mPresenter.errorReceived(mContext.getResources().getString(R.string.timeout_err));
                } else if (error instanceof NoConnectionError) {
                    mPresenter.errorReceived(mContext.getResources().getString(R.string.noconnection_err));
                } else if (error instanceof AuthFailureError) {
                    mPresenter.errorReceived(mContext.getResources().getString(R.string.authfailure_err));
                } else if (error instanceof ServerError) {
                    mPresenter.errorReceived(mContext.getResources().getString(R.string.server_err));
                } else if (error instanceof NetworkError) {
                    mPresenter.errorReceived(mContext.getResources().getString(R.string.network_err));
                } else if (error instanceof ParseError) {
                    mPresenter.errorReceived(mContext.getResources().getString(R.string.parse_err));
                }
            }
        });
        mRequestQueue.add(request);
    }
}
