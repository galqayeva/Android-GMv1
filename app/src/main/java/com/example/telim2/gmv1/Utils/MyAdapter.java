package com.example.telim2.gmv1.Utils;

/**
 * Created by galqayeva on 21.08.2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.telim2.gmv1.MainActivity;
import com.example.telim2.gmv1.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Model> modelList;
    private Context context;
    String url;

    String registerId,name,lon,lat;
    int ok=1;

    public MyAdapter(List<Model> modelList, Context context) {
        this.modelList=modelList;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Model model=modelList.get(position);
        holder.buttonAdd.setText("+");

        holder.textViewFriend.setText(model.getrName());

        url="http://172.16.200.200/GMv1/insertRest.php";



        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                                    Log.d("salus", "a"+error.getMessage());
                                }
                            }
                    ){

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("restName",model.getrName()); Log.d("a",model.getrName()+"------"+model.getLat()+"------"+model.getLng());
                            params.put("lon",model.getLng());
                            params.put("lat",model.getLat());
                            params.put("registerId",registerId);
                            params.put("onrest","1");
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(2 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleTon.getInstance(context).addToRequestQueue(stringRequest);

                    holder.buttonAdd.setText("here");


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
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewFriend=(TextView)itemView.findViewById(R.id.restaurantName);
            buttonAdd=(Button)itemView.findViewById(R.id.addButton);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.linearLayout);
        }
    }
}
