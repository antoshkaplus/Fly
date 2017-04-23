package com.antoshkaplus.bee.sync;

import java.util.List;

/**
 * Created by antoshkaplus on 4/20/17.
 */

public class Util {


    static public class Record {
        int id;
        int val;

        Record(int id, int val) {
            this.id = id;
            this.val = val;
        }

        public int getId() {
            return id;
        }
    }


    static public class ServerRecords {
        List<Record> records;

        ServerRecords(List<Record> records) {
            this.records = records;
        }
    }


    static class LocalUpdates {
        List<Record> updates;

        LocalUpdates(List<Record> updates) {
            this.updates = updates;
        }
    }

    static class ConflictResolution {
        int id;
        boolean leaveLocal;

        ConflictResolution(int id, boolean leaveLocal) {
            this.id = id;
            this.leaveLocal = leaveLocal;
        }

        public int getId() {
            return id;
        }
    }
}
