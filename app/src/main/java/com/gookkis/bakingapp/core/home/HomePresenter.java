package com.gookkis.bakingapp.core.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gookkis.bakingapp.core.recipe.RecipeActivity;
import com.gookkis.bakingapp.model.Recipe;
import com.gookkis.bakingapp.network.NetworkClient;
import com.gookkis.bakingapp.utils.Const;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by herikiswanto on 8/22/17.
 */
public class HomePresenter {
    private final HomeView view;
    private CompositeDisposable mCompositeDisposable;

    public HomePresenter(HomeView view) {
        this.view = view;
    }

    public void getRecipesList() {
        view.showWait();
        if (mCompositeDisposable == null)
            mCompositeDisposable = new CompositeDisposable();

        mCompositeDisposable.add(NetworkClient
                .getRequestAPI().getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

                .subscribe(
                        recipes -> handleRecipeSuccses(recipes),
                        throwable -> handleRecipeError(throwable)
                )
        );
    }

    private void handleRecipeSuccses(ArrayList<Recipe> recipes) {
        view.removeWait();
        view.getRecipesListSuccess(recipes);
    }

    private void handleRecipeError(Throwable throwable) {
        view.removeWait();
        view.onFailure(throwable.toString());
    }

    public void destroyData() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    public void passToRecipeAct(Context context, Recipe item) {
        Intent intent = new Intent(context, RecipeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.RECIPE, item);
        intent.putExtras(bundle);
        view.moveToRecipeAct(intent);
    }
}
