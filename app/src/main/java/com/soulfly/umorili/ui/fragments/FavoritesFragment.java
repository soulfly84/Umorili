package com.soulfly.umorili.ui.fragments;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.soulfly.umorili.R;
import com.soulfly.umorili.dataBase.AllJokesEntity;
import com.soulfly.umorili.presentation.FavoritesJokesPresenter;
import com.soulfly.umorili.ui.MainActivity;
import com.soulfly.umorili.ui.adapters.FavoritesAdapter;
import com.soulfly.umorili.ui.adapters.OnClickRemove;
import com.soulfly.umorili.view.FavsJokesView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.favorites_fragment)
public class FavoritesFragment extends MvpFragment implements FavsJokesView, OnClickRemove {
    private static final String TAG = "FavoritesFragment";
    @ViewById(R.id.favorites_fragment_root_layout)
    CoordinatorLayout rootLayout;
    @ViewById(R.id.list_of_all_favorites)
    RecyclerView recyclerView;
    @ViewById(R.id.emptyFavs)
    LinearLayout emptyFavs;
    @Bean
    @InjectPresenter
    FavoritesJokesPresenter presenter;
    List<AllJokesEntity> favsList;



    @AfterViews
    void LinearLayoutManager() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
        presenter.loadFavsJokes();
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.item_menu_favorites);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "TextsFragment. onOptionsItemSelected");
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                MainActivity.replaceFragment(new AllJokesFragment_());
                break;
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadFavsJokes();
    }

    @Override
    public void showFavsJokesList(List<AllJokesEntity> mFavsList) {
        Log.d(TAG, "showFavsJokesList. mFavsList = " + mFavsList);
        favsList = mFavsList;
        FavoritesAdapter mAdapter = new FavoritesAdapter(favsList, this);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showEmptyFavsCallback();
    }

    @Override
    public void showEmptyFavsCallback() {
        Log.d(TAG, "showEmptyFavsCallback. favsList = " + favsList);
        if (favsList != null && favsList.size() == 0 || favsList == null) {
            emptyFavs.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        } else  {
            emptyFavs.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }



}
