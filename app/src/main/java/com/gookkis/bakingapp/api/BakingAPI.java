package com.gookkis.bakingapp.api;

import com.gookkis.bakingapp.model.Recipe;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;


/**
 * Created by herikiswanto on 8/22/17.
 */

public interface BakingAPI {

    @GET("android-baking-app-json")
    Observable<ArrayList<Recipe>> getRecipes();

}
