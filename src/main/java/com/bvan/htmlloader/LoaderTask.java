package com.bvan.htmlloader;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author bvanchuhov
 */
final class LoaderTask implements Runnable {

    private final String url;
    private final String file;
    private final LoadingCallback finalLoadingCallback;

    public LoaderTask(String url, String file, LoadingCallback loadingCallback) {
        this.url = url;
        this.file = file;
        this.finalLoadingCallback = loadingCallback;
    }

    @Override
    public void run() {
        try {
            Files.createDirectories(Paths.get(file).getParent());
            try (InputStream input = new URL(this.url).openStream(); OutputStream output = new FileOutputStream(file)) {
                IOUtils.copy(input, output);
                finalLoadingCallback.onSuccess(url);
            }
        } catch (IOException e) {
            finalLoadingCallback.onException(url, e);
        }
    }
}
