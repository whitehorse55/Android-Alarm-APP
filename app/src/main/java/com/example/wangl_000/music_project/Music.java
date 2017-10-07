package com.example.wangl_000.music_project;

import android.app.Application;

/**
 * Created by WangL_000 on 2017/9/21.
 */

public class Music extends Application {

    private static Music sharedApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedApplication = this;
    }

    public static Music getSharedApplication()
    {
        return sharedApplication;
    }
}
