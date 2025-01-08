package bruzsa.laszlo.dartsapp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.test.MyViewModel;
import bruzsa.laszlo.dartsapp.test.ValamiDepender;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
public class FooTest {

    @Rule(order = 1)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject
    ValamiDepender valamiDepender;

    @Before
    public void init() {
        hiltRule.inject();


    }

    @Test
    public void hiltTest() {
        assertNotNull(valamiDepender);
        Assert.assertEquals(0, valamiDepender.getValami().getNum());
    }

    // ---------------------------------------------------------------------------------------------

    @AndroidEntryPoint
    public static final class TestActivity extends AppCompatActivity {
    }

    @Rule(order = 2)
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    MyViewModel myViewModel;

    @Test
    public void name() {
//        activityScenarioRule.getScenario().moveToState(Lifecycle.State.CREATED);
        activityScenarioRule.getScenario().onActivity(activity -> {
            assertNull(myViewModel);
            myViewModel = new ViewModelProvider(activity).get(MyViewModel.class);
            assertNotNull(myViewModel);
        });
//        activityScenarioRule.getScenario().moveToState(Lifecycle.State.DESTROYED);
    }


}
