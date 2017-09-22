package com.gookkis.bakingapp.core.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.utils.Const;
import com.gookkis.bakingapp.utils.Helpers;

import static com.gookkis.bakingapp.utils.Const.STEP_POS;


public class StepDescItemActivity extends AppCompatActivity {

    private int stepPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_details);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(STEP_POS)) {
            stepPosition = intent.getIntExtra(Const.STEP_POS, 0);
        }
        Helpers.updatePosition(stepPosition);
        StepsDescItemFragment stepsDescItemFragment = StepsDescItemFragment.newInstance(stepPosition);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_steps_detail, stepsDescItemFragment).commit();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stepPosition = savedInstanceState.getInt(Const.STEP_POS);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Const.STEP_POS, stepPosition);
    }
}
