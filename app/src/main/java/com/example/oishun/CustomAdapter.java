package com.example.oishun;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private int[] coverPhotos;
    private String[] photos;
    private String[] contentNames;
    private Context context;
    private LayoutInflater inflater;
    private String[] uploaderNames;
    private String[] durations;

    public CustomAdapter(Context context, String[] contentNames, String[] coverPhotos, String[] uploaderNames,String[] durations) {
        this.context = context;
        this.contentNames = contentNames;
        this.photos = coverPhotos;
        this.uploaderNames = uploaderNames;
        this.durations = durations;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Toast.makeText(context,"dhukse",Toast.LENGTH_LONG);
            convertView = inflater.inflate(R.layout.activity_content_layout, parent, false);

        }
        ImageView coverPhotoImage = (ImageView) convertView.findViewById(R.id.audio_cover_photo);
        TextView contentNameText = (TextView) convertView.findViewById(R.id.content_name_text);
        TextView artistName = convertView.findViewById(R.id.artist_name);
        TextView durationText = convertView.findViewById(R.id.duration);
        //  String name = contentNames[position];
        // coverPhotoImage.setImageResource(coverPhotos[position % 1]);
        Glide.with(context).load(photos[position]).into(coverPhotoImage);
        contentNameText.setText(contentNames[position]);
        artistName.setText(uploaderNames[position]);
        durationText.setText(durations[position]);
        //artistName.setOnClickListener(this);
           /* artistName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,UserPage.class);
                    intent.putExtra("user_name", "yamin");
                    context.startActivity(intent);
                }
            });*/
        return convertView;
    }


}
