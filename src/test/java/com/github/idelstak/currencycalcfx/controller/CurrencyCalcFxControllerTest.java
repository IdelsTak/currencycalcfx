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
import com.github.idelstak.currencycalcfx.model.*;
import com.github.idelstak.currencycalcfx.model.currency.ExchangeRate;
import com.google.inject.Guice;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.testfx.util.WaitForAsyncUtils.sleep;

@ExtendWith(ApplicationExtension.class)
public class CurrencyCalcFxControllerTest {

    private CurrencyExchange targetCurrencyExchange;
    private CurrencyExchange sourceCurrencyExchange;
    private Bid bid;

    @Start
    public void setup(Stage stage) {
        var injector = Guice.createInjector(new CurrencyCalcFxModule());
        var currencyCalcFxController = injector.getInstance(CurrencyCalcFxController.class);
        
        bid = currencyCalcFxController.getBid();
        sourceCurrencyExchange = currencyCalcFxController.getSourceCurrencyExchange();
        targetCurrencyExchange = currencyCalcFxController.getTargetCurrencyExchange();

        stage.setScene(new Scene(currencyCalcFxController.getView()));
        stage.show();
    }

    @Test
    public void selectable_bid_currencies_should_be_set_from_selected_currencies_in_the_exchange_rates() {
        Currency usd = Currency.getInstance("USD");
        sourceCurrencyExchange.setBaseCurrency(usd);
        Currency kes = Currency.getInstance("KES");
        sourceCurrencyExchange.setQuoteCurrency(kes);
        targetCurrencyExchange.setBaseCurrency(kes);
        Currency ugx = Currency.getInstance("UGX");
        targetCurrencyExchange.setQuoteCurrency(ugx);

        sleep(1, TimeUnit.SECONDS);

        assertIterableEquals(List.of(usd, kes, ugx), bid.getAvailableCurrencies());
    }

    @Test
    public void selecting_another_bid_currency_should_update_the_bidAmount_according_to_exchangeRate() {
        sourceCurrencyExchange.setBaseCurrency(Currency.getInstance("USD"));
        sourceCurrencyExchange.setQuoteCurrency(Currency.getInstance("KES"));
        targetCurrencyExchange.setBaseCurrency(Currency.getInstance("KES"));
        targetCurrencyExchange.setQuoteCurrency(Currency.getInstance("UGX"));

        sleep(1, TimeUnit.SECONDS);

        sourceCurrencyExchange.setExchangeRate(new ExchangeRate(sourceCurrencyExchange.getBaseCurrency(), sourceCurrencyExchange.getQuoteCurrency(), new BigDecimal("130")));
        targetCurrencyExchange.setExchangeRate(new ExchangeRate(targetCurrencyExchange.getBaseCurrency(), targetCurrencyExchange.getQuoteCurrency(), new BigDecimal("26")));

        sleep(1, TimeUnit.SECONDS);

        bid.setBidAmount(new BigDecimal("100"));
        bid.setCurrency(Currency.getInstance("KES"));

        sleep(1, TimeUnit.SECONDS);

        assertThat(bid.getBidAmount(), equalTo(new BigDecimal("13000").setScale(2, RoundingMode.HALF_UP)));
    }

}
