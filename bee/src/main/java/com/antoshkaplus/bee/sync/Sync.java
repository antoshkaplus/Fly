package com.antoshkaplus.bee.sync;

/**
 * Created by antoshkaplus on 3/14/17.
 *
 * Template Method design pattern
 * first you do Backend updates and after that local updates
 * how the fuck I should put 3 parameters to the template
 * cant they be resolved by themseves
 */

public class Sync <B_Updater extends BackendUpdater, L_Updater extends LocalUpdater> {

    private static final Object lock = new Object();

    private B_Updater backendUpdater;
    private L_Updater localUpdater;


    public void sync() {

        synchronized (lock) {
            backendUpdater.update();
            localUpdater.update();
        }

    }

    public void setBackendUpdater(B_Updater backendUpdater) {
        this.backendUpdater = backendUpdater;
    }

    public void setLocalUpdater(L_Updater localUpdater) {
        this.localUpdater = localUpdater;
    }


}
