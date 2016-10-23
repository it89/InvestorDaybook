package com.github.it89.investordiary.stockmarket;

/**
 * Created by Axel on 15.10.2016.
 */
public enum  CashFlowType {
    INCOME(false),
    INVESTMENT(false),
    COMMISSION_REPO(false),
    DIVIDEND(true),
    ACCUMULATED_COUPON_YIELD_PAYMENT(true),
    TRADE(false),
    COMMISSION_TRADE(false),
    ACCUMULATED_COUPON_YIELD(false);

    private final boolean isAssetIncome;

    CashFlowType(boolean isAssetIncome) {
        this.isAssetIncome = isAssetIncome;
    }

    public boolean isAssetIncome() {
        return isAssetIncome;
    }

    //    public static HashSet<String> getStringHashSet() {
//        CashFlowType[] values = CashFlowType.values();
//        HashSet<String> set = new HashSet<String>();
//        for(int i = 0; i < values.length; i++) {
//            set.add(values[i].name());
//        }
//        return set;
//    }
}
