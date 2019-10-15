package com.example.oishun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private int[] coverPhotos;
    private String[] contentNames;
    private Context context;
    private LayoutInflater inflater;
    private String[] uploaderNames;

    public CustomAdapter(Context context, String[] contentNames, int[] coverPhotos, String[] uploaderNames) {
        this.context = context;
        this.contentNames = contentNames;
        this.coverPhotos = coverPhotos;
        this.uploaderNames = uploaderNames;
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

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Toast.makeText(context,"dhukse",Toast.LENGTH_LONG);
            convertView = inflater.inflate(R.layout.activity_content_layout, parent, false);
            ImageView coverPhotoImage = (ImageView) convertView.findViewById(R.id.audio_cover_photo);
            TextView contentNameText = (TextView) convertView.findViewById(R.id.content_name_text);
            TextView artistName = convertView.findViewById(R.id.artist_name);
          //  String name = contentNames[position];
            coverPhotoImage.setImageResource(coverPhotos[position % 1]);
            contentNameText.setText(contentNames[position]);
            artistName.setText(uploaderNames[position]);
        }
        return convertView;
    }
}
