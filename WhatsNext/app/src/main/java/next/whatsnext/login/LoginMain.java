package next.whatsnext.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import next.whatsnext.mainRecycler.MainActivity;
import next.whatsnext.R;

public class LoginMain extends AppCompatActivity {

    ViewPager vp;
    SectionPageAdapter mAdapter;
    TabLayout tabLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                        Intent intent = new Intent(LoginMain.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                }

            }
        };

        mAdapter = new SectionPageAdapter(this.getSupportFragmentManager());

        vp= (ViewPager) findViewById(R.id.viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setUpViewPager(vp);

    }

    private void setUpViewPager(ViewPager viewPager){
        SectionPageAdapter adapter=new SectionPageAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new SignIn(),"SignIn");
        adapter.addFragment(new SignUp(),"SignUp");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(vp);
    }
}
