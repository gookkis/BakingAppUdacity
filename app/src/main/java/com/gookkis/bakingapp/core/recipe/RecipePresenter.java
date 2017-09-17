package com.gookkis.bakingapp.core.recipe;


import android.content.Intent;


/**
 * Created by herikiswanto on 8/22/17.
 */
public class RecipePresenter {
    private final RecipeView view;

    public RecipePresenter(RecipeView view) {
        this.view = view;
    }

    public void parsingRecipe(Intent intent) {
        view.parsingIntent(intent);
    }

    public void onFailure(String appErrorMessage) {
        view.onFailure(appErrorMessage);
    }
}
