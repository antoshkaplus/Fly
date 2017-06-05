package com.antoshkaplus.fly.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by antoshkaplus on 6/3/17.
 */
@Entity
public class Record {
    @Id
    String data;
    @Index
    Date creationDate;

    public Record() {}

    public Record(String data, Date date) {
        this.data = data;
        this.creationDate = date;
    }

    public String getData() {
        return data;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
