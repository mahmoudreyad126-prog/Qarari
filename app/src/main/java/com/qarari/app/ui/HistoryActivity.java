package com.qarari.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.qarari.app.data.DatabaseHelper;
import com.qarari.app.util.Ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends Activity {
    @Override protected void onCreate(Bundle b){super.onCreate(b);DatabaseHelper db=new DatabaseHelper(this);LinearLayout r=Ui.root(this);r.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);r.addView(Ui.title(this,"سجل القرارات"));List<String> h=db.getHistory();if(h.isEmpty())r.addView(Ui.text(this,"لسه مفيش مقارنات محفوظة.",16));SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);for(String x:h){int p=x.indexOf('|');long t=Long.parseLong(x.substring(0,p));String s=x.substring(p+1);r.addView(Ui.section(this,f.format(new Date(t))));r.addView(Ui.text(this,s,16));}setContentView(Ui.scroll(this,r));}
}
