package com.qarari.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.qarari.app.data.DatabaseHelper;
import com.qarari.app.data.ProfileStore;
import com.qarari.app.engine.DecisionEngine;
import com.qarari.app.model.AnalysisResult;
import com.qarari.app.model.Offer;
import com.qarari.app.util.PdfReportGenerator;
import com.qarari.app.util.Ui;

import java.io.File;
import java.util.List;

public class VaultActivity extends Activity {
    private DatabaseHelper db;
    @Override protected void onCreate(Bundle b){super.onCreate(b);db=new DatabaseHelper(this);build();}
    @Override protected void onResume(){super.onResume();build();}
    private void build(){
        LinearLayout r=Ui.root(this);r.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);r.addView(Ui.title(this,"Offer Vault"));
        List<Offer> offers=db.getOffers(); if(offers.isEmpty())r.addView(Ui.text(this,"لا توجد عروض محفوظة بعد.",16));
        for(Offer o:offers){ AnalysisResult a= DecisionEngine.analyze(o, ProfileStore.load(this)); LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL);card.setPadding(18,18,18,18);card.setBackgroundColor(o.currentJob?0xFFDCFCE7:0xFFFFFFFF);
            TextView t=Ui.text(this,(o.currentJob?"[وظيفتك الحالية] ":"")+o.company+" — "+o.role+"\n"+o.city+" | Score "+a.qarariScore+"/100 | "+Ui.money(a.trueValue),17);card.addView(t);
            android.widget.Button edit=Ui.button(this,"تعديل");edit.setOnClickListener(v->{Intent i=new Intent(this,OfferActivity.class);i.putExtra("id",o.id);startActivity(i);});card.addView(edit);
            android.widget.Button pdf=Ui.button(this,"إنشاء ومشاركة PDF");pdf.setOnClickListener(v->sharePdf(o));card.addView(pdf);
            android.widget.Button del=Ui.button(this,"حذف العرض");del.setOnClickListener(v->{db.deleteOffer(o.id);Toast.makeText(this,"تم الحذف",Toast.LENGTH_SHORT).show();build();});card.addView(del);
            LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,10,0,10);r.addView(card,p);
        }
        setContentView(Ui.scroll(this,r));
    }
    private void sharePdf(Offer o){try{File f=PdfReportGenerator.generate(this,o,ProfileStore.load(this));android.net.Uri uri= FileProvider.getUriForFile(this,"com.qarari.app.files",f);Intent s=new Intent(Intent.ACTION_SEND);s.setType("application/pdf");s.putExtra(Intent.EXTRA_STREAM,uri);s.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);startActivity(Intent.createChooser(s,"مشاركة تقرير قراري"));}catch(Exception e){Toast.makeText(this,"تعذر إنشاء التقرير: "+e.getMessage(),Toast.LENGTH_LONG).show();}}
}
