package com.openclassrooms.entrevoisins.ui.neighbour_details;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.intents.IntentName.INFORMATION_ACTIVITY_INTENT_NAME;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NeighbourDetailsActivityTest {

    private NeighbourApiService mApiService;

    @Rule
    public IntentsTestRule<NeighbourDetailsActivity> mActivityRule =
            new IntentsTestRule<NeighbourDetailsActivity>(NeighbourDetailsActivity.class, true, true) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = ApplicationProvider.getApplicationContext();
                    Intent result = new Intent(targetContext, NeighbourDetailsActivity.class);
                    result.putExtra(INFORMATION_ACTIVITY_INTENT_NAME, 1);
                    return result;
                }
            };

    @Before
    public void setUp() {
        this.mApiService = DI.getNewInstanceApiService();
    }

    /**
     * Test si le nom affiché est bien celui de l'utilisateur passé en paramètre.
     */
    @Test
    public void checkInitNameValue() {
        onView(withId(R.id.title_cardview)).check(matches(withText(mApiService.getNeighbour(1).getName())));
    }

}