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
package com.github.idelstak.currencycalcfx.controller;

import com.github.idelstak.currencycalcfx.model.Bid;
import com.github.idelstak.currencycalcfx.model.CurrencyExchange;
import com.github.idelstak.currencycalcfx.model.currency.ThreeWayCurrencyConversion;
import com.github.idelstak.currencycalcfx.view.CurrencyCalcFxPane;
import java.math.BigDecimal;
import java.util.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.Parent;

public class CurrencyCalcFxController {

    private final CurrencyExchange sourceCurrencyExchange;
    private final CurrencyExchange targetCurrencyExchange;
    private final CurrencyCalcFxPane currencyCalcFxPane;
    private final Bid bid;
    private ObjectBinding<List<Currency>> bidCurrenciesBinding;

    public CurrencyCalcFxController(
            ThreeWayCurrencyExchangeController threeWayCurrencyExchangeController,
            BidController bidController) {

        sourceCurrencyExchange = threeWayCurrencyExchangeController.getSourceCurrencyExchange();
        targetCurrencyExchange = threeWayCurrencyExchangeController.getTargetCurrencyExchange();
        bid = bidController.getBid();

        currencyCalcFxPane = new CurrencyCalcFxPane(threeWayCurrencyExchangeController.getView(), bidController.getView());
        currencyCalcFxPane.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());

        initBindings();
    }

    public Bid getBid() {
        return bid;
    }

    public CurrencyExchange getSourceCurrencyExchange() {
        return sourceCurrencyExchange;
    }

    public CurrencyExchange getTargetCurrencyExchange() {
        return targetCurrencyExchange;
    }

    public Parent getView() {
        return currencyCalcFxPane;
    }

    private void initBindings() {
        sourceCurrencyExchange.getBaseCurrencyProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> currencyCalcFxPane.getSourceBaseCurrenciesComboBox().getSelectionModel().select(newValue));
        });

        bidCurrenciesBinding = Bindings.createObjectBinding(() -> {
            var sourceBaseCurrency = sourceCurrencyExchange.getBaseCurrency();
            var targetBaseCurrency = targetCurrencyExchange.getBaseCurrency();
            var targetQuoteCurrency = targetCurrencyExchange.getQuoteCurrency();

            return (sourceBaseCurrency != null && targetBaseCurrency != null && targetQuoteCurrency != null)
                    ? List.of(sourceBaseCurrency, targetBaseCurrency, targetQuoteCurrency)
                    : bid.getAvailableCurrencies();
        },
                sourceCurrencyExchange.getBaseCurrencyProperty(),
                targetCurrencyExchange.getBaseCurrencyProperty(),
                targetCurrencyExchange.getQuoteCurrencyProperty());

        bidCurrenciesBinding.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> bid.setAvailableCurrencies(newValue));
        });

        threeWayCurrencyExchangeRateBinding = Bindings.createObjectBinding(() -> {
            var sourceExchangeRate = sourceCurrencyExchange.getExchangeRate();
            var targetExchangeRate = targetCurrencyExchange.getExchangeRate();
            var exchangeRates = new HashMap<Currency, Map<Currency, BigDecimal>>();

            if (sourceExchangeRate != null && targetExchangeRate != null) {
                var sourceBaseCurrency = sourceExchangeRate.baseCurrency();
                var sourceReverseBaseCurrency = sourceExchangeRate.getReverseRate().baseCurrency();
                var sourceQuoteCurrency = sourceExchangeRate.quoteCurrency();
                var sourceReverseQuoteCurrency = sourceExchangeRate.getReverseRate().quoteCurrency();
                var targetBaseCurrency = targetExchangeRate.baseCurrency();
                var targetReverseBaseCurrency = targetExchangeRate.getReverseRate().baseCurrency();
                var targetQuoteCurrency = targetExchangeRate.quoteCurrency();
                var targetReverseQuoteCurrency = targetExchangeRate.getReverseRate().quoteCurrency();

                if (sourceBaseCurrency != null && sourceQuoteCurrency != null && targetQuoteCurrency != null) {
                    exchangeRates.put(sourceBaseCurrency, new HashMap<>());
                    exchangeRates.put(sourceQuoteCurrency, new HashMap<>());
                    exchangeRates.put(targetQuoteCurrency, new HashMap<>());

                    exchangeRates.get(sourceBaseCurrency).put(sourceQuoteCurrency, sourceExchangeRate.rate());
                    exchangeRates.get(sourceReverseBaseCurrency).put(sourceReverseQuoteCurrency, sourceExchangeRate.getReverseRate().rate());
                    exchangeRates.getOrDefault(targetBaseCurrency, new HashMap<>()).put(targetQuoteCurrency, targetExchangeRate.rate());
                    exchangeRates.get(targetReverseBaseCurrency).put(targetReverseQuoteCurrency, targetExchangeRate.getReverseRate().rate());
                }
            }

            return new ThreeWayCurrencyConversion(exchangeRates);
        },
                sourceCurrencyExchange.getExchangeRateProperty(),
                targetCurrencyExchange.getExchangeRateProperty());

        threeWayCurrencyExchangeRateBinding.addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> bid.setThreeWayCurrencyConversion(newValue));
        });
    }
    private ObjectBinding<ThreeWayCurrencyConversion> threeWayCurrencyExchangeRateBinding;

}
