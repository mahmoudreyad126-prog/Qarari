package com.qarari.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.AlertDialog;

import com.qarari.app.data.ProfileStore;
import com.qarari.app.model.Profile;
import com.qarari.app.util.Ui;

public class SettingsActivity extends Activity {
    private EditText target,wf,wc,ww,wb;
    @Override protected void onCreate(Bundle b){super.onCreate(b);build();}
    private void build(){Profile p= ProfileStore.load(this);LinearLayout r=Ui.root(this);r.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);r.addView(Ui.eyebrow(this,"ملفك الشخصي"));r.addView(Ui.title(this,"الإعدادات والأولويات"));r.addView(Ui.text(this,"حدد ما يهمك في القرار. يتم تطبيع الأوزان تلقائيًا مع الحفاظ على توازن النتيجة.",15));
        target=Ui.input(this,"الحد الشهري المستهدف",true);wf=Ui.input(this,"وزن الجانب المالي",true);wc=Ui.input(this,"وزن التطور المهني",true);ww=Ui.input(this,"وزن توازن الحياة",true);wb=Ui.input(this,"وزن المزايا",true);
        target.setText(String.valueOf(p.minimumMonthlyTarget));wf.setText(String.valueOf(p.financialWeight));wc.setText(String.valueOf(p.careerWeight));ww.setText(String.valueOf(p.workLifeWeight));wb.setText(String.valueOf(p.benefitsWeight));r.addView(target);r.addView(wf);r.addView(wc);r.addView(ww);r.addView(wb);
        android.widget.Button save=Ui.button(this,"حفظ الأولويات");save.setOnClickListener(v->{Profile x=new Profile();x.minimumMonthlyTarget=d(target,5000);x.financialWeight=i(wf,40);x.careerWeight=i(wc,25);x.workLifeWeight=i(ww,25);x.benefitsWeight=i(wb,10);ProfileStore.save(this,x);Toast.makeText(this,"تم الحفظ",Toast.LENGTH_SHORT).show();finish();});r.addView(save);
        android.widget.Button reset=Ui.secondary(this,"مسح البيانات المحلية");reset.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("مسح كل البيانات؟").setMessage("سيتم حذف العروض وسجل القرارات من هذا الجهاز.").setNegativeButton("إلغاء",null).setPositiveButton("مسح",(d,w)->{new com.qarari.app.data.DatabaseHelper(this).getWritableDatabase().delete("offers",null,null);new com.qarari.app.data.DatabaseHelper(this).getWritableDatabase().delete("history",null,null);Toast.makeText(this,"تم مسح البيانات",Toast.LENGTH_SHORT).show();}).show());r.addView(reset);setContentView(Ui.scroll(this,r));}
    private int i(EditText e,int d){try{return Math.max(0,Integer.parseInt(e.getText().toString()));}catch(Exception x){return d;}}private double d(EditText e,double d){try{return Math.max(0,Double.parseDouble(e.getText().toString()));}catch(Exception x){return d;}}
}
