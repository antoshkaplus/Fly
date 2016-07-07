package com.antoshkaplus.fly.app;

import android.content.Context;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by antoshkaplus on 7/2/16.
 */
public class ListSearchAdapter extends BaseAdapter implements Filterable {

    private List<String> wordList = new ArrayList<>();
    private Context ctx;
    private Range<Integer> currentRange;


    ListSearchAdapter(Context ctx, List<String> wordList) {
        this.ctx = ctx;
        this.wordList = new ArrayList<>(wordList);
        Collections.sort(this.wordList, String.CASE_INSENSITIVE_ORDER);
        currentRange = entireRange();
    }

    @Override
    public int getCount() {
        return currentRange.getUpper() - currentRange.getLower();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }

        final String word = (String) getItem(i);
        TextView textView = (TextView)view;
        textView.setText(word);
        return view;
    }

    @Override
    public long getItemId(int i) {
        return i + currentRange.getLower();
    }

    @Override
    public Object getItem(int i) {
        return wordList.get(i + currentRange.getLower());
    }

    @Override
    public Filter getFilter() {
        return new WordFilter();
    }

    private Range<Integer> entireRange() {
        return new Range<>(0, this.wordList.size());
    }

    private Range<Integer> emptyRange() {
        return new Range<>(0, 0);
    }


    class WordFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            filterResults.count = 1;
            String str = charSequence.toString().toLowerCase();

            if (str.isEmpty()) {
                filterResults.values = entireRange();
                return filterResults;
            }

            int index = Collections.binarySearch(wordList, str, String.CASE_INSENSITIVE_ORDER);
            if (index < 0) {
                filterResults.values = emptyRange();
                return filterResults;
            }

            int end = index;
            while (end < wordList.size() && wordList.get(end).toLowerCase().startsWith(str)) {
                ++end;
            }
            filterResults.values = new Range<>(index, end);
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            // we
            if (filterResults.count == 0) return;
            currentRange = (Range) filterResults.values;
            notifyDataSetChanged();
        }
    }

}
