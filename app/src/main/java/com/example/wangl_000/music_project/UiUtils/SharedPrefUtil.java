package com.example.wangl_000.music_project.UiUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.wangl_000.music_project.Model.MusicModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPrefUtil {

    private static final String PREFERENCES_NAME = "MyPreferences";
    private static SharedPreferences mPreferences;
    private static Editor mEditor;

    /* save and load String */
    public static void saveString(Context context, String preferencesKey,
                                  String value) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString(preferencesKey, value);
        mEditor.commit();
    }

    public static String loadString(Context context, String preferencesKey,
                                    String defaultValue) {

        mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_MULTI_PROCESS);

//        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
//                Context.MODE_PRIVATE);
        return mPreferences.getString(preferencesKey, defaultValue);
    }

    /* save and load String set */
    public static void saveStringSet(Context context, String preferencesKey,
                                     Set<String> value) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putStringSet(preferencesKey, value);
        mEditor.commit();
    }

    public static Set<String> loadStringSet(Context context,
                                            String preferencesKey, Set<String> defaultValue) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return mPreferences.getStringSet(preferencesKey, defaultValue);
    }

    /* save and load int */
    public static void saveInt(Context context, String preferencesKey, int value) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putInt(preferencesKey, value);
        mEditor.commit();
    }

    public static int loadInt(Context context, String preferencesKey,
                              int defaultValue) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return mPreferences.getInt(preferencesKey, defaultValue);
    }

    /* save and load long */
    public static void saveLong(Context context, String preferencesKey,
                                long value) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putLong(preferencesKey, value);
        mEditor.commit();
    }

    public static long loadLong(Context context, String preferencesKey,
                                long defaultValue) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return mPreferences.getLong(preferencesKey, defaultValue);
    }

    /* save and load float */
    public static void saveFloat(Context context, String preferencesKey,
                                 float value) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putFloat(preferencesKey, value);
        mEditor.commit();
    }

    public static float loadFloat(Context context, String preferencesKey,
                                  float defaultValue) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return mPreferences.getFloat(preferencesKey, defaultValue);
    }

    /* save and load boolean */
    public static void saveBoolean(Context context, String preferencesKey,
                                   boolean value) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putBoolean(preferencesKey, value);
        mEditor.commit();
    }

    public static boolean loadBoolean(Context context, String preferencesKey,
                                      boolean defaultValue) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return mPreferences.getBoolean(preferencesKey, defaultValue);
    }

    /* save and load list key */
    public static void saveListKey(Context context, String preferencesKey,
                                   List<String> values) {
        Set<String> set = new HashSet<>();
        set.addAll(values);
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putStringSet(preferencesKey, set);
        mEditor.commit();
    }

    public static Set<String> loadListKey(Context context, String preferencesKey) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return mPreferences.getStringSet(preferencesKey, new HashSet<String>());
    }

    ///////////////////////////save arraylist in sharedpreference //////////////////////////////
    public static boolean saveArrayList(ArrayList<MusicModel> m_array, Context context)
    {
        ArrayList<MusicModel> model = new ArrayList<MusicModel>(  );
        model.clear();
        model.addAll( m_array );
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        Editor m_Editor = mPreferences.edit();
        m_Editor.clear();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        m_Editor.putString( Constants.PREF_MUSICARRAY,json );
        return  m_Editor.commit();
    }
    //////////////////////////load shared preference list /////////////////////////////
    public static ArrayList<MusicModel> loadArrayList(Context context)
    {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        Gson gson =  new Gson();
        String json = mPreferences.getString( Constants.PREF_MUSICARRAY,"" );
        ArrayList<MusicModel> arrayList = new ArrayList<MusicModel>(  );
        if (json.equals( "" )) {
            return null;
        }
        else {
            Type type = new TypeToken<ArrayList<MusicModel>>() {
            }.getType();
            arrayList = gson.fromJson( json, type );
            return arrayList ;
        }
    }

}
