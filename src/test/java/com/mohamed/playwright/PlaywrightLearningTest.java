package com.mohamed.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * My first three Playwright tests.
 * I kept them small on purpose: this file is me learning the basics,
 * not a real test suite. Each test has comments explaining what I understood.
 */
public class PlaywrightLearningTest {

    // The demo site I practice on
    private static final String BASE_URL = "https://www.saucedemo.com";

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeEach
    public void setUp() {
        // Playwright starts its own driver in the background - no chromedriver to manage
        playwright = Playwright.create();

        // headless = no browser window pops up. Set to false to watch the test run.
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        // A Page is one browser tab. All actions go through it.
        page = browser.newPage();
        page.navigate(BASE_URL);
    }

    @AfterEach
    public void tearDown() {
        // Close everything so no browser processes stay open
        browser.close();
        playwright.close();
    }

    @Test
    public void pageTitleIsSwagLabs() {
        // The simplest possible check: does the login page even open?
        assertEquals("Swag Labs", page.title());
    }

    @Test
    public void validLoginReachesProducts() {
        // locator() finds an element; fill() types into it; click() clicks it.
        // Playwright waits for each element by itself before acting.
        page.locator("#user-name").fill("standard_user");
        page.locator("#password").fill("secret_sauce");
        page.locator("#login-button").click();

        assertTrue(page.url().contains("inventory"),
                "After login we should land on the products page");
        assertEquals("Products", page.locator(".title").textContent());
    }

    @Test
    public void wrongPasswordShowsError() {
        page.locator("#user-name").fill("standard_user");
        page.locator("#password").fill("this_is_wrong");
        page.locator("#login-button").click();

        // The red error box should appear and say the credentials do not match
        String error = page.locator("[data-test='error']").textContent();
        assertTrue(error.contains("do not match"), "Expected a credentials error, got: " + error);
    }
}
