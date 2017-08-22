package com.example.telim2.gmv1;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.telim2.gmv1.Adapters.SectionsPageAdapter;
import com.example.telim2.gmv1.fragments.Fragment1;
import com.example.telim2.gmv1.fragments.Fragment2;
import com.example.telim2.gmv1.fragments.Fragment3;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {


    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
