package kr.ac.hansung.a3scalendar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    FragmentManager fm;
    Fragment fragment;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    ToggleButton MWTBtn;

    public static MainActivity Root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Root = this;




        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.container);
        if(fragment==null){
            fragment = new LoginFragment().newInstance();
            fm.beginTransaction().replace(R.id.container,fragment).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MWTBtn =(ToggleButton)findViewById(R.id.MWToggleBtn);
        MWTBtn.setVisibility(View.INVISIBLE);
        MWTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MWTBtn.isChecked()){
                   Fragment weekFragment = new WeekCalendarFragment().newInstance();
                  /* // FragmentManager fm = getFragmentManager();
                   FragmentTransaction fragmentTransaction = fm.beginTransaction();
                   fragmentTransaction.replace(R.id.container,fragment);
                   fragmentTransaction.commit();*/
                    fm.beginTransaction().replace(R.id.container,weekFragment).commit();

                }else{
                   fm.beginTransaction().replace(R.id.container,fragment).commit();
                    ///((MainActivity)getApplicationContext()).ChangeFragment(R.id.calendars);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else  if (mOnKeyBackPressedListener != null) { //백키 리스너 있을 경우
            mOnKeyBackPressedListener.onBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
        fragment.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        ChangeFragment(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ChangeFragment(int id){
        Fragment cf = null;
        switch (id){
            case R.id.calendars:
                cf = new MonthCalendarFragment().newInstance();
                Log.d("ddd","Calendars");
                break;
            case R.id.Weekcalendar:
                cf = new WeekCalendarFragment().newInstance();
                Log.d("ddd","WeekCalendar");

                break;
            case R.id.Community:
                cf = new FriendGroupFragment().newInstance();
                Log.d("ddd","WeekFragment");
                break;
            case R.id.Settings:
                cf = new SettingFragment().newInstance();
                Log.d("ddd","DateFragment");
                break;
            case R.id.Logout:
                cf = new LoginFragment().newInstance();
                //로그인 창으로 이동하게 만들기
                Log.d("ddd","Logout");
                break;
            case R.id.alarmSetting:
                cf = new AlarmSettingFragment().newInstance();
                Log.d("ddd","AlarmSettingFragment");
                break;
            case R.id.myCalendar:
                cf = new MyCalendarSettingFragment().newInstance();
                Log.d("ddd","MyCalendarSettingFragment");
                break;
            case R.id.otherCalendar:
                cf = new OtherCalendarSettingFragment().newInstance();
                Log.d("ddd","OtherCalendarSettingFragment");
                break;
        }
        if(cf!=null) {
            Log.d("ddd","setfm");
            fragment = cf;
            fm.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void ChangeActivity(View v){
        Intent intent = null;
        switch (v.getId()){
            default:
            //case R.id.alarmSetting:
                //intent = new Intent(MainActivity.this,MainActivity.class);
             //   break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }

    //프래그먼트 백키 관련
    public interface onKeyBackPressedListener {
        public void onBack();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
        Log.i("backKey","setOnKeyBackPressedListener");
    }
}
