package com.org.pattern.gangoffour.behavioral.templatemethod;

public class CsvDataMiner extends DataMiner {

    @Override
    protected String extractData(String path) {
        System.out.println("CSV: Reading file " + path);
        return "col1,col2,col3\n1,2,3\n4,5,6";
    }

    @Override
    protected String parseData(String rawData) {
        System.out.println("CSV: Parsing comma-separated data");
        return "parsed-csv-rows";
    }
}
