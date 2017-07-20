package com.soulfly.umorili.ui;


import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.soulfly.umorili.R;
import com.soulfly.umorili.ui.fragments.AllJokesFragment_;
import com.soulfly.umorili.ui.fragments.FavoritesFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;


@EActivity
public class MainActivity extends MvpAppCompatActivity
        implements FragmentManager.OnBackStackChangedListener {
    private static final String TAG = "MainLog";
    private static FragmentManager fragmentManager;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @StringRes(R.string.item_menu_all_jokes)
    String allJokesTitle;
    @StringRes(R.string.item_menu_favorites)
    String favoritesTitle;
    @InstanceState
    String toolbarTitle;

    MenuItem item_favorites;

    static ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            Fragment currentFragment = new AllJokesFragment_();
            replaceFragment(currentFragment);

        }



        actionBar = getSupportActionBar();

        ActivityCompat.invalidateOptionsMenu(this);
    }


    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        item_favorites = menu.findItem(R.id.item_favorites);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.item_favorites:
                replaceFragment(new FavoritesFragment_());
                //item_favorites.setVisible(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume----");
        super.onResume();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.main_container);
        if (currentFragment != null) {
            setToolbarTitle(currentFragment.getClass().getName());
        }
    }

    public void onBackStackChanged() {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.main_container);
        if (currentFragment != null) {
            setToolbarTitle(currentFragment.getClass().getName());
        }
    }


    public static void replaceFragment(Fragment fragment) {
        Log.d(TAG, "replaceFragment----");
        String backStackName = fragment.getClass().getName();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);

        if (!isFragmentPopped && fragmentManager.findFragmentByTag(backStackName) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment, backStackName);
            transaction.addToBackStack(backStackName);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();

        }
    }

    private void setToolbarTitle(String fragmName) {
        Log.d(TAG, "setToolbarTitle----");
        if (fragmName.equals(AllJokesFragment_.class.getName())) {
            setTitle(allJokesTitle);
            toolbarTitle = allJokesTitle;
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
            if (item_favorites != null) {
                item_favorites.setVisible(true);
            }
        } else if (fragmName.equals(FavoritesFragment_.class.getName())) {
            setTitle(favoritesTitle);
            toolbarTitle = favoritesTitle;

            if (item_favorites != null) {
                item_favorites.setVisible(false);
            }
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

        }
    }

}
