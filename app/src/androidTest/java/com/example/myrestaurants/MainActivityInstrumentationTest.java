package com.example.myrestaurants;

import android.view.View;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.myrestaurants.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void validateEditText() {
        onView(withId(R.id.locationEditText)).perform(typeText("Kigali"))
                .check(matches(withText("Kigali")));
    }

    @Test
    public void locationIsSentToRestaurantsActivity(){
        String location = "Kigali";
        onView(withId(R.id.locationEditText)).perform(typeText(location)).perform(closeSoftKeyboard());
        try {                             // the sleep method requires to be checked and handled so we use try block
            Thread.sleep(250);
        } catch (InterruptedException e){
            System.out.println("got interrupted!");
        }
        onView(withId(R.id.findRestaurantsButton)).perform(click());
        onView(withId(R.id.locationTextView)).check(matches
                (withText("Here are all the restaurants near: " + location)));
    }
    @Test
    public void listItemClickDisplaysToastWithCorrectRestaurant() {
        View activityDecorView = activityTestRule.getActivity().getWindow().getDecorView();
        String restaurantName = "Mi Mero Mole";
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .perform(click());
        onView(withText(restaurantName)).inRoot(withDecorView(not(activityDecorView)))
                .check(matches(withText(restaurantName)));
    }
}
