package com.example.oishun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {

    private int[] coverPhotos;
    private String[] contentList;
    Context context;
    private LayoutInflater inflater;

    UserAdapter(Context context, String[] contentList, int[] coverPhotos){
        this.contentList = contentList;
        this.context = context;
        this.coverPhotos = coverPhotos;
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

        coverPhotoImage.setImageResource(coverPhotos[position % 1]);
        contentNameText.setText(contentList[position]);

        return view;
    }
}
