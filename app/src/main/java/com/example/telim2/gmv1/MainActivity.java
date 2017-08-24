package com.example.telim2.gmv1;

import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.telim2.gmv1.Activities.LoginActivity;
import com.example.telim2.gmv1.Utils.MySingleTon;
import com.example.telim2.gmv1.Utils.SectionsPageAdapter;
import com.example.telim2.gmv1.fragments.Fragment1;
import com.example.telim2.gmv1.fragments.Fragment2;
import com.example.telim2.gmv1.fragments.Fragment3;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {


    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    String registerId,url2;

    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        url2="http://172.16.200.200/GMv1/check.php?registerId="+registerId;


        StringRequest stringRequest2=new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("ok")){

                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                                    startActivity(intent);



                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        MySingleTon.getInstance(getApplicationContext()).addToRequestQueue(stringRequest2);


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.restauran);
        tabLayout.getTabAt(1).setIcon(R.drawable.comm);
        tabLayout.getTabAt(2).setIcon(R.drawable.user);

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new Fragment2());
        adapter.addFragment(new Fragment1());
        adapter.addFragment(new Fragment3());
        viewPager.setAdapter(adapter);
    }

}
