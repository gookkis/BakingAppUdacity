package com.gookkis.bakingapp.core.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.model.Recipe;
import com.gookkis.bakingapp.utils.Helpers;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by herikiswanto on 8/22/17.
 */

public class HomeActivity extends AppCompatActivity implements HomeView {

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    private ProgressListener mListener;
    private boolean isMultiPane = false;
    private GridLayoutManager gridLayoutManager;
    private HomePresenter presenter;

    public interface ProgressListener {
        public void onProgressShown();

        public void onProgressDismissed();
    }

    public void setProgressListener(ProgressListener progressListener) {
        mListener = progressListener;
    }

    public boolean isInProgress() {
        // return true if progress is visible
        if (progressBar.isShown()) {
            return true;
        } else {
            return false;
        }
    }

    private void notifyListener(ProgressListener listener) {
        if (listener == null) {
            return;
        }
        if (isInProgress()) {
            listener.onProgressShown();
        } else {
            listener.onProgressDismissed();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderView();
        init();

        presenter = new HomePresenter(this);
        presenter.getRecipesList();
    }

    public void renderView() {
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    public void init() {

        if (Helpers.isMultiPane(this)) {
            gridLayoutManager = new GridLayoutManager(this, 3);
            list.setLayoutManager(gridLayoutManager);
        } else {
            list.setLayoutManager(new LinearLayoutManager(this));
        }

        list.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showWait() {
        progressBar.setVisibility(View.VISIBLE);
        notifyListener(mListener);
    }

    @Override
    public void removeWait() {
        progressBar.setVisibility(View.GONE);
        notifyListener(mListener);
    }

    @Override
    public void onFailure(String appErrorMessage) {
        Helpers.showToast(getBaseContext(), appErrorMessage);
        Timber.d("onFailure" + appErrorMessage);
    }

    @Override
    public void getRecipesListSuccess(ArrayList<Recipe> recipes) {

        HomeAdapter adapter = new HomeAdapter(getApplicationContext(), recipes,
                new HomeAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Recipe item) {
                        presenter.passToRecipeAct(HomeActivity.this, item);
                    }
                });
        list.setAdapter(adapter);

    }

    @Override
    public void moveToRecipeAct(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyData();
    }

}
