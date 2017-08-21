package com.example.telim2.gmv1.Adapters;

/**
 * Created by telim2 on 21.08.2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
    String url="http://172.16.205.132/android/friendrequest.php";

    public MyAdapter(List<Model> modelListt, Context context) {
        this.modelList=modelListt;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Model model=modelList.get(position);
        holder.buttonAdd.setText("add");
        holder.textViewFriend.setText(model.getName());



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewFriend;
        public Button buttonAdd;
        public ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewFriend=(TextView)itemView.findViewById(R.id.restaurantName);
            buttonAdd=(Button)itemView.findViewById(R.id.addButton);
        }
    }
}
