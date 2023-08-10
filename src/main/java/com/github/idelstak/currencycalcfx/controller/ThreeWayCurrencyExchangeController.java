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
import com.github.idelstak.currencycalcfx.view.CurrencyExchangePane;
import com.github.idelstak.currencycalcfx.view.ThreeWayCurrencyExchangePane;
import java.util.Currency;
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;

public class ThreeWayCurrencyExchangeController {

    private final CurrencyExchangeController sourceCurrencyExchangeController;
    private final CurrencyExchangeController targetCurrencyExchangeController;
    private final ThreeWayCurrencyExchangePane threeWayCurrencyExchangePane;
    private final ComboBox<Currency> sourceQuoteCurrenciesComboBox;
    private final SelectionModel<Currency> sourceBaseCurrenciesSelectionModel;
    private final SelectionModel<Currency> sourceQuoteCurrenciesSelectionModel;
    private final Currencies targetCurrencies;
    private final ComboBox<Currency> targetBaseCurrenciesComboBox;
    private final CurrencyExchangePane targetCurrencyExchangePane;
    private final CurrencyExchangePane sourceCurrencyExchangePane;

    public ThreeWayCurrencyExchangeController(
            CurrencyExchangeController sourceCurrencyExchangeController,
            CurrencyExchangeController targetCurrencyExchangeController) {

        this.sourceCurrencyExchangeController = sourceCurrencyExchangeController;
        this.targetCurrencyExchangeController = targetCurrencyExchangeController;
        
        sourceCurrencyExchangePane = sourceCurrencyExchangeController.getView();
        targetCurrencyExchangePane = targetCurrencyExchangeController.getView();

        threeWayCurrencyExchangePane = new ThreeWayCurrencyExchangePane(
                sourceCurrencyExchangePane,
                targetCurrencyExchangePane
        );

        targetCurrencies = targetCurrencyExchangeController.getCurrencies();

        sourceBaseCurrenciesSelectionModel = threeWayCurrencyExchangePane
                .getSourceBaseCurrenciesComboBox()
                .getSelectionModel();
        sourceQuoteCurrenciesComboBox = threeWayCurrencyExchangePane.getSourceQuoteCurrenciesComboBox();
        sourceQuoteCurrenciesSelectionModel = sourceQuoteCurrenciesComboBox.getSelectionModel();
        targetBaseCurrenciesComboBox = threeWayCurrencyExchangePane.getTargetBaseCurrenciesComboBox();

        initBindings();
    }

    public CurrencyExchange getSourceCurrencyExchange() {
        return sourceCurrencyExchangeController.getCurrencyExchange();
    }

    public CurrencyExchangePane getSourceCurrencyExchangePane() {
        return sourceCurrencyExchangePane;
    }

    public CurrencyExchange getTargetCurrencyExchange() {
        return targetCurrencyExchangeController.getCurrencyExchange();
    }

    public CurrencyExchangePane getTargetCurrencyExchangePane() {
        return targetCurrencyExchangePane;
    }

    public Node getView() {
        return threeWayCurrencyExchangePane;
    }

    private void initBindings() {
        var targetBaseCurrenciesProperty = targetBaseCurrenciesComboBox.itemsProperty();

        targetBaseCurrenciesProperty.addListener(this::selectFirstTargetBaseCurrency);
        targetBaseCurrenciesProperty.bind(createTargetBaseCurrenciesBinding());

        sourceBaseCurrenciesSelectionModel
                .selectedItemProperty()
                .addListener(this::setFilteredTargetCurrencies);
    }

    private void setFilteredTargetCurrencies(
            ObservableValue<? extends Currency> observable,
            Currency oldValue, Currency newValue) {
        targetCurrencies.setAvailableCurrencies(createFilteredTargetCurrencies());
    }

    private FilteredList<Currency> createFilteredTargetCurrencies() {
        return new FilteredList<>(targetCurrencies.getAllCurrencies(), this::withoutSourceBaseCurrency);
    }

    private boolean withoutSourceBaseCurrency(Currency currency) {
        var sourceBaseCurrency = sourceBaseCurrenciesSelectionModel.getSelectedItem();
        return sourceBaseCurrency != null && !Objects.equals(currency.toString(), sourceBaseCurrency.toString());
    }

    private ObjectBinding<ObservableList<Currency>> createTargetBaseCurrenciesBinding() {
        return Bindings.createObjectBinding(
                this::createTargetBaseCurrenciesList,
                sourceQuoteCurrenciesSelectionModel.selectedItemProperty()
        );
    }

    private ObservableList<Currency> createTargetBaseCurrenciesList() {
        return FXCollections.observableArrayList(sourceQuoteCurrenciesSelectionModel.getSelectedItem());
    }

    private void selectFirstTargetBaseCurrency(Observable observable) {
        Platform.runLater(targetBaseCurrenciesComboBox.getSelectionModel()::selectFirst);
    }

}
