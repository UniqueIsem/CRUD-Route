package com.example.crud_route.route;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.sax.ElementListener;
import android.text.TextUtils;

import java.util.ArrayList;

public class daoRoute {
    SQLiteDatabase db;
    ArrayList<Route> list = new ArrayList<Route>();
    Route r;
    Context ct;
    String nombreBD = "BDRoute";
    String table = "create table if not exists route(id integer primary key autoincrement, latA text, longA text, latB text, longB text, name text, type text, description text, rate integer, filePaths text)";


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
        String filePathsString = TextUtils.join(",", r.getFilePaths());
        container.put("filePaths", filePathsString);

        return (db.insert("route", null, container))>0;
    }

    public boolean delete(int id) {
        return true;
    }

    public boolean update(Route r) {
        return true;
    }

    public ArrayList<Route> viewAll() {
        list.clear();
        Cursor cursor = db.rawQuery("select * from route", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(new Route(cursor.getInt(0), //id
                        cursor.getDouble(1), //latA
                        cursor.getDouble(2), //longA
                        cursor.getDouble(3), //latB
                        cursor.getDouble(4), //longB
                        cursor.getString(5), //name
                        cursor.getString(6), //type
                        cursor.getString(7), //description
                        cursor.getDouble(8), //rate
                        cursor.getExtras().getStringArrayList(String.valueOf(9))
                        ));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public Route viewOne(int id) {
        Cursor cursor = db.rawQuery("select * from route", null);
        cursor.moveToPosition(id);
        r = new Route(cursor.getInt(0), //id
                cursor.getDouble(1), //latA
                cursor.getDouble(2), //longA
                cursor.getDouble(3), //latB
                cursor.getDouble(4), //longB
                cursor.getString(5), //name
                cursor.getString(6), //type
                cursor.getString(7), //description
                cursor.getDouble(8), //rate
                cursor.getExtras().getStringArrayList(String.valueOf(9)));
        return r;
    }
}
