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
package com.github.idelstak.currencycalcfx.module;

import com.github.idelstak.currencycalcfx.controller.*;
import com.github.idelstak.currencycalcfx.model.*;
import com.github.idelstak.currencycalcfx.view.*;
import com.google.inject.AbstractModule;

/**
 * This is a Guice module implementation that provides a way to configure and
 * bind dependencies for the application. It extends the Guice
 * {@link AbstractModule} class, which provides a convenient base class for
 * implementing Guice modules.
 * <p>
 * The {@link #configure()} method is overridden to define the bindings for the
 * different components of the application. Each bind method call maps a type to
 * a provider, which can be used to create instances of the corresponding class
 * when needed. The bindings defined in this module include:
 * <ul>
 * <li> {@link Currencies} bound to {@link CurrenciesProvider}
 * <li> {@link CurrencyExchange} bound to {@link CurrencyExchangeProvider}
 * <li> ExchangeRatePane bound to ExchangeRatePaneProvider
 * <li> CurrencyExchangePane bound to CurrencyExchangePaneProvider
 * <li> CurrencyExchangeController bound to CurrencyExchangeControllerProvider
 * <li> ThreeWayCurrencyExchangeController bound to
 * ThreeWayCurrencyExchangeControllerProvider
 * <li> BidController bound to an instance of BidController created with a Bid
 * object and a BidPane object
 * <li> CurrencyCalcFxController bound to CurrencyCalcFxControllerProvider
 * </ul>
 * By defining these bindings, this module specifies the dependencies of the
 * CurrencyCalcFX application and configures Guice to provide instances of these
 * dependencies when needed.
 */
public class CurrencyCalcFxModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Currencies.class).toProvider(CurrenciesProvider.class);
        bind(CurrencyExchange.class).toProvider(CurrencyExchangeProvider.class);
        bind(ExchangeRatePane.class).toProvider(ExchangeRatePaneProvider.class);
        bind(CurrencyExchangePane.class).toProvider(CurrencyExchangePaneProvider.class);
        bind(CurrencyExchangeController.class).toProvider(CurrencyExchangeControllerProvider.class);
        bind(ThreeWayCurrencyExchangeController.class).toProvider(ThreeWayCurrencyExchangeControllerProvider.class);
        bind(BidController.class).toInstance(new BidController(new Bid(), new BidPane()));
        bind(CurrencyCalcFxController.class).toProvider(CurrencyCalcFxControllerProvider.class);
    }

}
