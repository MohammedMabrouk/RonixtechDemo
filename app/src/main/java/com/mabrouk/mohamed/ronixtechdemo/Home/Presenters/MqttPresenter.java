package com.mabrouk.mohamed.ronixtechdemo.Home.Presenters;

import android.content.Context;

import com.mabrouk.mohamed.ronixtechdemo.Home.Model.MqttHelper;
import com.mabrouk.mohamed.ronixtechdemo.Home.Model.Repository;

public class MqttPresenter {
    private MqttPresenter.mqttListener mListener;
    private Context mContext;
    private MqttHelper mhelper;

    public MqttPresenter(MqttPresenter.mqttListener mListener, Context mContext){
        this.mListener = mListener;
        this.mContext = mContext;

    }

    public void subscribe(){
        mhelper = new MqttHelper(mContext, this);
    }

    // ui changes
    public void startSubscription(){
        mListener.onMqttStartConnection();
    }

    public void messageReceived(String message){
        mListener.onMqttMessageArrived(message);
    }

    public void errorReceived(String error){
        mListener.onMqttFailure(error);
    }

    public interface mqttListener{
        void onMqttStartConnection();
        void onMqttMessageArrived(String message);
        void onMqttFailure(String error);
    }
}
