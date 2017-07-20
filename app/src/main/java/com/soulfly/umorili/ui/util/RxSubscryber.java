package com.soulfly.umorili.ui.util;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.SearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class RxSubscryber {
    private static final String TAG = "MarksFragmentLog";

    public static Observable<String> loadListBySearchObservable(@NonNull final SearchView searchView) {
        Observable<String> textChangeObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        emitter.onComplete();
                        Log.d(TAG, "onQueryTextSubmit. onComplete emitter= " + query);

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        Log.d(TAG, "setQuery. searchView " + searchView);
                        emitter.onNext(query);
                        Log.d(TAG, "onQueryTextChange. onNext query= " + query);
                        return true;
                    }
                });


            }
        });

        return textChangeObservable
                .debounce(1000, TimeUnit.MILLISECONDS);//задержка 1 сек после последней набранной буквы

    }


    public static Observable<String> loadListObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("");
            }
        });
    }
}