package com.mabrouk.mohamed.ronixtechdemo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mabrouk.mohamed.ronixtechdemo.LoginRegister.Model.User;
import com.mabrouk.mohamed.ronixtechdemo.R;

public class UserLogin {

    public static void login(Activity mActivity, User user) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mActivity.getString(R.string.pref_logged_user_mail), user.getUserEmail());
        editor.putString(mActivity.getString(R.string.pref_logged_user_name), user.getUserName());
        editor.commit();
    }

    public static void logout(Activity mActivity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mActivity.getString(R.string.pref_logged_user_mail), "");
        editor.putString(mActivity.getString(R.string.pref_logged_user_name), "");
        editor.commit();
    }

    public static String checkLogin(Activity mActivity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return sharedPref.getString(mActivity.getString(R.string.pref_logged_user_mail), "");
    }

    public static User getLoggedUser(Activity mActivity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String uMail, uName;

        uMail = sharedPref.getString(mActivity.getString(R.string.pref_logged_user_mail), "");
        uName = sharedPref.getString(mActivity.getString(R.string.pref_logged_user_name), "");
        if(!uMail.equals("") && !uName.equals("")){
            return new User(uName, uMail, "");
        }else{
            return null;
        }
    }
}
