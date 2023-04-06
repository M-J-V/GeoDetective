package com.example.geodetective.guiListAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geodetective.R;

import java.util.ArrayList;

public class CustomListOfQuestsAdapter extends BaseAdapter {

    private final ArrayList<String> titles;
    private final ArrayList<String> creators;
    private final ArrayList<Bitmap> questImages;
    private final LayoutInflater inflater;

    public CustomListOfQuestsAdapter(Context ctx, ArrayList<String> quests, ArrayList<String> authors, ArrayList<Bitmap> images) {
        this.titles = quests;
        this.creators = authors;
        this.questImages = images;
        inflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return titles.size(); //number of row is same as number of input
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view, null);
        TextView txtView = view.findViewById(R.id.textView);
        TextView txtViewAuthor = view.findViewById(R.id.textDesc);
        ImageView buildingImg = view.findViewById(R.id.imageIcon);
        txtView.setText(titles.get(position));
        txtViewAuthor.setText(creators.get(position));
        buildingImg.setImageBitmap(questImages.get(position));
        return view;
    }
}
