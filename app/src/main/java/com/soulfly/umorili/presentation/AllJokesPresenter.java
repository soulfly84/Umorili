package com.soulfly.umorili.presentation;

import android.util.Log;
import android.widget.SearchView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.soulfly.umorili.dataBase.AllJokesEntity;
import com.soulfly.umorili.dataBase.UmoriliDataBase;
import com.soulfly.umorili.rest.RestService;
import com.soulfly.umorili.rest.models.QueryServerModel;
import com.soulfly.umorili.ui.util.ConstansManager;
import com.soulfly.umorili.ui.util.RxSubscryber;
import com.soulfly.umorili.view.AllJokesView;

import org.androidannotations.annotations.EBean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@EBean
@InjectViewState
public class AllJokesPresenter extends MvpPresenter<AllJokesView> {
    private static final String TAG = "AllJokesFragment";
    private Disposable mDisposable;
    private Disposable mDisposable2;

    public AllJokesPresenter() {
    }



    public void loadAllJokes(SearchView searchView) {
        Log.d(TAG, "AllJokesPresenter. loadAllJokes----------");
        Observable<String> loadListObservable;
        if (searchView == null) {
            Log.d(TAG, "searchView не инициализир");
            loadListObservable = RxSubscryber.loadListObservable();
        } else {
            Log.d(TAG, "searchView инициализир " + searchView);
           // loadListObservable = RxSubscryber.loadListBySearchObservable(searchView);
             loadListObservable = Observable.merge(RxSubscryber.loadListObservable(), RxSubscryber.loadListBySearchObservable(searchView));
        }


        mDisposable = loadListObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {

                        getViewState().showProgressBar();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<String, List<AllJokesEntity>>() {
                    @Override
                    public List<AllJokesEntity> apply(String query) {
                        return AllJokesEntity.selectAllSearch(query);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AllJokesEntity>>() {
                    @Override
                    public void accept(List<AllJokesEntity> result) {
                        Log.d(TAG, "AllJokesPresenter. result = " + result);
                        getViewState().showAllJokesList(result);
                        getViewState().hideProgressBar();
                    }
                });
    }


    public void connectionServer() {
        Observable<String> loadListObservable2 = RxSubscryber.loadListObservable();
        getViewState().showProgressBar();

        Log.d(TAG, "connectionServer----------");
        mDisposable2 = loadListObservable2
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        Log.d(TAG, "loadListObservable. doOnNext");
                        getViewState().showProgressBar();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String query) {
                        RestService restService = RestService.getInstance();
                        boolean connected = false;
                        try {
                            List<QueryServerModel> queryServerModel = restService
                                    .queryResponse(ConstansManager.SITE, ConstansManager.NAME, ConstansManager.NUM);
                            insertDataToDB(queryServerModel);
                            connected = true;

                        } catch (IOException e) {
                            e.printStackTrace();
                            connected = false;
                        }

                        return connected;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean connected) {
                        if (connected) {
                            loadAllJokes(null);
                            getViewState().showSucessToast();
                        } else {
                            getViewState().showServerErrorToast();
                        }
                        getViewState().hideProgressBar();
                    }
                });
    }

    private void insertDataToDB(final List<QueryServerModel> givenJokes) {
        Log.d(TAG, "insertDataToDB()");
        FlowManager.getDatabase(UmoriliDataBase.class).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                List<AllJokesEntity> allJokesEntity = AllJokesEntity.selectAll();
                ArrayList<String> existedIdList = new ArrayList<String>();
                for (AllJokesEntity existedJoke : allJokesEntity) {
                    String existedId = existedJoke.getId();
                    existedIdList.add(existedId);

                }

                for (QueryServerModel quote : givenJokes) {
                    String serverId = quote.getLink();
                    String givenId = null;
                    if (serverId != null) {
                        givenId = serverId.substring(serverId.length() - 8, serverId.length());
                    }

                    if (givenId != null && !existedIdList.contains(givenId)) {
                        AllJokesEntity newEntity = new AllJokesEntity(givenId, quote.getAnecdote(), false, getDateTime());
                        newEntity.save(databaseWrapper);

                    }
                }


            }

            ;
        });
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }




    public void clearResurses() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }if (mDisposable2!= null && !mDisposable2.isDisposed()) {
            mDisposable2.dispose();
        }
    }

}
