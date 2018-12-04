package com.github.dahaka934.jhocon.writer;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

class Node {
    private Object value;
    private String comment = null;
    private Node prev = this;

    Node() {}

    Node(Node prev) {
        this.prev = prev;
    }

    Object getValue() {
        return value;
    }

    void setValue(Object value) {
        this.value = value;
    }

    void comment(String comment) {
        this.comment = comment;
    }

    void put(Object value) {
        this.value = convert(value);
    }

    Object convert(Object value) {
        return ConfigValueFactory.fromAnyRef(value, comment);
    }

    Node beginArray() {
        return new NodeArray(this);
    }

    Node endArray() {
        prev.put(getValue());
        return prev;
    }

    Node beginObject() {
        return new NodeObject(this);
    }

    Node endObject() {
        prev.put(getValue());
        return prev;
    }

    void name(String name) {}

    void value(Object value) {
        put(value);
    }

    void jsonValue(String value) {
        try {
            Config config = ConfigFactory.parseString(value);
            put(config.root().unwrapped());
        } catch (Exception e) {
            put(null);
        }
    }

    void nullValue() {
        put(null);
    }
}
