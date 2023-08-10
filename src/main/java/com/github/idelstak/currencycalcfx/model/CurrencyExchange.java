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

import com.github.idelstak.currencycalcfx.model.currency.ExchangeRate;
import java.util.Currency;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CurrencyExchange {

    private final ObjectProperty<Currency> baseCurrencyProperty;
    private final ObjectProperty<Currency> quoteCurrencyProperty;
    private final ObjectProperty<ExchangeRate> exchangeRateProperty;

    public CurrencyExchange() {
        baseCurrencyProperty = new SimpleObjectProperty<>();
        quoteCurrencyProperty = new SimpleObjectProperty<>();
        exchangeRateProperty = new SimpleObjectProperty<>();
    }

    public Currency getBaseCurrency() {
        return baseCurrencyProperty.get();
    }

    public void setBaseCurrency(Currency baseCurrency) {
        baseCurrencyProperty.set(baseCurrency);
    }

    public Currency getQuoteCurrency() {
        return quoteCurrencyProperty.get();
    }

    public void setQuoteCurrency(Currency quoteCurrency) {
        quoteCurrencyProperty.set(quoteCurrency);
    }

    public ExchangeRate getExchangeRate() {
        return exchangeRateProperty.get();
    }

    public void setExchangeRate(ExchangeRate exchangeRate) {
        exchangeRateProperty.set(exchangeRate);
    }

    public ObjectProperty<Currency> getBaseCurrencyProperty() {
        return baseCurrencyProperty;
    }

    public ObjectProperty<Currency> getQuoteCurrencyProperty() {
        return quoteCurrencyProperty;
    }

    public ObjectProperty<ExchangeRate> getExchangeRateProperty() {
        return exchangeRateProperty;
    }

}
