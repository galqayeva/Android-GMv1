package com.example.telim2.gmv1.Adapters;

/**
 * Created by telim2 on 21.08.2017.
 */

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.telim2.gmv1.MainActivity;
import com.example.telim2.gmv1.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by telim2 on 01.08.2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Model> modelList;
    private Context context;
    String url;

    String registerId,name,lon,lat;

    public MyAdapter(List<Model> modelListt, Context context) {
        this.modelList=modelListt;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview,parent,false);

        registerId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Model model=modelList.get(position);
        holder.buttonAdd.setText("add");
        name=model.getName();
        lon=model.getLon();
        lat=model.getLan();
        holder.textViewFriend.setText(name);

        url="http://172.16.200.200/GMv1/insertRest.php?restName="+name+"&lon="+lon+"&lat="+lat+"&registerId="+registerId+"&onrest=1";

        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                               Toast.makeText(context,url,Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context,"noot",Toast.LENGTH_LONG).show();
                            }
                        }
                );
                MySingleTon.getInstance(context).addToRequestQueue(stringRequest);
            }
        });





    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewFriend;
        public Button buttonAdd;
        public ImageView imageView;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewFriend=(TextView)itemView.findViewById(R.id.restaurantName);
          buttonAdd=(Button)itemView.findViewById(R.id.addButton);
          //  checkBox=(CheckBox)itemView.findViewById(R.id.checkbox);
        }
    }
}
