package com.org.pattern.gangoffour.behavioral.templatemethod;

/**
 * Template Method — defines the skeleton of an algorithm in a base class,
 * deferring some steps to subclasses.
 *
 * Real-world analogy: Data mining pipeline — parse, extract, analyze, report.
 * The steps are the same but differ per file format.
 */
public abstract class DataMiner {

    public final void mine(String path) {
        String rawData = extractData(path);
        String parsedData = parseData(rawData);
        String analysis = analyzeData(parsedData);
        sendReport(analysis);
    }

    protected abstract String extractData(String path);
    protected abstract String parseData(String rawData);

    protected String analyzeData(String data) {
        return "Analysis of [" + data + "]: found 42 patterns";
    }

    protected void sendReport(String analysis) {
        System.out.println("Report: " + analysis);
    }
}
