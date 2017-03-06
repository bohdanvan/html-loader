package com.bvan.htmlloader;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author bvanchuhov
 */
class OutputLoadingCallback implements LoadingCallback {

    private int loadedCount;
    private final Lock loadedCountLock = new ReentrantLock();

    @Override
    public void onSuccess(String url) {
        loadedCountLock.lock();
        try {
            loadedCount++;
            consolePrintln(String.format(
                    "%-30s%s",
                    "Total pages loaded: " + loadedCount,
                    "Page '" + url + "' has been loaded")
            );
        } finally {
            loadedCountLock.unlock();
        }
    }

    @Override
    public void onException(String url, Exception e) {
        consolePrintln("Exception during page '" + url + "' loading: " + e.getMessage());
    }

    private void consolePrintln(String message) {
        synchronized (System.out) {
            System.out.println(message);
        }
    }
}
