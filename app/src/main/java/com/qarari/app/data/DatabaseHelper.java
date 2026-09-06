package com.qarari.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qarari.app.model.Offer;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB = "qarari.db";
    private static final int VERSION = 2;

    public DatabaseHelper(Context context) { super(context, DB, null, VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE offers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "company TEXT, role TEXT, city TEXT," +
                "basic REAL, housingAllowance REAL, transportAllowance REAL, otherAllowance REAL, overtime REAL, annualBonus REAL," +
                "rent REAL, transportCost REAL, food REAL, otherCost REAL," +
                "hoursPerDay REAL, daysPerWeek INTEGER, annualLeave INTEGER, commuteMinutes INTEGER," +
                "growth INTEGER, learning INTEGER, security INTEGER," +
                "housing INTEGER, transport INTEGER, fuel INTEGER, meals INTEGER, medical INTEGER, tickets INTEGER," +
                "currentJob INTEGER, notes TEXT, createdAt INTEGER)");
        db.execSQL("CREATE TABLE history (id INTEGER PRIMARY KEY AUTOINCREMENT, createdAt INTEGER, summary TEXT)");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS history (id INTEGER PRIMARY KEY AUTOINCREMENT, createdAt INTEGER, summary TEXT)");
        }
    }

    public long saveOffer(Offer o) {
        SQLiteDatabase db = getWritableDatabase();
        if (o.currentJob) {
            ContentValues reset = new ContentValues();
            reset.put("currentJob", 0);
            db.update("offers", reset, "currentJob=1", null);
        }
        ContentValues v = values(o);
        if (o.id > 0) {
            db.update("offers", v, "id=?", new String[]{String.valueOf(o.id)});
            return o.id;
        }
        return db.insert("offers", null, v);
    }

    public void deleteOffer(long id) {
        getWritableDatabase().delete("offers", "id=?", new String[]{String.valueOf(id)});
    }

    public Offer getOffer(long id) {
        Cursor c = getReadableDatabase().query("offers", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        try { return c.moveToFirst() ? from(c) : null; } finally { c.close(); }
    }

    public List<Offer> getOffers() {
        List<Offer> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query("offers", null, null, null, null, null, "currentJob DESC, createdAt DESC");
        try { while (c.moveToNext()) list.add(from(c)); } finally { c.close(); }
        return list;
    }

    public Offer getCurrentJob() {
        Cursor c = getReadableDatabase().query("offers", null, "currentJob=1", null, null, null, "createdAt DESC", "1");
        try { return c.moveToFirst() ? from(c) : null; } finally { c.close(); }
    }

    public void addHistory(String summary) {
        ContentValues v = new ContentValues();
        v.put("createdAt", System.currentTimeMillis());
        v.put("summary", summary);
        getWritableDatabase().insert("history", null, v);
    }

    public List<String> getHistory() {
        List<String> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query("history", new String[]{"createdAt","summary"}, null, null, null, null, "createdAt DESC", "50");
        try {
            while (c.moveToNext()) list.add(c.getLong(0) + "|" + c.getString(1));
        } finally { c.close(); }
        return list;
    }

    private ContentValues values(Offer o) {
        ContentValues v = new ContentValues();
        v.put("company", o.company); v.put("role", o.role); v.put("city", o.city);
        v.put("basic", o.basic); v.put("housingAllowance", o.housingAllowance); v.put("transportAllowance", o.transportAllowance);
        v.put("otherAllowance", o.otherAllowance); v.put("overtime", o.overtime); v.put("annualBonus", o.annualBonus);
        v.put("rent", o.rent); v.put("transportCost", o.transportCost); v.put("food", o.food); v.put("otherCost", o.otherCost);
        v.put("hoursPerDay", o.hoursPerDay); v.put("daysPerWeek", o.daysPerWeek); v.put("annualLeave", o.annualLeave); v.put("commuteMinutes", o.commuteMinutes);
        v.put("growth", o.growth); v.put("learning", o.learning); v.put("security", o.security);
        v.put("housing", b(o.housing)); v.put("transport", b(o.transport)); v.put("fuel", b(o.fuel)); v.put("meals", b(o.meals));
        v.put("medical", b(o.medical)); v.put("tickets", b(o.tickets)); v.put("currentJob", b(o.currentJob));
        v.put("notes", o.notes); v.put("createdAt", o.createdAt);
        return v;
    }

    private Offer from(Cursor c) {
        Offer o = new Offer();
        o.id = l(c,"id"); o.company = s(c,"company"); o.role = s(c,"role"); o.city = s(c,"city");
        o.basic = d(c,"basic"); o.housingAllowance = d(c,"housingAllowance"); o.transportAllowance = d(c,"transportAllowance");
        o.otherAllowance = d(c,"otherAllowance"); o.overtime = d(c,"overtime"); o.annualBonus = d(c,"annualBonus");
        o.rent = d(c,"rent"); o.transportCost = d(c,"transportCost"); o.food = d(c,"food"); o.otherCost = d(c,"otherCost");
        o.hoursPerDay = d(c,"hoursPerDay"); o.daysPerWeek = i(c,"daysPerWeek"); o.annualLeave = i(c,"annualLeave"); o.commuteMinutes = i(c,"commuteMinutes");
        o.growth = i(c,"growth"); o.learning = i(c,"learning"); o.security = i(c,"security");
        o.housing = z(c,"housing"); o.transport = z(c,"transport"); o.fuel = z(c,"fuel"); o.meals = z(c,"meals"); o.medical = z(c,"medical"); o.tickets = z(c,"tickets");
        o.currentJob = z(c,"currentJob"); o.notes = s(c,"notes"); o.createdAt = l(c,"createdAt");
        return o;
    }

    private int idx(Cursor c,String n){return c.getColumnIndexOrThrow(n);} private String s(Cursor c,String n){return c.getString(idx(c,n));}
    private int i(Cursor c,String n){return c.getInt(idx(c,n));} private long l(Cursor c,String n){return c.getLong(idx(c,n));}
    private double d(Cursor c,String n){return c.getDouble(idx(c,n));} private boolean z(Cursor c,String n){return c.getInt(idx(c,n))==1;}
    private int b(boolean v){return v?1:0;}
}
