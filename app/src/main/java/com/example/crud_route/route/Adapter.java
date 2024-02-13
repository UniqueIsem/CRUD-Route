package com.example.crud_route.route;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crud_route.R;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    ArrayList<Route> list;
    daoRoute dao;
    Route r;
    Activity a;
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

    public Double getLatitudeB() {
        return r.getLatB();
    }

    public Double getLongitudeB() {
        return r.getLongB();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_route, null);
        }
        r = list.get(position);
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

        name.setText(r.getName());
        type.setText(r.getType());
        //description.setText(r.getDescription());
        rate.setText("" + r.getRate());


        update.setTag(position);
        delete.setTag(position);

        View finalV = v;
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = Integer.parseInt(view.getTag().toString());
                final Dialog dialog = new Dialog(a);
                dialog.setTitle("Update Route");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.update_dialog);
                dialog.show();

                final EditText updateName = (EditText) dialog.findViewById(R.id.update_route_name);
                //Get value from spinner
                final Spinner updateType = (Spinner) dialog.findViewById(R.id.update_routeTypeSpinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(a.getApplication(), R.array.routeTypes, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                updateType.setAdapter(adapter);
                updateType.setOnItemSelectedListener(spinnerListener);

                final EditText updateDescription = (EditText) dialog.findViewById(R.id.update_route_description);
                final Button saveUpdatedData = (Button) dialog.findViewById(R.id.btnUpdate);
                //Get value from rate
                /*ImageButton rateStar1 = (ImageButton) finalV.findViewById(R.id.updateRateStar1);
                ImageButton rateStar2 = (ImageButton) finalV.findViewById(R.id.updateRateStar2);
                ImageButton rateStar3 = (ImageButton) finalV.findViewById(R.id.updateRateStar3);
                ImageButton rateStar4 = (ImageButton) finalV.findViewById(R.id.updateRateStar4);
                ImageButton rateStar5 = (ImageButton) finalV.findViewById(R.id.updateRateStar5);
                View.OnClickListener starClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.rateStar1) {
                            updateRate = 1;
                        } else if (v.getId() == R.id.rateStar2) {
                            updateRate = 2;
                        } else if (v.getId() == R.id.rateStar3) {
                            updateRate = 3;
                        } else if (v.getId() == R.id.rateStar4) {
                            updateRate = 4;
                        } else if (v.getId() == R.id.rateStar5) {
                            updateRate = 5;
                        }
                        updateStarIcons();
                    }

                    private void updateStarIcons() {
                        rateStar1.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_off));
                        rateStar2.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_off));
                        rateStar3.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_off));
                        rateStar4.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_off));
                        rateStar5.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_off));
                        switch (updateRate) {
                            case 5:
                                rateStar5.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_on));
                            case 4:
                                rateStar4.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_on));
                            case 3:
                                rateStar3.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_on));
                            case 2:
                                rateStar2.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_on));
                            case 1:
                                rateStar1.setImageDrawable(a.getResources().getDrawable(android.R.drawable.btn_star_big_on));
                        }
                    }
                };
                rateStar1.setOnClickListener(starClickListener);
                rateStar2.setOnClickListener(starClickListener);
                rateStar3.setOnClickListener(starClickListener);
                rateStar4.setOnClickListener(starClickListener);
                rateStar5.setOnClickListener(starClickListener);*/

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
                            adapter.notifyDataSetChanged();
                            Log.d("ISEM", "Route created");
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
                int pos = Integer.parseInt(view.getTag().toString());
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
        return v;
    }

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            String typeSpiner = adapterView.getItemAtPosition(position).toString();
            updateTypeString = typeSpiner;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {        }
    };
}
