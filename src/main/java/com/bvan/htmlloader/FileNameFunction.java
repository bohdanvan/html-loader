package com.bvan.htmlloader;

/**
 * @author bvanchuhov
 */
@FunctionalInterface
public interface FileNameFunction {
    String fileName(int count, String url);
}
