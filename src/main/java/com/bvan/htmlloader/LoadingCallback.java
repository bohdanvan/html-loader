package com.bvan.htmlloader;

/**
 * @author bvanchuhov
 */
@FunctionalInterface
public interface LoadingCallback {
    void onSuccess(String url);
    default void onException(String url, Exception e) {}
}
