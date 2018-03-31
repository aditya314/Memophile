package com.example.android.memophile.Upload;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.memophile.R;
import com.example.android.memophile.Utils.Permissions;
import com.example.android.memophile.Utils.SectionsPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.example.android.memophile.Utils.BottomNavigationViewHelper;


/**
 * Created by aditya314 on 2/3/2018.
 */

public class UploadActivity extends AppCompatActivity{

    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    private Context mContext = UploadActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            setupViewPager();
        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }

        //setupBottomNavigationView();
    }

    private void setupViewPager(){
        SectionsPagerAdapter adapter =  new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
    }

    public int getTask(){
        return getIntent().getFlags();
    }

    public void verifyPermissions(String[] permissions){

        ActivityCompat.requestPermissions(
                UploadActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /**
     * Check an array of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionsArray(String[] permissions){

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * Check a single permission is it has been verified
     * @param permission
     * @return
     */
    public boolean checkPermissions(String permission){

        int permissionRequest = ActivityCompat.checkSelfPermission(UploadActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
