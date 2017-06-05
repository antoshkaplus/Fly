/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.antoshkaplus.fly.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.fly.antoshkaplus.com",
                ownerName = "backend.fly.antoshkaplus.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    static {
        ObjectifyService.register(Record.class);
    }


    @ApiMethod(name = "placeData")
    public void placeData() {
        int N = 1000;
        Random random = new Random();
        for (int i = 0; i < N; ++i) {
            ofy().save().entity(new Record(UUID.randomUUID().toString(), new Date(random.nextLong()))).now();
        }
    }

    @ApiMethod(name = "clearData")
    public void clearData() {
        List<Key<Record>> keys = ofy().load().type(Record.class).keys().list();
        ofy().delete().keys(keys).now();
    }

    @ApiMethod(name = "getData")
    public RecordList getData(@Named("pageSize")int pageSize, @Named("cursor") @Nullable String cursor) {

        Query<Record> q = ofy().load()
                .type(Record.class)
                .order("-creationDate");
        if (cursor != null && !cursor.isEmpty()) {
            q = q.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Record> it = q.iterator();
        List<Record> list = new ArrayList<>();
        while (it.hasNext() && list.size() < pageSize) {
            list.add(it.next());
        }
        String nextCursor = it.getCursor().toWebSafeString();
        return new RecordList(list, nextCursor);

    }

    @ApiMethod(name = "getWholeData")
    public RecordList getWholeData() {
        return new RecordList(ofy().load().type(Record.class).order("-creationDate").list(), null);
    }

    @ApiMethod(name = "addRecord")
    public void addRecord(Record record) {
        ofy().save().entity(record).now();
    }
}
