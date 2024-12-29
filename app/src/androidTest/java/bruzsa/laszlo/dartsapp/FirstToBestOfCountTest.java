package bruzsa.laszlo.dartsapp;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
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
public class FirstToBestOfCountTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void setLegCountTest() {
        ViewInteraction chip = onView(
                allOf(withId(R.id.chip_x_01), withText("X01"),
                        childAtPosition(
                                allOf(withId(R.id.chipGroupGameTypes),
                                        childAtPosition(
                                                withId(R.id.layoutModes),
                                                0)),
                                0),
                        isDisplayed()));
        chip.perform(click());

        ViewInteraction chip2 = onView(
                allOf(withId(R.id.chipStartScore), withText("501"),
                        childAtPosition(
                                allOf(withId(R.id.chipGroupX01Settings),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        chip2.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.select_dialog_listview),
                        childAtPosition(
                                withId(com.google.android.material.R.id.contentPanel),
                                0)))
                .atPosition(0);
        materialTextView.perform(click());

        ViewInteraction chip3 = onView(
                allOf(withId(R.id.chipFirstTo), withText("first to 1"),
                        childAtPosition(
                                allOf(withId(R.id.chipGroupX01Settings),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        chip3.perform(click());

        DataInteraction materialTextView2 = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.select_dialog_listview),
                        childAtPosition(
                                withId(com.google.android.material.R.id.contentPanel),
                                0)))
                .atPosition(1);
        materialTextView2.perform(click());

        ViewInteraction chip4 = onView(
                allOf(withId(R.id.chipMatchType), withText("LEG"),
                        childAtPosition(
                                allOf(withId(R.id.chipGroupX01Settings),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        chip4.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.button_start_game), withText("Start Game!"),
                        childAtPosition(
                                allOf(withId(R.id.baseLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment_content_main),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction chip5 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip5.perform(click());

        ViewInteraction chip6 = onView(
                allOf(withText("  8  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip6.perform(click());

        ViewInteraction chip7 = onView(
                allOf(withText("  0  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip7.perform(click());

        ViewInteraction chip8 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip8.perform(click());

        ViewInteraction chip9 = onView(
                allOf(withText("  5  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip9.perform(click());

        ViewInteraction chip10 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip10.perform(click());

        ViewInteraction chip11 = onView(
                allOf(withText("  2  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip11.perform(click());

        ViewInteraction chip12 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip12.perform(click());

        ViewInteraction chip13 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip13.perform(click());

        DataInteraction materialTextView3 = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.select_dialog_listview),
                        childAtPosition(
                                withId(com.google.android.material.R.id.contentPanel),
                                0)))
                .atPosition(0);
        materialTextView3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textLegPlayer1), withText("2"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView.check(matches(withText("1")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textLegPlayer2), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView2.check(matches(withText("0")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.textSetPlayer1), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView3.check(matches(withText("0")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.textSetPlayer2), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView4.check(matches(withText("0")));

        ViewInteraction chip14 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip14.perform(click());

        ViewInteraction chip15 = onView(
                allOf(withText("  8  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip15.perform(click());

        ViewInteraction chip16 = onView(
                allOf(withText("  0  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip16.perform(click());

        ViewInteraction chip17 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip17.perform(click());

        ViewInteraction chip18 = onView(
                allOf(withText("  8  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip18.perform(click());

        ViewInteraction chip19 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip19.perform(click());

        ViewInteraction chip20 = onView(
                allOf(withText("  2  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip20.perform(click());

        ViewInteraction chip21 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip21.perform(click());

        ViewInteraction chip22 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip22.perform(click());

        DataInteraction materialTextView4 = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.select_dialog_listview),
                        childAtPosition(
                                withId(com.google.android.material.R.id.contentPanel),
                                0)))
                .atPosition(1);
        materialTextView4.perform(click());

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.textLegPlayer2), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView10.check(matches(withText("1")));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.textLegPlayer1), withText("2"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView11.check(matches(withText("1")));

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.textSetPlayer1), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView12.check(matches(withText("0")));

        ViewInteraction textView13 = onView(
                allOf(withId(R.id.textSetPlayer2), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView13.check(matches(withText("0")));

        ViewInteraction chip23 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip23.perform(click());

        ViewInteraction chip24 = onView(
                allOf(withText("  8  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip24.perform(click());

        ViewInteraction chip25 = onView(
                allOf(withText("  0  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip25.perform(click());

        ViewInteraction chip26 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip26.perform(click());

        ViewInteraction chip27 = onView(
                allOf(withText("  2  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip27.perform(click());

        ViewInteraction chip28 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip28.perform(click());

        ViewInteraction chip29 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip29.perform(click());

        ViewInteraction chip30 = onView(
                allOf(withText("  2  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip30.perform(click());

        ViewInteraction chip31 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip31.perform(click());

        ViewInteraction chip32 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip32.perform(click());

        DataInteraction materialTextView5 = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.select_dialog_listview),
                        childAtPosition(
                                withId(com.google.android.material.R.id.contentPanel),
                                0)))
                .atPosition(0);
        materialTextView5.perform(click());

        ViewInteraction textView15 = onView(
                allOf(withId(R.id.textLegPlayer1), withText("2"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView15.check(matches(withText("2")));

        ViewInteraction textView16 = onView(
                allOf(withId(R.id.textLegPlayer2), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView16.check(matches(withText("1")));

        ViewInteraction textView17 = onView(
                allOf(withId(R.id.textSetPlayer1), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView17.check(matches(withText("0")));

        ViewInteraction textView18 = onView(
                allOf(withId(R.id.textSetPlayer2), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView18.check(matches(withText("0")));

        ViewInteraction chip33 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip33.perform(click());

        ViewInteraction chip34 = onView(
                allOf(withText("  2  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip34.perform(click());

        ViewInteraction chip35 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip35.perform(click());

        ViewInteraction chip36 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip36.perform(click());

        ViewInteraction chip37 = onView(
                allOf(withText("  8  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip37.perform(click());

        ViewInteraction chip38 = onView(
                allOf(withText("  0  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip38.perform(click());

        ViewInteraction chip39 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip39.perform(click());

        ViewInteraction chip40 = onView(
                allOf(withText("  2  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip40.perform(click());

        ViewInteraction chip41 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip41.perform(click());

        ViewInteraction chip42 = onView(
                allOf(withText("  2  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        1),
                                0),
                        isDisplayed()));
        chip42.perform(click());

        ViewInteraction chip43 = onView(
                allOf(withText("  1  "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip43.perform(click());

        ViewInteraction chip44 = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        0),
                                0),
                        isDisplayed()));
        chip44.perform(click());

        DataInteraction materialTextView6 = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.select_dialog_listview),
                        childAtPosition(
                                withId(com.google.android.material.R.id.contentPanel),
                                0)))
                .atPosition(0);
        materialTextView6.perform(click());

        ViewInteraction textView21 = onView(
                allOf(withId(R.id.textLegPlayer1), withText("2"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView21.check(matches(withText("0")));

        ViewInteraction textView22 = onView(
                allOf(withId(R.id.textLegPlayer2), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView22.check(matches(withText("0")));

        ViewInteraction textView23 = onView(
                allOf(withId(R.id.textSetPlayer1), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView23.check(matches(withText("1")));

        ViewInteraction textView24 = onView(
                allOf(withId(R.id.textSetPlayer2), withText("0"),
                        withParent(allOf(withId(R.id.frameSetsLegs),
                                withParent(withId(R.id.frameNameScore)))),
                        isDisplayed()));
        textView24.check(matches(withText("0")));
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
