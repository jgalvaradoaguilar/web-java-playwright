package com.revel.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.revel.playwright.locators.CarsLocators;

public class CarsPage {
    private final Page page;
    private final CarsLocators locators;

    public CarsPage(Page page) {
        this.page = page;
        this.locators = new CarsLocators(page);
    }

    public void clickFilterButton(String filterName) {
        switch (filterName) {
            case "Fuel" -> locators.fuelFilterButton.click();
            case "Body Type" -> locators.bodyTypeFilterButton.click();
            case "Color" -> locators.colorFilterButton.click();
            default -> throw new IllegalArgumentException("Filter name not recognized: " + filterName);
        }
    }

    public void selectFilterOption(String filterOption) {
        switch (filterOption) {
            case "Electric Hybrid" -> locators.fuelElectricHybrid.click();
            case "SUV" -> locators.bodyTypeSuv.click();
            case "White" -> locators.colorWhite.click();
            default -> throw new IllegalArgumentException(
                    "Filter option not recognized: " + filterOption);
        }
    }

    public void resetFilterButton(String filterName) {
        switch (filterName) {
            case "Fuel" -> locators.fuelFilterResetButton.click();
            case "Body Type" -> locators.bodyTypeFilterResetButton.click();
            case "Color" -> locators.colorFilterResetButton.click();
            default -> throw new IllegalArgumentException("Filter name not recognized: " + filterName);
        }
    }

    public boolean isPartOfUrl(String part) {
        return page.url().contains(part);
    }

    public void scrollToFirstCarCard() {
        locators.carsCards.first().scrollIntoViewIfNeeded();
    }

    public String getTextOfFirstCarCard() {
        return locators.carsCards.first().innerText();
    }

    public boolean isFirstCarCardTextContaining(String part) {
        return getTextOfFirstCarCard().contains(part);
    }

    public boolean isNoResultsMessageVisible() {
        locators.noResultsMessage.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return locators.noResultsMessage.isVisible();
    }
}
