package com.gookkis.bakingapp.core.home;

import android.support.test.espresso.IdlingResource;

public class ProgressIdlingResource implements IdlingResource {

    private ResourceCallback resourceCallback;
    private HomeActivity loginActivity;
    private HomeActivity.ProgressListener progressListener;

    public ProgressIdlingResource(HomeActivity activity) {
        loginActivity = activity;

        progressListener = new HomeActivity.ProgressListener() {
            @Override
            public void onProgressShown() {
            }

            @Override
            public void onProgressDismissed() {
                if (resourceCallback == null) {
                    return;
                }
                resourceCallback.onTransitionToIdle();
            }
        };

        loginActivity.setProgressListener(progressListener);
    }

    @Override
    public String getName() {
        return "My idling resource";
    }

    @Override
    public boolean isIdleNow() {
        return !loginActivity.isInProgress();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}