package next.whatsnext.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class SignIn extends Fragment {

    EditText username;
    EditText pass;
    private FirebaseAuth auth;
    Button logInBut;

    TextView unut;

    String emailN;
    private SharedPreferences prefs = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_sign_in,container,false);

        username = (EditText) v.findViewById(R.id.LoginUser);
        pass = (EditText) v.findViewById(R.id.LoginPass);
        logInBut = (Button) v.findViewById(R.id.loginBut);
        unut = (TextView) v.findViewById(R.id.signInUnut);

        unut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),unutdum.class);
                startActivity(i);
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference q = ref.child("users");
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userKey = username.getText().toString().trim().toLowerCase();
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            String dataKey = data.getKey();
                            if (dataKey.equals(userKey)){
                                emailN = data.getValue(String.class);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("username",dataKey);
                                editor.commit();

                            }else {
                              //  username.setError("Username səhvdir!");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        logInBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String password = pass.getText().toString().trim();

                if (emailN == null) {

                    Toast.makeText(getActivity(), "Username Səhvdir!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(emailN.equals("")){
                    Toast.makeText(getActivity(), "Username-i daxil edin!", Toast.LENGTH_SHORT).show();
                    return;}

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Şifrəni daxil edin!", Toast.LENGTH_SHORT).show();
                    return;
                }



                //noinspection unchecked
                auth.signInWithEmailAndPassword(emailN,password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Username və ya şifrə səhvdir!,\nYenidən yoxlayın.", Toast.LENGTH_LONG).show();
                                } else {
                                    startActivity(new Intent(getActivity(), MainActivity.class));

                                }
                            }
                        });
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
