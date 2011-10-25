package com.foobnix.http;

public interface ProgressListener {
    void transferred(long num);
    void contentSize(long num);
}