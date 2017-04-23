package com.antoshkaplus.bee.sync;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.antoshkaplus.bee.sync.Util.*;

/**
 * Created by antoshkaplus on 4/20/17.
 */

public class BackendUpdaterImpl extends BackendUpdater<LocalUpdates, ServerRecords> {

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

    BackendUpdaterImpl() {}

    @Override
    void clearUpdateId() {
        updateId = null;
    }

    @Override
    void resetUpdates(LocalUpdates updates) {
        // leave what has to stay
        this.localUpdates.updates = updates.updates;
    }

    @Override
    LocalUpdates updateRemote(LocalUpdates updates) {
        serverUpdates = new ArrayList<>(updates.updates);
        for (Iterator<Record> iter = updates.updates.listIterator(); iter.hasNext(); ) {
            Record r = iter.next();
            if (!failedRemoteUpdates.contains(r.id)) {
                iter.remove();
            }
        }
        return ...
    }

    @Override
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

    @Override
    ServerRecords getServerRecords(LocalUpdates updates) {
        return serverRecords = new ServerRecords(serverRecordsList);
    }

    // extracts updates from the main tables
    @Override
    void extractUpdates() {
        localUpdates = new LocalUpdates(localUpdatesList);
        // we do nothing here probably, as updates set by user
    }

    @Override
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

    public void setLocalUpdates(LocalUpdates localUpdates) {
        this.localUpdates = localUpdates;
    }
}
