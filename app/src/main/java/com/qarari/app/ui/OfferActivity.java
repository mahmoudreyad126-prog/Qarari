package com.qarari.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qarari.app.data.DatabaseHelper;
import com.qarari.app.model.Offer;
import com.qarari.app.util.Ui;

public class OfferActivity extends Activity {
    private final EditText[] f = new EditText[20];
    private CheckBox housing,transport,fuel,meals,medical,tickets,current;
    private DatabaseHelper db; private Offer offer;

    @Override protected void onCreate(Bundle b){ super.onCreate(b); db=new DatabaseHelper(this); long id=getIntent().getLongExtra("id",0); offer=id>0?db.getOffer(id):new Offer(); if(offer==null)offer=new Offer(); build(); }

    private void build(){
        LinearLayout r=Ui.root(this); r.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL); r.addView(Ui.title(this, offer.id>0?"تعديل العرض":"إضافة عرض جديد"));
        String[] hints={"اسم الشركة","المسمى الوظيفي","المدينة","الراتب الأساسي","بدل السكن","بدل النقل","بدلات أخرى","أوفر تايم شهري","بونص سنوي","الإيجار الشهري","تكلفة المواصلات","الأكل والمعيشة","مصاريف أخرى","ساعات العمل يوميًا","أيام العمل أسبوعيًا","الإجازة السنوية","مدة التنقل بالدقائق","النمو المهني 1-5","التعلم 1-5","الأمان الوظيفي 1-5"};
        for(int i=0;i<hints.length;i++){ f[i]=Ui.input(this,hints[i],i>=3); r.addView(f[i]); }
        r.addView(Ui.section(this,"المزايا")); housing=cb("سكن من الشركة"); transport=cb("سيارة / مواصلات"); fuel=cb("وقود"); meals=cb("وجبات"); medical=cb("تأمين طبي"); tickets=cb("تذاكر سفر"); current=cb("هذه وظيفتي الحالية (Baseline)");
        r.addView(housing);r.addView(transport);r.addView(fuel);r.addView(meals);r.addView(medical);r.addView(tickets);r.addView(current);
        android.widget.Button save=Ui.button(this,"حفظ وتحليل العرض"); save.setOnClickListener(v->save()); r.addView(save); fill(); setContentView(Ui.scroll(this,r));
    }
    private CheckBox cb(String s){ CheckBox c=new CheckBox(this); c.setText(s); c.setTextSize(16); c.setPadding(0,8,0,8); return c; }
    private void fill(){ String[] a={offer.company,offer.role,offer.city,n(offer.basic),n(offer.housingAllowance),n(offer.transportAllowance),n(offer.otherAllowance),n(offer.overtime),n(offer.annualBonus),n(offer.rent),n(offer.transportCost),n(offer.food),n(offer.otherCost),n(offer.hoursPerDay),String.valueOf(offer.daysPerWeek),String.valueOf(offer.annualLeave),String.valueOf(offer.commuteMinutes),String.valueOf(offer.growth),String.valueOf(offer.learning),String.valueOf(offer.security)}; for(int i=0;i<a.length;i++)f[i].setText(a[i]); housing.setChecked(offer.housing);transport.setChecked(offer.transport);fuel.setChecked(offer.fuel);meals.setChecked(offer.meals);medical.setChecked(offer.medical);tickets.setChecked(offer.tickets);current.setChecked(offer.currentJob); }
    private String n(double d){return d==0?"":String.valueOf(d);}
    private double d(int i){try{return Double.parseDouble(f[i].getText().toString().trim());}catch(Exception e){return 0;}}
    private int x(int i,int def){try{return Integer.parseInt(f[i].getText().toString().trim());}catch(Exception e){return def;}}
    private void save(){ offer.company=f[0].getText().toString().trim(); if(offer.company.isEmpty()){Toast.makeText(this,"اكتب اسم الشركة",Toast.LENGTH_SHORT).show();return;} offer.role=f[1].getText().toString().trim(); offer.city=f[2].getText().toString().trim(); if(offer.city.isEmpty())offer.city="Riyadh";
        offer.basic=d(3);offer.housingAllowance=d(4);offer.transportAllowance=d(5);offer.otherAllowance=d(6);offer.overtime=d(7);offer.annualBonus=d(8);offer.rent=d(9);offer.transportCost=d(10);offer.food=d(11);offer.otherCost=d(12);offer.hoursPerDay=d(13);if(offer.hoursPerDay<=0)offer.hoursPerDay=8;offer.daysPerWeek=x(14,5);offer.annualLeave=x(15,21);offer.commuteMinutes=x(16,0);offer.growth=bound(x(17,3));offer.learning=bound(x(18,3));offer.security=bound(x(19,3)); offer.housing=housing.isChecked();offer.transport=transport.isChecked();offer.fuel=fuel.isChecked();offer.meals=meals.isChecked();offer.medical=medical.isChecked();offer.tickets=tickets.isChecked();offer.currentJob=current.isChecked(); offer.id=db.saveOffer(offer); Toast.makeText(this,"تم حفظ العرض",Toast.LENGTH_SHORT).show(); finish(); }
    private int bound(int v){return Math.max(1,Math.min(5,v));}
}
