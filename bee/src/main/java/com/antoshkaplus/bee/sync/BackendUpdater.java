package com.antoshkaplus.bee.sync;

import java.util.UUID;

/**
 * Created by antoshkaplus on 3/14/17.
 *
 * or maybe call sync
 */

// make it interface
public class BackendUpdater <LocalUpdates, ServerRecords> {

    // all updates are considered local unless opposite specified

    public void update() {

        prepare();

        LocalUpdates updates = getUpdates();
        UUID updateId;
        if (!extractedUpdatesEmpty()) {
            updateId = retrieveUpdateId();
            if (updateId == null) {
                updateId = generateUpdateId();
            }
        } else {
            updateId = generateUpdateId();
            extractUpdates();
            updates = getUpdates();
        }

        ServerRecords serverRecords = getServerRecords(updates);

        // so here we pause for sometime waiting while user resolves all the conflicts
        // leave everything in updates. don't return anything for now.
        // later on may change algorithm
        resolveConflicts(updates, serverRecords);

        // get success from the server
        // sometimes server may return records where new conflicts were discovered
        updateRemote(updates);
        resetUpdates(updates);
        clearUpdateId();
    }

    private void clearUpdateId() {
    }

    private void resetUpdates(LocalUpdates updates) {
    }

    private void updateRemote(LocalUpdates updates) {
    }

    private void resolveConflicts(LocalUpdates updates, ServerRecords serverRecords) {

    }

    private ServerRecords getServerRecords(LocalUpdates updates) {
        throw new RuntimeException();
    }

    private void extractUpdates() {
    }

    private boolean extractedUpdatesEmpty() {
        return true;
    }

    protected void prepareUpdates() {
    }

    protected UUID generateUpdateId() {
        throw new RuntimeException();
    }

    protected UUID retrieveUpdateId() {
        throw new RuntimeException();
    }

    protected LocalUpdates getUpdates() {
        throw new RuntimeException();
    }

    protected void prepare() {
    }
}
