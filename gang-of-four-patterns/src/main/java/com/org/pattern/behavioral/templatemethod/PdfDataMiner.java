package com.org.pattern.behavioral.templatemethod;

public class PdfDataMiner extends DataMiner {

    @Override
    protected String extractData(String path) {
        System.out.println("PDF: Extracting text from " + path);
        return "%PDF-1.4 ...binary content...";
    }

    @Override
    protected String parseData(String rawData) {
        System.out.println("PDF: Parsing PDF structure");
        return "parsed-pdf-text";
    }

    @Override
    protected void sendReport(String analysis) {
        System.out.println("PDF Report (emailed): " + analysis);
    }

    public static void demo() {
        System.out.println("=== Template Method Pattern Demo ===");
        DataMiner csvMiner = new CsvDataMiner();
        csvMiner.mine("data.csv");

        System.out.println();
        DataMiner pdfMiner = new PdfDataMiner();
        pdfMiner.mine("report.pdf");
    }
}
