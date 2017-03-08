package es.urjc.mov.javsan.miner;

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestSound {
    @Test
    public void sound_isCorrect() throws Exception {
        for (int i = 0 ; i < 10 ; i++) {
            SoundControl soundControl = new SoundControl();
            SoundTone soundTone = new SoundTone(50 * i + 500, soundControl);
            soundTone.execute();
            SystemClock.sleep(1000);
            soundControl.endSound();
        }

    }
}
