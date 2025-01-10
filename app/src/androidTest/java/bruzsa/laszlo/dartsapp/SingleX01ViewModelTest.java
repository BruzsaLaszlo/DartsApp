package bruzsa.laszlo.dartsapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import bruzsa.laszlo.dartsapp.ui.singlex01.SingleX01ViewModel;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
public class SingleX01ViewModelTest {

    @Rule(order = 1)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 2)
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    SingleX01ViewModel viewModel;


    @Before
    public void init() {
        hiltRule.inject();
        activityScenarioRule.getScenario().onActivity(activity -> {
            assertNull(viewModel);
            viewModel = new ViewModelProvider(activity).get(SingleX01ViewModel.class);
            assertNotNull(viewModel);

            viewModel.newThrow(180, 3);
            assertEquals(321, viewModel.getScore().getValue());
            viewModel.newThrow(180, 3);
            assertEquals(141, viewModel.getScore().getValue());
            viewModel.newThrow(180, 3);
            assertEquals(141, viewModel.getScore().getValue());
            viewModel.newThrow(141, 3);
            assertEquals(501, viewModel.getScore().getValue());

            viewModel.newThrow(200, 3);
            assertEquals(501, viewModel.getScore().getValue());
            viewModel.newThrow(167, 3);
            assertEquals(334, viewModel.getScore().getValue());
        });
    }

    @Test
    public void newThrow() {
        viewModel.newThrow(44, 3);
        assertEquals(290, viewModel.getScore().getValue());

        viewModel.newThrow(180, 3);
        assertEquals(110, viewModel.getScore().getValue());
        viewModel.newThrow(110, 3);
        assertEquals(501, viewModel.getScore().getValue());
    }

    @Test
    public void getScore() {
        viewModel.newThrow(100, 3);
        assertEquals(234, viewModel.getScore().getValue());
    }

}