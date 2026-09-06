package com.qarari.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.qarari.app.data.DatabaseHelper;
import com.qarari.app.data.ProfileStore;
import com.qarari.app.engine.DecisionEngine;
import com.qarari.app.model.AnalysisResult;
import com.qarari.app.model.Offer;
import com.qarari.app.util.Ui;

import java.util.ArrayList;
import java.util.List;

public class ScenarioActivity extends Activity {
    private DatabaseHelper db; private List<Offer> offers; private Spinner spinner; private EditText salary,rent,hours,overtime;
    @Override protected void onCreate(Bundle b){super.onCreate(b);db=new DatabaseHelper(this);build();}
    private void build(){LinearLayout r=Ui.root(this);r.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);r.addView(Ui.title(this,"Scenario Mode"));r.addView(Ui.text(this,"جرّب افتراضات مختلفة بدون تغيير العرض الأصلي.",15));
        offers=db.getOffers(); List<String> names=new ArrayList<>(); for(Offer o:offers)names.add(o.company+" — "+o.role); spinner=new Spinner(this);spinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,names));r.addView(spinner);
        salary=Ui.input(this,"راتب أساسي افتراضي (اتركه فارغًا للأصلي)",true);rent=Ui.input(this,"إيجار افتراضي",true);hours=Ui.input(this,"ساعات عمل افتراضية",true);overtime=Ui.input(this,"أوفر تايم افتراضي",true);r.addView(salary);r.addView(rent);r.addView(hours);r.addView(overtime);
        android.widget.Button run=Ui.button(this,"احسب السيناريو");run.setOnClickListener(v->run());r.addView(run);setContentView(Ui.scroll(this,r));}
    private void run(){if(offers.isEmpty()){Toast.makeText(this,"أضف عرضًا أولًا",Toast.LENGTH_SHORT).show();return;}Offer src=offers.get(spinner.getSelectedItemPosition());Offer o=copy(src);if(has(salary))o.basic=d(salary);if(has(rent))o.rent=d(rent);if(has(hours))o.hoursPerDay=d(hours);if(has(overtime))o.overtime=d(overtime);AnalysisResult base=DecisionEngine.analyze(src,ProfileStore.load(this));AnalysisResult alt=DecisionEngine.analyze(o,ProfileStore.load(this));
        LinearLayout r=Ui.root(this);r.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);r.addView(Ui.title(this,"نتيجة السيناريو"));r.addView(Ui.text(this,src.company+"\nالأصلي: "+base.qarariScore+"/100 — "+Ui.money(base.trueValue)+"\nالسيناريو: "+alt.qarariScore+"/100 — "+Ui.money(alt.trueValue)+"\nالفرق: "+Ui.money(alt.trueValue-base.trueValue),19));android.widget.Button back=Ui.button(this,"سيناريو جديد");back.setOnClickListener(v->build());r.addView(back);setContentView(Ui.scroll(this,r));}
    private boolean has(EditText e){return !e.getText().toString().trim().isEmpty();} private double d(EditText e){try{return Double.parseDouble(e.getText().toString().trim());}catch(Exception x){return 0;}}
    private Offer copy(Offer a){Offer o=new Offer();o.id=a.id;o.company=a.company;o.role=a.role;o.city=a.city;o.basic=a.basic;o.housingAllowance=a.housingAllowance;o.transportAllowance=a.transportAllowance;o.otherAllowance=a.otherAllowance;o.overtime=a.overtime;o.annualBonus=a.annualBonus;o.rent=a.rent;o.transportCost=a.transportCost;o.food=a.food;o.otherCost=a.otherCost;o.hoursPerDay=a.hoursPerDay;o.daysPerWeek=a.daysPerWeek;o.annualLeave=a.annualLeave;o.commuteMinutes=a.commuteMinutes;o.growth=a.growth;o.learning=a.learning;o.security=a.security;o.housing=a.housing;o.transport=a.transport;o.fuel=a.fuel;o.meals=a.meals;o.medical=a.medical;o.tickets=a.tickets;o.currentJob=a.currentJob;return o;}
}
