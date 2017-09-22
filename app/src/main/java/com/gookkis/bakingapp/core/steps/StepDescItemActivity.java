package com.gookkis.bakingapp.core.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.utils.Const;
import com.gookkis.bakingapp.utils.Helpers;
import com.gookkis.bakingapp.utils.StepsPosition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.gookkis.bakingapp.utils.Const.STEP_POS;


public class StepDescItemActivity extends AppCompatActivity {

    private int stepPosition = 0;
    StepsDescItemFragment stepsDescItemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_details);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(STEP_POS)) {
            stepPosition = intent.getIntExtra(Const.STEP_POS, 0);
        }

        if (savedInstanceState != null) {
            stepPosition = savedInstanceState.getInt(Const.STEP_POS);
        }
        stepsDescItemFragment = StepsDescItemFragment.newInstance(stepPosition);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_steps_detail, stepsDescItemFragment).commit();

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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stepPosition = savedInstanceState.getInt(Const.STEP_POS);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Const.STEP_POS, stepPosition);
    }
}
