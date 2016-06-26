package com.antoshkaplus.fly.app;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by antoshkaplus on 6/17/16.
 *
 * type is ignored
 *
 * supported URIs:
 * /search_suggest_query/?params
 * /search_suggest_query/str?params
 * /word?params
 *      - no params gives whole list of words
 *      - 'id' gives result by id/position
 *      - 'word' gives result by word itself
 *
 */
public class WordContentProvider extends ContentProvider {

    public static final String AUTHORITY =
            "com.antoshkaplus.fly.app.WordContentProvider";

    public static final Uri URI_CONTENT =
            Uri.parse("content://" + AUTHORITY);

    //
    //


    // some constants
    private static final int WORD_LIST = 1;
    private static final int WORD = 2;
    private static final int WORD_SUGGESTIONS_FREQUENT = 3;
    private static final int WORD_SUGGESTIONS = 4;

    private static final UriMatcher URI_MATCHER;

    // prepare the UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_QUERY + "/",
                WORD_SUGGESTIONS_FREQUENT);
        URI_MATCHER.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_QUERY + "/*",
                WORD_SUGGESTIONS);
        URI_MATCHER.addURI(AUTHORITY,
                Words.PATH,
                WORD);
    }

    private List<String> words = new ArrayList<>();



    @Override
    public boolean onCreate() {
        // create list of words
        try {
            words = Arrays.asList(Util.readWords(getContext()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case WORD_LIST:
                return Words.CONTENT_DIR_TYPE;
            case WORD:
                return Words.CONTENT_ITEM_TYPE;
            case WORD_SUGGESTIONS:
                return Words.CONTENT_DIR_TYPE;
            default:
                return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        // setNotificationUri call aren't really researched

        String limitStr = uri.getQueryParameter("limit");
        int limit = limitStr == null ? Integer.MAX_VALUE : Integer.parseInt(limitStr);

        // we create new matrix cursor and try to fill it
        // projection_all is too far away. better create it over here maybe
        MatrixCursor cursor = new MatrixCursor(Words.PROJECTION_ALL);
        int code = URI_MATCHER.match(uri);
        switch (URI_MATCHER.match(uri)) {
            case WORD:
                String word = uri.getQueryParameter("word");
                // look for position of the word
                if (word != null) {
                    int position = Collections.binarySearch(words, word, String.CASE_INSENSITIVE_ORDER);
                    if (position >= 0) {
                        addWord(cursor, position);
                    }
                    break;
                }
                String id = uri.getQueryParameter("id");
                if (id != null) {
                    int position = Integer.parseInt(id);
                    addWord(cursor, position);
                    break;
                }
                for (int i = 0; i < words.size() && cursor.getCount() < limit; ++i) {
                    addWord(cursor, i);
                }
                cursor.setNotificationUri(getContext().getContentResolver(), Words.CONTENT_URI);
                break;
            case WORD_SUGGESTIONS_FREQUENT:
                // have to think how to implement it
                fillWordSuggestionsFrequent(cursor);
                break;
            case WORD_SUGGESTIONS:
                s = uri.getLastPathSegment();
                if (s.length() < 4) {
                    fillWordSuggestionsFrequent(cursor);
                    break;
                }

                int start = Collections.binarySearch(words, s, String.CASE_INSENSITIVE_ORDER);
                if (start < 0) {
                    start = -(start+1);
                }
                int i = start;
                while (i < words.size() && words.get(i).toLowerCase().startsWith(s) && cursor.getCount() < limit) {
                    addWord(cursor, i);
                    ++i;
                }
                cursor.setNotificationUri(getContext().getContentResolver(), Words.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return cursor;
    }

    private void fillWordSuggestionsFrequent(MatrixCursor cursor) {
    }

    private void addWord(MatrixCursor cursor, int index) {
        cursor.newRow().add(index).add(words.get(index)).add(index);
    }

    static final class Words implements BaseColumns {
        public static final String PATH = "word";

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(WordContentProvider.URI_CONTENT,
                        PATH);

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/com.antoshkaplus.fly.app.words";

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/com.antoshkaplus.fly.app.words";

        public static final String[] PROJECTION_ALL =
                {_ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA};
    }
}
