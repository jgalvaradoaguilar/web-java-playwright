package com.revel.playwright.locators;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CarsLocators {
    public final Locator fuelFilterButton;
    public final Locator fuelElectricHybrid;
    public final Locator fuelFilterResetButton;
    public final Locator bodyTypeFilterButton;
    public final Locator bodyTypeSuv;
    public final Locator bodyTypeFilterResetButton;
    public final Locator colorFilterButton;
    public final Locator colorWhite;
    public final Locator colorFilterResetButton;
    public final Locator carsCards;
    public final Locator noResultsMessage;

    public CarsLocators(Page page) {
        this.fuelFilterButton = page.getByRole(AriaRole.PARAGRAPH)
                .filter(new Locator.FilterOptions().setHasText("Combustible"));
        this.fuelElectricHybrid = page.getByText("Híbrido enchufable").first();
        this.fuelFilterResetButton = resetButton(page, "Combustible");
        this.bodyTypeFilterButton = page.getByRole(AriaRole.PARAGRAPH)
                .filter(new Locator.FilterOptions().setHasText("Tipo de coche")).first();
        this.bodyTypeSuv = page.locator("[class*=\"BodyTypeFilterShortcut_button\"]")
                .filter(new Locator.FilterOptions().setHasText("SUV")).first();
        this.bodyTypeFilterResetButton = resetButton(page, "Tipo de coche");
        this.colorFilterButton = page.getByRole(AriaRole.PARAGRAPH)
                .filter(new Locator.FilterOptions().setHasText("Color"));
        this.colorWhite = page.getByText("Blanco");
        this.colorFilterResetButton = resetButton(page, "Color");
        this.carsCards = page.locator("[class*=\"CarCard_card__car--name\"]");
        this.noResultsMessage = page.getByText("¿No encuentras lo que buscas?");
    }

    private static Locator resetButton(Page page, String filterName) {
        return page.locator("div[class*=\"FilterShortcutButton_filter__button\"]")
                .filter(new Locator.FilterOptions().setHasText(filterName))
                .locator("div[class*=\"reset\"]");
    }
}
