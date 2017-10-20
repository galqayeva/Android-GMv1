package com.example.telim2.gmv1;

/**
 * Created by telim2 on 12.09.2017.
 */


public class Constants {

    public static String  gmApi(String latitude,String longitude,String radius){
       String url= "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius="+radius+"&type=restaurant&key=AIzaSyC3_ndLS93DsNFqSB-78VuA00A0hrI8B5A";
        return url;
    }
}
