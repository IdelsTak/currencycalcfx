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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ThreeWayCurrencyExchangePane extends VBox {

    private final CurrencyExchangePane sourceCurrencyExchangePane;
    private final CurrencyExchangePane targetCurrencyExchangePane;

    public ThreeWayCurrencyExchangePane(
            CurrencyExchangePane sourceCurrencyExchangePane,
            CurrencyExchangePane targetCurrencyExchangePane) {
        
        super(24);
        
        this.sourceCurrencyExchangePane = sourceCurrencyExchangePane;
        this.targetCurrencyExchangePane = targetCurrencyExchangePane;
        
        getChildren().addAll(this.sourceCurrencyExchangePane, this.targetCurrencyExchangePane);
    }

    public ComboBox<Currency> getSourceBaseCurrenciesComboBox() {
        return sourceCurrencyExchangePane.getBaseCurrenciesComboBox();
    }

    public ComboBox<Currency> getSourceQuoteCurrenciesComboBox() {
        return sourceCurrencyExchangePane.getQuoteCurrenciesComboBox();
    }

    public TextField getSourceQuoteCurrencyRateTextField() {
        return sourceCurrencyExchangePane.getQuoteCurrencyRateTextField();
    }

    public ComboBox<Currency> getTargetBaseCurrenciesComboBox() {
        return targetCurrencyExchangePane.getBaseCurrenciesComboBox();
    }

    public ComboBox<Currency> getTargetQuoteCurrenciesComboBox() {
        return targetCurrencyExchangePane.getQuoteCurrenciesComboBox();
    }

    public TextField getTargetQuoteCurrencyRateTextField() {
        return targetCurrencyExchangePane.getQuoteCurrencyRateTextField();
    }

}
