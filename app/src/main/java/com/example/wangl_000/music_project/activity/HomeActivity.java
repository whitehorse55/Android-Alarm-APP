package com.example.wangl_000.music_project.activity;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.wangl_000.music_project.Model.MusicModel;
import com.example.wangl_000.music_project.Music;
import com.example.wangl_000.music_project.R;
import com.example.wangl_000.music_project.UiUtils.Constants;
import com.example.wangl_000.music_project.UiUtils.SharedPrefUtil;
import com.example.wangl_000.music_project.adapter.CustomMusicListAdapter;
import com.example.wangl_000.music_project.adapter.ViewPagerAdapter;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import me.relex.circleindicator.CircleIndicator;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    ListView music_list;
    CustomMusicListAdapter music_adapter;
    ViewPager mViewPager;
    LinearLayout lay_gone;
    LinearLayout lay_parent;
    CircleIndicator indicator;

    ArrayList<MusicModel> mMusic_array;
    ArrayList<MusicModel> temp_music_array;

    EditText search_text;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initPushFunction();
        /////////auto disappear keyboard when starts activity////////////////////////////
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        mMusic_array   =   new ArrayList<MusicModel>();
        temp_music_array = new ArrayList<MusicModel>();

        findViewById(R.id.seg_featured).setOnClickListener(this);
        findViewById(R.id.seg_recent).setOnClickListener(this);
        findViewById(R.id.information).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);

        lay_gone = (LinearLayout)findViewById(R.id.lay_gone);

        search_text = (EditText)findViewById(R.id.search_edit);

//////////////////////////////////////////////Search part for word/////////////////////////////////////////////
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equals(""))
                {
                    initFirstStatus();
                }
                else {
                    searchItem(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // music_adapter.getFilter().filter(editable);

            }
        });

        initList();
        listItemClick();

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(viewPagerAdapter);

        indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.bringToFront();
        indicator.setViewPager(mViewPager);
        viewPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

    }

    private  void  initPushFunction()
    {
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private void initFirstStatus()
    {
        music_adapter = new CustomMusicListAdapter(this, temp_music_array);
        SharedPrefUtil.saveArrayList(temp_music_array, this);
        mMusic_array.clear();
        mMusic_array.addAll(SharedPrefUtil.loadArrayList(this));
        music_list.setAdapter(music_adapter);
        music_adapter.notifyDataSetChanged();
    }

    private void initList()
    {
        music_list = (ListView)findViewById(R.id.custom_listview);

        if (SharedPrefUtil.loadArrayList(this) != null)
        {
            mMusic_array = SharedPrefUtil.loadArrayList(this);
            temp_music_array = SharedPrefUtil.loadArrayList(this);
        }
        else {
            makeArray();
            SharedPrefUtil.saveArrayList(mMusic_array, this);
        }
            music_adapter = new CustomMusicListAdapter(this, mMusic_array);
            music_list.setAdapter(music_adapter);
    }

    private void listItemClick()
    {
        music_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MusicModel model = mMusic_array.get(i);
                model.setChecked(true);
                int model_index = model.getIndex();

                /////////////////////// save checked item to temparray/////////////
                saveCheckedSatusToArray(model_index);
                ///////////////////////////////////////////////////////////////////
                mMusic_array.set(i, model);

                Constants.g_model.clear();
                Constants.g_model.addAll(mMusic_array);

                Intent j = new Intent(HomeActivity.this, MusicActivity.class);
                j.putExtra("index",i);

                startActivityForResult(j, 1);


            }
        });
    }

    private void saveCheckedSatusToArray(int index)
    {
            for (int i = 0 ; i < temp_music_array.size() ; i++)
            {
                MusicModel model = temp_music_array.get(i);
                int temp_index = model.getIndex();
                if (temp_index == index)
                {
                    model.setChecked(true);
                    temp_music_array.set(i, model);
                }
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        search_text.setText("");
        initFirstStatus();
    }

    private void searchItem(String s)
    {
        flag = false;
        ArrayList<MusicModel> temp = new ArrayList<MusicModel>();
        for (int i=0; i<mMusic_array.size();i++ )
        {
            MusicModel model = mMusic_array.get(i);

            if (model.getMusic_name().contains(s))
            {
                temp.add(model);
            }
        }
        music_adapter = new CustomMusicListAdapter(this,temp);
        mMusic_array.clear();
        mMusic_array.addAll(temp);
        music_list.setAdapter(music_adapter);
        music_adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

//        search_text.setText("");
        if (music_adapter != null) {
//                initFirstStatus();
            }
    }

    private void makeArray()
    {
        genereateArray("Attention(Marimba Remix)","Charlie Puth","true",0, "music1" , "http://apple.co/2sww0Q9");
        genereateArray("Bank Account(Marimba Remix)","21 Savage","true",1,"music2","http://apple.co/2kiwusz");
        genereateArray("Bodak Yellow(Marimba Remix)","Cardi B","true",2,"music3","http://apple.co/2xOHUrs");
        genereateArray("Feels(Marimba Remix)","Calvin Harris ft. Pharrell Williams,Katy Perry, Big Sean","true",3,"music4","http://apple.co/2xaSFSQ");
        genereateArray("Mi Gente Marimba Remix)","J.Balvin,Willy William","true",4,"music5","http://apple.co/2vw57jL");
        genereateArray("Unforgettable(Marimba Remix)","French Montanna ft.Swae Lee","true",5,"music6"," http://apple.co/2yDYpWe");
    }

    private void genereateArray(String str_name, String str_img, String str_check, Integer index, String str_song, String str_get)
    {
        MusicModel model = new MusicModel();
        model.setChecked(false);
        model.setMusic_img(str_img);
        model.setMusic_name(str_name);
        model.setIndex(index);
        model.setMusic_song_name(str_song);
        model.setMusic_get_link(str_get);

        mMusic_array.add(model);
        temp_music_array.add(model);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.seg_featured:
                    lay_gone.setVisibility(View.VISIBLE);
                break;
            case R.id.seg_recent:
                    lay_gone.setVisibility(View.GONE);
                break;

            case R.id.information:
                Intent i = new Intent(HomeActivity.this, RequestActivity.class);
                startActivity(i);
                break;

            case R.id.share:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.facebook.com/MarimbaRemixes/"); // Simple text and URL to share
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;
        }
    }
}
