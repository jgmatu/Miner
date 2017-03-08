package es.urjc.mov.javsan.miner;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.test.espresso.ViewAssertion;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.regex.Pattern.matches;
import static junit.framework.Assert.fail;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestMiner  {

    private MinerActivity miner;
    private RecordsActivity records;

    /**
     * A JUnit {@link Rule @Rule} to launch your miner under test. This is a replacement
     * for {@link ActivityInstrumentationTestCase2}.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link Before @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the miner for you and also expose
     * the miner under test. To get a reference to the miner you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */
    @Rule
    public ActivityTestRule<MinerActivity> mActivityRuleMiner = new ActivityTestRule<MinerActivity>(
            MinerActivity.class, false, false) {
    };

    @Rule
    public ActivityTestRule<RecordsActivity> mActivityRuleRecords = new ActivityTestRule<RecordsActivity>(
            RecordsActivity.class, false , false) {
    };


    @Test
    public void typeTextInInput_clickButton_SubmitsForm() {
        // Lazily launch the Activity with a custom start Intent per test.
        miner = mActivityRuleMiner.launchActivity(new Intent());

        MinerGame game = miner.getGame();

        for (int i = 0 ; i < 10 ; i++) {
            onView(withId(i)).perform(click());
            SystemClock.sleep(500);
        }
        if (!game.isLostGame()) {
            fail();
        }
    }

    @Test
    public void record_isCorrect() {
        Bundle state = new Bundle();
        state.putInt("score" , 1000);

        Intent i = new Intent();
        i.putExtras(state);

        records = mActivityRuleRecords.launchActivity(i);

        onView(withId(RecordsActivity.REGISTERPLAYER.IDSCORE)).check(matches(withText("1000")));
    }
}