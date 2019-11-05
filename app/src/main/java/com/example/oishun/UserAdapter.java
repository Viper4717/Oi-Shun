package com.example.oishun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class UserAdapter extends BaseAdapter {

    String[] coverPhotos;
    private String[] contentList;
    Context context;
    private LayoutInflater inflater;
    private String[] uploaderNames;
    private String[] durations;


    UserAdapter(Context context, String[] contentList, String[] coverPhotos, String[] uploaderNames,String[] durations){
        this.contentList = contentList;
        this.context = context;
        this.coverPhotos = coverPhotos;
        this.uploaderNames = uploaderNames;
        this.durations = durations;
    }

    @Override
    public int getCount() {
        return contentList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_content_layout,viewGroup,false);
        }
        ImageView coverPhotoImage = view.findViewById(R.id.audio_cover_photo);
        TextView contentNameText = view.findViewById(R.id.content_name_text);
        TextView artistName = view.findViewById(R.id.artist_name);
        TextView durationText = view.findViewById(R.id.duration);

        Glide.with(context).load(coverPhotos[position]).into(coverPhotoImage);
        contentNameText.setText(contentList[position]);
        artistName.setText(uploaderNames[position%1]);
        durationText.setText(durations[position]);

        return view;
    }
}
