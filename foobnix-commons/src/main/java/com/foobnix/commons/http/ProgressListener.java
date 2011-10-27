package com.foobnix.commons.http;

public interface ProgressListener {
    void transferred(long num);
    void contentSize(long num);
}