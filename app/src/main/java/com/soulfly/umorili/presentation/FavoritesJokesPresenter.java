package com.soulfly.umorili.presentation;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.soulfly.umorili.dataBase.AllJokesEntity;
import com.soulfly.umorili.view.FavsJokesView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
@InjectViewState
public class FavoritesJokesPresenter extends MvpPresenter<FavsJokesView> {
    private static final String TAG = "MainLog";

    public FavoritesJokesPresenter() {    }

    @Background
    public void loadFavsJokes() {
        List<AllJokesEntity> jokesList = AllJokesEntity.selectFavorite();
        getViewState().showFavsJokesList(jokesList);

    }
}
