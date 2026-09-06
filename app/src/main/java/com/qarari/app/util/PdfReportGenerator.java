package com.qarari.app.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

import com.qarari.app.engine.DecisionEngine;
import com.qarari.app.model.AnalysisResult;
import com.qarari.app.model.Offer;
import com.qarari.app.model.Profile;
import com.qarari.app.model.RiskFlag;

import java.io.File;
import java.io.FileOutputStream;

public final class PdfReportGenerator {
    private PdfReportGenerator() {}

    public static File generate(Context context, Offer o, Profile p) throws Exception {
        AnalysisResult r = DecisionEngine.analyze(o,p);
        PdfDocument doc = new PdfDocument();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(595,842,1).create();
        PdfDocument.Page page = doc.startPage(info);
        Canvas c = page.getCanvas(); Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF0F172A); paint.setTextSize(24); paint.setFakeBoldText(true);
        c.drawText("QARARI - Job Offer Report",40,55,paint);
        paint.setFakeBoldText(false); paint.setTextSize(14); int y=90;
        y=line(c,paint,"Company: "+o.company,y); y=line(c,paint,"Role: "+o.role,y); y=line(c,paint,"City: "+o.city,y);
        y+=12; paint.setFakeBoldText(true); y=line(c,paint,"Qarari Score: "+r.qarariScore+"/100",y); paint.setFakeBoldText(false);
        y=line(c,paint,"Recommendation: "+DecisionEngine.recommendation(r),y);
        y+=12; y=line(c,paint,"Monthly income: "+Ui.money(r.income),y); y=line(c,paint,"Monthly costs: "+Ui.money(r.costs),y);
        y=line(c,paint,"True monthly value: "+Ui.money(r.trueValue),y); y=line(c,paint,"Hourly value: "+Ui.money(r.hourlyValue),y);
        y+=12; y=line(c,paint,"Financial: "+r.financialScore+" | Career: "+r.careerScore+" | Work-Life: "+r.workLifeScore+" | Benefits: "+r.benefitsScore,y);
        y+=18; paint.setFakeBoldText(true); y=line(c,paint,"Risk flags",y); paint.setFakeBoldText(false);
        if(r.risks.isEmpty()) y=line(c,paint,"No major flags detected.",y);
        else for(RiskFlag f:r.risks){ y=line(c,paint,"- ["+f.severity+"] "+f.title+": "+f.detail,y); if(y>790) break; }
        doc.finishPage(page);
        File dir = new File(context.getCacheDir(),"reports"); if(!dir.exists()) dir.mkdirs();
        File out = new File(dir,"Qarari_Report_"+System.currentTimeMillis()+".pdf");
        try(FileOutputStream fos=new FileOutputStream(out)){ doc.writeTo(fos); } finally { doc.close(); }
        return out;
    }

    private static int line(Canvas c, Paint p, String s, int y){ c.drawText(s,40,y,p); return y+24; }
}
