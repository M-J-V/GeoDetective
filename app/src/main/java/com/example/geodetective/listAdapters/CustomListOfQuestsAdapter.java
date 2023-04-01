package com.example.geodetective.listAdapters;

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

    Context context;
    ArrayList<String> titles;
    ArrayList<String> creators;
    ArrayList<Bitmap> questImages;
    LayoutInflater inflater;

    public CustomListOfQuestsAdapter(Context ctx, ArrayList<String> quests, ArrayList<String> authors, ArrayList<Bitmap> images) {
        this.context = ctx;
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

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view, null);
        TextView txtView = (TextView) view.findViewById(R.id.textView);
        TextView txtViewAuthor = (TextView) view.findViewById(R.id.textDesc);
        ImageView buildingImg = (ImageView) view.findViewById(R.id.imageIcon);
        txtView.setText(titles.get(position));
        txtViewAuthor.setText(creators.get(position));
        buildingImg.setImageBitmap(questImages.get(position));
        return view;
    }
}
