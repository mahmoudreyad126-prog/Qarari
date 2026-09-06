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
        x.financialWeight = Math.max(0, p.getInt("wf",40)); x.careerWeight = Math.max(0, p.getInt("wc",25));
        x.workLifeWeight = Math.max(0, p.getInt("ww",25)); x.benefitsWeight = Math.max(0, p.getInt("wb",10));
        normalize(x);
        return x;
    }
    public static void save(Context c, Profile x) {
        normalize(x);
        c.getSharedPreferences("qarari_profile", Context.MODE_PRIVATE).edit()
                .putLong("target", Double.doubleToLongBits(x.minimumMonthlyTarget))
                .putInt("wf",x.financialWeight).putInt("wc",x.careerWeight)
                .putInt("ww",x.workLifeWeight).putInt("wb",x.benefitsWeight).apply();
    }

    private static void normalize(Profile x) {
        x.minimumMonthlyTarget = Math.max(0, x.minimumMonthlyTarget);
        int total = x.financialWeight + x.careerWeight + x.workLifeWeight + x.benefitsWeight;
        if (total > 0) return;
        x.financialWeight = 40; x.careerWeight = 25; x.workLifeWeight = 25; x.benefitsWeight = 10;
    }
}
