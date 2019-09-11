package com.example.oishun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    private int[] coverPhotos;
    private String[] contentNames;
    private Context context;
    private LayoutInflater inflater;

    public CustomAdapter(Context context,String[] contentNames, int[] coverPhotos){
        this.context = context;
        this.contentNames = contentNames;
        this.coverPhotos = coverPhotos;
    }

    @Override
    public int getCount() {
        return contentNames.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_content_layout,parent,false);
            ImageView coverPhotoImage = (ImageView) convertView.findViewById(R.id.audio_cover_photo);
            TextView contentNameText = (TextView) convertView.findViewById(R.id.content_name_text);
            coverPhotoImage.setImageResource(coverPhotos[position % 1]);
            contentNameText.setText(contentNames[position]);
        }
        return convertView;
    }
}
