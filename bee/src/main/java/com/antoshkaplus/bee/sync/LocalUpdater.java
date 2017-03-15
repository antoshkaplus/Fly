package com.antoshkaplus.bee.sync;

/**
 * Created by antoshkaplus on 3/14/17.
 */

public class LocalUpdater<RemoteUpdates> {

    public void update() {

        prepare();

        int localVersion = getLastSyncedVersion();

        // try until we die or use some kind of counters
        int remoteVesion = fetchRemoteVersion();

        RemoteUpdates updates = fetchRemoteUpdates();
        // updates whatever is able to update
        // resolving conflict should be considered as an update
        updateLocal(updates);

        setLastSyncedVerstion(remoteVesion);
    }

    private void updateLocal(RemoteUpdates updates) {
    }

    private RemoteUpdates fetchRemoteUpdates() {
        throw new RuntimeException();
    }

    private int fetchRemoteVersion() {
        throw new RuntimeException();
    }

    private void prepare() {
    }

    public int getLastSyncedVersion() {
        return 0;
    }

    public void setLastSyncedVerstion(int lastSyncedVerstion) {
        lastSyncedVerstion = 0;
    }
}
