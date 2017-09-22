package com.gookkis.bakingapp.core.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.core.steps.StepsDescItemFragment;
import com.gookkis.bakingapp.model.Recipe;
import com.gookkis.bakingapp.model.Step;
import com.gookkis.bakingapp.utils.Const;
import com.gookkis.bakingapp.utils.Helpers;
import com.gookkis.bakingapp.utils.StepsPosition;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class RecipeActivity extends AppCompatActivity
        implements RecipeView, RecipeDetailFragment.OnFragmentInteractionListener {

    private String jsonReceipe;
    private boolean isMultiPane;
    private Recipe mRecipe = null;
    private RecipePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        setupAppBar();
        presenter = new RecipePresenter(this);
        presenter.parsingRecipe(this.getIntent());


        if (Helpers.isMultiPane(this)) {
            isMultiPane = true;
            initMultiPane(savedInstanceState, mRecipe);
        } else {
            isMultiPane = false;
            init(savedInstanceState);
        }
    }

    private void setupAppBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            StepsDescItemFragment stepsDescItemFragment = StepsDescItemFragment.newInstance(0);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_steps_detail, stepsDescItemFragment).commit();
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
        Helpers.updatePosition(selectPos.getPos());
        StepsDescItemFragment stepsDescItemFragment = StepsDescItemFragment.newInstance(selectPos.getPos());
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_steps_detail, stepsDescItemFragment).commit();
    }

    @Override
    public void parsingIntent(Intent intent) {
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            mRecipe = b.getParcelable(Const.RECIPE);
            jsonReceipe = new Gson().toJson(mRecipe, Recipe.class);
            Prefs.putString(Const.RECIPE, jsonReceipe);
        } else {
            String json = Prefs.getString(Const.RECIPE, "");
            mRecipe = new Gson().fromJson(json, Recipe.class);
        }
    }

    @Override
    public void onFailure(String appErrorMessage) {
        Helpers.showToast(getBaseContext(), appErrorMessage);
    }

}
