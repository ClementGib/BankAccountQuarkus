package com.cdx.bas.domain.utils;

import com.cdx.bas.domain.error.CurrencyException;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateUtils {
    public static String PIVOT_CURRENCY = "EUR";

    /**
     * Euro Exchange Rates for each currency
     *
     * @param currency key to find rate
     * @return euro exchange rate corresponding to the currency
     */
    public static Double getEuroExchangeRate(String currency) throws CurrencyException {
        if (currencyEuroExchangeRates.containsKey(currency)) {
            return currencyEuroExchangeRates.get(currency);
        } else {
            throw new CurrencyException("No exchange rate found for currency: " + currency);
        }
    }

    public static boolean hasCurrency(String currency) {
        return ExchangeRateUtils.currencyEuroExchangeRates.containsKey(currency);
    }

    /**
     * Map currency exchange rates for pivot currency EURO
     */
    private static final Map<String, Double> currencyEuroExchangeRates = new HashMap<String, Double>() {
        {
            put("USD", 1.0745);
            put("JPY", 158.18);
            put("BGN", 1.9558);
            put("CZK", 24.217);
            put("DKK", 7.4563);
            put("GBP", 0.85503);
            put("HUF", 389.15);
            put("PLN", 4.501);
            put("RON", 4.9603);
            put("SEK", 11.9105);
            put("CHF", 0.9561);
            put("ISK", 143.9);
            put("NOK", 11.492);
            put("TRY", 28.772);
            put("AUD", 1.6801);
            put("BRL", 5.3502);
            put("CAD", 1.4659);
            put("CNY", 7.8476);
            put("HKD", 8.4261);
            put("IDR", 16431.79);
            put("ILS", 4.0902);
            put("INR", 89.341);
            put("KRW", 1430.83);
            put("MXN", 18.8455);
            put("MYR", 5.0222);
            put("NZD", 1.8224);
            put("PHP", 61.257);
            put("SGD", 1.4624);
            put("THB", 38.166);
            put("ZAR", 20.6702);
        }
    };
}
