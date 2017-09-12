package next.whatsnext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import next.whatsnext.mainRecycler.MainActivity;

public class Profil extends AppCompatActivity {


    private ImageView img;
    EditText ad;
    EditText editemail;

    private String us;
    private FirebaseUser user;
    private ProgressDialog dialog;

    String UserName;

    private static final int GALLERY_INTENT = 2;
    private static final long ONE_MEGABYTE = 1024*1024;
    private StorageReference storRef;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        img = (ImageView) findViewById(R.id.profilImg);
        ad = (EditText) findViewById(R.id.profilAd);
        editemail = (EditText) findViewById(R.id.profileEmail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        us = user.getUid();


        SharedPreferences prefs = getApplicationContext().getSharedPreferences("WhatsNext",0);
        username = prefs.getString("username","");


        final String email = user.getEmail();
        editemail.setText(email);


        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference q1 = ref.child("users");

        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    String dataMail = data.getValue(String.class);
                    assert dataMail != null;
                    if (dataMail.equals(email)){
                        UserName = data.getKey();

                        DatabaseReference q2 = ref.child("UserProfile").child(UserName).child("ad");
                        q2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                String name1 = dataSnapshot1.getValue(String.class);
                                ad.setText(name1);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        storRef = FirebaseStorage.getInstance().getReference();

        dialog = new ProgressDialog(this);

        downloadStorageMethod();
        uploadStorageMethod();
    }


    public void onTesdiq1(View v){
        String ad123 =  ad.getText().toString().trim();

        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        ref1.child("UserProfile").child(UserName).child("ad").setValue(ad123);
        user.updateEmail(editemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                ref1.child("users").child(username).setValue(editemail.getText().toString());}
                else {
                    Toast.makeText(Profil.this,"E-mailinizi dəyişmək üçün əsas ekrandan logout oluub,yenidən daxil olun!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toast.makeText(this,"Profiliniz yeniləndi!",Toast.LENGTH_SHORT).show();
    }



    private void downloadStorageMethod(){


        final StorageReference imgRef = storRef.child("profil/"+username);
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Profil.this).load(uri).into(img);
            }
        });


    }

    private void uploadStorageMethod(){

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivityForResult(i,GALLERY_INTENT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            dialog.setMessage("Yüklənir...");

            Uri uri = data.getData();

            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                    .start(this);

        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            Log.e("test","resulok");

            final Uri resultUri = result.getUri();
            dialog.show();



            StorageReference filepath = storRef.child("profil").child(username);
            filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(Profil.this, "Şəkliniz Yükləndi!",Toast.LENGTH_LONG).show();
                    img.setImageURI(resultUri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profil.this,"Şəkil 1 mb-dan az olmalıdır!!!",Toast.LENGTH_LONG).show();
                }
            });

        }

    }

}
