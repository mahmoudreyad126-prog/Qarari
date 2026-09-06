package com.qarari.app.model;

public class RiskFlag {
    public final String severity;
    public final String title;
    public final String detail;

    public RiskFlag(String severity, String title, String detail) {
        this.severity = severity;
        this.title = title;
        this.detail = detail;
    }
}
