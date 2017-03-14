package es.urjc.mov.javsan.miner;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestMiner {

    private MinerActivity miner;
    private RecordsFragAc records;

    /**
     * A JUnit {@link Rule @Rule} to launch your miner under test. This is a replacement
     * for {@link //ActivityInstrumentationTestCase2}.
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
    public ActivityTestRule<RecordsFragAc> mActivityRuleRecords = new ActivityTestRule<RecordsFragAc>(
            RecordsFragAc.class, false, false) {
    };

    @Test
    public void game_isCorrect() {
        miner = mActivityRuleMiner.launchActivity(new Intent());

        MinerGame game = miner.getGame();
        int numRadars = MinerActivity.RADARS;

        for (int i = 0; i < game.getRows() * game.getCols(); i++) {
            Point p = new Point(i / game.getRows(), i % game.getCols());

            if (game.isEndGame()) {
                break;
            }

            if (!game.isHidden(p) || game.isFail(p)) {
                continue;
            }

            onView(withId(i)).perform(click());
            numRadars = checkNumRadars(numRadars);
            checkScore(game);
        }
        if (!game.isWinGame()) {
            fail();
        }
    }
    private int checkNumRadars (int numRadars) {
        onView(withText(R.string.radar)).perform(click());

        if (numRadars > 0) {
            numRadars--;
        }
        onView(withText(String.valueOf(numRadars))).check(matches(isDisplayed()));
        return numRadars;
    }

    private void checkScore(MinerGame game) {
        onView(withText(String.valueOf(game.getScore()))).check(matches(isDisplayed()));
    }

    @Test
    public void record_isCorrect() {
        int score = Integer.MAX_VALUE;
        records = mActivityRuleRecords.launchActivity(initActivity(score));
        Records r = new Records(records);

        // Default player name...
        onView(withId(R.id.player_name)).check(matches(isDisplayed()));

        // Score get in miner...
        onView(withText(String.valueOf(score))).check(matches(isDisplayed()));

        // Type new player test...
        onView(withId(R.id.player_name)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.player_name)).perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.confirm_record)).perform(click());

        // Show if is in the table of records player test...
        onView(withText("Test")).check(matches(isDisplayed()));
        onView(withText(String.valueOf(score))).check(matches(isDisplayed()));
        onView(withText(R.string.play)).check(matches(isDisplayed()));

        checkScores(r);
    }

    @Test
    public void record_minIsCorrect() {
        int score = 50;
        records = mActivityRuleRecords.launchActivity(initActivity(score));
        Records r = new Records(records);

        // Default player name...
        onView(withId(R.id.player_name)).check(matches(isDisplayed()));

        // Score get in miner...
        onView(withText(String.valueOf(score))).check(matches(isDisplayed()));

        // Type new player test...
        onView(withId(R.id.player_name)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.player_name)).perform(typeText("TestMin"), closeSoftKeyboard());
        onView(withId(R.id.confirm_record)).perform(click());

        // Show if is in the table of records player test...
        onView(withText("TestMin")).check(doesNotExist());
        onView(withText(String.valueOf(score))).check(doesNotExist());
        onView(withText(R.string.play)).check(matches(isDisplayed()));

        checkScores(r);
    }

    private Intent initActivity (int score) {
        Bundle state = new Bundle();
        state.putInt("score", score);

        Intent intent = new Intent();
        intent.putExtras(state);

        return intent;
    }

    private void checkScores(Records r) {
        HashMap <String, Integer> scores = r.getScores();
        if (scores ==  null) {
            return;
        }
        if (scores.size() > Records.TOP) {
            fail();
        }
        checkSortScores(r);
    }

    private void checkSortScores(Records r) {
        Object[] o = r.sortedByValues();
        int minValue = Integer.MAX_VALUE;

        for (Object e : o) {
            int val = ((HashMap.Entry<String, Integer>) e).getValue();

            if (val <= minValue) {
                minValue = val;
            } else {
                fail();
            }
        }
    }
}