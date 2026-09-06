package com.qarari.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qarari.app.data.DatabaseHelper;
import com.qarari.app.data.ProfileStore;
import com.qarari.app.engine.DecisionEngine;
import com.qarari.app.model.AnalysisResult;
import com.qarari.app.model.Offer;
import com.qarari.app.ui.CompareActivity;
import com.qarari.app.ui.HistoryActivity;
import com.qarari.app.ui.OfferActivity;
import com.qarari.app.ui.ScenarioActivity;
import com.qarari.app.ui.SettingsActivity;
import com.qarari.app.ui.VaultActivity;
import com.qarari.app.util.Ui;

public class MainActivity extends Activity {
    private DatabaseHelper db;
    @Override protected void onCreate(Bundle b){super.onCreate(b);db=new DatabaseHelper(this);build();}
    @Override protected void onResume(){super.onResume();build();}
    private void build(){
        LinearLayout r=Ui.root(this);r.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);
        r.addView(Ui.title(this,"قراري"));r.addView(Ui.text(this,"قرار وظيفي أذكى — بالأرقام، المخاطر، ومستقبلك المهني",16));
        Offer current=db.getCurrentJob();
        if(current!=null){ AnalysisResult a= DecisionEngine.analyze(current, ProfileStore.load(this)); TextView card=Ui.text(this,"وظيفتك الحالية: "+current.company+"\nQarari Score: "+a.qarariScore+"/100\nالقيمة الحقيقية: "+Ui.money(a.trueValue),18); card.setPadding(18,18,18,18); card.setBackgroundColor(0xFFE2E8F0); r.addView(card); }
        else r.addView(Ui.text(this,"لم تُحدد وظيفتك الحالية بعد. يمكنك إضافتها كـ Baseline للمقارنة.",15));
        android.widget.Button b1=Ui.button(this,"+ إضافة عرض وظيفي");b1.setOnClickListener(v->go(OfferActivity.class));r.addView(b1);
        android.widget.Button b2=Ui.button(this,"خزنة العروض Offer Vault");b2.setOnClickListener(v->go(VaultActivity.class));r.addView(b2);
        android.widget.Button b3=Ui.button(this,"مقارنة 2–5 عروض");b3.setOnClickListener(v->go(CompareActivity.class));r.addView(b3);
        android.widget.Button b4=Ui.button(this,"Scenario Mode");b4.setOnClickListener(v->go(ScenarioActivity.class));r.addView(b4);
        android.widget.Button b5=Ui.button(this,"سجل القرارات");b5.setOnClickListener(v->go(HistoryActivity.class));r.addView(b5);
        android.widget.Button b6=Ui.button(this,"الإعدادات والأولويات");b6.setOnClickListener(v->go(SettingsActivity.class));r.addView(b6);
        r.addView(Ui.section(this,"كيف يحسب قراري؟"));r.addView(Ui.text(this,"Financial + Career + Work-Life + Benefits + Risk → Qarari Score",15));
        setContentView(Ui.scroll(this,r));
    }
    private void go(Class<?> c){startActivity(new Intent(this,c));}
}
