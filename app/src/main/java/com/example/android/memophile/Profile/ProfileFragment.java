package com.example.android.memophile.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.memophile.R;
import com.example.android.memophile.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aditya314 on 3/11/2018.
 */

public class ProfileFragment extends Fragment {

    private static final int ACTIVITY_NUM = 4;

    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;

    private Context mContext;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mDisplayName =  view.findViewById(R.id.display_name);
        mUsername =  view.findViewById(R.id.username);
        mWebsite = view.findViewById(R.id.website);
        mDescription =  view.findViewById(R.id.description);
        mProfilePhoto =  view.findViewById(R.id.profile_photo);
        mPosts =  view.findViewById(R.id.tvPosts);
        mFollowers =  view.findViewById(R.id.tvFollowers);
        mFollowing =  view.findViewById(R.id.tvFollowing);
        mProgressBar =  view.findViewById(R.id.profileProgressBar);
        gridView =  view.findViewById(R.id.gridView);
        toolbar =  view.findViewById(R.id.profileToolBar);
        profileMenu =  view.findViewById(R.id.profileMenu);
        bottomNavigationView =  view.findViewById(R.id.bottomNavViewBar);
        mContext = getActivity();

        setupBottomNavigationView();
        setupToolbar();

        return view;
    }


    /**
     * Responsible for setting up the profile toolbar
     */
    private void setupToolbar(){

        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
