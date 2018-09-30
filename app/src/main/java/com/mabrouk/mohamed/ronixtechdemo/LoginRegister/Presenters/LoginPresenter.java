package com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Presenters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;


import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.AppDatabase;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.User;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.UserDao;

public class LoginPresenter {
    private final static String TAG = "LoginPresenter" + "TAGG";

    static OnUserLoginListener mListener;
    private Context mContext;
    private AppDatabase DB;
    private UserDao mUserDao;

    public LoginPresenter(OnUserLoginListener mListener, Context mContext){
        this.mListener = mListener;
        this.mContext = mContext;
        DB = AppDatabase.getInstance(mContext);
        mUserDao = DB.userDao();
    }

    public void login(String userMail, String userPassword){
        // check user info from repo
        new queryAsyncTask(mUserDao).execute(userMail, userPassword);
    }

    private static class queryAsyncTask extends AsyncTask<String, Void, User> {

        private UserDao mUserDao;

        queryAsyncTask(UserDao dao) {
            mListener.onStartLogin();
            mUserDao = dao;
        }

        @Override
        protected User doInBackground(String... params) {
            return mUserDao.getUser(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            if(user != null){
                mListener.onSuccessfulLogin(user);
            }else {
                mListener.onLoginFailure();
            }
            super.onPostExecute(user);
        }

    }



    public interface OnUserLoginListener{
        void onStartLogin();
        void onSuccessfulLogin(User user);
        void onLoginFailure();
    }
}
