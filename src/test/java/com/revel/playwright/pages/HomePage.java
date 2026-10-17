package com.revel.playwright.pages;

import com.microsoft.playwright.Page;
import com.revel.playwright.config.ConfigManager;
import com.revel.playwright.locators.HomeLocators;

public class HomePage {
    private final Page page;
    private final HomeLocators locators;

    public HomePage(Page page) {
        this.page = page;
        this.locators = new HomeLocators(page);
    }

    public void navigate() {
        page.navigate(ConfigManager.baseUrl() + "/");
    }

    public void acceptCookies() {
        locators.acceptCookiesButton.click();
    }

    public void clickLogin() {
        locators.loginLink.click();
    }

    public void clickOurCars() {
        locators.ourCarsLink.click();
    }
}
