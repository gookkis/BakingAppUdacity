package com.gookkis.bakingapp.ui;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.core.home.HomeActivity;
import com.gookkis.bakingapp.core.home.ProgressIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by herikiswanto on 8/23/17.
 */

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    private IdlingResource mIdlingResource;

    @Rule
    public IntentsTestRule<HomeActivity> mActivityRule
            = new IntentsTestRule<>(HomeActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = new ProgressIdlingResource(mActivityRule.getActivity());
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void testLaunchHomeActivity() {
        intending(allOf(
                hasComponent(hasShortClassName(".HomeActivity"))));
    }

    @Test
    public void testRecyclerView() {
        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyData(){
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
        onView(withText("Brownies")).check(matches(isDisplayed()));
        onView(withText("Yellow Cake")).check(matches(isDisplayed()));
        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }

    @Test
    public void verifyNutellaPie(){
        onView(withText("Nutella Pie")).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_ingredients)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_steps)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
