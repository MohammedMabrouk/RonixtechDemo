package com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Presenters;

import android.content.Context;
import android.os.AsyncTask;

import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.AppDatabase;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.User;
import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.UserDao;

public class RegisterPresenter {
    private static OnUserRegisteredListener mListener;
    private Context mContext;
    private AppDatabase DB;
    private UserDao mUserDao;



    public RegisterPresenter(RegisterPresenter.OnUserRegisteredListener mListener, Context mContext){
        this.mListener = mListener;
        this.mContext = mContext;
        DB = AppDatabase.getInstance(mContext);
        mUserDao = DB.userDao();
    }

    public void registerUser(User newUser){
        new insertAsyncTask(mUserDao).execute(newUser);
    }


    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mUserDao;

        insertAsyncTask(UserDao dao) {
            mListener.onStartRegistration();
            mUserDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mUserDao.insertUser(users[0]);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            mListener.onSuccessfulRegistration();
            super.onPostExecute(aVoid);
        }
    }



    public interface OnUserRegisteredListener{
        void onStartRegistration();
        void onSuccessfulRegistration();
    }
}
