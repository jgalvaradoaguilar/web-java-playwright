package com.revel.playwright.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.revel.playwright.pages.CarsPage;
import com.revel.playwright.pages.HomePage;
import com.revel.playwright.support.BrowserSession;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CarsSteps {
    private CarsPage carsPage;

    @Given("the user goes to the cars page on Revel")
    public void theUserGoesToTheCarsPageOnRevel() {
        HomePage homePage = new HomePage(BrowserSession.page());
        homePage.navigate();
        homePage.acceptCookies();
        homePage.clickOurCars();
        carsPage = new CarsPage(BrowserSession.page());
    }

    @When("the user clicks on filter {string}")
    public void theUserClicksOnFilter(String filter) {
        carsPage.clickFilterButton(filter);
    }

    @When("the user chooses the option {string}")
    public void theUserChoosesTheOption(String filterOption) {
        carsPage.selectFilterOption(filterOption);
    }

    @When("the user cleans the filter {string}")
    public void theUserCleansTheFilter(String filter) {
        carsPage.resetFilterButton(filter);
    }

    @Then("the filter option {string} appears in the URL")
    public void theFilterOptionAppearsInTheUrl(String filterOptionInUrl) {
        assertTrue(carsPage.isPartOfUrl(filterOptionInUrl),
                "Expected the URL to contain: " + filterOptionInUrl);
    }

    @Then("the first car in the results page is {string} {string}")
    public void theFirstCarInTheResultsPageIs(String firstCarModel, String firstCarVersion) {
        carsPage.scrollToFirstCarCard();
        assertTrue(carsPage.isFirstCarCardTextContaining(firstCarModel),
                "The first car should contain model: " + firstCarModel);
        assertTrue(carsPage.isFirstCarCardTextContaining(firstCarVersion),
                "The first car should contain version: " + firstCarVersion);
    }

    @Then("a message appears indicating that there are no results for the selected filters")
    public void aMessageAppearsIndicatingThatThereAreNoResults() {
        assertTrue(carsPage.isNoResultsMessageVisible(),
                "The no-results message should be visible.");
    }

    @Then("the URL does not contain any filter options")
    public void theUrlDoesNotContainAnyFilterOptions() {
        assertFalse(carsPage.isPartOfUrl("fuelTypes=electric-hybrid"));
        assertFalse(carsPage.isPartOfUrl("bodyType=suv"));
    }
}
