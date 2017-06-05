package com.antoshkaplus.fly.backend;

import java.util.List;

/**
 * Created by antoshkaplus on 6/3/17.
 */

public class RecordList {
    List<Record> list;
    String nextCursor;

    public RecordList() {}

    public RecordList(List<Record> list, String nextCursor) {
        this.list = list;
        this.nextCursor = nextCursor;
    }

    public List<Record> getList() {
        return list;
    }

    public String getNextCursor() {
        return nextCursor;
    }
}
