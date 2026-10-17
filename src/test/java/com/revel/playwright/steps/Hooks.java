package com.revel.playwright.steps;

import com.revel.playwright.support.BrowserSession;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before
    public void startBrowser() {
        BrowserSession.start();
    }

    @After
    public void closeBrowser() {
        BrowserSession.stop();
    }
}
