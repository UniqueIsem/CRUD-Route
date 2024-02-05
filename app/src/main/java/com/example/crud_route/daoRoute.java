package com.example.crud_route;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class daoRoute {
    SQLiteDatabase db;
    ArrayList<Route> list;
    Route r;
    Context ct;
    String nombreBD = "BDRoute";
    String table = "create table if not exists route(id integer primary key autoincrement, latA text, longA text, latB text, longB text, name text, type text, description text, rate integer)";


    public daoRoute(Context c) {
        this.ct = c;
        db = c.openOrCreateDatabase(nombreBD, Context.MODE_PRIVATE, null);
        db.execSQL(table);
    }

    public boolean insert(Route r) {
        ContentValues container = new ContentValues();
        container.put("latA", r.getLatA());
        container.put("longA", r.getLongA());
        container.put("latB", r.getLatB());
        container.put("longB", r.getLongB());
        container.put("name", r.getName());
        container.put("type", r.getType());
        container.put("description", r.getDescription());
        container.put("rate", r.getDescription());

        return (db.insert("route", null, container))>0;
    }

    public boolean delete(int id) {
        return true;
    }

    public boolean update(Route r) {
        return true;
    }

    public ArrayList<Route> viewAll() {
        return list;
    }

    public Route viewOne(int id) {
        return r;
    }
}
