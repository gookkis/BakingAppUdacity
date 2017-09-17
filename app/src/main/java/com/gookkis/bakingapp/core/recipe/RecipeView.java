package com.gookkis.bakingapp.core.recipe;

import android.content.Intent;

/**
 * Created by herikiswanto on 8/22/17.
 */

public interface RecipeView {
    void parsingIntent(Intent intent);

    void onFailure(String appErrorMessage);

}
