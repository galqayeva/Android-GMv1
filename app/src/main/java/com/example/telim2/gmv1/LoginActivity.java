package com.example.telim2.gmv1;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.telim2.gmv1.Adapters.MySingleTon;

public class LoginActivity extends AppCompatActivity {

    TextView tw;
    EditText editTextUsername;
    Button btn;

    private String registerId;
    String url,url2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername=(EditText)findViewById(R.id.etUsername);
        btn=(Button)findViewById(R.id.btnRegister);

        registerId = Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url="https://gunaya.000webhostapp.com/gmv1/insert.php?registerId="+registerId+"&username="+editTextUsername.getText();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),"check your internet connectio",Toast.LENGTH_LONG).show();
                            }
                        }
                );
                MySingleTon.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        });
    }
}
