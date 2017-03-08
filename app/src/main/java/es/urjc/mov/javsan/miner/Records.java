package es.urjc.mov.javsan.miner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

class Records {
    private static final String fileRecords = "records.txt";
    private static final int TOP = 10;

    private AppCompatActivity activity;
    private HashMap<String, Integer> scores;

    Records (AppCompatActivity a) {
        activity = a;
        scores = new HashMap<>();
        readRecords();
    }

    boolean isRecord(int score) {
        return (scores.size() < TOP || isTop(score)) && score > 0;
    }

    void newRecord(String name, int score) {
        scores.put(name, score);
        if (scores.size() > TOP) {
            delLastScore();
        }
        writeRecords();
    }

    @Override
    public String toString()    {
        Object[] a = sortedByValues();
        String result = String.format("NumScores : %d\n", scores.size());

        for (Object e : a) {
            result += String.format("Name : %7s, Value : %7d\n",
                    ((HashMap.Entry<String, Integer>) e).getKey(),
                    ((HashMap.Entry<String, Integer>) e).getValue());
        }
        return result;
    }

    Object[] sortedByValues() {
        Object[] a = scores.entrySet().toArray();

        Arrays.sort(a, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((HashMap.Entry<String, Integer>) o2).getValue()
                        .compareTo(((HashMap.Entry<String, Integer>) o1).getValue());
            }
        });
        return a;
    }


    private void delLastScore() {
        String keyMin = "";
        int valMin = Integer.MAX_VALUE;

        for (HashMap.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() < valMin) {
                valMin = entry.getValue();
                keyMin = entry.getKey();
            }
        }
        scores.remove(keyMin);
    }

    private boolean isTop(int score) {
        boolean isMax = false;

        for (HashMap.Entry<String, Integer> entry : scores.entrySet()) {
            if (score > entry.getValue()) {
                isMax = true;
            }
        }
        return isMax;
    }

    private void writeRecords() {
        FileOutputStream fos = null;

        try {

            fos = activity.openFileOutput(fileRecords, Context.MODE_PRIVATE);

            for (HashMap.Entry<String, Integer> entry : scores.entrySet()) {
                String newScore = entry.getKey() + " " + String.valueOf(entry.getValue()) + '\n';

                fos.write(newScore.getBytes());
                fos.flush();
            }

        } catch (IOException e) {

        } finally {
            close(fos);
        }
    }

    private void close (FileOutputStream fos) {
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
        }
    }

    private void readRecords() {
        FileInputStream fis = null;

        try {

            fis = activity.openFileInput(fileRecords);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                String[] record = line.split(" ");
                scores.put(record[0], Integer.valueOf(record[1]));
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            close(fis);
        }
    }

    private void close(FileInputStream fis) {
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
        }
    }
}
