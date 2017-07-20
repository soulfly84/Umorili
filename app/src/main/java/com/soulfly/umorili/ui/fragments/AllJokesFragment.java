package com.soulfly.umorili.ui.fragments;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.soulfly.umorili.R;
import com.soulfly.umorili.dataBase.AllJokesEntity;
import com.soulfly.umorili.presentation.AllJokesPresenter;
import com.soulfly.umorili.rest.NetworkStatusChecker;
import com.soulfly.umorili.ui.adapters.AdapterAllJokes;
import com.soulfly.umorili.view.AllJokesView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.all_jokes_fragment)
@OptionsMenu(R.menu.jokes_fragment_menu)
public class AllJokesFragment extends MvpFragment implements SwipeRefreshLayout.OnRefreshListener, AllJokesView {
    private static final String TAG = "AllJokesFragment";

    private AdapterAllJokes adapterAllJokes;

    SearchView searchView;

    @ViewById(R.id.all_jokes_fragment)
    CoordinatorLayout rootLayout;

    @ViewById(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @ViewById(R.id.list_of_all_jokes)
    RecyclerView recyclerView;

    @ViewById(R.id.nothingFoundTV)
    TextView nothingFoundTV;

    private String savedQuery = null;


    @Bean
    @InjectPresenter
    AllJokesPresenter presenter;


    @AfterViews
    void ready() {
        Log.d(TAG, "AfterViews---");

        setHasOptionsMenu(true);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated---");
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            savedQuery = savedInstanceState.getString("savedQuery");
            Log.d(TAG, "onActivityCreated. Восстанавливаю savedQuery = " + savedQuery);
        } else {
            savedQuery = null;
            loadAllJokes();

            mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(this);


    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        if (searchView != null) {
            savedQuery = searchView.getQuery().toString();
        }
        if (savedQuery != null) {
            outState.putString("savedQuery", savedQuery.toString());

        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        final MenuItem searchItem = menu.findItem(R.id.search_action);
        final MenuItem refresh_list = menu.findItem(R.id.refresh_list);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint(getString(R.string.search_title));
        if (!TextUtils.isEmpty(savedQuery) && savedQuery != null) {
            searchView.post(new Runnable() {
                @Override
                public void run() {
                    MenuItemCompat.expandActionView(searchItem);
                    searchView.onActionViewExpanded();
                    searchView.setQuery(savedQuery, true);
                }
            });

            refresh_list.setVisible(false);
        }

        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // refresh_list.setVisible(false);
                Log.d(TAG, "onCreateOptionsMenu OnSearchClickListener" );
                loadAllJokes();
                // setItemsVisibility(menu, searchItem, false);

            }
        });
        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                Log.d(TAG, "onCreateOptionsMenu OnCloseListener" );
                // setItemsVisibility(menu, searchItem, true);
                //refresh_list.setVisible(true);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_list:
                Log.d(TAG, "onOptionsItemSelected. refresh_list");
                connectToServer();
                break;

            case R.id.search_action:
                Log.d(TAG, "onOptionsItemSelected. search_action");
                break;
        }

        return (super.onOptionsItemSelected(item));
    }

    private boolean isQueryEmpty() {
        String nullQ = null;
        if (searchView != null && !searchView.getQuery().toString().equals("")) {
            nullQ = searchView.getQuery().toString();
            Log.d(TAG, "isQueryEmpty =  " + nullQ);
            return false;

        } else
            return true;
    }


    @Override
    public void onRefresh() {
        if (isQueryEmpty()){
            connectToServer();
        } else
            mSwipeRefreshLayout.setRefreshing(false);
    }

    void connectToServer() {
        if(isQueryEmpty() && savedQuery == null){
            Log.d(TAG, "startConnectionServer()");
            if (NetworkStatusChecker.isNetworkAvailable(getActivity()) ) {

                presenter.connectionServer();

            } else {
                Snackbar.make(rootLayout,
                        getString(R.string.error_no_internet),
                        Snackbar.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

    }


    public void loadAllJokes() {

        if (searchView == null) {
            presenter.loadAllJokes(null);
            //Log.d(TAG, "1. loadAllJokes() searchView " + searchView);
        } else {
            presenter.loadAllJokes(searchView);
            //Log.d(TAG, "2. loadAllJokes() searchView " + searchView);
        }

    }


    @Override
    public void showAllJokesList(List<AllJokesEntity> jokesList) {
        adapterAllJokes = new AdapterAllJokes(getActivity(), jokesList);
        Log.d(TAG, "showAllJokesList jokesList " + jokesList);
        Log.d(TAG, "showAllJokesList adapterAllJokes " + adapterAllJokes.getItemCount());

        if (jokesList.size() == 0 && !isQueryEmpty()) {
            Log.d(TAG, "1. showAllJokesList Ничего не найдено " + searchView.getQuery());
            mSwipeRefreshLayout.setVisibility(View.GONE);
            nothingFoundTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else if (adapterAllJokes.getItemCount() != 0) {
            Log.d(TAG, "2. showAllJokesList Показываю все jokesList " + jokesList);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
            recyclerView.setVisibility(View.VISIBLE);
            nothingFoundTV.setVisibility(View.GONE);

            recyclerView.setAdapter(adapterAllJokes);
        } else{
            Log.d(TAG, "3. showAllJokesList connectToServer" + jokesList);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(true);
            nothingFoundTV.setVisibility(View.GONE);
            connectToServer();
            recyclerView.setVisibility(View.GONE);
        }
        adapterAllJokes.notifyDataSetChanged();
        // adapterAllJokes = null;

    }

    @Override
    public void showProgressBar() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSucessToast() {
        Log.d(TAG, "showSucessToast");
        Snackbar.make(rootLayout,
                getString(R.string.refresh_finished),
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showServerErrorToast() {
        Snackbar.make(rootLayout,
                getString(R.string.unknown_error),
                Snackbar.LENGTH_LONG).show();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.clearResurses();
        }
    }


}