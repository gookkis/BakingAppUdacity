package com.gookkis.bakingapp.network;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import retrofit2.HttpException;
import timber.log.Timber;


public abstract class NetworkCallback<M> implements Observer<M> {
    private static final String TAG = NetworkCallback.class.getName();

    public abstract void onSuccess(M model);

    public abstract void onFailure(String message);

    public abstract void onFinish();

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            String message = httpException.getMessage();
            Timber.d(TAG + " onError: " + message);
            Timber.d(TAG + " onError: " + code);
            onFailure(message);
        }
    }

    @Override
    public void onNext(@NonNull M model) {
        onSuccess(model);
    }

    @Override
    public void onComplete() {
        onFinish();
    }
}
