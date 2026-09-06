package com.qarari.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qarari.app.data.DatabaseHelper;
import com.qarari.app.data.ProfileStore;
import com.qarari.app.engine.DecisionEngine;
import com.qarari.app.model.AnalysisResult;
import com.qarari.app.model.Offer;
import com.qarari.app.model.Profile;
import com.qarari.app.model.RiskFlag;
import com.qarari.app.util.Ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CompareActivity extends Activity {
    private DatabaseHelper db;
    private List<Offer> offers;
    private final List<CheckBox> checks = new ArrayList<>();

    @Override protected void onCreate(Bundle b){ super.onCreate(b); db=new DatabaseHelper(this); buildPicker(); }

    private void buildPicker(){
        LinearLayout r=Ui.root(this); r.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL); r.addView(Ui.title(this,"مقارنة العروض")); r.addView(Ui.text(this,"اختر من عرضين إلى 5 عروض. وظيفتك الحالية يمكن إدخالها ضمن المقارنة.",15));
        offers=db.getOffers(); checks.clear();
        for(Offer o:offers){ CheckBox c=new CheckBox(this); c.setText((o.currentJob?"[الحالي] ":"")+o.company+" — "+o.role); c.setTextSize(17); c.setPadding(0,10,0,10); checks.add(c); r.addView(c); }
        android.widget.Button go=Ui.button(this,"حلّل وقارن"); go.setOnClickListener(v->compare()); r.addView(go);
        setContentView(Ui.scroll(this,r));
    }

    private void compare(){
        List<Offer> selected=new ArrayList<>(); for(int i=0;i<checks.size();i++) if(checks.get(i).isChecked()) selected.add(offers.get(i));
        if(selected.size()<2 || selected.size()>5){Toast.makeText(this,"اختار من 2 إلى 5 عروض",Toast.LENGTH_SHORT).show();return;}
        Profile p=ProfileStore.load(this);
        List<Row> rows=new ArrayList<>(); for(Offer o:selected)rows.add(new Row(o,DecisionEngine.analyze(o,p)));
        rows.sort(Comparator.comparingInt((Row x)->x.a.qarariScore).reversed()); Row winner=rows.get(0); Row runner=rows.size()>1?rows.get(1):winner;
        double gap=DecisionEngine.negotiationGap(winner.a,runner.a);
        StringBuilder hist=new StringBuilder("الفائز: ").append(winner.o.company).append(" (Score ").append(winner.a.qarariScore).append(") | مقارنة مع: ");
        for(Row x:rows)hist.append(x.o.company).append(" "); db.addHistory(hist.toString());

        LinearLayout r=Ui.root(this); r.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL); r.addView(Ui.title(this,"نتيجة المقارنة"));
        r.addView(Ui.text(this,"🏆 الأفضل: "+winner.o.company+"\n"+DecisionEngine.recommendation(winner.a),20));
        for(Row x:rows){
            StringBuilder s=new StringBuilder();
            s.append(x.o.company).append(" — ").append(x.o.role).append("\n")
             .append("Qarari Score: ").append(x.a.qarariScore).append("/100\n")
             .append("القيمة الحقيقية: ").append(Ui.money(x.a.trueValue)).append("\n")
             .append("قيمة الساعة: ").append(Ui.money(x.a.hourlyValue)).append("\n")
             .append("Financial ").append(x.a.financialScore).append(" | Career ").append(x.a.careerScore)
             .append(" | Work-Life ").append(x.a.workLifeScore).append(" | Benefits ").append(x.a.benefitsScore).append("\n")
             .append("المخاطر: ").append(x.a.risks.size());
            r.addView(Ui.section(this,s.toString()));
            for(RiskFlag f:x.a.risks) r.addView(Ui.text(this,"• "+f.severity+" — "+f.title+": "+f.detail,14));
        }
        r.addView(Ui.section(this,"Negotiation Assistant"));
        if(gap>0)r.addView(Ui.text(this,"لو أنت متمسك بعرض "+runner.o.company+"، حاول تفاوض على زيادة تقريبية "+Ui.money(gap)+" شهريًا أو مزايا تعادلها للوصول لقيمة العرض الأفضل.",16));
        else r.addView(Ui.text(this,"القيمة الحقيقية للعروض متقاربة؛ ركز في التفاوض على ساعات العمل، الإجازات، التأمين والنمو المهني.",16));
        android.widget.Button back=Ui.button(this,"اختيار عروض أخرى"); back.setOnClickListener(v->buildPicker()); r.addView(back);
        setContentView(Ui.scroll(this,r));
    }

    private static class Row { Offer o; AnalysisResult a; Row(Offer o,AnalysisResult a){this.o=o;this.a=a;} }
}
