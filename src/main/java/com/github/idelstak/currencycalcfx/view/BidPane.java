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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BidPane extends VBox {

    private final ComboBox<Currency> currenciesComboBox;
    private final TextField commissionRateTextField;
    private final Label bidAmountLabel;
    private final TextField bidAmountTextField;
    private final Label youGetAmountlabel;
    private final TextField youGetAmountTextField;

    public BidPane() {
        super(6);

        var currencyLabel = new RightAlignedLabel("Use Currency");

        currenciesComboBox = new ComboBox<>();
        currenciesComboBox.setPrefWidth(100);

        var commissionRateLabel = new RightAlignedLabel("Commission (%)");
        commissionRateTextField = new DecimalTextField();
        bidAmountLabel = new RightAlignedLabel("Bid (USD)");
        bidAmountTextField = new DecimalTextField();
        youGetAmountlabel = new RightAlignedLabel("You Get (USD)");
        youGetAmountTextField = new DecimalTextField();

        var currencyBox = new HBox(6, currencyLabel, currenciesComboBox);
        var commissionRateBox = new HBox(6, commissionRateLabel, commissionRateTextField);
        var bidAmountBox = new HBox(6, bidAmountLabel, bidAmountTextField);
        var youGetAmountBox = new HBox(6, youGetAmountlabel, youGetAmountTextField);

        getChildren().addAll(currencyBox, commissionRateBox, bidAmountBox, youGetAmountBox);
    }

    public ComboBox<Currency> getCurrenciesComboBox() {
        return currenciesComboBox;
    }

    public TextField getCommissionRateTextField() {
        return commissionRateTextField;
    }

    public Label getBidAmountLabel() {
        return bidAmountLabel;
    }

    public TextField getBidAmountTextField() {
        return bidAmountTextField;
    }

    public Label getYouGetAmountlabel() {
        return youGetAmountlabel;
    }

    public TextField getYouGetAmountTextField() {
        return youGetAmountTextField;
    }

    private static class RightAlignedLabel extends Label {

        private RightAlignedLabel(String string) {
            super(string);

            setAlignment(Pos.CENTER_RIGHT);
            setMaxWidth(Double.MAX_VALUE);
            setMaxHeight(Double.MAX_VALUE);
            HBox.setHgrow(this, Priority.ALWAYS);
        }

    }

    private static class DecimalTextField extends TextField {

        private DecimalTextField() {
            setAlignment(Pos.CENTER_RIGHT);
            setPrefWidth(100);
        }

    }

}
