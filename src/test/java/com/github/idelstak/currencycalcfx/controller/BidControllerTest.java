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
import com.github.idelstak.currencycalcfx.module.CurrencyCalcFxModule;
import com.github.idelstak.currencycalcfx.view.BidPane;
import com.google.inject.Guice;
import java.util.Currency;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.testfx.matcher.control.ComboBoxMatchers.containsExactlyItemsInOrder;
import static org.testfx.matcher.control.ComboBoxMatchers.hasSelectedItem;
import org.testfx.matcher.control.LabeledMatchers;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
public class BidControllerTest {

    private Bid bid;
    private BidPane bidPane;
    private BidController bidController;

    @Start
    public void setup(Stage stage) {
        var injector = Guice.createInjector(new CurrencyCalcFxModule());
        bidController = injector.getInstance(BidController.class);
        
        bid = bidController.getBid();
        bidPane = (BidPane) bidController.getView();

        stage.setScene(new Scene((Parent) bidController.getView()));
        stage.show();
    }

    @Test
    public void selectable_currencies_should_be_set_from_the_bid_model() {
        var usd = Currency.getInstance("USD");
        var kes = Currency.getInstance("KES");
        var ugx = Currency.getInstance("UGX");

        bid.setAvailableCurrencies(List.of(usd, kes, ugx));

        verifyThat(bidPane.getCurrenciesComboBox(), containsExactlyItemsInOrder(bid.getAvailableCurrencies().stream().toArray()));
    }

    @Test
    public void first_available_currency_should_be_selected_on_start() {
        var usd = Currency.getInstance("USD");
        var kes = Currency.getInstance("KES");
        var ugx = Currency.getInstance("UGX");

        bid.setAvailableCurrencies(List.of(usd, kes, ugx));

        verifyThat(bidPane.getCurrenciesComboBox(), hasSelectedItem(usd));
    }

    @Test
    public void selected_currency_should_set_the_bidAmount_currency_suffix(
            FxRobot robot) {
        var usd = Currency.getInstance("USD");
        var kes = Currency.getInstance("KES");
        var ugx = Currency.getInstance("UGX");

        bid.setAvailableCurrencies(List.of(usd, kes, ugx));

        RobotActions.selectNextComboBoxItem(robot, bidPane.getCurrenciesComboBox());

        WaitForAsyncUtils.waitForFxEvents();

        var selectedCurrencyText = bidPane
                .getCurrenciesComboBox()
                .getSelectionModel()
                .getSelectedItem()
                .toString();

        verifyThat(bidPane.getBidAmountLabel(), LabeledMatchers.hasText(String.format("Bid (%s)", selectedCurrencyText)));
    }

    @Test
    public void selected_currency_should_set_the_youGetAmount_currency_suffix(
            FxRobot robot) {
        var usd = Currency.getInstance("USD");
        var kes = Currency.getInstance("KES");
        var ugx = Currency.getInstance("UGX");

        bid.setAvailableCurrencies(List.of(usd, kes, ugx));

        RobotActions.selectNextComboBoxItem(robot, bidPane.getCurrenciesComboBox());

        WaitForAsyncUtils.waitForFxEvents();

        var selectedCurrencyText = bidPane
                .getCurrenciesComboBox()
                .getSelectionModel()
                .getSelectedItem()
                .toString();

        verifyThat(bidPane.getYouGetAmountlabel(), LabeledMatchers.hasText(String.format("You Get (%s)", selectedCurrencyText)));
    }

    @Test
    public void commissionRateTextField_should_have_default_value_on_start() {
        verifyThat(bidPane.getCommissionRateTextField(), hasText("0.00"));
    }

    @Test
    public void commissionRateTextField_should_not_accept_non_numeric_inputs(
            FxRobot robot) {
        var commissionRateTextField = bidPane.getCommissionRateTextField();
        var oldText = commissionRateTextField.getText();

        RobotActions.writeText(robot, commissionRateTextField, "abcd");

        verifyThat(commissionRateTextField, hasText(oldText));
    }

    @Test
    public void commissionRateTextField_should_accept_numeric_inputs(
            FxRobot robot) {
        var commissionRateTextField = bidPane.getCommissionRateTextField();

        RobotActions.writeText(robot, commissionRateTextField, "45.85");

        verifyThat(commissionRateTextField, hasText("45.85"));
    }

    @Test
    public void bidAmountTextField_should_have_default_value_on_start() {
        verifyThat(bidPane.getBidAmountTextField(), hasText("0.00"));
    }

    @Test
    public void bidAmountTextField_should_not_accept_non_numeric_inputs(
            FxRobot robot) {
        var bidAmountTextField = bidPane.getBidAmountTextField();
        var oldText = bidAmountTextField.getText();

        RobotActions.writeText(robot, bidAmountTextField, "abcd");

        verifyThat(bidAmountTextField, hasText(oldText));
    }

    @Test
    public void bidAmountTextField_should_accept_numeric_inputs(
            FxRobot robot) {
        var bidAmountTextField = bidPane.getBidAmountTextField();

        RobotActions.writeText(robot, bidAmountTextField, "45.85");

        verifyThat(bidAmountTextField, hasText("45.85"));
    }

    @Test
    public void youGetAmountTextField_should_have_default_value_on_start() {
        verifyThat(bidPane.getYouGetAmountTextField(), hasText("0.00"));
    }

    @Test
    public void youGetAmountTextField_should_not_accept_non_numeric_inputs(
            FxRobot robot) {
        var youGetAmountTextField = bidPane.getYouGetAmountTextField();
        var oldText = youGetAmountTextField.getText();

        RobotActions.writeText(robot, youGetAmountTextField, "abcd");

        verifyThat(youGetAmountTextField, hasText(oldText));
    }

    @Test
    public void youGetAmountTextField_should_accept_numeric_inputs(
            FxRobot robot) {
        var youGetAmountTextField = bidPane.getYouGetAmountTextField();

        RobotActions.writeText(robot, youGetAmountTextField, "45.85");

        verifyThat(youGetAmountTextField, hasText("45.85"));
    }

    @Test
    public void typing_the_commissionRate_and_bidAmount_should_calculate_the_youGetAmount(
            FxRobot robot) {
        var commissionRateTextField = bidPane.getCommissionRateTextField();

        RobotActions.writeText(robot, commissionRateTextField, "25");

        var bidAmountTextField = bidPane.getBidAmountTextField();

        RobotActions.writeText(robot, bidAmountTextField, "100");

        verifyThat(bidPane.getYouGetAmountTextField(), hasText("75.00"));
    }

    @Test
    public void typing_the_commissionRate_and_youGetAmount_should_calculate_the_bidAmount(
            FxRobot robot) {
        var commissionRateTextField = bidPane.getCommissionRateTextField();

        RobotActions.writeText(robot, commissionRateTextField, "25");

        var youGetAmountTextField = bidPane.getYouGetAmountTextField();

        RobotActions.writeText(robot, youGetAmountTextField, "75");

        verifyThat(bidPane.getBidAmountTextField(), hasText("100.00"));
    }

}
