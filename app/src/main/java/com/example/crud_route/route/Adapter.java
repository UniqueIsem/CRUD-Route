package com.example.crud_route.route;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.crud_route.R;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    ArrayList<Route> list;
    daoRoute dao;
    Route r;
    Activity a;
    public Adapter(Activity a, ArrayList<Route> list, daoRoute dao) {
        this.a = a;
        this.list = list;
        this.dao = dao;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Route getItem(int i) {
        r = list.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        r = list.get(i);
        return r.getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_route, null);
        }
        r = list.get(position);
        TextView name = (TextView) v.findViewById(R.id.routeName);
        TextView type = (TextView) v.findViewById(R.id.routeType);
        TextView rate = (TextView) v.findViewById(R.id.routeRate);
        ImageButton update = (ImageButton) v.findViewById(R.id.routeUpdate);
        ImageButton delete = (ImageButton) v.findViewById(R.id.routeDelete);

        name.setText(r.getName());
        type.setText(r.getType());
        rate.setText((int) r.getRate());
        update.setTag(position);
        delete.setTag(position);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return v;
    }
}
