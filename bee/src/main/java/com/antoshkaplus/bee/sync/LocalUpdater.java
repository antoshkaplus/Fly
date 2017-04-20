package com.antoshkaplus.bee.sync;

/**
 * Created by antoshkaplus on 3/14/17.
 */

public abstract class LocalUpdater<RemoteUpdates> {

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

    protected abstract void updateLocal(RemoteUpdates updates);
    protected abstract RemoteUpdates fetchRemoteUpdates();
    protected abstract int fetchRemoteVersion();
    protected abstract void prepare();
    protected abstract int getLastSyncedVersion();
    protected abstract void setLastSyncedVerstion(int lastSyncedVerstion);
}
