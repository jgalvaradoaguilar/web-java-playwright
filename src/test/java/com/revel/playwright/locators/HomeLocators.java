package com.revel.playwright.locators;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HomeLocators {
    public final Locator acceptCookiesButton;
    public final Locator loginLink;
    public final Locator ourCarsLink;

    public HomeLocators(Page page) {
        this.acceptCookiesButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Aceptar").setExact(true));
        this.loginLink = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Login"));
        this.ourCarsLink = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Nuestros coches"));
    }
}
