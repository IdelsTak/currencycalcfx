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

import com.github.idelstak.currencycalcfx.module.CurrencyCalcFxModule;
import com.github.idelstak.currencycalcfx.view.CurrencyExchangePane;
import com.google.inject.Guice;
import java.util.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.testfx.matcher.control.ComboBoxMatchers.*;

@ExtendWith(ApplicationExtension.class)
public class ThreeWayCurrencyExchangeControllerTest {

    private CurrencyExchangePane sourceCurrencyExchangePane;
    private CurrencyExchangePane targetCurrencyExchangePane;

    @Start
    public void setup(Stage stage) {
        var injector = Guice.createInjector(new CurrencyCalcFxModule());
        var threeWayCurrencyExchangeController = injector.getInstance(ThreeWayCurrencyExchangeController.class);
        
        sourceCurrencyExchangePane = threeWayCurrencyExchangeController.getSourceCurrencyExchangePane();
        targetCurrencyExchangePane = threeWayCurrencyExchangeController.getTargetCurrencyExchangePane();

        stage.setScene(new Scene((Parent) threeWayCurrencyExchangeController.getView()));
        stage.show();
    }

    @Test
    public void selected_source_quote_currency_should_be_selected_as_target_base_currency(
            FxRobot robot) {
        var sourceQuoteCurrency = sourceCurrencyExchangePane
                .getQuoteCurrenciesComboBox()
                .getSelectionModel()
                .getSelectedItem();

        var targetBaseCurrenciesComboBox = targetCurrencyExchangePane
                .getBaseCurrenciesComboBox();

        verifyThat(targetBaseCurrenciesComboBox, hasSelectedItem(sourceQuoteCurrency));
    }

    @Test
    public void only_one_target_base_currency_option_should_be_available(
            FxRobot robot) {

        var targetBaseCurrenciesComboBox = targetCurrencyExchangePane
                .getBaseCurrenciesComboBox();

        verifyThat(targetBaseCurrenciesComboBox, hasItems(1));
    }

    @Test
    public void selected_source_base_currency_should_be_removed_from_target_quote_currencies(
            FxRobot robot) {

        var sourceBaseCurrency = sourceCurrencyExchangePane
                .getBaseCurrenciesComboBox()
                .getSelectionModel()
                .getSelectedItem();
        var targetQuoteCurrenciesComboBox = targetCurrencyExchangePane
                .getQuoteCurrenciesComboBox();
        var currencies = targetQuoteCurrenciesComboBox.getItems();

        if (currencies.isEmpty()) {
            fail("Target quote currencies should not be empty");
        }

        verifyThat(targetQuoteCurrenciesComboBox, containsExactlyItems(withoutCurrency(currencies, sourceBaseCurrency)));
    }

    private static Currency[] withoutCurrency(List<Currency> currencies,
            Currency currency) {
        return currencies
                .stream()
                .filter(c -> (currency != null && !Objects.equals(c.toString(), currency.toString())))
                .toArray(Currency[]::new);
    }

}
