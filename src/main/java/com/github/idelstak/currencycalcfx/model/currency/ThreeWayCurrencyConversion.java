/*
 * MIT License
 *
 * Copyright (c) 2023 Hiram K
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.idelstak.currencycalcfx.model.currency;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides functionality for converting currencies using exchange rates.
 * Supports direct conversions as well as conversions that require an
 * intermediate currency. Exchange rates are represented as a Map of Maps, where
 * the outer map represents the source currency, the inner map represents the
 * target currency, and the value represents the conversion rate.
 * <p>
 * Provides a method to convert a given amount from one currency to another. If
 * a direct conversion rate is available, it is used to perform the conversion.
 * If a direct conversion rate is not available, a fallback rate is used, which
 * involves using an intermediate currency.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * // Create a ThreeWayCurrencyConversion object with exchange rates
 * Map<Currency, Map<Currency, BigDecimal>> rates = new HashMap<>();
 * rates.put(Currency.USD, new HashMap<>() {{
 *     put(Currency.EUR, BigDecimal.valueOf(0.82));
 *     put(Currency.GBP, BigDecimal.valueOf(0.72));
 * }});
 * rates.put(Currency.EUR, new HashMap<>() {{
 *     put(Currency.USD, BigDecimal.valueOf(1.22));
 *     put(Currency.GBP, BigDecimal.valueOf(0.87));
 * }});
 * rates.put(Currency.GBP, new HashMap<>() {{
 *     put(Currency.USD, BigDecimal.valueOf(1.39));
 *     put(Currency.EUR, BigDecimal.valueOf(1.15));
 * }});
 * ThreeWayCurrencyConversion converter = new ThreeWayCurrencyConversion(rates);
 *
 * // Convert 100 USD to EUR
 * BigDecimal amount = BigDecimal.valueOf(100);
 * Currency fromCurrency = Currency.USD;
 * Currency toCurrency = Currency.EUR;
 * BigDecimal result = converter.convert(fromCurrency, toCurrency, amount);
 * System.out.println(amount + " " + fromCurrency + " = " + result + " " + toCurrency);
 * // Output: 100 USD = 82 EUR
 * }
 * </pre>
 * <p>
 * Note: The conversion rates are assumed to be accurate and up-to-date.
 */
public class ThreeWayCurrencyConversion {

    private final Map<Currency, Map<Currency, BigDecimal>> exchangeRates;

    public ThreeWayCurrencyConversion(
            Map<Currency, Map<Currency, BigDecimal>> exchangeRates) {
        this.exchangeRates = new HashMap<>(exchangeRates);
    }
    private static final Logger LOG = Logger.getLogger(ThreeWayCurrencyConversion.class.getName());

    public BigDecimal convert(Currency fromCurrency, Currency toCurrency,
            BigDecimal amount) {

        // If the source and target currencies are the same, return the original amount
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        // Get the conversion rate from the exchange rate map
        BigDecimal conversionRate = exchangeRates.get(fromCurrency).get(toCurrency);

        // If there is no direct conversion rate, use a fallback rate involving an intermediate currency
        if (conversionRate == null) {
            // Get the rates for the source currency
            Map<Currency, BigDecimal> fromCurrencyRates = exchangeRates.get(fromCurrency);

            LOG.log(Level.INFO, "No direct conversion rate available for {0} -> {1}, using fallback rate",
                    new Object[]{fromCurrency, toCurrency});

            // Use the first available intermediate currency and rate as the fallback
            Map.Entry<Currency, BigDecimal> fallbackEntry = fromCurrencyRates.entrySet().stream().findFirst().orElseThrow();

            // Convert the amount to the intermediate currency using the fallback rate
            BigDecimal convertedAmount = fallbackEntry.getValue().multiply(amount);

            // Get the rates for the intermediate currency
            Map<Currency, BigDecimal> fallbackRates = exchangeRates.get(fallbackEntry.getKey());

            LOG.log(Level.INFO, "Fallback conversion rate used: {0} {1} = {2} {3}",
                    new Object[]{fromCurrency, amount, toCurrency, convertedAmount});

            // Calculate the final amount using the conversion rate from the intermediate currency to the target currency
            return fallbackRates.get(toCurrency).multiply(convertedAmount);
        }

        // If there is a direct conversion rate, use it to calculate the final amount
        BigDecimal result = amount.multiply(conversionRate);

        LOG.log(Level.INFO, "Conversion rate used: {0} {1} = {2} {3}",
                new Object[]{fromCurrency, amount, toCurrency, result});

        return result;
    }

    @Override
    public String toString() {
        return "ThreeWayCurrencyConversion{" + "rates=" + exchangeRates + '}';
    }

}
