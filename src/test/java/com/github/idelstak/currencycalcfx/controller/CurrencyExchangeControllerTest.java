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
import static com.github.idelstak.currencycalcfx.matchers.CustomMatchers.editable;
import com.github.idelstak.currencycalcfx.model.Currencies;
import com.github.idelstak.currencycalcfx.model.CurrencyExchange;
import com.github.idelstak.currencycalcfx.view.CurrencyExchangePane;
import com.google.inject.Guice;
import java.util.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.testfx.matcher.control.ComboBoxMatchers.*;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
public class CurrencyExchangeControllerTest {

    private Currencies currencies;
    private CurrencyExchange currencyExchange;
    private CurrencyExchangePane currencyExchangePane;

    @Start
    public void setup(Stage stage) {
        var injector = Guice.createInjector(new CurrencyCalcFxModule());
        var currencyExchangeController = injector.getInstance(CurrencyExchangeController.class);
        
        currencies = currencyExchangeController.getCurrencies();
        currencyExchange = currencyExchangeController.getCurrencyExchange();
        currencyExchangePane = currencyExchangeController.getView();

        stage.setScene(new Scene(currencyExchangeController.getView()));
        stage.show();
    }

    @Test
    public void baseCurrenciesComboBox_should_contain_all_available_currencies(
            FxRobot robot) {

        var items = currencies.getAllCurrencies().stream().toArray(Currency[]::new);
        verifyThat(currencyExchangePane.getBaseCurrenciesComboBox(), containsExactlyItems(items));
    }

    @Test
    public void base_currencies_should_be_sorted_alphabetically() {
        var items = currencies.getAllCurrencies().stream().sorted(Comparator.comparing(Currency::getDisplayName)).toArray(Currency[]::new);
        verifyThat(currencyExchangePane.getBaseCurrenciesComboBox(), containsExactlyItemsInOrder(items));
    }

    @Test
    public void the_locale_default_currency_should_be_selected_in_baseCurrenciesComboBox(
            FxRobot robot) {

        var defaultCurrency = currencies.getDefaultCurrency();
        verifyThat(currencyExchangePane.getBaseCurrenciesComboBox(), hasSelectedItem(defaultCurrency));
    }

    @Test
    public void quote_currencies_should_be_sorted_alphabetically() {
        var items = Arrays.stream(getFilteredQuoteCurrencies()).sorted(Comparator.comparing(Currency::getDisplayName)).toArray(Currency[]::new);
        verifyThat(currencyExchangePane.getQuoteCurrenciesComboBox(), containsExactlyItemsInOrder(items));
    }

    @Test
    public void base_currency_selection_removes_matching_quote_currency_from_its_combo_box(
            FxRobot robot) {
        // Given the base currencies combo box
        var baseCurrenciesComboBox = currencyExchangePane.getBaseCurrenciesComboBox();

        // When a base currency is selected
        RobotActions.selectNextComboBoxItem(robot, baseCurrenciesComboBox);

        // Then the quote currencies combo box should have all currencies 
        // except the selected base currency
        verifyThat(
                currencyExchangePane.getQuoteCurrenciesComboBox(),
                containsExactlyItems(getFilteredQuoteCurrencies())
        );
    }

    @Test
    public void baseCurrencyTextField_should_be_uneditable(FxRobot robot) {
        verifyThat(currencyExchangePane.getBaseCurrencyRateTextField(), not(editable()));
    }

    @Test
    public void baseCurrencyTextField_should_have_predetermined_formatted_text(
            FxRobot robot) {
        verifyThat(currencyExchangePane.getBaseCurrencyRateTextField(), hasText("1.00"));
    }

    @Test
    public void on_start_quoteCurrencyTextField_should_have_predetermined_formatted_text(
            FxRobot robot) {
        verifyThat(currencyExchangePane.getQuoteCurrencyRateTextField(), hasText("1.00"));
    }

    @Test
    public void quoteCurrencyTextField_text_should_not_change_when_a_non_numeric_input_is_entered(
            FxRobot robot) {

        var quoteCurrencyRateTextField = currencyExchangePane.getQuoteCurrencyRateTextField();
        var oldText = quoteCurrencyRateTextField.getText();

        RobotActions.writeText(robot, quoteCurrencyRateTextField, "abcd");

        verifyThat(quoteCurrencyRateTextField, hasText(oldText));
    }

    @Test
    public void quoteCurrencyTextField_should_only_accept_currency_like_number_inputs(
            FxRobot robot) {

        var quoteCurrencyRateTextField = currencyExchangePane.getQuoteCurrencyRateTextField();

        RobotActions.writeText(robot, quoteCurrencyRateTextField, "48.53");

        verifyThat(quoteCurrencyRateTextField, hasText("48.53"));
    }

    @Test
    public void selecting_base_currency_should_update_the_exchange_rate(
            FxRobot robot) {
        var baseCurrenciesComboBox = currencyExchangePane.getBaseCurrenciesComboBox();
        var oldRate = currencyExchange.getExchangeRate();

        RobotActions.selectNextComboBoxItem(robot, baseCurrenciesComboBox);

        assertThat(oldRate, not(currencyExchange.getExchangeRate()));
    }

    @Test
    public void selecting_quote_currency_should_update_the_exchange_rate(
            FxRobot robot) {
        var quoteCurrenciesComboBox = currencyExchangePane.getQuoteCurrenciesComboBox();
        var oldRate = currencyExchange.getExchangeRate();

        RobotActions.selectNextComboBoxItem(robot, quoteCurrenciesComboBox);

        assertThat(oldRate, not(currencyExchange.getExchangeRate()));
    }

    @Test
    public void typing_quote_currency_rate_should_update_the_exchange_rate(
            FxRobot robot) {
        var quoteCurrencyTextField = currencyExchangePane.getQuoteCurrencyRateTextField();
        var oldRate = currencyExchange.getExchangeRate();

        RobotActions.writeText(robot, quoteCurrencyTextField, "15");

        assertThat(oldRate, not(currencyExchange.getExchangeRate()));
    }

    private Currency[] getFilteredQuoteCurrencies() {
        return currencyExchangePane
                .getQuoteCurrenciesComboBox()
                .getItems()
                .stream()
                .filter(this::selectedBaseCurrency)
                .toArray(Currency[]::new);
    }

    private boolean selectedBaseCurrency(Currency currency) {
        var selectedBaseCurrency = currencyExchangePane
                .getBaseCurrenciesComboBox()
                .getSelectionModel()
                .getSelectedItem();
        return selectedBaseCurrency != null
                && !selectedBaseCurrency.toString().equals(currency.toString());
    }
}
