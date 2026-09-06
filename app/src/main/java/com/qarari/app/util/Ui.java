package com.qarari.app.util;

import android.content.Context;
import android.graphics.Typeface;
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
    public static int dp(Context c, int v){ return Math.round(v*c.getResources().getDisplayMetrics().density); }

    public static LinearLayout root(Context c) {
        LinearLayout l=new LinearLayout(c); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(dp(c,18),dp(c,18),dp(c,18),dp(c,28));
        l.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return l;
    }
    public static ScrollView scroll(Context c, View child){ ScrollView s=new ScrollView(c); s.addView(child); return s; }
    public static TextView title(Context c,String t){ TextView v=text(c,t,26); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); v.setTextColor(0xFF0F172A); v.setGravity(Gravity.RIGHT); return v; }
    public static TextView section(Context c,String t){ TextView v=text(c,t,18); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); v.setPadding(0,dp(c,18),0,dp(c,8)); v.setTextColor(0xFF0F172A); return v; }
    public static TextView text(Context c,String t,int sp){ TextView v=new TextView(c); v.setText(t); v.setTextSize(sp); v.setTextColor(0xFF334155); v.setPadding(0,dp(c,5),0,dp(c,5)); v.setGravity(Gravity.RIGHT); return v; }
    public static EditText input(Context c,String hint, boolean number){ EditText e=new EditText(c); e.setHint(hint); e.setTextSize(16); e.setGravity(Gravity.RIGHT); e.setPadding(dp(c,12),dp(c,10),dp(c,12),dp(c,10)); if(number)e.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL); return e; }
    public static Button button(Context c,String t){ Button b=new Button(c); b.setText(t); b.setTextSize(16); b.setAllCaps(false); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp(c,52)); p.setMargins(0,dp(c,7),0,dp(c,7)); b.setLayoutParams(p); return b; }
    public static String money(double v){ return String.format(java.util.Locale.US,"%,.0f ر.س",v); }
}
