package com.sjoqvist.wigell_mc_rental.lib;

public final class MoneyConverter {
    public static final double exchangeRateGbp = 0.08;

    public static double sekToGbp(double sek) {
        return sek * exchangeRateGbp;
    }
}
