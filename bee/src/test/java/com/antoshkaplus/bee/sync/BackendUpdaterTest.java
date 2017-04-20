package com.antoshkaplus.bee.sync;

import com.sun.corba.se.spi.activation.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by antoshkaplus on 3/15/17.
 */

public class BackendUpdaterTest {


    // we need to implement updater and some scenarios
    // like what to expect in some kinds of situations
    // we also should try to throw exceptions somewhere in the middle of update

    static class Record {
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


    static class LocalUpdates {
        List<Record> updates;

        LocalUpdates(List<Record> updates) {
            this.updates = updates;
        }
    }


    static class ServerRecords {
        List<Record> records;

        ServerRecords(List<Record> records) {
            this.records = records;
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


    static class BackendUpdaterImpl extends BackendUpdater<LocalUpdates, ServerRecords> {

        List<Record> localUpdatesList;
        List<Record> serverRecordsList;
        List<ConflictResolution> conflictResolutions;
        Set<Integer> failedRemoteUpdates;

        UUID updateId;

        LocalUpdates localUpdates;
        // correspond to local updates ids
        ServerRecords serverRecords;
        List<Record> serverUpdates;


        BackendUpdaterImpl(List<Record> localUpdates, List<Record> serverRecords, List<Integer> failedRemoteUpdates, List<ConflictResolution> conflictResolutions) {
            localUpdatesList = localUpdates;
            serverRecordsList = serverRecords;
            this.conflictResolutions = conflictResolutions;
            this.failedRemoteUpdates = new TreeSet<>(failedRemoteUpdates);
        }

        void clearUpdateId() {
            updateId = null;
        }

        void resetUpdates(LocalUpdates updates) {
            // leave what has to stay
            this.localUpdates.updates = updates.updates;
        }

        void updateRemote(LocalUpdates updates) {
            serverUpdates = new ArrayList<>(updates.updates);
            for (Iterator<Record> iter = updates.updates.listIterator(); iter.hasNext(); ) {
                Record r = iter.next();
                if (!failedRemoteUpdates.contains(r.id)) {
                    iter.remove();
                }
            }
        }

        void resolveConflicts(LocalUpdates updates, ServerRecords serverRecords) {
            Map<Integer, ConflictResolution> resolutionMap = conflictResolutions.stream().collect(Collectors.toMap(ConflictResolution::getId, Function.identity()));
            Map<Integer, Record> serverMap = serverRecords.records.stream().collect(Collectors.toMap(Record::getId, Function.identity()));

            updates.updates.forEach(u -> {
                Record r = serverMap.get(u.getId());
                if (r != null && r.val != u.val && !resolutionMap.get(u.getId()).leaveLocal) {
                    u.val = r.val;
                }
            });
        }

        ServerRecords getServerRecords(LocalUpdates updates) {
            return serverRecords = new ServerRecords(serverRecordsList);
        }

        // extracts updates from the main tables
        void extractUpdates() {
            localUpdates = new LocalUpdates(localUpdatesList);
            // we do nothing here probably, as updates set by user
        }

        boolean extractedUpdatesEmpty() {
            return localUpdates == null || localUpdates.updates.isEmpty();
        }

        UUID generateUpdateId() {
            return updateId = UUID.randomUUID();
        }

        UUID retrieveUpdateId() {
            return updateId;
        }

        LocalUpdates getUpdates() {
            return localUpdates;
        }

        void prepare() {
            // do nothing as everything should be done already
        }
    }

    Record R(int id, int val) {
        return new Record(id, val);
    }

    ConflictResolution CR(int id, boolean leaveLocal) {
        return new ConflictResolution(id, leaveLocal);
    }

    @Test
    public void test_1() {
        // have to use mocks to imitate Method failures
        List<Record> updates = new ArrayList<>(Arrays.asList(R(1, 1), R(2, 2), R(3, 3)));
        List<Record> records = Arrays.asList(R(1, 1), R(2, 3), R(3, 3));
        List<Integer> failedRemoteUpdate = Arrays.asList(2);
        List<ConflictResolution> conflictResolutions = Arrays.asList(CR(2, true));

        BackendUpdaterImpl updater = new BackendUpdaterImpl(updates, records, failedRemoteUpdate, conflictResolutions);

        updater.update();

        List<Record> serverUpdates = Arrays.asList(R(2, 2));
        List<Record> localLeft = Arrays.asList(R(2, 2));

        assertEquals(serverUpdates, updater.serverUpdates);
        assertEquals(localLeft, updater.localUpdates);
    }




}
