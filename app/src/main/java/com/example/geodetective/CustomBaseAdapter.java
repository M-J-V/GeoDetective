package com.example.geodetective;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    String listQuest[];
    int listQuestImages[];
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, String [] quests, int [] images) {
        this.context = ctx;
        this.listQuest = quests;
        this.listQuestImages = images;
        inflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return listQuest.length; //number of row is same as number of input
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
        ImageView buildingImg = (ImageView) view.findViewById(R.id.imageIcon);
        txtView.setText(listQuest[position]);
        buildingImg.setImageResource(listQuestImages[position]);
        return view;
    }
}
