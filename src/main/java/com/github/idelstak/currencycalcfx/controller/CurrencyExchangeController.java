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

import com.github.idelstak.currencycalcfx.model.Currencies;
import com.github.idelstak.currencycalcfx.model.CurrencyExchange;
import com.github.idelstak.currencycalcfx.model.currency.ExchangeRate;
import com.github.idelstak.currencycalcfx.view.CurrencyExchangePane;
import com.github.idelstak.currencycalcfx.view.formatter.CurrencyTextFormatter;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Currency;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;

public class CurrencyExchangeController {

    private final Currencies currencies;
    private final CurrencyExchange currencyExchange;
    private final CurrencyExchangePane currencyExchangePane;
    private final Currency defaultCurrency;
    private final ComboBox<Currency> baseCurrenciesComboBox;
    private final SelectionModel<Currency> baseCurrenciesSelectionModel;
    private final ReadOnlyObjectProperty<Currency> selectedBaseCurrencyProperty;
    private final ComboBox<Currency> quoteCurrenciesComboBox;
    private final SelectionModel<Currency> quoteCurrenciesSelectionModel;
    private final TextField baseCurrencyRateTextField;
    private final TextField quoteCurrencyRateTextField;

    public CurrencyExchangeController(Currencies currencies,
            CurrencyExchange currencyExchange,
            CurrencyExchangePane currencyExchangePane) {

        this.currencies = currencies;
        this.currencyExchange = currencyExchange;
        this.currencyExchangePane = currencyExchangePane;

        defaultCurrency = currencies.getDefaultCurrency();

        baseCurrenciesComboBox = this.currencyExchangePane.getBaseCurrenciesComboBox();
        baseCurrenciesSelectionModel = baseCurrenciesComboBox.getSelectionModel();
        selectedBaseCurrencyProperty = baseCurrenciesSelectionModel.selectedItemProperty();
        baseCurrencyRateTextField = this.currencyExchangePane.getBaseCurrencyRateTextField();

        quoteCurrenciesComboBox = this.currencyExchangePane.getQuoteCurrenciesComboBox();
        quoteCurrenciesSelectionModel = quoteCurrenciesComboBox.getSelectionModel();
        quoteCurrencyRateTextField = this.currencyExchangePane.getQuoteCurrencyRateTextField();

        initComponents();
        initBinding();
    }

    public CurrencyExchange getCurrencyExchange() {
        return currencyExchange;
    }

    public CurrencyExchangePane getView() {
        return currencyExchangePane;
    }

    public Currencies getCurrencies() {
        return currencies;
    }

    private void initComponents() {
        baseCurrencyRateTextField.setEditable(false);
        baseCurrencyRateTextField.setText("1.00");

        quoteCurrencyRateTextField.setTextFormatter(new CurrencyTextFormatter());
        quoteCurrencyRateTextField.setText("1.00");
    }

    private void initBinding() {
        baseCurrenciesComboBox.skinProperty().addListener(this::setDefaultCurrencySelection);
        baseCurrenciesComboBox.setItems(new SortedList<>(currencies.getAllCurrencies(), Comparator.comparing(Currency::getDisplayName)));
        selectedBaseCurrencyProperty.addListener(this::setExchangeRateFromSelectedBaseCurrency);
        selectedBaseCurrencyProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            Platform.runLater(() -> {
                currencyExchange.setBaseCurrency(newValue);
            });
        });

        quoteCurrenciesComboBox.itemsProperty().addListener(this::selectFirstQuoteCurrency);
        quoteCurrenciesComboBox.itemsProperty().bind(createQuoteCurrenciesBinding());
        quoteCurrenciesSelectionModel.selectedItemProperty().addListener(this::setExchangeRateFromSelectedQuoteCurrency);
        quoteCurrenciesSelectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            Platform.runLater(() -> {
                currencyExchange.setQuoteCurrency(newValue);
            });
        });

        quoteCurrencyRateTextField.textProperty().addListener(this::setExchangeRateFromQuoteRateText);
    }

    private void setExchangeRateFromSelectedBaseCurrency(
            ObservableValue<? extends Currency> observable, Currency oldValue,
            Currency newValue) throws NumberFormatException {
        var rate = new BigDecimal(Double.parseDouble(baseCurrencyRateTextField.getText()));
        var exchangeRate = new ExchangeRate(newValue, currencyExchange.getQuoteCurrency(), rate);

        if (newValue == null) {
            return;
        }

        Platform.runLater(() -> {
            currencyExchange.setExchangeRate(exchangeRate);
        });
    }

    private void setExchangeRateFromSelectedQuoteCurrency(
            ObservableValue<? extends Currency> observable, Currency oldValue,
            Currency newValue) throws NumberFormatException {
        var rate = new BigDecimal(Double.parseDouble(quoteCurrencyRateTextField.getText()));
        var exchangeRate = new ExchangeRate(currencyExchange.getBaseCurrency(), newValue, rate);

        if (newValue == null) {
            return;
        }

        Platform.runLater(() -> {
            currencyExchange.setExchangeRate(exchangeRate);
        });
    }

    private void setExchangeRateFromQuoteRateText(
            ObservableValue<? extends String> observable, String oldValue,
            String newValue) throws
            NumberFormatException {
        var rate = new BigDecimal(Double.parseDouble(newValue));
        var exchangeRate = new ExchangeRate(currencyExchange.getBaseCurrency(), currencyExchange.getQuoteCurrency(), rate);
        
        if (newValue == null) {
            return;
        }

        Platform.runLater(() -> currencyExchange.setExchangeRate(exchangeRate));
    }

    private ObjectBinding<ObservableList<Currency>> createQuoteCurrenciesBinding() {
        return Bindings.createObjectBinding(this::createSortedQuoteCurrenciesList, selectedBaseCurrencyProperty);
    }

    private ObservableList<Currency> createSortedQuoteCurrenciesList() {
        return new SortedList<>(createFilteredQuoteCurrenciesList(), Comparator.comparing(Currency::getDisplayName));
    }

    private ObservableList<Currency> createFilteredQuoteCurrenciesList() {
        return new FilteredList<>(currencies.getAvailableCurrencies(), this::withoutSelectedBaseCurrency);
    }

    private boolean withoutSelectedBaseCurrency(Currency currency) {
        var baseCurrency = baseCurrenciesSelectionModel.getSelectedItem();
        return baseCurrency != null && !baseCurrency.toString().equals(currency.toString());
    }

    private void setDefaultCurrencySelection(Observable observable) {
        Platform.runLater(this::selectDefaultCurrency);
    }

    private void selectDefaultCurrency() {
        baseCurrenciesSelectionModel.select(defaultCurrency);
    }

    private void selectFirstQuoteCurrency(Observable observable) {
        Platform.runLater(quoteCurrenciesSelectionModel::selectFirst);
    }

}
