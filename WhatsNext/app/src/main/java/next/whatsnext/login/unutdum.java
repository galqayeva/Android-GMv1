package next.whatsnext.login;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import next.whatsnext.R;

public class unutdum extends AppCompatActivity {

    EditText unut;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unutdum);
        auth = FirebaseAuth.getInstance();

        unut = (EditText) findViewById(R.id.unutEmail);
    }

    public void onSendE(View v){
        String email = unut.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            unut.setError("Emaili daxil edin!");
            return;
        }


        //noinspection unchecked
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(unutdum.this, "Şifrə Emailinizə Göndərildi!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(unutdum.this, "Bu email bazada yoxdur!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }
}
