package com.example.crud_route.route;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Parcelable {
    int id;
    double latA, longA, latB, longB, rate;
    String name, type, description;
    ArrayList<String> filePaths;
    public Route() {

    }

    public Route(double latA, double longA, double latB, double longB, String name, String type, String description, double rate, ArrayList<String> filePaths) {
        this.latA = latA;
        this.longA = longA;
        this.latB = latB;
        this.longB = longB;
        this.name = name;
        this.type = type;
        this.description = description;
        this.rate = rate;
        this.filePaths = filePaths;
    }

    public Route(int id, double latA, double longA, double latB, double longB, String name, String type, String description, double rate, ArrayList<String> filePaths) {
        this.id = id;
        this.latA = latA;
        this.longA = longA;
        this.latB = latB;
        this.longB = longB;
        this.name = name;
        this.type = type;
        this.description = description;
        this.rate = rate;
        this.filePaths = filePaths;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatA() {
        return latA;
    }

    public void setLatA(double latA) {
        this.latA = latA;
    }

    public double getLongA() {
        return longA;
    }

    public void setLongA(double longA) {
        this.longA = longA;
    }

    public double getLatB() {
        return latB;
    }

    public void setLatB(double latB) {
        this.latB = latB;
    }

    public double getLongB() {
        return longB;
    }

    public void setLongB(double longB) {
        this.longB = longB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public ArrayList<String> getFilePaths() { return filePaths; }

    public void setFilePaths(ArrayList<String> filePaths) {
        this.filePaths = filePaths;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int i) {
        dest.writeDouble(latA);
        dest.writeDouble(longA);
        dest.writeDouble(latB);
        dest.writeDouble(longB);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeDouble(rate);
        //dest.write filePaths
    }
}

