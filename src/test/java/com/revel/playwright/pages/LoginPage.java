package com.revel.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.revel.playwright.locators.LoginLocators;

public class LoginPage {
    private final LoginLocators locators;

    public LoginPage(Page page) {
        this.locators = new LoginLocators(page);
    }

    public void setPhone(String telephone) {
        locators.phoneInput.fill(telephone);
    }

    public void clickContinueButton() {
        locators.continueButton.click();
    }

    public void skipHumanVerification() {
        locators.skipButton.click();
    }

    public void setOtp(String otp) {
        locators.otpInput.fill(otp);
    }

    public boolean isLoginSuccessfulImageVisible() {
        locators.loginSuccessfulImage.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return locators.loginSuccessfulImage.isVisible();
    }

    public boolean isLoginErrorMessageVisible() {
        locators.loginFailedMessage.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return locators.loginFailedMessage.isVisible();
    }
}
