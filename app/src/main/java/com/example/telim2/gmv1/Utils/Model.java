package com.example.telim2.gmv1.Utils;

/**
 * Created by telim2 on 21.08.2017.
 */

public class Model {
    String lon,lan,name;

    public Model(String lon, String lan, String name) {
        this.lon = lon;
        this.lan = lan;
        this.name = name;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
