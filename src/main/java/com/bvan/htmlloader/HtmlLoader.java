package com.bvan.htmlloader;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author bvanchuhov
 */
public final class HtmlLoader {

    private final String mainUrl;
    private final int threads;
    private final FileNameFunction fileNameFunction;
    private final LoadingCallback loadingCallback;

    private ExecutorService executorService;

    public HtmlLoader(String mainUrl, int threads) {
        this(mainUrl, threads, "");
    }

    public HtmlLoader(String mainUrl, int threads, String baseDir) {
        this(mainUrl, threads, (count, url) -> baseDir + "/" + count + ".html", new OutputLoadingCallback());
    }

    public HtmlLoader(String mainUrl, int threads, FileNameFunction fileNameFunction, LoadingCallback loadingCallback) {
        this.mainUrl = mainUrl;
        this.threads = threads;
        this.fileNameFunction = fileNameFunction;
        this.loadingCallback = loadingCallback;
    }

    public void start() throws IOException {
        executorService = Executors.newFixedThreadPool(threads);

        try {
            submitLoaderThreads();
        } finally {
            executorService.shutdown();
        }
    }

    private void submitLoaderThreads() throws IOException {
        int taskCount = 1;
        for (Element linkElement : findLinkElements()) {
            String url = linkElement.absUrl("href");
            String fileName = fileNameFunction.fileName(taskCount, url);
            executorService.execute(new LoaderTask(url, fileName, loadingCallback));

            if (taskCount++ >= threads) {
                break;
            }
        }
    }

    private Elements findLinkElements() throws IOException {
        Connection connect = Jsoup.connect(mainUrl);
        Document document = connect.get();
        return document.select("a");
    }

    public void shutdown() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
