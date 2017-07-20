package com.soulfly.umorili.view;


import com.arellomobile.mvp.MvpView;
import com.soulfly.umorili.dataBase.AllJokesEntity;

import java.util.List;

public interface AllJokesView extends MvpView {
    void showAllJokesList(List<AllJokesEntity> jokesList);
    void loadAllJokes();
    void showProgressBar();
    void hideProgressBar();
    void showSucessToast();
    void showServerErrorToast();

}
