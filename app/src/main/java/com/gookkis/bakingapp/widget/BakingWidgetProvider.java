package com.gookkis.bakingapp.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.model.Recipe;
import com.gookkis.bakingapp.network.NetworkClient;
import com.gookkis.bakingapp.utils.Const;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BakingWidgetProvider extends AppWidgetProvider {

    private CompositeDisposable mCompositeDisposable;
    ArrayList<Recipe> recipeArrayList = new ArrayList<Recipe>();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);
            appWidgetManager.updateAppWidget(widgetId, mView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

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


        String sRecipe = new Gson().toJson(recipeArrayList);

        intent.putExtra(Const.SELECTED_RECIPE, sRecipe);
        mView.setRemoteAdapter(widgetId, R.id.list, intent);
        return mView;
    }


    private void handleRecipeError(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void handleRecipeSuccses(ArrayList<Recipe> recipes) {
        recipeArrayList = recipes;
    }
}