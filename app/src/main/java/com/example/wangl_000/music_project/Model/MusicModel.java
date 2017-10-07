package com.example.wangl_000.music_project.Model;

import android.media.Image;

/**
 * Created by WangL_000 on 2017/9/23.
 */

public class MusicModel {

    private boolean isChecked;
    private String  music_img;
    private String  music_name;
    private String  music_song_name;
    private String  music_get_link;
    private Integer index;

    public MusicModel()
    {
        this.music_name = "";
        this.music_song_name = "";
        this.music_get_link = "";
        this.isChecked = false;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }



    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getMusic_img() {
        return music_img;
    }

    public void setMusic_img(String music_img) {
        this.music_img = music_img;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getMusic_song_name() {
        return music_song_name;
    }

    public void setMusic_song_name(String music_song_name) {
        this.music_song_name = music_song_name;
    }

    public String getMusic_get_link() {
        return music_get_link;
    }

    public void setMusic_get_link(String music_get_link) {
        this.music_get_link = music_get_link;
    }
}
