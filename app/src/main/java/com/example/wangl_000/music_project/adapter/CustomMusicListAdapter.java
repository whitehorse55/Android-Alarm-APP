package com.example.wangl_000.music_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangl_000.music_project.Model.MusicModel;
import com.example.wangl_000.music_project.Music;
import com.example.wangl_000.music_project.R;
import com.example.wangl_000.music_project.UiUtils.Constants;

import java.util.ArrayList;
import java.util.Locale;

import static android.R.id.message;

/**
 * Created by WangL_000 on 2017/9/23.
 */

public class CustomMusicListAdapter extends BaseAdapter implements Filterable {

    @Override
    public Filter getFilter() {
        return null;
    }

    private static class ViewHolder
    {
        TextView    music_name;
        TextView    music_art;
        ImageView   music_checked;
        ImageView   music_icon;
        Button      music_get_button;
    }

    private Context mContext;
    private ArrayList<MusicModel> music_model;


    public CustomMusicListAdapter(Context context, ArrayList<MusicModel> model) {

        mContext = context;

        music_model = new ArrayList<MusicModel>(  );
        music_model.clear();
        music_model.addAll(model) ;
    }

    @Override
    public int getCount() {
        return music_model.size();
    }

    @Override
    public Object getItem(int i) {
        return getItemId(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        MusicModel model = new MusicModel();
        model = music_model.get(position);
        final String get_url = model.getMusic_get_link();

        CustomMusicListAdapter.ViewHolder holder = new CustomMusicListAdapter.ViewHolder();
        if (convertView == null)
        {
            LayoutInflater inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.custom_item_music, null);

            holder.music_name       =   (TextView) convertView.findViewById(R.id.music_name);
            holder.music_art        =   (TextView) convertView.findViewById(R.id.music_art);
            holder.music_checked    =   (ImageView)convertView.findViewById(R.id.music_checked_image);
            holder.music_icon       =   (ImageView)convertView.findViewById(R.id.music_icon_image);
            holder.music_get_button =   (Button)convertView.findViewById(R.id.get_button);
            convertView.setTag( holder );
        }
        else
        {
            holder = (CustomMusicListAdapter.ViewHolder)convertView.getTag();
        }

        holder.music_name.setText(model.getMusic_name());
        holder.music_art.setText(model.getMusic_img());
        holder.music_icon.setImageResource(Constants.music_image_names_list[model.getIndex()]);


        holder.music_get_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(get_url));
                 mContext.startActivity(i);
            }
        });

        if (model.isChecked() == true)
        {
//            holder.music_checked.setImageResource(R.drawable.ic_panorama_fish_eye_black_24dp);
            holder.music_checked.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.music_checked.setImageResource(R.drawable.circle);
        }
        return convertView;
    }

}
