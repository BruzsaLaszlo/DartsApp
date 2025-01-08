package bruzsa.laszlo.dartsapp;

import static org.junit.Assert.assertEquals;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
public class TestViewModelTest {

    @Rule(order = 1)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 2)
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    public TestViewModel testViewModel;


    @Before
    public void init() {
        hiltRule.inject();
        activityScenarioRule.getScenario().onActivity(activity ->
                testViewModel = new ViewModelProvider(activity).get(TestViewModel.class));

    }

    @Test
    public void name() {
        int count = testViewModel.getSettings().getX01Settings().getCount();
        assertEquals(1, count);
    }
}