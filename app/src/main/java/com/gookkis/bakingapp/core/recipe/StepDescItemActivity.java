package com.gookkis.bakingapp.core.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.model.Step;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.gookkis.bakingapp.utils.Const.STEPS;


public class StepDescItemActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private StepsDetailPagerAdapter adapter;

    private ArrayList<Step> steps = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_details);
        viewPager = findViewById(R.id.view_pager);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(STEPS)) {
            String ssteps = intent.getStringExtra(STEPS);
            Type type = new TypeToken<ArrayList<Step>>() {
            }.getType();
            steps.clear();
            steps = new Gson().fromJson(ssteps, type);
        }

        adapter = new StepsDetailPagerAdapter(getSupportFragmentManager(), this, steps);
        viewPager.setAdapter(adapter);
    }
}
