package com.example.campuseetest;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CTestPublisherEventDescription {

    private UiDevice mUiDevice;

    @Before
    public void before() throws Exception {
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testPublisherEventDescription() throws UiObjectNotFoundException {
        ViewInteraction fn = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.sign_in_button),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        fn.perform(click());

        UiObject mText = mUiDevice.findObject(new UiSelector().text("vimanyuawal@gmail.com"));
        mText.click();

        ViewInteraction textView = onView(
                allOf(withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tableLayout1),
                                        1),
                                0)));
        textView.perform(scrollTo(), click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.eventNameField), withText("Event Name= Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ScrollView01),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Event Name= Enter")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.eventLocationField), withText("Event Location= RTH"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ScrollView01),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Event Location= RTH")));
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
}
