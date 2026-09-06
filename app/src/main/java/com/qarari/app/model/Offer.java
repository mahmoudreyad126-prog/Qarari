package com.qarari.app.model;

public class Offer {
    public long id;
    public String company = "";
    public String role = "";
    public String city = "Riyadh";
    public double basic;
    public double housingAllowance;
    public double transportAllowance;
    public double otherAllowance;
    public double overtime;
    public double annualBonus;
    public double rent;
    public double transportCost;
    public double food;
    public double otherCost;
    public double hoursPerDay = 8;
    public int daysPerWeek = 5;
    public int annualLeave = 21;
    public int commuteMinutes;
    public int growth = 3;
    public int learning = 3;
    public int security = 3;
    public boolean housing;
    public boolean transport;
    public boolean fuel;
    public boolean meals;
    public boolean medical;
    public boolean tickets;
    public boolean currentJob;
    public String notes = "";
    public long createdAt = System.currentTimeMillis();
}
