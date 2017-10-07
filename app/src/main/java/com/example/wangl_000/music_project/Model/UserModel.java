package com.example.wangl_000.music_project.Model;

/**
 * Created by WangL_000 on 2017/9/22.
 */

public class UserModel {

    private String user_name;
    private String user_pass;
    private String user_email;

    public UserModel()
    {
        user_email= "";
        user_name = "";
        user_pass = "";
    }

    public UserModel(String u_mail, String u_pass, String u_name)
    {
        user_name   = u_name;
        user_pass   = u_pass;
        user_email  = u_mail;
    }
    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
