package com.gookkis.bakingapp.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.model.Recipe;
import com.gookkis.bakingapp.network.NetworkClient;
import com.gookkis.bakingapp.utils.Const;
import com.gookkis.bakingapp.utils.Helpers;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BakingWidgetConfigurationActivity extends AppCompatActivity {

    @BindView(R.id.rv_recipes)
    RecyclerView rvRecipes;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private int mAppWidgetId;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        setContentView(R.layout.widget_receipe_chooser);
        ButterKnife.bind(this);

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

    private void handleRecipeError(Throwable throwable) {
        Helpers.showToast(this, throwable.getMessage());
    }

    private void handleRecipeSuccses(ArrayList<Recipe> recipes) {
        rvRecipes.setAdapter(new RecyclerViewWidgetAdapter(recipes));
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void execute(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = initViews(this, appWidgetManager, mAppWidgetId, recipe);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent(this, BakingWidgetService.class);
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        setResult(RESULT_OK, resultValue);

        finish();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId, Recipe recipes) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        String recipe = new GsonBuilder().create().toJson(recipes);

        intent.putExtra(Const.SELECTED_RECIPE, recipe);
        mView.setRemoteAdapter(widgetId, R.id.list, intent);

        return mView;
    }


    class RecyclerViewWidgetAdapter extends RecyclerView.Adapter<RecyclerViewWidgetAdapter.WidgetAdapter> {

        private Context context;
        List<Recipe> recipes = new ArrayList<>();

        public RecyclerViewWidgetAdapter(List<Recipe> recipes) {
            this.recipes = recipes;
        }

        @Override
        public WidgetAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            WidgetAdapter widgetAdapter = new WidgetAdapter(view);
            return widgetAdapter;
        }

        @Override
        public void onBindViewHolder(WidgetAdapter holder, int position) {
            final Recipe recipe = recipes.get(position);
            holder.tv.setText(recipe.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    execute(recipe);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (recipes != null)
                return recipes.size();
            else return 0;
        }

        class WidgetAdapter extends RecyclerView.ViewHolder {
            @BindView(android.R.id.text1)
            TextView tv;

            public WidgetAdapter(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }
}
