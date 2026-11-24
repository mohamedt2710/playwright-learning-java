package com.mohamed.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Second batch of Playwright tests: real user flows on Swag Labs
 * (cart, sorting, checkout, logout, locked user).
 * Each test starts from a fresh browser page on the login screen.
 */
public class SwagLabsFlowTest {

    private static final String BASE_URL = "https://www.saucedemo.com";

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        page = browser.newPage();
        page.navigate(BASE_URL);
    }

    @AfterEach
    public void tearDown() {
        browser.close();
        playwright.close();
    }

    private void login(String user, String password) {
        page.locator("#user-name").fill(user);
        page.locator("#password").fill(password);
        page.locator("#login-button").click();
    }

    private void loginAsStandardUser() {
        login("standard_user", "secret_sauce");
        page.locator(".inventory_list").waitFor();
    }

    @Test
    public void lockedOutUserSeesError() {
        login("locked_out_user", "secret_sauce");
        String error = page.locator("[data-test='error']").textContent();
        assertTrue(error.contains("locked out"), "Expected a locked out message, got: " + error);
    }

    @Test
    public void emptyUsernameShowsRequiredError() {
        page.locator("#login-button").click();
        String error = page.locator("[data-test='error']").textContent();
        assertTrue(error.contains("Username is required"), "Got: " + error);
    }

    @Test
    public void productsPageShowsSixItems() {
        loginAsStandardUser();
        assertEquals(6, page.locator(".inventory_item").count());
    }

    @Test
    public void addingItemUpdatesCartBadge() {
        loginAsStandardUser();
        page.locator("#add-to-cart-sauce-labs-backpack").click();
        assertEquals("1", page.locator(".shopping_cart_badge").textContent());
        page.locator("#add-to-cart-sauce-labs-bike-light").click();
        assertEquals("2", page.locator(".shopping_cart_badge").textContent());
    }

    @Test
    public void removingItemFromCartClearsBadge() {
        loginAsStandardUser();
        page.locator("#add-to-cart-sauce-labs-backpack").click();
        page.locator("#remove-sauce-labs-backpack").click();
        assertEquals(0, page.locator(".shopping_cart_badge").count());
    }

    @Test
    public void cartPageListsTheAddedItem() {
        loginAsStandardUser();
        page.locator("#add-to-cart-sauce-labs-backpack").click();
        page.locator(".shopping_cart_link").click();
        assertTrue(page.url().contains("cart"));
        assertEquals("Sauce Labs Backpack", page.locator(".inventory_item_name").textContent());
    }

    @Test
    public void sortByPriceLowToHighOrdersPrices() {
        loginAsStandardUser();
        page.locator("[data-test='product-sort-container']").selectOption("lohi");
        List<Double> prices = new ArrayList<>();
        for (String text : page.locator(".inventory_item_price").allTextContents()) {
            prices.add(Double.parseDouble(text.replace("$", "")));
        }
        for (int i = 1; i < prices.size(); i++) {
            assertTrue(prices.get(i - 1) <= prices.get(i), "Prices are not ascending: " + prices);
        }
    }

    @Test
    public void checkoutWithoutFirstNameShowsError() {
        loginAsStandardUser();
        page.locator("#add-to-cart-sauce-labs-backpack").click();
        page.locator(".shopping_cart_link").click();
        page.locator("#checkout").click();
        page.locator("#continue").click();
        String error = page.locator("[data-test='error']").textContent();
        assertTrue(error.contains("First Name is required"), "Got: " + error);
    }

    @Test
    public void fullCheckoutCompletesOrder() {
        loginAsStandardUser();
        page.locator("#add-to-cart-sauce-labs-backpack").click();
        page.locator(".shopping_cart_link").click();
        page.locator("#checkout").click();
        page.locator("#first-name").fill("Test");
        page.locator("#last-name").fill("User");
        page.locator("#postal-code").fill("12345");
        page.locator("#continue").click();
        page.locator("#finish").click();
        assertEquals("Thank you for your order!", page.locator(".complete-header").textContent());
    }

    @Test
    public void logoutReturnsToLoginPage() {
        loginAsStandardUser();
        page.locator("#react-burger-menu-btn").click();
        page.locator("#logout_sidebar_link").click();
        assertTrue(page.locator("#login-button").isVisible());
    }
}
