package com.gookkis.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.gookkis.bakingapp.model.Ingredient;
import com.gookkis.bakingapp.model.Recipe;
import com.gookkis.bakingapp.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class BakingDataProvider implements RemoteViewsService.RemoteViewsFactory {


    private Context context;
    private Intent intent;
    private List<Ingredient> ingredientList = new ArrayList<>();

    public BakingDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    void initData() {
        String sRecipe = intent.getStringExtra(Const.SELECTED_RECIPE);
        Recipe recipe = new Gson().fromJson(sRecipe, Recipe.class);
        ingredientList.addAll(recipe.getIngredients());
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredientList.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
        views.setTextViewText(android.R.id.text1, ingredient.getQuantity() + " " +
                ingredient.getMeasure() + " " +
                ingredient.getIngredient());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
