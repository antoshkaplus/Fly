package com.antoshkaplus.fly.app;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antoshkaplus on 6/20/16.
 */
public class Util {

    public static String[] readWords(Context ctx) throws IOException {
        InputStream inputStream = ctx.getResources().openRawResource(R.raw.words);
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        List<String> wordList = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine())!= null) {
            wordList.add(line);
        }
        return wordList.toArray(new String[wordList.size()]);
    }

}
