package com.example.crud_route.route;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crud_route.HomePage;
import com.example.crud_route.ItemFileAdapter;
import com.example.crud_route.Map;
import com.example.crud_route.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter {
    ArrayList<Route> list;
    ArrayList<String> imageUrls;
    daoRoute dao;
    Route r;
    Activity a;
    HomePage homePage;
    String updateTypeString;
    int updateRate = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id = 0;
    public Adapter(Activity a, ArrayList<Route> list, daoRoute dao) {
        this.a = a;
        this.list = list;
        this.dao = dao;
        this.homePage = homePage;
    }
    public Adapter(ArrayList<String> urls) {
        this.imageUrls = urls;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Route getItem(int i) {
        r = list.get(i);
        return r;
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
        RecyclerView recyclerView = v.findViewById(R.id.recycler_view_img);
        List<String> imageUrls = getItem(position).getFilePaths();
        ItemFileAdapter itemFileAdapter = new ItemFileAdapter(a, imageUrls);
        recyclerView.setAdapter(itemFileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(a, RecyclerView.HORIZONTAL, false));
        r = list.get(position);
        // Initialization of tags from selected item_route
        TextView latA = v.findViewById(R.id.routeLatitudeA);
        TextView longA = v.findViewById(R.id.routeLongitudeA);
        TextView latB = v.findViewById(R.id.routeLatitudeB);
        TextView longB = v.findViewById(R.id.routeLongitudeB);
        TextView name = v.findViewById(R.id.routeName);
        TextView type = v.findViewById(R.id.routeType);
        TextView description = v.findViewById(R.id.routeDescription);
        TextView rate = v.findViewById(R.id.routeRate);
        ImageButton update = v.findViewById(R.id.routeUpdate);
        ImageButton delete = v.findViewById(R.id.routeDelete);
        ImageButton directions = v.findViewById(R.id.routeDirections);
        //Set values to tags from DataBase
        latB.setText(String.valueOf(r.getLatB()));
        longB.setText(String.valueOf(r.getLongB()));
        name.setText(r.getName());
        type.setText(r.getType());
        description.setText(r.getDescription());
        rate.setText("" + r.getRate());
        // Get buttons from selected item_route
        update.setTag(position);
        delete.setTag(position);
        directions.setTag(position);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = Integer.parseInt(view.getTag().toString());
                final Dialog dialog = new Dialog(a);
                dialog.setTitle("Update Route");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.update_dialog);
                dialog.show();

                final EditText updateName = (EditText) dialog.findViewById(R.id.update_route_name);
                final EditText updateDescription = (EditText) dialog.findViewById(R.id.update_route_description);
                final Button saveUpdatedData = (Button) dialog.findViewById(R.id.btnUpdate);

                r = list.get(pos);
                setId(r.getId());
                updateName.setText(r.getName());
                updateDescription.setText(r.getDescription());
                saveUpdatedData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            r = new Route(getId(),
                                    Double.parseDouble(latA.getText().toString()),
                                    Double.parseDouble(longA.getText().toString()),
                                    Double.parseDouble(latB.getText().toString()),
                                    Double.parseDouble(longB.getText().toString()),
                                    updateName.getText().toString(),
                                    updateTypeString,
                                    updateDescription.getText().toString(),
                                    updateRate,
                                    null);
                            dao.update(r);
                            list = dao.viewAll();
                            notifyDataSetChanged();
                            dialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(a, "ERROR: " + e , Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = Integer.parseInt(view.getTag().toString());
                r = list.get(pos);
                setId(r.getId());
                AlertDialog.Builder del = new AlertDialog.Builder(a);
                del.setMessage("This will delete the selected route \n Are you sure?");
                del.setCancelable(false);
                del.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.delete(getId());
                        list = dao.viewAll();
                        notifyDataSetChanged();
                    }
                });
                del.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                del.show();
            }
        });
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(a, Map.class);
                intent.putExtra("latitude", Double.parseDouble(latB.getText().toString()));
                intent.putExtra("longitude", Double.parseDouble(longB.getText().toString()));
                a.startActivity(intent);
            }
        });
        return v;
    }

    public void viewAllRoutes() {
        list = dao.viewAll();
        notifyDataSetChanged();
    }
}
