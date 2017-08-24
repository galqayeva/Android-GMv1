package com.example.telim2.gmv1.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.telim2.gmv1.Adapters.GPSTracker;
import com.example.telim2.gmv1.Adapters.Model;
import com.example.telim2.gmv1.Adapters.MyAdapter;
import com.example.telim2.gmv1.Adapters.MySingleTon;
import com.example.telim2.gmv1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by telim2 on 21.08.2017.
 */

public class Fragment2 extends Fragment {



    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    String url;
    TextView tw;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Model> modelList;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment2,container,false);

        gpsTracker = new GPSTracker(getActivity());
        mLocation = gpsTracker.getLocation();

        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        recyclerView=(RecyclerView)view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        modelList=new ArrayList<>();



        url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+Double.toString(latitude)+","+Double.toString(longitude)+"&radius=500&type=restaurant&key=AIzaSyC3_ndLS93DsNFqSB-78VuA00A0hrI8B5A";
        //url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=40.404654,49.8722473&radius=500&type=restaurant&key=AIzaSyC3_ndLS93DsNFqSB-78VuA00A0hrI8B5A";



        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("results");

                            for (int i=0;i<jsonArray.length();i++) {

                                JSONObject jsonobject=jsonArray.getJSONObject(i);
                                String lat = jsonobject.getJSONObject("geometry").getJSONObject("location").getString("lat");
                                String lan = jsonobject.getJSONObject("geometry").getJSONObject("location").getString("lng");
                                String location=jsonobject.getString("name");

                                Model item=new Model(lan,lat,location);
                                modelList.add(item);


                            }


                            adapter=new MyAdapter(modelList,getActivity());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();

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
