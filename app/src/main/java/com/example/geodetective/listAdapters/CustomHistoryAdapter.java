package com.example.geodetective.listAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.geodetective.R;

import java.util.ArrayList;

public class CustomHistoryAdapter extends BaseAdapter {

    final String SUCCESS = "Success";
    final String FAIL = "Fail";
    Context context;
    ArrayList<String> titles;
    ArrayList<String> timesOfCompletion;

    ArrayList<Integer> outcomes;
    LayoutInflater inflater;

    public CustomHistoryAdapter(Context ctx, ArrayList<String> quests, ArrayList<String> times, ArrayList<Integer> outcomes) {
        this.context = ctx;
        this.titles = quests;
        this.timesOfCompletion = times;
        this.outcomes = outcomes;
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
        view = inflater.inflate(R.layout.activity_custom_history, null);
        TextView titleTxt = view.findViewById(R.id.textTitle);
        TextView timeTxt = view.findViewById(R.id.textTime);
        TextView passTxt = view.findViewById(R.id.textResult);
        titleTxt.setText(titles.get(position));
        timeTxt.setText(timesOfCompletion.get(position));
        if(outcomes.get(position) == 1) {
            passTxt.setTextColor(Color.GREEN);
            passTxt.setText(SUCCESS);
        } else {
            passTxt.setTextColor(Color.RED);
            passTxt.setText(FAIL);
        }
        return view;
    }
}
