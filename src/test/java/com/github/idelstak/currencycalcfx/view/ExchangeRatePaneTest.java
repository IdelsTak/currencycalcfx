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
import java.util.Locale;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class ExchangeRatePaneTest {

    private ExchangeRatePane exchangeRatePane;

    @Start
    public void setup(Stage stage) {
        exchangeRatePane = new ExchangeRatePane();

        stage.setScene(new Scene(exchangeRatePane));
        stage.show();
    }

    @Test
    public void currency_displayName_should_be_used_in_comboBox_items_text() {
        var currenciesComboBox = exchangeRatePane.getCurrenciesComboBox();
        var currency = Currency.getInstance(Locale.getDefault());
        var currencyText = currenciesComboBox.getConverter().toString(currency);

        assertThat(currencyText, equalTo(String.format("(%s) %s", currency.getCurrencyCode(), currency.getDisplayName())));
    }

}
