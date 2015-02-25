package jk.com.splitbill;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jim on 13/2/2015.
 */
public class Methods extends Application {
    public static int times=0;
    public static int animationDuration = 150;
    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<Charge> charges = new ArrayList<Charge>();

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> userz) {
        users = userz;
    }

    public static ArrayList<Charge> getCharges() {
        return charges;
    }

    public static void setCharges(ArrayList<Charge> charges) {
        Methods.charges = charges;
    }

    private static ArrayList<Integer> mSelectedUSERz = new ArrayList<Integer>();//// Users selected to slpit amount


    public static void setmSelectedUserz(ArrayList<Integer> selectedUsers){
        mSelectedUSERz.clear();
        mSelectedUSERz = selectedUsers;
    }
    public static  ArrayList<Integer> getmSelectedUserz(){
        return mSelectedUSERz;
    }

    public static int getButtonHeight(Resources resources){
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int DPI =  metrics.densityDpi;
        return ((DPI*150)/480);
    }

    static public void savePrefsString(String toBeSaved, String valueToBeSaved,
                                       Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(toBeSaved, valueToBeSaved);
        edit.commit();
        Log.e("Execute Preference Command", "SAVE STRING PREFERENCE '" + toBeSaved + "' WITH VALUE '" + valueToBeSaved + "'");
    }

    static public String loadPrefsString(String name, String defaultValue, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString(name, defaultValue);
        Log.e("Execute Preference Command", "LOAD STRING PREFERENCE '" + name + "' WITH VALUE '" + value + "'");
        return value;
    }

}
