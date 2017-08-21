package com.example.telim2.gmv1.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.telim2.gmv1.Adapters.GPSTracker;
import com.example.telim2.gmv1.Adapters.MySingleTon;
import com.example.telim2.gmv1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by telim2 on 21.08.2017.
 */

public class Fragment2 extends Fragment {



    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    String url;
TextView tw;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment2,container,false);

        gpsTracker = new GPSTracker(getActivity());
        mLocation = gpsTracker.getLocation();

        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+Double.toString(latitude)+","+Double.toString(longitude)+"&radius=500&type=restaurant&key=AIzaSyBTpzRpZ2Be4zq5TMfCupjSruw8MeXHda8";


        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "okkkk",Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("results");

                            for (int i=0;i<=jsonArray.length();i++) {

                                JSONObject jsonobject=jsonArray.getJSONObject(i);
                                String lat = jsonobject.getJSONObject("geometry").getJSONObject("location").getString("lat");
                                String lan = jsonobject.getJSONObject("geometry").getJSONObject("location").getString("lng");
                                String location=jsonobject.getString("name");


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "not ok",Toast.LENGTH_LONG).show();
                    }
                }

        );
        MySingleTon.getInstance(getActivity()).addToRequestQueue(stringRequest);



        return view;

    }
}
