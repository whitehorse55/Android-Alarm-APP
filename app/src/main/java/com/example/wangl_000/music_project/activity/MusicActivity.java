package com.example.wangl_000.music_project.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangl_000.music_project.Model.MusicModel;
import com.example.wangl_000.music_project.Music;
import com.example.wangl_000.music_project.R;
import com.example.wangl_000.music_project.UiUtils.Constants;
import com.example.wangl_000.music_project.UiUtils.SharedPrefUtil;
import com.example.wangl_000.music_project.UiUtils.UiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{


    ImageView   icon_image;
    TextView    song_name;
    TextView    timeCurrent, timeEnd;
    SeekBar     music_seekbar;
    SeekBar     volme_seek;
    MediaPlayer mix;

    AudioManager audioManager;

    boolean btn_flage;
    boolean m_cancel;
    int music_index;
    int music_index_first;
    int music_num;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_music);

        btn_flage = false;
        m_cancel  = false;

        Bundle bundle = getIntent().getExtras();
        music_index = bundle.getInt("index");
        music_index_first =  0;

        findViewById(R.id.button_prev).setOnClickListener(this);
        findViewById(R.id.button_pause).setOnClickListener(this);
        findViewById(R.id.button_next).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);

        icon_image    =     (ImageView)findViewById(R.id.music_image_icon);
        song_name     =     (TextView)findViewById(R.id.music_song_name);
        timeCurrent   =     (TextView)findViewById(R.id.time_current);
        timeEnd       =     (TextView)findViewById(R.id.time_end);
        music_seekbar =     (SeekBar)findViewById(R.id.music_seekbar);
        volme_seek    =     (SeekBar)findViewById(R.id.volume_seek);

        music_seekbar.setOnSeekBarChangeListener(this);
        volme_seek.setOnSeekBarChangeListener(this);

        music_seekbar.setEnabled(true);
        volme_seek.setEnabled(true);

//        mix = new MediaPlayer();

        final String str_song_name =  getSongNameFromModel(music_index);
        changeImageIcon(music_index);
        playMusic(str_song_name);

//        initMediaplayerFunction();

        initControls();
        initgetbutton();

    }

    private void initgetbutton()
    {
        Button get_button = (Button)findViewById(R.id.music_get_button);
        get_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                String get_url = Constants.g_model.get(music_index).getMusic_get_link();
                i.setData(Uri.parse(get_url));
                startActivity(i);
            }
        });
    }

    private void initControls()
    {
        try
        {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volme_seek .setMax(audioManager
                    .getStreamMaxVolume(audioManager.STREAM_SYSTEM));
            volme_seek .setProgress(1);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private String getSongNameFromModel(int index)
    {
        music_num = Constants.g_model.size();
        MusicModel model = Constants.g_model.get(index);
        String name = model.getMusic_song_name();
        return name;
    }

    private void playMusic(final String str)
    {
        if (mix != null)
        {
            mix.stop();
        }

//        mix = new MediaPlayer();
        mix =MediaPlayer.create(getApplicationContext(), getResources().getIdentifier(str,"raw",getPackageName()));
        mix.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mix.start();
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        mix.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (music_index + 1 >= music_num ) {
                    final String str_song_name =  getSongNameFromModel(music_index_first);
                    changeImageIcon(music_index_first);
                    music_index = 0;
                    music_index_first = 1;
                    music_index += music_index_first;
                    playMusic(str_song_name);
                }
                else {
//                    UiUtils.showShortToast("next song is enable");
                    onClikNextButton();
                }
            }
        });


        final android.os.Handler mHandler = new android.os.Handler();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                music_seekbar.setMax((int)mix.getDuration() / 1000);
                long mCurrentPostion = mix.getCurrentPosition();
                music_seekbar.setProgress((int)mCurrentPostion / 1000);

                timeCurrent.setText(milliSecondsToTimer(mCurrentPostion));
                timeEnd.setText(milliSecondsToTimer(mix.getDuration()));

                mHandler.postDelayed(this, 1000);
            }
        });
    }

    public  String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private void changeImageIcon(int index)
    {

        MusicModel model = Constants.g_model.get(index);
        int imageIndex = model.getIndex();
        icon_image.setImageResource(Constants.music_image_names[imageIndex]);
        song_name.setText(model.getMusic_name() + "\n" + model.getMusic_img());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_button:
                mix.stop();
                finish();
                break;

            case R.id.button_prev:
                onClikPrevButton();
                break;

            case R.id.button_pause:
                onClikPauseButton();
                break;

            case R.id.button_next:
                onClikNextButton();
                break;
        }
    }

    private void onClikNextButton()
    {

        music_index += 1;

        if (music_index == music_num)
        {
//            findViewById(R.id.button_next).setVisibility(View.INVISIBLE);
            music_index = 0;
            String str_name = getSongNameFromModel(music_index);
            changeImageIcon(music_index);
            playMusic(str_name);
        }
        else {
            findViewById(R.id.button_next).setVisibility(View.VISIBLE);
            String str_name = getSongNameFromModel(music_index);
            changeImageIcon(music_index);
            playMusic(str_name);
        }
    }

    public void cancel(){
        m_cancel = true;
    }

    public void onPrepared(MediaPlayer player){
        if(m_cancel){
            player.release();
            //nullify your MediaPlayer reference
            mix = null;
        }
    }

    private void onClikPrevButton()
    {
        mix.stop();

        if (music_index == music_num)
        {
            music_index = 0;
            String str_name = getSongNameFromModel(music_index);
            changeImageIcon(music_index);
            playMusic(str_name);
        }

        if (music_index == 0)
        {
            music_index = music_num -1 ;
            String str_name = getSongNameFromModel(music_index);
            changeImageIcon(music_index);
            playMusic(str_name);

        }
        else {
            music_index -= 1;
            if (music_index < music_num -1)
            {
                findViewById(R.id.button_next).setVisibility(View.VISIBLE);
            }

            findViewById(R.id.button_prev).setVisibility(View.VISIBLE);
            changeImageIcon(music_index);
            String str_name1 = getSongNameFromModel(music_index);
            playMusic(str_name1);
        }
    }

    private void onClikPauseButton()
    {

        btn_flage = !btn_flage;

        if (btn_flage)
        {
            findViewById(R.id.button_pause).setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
            mix.pause();
        }
        else
        {
            findViewById(R.id.button_pause).setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
            mix.start();
        }
    }
    @Override
    public void onBackPressed()
    {
        // Your Code Here. Leave empty if you want nothing to happen on back press.
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


        switch (seekBar.getId())
        {
            case R.id.music_seekbar:
                try {
                    if (mix.isPlaying() || mix != null) {
                        if (b)
                            mix.seekTo(i * 1000);
                    } else if (mix == null) {
                        Toast.makeText(getApplicationContext(), "Media is not running",
                                Toast.LENGTH_SHORT).show();
                        seekBar.setProgress(0);
                    }
                } catch (Exception e) {
//            Log.e("seek bar", "" + e);
                    seekBar.setEnabled(false);

                }
                break;

            case R.id.volume_seek:

                audioManager.setStreamVolume(audioManager.STREAM_MUSIC,
                        i, 0);
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
