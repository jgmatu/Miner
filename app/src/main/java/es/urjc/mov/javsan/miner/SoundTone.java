package es.urjc.mov.javsan.miner;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;


class SoundTone extends AsyncTask<String, String, String> {

    private final int duration = 1; // seconds
    private final int sampleRate = 24000;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    private double freqOfTone; // hz

    private final byte generatedSnd[] = new byte[2 * numSamples];

    private SoundControl control;

    SoundTone(double fq, SoundControl c) {
        freqOfTone = fq;
        control = c;
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            genTone();
            AudioTrack audioTrack = playSound();
            audioTrack.write(generatedSnd, 0, generatedSnd.length);
            audioTrack.play();
            control.waitSound();
            audioTrack.stop();
            audioTrack.release();

        } catch (InterruptedException e) {
        }
        return "";
    }

    private void genTone(){
        // fill out the array.
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
        }
        // Convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // Scale to maximum amplitude.
            final short val = (short) ((dVal * 32767));
            // In 16 bit wav PCM, first byte is the low order byte.
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    private AudioTrack playSound() {
        return new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                generatedSnd.length, AudioTrack.MODE_STATIC);
    }
}
