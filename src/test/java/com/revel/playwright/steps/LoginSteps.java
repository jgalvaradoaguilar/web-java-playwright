package com.revel.playwright.steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.revel.playwright.config.ConfigManager;
import com.revel.playwright.pages.HomePage;
import com.revel.playwright.pages.LoginPage;
import com.revel.playwright.support.BrowserSession;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {
    private LoginPage loginPage;

    @Given("the user goes to the Revel login page")
    public void theUserGoesToTheRevelLoginPage() {
        HomePage homePage = new HomePage(BrowserSession.page());
        homePage.navigate();
        homePage.acceptCookies();
        homePage.clickLogin();
        loginPage = new LoginPage(BrowserSession.page());
    }

    @When("the user logs in with the configured phone")
    public void theUserLogsInWithTheConfiguredPhone() {
        loginPage.setPhone(ConfigManager.testPhone());
        loginPage.clickContinueButton();
    }

    @When("the user submits the configured valid OTP")
    public void theUserSubmitsTheConfiguredValidOtp() {
        loginPage.setOtp(ConfigManager.validOtp());
    }

    @When("the user submits a configured invalid OTP")
    public void theUserSubmitsAConfiguredInvalidOtp() {
        loginPage.setOtp(ConfigManager.invalidOtp());
    }

    @When("the user skips human verification")
    public void theUserSkipsHumanVerification() {
        loginPage.skipHumanVerification();
    }

    @Then("the user is logged successfully")
    public void theUserIsLoggedSuccessfully() {
        assertTrue(loginPage.isLoginSuccessfulImageVisible(),
                "The login success image should be visible.");
    }

    @Then("an error message is shown")
    public void anErrorMessageIsShown() {
        assertTrue(loginPage.isLoginErrorMessageVisible(),
                "The invalid OTP error message should be visible.");
    }
}
