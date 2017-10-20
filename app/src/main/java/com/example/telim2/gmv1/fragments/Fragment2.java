package com.example.telim2.gmv1.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.telim2.gmv1.Constants;
import com.example.telim2.gmv1.Utils.DatabaseHelper;
import com.example.telim2.gmv1.Utils.GPSTracker;
import com.example.telim2.gmv1.Utils.Model;
import com.example.telim2.gmv1.Utils.MyAdapter;
import com.example.telim2.gmv1.Utils.MySingleTon;
import com.example.telim2.gmv1.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by galqayeva on 21.08.2017.
 */

public class Fragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Model> modelList;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    DatabaseHelper myDB;
    String url,Tag="salus";


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment2,container,false);

        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myDB = new DatabaseHelper(getActivity());
        modelList=new ArrayList<>();

        gpsTracker = new GPSTracker(getActivity());
        latitude = gpsTracker.getLocation().getLatitude();
        longitude = gpsTracker.getLocation().getLongitude();

        url= Constants.gmApi(Double.toString(latitude),Double.toString(longitude),"500");
        loadListview();
        loadRestaurants();

        return view;
    }


    public void loadRestaurants(){

        Cursor data = myDB.getAlldata();

        if(data.getCount() == 0)
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Getting Restaurants");
            progressDialog.show();

            StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                JSONArray jsonArrayResult=jsonObject.getJSONArray("results");

                                for (int i=0;i<jsonArrayResult.length();i++)
                                {
                                    JSONObject jsonobject=jsonArrayResult.getJSONObject(i);
                                    String lat = jsonobject.getJSONObject("geometry").getJSONObject("location").getString("lat");
                                    String lng = jsonobject.getJSONObject("geometry").getJSONObject("location").getString("lng");
                                    String restaurantName=jsonobject.getString("name");

                                    boolean insertData = myDB.addData(restaurantName,lng,lat);
                                    if(!insertData==true)
                                        Log.d(Tag,"Inserting problem");
                                }

                                modelList.clear();
                                loadListview();
                                progressDialog.dismiss();

                            } catch (JSONException e) {

                                Log.d(Tag, "json parse error");
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d(Tag,"String request Response error ----"+ error.getMessage());
                        }
                    }

            );
            MySingleTon.getInstance(getActivity()).addToRequestQueue(stringRequest);

            if(swipeRefreshLayout.isRefreshing())
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }


    public void loadListview(){

        Cursor data = myDB.getAlldata();

        if(data.getCount() != 0)
        {
            Log.d(Tag,"----------"+data.getCount());
            while(data.moveToNext())
            {
                Model item=new Model(data.getString(1),data.getString(2),data.getString(3));
                modelList.add(item);
            }

            adapter=new MyAdapter(modelList,getActivity());
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void onRefresh() {
        myDB.deleteAll();
        loadRestaurants();
    }
}
