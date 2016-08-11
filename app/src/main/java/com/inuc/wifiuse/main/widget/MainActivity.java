package com.inuc.wifiuse.main.widget;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.inuc.wifiuse.R;
import com.inuc.wifiuse.beans.Personnel;
import com.inuc.wifiuse.main.presenter.MainPresenter;
import com.inuc.wifiuse.main.presenter.MainPresenterImpl;
import com.inuc.wifiuse.main.view.MainView;
import com.inuc.wifiuse.utils.ActivityCollector;
import com.inuc.wifiuse.utils.GetTimesAndCode;
import com.squareup.picasso.Picasso;



import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 景贝贝 on 2016/6/27.
 */
public class MainActivity extends AppCompatActivity implements MainView {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private MainPresenter mMainPresenter;
    private CircleImageView headIV;
    private TextView nickName;
    private CoordinatorLayout mainContent;
    private SharedPreferences pref;
    private long applicationid = 1;
    private String times;
    private String code;
    private String username;
     private View headerView;
    private Personnel myPersonnel;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActivityCollector.addActivity(this);

        mainContent = (CoordinatorLayout) findViewById(R.id.main_content);

        pref=getSharedPreferences("data",MODE_PRIVATE);
        times= GetTimesAndCode.getTimes();
        code=GetTimesAndCode.getCode(times);
        applicationid=pref.getLong("applicationID",1);
        username=pref.getString("username","");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        headerView=mNavigationView.getHeaderView(0);
        headIV = (CircleImageView) headerView.findViewById(R.id.profile_image);
        nickName = (TextView) headerView.findViewById(R.id.name_tv);
        setupDrawerContent(mNavigationView);

        mMainPresenter = new MainPresenterImpl(this);
        mMainPresenter.loadPersonnel(times,code,applicationid,username);
        switch2News();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mMainPresenter.switchNavigation(menuItem.getItemId());
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void switch2News() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new MainFragment()).commit();
        mToolbar.setTitle(R.string.navigation_main);
    }

    @Override
    public void switch2Images() {
        Bundle bundle=new Bundle();
        bundle.putSerializable("personnel",  myPersonnel);
        SettingFragment settingfragment=new SettingFragment();
        settingfragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,settingfragment ).commit();
        mToolbar.setTitle(R.string.navigation_images);
    }

    @Override
    public void switch2Weather() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new CheckUpdateFragment()).commit();
        mToolbar.setTitle(R.string.navigation_weather);
    }

    @Override
    public void switch2About() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AboutFragment()).commit();
        mToolbar.setTitle(R.string.navigation_about);
    }

    @Override
    public void showLoadFailmsg(String msg) {
        Snackbar.make(mainContent, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void loadPersonnel(Personnel personnel) {
        myPersonnel=personnel;
        Picasso.with(MainActivity.this)
                .load(personnel.getPictureUrl())
                .placeholder(R.drawable.protrait)
                .error(R.drawable.protrait)
                .into(headIV);
        String s=personnel.getPictureUrl();
        nickName.setText(personnel.getNickname());
    }
}
