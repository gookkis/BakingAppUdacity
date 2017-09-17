package com.gookkis.bakingapp.core.recipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.model.Recipe;
import com.gookkis.bakingapp.model.Step;
import com.gookkis.bakingapp.utils.Const;
import com.gookkis.bakingapp.utils.Helpers;
import com.gookkis.bakingapp.utils.StepsPosition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import timber.log.Timber;


public class RecipeActivity extends AppCompatActivity
        implements RecipeView, RecipeDetailFragment.OnFragmentInteractionListener {

    private String jsonReceipe;
    private boolean isMultiPane;
    private ViewPager viewPager;
    private StepsDetailPagerAdapter stepsDetailPagerAdapter;
    private Recipe mRecipe = null;
    private RecipePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        presenter = new RecipePresenter(this);
        presenter.parsingRecipe(this.getIntent());


        if (findViewById(R.id.view_pager) != null) {
            isMultiPane = true;
            initMultiPane(savedInstanceState, mRecipe);
        } else {
            isMultiPane = false;
            init(savedInstanceState);
        }
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            RecipeDetailFragment rdf = RecipeDetailFragment.newInstance(jsonReceipe, isMultiPane);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, rdf);
            ft.commit();
        }
    }

    private void initMultiPane(Bundle savedInstanceState, Recipe mRecipe) {
        init(savedInstanceState);
        initViewPager(savedInstanceState, mRecipe.getSteps());
    }

    private void initViewPager(Bundle savedInstanceState, ArrayList<Step> steps) {
        if (savedInstanceState == null) {
            viewPager = findViewById(R.id.view_pager);
            stepsDetailPagerAdapter = new StepsDetailPagerAdapter(getSupportFragmentManager(), this, steps);
            viewPager.setAdapter(stepsDetailPagerAdapter);
        }
    }

    @Override
    public void onFragmentMasterSelected(int position) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StepsPosition selectPos) {
        viewPager.setCurrentItem(selectPos.getPos());
    }

    @Override
    public void parsingIntent(Intent intent) {
        SharedPreferences prefRecipe = getPreferences(MODE_PRIVATE);
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            mRecipe = b.getParcelable(Const.RECIPE);
            SharedPreferences.Editor prefsEditor = prefRecipe.edit();
            jsonReceipe = new Gson().toJson(mRecipe);
            prefsEditor.putString(Const.RECIPE, jsonReceipe);
            prefsEditor.apply();
        } else {
            String json = prefRecipe.getString(Const.RECIPE, "");
            mRecipe = new Gson().fromJson(json, Recipe.class);
        }
    }

    @Override
    public void onFailure(String appErrorMessage) {
        Helpers.showToast(getBaseContext(), appErrorMessage);
        Timber.d("onFailure" + appErrorMessage);
    }

}
