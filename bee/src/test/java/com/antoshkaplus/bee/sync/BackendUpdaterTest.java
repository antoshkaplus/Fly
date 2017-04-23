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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.antoshkaplus.bee.sync.Util.*;

/**
 * Created by antoshkaplus on 3/15/17.
 */

public class BackendUpdaterTest {


    // we need to implement updater and some scenarios
    // like what to expect in some kinds of situations
    // we also should try to throw exceptions somewhere in the middle of update




    Record R(int id, int val) {
        return new Record(id, val);
    }

    ConflictResolution CR(int id, boolean leaveLocal) {
        return new ConflictResolution(id, leaveLocal);
    }

    @Test
    public void testClearUpdateId() {
        BackendUpdaterImpl updater = new BackendUpdaterImpl();
        assertNull(updater.retrieveUpdateId());
        assertNotNull(updater.generateUpdateId());
        assertNotNull(updater.retrieveUpdateId());
        updater.clearUpdateId();
        assertNull(updater.retrieveUpdateId());
    }

    @Test
    public void testResetUpdates() {
        LocalUpdates newUpdates = new LocalUpdates(new ArrayList<>());
        LocalUpdates localUpdates = new LocalUpdates(new ArrayList<>());

        BackendUpdaterImpl updater = new BackendUpdaterImpl();
        updater.setLocalUpdates(localUpdates);
        assertEquals(updater.getUpdates(), localUpdates);
        updater.resetUpdates(newUpdates);
        assertEquals(updater.getUpdates(), newUpdates);
    }

    @Test
    public void testUpdateRemote() {

        BackendUpdaterImpl updater = new BackendUpdaterImpl();
        List<Integer> failed ..
        List<Record> Update

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
