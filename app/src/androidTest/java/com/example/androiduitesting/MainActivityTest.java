package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testAddCity() {
        // Click on Add City Button
        onView(withId(R.id.button_add)).perform(click());

        // Type "Edmonton" in the editText
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("Edmonton"));

        // Click on Confirm
        onView(withId(R.id.button_confirm)).perform(click());

        // Check if text "Edmonton" is matched with any of the text
        // displayed on screen
        onView(withText("Edmonton")).check(matches(isDisplayed()));
    }

    @Test
    public void testClearCity() {
        // Add first city to the list
        onView(withId(R.id.button_add)).perform(click());

        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("Edmonton"));
        onView(withId(R.id.button_confirm)).perform(click());

        // Add another city to the list
        onView(withId(R.id.button_add)).perform(click());

        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("Vancouver"));
        onView(withId(R.id.button_confirm)).perform(click());

        // Clear the list
        onView(withId(R.id.button_clear)).perform(click());
        onView(withText("Edmonton")).check(doesNotExist());
        onView(withText("Vancouver")).check(doesNotExist());
    }

    @Test
    public void testListView() {
        // Add a city
        onView(withId(R.id.button_add)).perform(click());

        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("Edmonton"));
        onView(withId(R.id.button_confirm)).perform(click());

        // Check if in the Adapter view (given id of that adapter view),
        // there is a data, (which is an instance of String) located at
        // position zero.
        // If this data matches the text we provided then our test should pass.
        // You can also use anything() in place of is(instanceOf(String.class))

        onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.city_list
        )).atPosition(0).check(matches((withText("Edmonton"))));
    }

    @Test
    public void testNewActivity() throws InterruptedException {
        /* Check if a new activity is open */

        addCity("Edmonton");

        // Click on the city in the listview
        clickCityList(0);

        Thread.sleep(500);

        // Verify if a text field in a new activity is displayed
        onView(withId(R.id.clicked_city)).check(matches(isDisplayed()));
    }

    @Test
    public void testShowCityNameInNewActivity() throws InterruptedException {
        /* Check if the new activity display the correct city */

        addCity("Edmonton");
        addCity("Vancouver");
        addCity("Calgary");

        // Click on Vancouver
        clickCityList(1);

        Thread.sleep(500);

        // Verify if the new activity is displaying Vancouver
        onView(withId(R.id.clicked_city)).check(matches(withText("Vancouver")));
    }

    @Test
    public void testGoBack() throws InterruptedException {
        /* Check if we can go back to the main activity */

        addCity("Edmonton");
        addCity("Vancouver");
        addCity("Calgary");

        clickCityList(2);

        // Sanity check by testing teshShowCityNameInNewActivity again
        onView(withId(R.id.clicked_city)).check(matches(withText("Calgary")));

        Thread.sleep(500);

        onView(withId(R.id.back_button)).perform(click());

        Thread.sleep(500);

        // Check if we are back to the main activity
        onView(withId(R.id.button_add)).check(matches(isDisplayed()));
    }

    private void addCity(String name) {
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText(name));
        onView(withId(R.id.button_confirm)).perform(click());
    }

    /* Click on a city in the city list */
    private void clickCityList(int position) {
        onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.city_list))
                .atPosition(position)
                .perform(click());
    }
}
