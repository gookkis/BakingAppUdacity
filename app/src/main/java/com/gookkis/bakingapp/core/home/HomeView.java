package com.gookkis.bakingapp.core.home;

import android.content.Intent;

import com.gookkis.bakingapp.model.Recipe;

import java.util.ArrayList;

/**
 * Created by herikiswanto on 8/22/17.
 */

public interface HomeView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void getRecipesListSuccess(ArrayList<Recipe> recipes);

    void moveToRecipeAct(Intent intent);
}
