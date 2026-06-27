package com.org.pattern.behavioral.chainofresponsibility;

public class SupportTicket {

    private final int id;
    private final String issue;
    private final int severity;

    public SupportTicket(int id, String issue, int severity) {
        this.id = id;
        this.issue = issue;
        this.severity = severity;
    }

    public int getId() { return id; }
    public String getIssue() { return issue; }
    public int getSeverity() { return severity; }
}
