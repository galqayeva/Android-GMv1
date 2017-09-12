package next.whatsnext.mainRecycler;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import next.whatsnext.R;


@SuppressWarnings("ALL")
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private List<MainData> data;

    private Context context;
    MainAdapter(Context context, List<MainData> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new MainAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.ad.setText(data.get(position).getMname());
        holder.mes.setText(data.get(position).getMmessage());
        holder.dat.setText(data.get(position).getMdate());

        StorageReference reference = FirebaseStorage.getInstance().getReference();

        StorageReference ref = reference.child("profil").child(data.get(position).getMname());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String urlst = uri.toString();
                Glide.with(context).load(urlst).apply(RequestOptions.centerCropTransform()).into(holder.img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ad;
        ImageView img;
        TextView mes;
        TextView dat;



        MyViewHolder(View view){
            super(view);
            ad = view.findViewById(R.id.MainName);
            img = view.findViewById(R.id.Mainimg);
            mes = view.findViewById(R.id.MainMassage);
            dat = view.findViewById(R.id.MainDate);
        }
    }
}
