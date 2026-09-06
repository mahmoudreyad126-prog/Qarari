package com.qarari.app.engine;

import com.qarari.app.model.AnalysisResult;
import com.qarari.app.model.Offer;
import com.qarari.app.model.Profile;
import com.qarari.app.model.RiskFlag;

public final class DecisionEngine {
    private DecisionEngine() {}

    public static AnalysisResult analyze(Offer o, Profile p) {
        AnalysisResult r = new AnalysisResult();

        double cityFactor = cityFactor(o.city);
        r.income = o.basic + o.housingAllowance + o.transportAllowance + o.otherAllowance + o.overtime + (o.annualBonus / 12.0);
        r.costs = (o.rent + o.transportCost + o.food + o.otherCost) * cityFactor;
        r.net = r.income - r.costs;
        r.benefitsValue = benefitValue(o);
        r.trueValue = r.net + r.benefitsValue;
        double monthlyHours = Math.max(1, o.hoursPerDay * o.daysPerWeek * 4.33);
        r.hourlyValue = r.trueValue / monthlyHours;

        double target = Math.max(1000, p.minimumMonthlyTarget);
        r.financialScore = clamp((int)Math.round(50 + ((r.trueValue - target) / target) * 45));
        r.careerScore = clamp((int)Math.round(((o.growth + o.learning + o.security) / 15.0) * 100));

        int work = 100;
        if (o.hoursPerDay > 8) work -= (int)Math.round((o.hoursPerDay - 8) * 9);
        if (o.daysPerWeek > 5) work -= (o.daysPerWeek - 5) * 15;
        if (o.annualLeave < 30) work -= (30 - o.annualLeave);
        if (o.commuteMinutes > 45) work -= Math.min(20, (o.commuteMinutes - 45) / 3);
        r.workLifeScore = clamp(work);

        int benefitCount = 0;
        if (o.housing) benefitCount++;
        if (o.transport) benefitCount++;
        if (o.fuel) benefitCount++;
        if (o.meals) benefitCount++;
        if (o.medical) benefitCount++;
        if (o.tickets) benefitCount++;
        r.benefitsScore = clamp((int)Math.round((benefitCount / 6.0) * 100));

        buildRisks(o, p, r);
        int penalty = 0;
        for (RiskFlag f : r.risks) {
            if ("CRITICAL".equals(f.severity)) penalty += 25;
            else if ("HIGH".equals(f.severity)) penalty += 15;
            else if ("MEDIUM".equals(f.severity)) penalty += 8;
            else penalty += 3;
        }
        r.riskScore = clamp(100 - penalty);

        int totalWeight = Math.max(1, p.financialWeight + p.careerWeight + p.workLifeWeight + p.benefitsWeight);
        double weighted = r.financialScore * p.financialWeight
                + r.careerScore * p.careerWeight
                + r.workLifeScore * p.workLifeWeight
                + r.benefitsScore * p.benefitsWeight;
        int base = (int)Math.round(weighted / totalWeight);
        r.qarariScore = clamp((int)Math.round(base * 0.85 + r.riskScore * 0.15));
        return r;
    }

    private static void buildRisks(Offer o, Profile p, AnalysisResult r) {
        if (o.hoursPerDay >= 11) r.risks.add(new RiskFlag("HIGH", "ساعات عمل مرتفعة", "11 ساعة أو أكثر يوميًا"));
        else if (o.hoursPerDay > 9) r.risks.add(new RiskFlag("MEDIUM", "ساعات عمل طويلة", "أكثر من 9 ساعات يوميًا"));
        if (o.daysPerWeek >= 6) r.risks.add(new RiskFlag("HIGH", "أسبوع عمل مرهق", "6 أيام أو أكثر أسبوعيًا"));
        if (o.annualLeave < 21) r.risks.add(new RiskFlag("HIGH", "إجازة سنوية منخفضة", "أقل من 21 يومًا"));
        if (r.net < 0) r.risks.add(new RiskFlag("CRITICAL", "صافي شهري سلبي", "المصاريف أعلى من الدخل"));
        if (r.trueValue < p.minimumMonthlyTarget) r.risks.add(new RiskFlag("MEDIUM", "أقل من هدفك المالي", "القيمة الحقيقية أقل من الحد الشهري المستهدف"));
        if (!o.medical) r.risks.add(new RiskFlag("MEDIUM", "لا يوجد تأمين طبي", "راجع العرض أو العقد قبل القبول"));
        if (!o.housing && o.rent <= 0) r.risks.add(new RiskFlag("LOW", "تكلفة السكن غير محسوبة", "أضف الإيجار للحصول على نتيجة أدق"));
        if (o.growth <= 2 && o.learning <= 2) r.risks.add(new RiskFlag("MEDIUM", "نمو مهني محدود", "فرص التطور والتعلم منخفضة"));
        if (o.commuteMinutes >= 90) r.risks.add(new RiskFlag("HIGH", "تنقل طويل", "90 دقيقة أو أكثر يوميًا في اتجاه واحد"));
    }

    public static String recommendation(AnalysisResult r) {
        if (r.qarariScore >= 80) return "ممتاز — العرض قوي جدًا وفق بياناتك الحالية";
        if (r.qarariScore >= 68) return "جيد — مناسب مع مراجعة نقاط التفاوض";
        if (r.qarariScore >= 55) return "متوسط — قارن البدائل قبل اتخاذ القرار";
        return "ضعيف — يحتاج تفاوض قوي أو بديل أفضل";
    }

    public static double negotiationGap(AnalysisResult winner, AnalysisResult loser) {
        double gap = Math.max(0, winner.trueValue - loser.trueValue);
        if (gap == 0) return 0;
        return Math.max(300, Math.ceil(gap / 50.0) * 50.0);
    }

    private static double cityFactor(String city) {
        if (city == null) return 1.0;
        String c = city.trim().toLowerCase();
        if (c.contains("riyadh") || c.contains("الرياض")) return 1.15;
        if (c.contains("jeddah") || c.contains("جدة")) return 1.08;
        if (c.contains("dammam") || c.contains("الدمام")) return 1.03;
        return 1.0;
    }

    private static double benefitValue(Offer o) {
        double v = 0;
        if (o.housing) v += 900;
        if (o.transport) v += 500;
        if (o.fuel) v += 250;
        if (o.meals) v += 350;
        if (o.medical) v += 250;
        if (o.tickets) v += 150;
        return v;
    }

    private static int clamp(int v) { return Math.max(0, Math.min(100, v)); }
}
