package next.whatsnext.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import next.whatsnext.mainRecycler.MainActivity;
import next.whatsnext.R;

public class SignUp extends Fragment {

    private FirebaseAuth auth;
    private SharedPreferences prefs = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_sign_up,container,false);

        final EditText ad = v.findViewById(R.id.signUpAd);
        final EditText pass = v.findViewById(R.id.signUpPass);
        final EditText mail = v.findViewById(R.id.signUpMail);
        final EditText username = v.findViewById(R.id.signUpUsername);

        Button tesdiq = v.findViewById(R.id.signUpTesdiq);

        tesdiq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pass.getText().toString().trim().length()>6){
                    pass.setError("Şifrə 6 simvoldan az ola bilməz!");
                }
                else {

                    final String email = mail.getText().toString().trim().toLowerCase();
                    final String passw = pass.getText().toString().trim();
                    final String adw = ad.getText().toString().trim();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference q = ref.child("users");
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String dataKey = data.getKey();
                                final String userKey = username.getText().toString().trim().toLowerCase();


                                if (userKey.equals(dataKey)) {
                                    username.setError("Bu username artiq istifadə olunur!");
                                    username.setText("");
                                } else {
                                    //noinspection unchecked
                                    auth.createUserWithEmailAndPassword(email, passw)
                                            .addOnCompleteListener(getActivity(), new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Authentication failed." + task.getException(),
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                        ref.child("users").child(userKey).setValue(email);
                                                        SharedPreferences.Editor editor = prefs.edit();
                                                        editor.putString("username",userKey);
                                                        editor.commit();

                                                        ref.child("UserProfile").child(userKey).child("ad").setValue(adw);

                                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                                    }
                                                }
                                            });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        prefs = getActivity().getSharedPreferences("WhatsNext",0);
    }
}
