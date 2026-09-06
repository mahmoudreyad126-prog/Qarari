package com.qarari.app.model;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResult {
    public double income;
    public double costs;
    public double net;
    public double benefitsValue;
    public double trueValue;
    public double hourlyValue;
    public int financialScore;
    public int careerScore;
    public int workLifeScore;
    public int benefitsScore;
    public int riskScore;
    public int qarariScore;
    public List<RiskFlag> risks = new ArrayList<>();
}
