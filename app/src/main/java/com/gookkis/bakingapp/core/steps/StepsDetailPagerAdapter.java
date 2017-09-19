package com.gookkis.bakingapp.core.steps;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gookkis.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * Created by herikiswanto on 8/22/17.
 */

public class StepsDetailPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private ArrayList<Step> steps;

    public StepsDetailPagerAdapter(FragmentManager fm, Context context, ArrayList<Step> steps) {
        super(fm);
        this.context = context;
        this.steps = new ArrayList<Step>();
        this.steps.addAll(steps);
    }

    @Override
    public Fragment getItem(int position) {
        Step step = steps.get(position);
        String videoURL = step.getVideoURL();
        String description = step.getDescription();
        return StepsDescItemFragment.newInstance(videoURL, description);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Step step = steps.get(position);
        return step.getShortDescription();
    }

    @Override
    public int getCount() {
        return steps.size();
    }


}
