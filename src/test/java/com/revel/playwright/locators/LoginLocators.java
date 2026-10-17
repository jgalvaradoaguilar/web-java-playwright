package com.revel.playwright.locators;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginLocators {
    public final Locator phoneInput;
    public final Locator continueButton;
    public final Locator skipButton;
    public final Locator otpInput;
    public final Locator loginSuccessfulImage;
    public final Locator loginFailedMessage;

    public LoginLocators(Page page) {
        this.phoneInput = page.locator("input[name=\"phone number\"]");
        this.continueButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continuar"));
        this.skipButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Skip"));
        this.otpInput = page.getByRole(AriaRole.TEXTBOX)
                .and(page.locator("[inputmode=\"numeric\"]"));
        this.loginSuccessfulImage = page.locator("img[alt=\"Login Successful\"]");
        this.loginFailedMessage = page.getByText("Este código no es válido");
    }
}
