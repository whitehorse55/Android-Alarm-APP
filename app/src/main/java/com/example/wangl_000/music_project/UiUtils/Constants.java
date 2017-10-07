package com.example.wangl_000.music_project.UiUtils;

import com.example.wangl_000.music_project.Model.MusicModel;
import com.example.wangl_000.music_project.R;

import java.util.ArrayList;

/**
 * Created by WangL_000 on 2017/9/21.
 */

public class Constants {

    public static String PREF_LOGINSTATE = "login_state";
    public static String PREF_MUSICARRAY = "music_array";
    public static String PREF_CHECKED = "checked";

    public static int [] music_image_names =
            {       R.drawable.attentionapp,
                    R.drawable.bankaccountapp,
                    R.drawable.bodakapp,
                    R.drawable.feelsapp,
                    R.drawable.migentreapp,
                    R.drawable.unforgettableapp
            };

    public static int [] music_image_names_list =
            {       R.drawable.attentionapp1,
                    R.drawable.bankaccountapp1,
                    R.drawable.bodakapp1,
                    R.drawable.feelsapp1,
                    R.drawable.migentreapp1,
                    R.drawable.unforgettableapp1
            };

    public static final ArrayList<MusicModel>g_model = new ArrayList<MusicModel>();

}
