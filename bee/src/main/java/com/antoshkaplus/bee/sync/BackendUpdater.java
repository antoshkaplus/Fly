package com.antoshkaplus.bee.sync;

import java.util.UUID;

/**
 * Created by antoshkaplus on 3/14/17.
 *
 * or maybe call sync
 */

// make it interface
public abstract class BackendUpdater <LocalUpdates, ServerRecords> {

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

    abstract void clearUpdateId();

    // cleans up all updates except those in the argument
    abstract void resetUpdates(LocalUpdates updates);

    //
    abstract LocalUpdates updateRemote(LocalUpdates updates);
    abstract void resolveConflicts(LocalUpdates updates, ServerRecords serverRecords);
    abstract ServerRecords getServerRecords(LocalUpdates updates);
    abstract void extractUpdates();
    abstract boolean extractedUpdatesEmpty();
    abstract UUID generateUpdateId();
    abstract UUID retrieveUpdateId();
    abstract LocalUpdates getUpdates();
    abstract void prepare();
}
