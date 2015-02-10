package com.uwaterloo.fydp.structure;

public class Envelope<T> {
    private Metadata metadata;
    T result;

    public Metadata getMetadata() {
        return metadata;
    }

    public T getResult() {
        return result;
    }
}
