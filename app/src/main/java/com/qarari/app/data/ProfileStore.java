package com.qarari.app.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.qarari.app.model.Profile;

public final class ProfileStore {
    private ProfileStore() {}
    public static Profile load(Context c) {
        SharedPreferences p = c.getSharedPreferences("qarari_profile", Context.MODE_PRIVATE);
        Profile x = new Profile();
        x.minimumMonthlyTarget = Double.longBitsToDouble(p.getLong("target", Double.doubleToLongBits(5000)));
        x.financialWeight = p.getInt("wf",40); x.careerWeight = p.getInt("wc",25);
        x.workLifeWeight = p.getInt("ww",25); x.benefitsWeight = p.getInt("wb",10);
        return x;
    }
    public static void save(Context c, Profile x) {
        c.getSharedPreferences("qarari_profile", Context.MODE_PRIVATE).edit()
                .putLong("target", Double.doubleToLongBits(x.minimumMonthlyTarget))
                .putInt("wf",x.financialWeight).putInt("wc",x.careerWeight)
                .putInt("ww",x.workLifeWeight).putInt("wb",x.benefitsWeight).apply();
    }
}
