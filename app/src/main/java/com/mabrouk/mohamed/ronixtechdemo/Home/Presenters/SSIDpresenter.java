package com.mabrouk.mohamed.ronixtechdemo.Home.Presenters;

import android.content.Context;

import com.mabrouk.mohamed.ronixtechdemo.Home.Model.Repository;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.User;

public class SSIDpresenter {
    private OnSSIDrequestListener mListener;
    private Context mContext;
    private Repository mRepository;

    public SSIDpresenter(OnSSIDrequestListener mListener, Context mContext){
        this.mListener = mListener;
        this.mContext = mContext;
        mRepository = new Repository(mContext, this);
    }

    public void getSSID(){
        mRepository.getSSID();
    }

    // ui changes
    public void startRequest(){
        mListener.onStartConnection();
    }

    public void resultReceived(String ssid){
        mListener.onSucess(ssid);
    }

    public void errorReceived(String error){
        mListener.onFailure(error);
    }

    public interface OnSSIDrequestListener{
        void onStartConnection();
        void onSucess(String SSID);
        void onFailure(String error);
    }
}
