package com.bvan.htmlloader;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

import java.io.IOException;

/**
 * @author bvanchuhov
 */
public class HtmlLoaderRunner {

    public static void main(String[] args) throws JSAPException, IOException {
        ConsoleParams params = getConsoleParams(args);
        HtmlLoader htmlLoader = new HtmlLoader(params.mainUrl, params.threads, params.baseDir);
        htmlLoader.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> htmlLoader.shutdown()));
    }

    private static ConsoleParams getConsoleParams(String[] args) throws JSAPException {
        JSAPResult jsapResult = parseArgs(args);

        return new ConsoleParams(
                jsapResult.getString("main-url"),
                jsapResult.getInt("threads", Runtime.getRuntime().availableProcessors()),
                jsapResult.getString("base-dir", "")
        );
    }

    private static JSAPResult parseArgs(String[] args) throws JSAPException {
        JSAP jsap = new JSAP();

        jsap.registerParameter(new FlaggedOption("threads")
                .setStringParser(JSAP.INTEGER_PARSER)
                .setLongFlag("threads"));
        jsap.registerParameter(new FlaggedOption("base-dir")
                .setStringParser(JSAP.STRING_PARSER)
                .setLongFlag("base-dir"));
        jsap.registerParameter(new FlaggedOption("main-url")
                .setStringParser(JSAP.STRING_PARSER)
                .setLongFlag("main-url"));

        return jsap.parse(args);
    }

    private static class ConsoleParams {
        public final String mainUrl;
        public final int threads;
        public final String baseDir;

        public ConsoleParams(String mainUrl, int threads, String baseDir) {
            this.mainUrl = mainUrl;
            this.threads = threads;
            this.baseDir = baseDir;
        }
    }
}
