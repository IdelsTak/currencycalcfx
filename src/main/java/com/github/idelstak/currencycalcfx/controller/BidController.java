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
import com.github.idelstak.currencycalcfx.view.BidPane;
import com.github.idelstak.currencycalcfx.view.formatter.CurrencyTextFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.Node;
import javafx.util.converter.BigDecimalStringConverter;

public class BidController {

    private final Bid bid;
    private final BidPane bidPane;
    private ObjectBinding<BigDecimal> youGetAmountBinding;

    public BidController(Bid bid, BidPane bidPane) {
        this.bid = bid;
        this.bidPane = bidPane;

        initBindings();
    }

    public Bid getBid() {
        return bid;
    }

    public Node getView() {
        return bidPane;
    }

    private void initBindings() {
        bidPane.getCurrenciesComboBox().itemsProperty().addListener(observable -> {
            Platform.runLater(() -> {
                bidPane.getCurrenciesComboBox().getSelectionModel().selectFirst();
                bid.setBidAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            });
        });
        bidPane.getCurrenciesComboBox().itemsProperty().bind(Bindings.createObjectBinding(
                bid::getAvailableCurrencies,
                bid.getAvailableCurrencies()));

        bidPane.getCommissionRateTextField().setTextFormatter(new CurrencyTextFormatter());
        bidPane.getCommissionRateTextField().textProperty().bindBidirectional(bid.getCommissionRateProperty(), new BigDecimalStringConverter());

        bidPane.getBidAmountLabel().textProperty().bind(Bindings.createStringBinding(() -> {
            var currency = bidPane.getCurrenciesComboBox().getSelectionModel().getSelectedItem();
            return currency != null
                    ? String.format("Bid (%s)", currency.toString()) : null;
        }, bidPane.getCurrenciesComboBox().getSelectionModel().selectedItemProperty()));

        bidPane.getBidAmountTextField().setTextFormatter(new CurrencyTextFormatter());
        bidPane.getBidAmountTextField().textProperty().bindBidirectional(bid.getBidAmountProperty(), new BigDecimalStringConverter());

        bidPane.getYouGetAmountlabel().textProperty().bind(Bindings.createStringBinding(() -> {
            var currency = bidPane.getCurrenciesComboBox().getSelectionModel().getSelectedItem();
            return currency != null
                    ? String.format("You Get (%s)", currency.toString()) : null;
        }, bidPane.getCurrenciesComboBox().getSelectionModel().selectedItemProperty()));

        bidPane.getYouGetAmountTextField().setTextFormatter(new CurrencyTextFormatter());
        bidPane.getYouGetAmountTextField().textProperty().bindBidirectional(bid.getYouGetAmountProperty(), new BigDecimalStringConverter());

        bidPane.getCurrenciesComboBox().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> bid.setCurrency(newValue));
        });

        bid.getCurrencyProperty().addListener((observable, oldValue, newValue) -> {
            var threeWayCurrencyConversion = bid.getThreeWayCurrencyConversion();
            
            if (oldValue != null && newValue != null && threeWayCurrencyConversion != null) {
                BigDecimal convertedBidAmount = threeWayCurrencyConversion.convert(oldValue, newValue, bid.getBidAmount());

                bid.setBidAmount(convertedBidAmount);
            }
        });

        youGetAmountBinding = Bindings.createObjectBinding(
                bid::calculateYouGetAmount,
                bid.getCommissionRateProperty(), bid.getBidAmountProperty());
        youGetAmountBinding.addListener((observable, oldValue, newValue) -> {
            bid.setYouGetAmount(newValue);
        });

        bidAmountBinding = Bindings.createObjectBinding(
                bid::calculateBidAmount,
                bid.getCommissionRateProperty(), bid.getYouGetAmountProperty());
        bidAmountBinding.addListener((observable, oldValue, newValue) -> {
            bid.setBidAmount(newValue);
        });

    }
    private ObjectBinding<BigDecimal> bidAmountBinding;

}
