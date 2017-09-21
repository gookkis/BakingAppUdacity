package com.gookkis.bakingapp.core.recipe;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.core.steps.StepsAdapter;
import com.gookkis.bakingapp.model.Recipe;
import com.gookkis.bakingapp.model.Step;

import java.util.ArrayList;

import static com.gookkis.bakingapp.utils.Const.ISMULTIPANE;
import static com.gookkis.bakingapp.utils.Const.RECIPE;
import static com.gookkis.bakingapp.utils.Const.STEPS_POSITION;

public class RecipeDetailFragment extends Fragment implements StepsAdapter.OnStepAdapterListener {

    private boolean isMultiPane;
    private String jsonReceipe;
    private int stepsPosition = -1;
    private OnFragmentInteractionListener mListener;

    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(String jsonRecipe, boolean isMultiPane) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(RECIPE, jsonRecipe);
        args.putBoolean(ISMULTIPANE, isMultiPane);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEPS_POSITION, stepsPosition);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(RECIPE) && getArguments().containsKey(ISMULTIPANE)) {
            jsonReceipe = getArguments().getString(RECIPE);
            isMultiPane = getArguments().getBoolean(ISMULTIPANE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            stepsPosition = savedInstanceState.getInt(STEPS_POSITION);
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        Recipe mRecipe = new Gson().fromJson(jsonReceipe, Recipe.class);

        TextView tvIngredients = rootView.findViewById(R.id.tv_ingredients);
        String ingredients = "";
        for (int i = 0; i < mRecipe.getIngredients().size(); i++) {
            ingredients = ingredients.concat(mRecipe.getIngredients().get(i).getQuantity() + " " +
                    mRecipe.getIngredients().get(i).getMeasure() + " " +
                    mRecipe.getIngredients().get(i).getIngredient() + "\n");
        }
        tvIngredients.setText(ingredients);

        ArrayList<Step> steps = mRecipe.getSteps();
        RecyclerView rvSteps = rootView.findViewById(R.id.rv_steps);
        StepsAdapter stepsAdapter = new StepsAdapter(getActivity(), steps, isMultiPane, this);
        rvSteps.setAdapter(stepsAdapter);
        rvSteps.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (stepsPosition != -1)
            rvSteps.smoothScrollToPosition(stepsPosition);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStepAdapterSelected(int position) {
        this.stepsPosition = position;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentMasterSelected(int position);
    }
}
