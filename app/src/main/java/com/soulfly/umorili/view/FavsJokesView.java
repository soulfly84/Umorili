package com.soulfly.umorili.view;


import com.arellomobile.mvp.MvpView;
import com.soulfly.umorili.dataBase.AllJokesEntity;

import java.util.List;

public interface FavsJokesView extends MvpView {
    void showFavsJokesList(List<AllJokesEntity> favsList);
}
