package com.qarari.app.util;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public final class Ui {
    private Ui() {}
    public static final int NAVY = 0xFF0B1F33;
    public static final int INK = 0xFF172B3A;
    public static final int MUTED = 0xFF647684;
    public static final int SURFACE = 0xFFF5F8FA;
    public static final int BORDER = 0xFFDCE5EA;
    public static final int EMERALD = 0xFF087F5B;
    public static final int AMBER = 0xFFD9822B;
    public static final int RED = 0xFFC0392B;

    public static int dp(Context c, int v){ return Math.round(v*c.getResources().getDisplayMetrics().density); }

    public static LinearLayout root(Context c) {
        LinearLayout l=new LinearLayout(c); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(dp(c,18),dp(c,18),dp(c,18),dp(c,28));
        l.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        l.setBackgroundColor(SURFACE);
        return l;
    }
    public static ScrollView scroll(Context c, View child){ ScrollView s=new ScrollView(c); s.setFillViewport(true); s.setBackgroundColor(SURFACE); s.addView(child); return s; }
    public static TextView title(Context c,String t){ TextView v=text(c,t,28); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); v.setTextColor(INK); v.setPadding(0,0,0,dp(c,8)); return v; }
    public static TextView eyebrow(Context c,String t){ TextView v=text(c,t,12); v.setTextColor(EMERALD); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); v.setPadding(0,0,0,dp(c,4)); return v; }
    public static TextView section(Context c,String t){ TextView v=text(c,t,18); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); v.setPadding(0,dp(c,18),0,dp(c,8)); v.setTextColor(INK); return v; }
    public static TextView text(Context c,String t,int sp){ TextView v=new TextView(c); v.setText(t); v.setTextSize(sp); v.setTextColor(MUTED); v.setPadding(0,dp(c,5),0,dp(c,5)); v.setGravity(Gravity.RIGHT); v.setLineSpacing(0,1.12f); return v; }
    public static TextView label(Context c,String t){ TextView v=text(c,t,12); v.setTextColor(MUTED); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); return v; }
    public static TextView score(Context c,int value){ TextView v=text(c,value+"/100",30); v.setTextColor(EMERALD); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); v.setGravity(Gravity.CENTER); return v; }
    public static EditText input(Context c,String hint, boolean number){ EditText e=new EditText(c); e.setHint(hint); e.setTextSize(16); e.setTextColor(INK); e.setHintTextColor(0xFF91A0AA); e.setGravity(Gravity.RIGHT); e.setPadding(dp(c,14),dp(c,10),dp(c,14),dp(c,10)); e.setSingleLine(true); e.setBackground(round(c,0xFFFFFFFF,BORDER,10)); if(number)e.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp(c,52)); p.setMargins(0,dp(c,4),0,dp(c,7)); e.setLayoutParams(p); return e; }
    public static Button button(Context c,String t){ return action(c,t,EMERALD,0xFFFFFFFF); }
    public static Button secondary(Context c,String t){ return action(c,t,0xFFFFFFFF,INK); }
    public static LinearLayout card(Context c){ LinearLayout l=new LinearLayout(c); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(dp(c,16),dp(c,15),dp(c,16),dp(c,15)); l.setBackground(round(c,0xFFFFFFFF,BORDER,14)); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT); p.setMargins(0,dp(c,6),0,dp(c,6)); l.setLayoutParams(p); return l; }
    public static LinearLayout stat(Context c,String label,String value){ LinearLayout l=card(c); l.setPadding(dp(c,13),dp(c,12),dp(c,13),dp(c,12)); TextView number=text(c,value,21); number.setTextColor(INK); number.setTypeface(Typeface.DEFAULT,Typeface.BOLD); l.addView(number); l.addView(label(c,label)); return l; }
    public static TextView badge(Context c,String label,int color){ TextView v=text(c,label,12); v.setTextColor(color); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); v.setGravity(Gravity.CENTER); v.setPadding(dp(c,10),dp(c,4),dp(c,10),dp(c,4)); v.setBackground(round(c,0xFFFFFFFF,color,20)); return v; }
    public static LinearLayout nav(Context c, String active){ LinearLayout n=new LinearLayout(c); n.setGravity(Gravity.CENTER); n.setPadding(0,dp(c,6),0,dp(c,5)); n.setBackground(round(c,0xFFFFFFFF,BORDER,18)); String[] labels={"الرئيسية","العروض","مقارنة","السجل","حسابي"}; for(String label:labels){ TextView item=text(c,label,11); item.setGravity(Gravity.CENTER); item.setTextColor(label.equals(active)?EMERALD:MUTED); item.setTypeface(Typeface.DEFAULT, label.equals(active)?Typeface.BOLD:Typeface.NORMAL); n.addView(item,new LinearLayout.LayoutParams(0,dp(c,48),1)); } return n; }
    public static String money(double v){ return String.format(java.util.Locale.US,"%,.0f ر.س",v); }

    private static Button action(Context c,String t,int background,int foreground){ Button b=new Button(c); b.setText(t); b.setTextSize(15); b.setTextColor(foreground); b.setAllCaps(false); b.setTypeface(Typeface.DEFAULT,Typeface.BOLD); b.setGravity(Gravity.CENTER); b.setPadding(dp(c,12),0,dp(c,12),0); b.setBackground(round(c,background,background==0xFFFFFFFF?BORDER:background,12)); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp(c,50)); p.setMargins(0,dp(c,5),0,dp(c,5)); b.setLayoutParams(p); return b; }
    private static GradientDrawable round(Context c,int color,int stroke,int radius){ GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(c,radius)); if(stroke!=color)d.setStroke(dp(c,1),stroke); return d; }
}
