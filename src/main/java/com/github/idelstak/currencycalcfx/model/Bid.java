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
package com.github.idelstak.currencycalcfx.model;

import com.github.idelstak.currencycalcfx.model.currency.ThreeWayCurrencyConversion;
import java.math.BigDecimal;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import java.util.Currency;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents a bid made by a user in the currency exchange
 * application.
 * <p>
 * It holds the information related to the bid such as the available currencies,
 * the selected currency, commission rate, bid amount, youGet amount, and
 * three-way currency conversion.
 * <p>
 * It provides methods to calculate the bid amount and the amount received after
 * the commission is deducted.
 */
public class Bid {

    private final ObservableList<Currency> availableCurrencies;
    private final ObjectProperty<Currency> currencyProperty;
    private final ObjectProperty<BigDecimal> commissionRateProperty;
    private final ObjectProperty<BigDecimal> bidAmountProperty;
    private final ObjectProperty<BigDecimal> youGetAmountProperty;
    private final ObjectProperty<ThreeWayCurrencyConversion> threeWayCurrencyConversionProperty;

    public Bid() {
        availableCurrencies = FXCollections.observableArrayList();
        currencyProperty = new SimpleObjectProperty<>();
        commissionRateProperty = new SimpleObjectProperty<>();
        bidAmountProperty = new SimpleObjectProperty<>();
        youGetAmountProperty = new SimpleObjectProperty<>();
        threeWayCurrencyConversionProperty = new SimpleObjectProperty<>();
    }

    public ObservableList<Currency> getAvailableCurrencies() {
        return FXCollections.unmodifiableObservableList(availableCurrencies);
    }

    public void setAvailableCurrencies(List<Currency> currencies) {
        availableCurrencies.clear();
        availableCurrencies.setAll(currencies);
    }

    public Currency getCurrency() {
        return currencyProperty.get();
    }

    public void setCurrency(Currency currency) {
        currencyProperty.set(currency);
    }

    public BigDecimal getCommissionRate() {
        return commissionRateProperty.get();
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        commissionRateProperty.set(commissionRate);
    }

    public BigDecimal getBidAmount() {
        return bidAmountProperty.get();
    }

    public void setBidAmount(BigDecimal bidAmount) {
        bidAmountProperty.set(bidAmount);
    }

    public BigDecimal getYouGetAmount() {
        return youGetAmountProperty.get();
    }

    public void setYouGetAmount(BigDecimal youGetAmount) {
        youGetAmountProperty.set(youGetAmount);
    }

    public ObjectProperty<Currency> getCurrencyProperty() {
        return currencyProperty;
    }

    public ObjectProperty<BigDecimal> getCommissionRateProperty() {
        return commissionRateProperty;
    }

    public ObjectProperty<BigDecimal> getBidAmountProperty() {
        return bidAmountProperty;
    }

    public ObjectProperty<BigDecimal> getYouGetAmountProperty() {
        return youGetAmountProperty;
    }

    public ThreeWayCurrencyConversion getThreeWayCurrencyConversion() {
        return threeWayCurrencyConversionProperty.get();
    }

    public void setThreeWayCurrencyConversion(
            ThreeWayCurrencyConversion threeWayCurrencyConversion) {
        threeWayCurrencyConversionProperty.set(threeWayCurrencyConversion);
    }

    public ObjectProperty<ThreeWayCurrencyConversion> getThreeWayCurrencyConversionProperty() {
        return threeWayCurrencyConversionProperty;
    }

    /**
     * This method calculates the bid amount based on the commission rate and
     * the amount of currency that the user will receive after the exchange.
     * <p>
     * If the commission rate or the amount to receive is not set, it will be
     * assumed to be zero. The method calculates the commission fraction and
     * divides the amount to receive by one minus the commission fraction. The
     * result is then rounded to two decimal places using the HALF_UP rounding
     * mode and returned as a BigDecimal.
     *
     * @return a bid amount rounded to two decimal places using the HALF_UP
     * rounding mode and returned as a BigDecimal.
     *
     */
    public BigDecimal calculateBidAmount() {
        var commissionRate = getCommissionRate() != null
                ? getCommissionRate()
                : ZERO;
        var youGetAmount = getYouGetAmount() != null
                ? getYouGetAmount()
                : ZERO;
        var commissionFraction = commissionRate.divide(new BigDecimal("100.0"));

        return youGetAmount.divide(ONE.subtract(commissionFraction), 2, HALF_UP);
    }

    public BigDecimal calculateYouGetAmount() {
        var commissionRate = getCommissionRate() != null
                ? getCommissionRate()
                : ZERO;
        var bidAmount = getBidAmount() != null
                ? getBidAmount()
                : ZERO;
        var commissionFraction = commissionRate.divide(new BigDecimal("100"));

        return (ONE.subtract(commissionFraction)).multiply(bidAmount).setScale(2, HALF_UP);
    }

}
