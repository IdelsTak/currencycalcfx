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
package com.github.idelstak.currencycalcfx.view;

import java.util.Currency;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class CurrencyCalcFxPane extends VBox {

    private final ThreeWayCurrencyExchangePane threeWayCurrencyExchangePane;
    private final BidPane bidPane;

    public CurrencyCalcFxPane(Node threeWayCurrencyExchangePane, Node bidPane) {
        super(24);

        this.threeWayCurrencyExchangePane = (ThreeWayCurrencyExchangePane) threeWayCurrencyExchangePane;
        this.bidPane = (BidPane) bidPane;

        setPadding(new Insets(24));

        getChildren().addAll(this.threeWayCurrencyExchangePane, this.bidPane);
    }

    public ComboBox<Currency> getSourceBaseCurrenciesComboBox() {
        return threeWayCurrencyExchangePane.getSourceBaseCurrenciesComboBox();
    }

    public ComboBox<Currency> getSourceQuoteCurrenciesComboBox() {
        return threeWayCurrencyExchangePane.getSourceQuoteCurrenciesComboBox();
    }

    public ComboBox<Currency> getTargetBaseCurrenciesComboBox() {
        return threeWayCurrencyExchangePane.getTargetBaseCurrenciesComboBox();
    }

    public ComboBox<Currency> getTargetQuoteCurrenciesComboBox() {
        return threeWayCurrencyExchangePane.getTargetQuoteCurrenciesComboBox();
    }

    public ComboBox<Currency> getCurrenciesComboBox() {
        return bidPane.getCurrenciesComboBox();
    }

}
