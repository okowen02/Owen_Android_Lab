package algonquin.cst2335.aust0076;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(withId(R.id.editTextPassword));
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.startView));
        textView.check(matches(withText("You shall not pass!")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /**
     * This function checks if, when an upper case letter is missing from the password, the correct
     *      response is displayed.
     */
    @Test
    public void findMissingUpperCase() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editTextPassword));
        appCompatEditText.perform(replaceText("password123#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.startView));
        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * This function checks if, when a lower case letter is missing from the password, the correct
     *      response is displayed.
     */
    @Test
    public void findMissingLowerCase() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editTextPassword));
        appCompatEditText.perform(replaceText("PASSWORD123#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.startView));
        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * This function checks if, when a special character is missing from the password, the correct
     *      response is displayed.
     */
    @Test
    public void findMissingSpecialCase() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editTextPassword));
        appCompatEditText.perform(replaceText("Password123"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.startView));
        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * This function checks if, when a number is missing from the password, the correct
     *      response is displayed.
     */
    @Test
    public void findMissingNumberCase() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editTextPassword));
        appCompatEditText.perform(replaceText("Password#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.startView));
        textView.check(matches(withText("You shall not pass!")));

    }

    /**
     * This function checks if, when the password is correct response is displayed.
     */
    @Test
    public void finishedPasswordCase() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editTextPassword));
        appCompatEditText.perform(replaceText("Password123#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.startView));
        textView.check(matches(withText("Your password meets the requirements")));

    }

}
