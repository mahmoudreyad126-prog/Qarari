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
        r.addView(Ui.eyebrow(this,"QARARI FINAL  •  قراري"));
        r.addView(Ui.title(this,"خذ قرارك بثقة"));
        r.addView(Ui.text(this,"حلّل عروضك الوظيفية بالأرقام، واكتشف قيمتها الحقيقية قبل أن تقول نعم.",16));
        Offer current=db.getCurrentJob();
        if(current!=null){ AnalysisResult a= DecisionEngine.analyze(current, ProfileStore.load(this)); LinearLayout hero=Ui.card(this); hero.addView(Ui.eyebrow(this,"ملخص وظيفتك الحالية")); TextView company=Ui.text(this,current.company+"  •  "+current.role,17); company.setTextColor(Ui.INK); company.setTypeface(null,1); hero.addView(company); hero.addView(Ui.score(this,a.qarariScore)); hero.addView(Ui.text(this,DecisionEngine.recommendation(a)+"\nالقيمة الحقيقية: "+Ui.money(a.trueValue),14)); r.addView(hero); }
        else { LinearLayout empty=Ui.card(this); empty.addView(Ui.eyebrow(this,"ابدأ من هنا")); empty.addView(Ui.text(this,"أضف وظيفتك الحالية كخط أساس، ثم قارن أي عرض جديد بها.",15)); r.addView(empty); }
        LinearLayout stats=new LinearLayout(this); stats.setOrientation(LinearLayout.HORIZONTAL); stats.setWeightSum(2); String[] values={String.valueOf(db.getOffers().size()),current==null?"—":Ui.money(DecisionEngine.analyze(current,ProfileStore.load(this)).trueValue)}; String[] labels={"عروض محفوظة","القيمة الحالية"}; for(int i=0;i<2;i++){LinearLayout s=Ui.stat(this,labels[i],values[i]); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1); p.setMargins(0,Ui.dp(this,4),i==0?Ui.dp(this,6):0,Ui.dp(this,4)); stats.addView(s,p);} r.addView(stats);
        android.widget.Button b1=Ui.button(this,"إضافة عرض وظيفي");b1.setOnClickListener(v->go(OfferActivity.class));r.addView(b1);
        r.addView(Ui.section(this,"الوصول السريع"));
        android.widget.Button b2=Ui.secondary(this,"خزنة العروض");b2.setOnClickListener(v->go(VaultActivity.class));r.addView(b2);
        android.widget.Button b3=Ui.secondary(this,"مقارنة العروض");b3.setOnClickListener(v->go(CompareActivity.class));r.addView(b3);
        android.widget.Button b4=Ui.secondary(this,"وضع السيناريو");b4.setOnClickListener(v->go(ScenarioActivity.class));r.addView(b4);
        android.widget.Button b5=Ui.secondary(this,"سجل القرارات");b5.setOnClickListener(v->go(HistoryActivity.class));r.addView(b5);
        r.addView(Ui.section(this,"منهجية قراري"));r.addView(Ui.text(this,"نوازن الجانب المالي، التطور المهني، توازن الحياة، المزايا، والمخاطر في نتيجة واحدة واضحة من 100.",15));
        android.widget.Button b6=Ui.secondary(this,"الملف الشخصي والأولويات");b6.setOnClickListener(v->go(SettingsActivity.class));r.addView(b6);
        r.addView(Ui.nav(this,"الرئيسية"));
        setContentView(Ui.scroll(this,r));
    }
    private void go(Class<?> c){startActivity(new Intent(this,c));}
}
