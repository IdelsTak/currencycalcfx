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
package com.github.idelstak.currencycalcfx;

import com.github.idelstak.currencycalcfx.controller.CurrencyCalcFxController;
import com.github.idelstak.currencycalcfx.module.CurrencyCalcFxModule;
import com.google.inject.Guice;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the entry point for this application.
 * <p>
 * It creates a Guice {@code Injector} with the {@link CurrencyCalcFxModule}
 * that binds dependencies for the {@link CurrencyCalcFxController}, retrieves
 * the controller from the injector, and sets its view as the primary stage's
 * scene.
 */
public class CurrencyCalcFxApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create a new Guice injector with the CurrencyCalcFxModule 
        // to bind dependencies for the CurrencyCalcFxController
        var injector = Guice.createInjector(new CurrencyCalcFxModule());
        // Retrieve an instance of the CurrencyCalcFxController from the injector
        var currencyCalcFxController = injector.getInstance(CurrencyCalcFxController.class);

        // Set the view of the CurrencyCalcFxController as the scene 
        // for the primary stage
        primaryStage.setScene(new Scene(currencyCalcFxController.getView()));
        // Set the title of the primary stage
        primaryStage.setTitle("CurrencyCalcFX");
        // Disable the ability to resize the primary stage
        primaryStage.setResizable(false);
        // Display the primary stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Call the launch method to launch the JavaFX application, 
        // passing in any command-line arguments
        launch(args);
    }

}
